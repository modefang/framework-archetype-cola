package ${package}.util;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * gson工具类
 */
public class GsonUtil {

    private GsonUtil() {
    }

    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                // 序列化
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) ->
                        new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern(FORMAT_DATE_TIME))))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) ->
                        new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern(FORMAT_DATE))))
                // 反序化
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(FORMAT_DATE_TIME)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, jsonDeserializationContext) ->
                        LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(FORMAT_DATE)))
                // gson在反序列化Map<String, Object>类型时，避免将int类型转换成double
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                }.getType(), new MapTypeAdapter())
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    /**
     * 对象转json字符串
     *
     * @param object 对象
     * @return json字符串
     */
    public static String objectToJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 对象转json字符串，根据key顺序排序
     *
     * @param object 对象
     * @return json字符串
     */
    public static String objectToJsonSortedByKey(Object object) {
        String json = objectToJson(object);
        // 转成SortedMap会根据key顺序排序
        SortedMap<String, Object> sortedObject = jsonToSortedMap(json);
        return objectToJson(sortedObject);
    }

    /**
     * json字符串转JsonObject对象
     *
     * @param json json字符串
     * @return JsonObject对象
     */
    public static JsonObject jsonToObject(String json) {
        return GSON.fromJson(json, JsonObject.class);
    }

    /**
     * json转对象
     *
     * @param json json字符串
     * @param type 类型
     * @return T对象
     */
    public static <T> T jsonToBean(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * json转Map<String, Object>
     *
     * @param json json字符串
     * @return Map<String, Object>对象
     */
    public static Map<String, Object> jsonToMap(String json) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return jsonToBean(json, type);
    }

    /**
     * json转SortedMap<String, Object>
     *
     * @param json json字符串
     * @return SortedMap<String, Object>对象
     */
    public static SortedMap<String, Object> jsonToSortedMap(String json) {
        Type type = new TypeToken<SortedMap<String, Object>>() {
        }.getType();
        return jsonToBean(json, type);
    }

    /**
     * json转Map<String, String>
     *
     * @param json json字符串
     * @return Map<String, String>对象
     */
    public static Map<String, String> jsonToMapString(String json) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return jsonToBean(json, type);
    }

    /**
     * json转List<Map<String, Object>>
     *
     * @param json json字符串
     * @return List<Map < String, Object>>对象
     */
    public static List<Map<String, Object>> jsonToListMap(String json) {
        Type type = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        return jsonToBean(json, type);
    }

    /**
     * json转List
     *
     * @param json json字符串
     * @return List<Object>>对象
     */
    public static List<Object> jsonToList(String json) {
        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return jsonToBean(json, type);
    }

    /**
     * json转List<clazz>
     *
     * @param json json字符串
     * @return List<T>>对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl<T>(clazz);
        return jsonToBean(json, type);
    }

    /**
     * 实现ParameterizedType接口
     * 设定这个实现类每次返回的类型都是List&lt;clazz&gt;
     * <br><br>
     * 如果Map<String, Integer>通过ParameterizedType来解析
     * getRawType()方法返回的值是：Map.class
     * getActualTypeArguments()方法返回的值是：[ String.class, Integer.class ]
     * 该类的实现规则是：getRawType<getActualTypeArguments>
     */
    private static class ParameterizedTypeImpl<T> implements ParameterizedType {

        Class<T> clazz;

        public ParameterizedTypeImpl(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

    }

    /**
     * gson在反序列化Map<String, Object>类型时，避免将int类型转换成double
     */
    private static class MapTypeAdapter extends TypeAdapter<Object> {

        private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            delegate.write(out, value);
        }

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    /*
                      改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        try {
                            return (int) lngNum;
                        } catch (Exception e) {
                            return lngNum;
                        }
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

    }

    /**
     * gson在反序列化Date类型时，兼容时间戳和时间字符串
     */
    private static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext context) throws JsonParseException {
            String value = jsonElement.getAsString();
            try {
                new BigDecimal(value);
            } catch (Exception e) {
                // 异常，说明包含非数字
                String dateStr = value.split("\\.")[0];
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return format.parse(dateStr);
                } catch (ParseException e1) {
                    return null;
                }
            }
            // 时间戳转换成date
            long time = jsonElement.getAsLong() * 1000;
            return new Date(time);
        }

    }

    /**
     * 获取json字符串的字符串值
     *
     * @param json json字符串
     * @param key  键值
     * @return 键值对应的字符串值
     */
    public static String getAsString(String json, String... key) {
        try {
            JsonElement jsonElement = jsonToBean(json, JsonElement.class);
            JsonObject jsonObject = jsonElement.isJsonArray() ?
                    jsonElement.getAsJsonArray().get(0).getAsJsonObject() : jsonElement.getAsJsonObject();
            return getAsString(jsonObject, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取JsonObject的字符串
     *
     * @param jsonObject JsonObject对象
     * @param key        键值
     * @return 键值对应的字符串值
     */
    public static String getAsString(JsonObject jsonObject, String... key) {
        String empty = "";
        if (jsonObject == null) {
            return empty;
        }

        JsonObject obj = jsonObject;
        for (String s : key) {
            JsonElement element = obj.get(s);
            if (element == null) {
                return empty;
            }
            if (element.isJsonPrimitive()) {
                return element.getAsString();
            }
            obj = element.getAsJsonObject();
        }
        return empty;
    }

    /**
     * 获取JsonObject的整数
     *
     * @param jsonObject JsonObject对象
     * @param key        键值
     * @return 键值对应的整数值
     */
    public static int getAsInt(JsonObject jsonObject, String... key) {
        if (jsonObject == null) {
            throw new IllegalArgumentException("jsonObject is null");
        }

        JsonObject obj = jsonObject;
        for (String s : key) {
            JsonElement element = obj.get(s);
            if (element == null) {
                throw new IllegalArgumentException("jsonObject is null");
            }
            if (element.isJsonPrimitive()) {
                return element.getAsInt();
            }
            obj = element.getAsJsonObject();
        }
        throw new IllegalArgumentException("jsonObject is null");
    }

}

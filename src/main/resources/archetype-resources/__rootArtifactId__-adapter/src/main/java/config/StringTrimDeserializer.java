package ${package}.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * String类型从json转成对象的时候，清除前后空字符
 */
@JacksonStdImpl
public final class StringTrimDeserializer extends StdScalarDeserializer<String> {

    public static final StringTrimDeserializer INSTANCE = new StringTrimDeserializer();

    public StringTrimDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return StringUtils.trim(p.getValueAsString());
    }

}

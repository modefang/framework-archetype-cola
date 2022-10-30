package ${package}.config;

import ${package}.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
public class RequestBodyReaderWrapper extends HttpServletRequestWrapper {

    private static final Set<String> NOT_ALLOWED_KEY_WORDS = new HashSet<>(0);
    private static final String NOT_ALLOWED_SQL_KEY_WORDS = "and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+";
    private static final String REPLACED_STRING = "INVALID";

    static {
        String[] keyStr = NOT_ALLOWED_SQL_KEY_WORDS.split("\\|");
        Collections.addAll(NOT_ALLOWED_KEY_WORDS, keyStr);
    }

    /**
     * 存储body数据的容器
     */
    private final byte[] body;

    public RequestBodyReaderWrapper(HttpServletRequest request) {
        super(request);

        // 将body数据存储起来
        String bodyStr = getBodyString(request);
        body = bodyStr.getBytes(Charset.defaultCharset());

        if (!"".equals(bodyStr)) {
            log.info("request body: {}", bodyStr);
        }
    }

    /**
     * 获取请求Body
     *
     * @param request request
     * @return String
     */
    public String getBodyString(final ServletRequest request) {
        try {
            return inputStream2String(request.getInputStream());
        } catch (IOException e) {
            log.error("获取请求Body异常", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求Body
     *
     * @return String
     */
    public String getBodyString() {
        final InputStream inputStream = new ByteArrayInputStream(body);
        return inputStream2String(inputStream);
    }

    /**
     * 获取请求Body
     *
     * @return String
     */
    public Map<String, Object> getBodyParams() {
        String bodyString = this.getBodyString();
        return GsonUtil.jsonToMap(bodyString);
    }

    /**
     * 获取URL请求参数
     *
     * @param excludeName 需要排除的字段名称
     * @return URL请求参数Map
     */
    public Map<String, Object> getUrlParams(String excludeName) {
        Enumeration<String> paramNames = this.getParameterNames();
        Map<String, Object> params = new HashMap<>(10);
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (excludeName.equals(paramName)) {
                continue;
            }

            Object paramValue = this.getParameter(paramName);
            params.put(paramName, paramValue);
        }
        return params;
    }

    /**
     * 获取请求的所有参数（url，body）
     *
     * @param excludeName 需要排除的字段名称
     * @return 请求的所有参数，url，body
     */
    public SortedMap<String, Object> getAllParams(String excludeName) {
        //获取URL上的参数
        Map<String, Object> urlParams = this.getUrlParams(excludeName);

        SortedMap<String, Object> allParams = new TreeMap<>(urlParams);
        // get请求不需要拿body参数
        if (!HttpMethod.GET.name().equals(this.getMethod())) {
            Map<String, Object> bodyParams = this.getBodyParams();
            allParams.putAll(bodyParams);
        }
        return allParams;
    }

    /**
     * 将inputStream里的数据读取出来并转换成字符串
     *
     * @param inputStream inputStream
     * @return String
     */
    private String inputStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("获取请求Body异常", e);
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("获取请求Body异常", e);
                }
            }
        }

        return this.cleanXssAndSqlAttack(sb.toString());
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    /**
     * 清除xss和sql注入攻击字符
     *
     * @param value 参数值
     * @return 过滤后的字符串
     */
    private String cleanXssAndSqlAttack(String value) {
        // You'll need to remove the spaces from the html entities below
        String cleanValue = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        cleanValue = cleanValue.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        cleanValue = cleanValue.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        cleanValue = cleanValue.replaceAll("'", "& #39;");
        cleanValue = cleanValue.replaceAll("eval\\((.*)\\)", "");
        cleanValue = cleanValue.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        cleanValue = cleanValue.replaceAll("script", "");
        cleanValue = cleanValue.replaceAll("jndi:", "");
        cleanValue = cleanValue.replaceAll("rmi:", "");
        cleanValue = cleanSqlKeyWords(cleanValue);
        return cleanValue;
    }

    /**
     * 清除sql注入攻击字符
     *
     * @param value 参数值
     * @return 过滤后的字符串
     */
    private String cleanSqlKeyWords(String value) {
        String cleanValue = value;
        for (String keyword : NOT_ALLOWED_KEY_WORDS) {
            if (cleanValue.length() > keyword.length() + 4
                    && (cleanValue.contains(" " + keyword) || cleanValue.contains(keyword + " ") || cleanValue.contains(" " + keyword + " "))) {
                cleanValue = StringUtils.replace(cleanValue, keyword, REPLACED_STRING);
                log.error("已被过滤，因为参数中包含不允许sql的关键词({}});参数：{};过滤后的参数：{}", keyword, value, cleanValue);
            }
        }
        return cleanValue;
    }

}

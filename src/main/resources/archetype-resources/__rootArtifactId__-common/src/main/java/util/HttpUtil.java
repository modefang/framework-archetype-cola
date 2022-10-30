package ${package}.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Map;

@Log4j2
public class HttpUtil {

    private static final RestTemplate REST_TEMPLATE;

    static {
        REST_TEMPLATE = new RestTemplate();
        // 设置字符串的转换使用UTF-8编码
        REST_TEMPLATE.getMessageConverters().stream()
                .filter(converter -> converter.getClass().equals(StringHttpMessageConverter.class))
                .forEach(converter -> ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8));
        // 设置请求超时时间
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);
        REST_TEMPLATE.setRequestFactory(requestFactory);
    }

    private HttpUtil() {
    }

    public static String get(String url) {
        return call(url, HttpMethod.GET, null, null, null, String.class);
    }

    public static String get(String url, Map<String, String> headers) {
        return call(url, HttpMethod.GET, headers, null, null, String.class);
    }

    public static <T> T get(String url, Class<T> clazz) {
        return call(url, HttpMethod.GET, null, null, null, clazz);
    }

    /**
     * 发送get请求并且不进行url编码
     *
     * @param url 地址
     * @return 接口返回的结果
     */
    public static String getWithoutEncode(String url) {
        return HttpUtil.getWithoutEncode(url, String.class);
    }

    /**
     * 发送get请求并且不进行url编码
     *
     * @param url          地址
     * @param responseType 返回值类型
     * @return 接口返回的结果
     */
    public static <T> T getWithoutEncode(String url, Class<T> responseType) {
        try {
            log.info("HttpUtil getWithoutEncode - url: {}", url);
            URI uri = new URI(url);
            T result = REST_TEMPLATE.getForObject(uri, responseType);

            // 判断是否为byte[]类型，byte[]类型不输出日志
            boolean isByteArray = "[B".equals(responseType.getName());
            log.info("HttpUtil getWithoutEncode - url: {}, result: {}", url, isByteArray ? "byte[]" : result);
            return result;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String post(String url) {
        return call(url, HttpMethod.POST, null, null, null, String.class);
    }

    public static String post(String url, Object body) {
        return call(url, HttpMethod.POST, null, MediaType.APPLICATION_JSON, body, String.class);
    }

    public static String post(String url, Object body, Map<String, String> headers) {
        return call(url, HttpMethod.POST, headers, MediaType.APPLICATION_JSON, body, String.class);
    }

    /**
     * 发送post请求并提交表单
     *
     * @param url  地址
     * @param form 表单
     * @return 接口返回的结果
     */
    public static String postForForm(String url, MultiValueMap<String, Object> form) {
        return call(url, HttpMethod.POST, null, MediaType.APPLICATION_FORM_URLENCODED, form, String.class);
    }

    /**
     * 发送http请求
     *
     * @param url          地址
     * @param method       请求方式 { GET, POST, ... }
     * @param headers      请求头
     * @param contentType  请求体的类型
     * @param body         请求体
     * @param responseType 返回值类型
     * @return 接口返回的结果
     */
    public static <T> T call(String url, HttpMethod method, Map<String, String> headers,
                             MediaType contentType, Object body, Class<T> responseType) {
        // 初始化headers
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.keySet().forEach(key -> httpHeaders.add(key, headers.get(key)));
        }

        // http请求携带的信息
        Object entityBody;
        HttpEntity<Object> entity;
        if (body instanceof MultipartFile) {
            entityBody = "MultipartFile";
            // 发送文件
            httpHeaders.set("Content-Type", contentType.toString()
                    + "; boundary=--------------------------" + System.currentTimeMillis());
            Resource resource = ((MultipartFile) body).getResource();
            LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("files", resource);
            entity = new HttpEntity<>(params, httpHeaders);
        } else {
            entityBody = body instanceof String ? body.toString() :
                    MediaType.APPLICATION_JSON.equals(contentType) ? GsonUtil.objectToJson(body) : body;
            httpHeaders.setContentType(contentType);
            entity = new HttpEntity<>(entityBody, httpHeaders);
        }

        // 向url发送method请求，请求携带信息entity，返回clazz类型的数据
        log.info("HttpUtil {} url: {}, header: {}, body: {}", method.toString(), url, headers, entityBody);
        ResponseEntity<T> response = REST_TEMPLATE.exchange(url, method, entity, responseType);
        T responseBody = response.getBody();

        // 判断是否为byte[]类型，byte[]类型不输出日志
        boolean isByteArray = "[B".equals(responseType.getName());
        log.info("HttpUtil {} url: {}, header: {}, body: {}, result: {}",
                method, url, headers, entityBody, isByteArray ? "byte[]" : responseBody);
        return responseBody;
    }

    /**
     * 获取HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }

    /**
     * 取得用户IP
     *
     * @return 当前请求的IP地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }

        String ipUnknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ipUnknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ipUnknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ipUnknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取请求里的header
     *
     * @param request 请求
     * @param key     键
     * @return 值
     */
    public static String getHeader(HttpServletRequest request, String key) {
        if (key != null && !"".equals(key)) {
            Enumeration<String> requestHeader = request.getHeaderNames();
            while (requestHeader.hasMoreElements()) {
                String headerKey = requestHeader.nextElement();
                if (key.equals(headerKey)) {
                    return request.getHeader(headerKey);
                }
            }
        }
        return null;
    }

    /**
     * 获取请求里的cookie
     *
     * @param request 请求
     * @param key     键
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 对字符串进行URLEncode编码
     *
     * @param str 字符串
     * @return 编码后的字符串
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 对字符串进行URLDecode解码
     *
     * @param str URLEncode编码的字符串
     * @return 解码后的字符串
     */
    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.error("URLDecode解码异常 str: {}", str, e);
            return "";
        }
    }

    /**
     * 对字符串进行base64编码
     *
     * @param str 字符串
     * @return 编码后的字符串
     */
    public static String base64Encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 对base64字符串进行解码
     *
     * @param str base64字符串
     * @return 解码后的字符串
     */
    public static String base64Decode(String str) {
        return new String(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)));
    }

}

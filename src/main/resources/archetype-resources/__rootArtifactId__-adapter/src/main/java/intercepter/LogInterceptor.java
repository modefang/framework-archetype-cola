package ${package}.intercepter;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    /**
     * 日志打印key：全链路跟踪id
     */
    private static final String LOG_KEY_TRACE_ID = "trace.id";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 过滤资源请求
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // 记录全链路跟踪id
        this.logTraceId(request);

        // 记录url
        String url = this.getRequestUrl(request);
        log.info("request url: {}", url);

        return true;
    }

    /**
     * 记录全链路跟踪id
     *
     * @param request 请求
     */
    private void logTraceId(HttpServletRequest request) {
        String traceId = MDC.get(LOG_KEY_TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            return;
        }

        traceId = request.getHeader(LOG_KEY_TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put(LOG_KEY_TRACE_ID, traceId);
    }

    /**
     * 获取请求的url
     *
     * @param request 请求
     * @return url
     */
    private String getRequestUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();

        // 获取域名
        url.append(request.getScheme()).append("://").append(request.getServerName());

        // 获取端口
        final List<Integer> port = Lists.newArrayList(80, 443);
        if (!port.contains(request.getServerPort())) {
            url.append(":").append(request.getServerPort());
        }

        // 获取路径
        url.append(request.getServletPath());

        // 获取参数
        if (request.getQueryString() != null) {
            url.append("?").append(request.getQueryString());
        }

        return url.toString();
    }

}

package ${package}.intercepter;

import ${package}.config.RequestBodyReaderWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(1)
@Component
public class RequestBodyReaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        // 过滤掉文件上传请求
        if (StringUtils.isNotBlank(request.getContentType())
                && request.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            filterChain.doFilter(request, response);
        } else {
            // 读取body里的参数
            ServletRequest requestBodyReaderWrapper = new RequestBodyReaderWrapper((HttpServletRequest) request);
            filterChain.doFilter(requestBodyReaderWrapper, response);
        }
    }

}

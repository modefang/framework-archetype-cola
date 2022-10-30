package ${package}.intercepter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final OptionsInterceptor optionsInterceptor;
    private final LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 按顺序从上到下执行拦截器
        this.addInterceptor(registry, this.optionsInterceptor);
        this.addInterceptor(registry, this.logInterceptor);
    }

    /**
     * 添加拦截器
     *
     * @param registry    拦截器注册器
     * @param interceptor 拦截器
     */
    private void addInterceptor(InterceptorRegistry registry, HandlerInterceptor interceptor) {
        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns("/error", "/doc/**", "/actuator/**");
    }

}

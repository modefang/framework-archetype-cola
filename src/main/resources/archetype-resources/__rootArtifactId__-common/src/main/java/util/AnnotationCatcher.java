package ${package}.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationCatcher {

    private AnnotationCatcher() {
    }

    /**
     * 获取方法或者类声明的注解，方法优先级高于类
     *
     * @param handler         拦截器的handler
     * @param annotationClass 注解类
     * @return 注解实例
     */
    public static <A extends Annotation> A catchHandlerAnnotation(Object handler, Class<A> annotationClass) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        return catchAnnotation(method, annotationClass);
    }

    /**
     * 获取方法或者类声明的注解，方法优先级高于类
     *
     * @param method          方法
     * @param annotationClass 注解类
     * @return 注解实例
     */
    public static <A extends Annotation> A catchAnnotation(Method method, Class<A> annotationClass) {
        A annotation = AnnotationUtils.getAnnotation(method, annotationClass);
        if (annotation == null) {
            annotation = AnnotationUtils.getAnnotation(method.getDeclaringClass(), annotationClass);
        }
        return annotation;
    }

}

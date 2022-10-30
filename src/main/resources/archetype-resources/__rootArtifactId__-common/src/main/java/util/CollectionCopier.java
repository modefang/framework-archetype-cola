package ${package}.util;

import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CollectionCopier {

    private CollectionCopier() {
    }

    /**
     * 将集合数据拷贝到新的目标类型集合
     *
     * @param sources 源数据
     * @param target  生成目标类型实例的方法
     * @param <S>     源类型
     * @param <T>     目标类型
     * @return 目标类型集合
     */
    public static <S, T> List<T> copy(Collection<S> sources, Supplier<T> target) {
        return copy(sources, target, null);
    }

    /**
     * 将集合数据拷贝到新的目标类型集合
     *
     * @param sources  源数据
     * @param target   生成目标类型实例的方法
     * @param callback 回调方法
     * @param <S>      源类型
     * @param <T>      目标类型
     * @return 目标类型集合
     */
    public static <S, T> List<T> copy(Collection<S> sources, Supplier<T> target, CopyCallback<S, T> callback) {
        if (sources == null) {
            return Collections.emptyList();
        }

        return sources.stream()
                .map(s -> {
                    T t = target.get();
                    BeanUtils.copyProperties(s, t);
                    if (callback != null) {
                        callback.callback(s, t);
                    }
                    return t;
                }).collect(Collectors.toList());
    }

    /**
     * 将集合数据拷贝到新的目标类型集合，回调携带索引
     *
     * @param sources  源数据
     * @param target   生成目标类型实例的方法
     * @param callback 回调方法
     * @param <S>      源类型
     * @param <T>      目标类型
     * @return 目标类型集合
     */
    public static <S, T> List<T> copyWithIndexCallback(List<S> sources, Supplier<T> target, CopyCallbackWithIndex<S, T> callback) {
        if (sources == null) {
            return Collections.emptyList();
        }

        return IntStream.range(0, sources.size())
                .mapToObj(i -> {
                    S s = sources.get(i);
                    T t = target.get();
                    BeanUtils.copyProperties(s, t);
                    if (callback != null) {
                        callback.callback(s, t, i);
                    }
                    return t;
                }).collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface CopyCallback<S, T> {

        /**
         * 回调函数，复制完成后执行
         *
         * @param s 源数据
         * @param t 生成目标类型实例的方法
         */
        void callback(S s, T t);

    }

    @FunctionalInterface
    public interface CopyCallbackWithIndex<S, T> {

        /**
         * 回调函数，复制完成后执行
         *
         * @param s 源数据
         * @param t 生成目标类型实例的方法
         * @param i 集合索引
         */
        void callback(S s, T t, int i);

    }

}

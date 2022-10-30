package ${package}.util;

import org.springframework.beans.BeanUtils;

import java.util.function.Supplier;

public class BeanCopier {

    private BeanCopier() {
    }

    /**
     * 将集合数据拷贝到新的目标类型集合
     *
     * @param source 源数据
     * @param target 生成目标类型实例的方法
     * @param <S>    源类型
     * @param <T>    目标类型
     * @return 目标类型集合
     */
    public static <S, T> T copy(S source, Supplier<T> target) {
        return copy(source, target, null);
    }

    /**
     * 将集合数据拷贝到新的目标类型集合
     *
     * @param source   源数据
     * @param target   生成目标类型实例的方法
     * @param callback 回调方法
     * @param <S>      源类型
     * @param <T>      目标类型
     * @return 目标类型集合
     */
    public static <S, T> T copy(S source, Supplier<T> target, CopyCallback<S, T> callback) {
        if (source == null) {
            return null;
        }

        T t = target.get();
        BeanUtils.copyProperties(source, t);
        if (callback != null) {
            callback.callback(source, t);
        }
        return t;
    }

    @FunctionalInterface
    public interface CopyCallback<S, T> {

        void callback(S s, T t);

    }

}

package ${package}.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

/**
 * 批处理工具
 */
public class BatchProcessor {

    private BatchProcessor() {
    }

    /**
     * 多线程并行处理数据，线程数等于核心数
     *
     * @param list     数据集合
     * @param consumer 处理逻辑
     * @param <T>      数据类型
     */
    public static <T> void execute(List<T> list, Consumer<T> consumer) {
        // Lists.partition分批拆分成多个子List，然后parallelStream多线程并行处理数据，线程数等于核心数
        Lists.partition(list, 1000).parallelStream().forEach(subList -> subList.forEach(consumer));
    }

    /**
     * 多线程并行处理数据，线程数等于核心数
     *
     * @param list          数据集合
     * @param partitionSize 每批处理的数据量
     * @param consumer      处理逻辑
     * @param <T>           数据类型
     */
    public static <T> void execute(List<T> list, int partitionSize, Consumer<T> consumer) {
        // Lists.partition分批拆分成多个子List，然后parallelStream多线程并行处理数据，线程数等于核心数
        Lists.partition(list, partitionSize).parallelStream().forEach(subList -> subList.forEach(consumer));
    }

}

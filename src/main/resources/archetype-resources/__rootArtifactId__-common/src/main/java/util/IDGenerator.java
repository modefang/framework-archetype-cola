package ${package}.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * ID生成器
 */
public class IDGenerator {

    private IDGenerator() {
    }

    /**
     * 基于时间生成：年月日时分秒毫秒 + 随机10位字符的hashcode取4位整数
     *
     * @return 时间id
     */
    public static String nextTimeId() {
        return nextTimeId(null);
    }

    /**
     * 基于时间生成：年月日时分秒毫秒 + 盐加上默认随机10位字符的hashcode取4位整数
     *
     * @param salt 盐
     * @return 时间id
     */
    public static String nextTimeId(String salt) {
        // 盐加上随机10位字符
        StringBuilder saltBuilder = new StringBuilder(StringUtils.defaultString(salt))
                .append(RandomStringUtils.randomAlphanumeric(10));
        // suffix 4位整数，不足补0
        String suffix = String.format("%04d", Math.abs(saltBuilder.hashCode() % 1000));
        return DateUtil.nowDateTime("yyyyMMddHHmmssSSS") + suffix;
    }

}

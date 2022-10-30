package ${package}.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 */
public class DateUtil {

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final String FORMAT_YEAR_MONTH = "yyyy-MM";

    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

    private DateUtil() {
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期，格式：yyyy-MM-dd
     */
    public static String nowDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(FORMAT_DATE));
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String nowDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT_DATE_TIME));
    }

    /**
     * 获取当前时间
     *
     * @param pattern 格式
     * @return 当前时间
     */
    public static String nowDateTime(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDate转String
     *
     * @param localDate 日期
     * @return 日期字符串
     */
    public static String toString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        return formatter.format(localDate);
    }

    /**
     * LocalDateTime转String
     *
     * @param localDateTime 日期
     * @return 日期字符串
     */
    public static String toString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);
        return formatter.format(localDateTime);
    }

    /**
     * String转LocalDateTime日期
     *
     * @param str 字符串，默认格式：yyyy-MM-dd HH:mm:ss
     * @return LocalDateTime日期
     */
    public static LocalDate toLocalDate(String str) {
        return toLocalDate(str, FORMAT_DATE);
    }

    /**
     * String转LocalDateTime日期
     *
     * @param str     字符串
     * @param pattern 格式
     * @return LocalDateTime日期
     */
    public static LocalDate toLocalDate(String str, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(str, formatter);
    }

    /**
     * String转LocalDate
     *
     * @param str 字符串，默认格式：yyyy-MM-dd
     * @return LocalDate
     */
    public static LocalDateTime toLocalDateTime(String str) {
        return toLocalDateTime(str, FORMAT_DATE_TIME);
    }

    /**
     * String转LocalDate
     *
     * @param str     字符串
     * @param pattern 格式
     * @return LocalDate
     */
    public static LocalDateTime toLocalDateTime(String str, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(str, formatter);
    }

    /**
     * LocalDateTime转时间戳（秒）
     *
     * @param localDateTime 时间
     * @return 时间戳（秒）
     */
    public static long toEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(DEFAULT_ZONE_OFFSET);
    }

    /**
     * 年月字符串 转 YearMonth
     *
     * @param yearMonth 年月字符串，格式：yyyy-MM
     * @return YearMonth
     */
    public static YearMonth toYearMonth(String yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH);
        return YearMonth.parse(yearMonth, formatter);
    }

    /**
     * 月份的第一天
     *
     * @param yearMonth 年月字符串，格式：yyyy-MM
     * @return 月份的第一天
     */
    public static String firstDateTimeOfMonth(String yearMonth) {
        YearMonth initial = toYearMonth(yearMonth);
        return firstDateTimeOfMonth(initial);
    }

    /**
     * 月份的第一天
     *
     * @param yearMonth 年月
     * @return 月份的第一天
     */
    public static String firstDateTimeOfMonth(YearMonth yearMonth) {
        LocalDate firstDate = yearMonth.atDay(1);
        LocalDateTime firstDateTime = LocalDateTime.of(firstDate, LocalTime.MIN);
        return toString(firstDateTime);
    }

    /**
     * 月份的最后一天
     *
     * @param yearMonth 年月字符串，格式：yyyy-MM
     * @return 月份的最后一天
     */
    public static String lastDateTimeOfMonth(String yearMonth) {
        YearMonth initial = toYearMonth(yearMonth);
        return lastDateTimeOfMonth(initial);
    }

    /**
     * 月份的最后一天
     *
     * @param yearMonth 年月
     * @return 月份的最后一天
     */
    public static String lastDateTimeOfMonth(YearMonth yearMonth) {
        LocalDate lastDate = yearMonth.atEndOfMonth();
        LocalDateTime lastDateTime = LocalDateTime.of(lastDate, LocalTime.MAX);
        return toString(lastDateTime);
    }

}

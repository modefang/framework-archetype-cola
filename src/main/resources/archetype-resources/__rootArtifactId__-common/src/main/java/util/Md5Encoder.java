package ${package}.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * MD5工具类
 */
public class Md5Encoder {

    private Md5Encoder() {
    }

    /**
     * 对字符串进行MD5加密，默认生成32位加密字符串
     *
     * @param origin 源字符串
     * @return 加密后的字符串
     */
    public static String encode(String origin) {
        return encode(origin, null, false);
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param origin        源字符串
     * @param isShortString 判断是否需要16位的加密字符，默认生成32位
     * @return 加密后的字符串
     */
    public static String encode(String origin, boolean isShortString) {
        return encode(origin, null, isShortString);
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param origin        源字符串
     * @param charsetName   字符集，默认UTF_8
     * @param isShortString 判断是否需要16位的加密字符，默认生成32位
     * @return 加密后的字符串
     */
    public static String encode(String origin, String charsetName, boolean isShortString) {
        charsetName = charsetName == null ? StandardCharsets.UTF_8.name() : charsetName;
        try {
            String encodeString = DigestUtils.md5DigestAsHex(origin.getBytes(charsetName));
            if (isShortString) {
                return encodeString.substring(8, 24);
            }
            return encodeString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}

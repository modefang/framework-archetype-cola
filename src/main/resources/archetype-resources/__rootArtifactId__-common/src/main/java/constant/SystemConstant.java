package ${package}.constant;

/**
 * 系统常量
 */
public class SystemConstant {

    private SystemConstant() {
    }

    /**
     * 系统用户
     */
    public static final String SYSTEM_USER = "SYSTEM";

    /**
     * 测试环境配置
     */
    public static final String PROFILE_TEST = "dev|test";

    /**
     * 业务代码
     */
    public static final String BIZ_CODE = "";

    /**
     * 业务名称
     */
    public static final String BIZ_NAME = "";

    public static boolean isTestProfile(String profileActive) {
        return SystemConstant.PROFILE_TEST.contains(profileActive);
    }

}

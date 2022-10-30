package ${package}.exception;

/**
 * 错误码主要有3部分组成：类型+场景+自定义标识
 */
public enum SysErrorCode {

    // 系统异常
    S_UNKNOWN_ERROR("S_UNKNOWN_ERROR", "系统异常，请刷新后重试"),
    S_PARAM_ERROR("S_PARAM_ERROR", "参数异常"),
    S_LOGIN_LoginError("S_LOGIN_LoginError", "登录失败，请刷新后重试"),
    S_LOCK_ERROR("S_LOCK_ERROR", "系统异常，请刷新后重试"),
    S_PAY_ERROR("S_PAY_ERROR", "支付异常，请稍后重试"),
    S_REFUND_ERROR("S_REFUND_ERROR", "退款异常，请稍后重试"),

    // 限流
    S_LIMIT_RequestLimit("S_LIMIT_RequestLimit", "操作太快了，请稍后重试"),

    // 验证码
    S_CAPTCHA_VerifyFailed("S_CAPTCHA_VerifyFailed", "验证码验证异常，请刷新后重试"),

    // 开放接口
    S_OPENAPI_VerifyFailed("S_OPENAPI_VerifyFailed", "签名校验失败"),
    S_OPENAPI_Replay("S_OPENAPI_Replay", "请勿重复请求"),
    S_OPENAPI_Expired("S_OPENAPI_Expired", "签名已过期"),
    ;

    private final String errCode;
    private final String errDesc;

    SysErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

}

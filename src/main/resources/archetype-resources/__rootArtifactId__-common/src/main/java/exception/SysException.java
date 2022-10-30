package ${package}.exception;

public class SysException extends RuntimeException {

    private static final String DEFAULT_ERR_CODE = "SYS_ERROR";

    private final String errCode;

    private final String errDesc;

    public SysException(String errMessage) {
        super(errMessage);
        this.errCode = DEFAULT_ERR_CODE;
        this.errDesc = errMessage;
    }

    public SysException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.errCode = DEFAULT_ERR_CODE;
        this.errDesc = errMessage;
    }

    public SysException(SysErrorCode error) {
        super(error.getErrDesc());
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public SysException(SysErrorCode error, Throwable e) {
        super(error.getErrDesc(), e);
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public SysException(SysErrorCode error, String errDetail) {
        super(error.getErrDesc() + "，" + errDetail);
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public SysException(SysErrorCode error, String errDetail, Throwable e) {
        super(error.getErrDesc() + "，" + errDetail, e);
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public static SysException of(String errMessage) {
        return new SysException(errMessage);
    }

    public static SysException of(String errMessage, Throwable e) {
        return new SysException(errMessage, e);
    }

    public static SysException of(SysErrorCode error) {
        return new SysException(error);
    }

    public static SysException of(SysErrorCode error, Throwable e) {
        return new SysException(error, e);
    }

    public static SysException of(SysErrorCode error, String errDetail) {
        return new SysException(error, errDetail);
    }

    public static SysException of(SysErrorCode error, String errDetail, Throwable e) {
        return new SysException(error, errDetail, e);
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

}

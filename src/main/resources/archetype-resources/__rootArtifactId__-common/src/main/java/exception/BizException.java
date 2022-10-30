package ${package}.exception;

public class BizException extends RuntimeException {

    private static final String DEFAULT_ERR_CODE = "BIZ_ERROR";

    private final String errCode;

    private final String errDesc;

    public BizException(String errMessage) {
        super(errMessage);
        this.errCode = DEFAULT_ERR_CODE;
        this.errDesc = errMessage;
    }

    public BizException(String errMessage, Throwable e) {
        super(errMessage, e);
        this.errCode = DEFAULT_ERR_CODE;
        this.errDesc = errMessage;
    }

    public BizException(BizErrorCode error) {
        super(error.getErrDesc());
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public BizException(BizErrorCode error, Throwable e) {
        super(error.getErrDesc(), e);
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public BizException(BizErrorCode error, String errDetail) {
        super(error.getErrDesc() + "，" + errDetail);
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public BizException(BizErrorCode error, String errDetail, Throwable e) {
        super(error.getErrDesc() + "，" + errDetail, e);
        this.errCode = error.getErrCode();
        this.errDesc = error.getErrDesc();
    }

    public static BizException of(String errMessage) {
        return new BizException(errMessage);
    }

    public static BizException of(String errMessage, Throwable e) {
        return new BizException(errMessage, e);
    }

    public static BizException of(BizErrorCode error) {
        return new BizException(error);
    }

    public static BizException of(BizErrorCode error, Throwable e) {
        return new BizException(error, e);
    }

    public static BizException of(BizErrorCode error, String errDetail) {
        return new BizException(error, errDetail);
    }

    public static BizException of(BizErrorCode error, String errDetail, Throwable e) {
        return new BizException(error, errDetail, e);
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

}

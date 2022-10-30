package ${package}.exception;

public class Response {

    private static final long serialVersionUID = 1L;
    /**
     * 成功标识
     */
    private boolean success;
    /**
     * 响应代码
     */
    private String errCode;
    /**
     * 响应消息
     */
    private String errMessage;

    public Response() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return this.errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    @Override
    public String toString() {
        return "Response [success=" + this.success + ", errCode=" + this.errCode + ", errMessage=" + this.errMessage + "]";
    }

    public static Response buildSuccess() {
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    public static Response buildFailure(String errCode, String errMessage) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

}

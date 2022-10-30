package ${package}.exception;

/**
 * 错误码主要有3部分组成：类型+场景+自定义标识
 */
public enum BizErrorCode {

    // 参数异常
//    P_USER_UserIdNotNull("P_USER_UserIdNotNull", "用户id不能为空"),

    // 业务异常 - 订单
    B_ORDER_OrderNotExists("B_ORDER_OrderNotExists", "订单不存在"),
    B_ORDER_OrderItemNotExists("B_ORDER_OrderItemNotExists", "子订单不存在"),
    B_ORDER_OrderStateIncorrect("B_ORDER_OrderStateIncorrect", "订单状态不正确"),
    B_ORDER_NotSupportPay("B_ORDER_NotSupportPay", "无法支付"),

    // 业务异常 - 交易
    B_TRANSACTION_TransactionNotExists("B_TRANSACTION_TransactionNotExists", "交易不存在"),

    // 业务异常 - 服务单
    B_REFUND_RefundNotExists("B_REFUND_RefundNotExists", "服务单不存在"),
    B_REFUND_NotSupportRefund("B_REFUND_NotSupportRefund", "订单无法退款"),
    B_REFUND_RefundStateIncorrect("B_REFUND_RefundStateIncorrect", "服务单状态不支持该操作"),
    B_SERVICE_OrderStateIncorrect("B_SERVICE_OrderStateIncorrect", "订单状态不支持创建服务单"),
    ;

    private final String errCode;
    private final String errDesc;

    BizErrorCode(String errCode, String errDesc) {
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

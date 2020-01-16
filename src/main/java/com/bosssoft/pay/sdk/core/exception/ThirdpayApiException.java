package com.bosssoft.pay.sdk.core.exception;

/**
 * @Title 接口处理异常
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpayApiException extends Exception {

    private String errCode;
    private String errMsg;

    public ThirdpayApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThirdpayApiException(String message) {
        super(message);
    }

    public ThirdpayApiException(Throwable cause) {
        super(cause);
    }

    public ThirdpayApiException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }
}

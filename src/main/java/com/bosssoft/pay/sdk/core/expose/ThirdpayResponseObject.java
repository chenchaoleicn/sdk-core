package com.bosssoft.pay.sdk.core.expose;

import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import com.bosssoft.pay.sdk.core.ThirdpayResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Title 响应数据对象
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public abstract class ThirdpayResponseObject implements Serializable {

    @JsonProperty(ThirdpayConstants.RESPONSE_CODE)
    private String code;

    @JsonProperty(ThirdpayConstants.RESPONSE_MSG)
    private String msg;

    @JsonProperty(ThirdpayConstants.RESPONSE_SUB_CODE)
    private String subCode;

    @JsonProperty(ThirdpayConstants.RESPONSE_SUB_MSG)
    private String subMsg;

    @JsonIgnore
    private String body;

    @JsonIgnore
    ThirdpayResponse thirdpayResponse;

    // getters setters set to final, forbid Override
    @JsonIgnore
    public final boolean isSuccess() {
        return StringUtils.isEmpty(subCode);
    }

    public final String getCode() {
        return code;
    }

    public final void setCode(String code) {
        this.code = code;
    }

    public final String getMsg() {
        return msg;
    }

    public final void setMsg(String msg) {
        this.msg = msg;
    }

    public final String getSubCode() {
        return subCode;
    }

    public final void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public final String getSubMsg() {
        return subMsg;
    }

    public final void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public final String getBody() {
        return body;
    }

    public final void setBody(String body) {
        this.body = body;
    }

    public final ThirdpayResponse getThirdpayResponse() {
        return thirdpayResponse;
    }

    public final void setThirdpayResponse(ThirdpayResponse thirdpayResponse) {
        this.thirdpayResponse = thirdpayResponse;
    }

}

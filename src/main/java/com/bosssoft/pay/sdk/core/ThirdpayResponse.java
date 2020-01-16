package com.bosssoft.pay.sdk.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @Title 响应模型
 * @Description 网关中响应的数据模型
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpayResponse implements Serializable {

    /**
     * 响应码
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_CODE)
    private Integer code;

    /**
     * 响应信息
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_MSG)
    private String msg;

    /**
     * 响应子码
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_SUB_CODE)
    private String subCode;

    /**
     * 响应子信息
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_SUB_MSG)
    private String subMsg;

    /**
     * rsa类型
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_RSA_TYPE)
    private String rsaType;

    /**
     * 字符集
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_CHARSET)
    private String charset;

    /**
     * 格式
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_FORMAT)
    private String format;

    /**
     * 签名
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_SIGN)
    private String sign;

    /**
     * 签名类型
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_SIGN_TYPE)
    private String signType;

    /**
     * 加密类型
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_ENCRYPT_TYPE)
    private String encryptType;

    /**
     * 加密秘钥
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_ENCRYPT_KEY)
    private String encryptKey;

    /**
     * 响应的原始内容
     */
    @JsonProperty(ThirdpayConstants.RESPONSE_CONTENT)
    private String content;

    /**
     * 响应的真实内容(明文)
     */
    @JsonIgnore
    private String realContent;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getRsaType() {
        return rsaType;
    }

    public void setRsaType(String rsaType) {
        this.rsaType = rsaType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRealContent() {
        return realContent;
    }

    public void setRealContent(String realContent) {
        this.realContent = realContent;
    }
}

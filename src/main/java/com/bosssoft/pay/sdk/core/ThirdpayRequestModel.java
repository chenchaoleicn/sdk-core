package com.bosssoft.pay.sdk.core;

/**
 * @Title 请求模型
 * @Description 网关中接收的数据模型
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpayRequestModel {

    private String merchantId;

    private String apiName;

    private String rsaType;

    private String charset;

    private String format;

    private String signType;

    private String sign;

    private String encryptType;

    private String encryptKey;

    private String content;

    private String version;

    private String timestamp;

    private String thirdpaySdk;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
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

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getThirdpaySdk() {
        return thirdpaySdk;
    }

    public void setThirdpaySdk(String thirdpaySdk) {
        this.thirdpaySdk = thirdpaySdk;
    }
}

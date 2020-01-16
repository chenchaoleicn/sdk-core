package com.bosssoft.pay.sdk.core;

import com.bosssoft.pay.sdk.core.config.ApiConfigModel;
import com.bosssoft.pay.sdk.core.config.ConfigService;
import com.bosssoft.pay.sdk.core.expose.ThirdpayRequestObject;
import com.bosssoft.pay.sdk.core.expose.ThirdpayResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @Title API请求抽象扩展
 * @Description 实现IThirdpayRequest中所有接口, 便于SDK中API定义时只专注于业务模型定义
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public abstract class ThirdpayRequest<Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject>
        implements IThirdpayRequest<Request, Response> {

    private String apiName;
    private String apiVersion;
    private boolean needEncrypt;
    private String bizContent;

    private Class<Request> requestClass;
    private Class<Response> responseClass;

    private ApiConfigModel config;

    public ThirdpayRequest() {
        config = ConfigService.getApiConfig(this);
        this.apiName = config.getName();
        this.apiVersion = config.getVersion();
        this.needEncrypt = config.isEncrypt();
        this.requestClass = config.getRequestClass();
        this.responseClass = config.getResponseClass();
    }

    @Override
    public Class<Request> getRequestClass() {
        return requestClass;
    }

    @Override
    public Class<Response> getResponseClass() {
        return responseClass;
    }

    protected void setBizContentForJsonObject(Object requestObject) {
        try {
            this.bizContent = new ObjectMapper().writeValueAsString(requestObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("parse to json error", e);
        }
    }

    protected void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setBizContentForJsonFormat(Map requestObject) {
        setBizContentForJsonObject(requestObject);
    }

    public void setBizContentForJsonFormat(ThirdpayRequestObject requestObject) {
        setBizContentForJsonObject(requestObject);
    }

    @Override
    public String getApiName() {
        return apiName;
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public boolean isNeedEncrypt() {
        return needEncrypt;
    }

    @Override
    public void setNeedEncrypt(boolean needEncrypt) {
        this.needEncrypt = needEncrypt;
    }

    @Override
    public String getBizContent() {
        return bizContent;
    }

    @Override
    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }

}

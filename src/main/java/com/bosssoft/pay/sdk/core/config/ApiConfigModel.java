package com.bosssoft.pay.sdk.core.config;

import com.bosssoft.pay.sdk.core.expose.ThirdpayRequestObject;
import com.bosssoft.pay.sdk.core.expose.ThirdpayResponseObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Title api配置
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ApiConfigModel<Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> {

    /**
     * api名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * api版本
     */
    @JsonProperty("version")
    private String version;

    /**
     * 请求是否加密
     */
    @JsonProperty("encrypt")
    private Boolean encrypt;

    /**
     * 是否校验业务请求参数
     */
    @JsonProperty("valid")
    private Boolean valid;

    /**
     * url(在生成具体的url时, url优先级高于urlMapRule)
     */
    @JsonProperty("url")
    private String url;

    /**
     * api名称映射为url的映射规则(在生成具体的url时, url优先级高于urlMapRule)
     */
    @JsonProperty("map_url_rule")
    private String mapUrlRule;

    /**
     * url名称前缀
     */
    @JsonProperty("url_prefix")
    private String urlPrefix;

    /**
     * url名称后缀
     */
    @JsonProperty("url_suffix")
    private String urlSuffix;

    /**
     * 忽略api版本
     */
    @JsonProperty("ignore_version")
    private Boolean ignoreVersion;

    @JsonIgnore
    private Class<Request> requestClass;

    @JsonIgnore
    private Class<Response> responseClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Boolean encrypt) {
        this.encrypt = encrypt;
    }

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMapUrlRule() {
        return mapUrlRule;
    }

    public void setMapUrlRule(String mapUrlRule) {
        this.mapUrlRule = mapUrlRule;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public Boolean isIgnoreVersion() {
        return ignoreVersion;
    }

    public void setIgnoreVersion(Boolean ignoreVersion) {
        this.ignoreVersion = ignoreVersion;
    }

    public Class<Request> getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(Class<Request> requestClass) {
        this.requestClass = requestClass;
    }

    public Class<Response> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<Response> responseClass) {
        this.responseClass = responseClass;
    }
}

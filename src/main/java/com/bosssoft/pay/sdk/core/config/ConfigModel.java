package com.bosssoft.pay.sdk.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * @Title 全局配置
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ConfigModel {

    @JsonProperty("api_common_config")
    private ApiCommonConfigModel apiCommonConfig;

    @JsonProperty("api_configs")
    private HashMap<String, ApiConfigModel> apiConfigs;

    public ApiCommonConfigModel getApiCommonConfig() {
        return apiCommonConfig;
    }

    public void setApiCommonConfig(ApiCommonConfigModel apiCommonConfig) {
        this.apiCommonConfig = apiCommonConfig;
    }

    public HashMap<String, ApiConfigModel> getApiConfigs() {
        return apiConfigs;
    }

    public void setApiConfigs(HashMap<String, ApiConfigModel> apiConfigs) {
        this.apiConfigs = apiConfigs;
    }
}

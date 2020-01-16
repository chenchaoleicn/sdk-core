package com.bosssoft.pay.sdk.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @Title api通用配置
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ApiCommonConfigModel {

    @JsonProperty("package_scan")
    private List<String> packageScan;

    @JsonProperty("default_config")
    private ApiConfigModel defaultConfig;

    public List<String> getPackageScan() {
        return packageScan;
    }

    public void setPackageScan(List<String> packageScan) {
        this.packageScan = packageScan;
    }

    public ApiConfigModel getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(ApiConfigModel defaultConfig) {
        this.defaultConfig = defaultConfig;
    }
}

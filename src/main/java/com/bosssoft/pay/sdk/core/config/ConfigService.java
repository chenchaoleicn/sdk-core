package com.bosssoft.pay.sdk.core.config;

import com.bosssoft.pay.sdk.core.IThirdpayRequest;
import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import com.bosssoft.pay.sdk.core.ThirdpayRequest;
import com.bosssoft.pay.sdk.core.internal.mapping.ApiService;
import com.bosssoft.pay.sdk.core.internal.util.ClassUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title 配置服务
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ConfigService {

    private static Map<String, ApiConfigModel> apiConfigMap = new HashMap<String, ApiConfigModel>();

    private static ObjectMapper lazyMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static ObjectMapper mapperWithIgnoreEmpty = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    private static ConfigModel globalConfig = new ConfigModel();

    static {
        try {
            apiConfigMap = getLoadableAPiConfigs();
        } catch (IOException e) {
            throw new RuntimeException("API配置载入失败, 异常栈:", e);
        }
    }

    private static ApiConfigModel generateApiConfig(Class<?> clazz) {
        ApiService apiService = clazz.getAnnotation(ApiService.class);
        String name = apiService.value();

        Map<String, Object> mapConfig = new HashMap<String, Object>();

        // 注解配置
        ApiConfigModel annotationConfig = new ApiConfigModel();
        annotationConfig.setName(name);
        annotationConfig.setVersion(apiService.version());
        annotationConfig.setEncrypt(apiService.encrypt());
        annotationConfig.setValid(apiService.valid());
        annotationConfig.setUrl(apiService.url());
        annotationConfig.setMapUrlRule(apiService.mapUrlRule());
        annotationConfig.setUrlPrefix(apiService.urlPrefix());
        annotationConfig.setUrlSuffix(apiService.urlSuffix());
        annotationConfig.setIgnoreVersion(apiService.ignoreVersion());
        mapConfig.putAll(mapperWithIgnoreEmpty.convertValue(annotationConfig, HashMap.class));

        // 通用配置
        ApiConfigModel commonConfig = globalConfig.getApiCommonConfig().getDefaultConfig();
        mapConfig.putAll(mapperWithIgnoreEmpty.convertValue(commonConfig, HashMap.class));

        // 定制化配置
        HashMap<String, ApiConfigModel> apiConfigs = globalConfig.getApiConfigs();
        if (apiConfigs != null && apiConfigs.containsKey(name)) {
            ApiConfigModel customConfig = apiConfigs.get(name);
            mapConfig.putAll(mapperWithIgnoreEmpty.convertValue(customConfig, HashMap.class));
        }

        ApiConfigModel apiConfig = mapperWithIgnoreEmpty.convertValue(mapConfig, ApiConfigModel.class);
        Type type = clazz.getGenericSuperclass();
        // 由于type是ThirdpayRequest的实现类型, 而ThirdpayRequest含有泛型信息
        // 故instanceof ParameterizedType == true, 可直接强制转型
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        apiConfig.setRequestClass((Class) types[0]);
        apiConfig.setResponseClass((Class) types[1]);
        return apiConfig;
    }

    /**
     * 获取api配置
     * @param instance
     * @return
     */
    public static ApiConfigModel getApiConfig(IThirdpayRequest instance) {
        ApiService service = instance.getClass().getAnnotation(ApiService.class);
        if (service != null) {
            return apiConfigMap.get(service.value());
        }
        return null;
    }

    /**
     * 获取api配置
     * @param name
     * @return
     */
    public static ApiConfigModel getApiConfig(String name) {
        return apiConfigMap.get(name);
    }

    /**
     * 获取所有api配置
     * @return
     */
    public static Map<String, ApiConfigModel> getApiConfigs() {
        return apiConfigMap;
    }

    /**
     * 获取可载入的api配置
     * @return
     * @throws IOException
     */
    private static Map<String, ApiConfigModel> getLoadableAPiConfigs() throws IOException {
        Map<String, ApiConfigModel> apiConfigMapTemp = new HashMap<String, ApiConfigModel>();
        InputStream is = ConfigService.class.getClassLoader().getResourceAsStream(ThirdpayConstants.CONFIG_FILE);
        if (is != null) {
            globalConfig = lazyMapper.readValue(is, ConfigModel.class);
        }
        wrapper(globalConfig);
        List<Class<?>> classes = new ArrayList<Class<?>>();
        List<String> packageScans = globalConfig.getApiCommonConfig().getPackageScan();
        packageScans.add(ThirdpayConstants.DEAFULT_PACKAGE_FOR_LOAD_APIS);

        for (String packageScan: packageScans) {
            classes.addAll(ClassUtils.getAllClassByPackageName(packageScan));
        }

        for (Class<?> clazz: classes) {
            ApiService apiService = clazz.getAnnotation(ApiService.class);
            if (ThirdpayRequest.class.isAssignableFrom(clazz)
                    && apiService != null) {
                String name = apiService.value();
                apiConfigMapTemp.put(name, generateApiConfig(clazz));
            }
        }
        return apiConfigMapTemp;
    }

    private static void wrapper(ConfigModel globalConfig) {
        if(globalConfig == null) {
            globalConfig = new ConfigModel();
        }
        if (globalConfig.getApiCommonConfig() == null) {
            globalConfig.setApiCommonConfig(new ApiCommonConfigModel());
        }
        if (globalConfig.getApiCommonConfig().getDefaultConfig() == null) {
            globalConfig.getApiCommonConfig().setDefaultConfig(new ApiConfigModel());
        }
        if (globalConfig.getApiCommonConfig().getPackageScan() == null) {
            globalConfig.getApiCommonConfig().setPackageScan(new ArrayList<String>());
        }
        if (globalConfig.getApiConfigs() == null) {
            globalConfig.setApiConfigs(new HashMap<String, ApiConfigModel>());
        }
    }
}

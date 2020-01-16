package com.bosssoft.pay.sdk.core.internal.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title 接口名映射为url的策略
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public enum MapUrlStrategy {

    /**
     * 点分式映射为url分隔符
     */
    POINT_TO_URL_SEPERATOR("default");

    private String value;

    MapUrlStrategy(String value) {
        this.value = value;
    }

    private static final Map<String, MapUrlStrategy> valueMap = new HashMap<String, MapUrlStrategy>();

    static {
        for (MapUrlStrategy dictItem : values()) {
            valueMap.put(dictItem.value, dictItem);
        }
    }

    public static MapUrlStrategy convert(String value) {
        return valueMap.get(value);
    }

}

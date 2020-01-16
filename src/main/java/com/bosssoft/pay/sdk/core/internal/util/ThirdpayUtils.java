package com.bosssoft.pay.sdk.core.internal.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Title thirdpay工具类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpayUtils {

    /**
     * 获取继承链(包括自身)
     * @param calzz
     * @return
     */
    public static List<Class<?>> getSuperClassWithSelf(Class<?> calzz){
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(calzz);
        Class<?> superClass = calzz.getSuperclass();
        while (superClass != null) {
            if(superClass.getName().equals("java.lang.Object")){
                break;
            }
            list.add(superClass);
            superClass = superClass.getSuperclass();
        }
        return list;
    }

    /**
     * 验证SDK版本号
     * @param versionNo
     * @return
     */
    public static boolean isValidSdkVersion(String versionNo) {
        if (StringUtils.isEmpty(versionNo)) {
            return false;
        }
        return true;
    }

    /**
     * 转换为有序的kv结构串(todo 重构, 使用LinkedHashMap排序, 并处理递归的List、Map)
     * @param params
     * @return
     */
    public static String getSortKV(Map params) {
        StringBuilder sb = new StringBuilder();
        ArrayList keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        int index = 0;

        for (int i = 0; i < keys.size(); ++i) {
            String key = (String) keys.get(i);
            Object value = params.get(key);
            value = value == null ? (String) value : value.toString();
            if (StringUtils.isNotEmpty(key)) {
                sb.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }

        return sb.toString();
    }

}

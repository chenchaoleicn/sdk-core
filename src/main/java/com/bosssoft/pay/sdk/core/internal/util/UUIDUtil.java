package com.bosssoft.pay.sdk.core.internal.util;

import java.util.UUID;

/**
 * @Title UUID工具
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class UUIDUtil {

    /**
     * nil uuid
     */
    public static String NIL_UUID = "00000000000000000000000000000000";

    private static final String EMPTY_STRING = "";

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", EMPTY_STRING);
    }

    /**
     * 获取uuid
     * @param number
     * @return
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] uuids = new String[number];
        for (int i = 0; i < number; i++) {
            uuids[i] = getUUID();
        }
        return uuids;
    }
}

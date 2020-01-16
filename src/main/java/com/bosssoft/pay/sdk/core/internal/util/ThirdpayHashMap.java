package com.bosssoft.pay.sdk.core.internal.util;

import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @Title HashMap扩展类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpayHashMap extends HashMap<String, String> {
    public ThirdpayHashMap() {
    }

    public ThirdpayHashMap(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public String put(String key, Object value) {
        String strValue;
        if(value == null) {
            strValue = null;
        } else if(value instanceof String) {
            strValue = (String)value;
        } else if(value instanceof Integer) {
            strValue = ((Integer)value).toString();
        } else if(value instanceof Long) {
            strValue = ((Long)value).toString();
        } else if(value instanceof Float) {
            strValue = ((Float)value).toString();
        } else if(value instanceof Double) {
            strValue = ((Double)value).toString();
        } else if(value instanceof Boolean) {
            strValue = ((Boolean)value).toString();
        } else if(value instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat(ThirdpayConstants.DATE_TIME_FORMAT);
            format.setTimeZone(TimeZone.getTimeZone(ThirdpayConstants.DATE_TIMEZONE));
            strValue = format.format((Date)value);
        } else {
            strValue = value.toString();
        }
        return this.put(key, strValue);
    }

    @Override
    public String put(String key, String value) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
            return (String)super.put(key, value);
        }
        return null;
    }
}

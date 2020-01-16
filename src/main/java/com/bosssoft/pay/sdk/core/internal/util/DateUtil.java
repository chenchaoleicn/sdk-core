package com.bosssoft.pay.sdk.core.internal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Title 日期工具类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class DateUtil {

    public static Date parseDate(String dateTime, String format) {
        SimpleDateFormat parse = new SimpleDateFormat(format);
        try {
            return parse.parse(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

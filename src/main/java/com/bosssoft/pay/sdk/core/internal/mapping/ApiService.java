package com.bosssoft.pay.sdk.core.internal.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title api配置注解
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ApiService {

    /**
     * 接口名
     * @return
     */
    String value();

    /**
     * 接口版本
     * @return
     */
    String version() default "1.0";

    /**
     * 加密
     * @return
     */
    boolean encrypt() default true;

    /**
     * 校验
     * @return
     */
    boolean valid() default true;

    /**
     * url
     * @return
     */
    String url() default "";

    /**
     * api名称映射为url的映射规则
     * 具体定义参照 MapUrlRuleEnum.java
     * @return
     */
    String mapUrlRule() default "default";

    /**
     * url前缀
     * @return
     */
    String urlPrefix() default "";

    /**
     * url后缀
     * @return
     */
    String urlSuffix() default ".do";

    /**
     * 是否忽略版本
     * @return
     */
    boolean ignoreVersion() default true;
}

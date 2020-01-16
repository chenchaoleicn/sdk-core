package com.bosssoft.pay.sdk.core;

/**
 * @Title 常量类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpayConstants {

    // 默认配置
    public static final int DEFAULT_CONNECT_TIME_OUT = 3000;
    public static final int DEFAULT_READ_TIME_OUT = 30000;
    public static final int DEFAULT_SSL_SESSION_TIME_OUT = 15;
    public static final int DEFAULT_SSL_SESSION_CACHE_SIZE = 1000;

    // RSA算法(加密、签名)
    public static final String ALGORITHM_TYPE_RSA = "RSA";

    // RSA类型, RSA对应于1024位密钥的加密解密, RSA2对应于2048位密钥的加密解密
    public static final String RSA_TYPE_V1 = "RSA";
    public static final String RSA_TYPE_V2 = "RSA2";

    // 签名类型, RSA对应SHA1WithRSA算法, RSA2对应SHA256WithRSA算法
    public static final String SIGN_TYPE_RSA_V1 = "RSA";
    public static final String SIGN_TYPE_RSA_V2 = "RSA2";
    public static final String SIGN_SHA1RSA_ALGORITHMS = "SHA1WithRSA";
    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    // 加密类型, AES为对称加密
    public static final String ENCRYPT_TYPE_AES = "AES";

    // 请求相关常量
    public static final String MERCHANT_ID = "merchant_id";
    public static final String API_NAME = "api_name";
    public static final String RSA_TYPE = "rsa_type";
    public static final String CHARSET = "charset";
    public static final String FORMAT = "format";
    public static final String SIGN = "sign";
    public static final String SIGN_TYPE = "sign_type";
    public static final String TIMESTAMP = "timestamp";
    public static final String VERSION = "version";
    public static final String ENCRYPT_TYPE = "encrypt_type";
    public static final String ENCRYPT_KEY = "encrypt_key";
    public static final String THIRDPAY_SDK = "thirdpay_sdk";
    public static final String CONTENT = "content";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final String FORMAT_JSON = "json";
    public static final String FORMAT_XML = "xml";
    public static final String SDK_VERSION = "thirdpay-sdk-java-dynamicVersionNo";
    // 轨迹id(保留字段)
    public static final String TRACE_ID = "bosssoftpaytraceid";

    // 响应相关常量
    public static final String RESPONSE_CHARSET = "charset";
    public static final String RESPONSE_FORMAT = "format";
    public static final String RESPONSE_RSA_TYPE = "rsa_type";
    public static final String RESPONSE_SIGN = "sign";
    public static final String RESPONSE_SIGN_TYPE = "sign_type";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ENCRYPT_TYPE = "encrypt_type";
    public static final String RESPONSE_ENCRYPT_KEY = "encrypt_key";
    public static final String RESPONSE_CODE = "code";
    public static final String RESPONSE_MSG = "msg";
    public static final String RESPONSE_SUB_CODE = "sub_code";
    public static final String RESPONSE_SUB_MSG = "sub_msg";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIMEZONE = "GMT+8";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_GET = "GET";

    public static final String DEFAULT_API_NAME = "default";
    public static final String DEAFULT_PACKAGE_FOR_LOAD_APIS = "com.bosssoft.pay.sdk.core.request";

    public static final String CONFIG_FILE = "sdk-config.json";

    // 外部调用时使用的常量
    public static final String API_SEPARATOR = ".";
    public static final String URL_SEPERATOR = "/";

}

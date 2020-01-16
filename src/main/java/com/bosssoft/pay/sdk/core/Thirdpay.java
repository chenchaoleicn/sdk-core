package com.bosssoft.pay.sdk.core;

import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;

import java.util.Map;

/**
 * @Title 门面类
 * @Description 对外提供简易调用接口
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class Thirdpay extends ThirdpayFacade {

    /**
     * 请求(V1版, 对应1024位rsa秘钥的请求)
     * @param serviceUrl
     * @param merchantId
     * @param apiName
     * @param privateKey
     * @param thirdpayPublicKey
     * @param bizContentForJson
     * @return
     * @throws ThirdpayApiException
     */
    public static String request(String serviceUrl, String merchantId, String apiName, String privateKey, String thirdpayPublicKey, String bizContentForJson) throws ThirdpayApiException {
        return ThirdpayFacade.requestV1(serviceUrl, merchantId, apiName, privateKey, thirdpayPublicKey, bizContentForJson);
    }

    /**
     * 请求(V1版, 对应1024位rsa秘钥的请求)
     * @param serviceUrl
     * @param merchantId
     * @param apiName
     * @param privateKey
     * @param thirdpayPublicKey
     * @param bizContent
     * @return
     * @throws ThirdpayApiException
     */
    public static String request(String serviceUrl, String merchantId, String apiName, String privateKey, String thirdpayPublicKey, Map bizContent) throws ThirdpayApiException {
        return ThirdpayFacade.requestV1(serviceUrl, merchantId, apiName, privateKey, thirdpayPublicKey, bizContent);
    }

    /**
     * 请求(V2版, 对应2048位rsa秘钥的请求)
     * @param serviceUrl
     * @param merchantId
     * @param apiName
     * @param privateKey
     * @param thirdpayPublicKey
     * @param bizContentForJson
     * @return
     * @throws ThirdpayApiException
     */
    public static String requestV2(String serviceUrl, String merchantId, String apiName, String privateKey, String thirdpayPublicKey, String bizContentForJson) throws ThirdpayApiException {
        return ThirdpayFacade.requestV2(serviceUrl, merchantId, apiName, privateKey, thirdpayPublicKey, bizContentForJson);
    }

    /**
     * 请求(V2版, 对应2048位rsa秘钥的请求)
     * @param serviceUrl
     * @param merchantId
     * @param apiName
     * @param privateKey
     * @param thirdpayPublicKey
     * @param bizContent
     * @return
     * @throws ThirdpayApiException
     */
    public static String requestV2(String serviceUrl, String merchantId, String apiName, String privateKey, String thirdpayPublicKey, Map bizContent) throws ThirdpayApiException {
        return ThirdpayFacade.requestV2(serviceUrl, merchantId, apiName, privateKey, thirdpayPublicKey, bizContent);
    }

}

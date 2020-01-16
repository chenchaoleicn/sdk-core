package com.bosssoft.pay.sdk.core;

import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;
import com.bosssoft.pay.sdk.core.request.DefaultRequest;
import com.bosssoft.pay.sdk.core.response.DefaultResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Title 门面类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public abstract class ThirdpayFacade {

    /**
     * 发起请求(V1版, 对应1024位rsa秘钥的请求)
     * @param serviceUrl
     * @param merchantId
     * @param apiName
     * @param privateKey
     * @param thirdpayPublicKey
     * @param bizContent
     * @return
     * @throws ThirdpayApiException
     */
    protected static String requestV1(String serviceUrl, String merchantId, String apiName, String privateKey, String thirdpayPublicKey, Object bizContent) throws ThirdpayApiException {
        IThirdpayClient client = new DefaultThirdpayClient(
                serviceUrl, merchantId, ThirdpayConstants.RSA_TYPE_V1, privateKey,
                thirdpayPublicKey, ThirdpayConstants.CHARSET_UTF8, ThirdpayConstants.FORMAT_JSON);
        DefaultRequest request = new DefaultRequest(apiName);
        request.setBizContentForJsonObject(bizContent);
        DefaultResponse response = client.execute(request);
        if (!response.isSuccess()) {
            try {
                throw new ThirdpayApiException(new ObjectMapper().writeValueAsString(response));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return response.getBody();
    }

    /**
     * 发起请求(V2版, 对应2048位rsa秘钥的请求)
     * @param serviceUrl
     * @param merchantId
     * @param apiName
     * @param privateKey
     * @param thirdpayPublicKey
     * @param bizContent
     * @return
     * @throws ThirdpayApiException
     */
    protected static String requestV2(String serviceUrl, String merchantId, String apiName, String privateKey, String thirdpayPublicKey, Object bizContent) throws ThirdpayApiException {
        IThirdpayClient client = new DefaultThirdpayClient(
                serviceUrl, merchantId, ThirdpayConstants.RSA_TYPE_V2, privateKey,
                thirdpayPublicKey, ThirdpayConstants.CHARSET_UTF8, ThirdpayConstants.FORMAT_JSON);
        DefaultRequest request = new DefaultRequest(apiName);
        request.setBizContentForJsonObject(bizContent);
        DefaultResponse response = client.execute(request);
        if (!response.isSuccess()) {
            try {
                throw new ThirdpayApiException(new ObjectMapper().writeValueAsString(response));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return response.getBody();
    }
}

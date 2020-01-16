package com.bosssoft.pay.sdk.core;

import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;
import com.bosssoft.pay.sdk.core.expose.ThirdpayRequestObject;
import com.bosssoft.pay.sdk.core.expose.ThirdpayResponseObject;

/**
 * @Title 客户端
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public interface IThirdpayClient {

    /**
     * 发起请求
     * @param request
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> Response execute(IThirdpayRequest<Request, Response> request) throws ThirdpayApiException;

    /**
     * 发起请求
     * @param request
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> Response pageExecute(IThirdpayRequest<Request, Response> request) throws ThirdpayApiException;

}
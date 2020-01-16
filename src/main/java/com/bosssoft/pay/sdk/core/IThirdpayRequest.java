package com.bosssoft.pay.sdk.core;

import com.bosssoft.pay.sdk.core.expose.ThirdpayRequestObject;
import com.bosssoft.pay.sdk.core.expose.ThirdpayResponseObject;

/**
 * @Title 请求接口
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public interface IThirdpayRequest<Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> {

    String getApiName();

    String getApiVersion();

    boolean isNeedEncrypt();

    void setNeedEncrypt(boolean needEncrypt);

    String getBizContent();

    void setBizContent(String bizContent);

    Class<Request> getRequestClass();

    Class<Response> getResponseClass();
}

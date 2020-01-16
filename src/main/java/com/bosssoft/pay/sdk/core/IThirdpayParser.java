package com.bosssoft.pay.sdk.core;

import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;
import com.bosssoft.pay.sdk.core.expose.ThirdpayResponseObject;

import java.util.Map;

/**
 * @Title 解析器
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public interface IThirdpayParser<Response extends ThirdpayResponseObject> {

    /**
     * 获取业务的响应类
     * @return
     * @throws ThirdpayApiException
     */
    Class<Response> getResponseClass() throws ThirdpayApiException;

    /**
     * 解析响应结果
     * @param rspBody
     * @return
     * @throws ThirdpayApiException
     */
    ThirdpayResponse parse(String rspBody) throws ThirdpayApiException;

    /**
     * 解析业务响应结果
     * @param bizRspBody
     * @return
     * @throws ThirdpayApiException
     */
    Response parseBizResponse(String bizRspBody) throws ThirdpayApiException;

    /**
     * 转换为Map格式
     * @param content
     * @return
     * @throws ThirdpayApiException
     */
    Map<String, ?> parseContent2Map(String content) throws ThirdpayApiException;
}

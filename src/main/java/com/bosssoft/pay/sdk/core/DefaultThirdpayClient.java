package com.bosssoft.pay.sdk.core;

import com.bosssoft.pay.sdk.core.internal.parser.json.ObjectJsonParser;
import com.bosssoft.pay.sdk.core.internal.util.ThirdpayEncrypt;
import com.bosssoft.pay.sdk.core.internal.util.ThirdpayHashMap;
import com.bosssoft.pay.sdk.core.internal.util.ThirdpaySignature;
import com.bosssoft.pay.sdk.core.internal.util.WebUtils;
import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;
import com.bosssoft.pay.sdk.core.expose.ThirdpayRequestObject;
import com.bosssoft.pay.sdk.core.expose.ThirdpayResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title 默认客户端
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class DefaultThirdpayClient implements IThirdpayClient {

    private String serverUrl;
    private String merchantId;
    private String rsaType;
    private String privateKey;
    private String thirdpayPublicKey;
    private String charset;
    private String format;
    private String signType;
    private String encryptType;
    private String encryptKey;
    private int connectTimeout;
    private int readTimeout;

    /**
     * 客户端
     * @param serverUrl
     * @param merchantId
     * @param privateKey
     * @param thirdpayPublicKey
     */
    public DefaultThirdpayClient(String serverUrl, String merchantId, String rsaType, String privateKey, String thirdpayPublicKey) {
        this.serverUrl = serverUrl;
        this.merchantId = merchantId;
        this.rsaType = rsaType;
        this.privateKey = privateKey;
        this.thirdpayPublicKey = thirdpayPublicKey;
        this.charset = ThirdpayConstants.CHARSET_UTF8;
        this.format = ThirdpayConstants.FORMAT_JSON;
        this.signType = rsaTypeToSignTypeMap.get(rsaType);
        this.encryptType = ThirdpayConstants.ENCRYPT_TYPE_AES;
        this.encryptKey = null;
        this.connectTimeout = ThirdpayConstants.DEFAULT_CONNECT_TIME_OUT;
        this.readTimeout = ThirdpayConstants.DEFAULT_READ_TIME_OUT;
    }

    public DefaultThirdpayClient(String serverUrl, String merchantId, String rsaType, String privateKey, String thirdpayPublicKey, String charset) {
        this(serverUrl, merchantId, rsaType, privateKey, thirdpayPublicKey);
        this.charset = charset;
    }

    public DefaultThirdpayClient(String serverUrl, String merchantId, String rsaType, String privateKey, String thirdpayPublicKey, String charset, String format) {
        this(serverUrl, merchantId, rsaType, privateKey, thirdpayPublicKey, charset);
        this.format = format;
    }

    public DefaultThirdpayClient(String serverUrl, String merchantId, String rsaType, String privateKey, String thirdpayPublicKey, String charset, String format, String encryptType) {
        this(serverUrl, merchantId, rsaType, privateKey, thirdpayPublicKey, charset, format);
        this.encryptType = encryptType;
    }

    public DefaultThirdpayClient(String serverUrl, String merchantId, String rsaType, String privateKey, String thirdpayPublicKey, String charset, String format, String encryptType, int connectTimeout, int readTimeout) {
        this(serverUrl, merchantId, rsaType, privateKey, thirdpayPublicKey, charset, format, encryptType);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    /**
     * 发起请求
     * @param request
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    @Override
    public <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> Response execute(
            IThirdpayRequest<Request, Response> request) throws ThirdpayApiException {
        return this._execute(request);
    }

    /**
     * 发起请求
     * @param request
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    @Override
    public <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> Response pageExecute(
            IThirdpayRequest<Request, Response> request)
            throws ThirdpayApiException {
        return this._pageExecute(request, ThirdpayConstants.HTTP_METHOD_POST);
    }

    /**
     * 发起请求
     * @param request
     * @param httpMethod
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    public <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> Response _pageExecute(
            IThirdpayRequest<Request, Response> request, String httpMethod) throws ThirdpayApiException {
        IThirdpayParser<Response> parser = getParser(request);
        ThirdpayRequestModel thirdpayRequestModel = buildThirdpayRequestModel(request, parser);
        String requestUrl = this.getRequestUrl(thirdpayRequestModel);
        ThirdpayHashMap contentMap = getRequestParams(thirdpayRequestModel);

        Class responseClass = request.getResponseClass();
        Response bizResponse = null;
        try {
            bizResponse = (Response) responseClass.newInstance();
        } catch (InstantiationException e) {
            throw new ThirdpayApiException("init response object error, original exception class name is InstantiationException", e);
        } catch (IllegalAccessException e) {
            throw new ThirdpayApiException("init response object error, original exception class name is IllegalAccessException", e);
        }

        if(ThirdpayConstants.HTTP_METHOD_POST.equalsIgnoreCase(httpMethod)) {
            bizResponse.setBody(WebUtils.buildForm(requestUrl, contentMap, this.charset));
        } else {
            throw new ThirdpayApiException("does not implement method:" + httpMethod);
        }

        return bizResponse;
    }

    /**
     * 发起请求
     * @param request
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    private <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> Response _execute(
            IThirdpayRequest<Request, Response> request)
            throws ThirdpayApiException {
        IThirdpayParser<Response> parser = getParser(request);
        ThirdpayRequestModel thirdpayRequestModel = buildThirdpayRequestModel(request, parser);
        ThirdpayResponse response = this.doPost(request, parser, thirdpayRequestModel);
        if(response == null) {
            return null;
        }

        Response bizResponse = null;
        if (isUnexceptedError(response)) {
            bizResponse = buildBizResponse(response, request.getResponseClass());
            return bizResponse;
        }
        try {
            this.decryptResponse(request, response, parser);
            this.checkResponseSign(request, response, parser);
            bizResponse = this.parseBizResponse(response, parser);
        } catch (RuntimeException e) {
            throw new ThirdpayApiException("parse rspBody error", e);
        } catch (ThirdpayApiException e) {
            throw new ThirdpayApiException("parse rspBody error", e);
        }

        return (Response) bizResponse;
    }


    /**
     * 发起post请求
     * @param request
     * @param parser
     * @param thirdpayRequestModel
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    private <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> ThirdpayResponse doPost(
            IThirdpayRequest<Request, Response> request, IThirdpayParser<Response> parser, ThirdpayRequestModel thirdpayRequestModel)
            throws ThirdpayApiException {
        String url = this.getRequestUrl(thirdpayRequestModel);
        ThirdpayHashMap contentMap = getRequestParams(thirdpayRequestModel);
        String rsp = null;
        try {
            rsp = WebUtils.doPost(url, contentMap, this.charset, this.connectTimeout, this.readTimeout);
        } catch (IOException e) {
            throw new ThirdpayApiException(e);
        }
        return parser.parse(rsp);
    }

    private ThirdpayHashMap getRequestParams(ThirdpayRequestModel thirdpayRequestModel) {
        ThirdpayHashMap contentMap = new ThirdpayHashMap();
        contentMap.put(ThirdpayConstants.SIGN, thirdpayRequestModel.getSign());
        contentMap.put(ThirdpayConstants.ENCRYPT_KEY, thirdpayRequestModel.getEncryptKey());
        contentMap.put(ThirdpayConstants.CONTENT, thirdpayRequestModel.getContent());
        return contentMap;
    }

    /**
     * 获取解析器
     * @param request
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    public <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject>
    IThirdpayParser<Response> getParser(IThirdpayRequest<Request, Response> request) throws ThirdpayApiException {
        if (ThirdpayConstants.FORMAT_JSON.equals(this.format)) {
            return new ObjectJsonParser(request.getResponseClass());
        } else {
            throw new ThirdpayApiException("does not support data format:" + format);
        }
    }

    /**
     * 是否属于未知异常
     * @param response
     * @return
     */
    private boolean isUnexceptedError(ThirdpayResponse response) {
        String signType = response.getSignType();
        if (StringUtils.isEmpty(signType)) {
            return true;
        }
        return false;
    }

    /**
     * 构建请求model
     * @param request
     * @param parser
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    private <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> ThirdpayRequestModel buildThirdpayRequestModel(
            IThirdpayRequest<Request, Response> request, IThirdpayParser<Response> parser) throws ThirdpayApiException {
        ThirdpayRequestModel requestModel = new ThirdpayRequestModel();
        requestModel.setMerchantId(this.merchantId);
        requestModel.setApiName(request.getApiName());
        requestModel.setRsaType(this.rsaType);
        requestModel.setCharset(this.charset);
        requestModel.setFormat(this.format);

        String bizContent = request.getBizContent();
        // String signOriContent = ThirdpaySignature.getSignOriginalContent(parser.parseContent2Map(bizContent));
        String signOriContent = bizContent;
        String sign = ThirdpaySignature.sign(signOriContent, this.privateKey, this.charset, this.signType);
        requestModel.setSignType(this.signType);
        requestModel.setSign(sign);

        String encryptKey = this.encryptKey;
        if(request.isNeedEncrypt()) {
            String content = bizContent;
            if (StringUtils.isEmpty(this.encryptType)) {
                throw new ThirdpayApiException("API请求必须加密, 请设置对称加密的密钥类型, encryptType=" + this.encryptType);
            }
            if (ThirdpaySignature.isBelongToPublicKeyInfrastructure(this.encryptType)) {
                // 非对称加密
                content = ThirdpaySignature.rsaEncrypt(bizContent, this.thirdpayPublicKey, this.charset, this.encryptType);
            } else {
                // 对称加密
                if(StringUtils.isEmpty(encryptKey)) {
                    encryptKey = ThirdpayEncrypt.generateKey(this.encryptType);
                }
                content = ThirdpayEncrypt.encrypt(bizContent, this.encryptType, encryptKey, this.charset);
                encryptKey = ThirdpaySignature.rsaEncrypt(encryptKey, this.thirdpayPublicKey, this.charset, this.rsaType);
            }
            requestModel.setEncryptType(this.encryptType);
            requestModel.setEncryptKey(encryptKey);
            requestModel.setContent(content);
        } else {
            requestModel.setEncryptType(null);
            requestModel.setEncryptKey(null);
            requestModel.setContent(bizContent);
        }

        requestModel.setVersion(request.getApiVersion());

        Long timestamp = Long.valueOf(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat(ThirdpayConstants.DATE_TIME_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(ThirdpayConstants.DATE_TIMEZONE));
        requestModel.setTimestamp(df.format(new Date(timestamp.longValue())));

        requestModel.setThirdpaySdk(ThirdpayConstants.SDK_VERSION);

        return requestModel;
    }

    /**
     * 解密响应对象
     * @param request
     * @param response
     * @param parser
     * @param <Request>
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    private <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> void decryptResponse(
            IThirdpayRequest<Request, Response> request, ThirdpayResponse response, IThirdpayParser<Response> parser)
            throws ThirdpayApiException {
        String rspContent = response.getContent();
        String realContent;
        String charset = response.getCharset();
        String rsaType = response.getRsaType();
        String encryptType = response.getEncryptType();
        String encryptKey = response.getEncryptKey();
        if(request.isNeedEncrypt()) {
            if (ThirdpaySignature.isBelongToPublicKeyInfrastructure(encryptType)) {
                realContent = ThirdpaySignature.rsaDecrypt(rspContent, this.privateKey, charset, encryptType);
            } else {
                encryptKey = ThirdpaySignature.rsaDecrypt(encryptKey, this.privateKey, charset, rsaType);
                realContent = ThirdpayEncrypt.decrypt(rspContent, encryptType, encryptKey, charset);
            }
        } else {
            realContent = rspContent;
        }
        response.setRealContent(realContent);
    }

    /**
     * 验签
     * @param request
     * @param response
     * @param parser
     * @param <Request>
     * @param <Response>
     * @throws ThirdpayApiException
     */
    private <Request extends ThirdpayRequestObject, Response extends ThirdpayResponseObject> void checkResponseSign(
            IThirdpayRequest<Request, Response> request, ThirdpayResponse response, IThirdpayParser<Response> parser) throws ThirdpayApiException {
        String realContent = response.getRealContent();
        String signOriContent = realContent;
        String  sign = response.getSign();
        String signType = response.getSignType();
        String charset = response.getCharset();
        boolean checkResult = ThirdpaySignature.rsaCheck(signOriContent, sign, signType, this.thirdpayPublicKey, charset);
        if (!checkResult) {
            throw new ThirdpayApiException("check sign fail");
        }
    }

    /**
     * 解析业务响应
     * @param response
     * @param parser
     * @param <Response>
     * @return
     * @throws ThirdpayApiException
     */
    private <Response extends ThirdpayResponseObject> Response parseBizResponse(
            ThirdpayResponse response, IThirdpayParser<Response> parser) throws ThirdpayApiException {
        ThirdpayResponseObject bizResponse = parser.parseBizResponse(response.getRealContent());
        bizResponse.setCode(String.valueOf(response.getCode()));
        bizResponse.setMsg(response.getMsg());
        bizResponse.setSubCode(response.getSubCode());
        bizResponse.setSubMsg(response.getSubMsg());
        return (Response) bizResponse;
    }

    private <Response extends ThirdpayResponseObject> Response buildBizResponse(
            ThirdpayResponse response, Class bizResponseClass) throws ThirdpayApiException {
        Map  map = new HashMap<String, String>();
        map.put(ThirdpayConstants.RESPONSE_CODE, String.valueOf(response.getCode()));
        map.put(ThirdpayConstants.RESPONSE_MSG, response.getMsg());
        map.put(ThirdpayConstants.RESPONSE_SUB_CODE, response.getSubCode());
        map.put(ThirdpayConstants.RESPONSE_SUB_MSG, response.getSubMsg());
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (Response) mapper.readValue(mapper.writeValueAsString(map), bizResponseClass);
        } catch (Exception e) {
            throw new ThirdpayApiException("build bizResponse error", e);
        }
    }

    /**
     * 获取请求url
     * @param thirdpayRequestModel
     * @return
     * @throws ThirdpayApiException
     */
    private String getRequestUrl(ThirdpayRequestModel thirdpayRequestModel) throws ThirdpayApiException {
        try {
            Map urlParams = new LinkedHashMap<String, String>();
            urlParams.put(ThirdpayConstants.MERCHANT_ID, thirdpayRequestModel.getMerchantId());
            urlParams.put(ThirdpayConstants.API_NAME, thirdpayRequestModel.getApiName());
            urlParams.put(ThirdpayConstants.RSA_TYPE, thirdpayRequestModel.getRsaType());
            urlParams.put(ThirdpayConstants.CHARSET, thirdpayRequestModel.getCharset());
            urlParams.put(ThirdpayConstants.FORMAT, thirdpayRequestModel.getFormat());
            urlParams.put(ThirdpayConstants.SIGN_TYPE, thirdpayRequestModel.getSignType());
            urlParams.put(ThirdpayConstants.ENCRYPT_TYPE, thirdpayRequestModel.getEncryptType());
            urlParams.put(ThirdpayConstants.VERSION, thirdpayRequestModel.getVersion());
            urlParams.put(ThirdpayConstants.TIMESTAMP, thirdpayRequestModel.getTimestamp());
            urlParams.put(ThirdpayConstants.THIRDPAY_SDK, thirdpayRequestModel.getThirdpaySdk());
            return this.serverUrl + "?" + WebUtils.buildQuery(urlParams, this.charset);
        } catch (IOException e) {
            throw new ThirdpayApiException(e);
        }
    }

    private static Map<String, String> rsaTypeToSignTypeMap = new HashMap<String, String>();

    static {
        rsaTypeToSignTypeMap.put(ThirdpayConstants.RSA_TYPE_V1, ThirdpayConstants.SIGN_TYPE_RSA_V1);
        rsaTypeToSignTypeMap.put(ThirdpayConstants.RSA_TYPE_V2, ThirdpayConstants.SIGN_TYPE_RSA_V2);
    }

    static {
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }
}

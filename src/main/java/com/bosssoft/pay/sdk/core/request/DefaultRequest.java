package com.bosssoft.pay.sdk.core.request;

import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import com.bosssoft.pay.sdk.core.ThirdpayRequest;
import com.bosssoft.pay.sdk.core.domain.DefaultModel;
import com.bosssoft.pay.sdk.core.internal.mapping.ApiService;
import com.bosssoft.pay.sdk.core.response.DefaultResponse;


/**
 * @Title 默认(通用)api接口
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
@ApiService(ThirdpayConstants.DEFAULT_API_NAME)
public class DefaultRequest extends ThirdpayRequest<DefaultModel, DefaultResponse> {

    public DefaultRequest(String apiName) {
        this.setApiName(apiName);
    }

    @Override
    public Class<DefaultModel> getRequestClass() {
        return DefaultModel.class;
    }

    @Override
    public Class<DefaultResponse> getResponseClass() {
        return DefaultResponse.class;
    }

}

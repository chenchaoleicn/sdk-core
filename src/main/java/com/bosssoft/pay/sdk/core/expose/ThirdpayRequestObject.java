package com.bosssoft.pay.sdk.core.expose;

import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Title 请求数据对象
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public abstract class ThirdpayRequestObject {

    @JsonProperty(ThirdpayConstants.MERCHANT_ID)
    @NotBlank(message = "merchant_id不能为空")
    @Length(max = 64)
    private String merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}

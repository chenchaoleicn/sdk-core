# sdk-core
## 背景
在支付宝、银联、微信等支付平台接入时，支付宝的SDK包最简洁，其内部设计具有良好的扩展性。
## 开发
在支付宝SDK包原设计上，增加了定制功能(对称加密、非对称加密、数据校验、http链路跟踪等)，并对其核心进行了重构。
为了在设计多个SDK时进行代码复用，将其核心功能拆分出来，即为当前项目。
其业务型部分拆分到xxx-sdk项目，可参考样例sdk(pay-sdk): https://github.com/chenchaolei/pay-sdk.git
## 使用
下文中提到的接口thirdpay.trade.page.pay(对应的类为ThirdpayTradePagePayRequest)来自于pay-sdk项目
##### 1.常规方式(与支付宝SDK中请求方式类似)
```
    IThirdpayClient client = new DefaultThirdpayClient(
            "<your gateway url>",
            "<your merchant no>",
            ThirdpayConstants.RSA_TYPE_V1,
            "<your merchant private key>",
            "<pay platform public key>");
    // 创建API对应的request
    ThirdpayTradePagePayRequest request = new ThirdpayTradePagePayRequest();
    ThirdpayTradePagePayModel requestModel = new ThirdpayTradePagePayModel();
    requestModel.setOutTradeNo("12345678");
    requestModel.setProductCode("1");
    requestModel.setTotalAmount("100");
    requestModel.setSubject("订单主题");
    requestModel.setBody("订单描述");
    requestModel.setTimeoutExpress("1c");
    requestModel.setReturnUrl("<your return url>");
    requestModel.setNotifyUrl("<your notify url>");
    
    ThirdpayTradePagePayResponse response = null;
    try {
        response = client.execute(request);
    } catch (ThirdpayApiException e) {
        throw new RuntimeException(e);
    }
    if (!response.isSuccess()) {
        System.out.println("调用失败"
                + ", 响应码:" + response.getCode() + ", 响应信息:" + response.getMsg()
                + ", 响应子码:" + response.getSubCode() + ", 响应子信息:" + response.getSubMsg()
        );
    }
    // todo 使用response处理业务
```
##### 2.便捷方式
```
    Map bizContent = new HashMap<String, Object>();
    bizContent.put("out_trade_no", "12345678");
    bizContent.put("product_code", "1");
    bizContent.put("total_amount", "100");
    bizContent.put("subject", "订单主题");
    bizContent.put("body", "订单描述");
    bizContent.put("timeout_express", "1c");
    bizContent.put("return_url", "<your return url>");
    bizContent.put("notify_url", "<your notify url>");

    String response = Thirdpay.request("<server url>", "<your merchant id>", "thirdpay.trade.page.pay", "<private key for your merchant>", "<platform public key>", bizContent);
    
    // todo 使用response处理业务
```
## 贡献
欢迎提交代码，有问题可以建```issue```。

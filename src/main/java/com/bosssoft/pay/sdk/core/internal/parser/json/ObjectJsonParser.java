package com.bosssoft.pay.sdk.core.internal.parser.json;

import com.bosssoft.pay.sdk.core.IThirdpayParser;
import com.bosssoft.pay.sdk.core.ThirdpayResponse;
import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;
import com.bosssoft.pay.sdk.core.expose.ThirdpayResponseObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * @Title json解析器
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ObjectJsonParser<Response extends ThirdpayResponseObject> implements IThirdpayParser<Response> {

    private static ObjectMapper mapper = new ObjectMapper();

    private static ObjectMapper lazyMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Class<Response> clazz;

    public ObjectJsonParser(Class<Response> clazz) {
        this.clazz = clazz;
    }

    /**
     * 获取业务的响应类
     * @return
     */
    @Override
    public Class<Response> getResponseClass() {
        return this.clazz;
    }

    /**
     * 解析响应结果
     * @param rspBody
     * @return
     * @throws ThirdpayApiException
     */
    @Override
    public ThirdpayResponse parse(String rspBody) throws ThirdpayApiException {
        try {
            return lazyMapper.readValue(rspBody, ThirdpayResponse.class);
        } catch (Exception e) {
            throw new ThirdpayApiException("parse rspBody error, rspBody=" + rspBody, e);
        }
    }

    /**
     * 解析业务响应结果
     * @param bizRspBody
     * @return
     * @throws ThirdpayApiException
     */
    @Override
    public Response parseBizResponse(String bizRspBody) throws ThirdpayApiException {
        Response rsp = null;
        try {
            rsp = lazyMapper.readValue(bizRspBody, clazz);
        } catch (Exception e) {
            throw new ThirdpayApiException("parse responseBody error, bizRspBody=" + bizRspBody, e);
        }
        rsp.setBody(bizRspBody);
        return rsp;
    }


    /**
     * 转换为Map格式
     * @param content
     * @return
     * @throws ThirdpayApiException
     */
    @Override
    public Map<String, ?> parseContent2Map(String content) throws ThirdpayApiException {
        try {
            return new ObjectMapper().readValue(content, Map.class);
        } catch (IOException e) {
            throw new ThirdpayApiException("parse content to map error, content is " + content, e);
        }
    }

//    /**
//     * 解析(已废弃)
//     * @param responseBody
//     * @return
//     * @throws ThirdpayApiException
//     */
//    public Response parse(String responseBody) throws ThirdpayApiException {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode root;
//        try {
//            root = mapper.readTree(responseBody);
//        } catch (IOException e) {
//            throw new ThirdpayApiException("parse responseBody error, responseBody=" + responseBody, e);
//        }
//
//        Response rsp = null;
//        try {
//            rsp = clazz.newInstance();
//
//            for (Class<?> c : ThirdpayUtils.getSuperClassWithSelf(clazz)) {
//                Field[] fields = c.getDeclaredFields();
//                Map<String, Field> nodeName2FieldMap = new HashMap<String, Field>();
//                for (int i = 0; i < fields.length; i++) {
//                    Field field = fields[i];
//
//                    JsonProperty apiField = field.getAnnotation(JsonProperty.class);
//
//                    if (apiField != null) {
//                        nodeName2FieldMap.put(apiField.value(), field);
//                    }
//                }
//
//                Iterator<String> it = nodeName2FieldMap.keySet().iterator();
//                while (it.hasNext()) {
//                    String nodeName = it.next();
//                    if (root.has(nodeName)) {
//                        Field field = nodeName2FieldMap.get(nodeName);
//                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
//                        propertyDescriptor.getWriteMethod().invoke(rsp, parse(root.get(nodeName), field.getType(), field.getGenericType()));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new ThirdpayApiException("parse to ThirdpayResponse error, responseBody=" + responseBody, e);
//        }
//
//        rsp.setBody(responseBody);
//        return rsp;
//    }
//
//    private <C> C parse(JsonNode node, Class clazz, Type genericType) throws ThirdpayApiException {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNodeFactory factory = mapper.getNodeFactory();
//
//        if(String.class.isAssignableFrom(clazz)) {
//            return (C) node.asText(null);
//        } else if (Integer.class.isAssignableFrom(clazz)) {
//            return (C) Integer.valueOf(node.asInt());
//        } else if (Long.class.isAssignableFrom(clazz)) {
//            return (C) Long.valueOf(node.asLong());
//        } else if (Boolean.class.isAssignableFrom(clazz)) {
//            return (C) Boolean.valueOf(node.asBoolean());
//        } else if (Double.class.isAssignableFrom(clazz)) {
//            return (C) Double.valueOf(node.asDouble());
//        } else if (Date.class.isAssignableFrom(clazz)) {
//            return (C) DateUtil.parseDate(node.asText(), ThirdpayConstants.DATE_TIME_FORMAT);
//        } else if (List.class.isAssignableFrom(clazz)) {
//            ArrayList list = new ArrayList();
//            if(genericType instanceof ParameterizedType) {
//                ParameterizedType parameterizedType = (ParameterizedType) genericType;
//                Type[] genericTypes = parameterizedType.getActualTypeArguments();
//                if(genericTypes != null && genericTypes.length > 0 && genericTypes[0] instanceof Class) {
//                    Class itemClass = (Class) genericTypes[0];
//                    Type itemType = genericTypes[0];
//                    if (node.isArray()) {
//                        Iterator<JsonNode> it = node.elements();
//                        while (it.hasNext()) {
//                            JsonNode itemNode = it.next();
//                            list.add(parse(itemNode, itemClass, itemType));
//                        }
//                        return (C) list;
//                    }
//                }
//            }
//        } else if (Map.class.isAssignableFrom(clazz)) {
//            Map map = new HashMap();
//            if(genericType instanceof ParameterizedType) {
//                ParameterizedType parameterizedType = (ParameterizedType) genericType;
//                Type[] genericTypes = parameterizedType.getActualTypeArguments();
//                if(genericTypes != null  && genericTypes.length > 1
//                        && genericTypes[0] instanceof Class && genericTypes[1] instanceof Class) {
//                    Class itemClass0 = (Class) genericTypes[0];
//                    Class itemClass1 = (Class) genericTypes[0];
//                    Type itemType0 = genericTypes[0];
//                    Type itemType1 = genericTypes[1];
//                    if (node.isContainerNode()) {
//                        Iterator<String> it = node.fieldNames();
//
//                        while (it.hasNext()) {
//                            String fieldName = it.next();
//                            JsonNode itemNode = node.get(fieldName);
//                            TextNode keyNode = factory.textNode(fieldName);
//                            map.put(parse(keyNode, itemClass0, itemType0), parse(itemNode, itemClass1, itemType1));
//                        }
//                        return (C) map;
//                    }
//                }
//            }
//        } else {
//            throw new ThirdpayApiException("parse json error, does not support type:" + clazz.toString());
//        }
//        return null;
//    }
}
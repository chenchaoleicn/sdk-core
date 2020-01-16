package com.bosssoft.pay.sdk.core.internal.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * @Title json工具类(基于jackson)
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final ObjectMapper mapperWithIndentOutput = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static final ObjectMapper mapperWithLazy = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final ObjectMapper mapperWithIgnoreNull = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final ObjectMapper mapperWithIgnoreEmpty = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    /**
     * 对象转换为json字符串(序列化)
     * @param value
     * @return
     */
    public static String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为json字符串(序列化)(缩进格式)
     * @param value
     * @return
     */
    public static String writeValueAsStringWithIndentOutput(Object value) {
        try {
            return mapperWithIndentOutput.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为json字符串(序列化)(忽略null值)
     * @param value
     * @return
     */
    public static String writeValueAsStringWithIgnoreNull(Object value) {
        try {
            return mapperWithIgnoreNull.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为json字符串(序列化)(忽略null或""值)
     * @param value
     * @return
     */
    public static String writeValueAsStringWithIgnoreEmpty(Object value) {
        try {
            return mapperWithIgnoreEmpty.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转换为对象(反序列化)
     * @param value
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T readValue(String value, Class<T> valueType) {
        try {
            return mapperWithLazy.readValue(value, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换
     * @param value
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T convertValue(Object value, Class<T> valueType) {
        return mapperWithLazy.convertValue(value, valueType);
    }
}

package com.dzx.seckill.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @description: json工具类
 * @author: dzx
 * @date: 2022/3/8
 */


public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonToObject(String jsonStr,Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr.getBytes(StandardCharsets.UTF_8), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换为List<beanType>
     * @param jsonStr
     * @param beanType
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> beanType) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

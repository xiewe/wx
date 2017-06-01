package com.framework.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JsonAndObjectUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T getObject(String jsonStr, Class<T> clazz) {
        try {
            // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            T o = mapper.readValue(jsonStr, clazz);

            return o;

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> getListObject(String jsonStr, Class<T> clazz) {
        try {
            // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
            List<T> list = mapper.readValue(jsonStr, javaType);

            return list;

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> Map<String, T> getMapObject(String jsonStr, Class<T> clazz) {
        try {
            // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            JavaType javaType = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, clazz);
            Map<String, T> map = mapper.readValue(jsonStr, javaType);

            return map;

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> String getJson(Object o, Class<T> clazz, String... propertyes) {
        try {

            mapper.setSerializationInclusion(Include.NON_NULL);

            // 动态过滤JSON
            mapper.setFilters(filter("objectFilter", false, propertyes));
            mapper.addMixInAnnotations(clazz, ObjectFilterMixIn.class);

            return mapper.writeValueAsString(o);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @JsonFilter("objectFilter")
    public interface ObjectFilterMixIn {
    }

	/**
	 * 动态过滤JSON
	 * 
	 * @param filterName
	 * @param isOutAllExcept
	 *            true:想要的字段,false 不想要的字段
	 * @param propertyes
	 * @return
	 */
    public static FilterProvider filter(String filterName, boolean isOutAllExcept, String... propertyes) {
        if (isOutAllExcept) {
            // 过滤想要的
            FilterProvider filter = new SimpleFilterProvider().addFilter(filterName,
                    SimpleBeanPropertyFilter.filterOutAllExcept(propertyes));
            return filter;
        } else {
            // 过滤不想要的
            FilterProvider filter = new SimpleFilterProvider().addFilter(filterName,
                    SimpleBeanPropertyFilter.serializeAllExcept(propertyes));
            return filter;
        }
    }
}

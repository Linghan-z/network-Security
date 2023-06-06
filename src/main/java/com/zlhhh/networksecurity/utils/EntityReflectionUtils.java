package com.zlhhh.networksecurity.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityReflectionUtils {

    public static Map<String, Object> getNonNullProperties(Object object, List<String> propertyNames) throws IllegalAccessException {
        Map<String, Object> result = new HashMap<>();

        Class<?> aClass = object.getClass();
        for (String propertyName : propertyNames) {
            Field field;
            try {
                field = aClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException ex) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null) {
                result.put(propertyName, value);
            }
        }

        return result;
    }
}
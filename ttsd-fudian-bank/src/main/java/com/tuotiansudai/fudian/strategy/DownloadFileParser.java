package com.tuotiansudai.fudian.strategy;


import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DownloadFileParser {

    public static <T> List<T> parse(Class<T> dto, ArrayList<String> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return null;
        }
        List<T> list = new ArrayList<>();
        try {
            Constructor constructor = dto.getConstructor(String.class);
            for (String params : paramsList) {
                list.add((T) constructor.newInstance(params));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ignored) {
        }
        return list;
    }
}

package com.tuotiansudai.fudian.strategy;


import org.springframework.util.CollectionUtils;

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
            Method method = dto.getMethod("match", String.class);
            for (String params : paramsList){
                list.add((T) method.invoke(dto, params));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return list;
    }

}

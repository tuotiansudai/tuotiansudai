package com.tuotiansudai.fudian.strategy;


import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DownloadFileParser {

    public static <T> List<T> parse(Class<T> dto, ArrayList<String> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return null;
        }



        List<T> list = new ArrayList<>();
        try {
            Constructor constructor = dto.getConstructor(String.class);

            Method method = dto.getMethod("match", Integer.class);
            for (String params : paramsList) {
                Map<String, String> maps = new HashMap<>();
                String[] param = params.split("\\|");
                IntStream.range(0, param.length).collect(Collectors.toMap(i -> i, i->param[i]));

                list.add((T) constructor.newInstance(params));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ignored) {
        }
        return list;
    }
}

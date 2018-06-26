package com.tuotiansudai.fudian.strategy;


import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DownloadFileParser {

    @SuppressWarnings(value = "unchecked")
    public static <T> List<T> parse(Class<T> dto, ArrayList<String> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return null;
        }
        List<T> list = new ArrayList<>();
        try {
            Method method = dto.getMethod("match", null);
            Map<Integer, String> match = (Map<Integer, String>) method.invoke(dto, null);
            for (String params : paramsList) {
                String[] param = params.split("\\|");
                Map<String, String> map = IntStream.range(0, param.length)
                        .boxed()
                        .collect(Collectors.toMap(index -> match.get(index), index -> param[index]));
                list.add(new ObjectMapper().convertValue(map, dto));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return list;
    }
}

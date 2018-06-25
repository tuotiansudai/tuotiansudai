package com.tuotiansudai.fudian.strategy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadFileParser {

    private static Logger logger = LoggerFactory.getLogger(DownloadFileParser.class);


    public static <T> void parse(List<T> dtos, ArrayList<String> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }

//        try {
//            Method method = dtos.getClass().getMethod("get", null);
//            Class TClass = method.getReturnType();
//            Method matchMethod = TClass.getMethod("match", Map.class);
//            paramsList.forEach(i -> {
//                String[] params = i.split("\\|");
//                try {
//                    matchMethod.invoke(TClass, (Object) params);
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            });
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

}

package com.tuotiansudai.api.util;

import com.google.common.collect.ImmutableMap;

import java.util.Iterator;
import java.util.Map;


public class DistrictUtil {
    private static final ImmutableMap<String, String> PROVINCES = ImmutableMap.<String, String>builder()
            .put("11", "北京")
            .put("12", "天津")
            .put("13", "河北")
            .put("14", "山西")
            .put("15", "内蒙古")
            .put("21", "辽宁")
            .put("22", "吉林")
            .put("23", "黑龙江")
            .put("31", "上海")
            .put("32", "江苏")
            .put("33", "浙江")
            .put("34", "安徽")
            .put("35", "福建")
            .put("36", "江西")
            .put("37", "山东")
            .put("41", "河南")
            .put("42", "湖北")
            .put("43", "湖南")
            .put("44", "广东")
            .put("45", "广西")
            .put("46", "海南")
            .put("50", "重庆")
            .put("51", "四川")
            .put("52", "贵州")
            .put("53", "云南")
            .put("54", "西藏")
            .put("61", "陕西")
            .put("62", "甘肃")
            .put("63", "青海")
            .put("64", "宁夏")
            .put("65", "新疆")
            .put("71", "台湾")
            .put("81", "香港")
            .put("82", "澳门").build();

    public static ImmutableMap<String, String> getProvinces() {
        return PROVINCES;
    }

    public static String convertCodeToName(String code) {
        return PROVINCES.get(code);
    }

    public static String convertNameToCode(String desc) {
        Iterator<Map.Entry<String, String>> iterator = PROVINCES.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (desc.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}

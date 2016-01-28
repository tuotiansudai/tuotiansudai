package com.tuotiansudai.util;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class DistrictUtil {
    private static final LinkedHashMap<String, String> PROVINCES = new LinkedHashMap<String,String>();

    static{
        PROVINCES.put("11", "北京");
        PROVINCES.put("12", "天津");
        PROVINCES.put("31", "上海");
        PROVINCES.put("50", "重庆");
        PROVINCES.put("13", "河北");
        PROVINCES.put("14", "山西");
        PROVINCES.put("15", "内蒙古");
        PROVINCES.put("21", "辽宁");
        PROVINCES.put("22", "吉林");
        PROVINCES.put("23", "黑龙江");
        PROVINCES.put("32", "江苏");
        PROVINCES.put("33", "浙江");
        PROVINCES.put("34", "安徽");
        PROVINCES.put("35", "福建");
        PROVINCES.put("36", "江西");
        PROVINCES.put("37", "山东");
        PROVINCES.put("41", "河南");
        PROVINCES.put("42", "湖北");
        PROVINCES.put("43", "湖南");
        PROVINCES.put("44", "广东");
        PROVINCES.put("45", "广西");
        PROVINCES.put("46", "海南");
        PROVINCES.put("51", "四川");
        PROVINCES.put("52", "贵州");
        PROVINCES.put("53", "云南");
        PROVINCES.put("54", "西藏");
        PROVINCES.put("61", "陕西");
        PROVINCES.put("62", "甘肃");
        PROVINCES.put("63", "青海");
        PROVINCES.put("64", "宁夏");
        PROVINCES.put("65", "新疆");
        PROVINCES.put("71", "台湾");
        PROVINCES.put("81", "香港");
        PROVINCES.put("82", "澳门");
    }


    public static LinkedHashMap<String, String> getProvinces() {
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

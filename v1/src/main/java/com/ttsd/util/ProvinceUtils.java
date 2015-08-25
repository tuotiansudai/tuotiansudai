package com.ttsd.util;

import java.util.HashMap;
import java.util.Map;

public class ProvinceUtils {
    private static String DEFAULT_AREA_NAME = "中国";
    private static Map<String, String[]> AREA_MAP;
    private static Map<String, String> PROVINCE_AREA;

    static {
        // 华北
        String[] CHINA_NORTHS = new String[]{"北京", "天津", "河北", "山西", "内蒙古"};
        // 东北
        String[] CHINA_NORTHEASTS = new String[]{"辽宁", "吉林", "黑龙江"};
        // 华东
        String[] CHINA_EASTS = new String[]{"上海", "江苏", "浙江", "安徽", "福建", "江西", "山东"};
        // 中南
        String[] CHINA_SOUTHS = new String[]{"河南", "湖北", "湖南", "广东", "广西", "海南"};
        // 西南
        String[] CHINA_SOUTHWESTS = new String[]{"重庆", "四川", "贵州", "云南", "西藏"};
        // 西北
        String[] CHINA_NORTHWESTS = new String[]{"陕西", "甘肃", "青海", "宁夏", "新疆"};

        AREA_MAP = new HashMap<>(6);
        AREA_MAP.put("华北", CHINA_NORTHS);
        AREA_MAP.put("东北", CHINA_NORTHEASTS);
        AREA_MAP.put("华东", CHINA_EASTS);
        AREA_MAP.put("中南", CHINA_SOUTHS);
        AREA_MAP.put("西南", CHINA_SOUTHWESTS);
        AREA_MAP.put("西北", CHINA_NORTHWESTS);

        PROVINCE_AREA = new HashMap<>();
        for (String area : AREA_MAP.keySet()) {
            String[] provinces = AREA_MAP.get(area);
            for (String province : provinces) {
                PROVINCE_AREA.put(province, area);
            }
        }
    }

    public static String getAreaByProvince(String province) {
        if (PROVINCE_AREA.containsKey(province)) {
            return PROVINCE_AREA.get(province);
        } else {
            return DEFAULT_AREA_NAME;
        }
    }
}

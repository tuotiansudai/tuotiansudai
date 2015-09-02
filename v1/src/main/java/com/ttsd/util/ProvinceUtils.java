package com.ttsd.util;

import java.util.HashMap;
import java.util.Map;

public class ProvinceUtils {
    private static Map<ChinaArea, String[]> AREA_MAP;
    private static Map<String, ChinaArea> PROVINCE_AREA;

    static {
        // 华北
        String[] CHINA_NORTHS = new String[]{"北京", "天津", "河北", "山西", "内蒙古"};
        // 东北
        String[] CHINA_NORTHEASTS = new String[]{"辽宁", "吉林", "黑龙江"};
        // 华东
        String[] CHINA_EASTS = new String[]{"上海", "江苏", "浙江", "安徽", "福建", "江西", "山东"};
        // 西北
        String[] CHINA_NORTHWESTS = new String[]{"陕西", "甘肃", "青海", "宁夏", "新疆"};
        // 华南
        String[] CHINA_SOUTH = new String[]{"河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏"};
        // 中南
        // String[] CHINA_SOUTHMIDDLES = new String[]{"河南", "湖北", "湖南", "广东", "广西", "海南"};
        // 西南
        // String[] CHINA_SOUTHWESTS = new String[]{"重庆", "四川", "贵州", "云南", "西藏"};

        AREA_MAP = new HashMap<>(6);
        // 华北
        AREA_MAP.put(ChinaArea.CHINA_NORTH, CHINA_NORTHS);
        // 东北
        AREA_MAP.put(ChinaArea.CHINA_NORTHEAST, CHINA_NORTHEASTS);
        // 西北
        AREA_MAP.put(ChinaArea.CHINA_NORTHWEST, CHINA_NORTHWESTS);
        // 华东
        AREA_MAP.put(ChinaArea.CHINA_EAST, CHINA_EASTS);
        // 华南
        AREA_MAP.put(ChinaArea.CHINA_SOUTH, CHINA_SOUTH);
        // 中南
        // AREA_MAP.put(ChinaArea.CHINA_SOUTHMIDDLE, CHINA_SOUTHMIDDLES);
        // 西南
        // AREA_MAP.put(ChinaArea.CHINA_SOUTHWEST, CHINA_SOUTHWESTS);

        PROVINCE_AREA = new HashMap<>();
        for (ChinaArea area : AREA_MAP.keySet()) {
            String[] provinces = AREA_MAP.get(area);
            for (String province : provinces) {
                PROVINCE_AREA.put(province, area);
            }
        }
    }

    public static ChinaArea getAreaByProvince(String province) {
        if (PROVINCE_AREA.containsKey(province)) {
            return PROVINCE_AREA.get(province);
        } else {
            return ChinaArea.CHINA;
        }
    }
}

package com.ttsd.api.util;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CommonUtils {

    public static String encryptUserName(String userName) {

        if (userName.length() > 3) {

            userName = userName.substring(0, 3) + "***";

        } else {

            userName += "***";

        }
        return userName;
    }

    public static String convertRealMoneyByType(double money, String type) {
        if ("ti_balance".equals(type)){
            return "+" + money;
        }else if("to_balance".equals(type) || "to_frozen".equals(type) ) {
            return "-" + money;
        }
        return "" + money;
    }

    public static String mapToFormData(Map<String,String> map){
        String formData = "";
        if (map != null && map.size()>0){
            Set<String> set = map.keySet();
            Iterator iterator = set.iterator();
            StringBuffer stringBuffer = new StringBuffer();
            while (iterator.hasNext()){
                String mapKey = (String) iterator.next();
                String mapValue = map.get(mapKey);
                stringBuffer.append(mapKey + "=" + mapValue + "&");
                iterator.next();
            }
            formData = stringBuffer.toString();
            formData.substring(0,formData.length()-1);
        }
        return formData;
    }
}

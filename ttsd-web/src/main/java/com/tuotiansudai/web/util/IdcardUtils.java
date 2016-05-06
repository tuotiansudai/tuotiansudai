
package com.tuotiansudai.web.util;


import org.apache.commons.lang.StringUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdcardUtils extends StringUtils {

    /*** 中国公民身份证号码最小长度。*/
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /*** 中国公民身份证号码最大长度。*/
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /** 第18位校检码 */
    private static String[] ValCodeArr = {
            "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
    /** 每位加权因子 */
    private static String[] Wi = {
            "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};

    public static boolean validateIdentity(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH && idCard.length() != CHINA_ID_MAX_LENGTH) {
            return false;
        }
        String code17 = getIdCode17(idCard);
        if (isNumeric(code17) == false) {
            return false;
        }
        if(!validateTime(code17)){
            return false;
        }
        if(!compareIdCard(idCard,countCode18(code17))){
            return false;
        }
        return true;
    }

    private static String getIdCode17(String idCard){
        if (idCard.length() == CHINA_ID_MAX_LENGTH) {
            return idCard.substring(0, 17);
        } else if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            return idCard.substring(0, 6) + "19" + idCard.substring(6, CHINA_ID_MIN_LENGTH);
        }
        return "";
    }

    private static boolean compareIdCard(String idCatd,String countIdCard){
        if (idCatd.length() == 18) {
            if (!countIdCard.equals(idCatd)) {
                return false;
            }
        }
        return true;
    }

    private static String countCode18(String code17){
        int iSum = 0;
        for (int i = 0; i < 17; i++) {
            iSum = iSum
                    + Integer.parseInt(String.valueOf(code17.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = iSum % 11;
        String strVerifyCode = ValCodeArr[modValue];
        return code17 + strVerifyCode;
    }

    private static boolean validateTime(String idCard){
        String strYear = idCard.substring(6, 10);
        String strMonth = idCard.substring(10, 12);
        String strDay = idCard.substring(12, 14);
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            return false;
        }
        if (Integer.parseInt(strYear) < 1900 || Integer.parseInt(strYear) == 0) {
            return false;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
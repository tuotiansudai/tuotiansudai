package com.tuotiansudai.util;


import com.google.common.base.Strings;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IdentityNumberValidator {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /***
     * 中国公民身份证号码最小长度。
     */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度。
     */
    public static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 第18位校检码
     */
    private static String[] ValCodeArr = {
            "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
    /**
     * 每位加权因子
     */
    private static String[] Wi = {
            "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};

    public static boolean validateIdentity(String idCard) {
        if (idCard.length() != CHINA_ID_MIN_LENGTH && idCard.length() != CHINA_ID_MAX_LENGTH) {
            return false;
        }
        if (!validateFistNumber(idCard)) {
            return false;
        }
        String code17 = getIdCode17(idCard);
        return isNumeric(code17) && validateTime(code17) && compareIdCard(idCard, countCode18(code17)) && validateYearByAdult(code17);

    }

    public static boolean isNumeric(String val) {
        return !Strings.isNullOrEmpty(val) && val.matches("^[0-9]*$");
    }

    private static boolean validateFistNumber(String idCard){
        return Integer.parseInt(idCard.substring(0, 1)) > 0 && Integer.parseInt(idCard.substring(0, 1)) < 9;
    }

    private static String getIdCode17(String idCard) {
        if (idCard.length() == CHINA_ID_MAX_LENGTH) {
            return idCard.substring(0, 17);
        } else if (idCard.length() == CHINA_ID_MIN_LENGTH) {
            return convert15CardTo18(idCard).substring(0, 17);
        }
        return null;
    }

    private static boolean compareIdCard(String idCard, String countIdCard) {
        if (idCard.length() == 18) {
            if (!countIdCard.equalsIgnoreCase(idCard)) {
                return false;
            }
        }
        return true;
    }

    private static String countCode18(String code17) {
        int iSum = 0;
        for (int i = 0; i < 17; i++) {
            iSum = iSum
                    + Integer.parseInt(String.valueOf(code17.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        return code17 + ValCodeArr[iSum % 11];
    }

    private static boolean validateTime(String idCard) {
        String strYear = idCard.substring(6, 10);
        if (Integer.parseInt(strYear) < 1900 || Integer.parseInt(strYear) == 0) {
            return false;
        }
        String strMonth = idCard.substring(10, 12);
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        String strDay = idCard.substring(12, 14);
        return !(Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0);
    }

    private static boolean validateYearByAdult(String idCard){
        return ((Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(idCard.substring(6, 14))) / 10000 >= 18);
    }

    private static String convert15CardTo18(String idCard) {
        String birthday = idCard.substring(6, 12);
        Date birthDate = null;
        try {
            birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            Calendar cal = Calendar.getInstance();
            if (birthDate != null)
                cal.setTime(birthDate);
            String sYear = String.valueOf(cal.get(Calendar.YEAR));
            String code18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
            return code18 + countCode18(code18);
        }
    }

    /**
     * 根据18位身份证 和15位身份证号 获取年龄
     *
     * @param cardNumber
     * @param defaultValue
     * @return
     */
    public static int getAgeByIdentityCard(String cardNumber, int defaultValue) {
        if (cardNumber == null) {
            return defaultValue;
        }
        int length = cardNumber.trim().length();
        Calendar calendar = Calendar.getInstance();
        if (length == 18) {
            String bornYear = cardNumber.substring(6, 10);
            return calendar.get(Calendar.YEAR) - Integer.parseInt(bornYear);
        }
        if (length == 15) {
            String bornYear = "19" + cardNumber.substring(6, 8);
            return calendar.get(Calendar.YEAR) - Integer.parseInt(bornYear);
        }
        return defaultValue;
    }

    /**
     * 根据18位和15位身份证号获取性别
     *
     * @param cardNumber
     * @param defaultValue
     * @return
     */
    public static String getSexByIdentityCard(String cardNumber, String defaultValue) {
        if (cardNumber == null) {
            return defaultValue;
        }
        int length = cardNumber.trim().length();
        if (length == 18) {
            String sexNum = cardNumber.substring(16, 17);
            return Integer.parseInt(sexNum) % 2 == 0 ? "FEMALE" : "MALE";
        }
        if (length == 15) {
            String sexNum = cardNumber.substring(14);
            return Integer.parseInt(sexNum) % 2 == 0 ? "FEMALE" : "MALE";
        }
        return defaultValue;
    }
}
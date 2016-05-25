
package com.tuotiansudai.web.util;


import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IdentityNumberValidator {

    static Logger logger = Logger.getLogger(IdentityNumberValidator.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /***
     * 中国公民身份证号码最小长度。
     */
    public static final int CHINA_ID_MIN_LENGTH = 15;

    /***
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
        if(!validateFistNumber(idCard)){
            return false;
        }
        String code17 = getIdCode17(idCard);
        if (isNumeric(code17) == false) {
            return false;
        }
        if (!validateTime(code17)) {
            return false;
        }
        if (!compareIdCard(idCard, countCode18(code17))) {
            return false;
        }

        if(!validateYearByAdult(code17)){
            return false;
        }

        return true;
    }

    public static boolean isNumeric(String val) {
        return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
    }

    private static boolean validateFistNumber(String idCard){
        if(Integer.parseInt(idCard.substring(0, 1)) > 0 && Integer.parseInt(idCard.substring(0, 1)) < 9){
            return true;
        }
        return false;
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
            if (!countIdCard.equals(idCard)) {
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
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }
        return true;
    }

    private static boolean validateYearByAdult(String idCard){
        if(((Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(idCard.substring(6,14))) / 10000 >= 18)){
            return true;
        }
        return false;
    }

    private static String convert15CardTo18(String idCard) {
        String birthday = idCard.substring(6, 12);
        Date birthDate = null;
        try {
            birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        Calendar cal = Calendar.getInstance();
        if (birthDate != null)
            cal.setTime(birthDate);
        String sYear = String.valueOf(cal.get(Calendar.YEAR));
        String code18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
        return code18 + countCode18(code18);
    }
}
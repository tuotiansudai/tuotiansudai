package com.tuotiansudai.util;

import com.google.common.base.Strings;
import org.joda.time.DateTime;

import java.text.MessageFormat;

public class UserBirthdayUtil {

    public static boolean isBirthMonth(String identityNumber) {
        if (Strings.isNullOrEmpty(identityNumber)) {
            return false;
        }

        int birthMonth = Integer.parseInt(identityNumber.length() == 18 ? identityNumber.substring(10, 12) : identityNumber.substring(8, 10));

        int monthOfYear = new DateTime().getMonthOfYear();

        return monthOfYear == birthMonth;
    }

    public static DateTime getUserBirthday(String identityNumber) {
        if (Strings.isNullOrEmpty(identityNumber)) {
            return null;
        }

        return identityNumber.length() == 18 ?
                new DateTime().withDate(Integer.parseInt(identityNumber.substring(6, 10)),
                        Integer.parseInt(identityNumber.substring(10, 12)),
                        Integer.parseInt(identityNumber.substring(12, 14))).withTimeAtStartOfDay() :
                new DateTime().withDate(Integer.parseInt(MessageFormat.format("19{0}", identityNumber.substring(6, 8))),
                        Integer.parseInt(identityNumber.substring(8, 10)),
                        Integer.parseInt(identityNumber.substring(10, 12))).withTimeAtStartOfDay();
    }
}

package com.tuotiansudai.util;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountConverter {
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String[] RADICES = {"", "拾", "佰", "仟"};
    private static final String[] BIG_RADICES = {"", "万", "亿", "兆"};

    final static private Pattern pattern = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    public static long convertStringToCent(String amount) {
        if (amount == null || amount.length() == 0) {
            return 0;
        }
        Matcher matcher = pattern.matcher(amount);
        if (matcher.matches()) {
            String[] split = amount.split("\\.");
            long integer = Long.parseLong(split[0]);
            int fraction = 0;
            if (split.length > 1) {
                String fractionString = split[1];
                fraction = fractionString.length() == 1 ? Integer.parseInt(fractionString) * 10 : Integer.parseInt(fractionString);
            }
            return integer * 100 + fraction;
        }
        return 0;
    }

    public static String convertCentToString(long amount) {
        return String.format("%.2f", amount / 100D);
    }

    /**
     * 获取大写的人名币的金额，单位精确到分
     *
     * @param money 人民币，单位：分
     * @return 人民币大写的金额
     */
    public static String getRMBStr(long money) {
        StringBuilder result = new StringBuilder("");
        if (money == 0) {
            return "零元整";
        }
        long integral = money / 100;
        int integralLen = (integral + "").length();
        int decimal = (int) (money % 100);
        if (integral > 0) {
            int zeroCount = 0;
            for (int i = 0; i < integralLen; i++) {
                int unitLen = integralLen - i - 1;
                int d = Integer.parseInt((integral + "").substring(i, i + 1));
                int quotient = unitLen / 4;
                int modulus = unitLen % 4;
                if (d == 0) {
                    zeroCount++;
                } else {
                    if (zeroCount > 0) {
                        result.append(CN_UPPER_NUMBER[0]);
                    }
                    zeroCount = 0;
                    result.append(CN_UPPER_NUMBER[d]).append(RADICES[modulus]);
                }
                if (modulus == 0 && zeroCount < 4) {
                    result.append(BIG_RADICES[quotient]);
                }
            }
            result.append("元");
        }
        if (decimal > 0) {
            int j = decimal / 10;
            if (j > 0) {
                result.append(CN_UPPER_NUMBER[j]).append("角");
            }
            j = decimal % 10;
            if (j > 0) {
                result.append(CN_UPPER_NUMBER[j]).append("分");
            }
        }
        return result.toString();
    }


}

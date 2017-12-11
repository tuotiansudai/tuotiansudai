package com.tuotiansudai.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class BankCardUtil {

    private static final Map<String, String> BANK_CODE_MAPPING = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("ICBC", "中国工商银行")
            .put("CMB", "招商银行")
            .put("ABC", "中国农业银行")
            .put("CCB", "中国建设银行")
            .put("CMBC", "中国民生银行")
            .put("SPDB", "浦东发展银行")
            .put("GDB", "广发银行")
            .put("HXB", "华夏银行")
            .put("PSBC", "中国邮政储蓄银行")
            .put("BOC", "中国银行")
            .put("CEB", "中国光大银行")
            .put("BEA", "东亚银行")
            .put("CIB", "兴业银行")
            .put("COMM", "交通银行")
            .put("CITIC", "中信银行")
            .put("BJB", "北京银行")
            .put("CZSB", "浙商银行")
            .put("SPAB", "平安银行")
            .put("BHB", "河北银行")
            .put("LZB", "兰州银行")
            .put("NBB", "宁波银行")
            .build());

    private static final List<String> RECHARGE_BANKS = Lists.newArrayList("ICBC", "CMB", "ABC", "CCB",
            "SPDB", "GDB", "HXB", "PSBC", "BOC",
            "CEB", "BEA", "COMM", "CITIC", "BJB");


    private static final List<String> WITHDRAW_BANKS = Lists.newArrayList("BOC", "ABC", "CCB", "COMM", "PSBC", "CMBC",
            "CITIC", "CEB", "HXB", "GDB", "CIB", "SPDB",
            "CZSB", "SPAB", "BHB", "LZB", "CMB", "ICBC", "NBB");

    private static final List<String> FAST_PAY_BANKS = Lists.newArrayList("ICBC", "ABC", "CCB", "BOC", "CEB",
            "CIB", "SPDB", "COMM", "GDB", "CITIC", "CMB", "SPAB");

    public static List<String> getRechargeBanks() {
        return RECHARGE_BANKS;
    }

    public static List<String> getWithdrawBanks() {
        return WITHDRAW_BANKS;
    }

    public static List<String> getFastPayBanks() {
        return FAST_PAY_BANKS;
    }

    public static String getBankName(String bankCode) {
        return BANK_CODE_MAPPING.get(bankCode);
    }

    public static boolean canEnableFastPay(String bankCode) {
        return FAST_PAY_BANKS.contains(bankCode);
    }

}
package com.tuotiansudai.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
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
            .put("BJBANK", "北京银行")
            .put("SHRCB", "上海农商银行")
            .put("WZCB", "温州银行")
            .put("CZSB", "浙商银行")
            .put("SPAB", "平安银行")
            .put("BHB", "河北银行")
            .put("LZB", "兰州银行")
            .build());

    private static final List<String> RECHARGE_BANKS = Lists.newArrayList("ICBC", "CMB", "ABC", "CCB", "CMBC", "SPDB",
            "GDB", "HXB", "PSBC", "BOC", "CEB", "BEA",
            "CIB", "COMM", "CITIC", "BJBANK", "SHRCB", "WZCB");


    private static final List<String> WITHDRAW_BANKS = Lists.newArrayList("BOC", "ABC", "CCB", "COMM", "PSBC", "CMBC",
            "CITIC", "CEB", "HXB", "GDB", "CIB", "SPDB",
            "CZSB", "SPAB", "BHB", "LZB", "CMB", "ICBC");

    private static final List<String> FAST_PAY_BANKS = Lists.newArrayList("ICBC", "ABC", "CCB", "HXB", "BOC",
            "PSBC", "SPDB", "COMM", "CMBC", "GDB",
            "CITIC", "CIB", "CEB", "CMB", "SPAB");


    public static List<String> getRechargeBanks() {
        return RECHARGE_BANKS;
    }

    public static List<String> getWithdrawBanks() {
        return WITHDRAW_BANKS;
    }

    public static String getBankName(String bankCode) {
        return BANK_CODE_MAPPING.get(bankCode);
    }

}



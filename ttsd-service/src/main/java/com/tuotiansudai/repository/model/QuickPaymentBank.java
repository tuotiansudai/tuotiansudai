package com.tuotiansudai.repository.model;

public enum QuickPaymentBank {
    ICBC("中国工商银行"),
    ABC("中国农业银行"),
    CCB("中国建设银行"),
    BOC("中国银行"),
    CEB("光大银行"),
    CIB("兴业银行"),
    HXB("华夏银行"),
    PSBC("中国邮政储蓄银行"),
    SPDB("浦发银行"),
    COMM("交通银行"),
    GDB("广发银行"),
    CITIC("中信银行"),
    CMB("招商银行"),
    PAB("平安银行"),
    CMBC("中国民生银行");

    private final String description;

    public String getDescription() {
        return description;
    }

    QuickPaymentBank(String description) {
        this.description = description;
    }

    public static boolean isQuickPaymentBank(String bankNumber){

        for (QuickPaymentBank key : QuickPaymentBank.values()){
            if(bankNumber.equals(key.name())){
                return true;
            }
        }
       return false;
    }

}

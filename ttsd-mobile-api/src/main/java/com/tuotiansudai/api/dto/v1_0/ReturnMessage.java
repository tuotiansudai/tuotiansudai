package com.tuotiansudai.api.dto.v1_0;

public enum ReturnMessage {
    SUCCESS("0000",""),
    FAIL("1234", "系统繁忙"),
    /******手机端注册返回码******/
    MOBILE_NUMBER_IS_NULL("0001", "手机号码为空"),
    MOBILE_NUMBER_IS_INVALID("0002","手机号码不合法"),
    MOBILE_NUMBER_IS_EXIST("0003","手机号已经存在"),
    SEND_SMS_IS_FAIL("0004","短信验证码发送失败"),
    USER_NAME_IS_NULL("0005","用户名为空"),
    USER_NAME_IS_INVALID("0006","用户名必须为5至25位字母和数字组合"),
    USER_NAME_IS_EXIST("0007","用户名已经存在"),
    REFERRER_IS_NOT_EXIST("0008","推荐人不存在"),
    SMS_CAPTCHA_ERROR("0009","短信验证码不正确"),
    SMS_CAPTCHA_IS_OVERDUE("0010","短信验证码已经过期"),
    USER_IS_ACTIVE("0011","用户已经被激活"),
    PASSWORD_IS_INVALID("0012","密码是数字和字母的组合，长度为6-20位"),
    USER_NAME_OR_PASSWORD_IS_INVALID("0018","用户名或密码不符合规则"),
    LOGIN_FAILED("0019","用户名或密码错误,还可以再试{0}次"),
    /******手机端注册返回码******/

    /******手机端实名认证返回码******/
    ID_CARD_IS_NULL("0013","身份号码为空"),
    REAL_NAME_IS_NULL("0014","真实姓名为空"),
    ID_CARD_IS_EXIST("0015","身份证号已被实名认证"),
    CERTIFICATION_FAIL("0016","实名认证失败"),
    USER_ID_IS_NULL("0017","用户ID为空"),
    USER_ID_NOT_EXIST("0018","用户不存在"),
    /******手机端实名认证返回码******/

    /******手机端找回密码返回码******/
    SMS_CAPTCHA_IS_NULL("0020","验证码为空"),
    MOBILE_NUMBER_NOT_EXIST("0021","没有该手机号的用户"),
    SMS_CAPTCHA_TYPE_IS_NULL("0022","验证码类型不能为空"),
    /******手机端找回密码返回码******/

    /******手机端投资列表******/
    REQUEST_PARAM_IS_WRONG("0023","请求参数错误"),
    /******手机端投资列表******/

    /******手机端标的详情******/
    LOAN_NOT_FOUND("0024","标的不存在"),
    /******手机端标的详情******/

    /******手机端投资******/
    INVESTOR_SAME_TO_LOANER("0025","你不能投自己的项目"),
    INVEST_PASSWORD_IS_WRONG("0029","投资密码不匹配"),
    INVEST_PASSWORD_IS_NULL("0030","投资密码为空"),
    UMPAY_INVEST_MESSAGE_INVALID("0031","请求第三方加密失败"),
    INSUFFICIENT_BALANCE("0032","账户余额不足，请充值"),
    EXCEED_MONEY_NEED_RAISED("0033","标的可投金额不足"),
    UNREACHED_MONEY_LIMIT_EXCETPTION("0034","投资金额未到达优惠券使用条件"),
    ILLEGAL_LOAN_STATUS("0035","当前借款不可投资"),
    NO_MATCHING_OBJECTS_EXCEPTION("0036","投资失败"),
    EXCEED_DEAD_LINE_EXCEPTION("0037","优惠券已过期"),
    USER_IS_NOT_CERTIFICATED("0038","您尚未进行实名认证"),
    INVEST_CAN_NOT_BE_FOUND("0040","查找不到投资详情"),
    NOT_ENOUGH_BALANCE("0045","账户余额不足"),
    NO_MATCH_XS_INVEST_CONDITION("0050","不符合新手标投资条件"),
    ILLEGAL_INVEST_AMOUNT("0051","投资金额不符合递增金额要求"),
    MORE_THAN_MAX_INVEST_AMOUNT("0052","投资金额超过了用户投资限额"),
    LOAN_IS_FULL("0053","标的已满"),
    LESS_THAN_MIN_INVEST_AMOUNT("0054","投资金额小于标的最小投资金额"),
    OUT_OF_NOVICE_INVEST_LIMIT("0055","您已购买过新手专享产品"),
    INVEST_FAILED("0056","投资失败"),
    APPLICATION_IS_HIS_OWN("0057","您不能接手自己的债权"),
    /******手机端投资******/


    /******绑卡失败******/
    BIND_CARD_FAIL("0026","绑卡失败"),
    BIND_CARD_LIMIT_FAIL("0203","暂不支持该银行卡"),
    /******绑卡失败******/

    /******签约失败******/
    BANK_CARD_SIGN_FAIL("0027","签约失败"),
    NOT_BIND_CARD("0039","该用户尚未绑卡，签约失败"),
    AUTO_INVEST("0088","已经成功授权"),
    /******签约失败******/

    /******文章管理******/
    NODE_ID_IS_NOT_EXIST("0041","内容不存在"),

    /******换卡******/
    REPLACE_CARD_FAIL_BANK_CARDNO_IS_NULL("0042","换卡失败:银行卡号为空"),
    REPLACE_CARD_FAIL_BANK_CARD_EXIST("0043","换卡失败:该银行卡已存在"),
    REPLACE_CARD_FAIL_ACCOUNT_BALANCE_IS_NOT_ZERO("0047","换卡失败:用户账户余额不为0"),
    REPLACE_CARD_FAIL_HAS_OPEN_FAST_PAYMENT("0048","换卡失败:已开通快捷支付不能换卡"),


    /*****联动优势相关******/
    UMPAY_OPERATION_EXCEPTION("0044","联动优势处理失败"),

    /******未开通快捷支付******/
    FAST_PAY_OFF("0046","未开通快捷支付"),
    PASSWORD_INVEST_OFF("0058", "尚未开启免密投资"),

    USER_IS_DISABLED("0049", "错误次数太多,请{0}分钟以后再试。"),
    IMAGE_CAPTCHA_IS_WRONG("0081", "图形验证码错误"),
    NEED_IMAGE_CAPTCHA("0077", "需要图形验证码"),
    CANNOT_GET_APK_VERSION("0060", "查找不到版本信息"),
    /******自动投标******/
    AUTO_INVEST_PLAN_NOT_EXIST("0061","自动投标计划不存在"),
    MIN_NOT_EXCEED_MAX_INVEST_AMOUNT("0062","最小投资金额不能超过最大投资金额"),
    MIN_INVEST_AMOUNT_NOT_NULL("0063","最小投资金额不能空"),
    MAX_INVEST_AMOUNT_NOT_NULL("0064","最大投资金额不能空"),
    RETENTION_AMOUNT_NOT_NULL("0065","保留金额不能空"),
    AUTO_INVEST_PERIODS_NOT_NULL("0066","保留金额不能空"),


    /**优惠券兑换**/
    POINT_EXCHANGE_POINT_INSUFFICIENT("0100","财豆数量不够"),
    POINT_COUPON_NUM_INSUFFICIENT("0101","当前优惠券已兑完，请兑换其他优惠券"),
    POINT_EXCHANGE_FAIL("0103","财豆兑换失败"),
    /**兑换码兑换**/
    EXCHANGE_CODE_IS_INVALID("0104","兑换码不正确"),
    EXCHANGE_CODE_IS_EXPIRE("0105","兑换码已过期"),
    EXCHANGE_CODE_IS_USED("0106","兑换码已被使用"),
    EXCHANGE_CODE_OVER_DAILY_COUNT("0107","当天兑换次数达到上限"),
    USER_COUPON_IS_NOT_EXIST("0108",""),
    EXCHANGE_CODE_IS_FAIL("0109","兑换码兑换失败"),

    /******提现******/
    WITHDRAW_AMOUNT_NOT_REACH_FEE("0070","提现金额需大于手续费"),

    TRANSFER_APPLY_IS_FAIL("0073","债权转让失败"),

    TRANSFER_AMOUNT_OUT_OF_RANGE("0074","转让价格超出折价范围"),
    TRANSFER_CANCEL_IS_FAIL("0075","债权转让取消失败"),
    TRANSFER_ALREADY_CANCELED_TODAY("0076","该项目今天已经申请过转让，当日不能再进行转让申请。"),
    TRANSFER_IMPEND_REPAYING("0078","该项目即将回款，请在本期回款完成后再申请转让。"),
    TRANSFER_IS_OVERDUE("0079","该项目正处于逾期当中，暂时无法申请转让。"),
    REPAY_IS_GENERATION_IN("0082","回款计划生成中，暂不可申请转让。"),


    /******意见反馈******/
    FEEDBACK_CAN_NOT_BE_EMPTY("0080", "内容不能为空"),

    /******手机端修改密码******/
    CHANGEPASSWORD_INVALID_PASSWORD("0090", "原密码错误"),

    /******签到******/
    MULTIPLE_SIGN_IN("0100", "今日已经签到过，不可重复签到"),

    /******
     * 安全
     ******/
    WITHDRAW_IN_BLACKLIST("0102", "操作失败(错误代码:YM001), 请联系客服"),   //命中提现黑名单

    /******债权转让******/
    TRANSFER_IS_NOT_EXIST("0071", "该债权不能申请转让"),

    BAD_REQUEST("0400", "非法请求"),
    UNAUTHORIZED("0401", "未授权"),

    ERROR("9999", "错误"),
    /******
     * APP积分商城
     ******/
    POINTS_PRODUCT_IS_NOT_NULL("0110", "商品不能为空"),
    POINTS_PRODUCT_NUM_IS_NOT_NULL("0111", "商品数量不能为空"),
    INSUFFICIENT_POINTS_BALANCE("0112", "积分余额不足"),
    INSUFFICIENT_PRODUCT_NUM("0112", "商品剩余数量不足"),
    USER_ADDRESS_IS_NOT_NULL("0113", "收货地址不可以为空"),

    /******增值特权购买******/
    MEMBERSHIP_PRIVILEGE_PURCHASE_FAILED("0200", "增值特权购买失败"),
    MEMBERSHIP_PRIVILEGE_IS_PURCHASED("0201", "增值特权已购买，请勿重复购买"),
    MEMBERSHIP_PRIVILEGE_PURCHASE_NO_ENOUGH_AMOUNT("0202", "账户金额不足，请充值"),

    /******回款日历******/
    REPAY_CALENDAR_QUERY_FAILED("0114", "没有更多回款信息了哦");

    private String code;
    private String msg;

    ReturnMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getErrorMsgByCode(String code){

        for(ReturnMessage errorMsg : ReturnMessage.values() ){
            if(errorMsg.code.equals(code)){
                return errorMsg.msg;
            }
        }
        return null;
    }


}

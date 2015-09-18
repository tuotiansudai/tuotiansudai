package com.ttsd.api.dto;

public enum ReturnMessage {
    SUCCESS("0000",""),
    /******手机端注册返回码******/
    MOBILE_NUMBER_IS_NULL("0001", "手机号码为空"),
    MOBILE_NUMBER_IS_INVALID("0002","手机号码不合法"),
    MOBILE_NUMBER_IS_EXIST("0003","手机号已经存在"),
    SEND_SMS_IS_FAIL("0004","短信验证码发送失败"),
    USER_NAME_IS_NULL("0005","用户名为空"),
    USER_NAME_IS_INVALID("0006","用户名不允许包含特殊字符，长度为5-16位，请勿使用手机号"),
    USER_NAME_IS_EXIST("0007","用户名已经存在"),
    REFERRER_IS_NOT_EXIST("0008","推荐人不存在"),
    SMS_CAPTCHA_ERROR("0009","短信验证码不正确"),
    SMS_CAPTCHA_IS_OVERDUE("0010","短信验证码已经过期"),
    USER_IS_ACTIVE("0011","用户已经被激活"),
    PASSWORD_IS_INVALID("0012","密码是数字和字母的组合，长度为6-16位"),
    LOGIN_FAILED("0019","用户名或密码错误"),
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
    LOAN_ID_IS_NOT_EXIST("0024","标的详细信息不存在"),
    /******手机端标的详情******/

    /******手机端投资******/
    INVESTOR_SAME_TO_LOANER("0025","你不能投自己的项目"),
    INVEST_PASSWORD_IS_WRONG("0029","投资密码不匹配"),
    INVEST_PASSWORD_IS_NULL("0030","投资密码为空"),
    UMPAY_INVEST_MESSAGE_INVALID("0031","请求第三方加密失败"),
    INSUFFICIENT_BALANCE("0032","账户余额不足，请充值"),
    EXCEED_MONEY_NEED_RAISED("0033","投资金额不能大于尚未募集的金额！"),
    UNREACHED_MONEY_LIMIT_EXCETPTION("0034","投资金额未到达优惠券使用条件"),
    LLLEGAL_LOAN_STATUS_EXCEPTION("0035","当前借款不可投资"),
    NO_MATCHING_OBJECTS_EXCEPTION("0036","投资失败"),
    EXCEED_DEAD_LINE_EXCEPTION("0037","优惠券已过期"),
    USER_IS_NOT_CERTIFICATED("0038","您尚未进行实名认证无法投资"),
    INVEST_CAN_NOT_BE_FOUND("0040","查找不到投资详情"),
    NOT_SUFFICIENT_FUNDS("0045","账户余额不足"),
    NO_MATCH_XS_INVEST_CONDITION("0050","不符合新手标投资条件"),

    /******手机端投资******/


    /******绑卡失败******/
    BIND_CARD_FAIL("0026","绑卡失败"),
    /******绑卡失败******/

    /******签约失败******/
    BANK_CARD_SIGN_FAIL("0027","签约失败"),
    NOT_BIND_CARD("0039","该用户尚未绑卡，签约失败"),
    /******签约失败******/

    /******手机端请求第三方接口时，出现网络异常******/
    NETWORK_EXCEPTION("0028","网络异常"),
    /******手机端请求第三方接口时，出现网络异常******/

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
    NOT_OPNE_FAST_PAYMENT("0046","未开通快捷支付"),

    USER_IS_DISABLED("0049", "登录尝试次数过多，用户已被禁用"),

    BAD_REQUEST("0400", "非法请求"),
    UNAUTHORIZED("0401", "未授权");

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

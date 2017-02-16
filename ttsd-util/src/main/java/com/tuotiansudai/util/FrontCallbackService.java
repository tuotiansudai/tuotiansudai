package com.tuotiansudai.util;

public enum FrontCallbackService {
    /**
     * 绑定银行卡
     */
    PTP_MER_BIND_CARD("ptp_mer_bind_card", "银行卡绑定成功"),
    /**
     * 换卡
     */
    PTP_MER_REPLACE_CARD("ptp_mer_replace_card", "提交成功");

    private final String serviceName;

    private final String message;


    FrontCallbackService(String serviceName, String message) {
        this.serviceName = serviceName;
        this.message = message;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMessage() {
        return message;
    }

    public static FrontCallbackService getService(String service){

        for(FrontCallbackService frontCallbackService : FrontCallbackService.values() ){
            if(frontCallbackService.getServiceName().equalsIgnoreCase(service)){
                return frontCallbackService;
            }
        }
        return null;
    }

}

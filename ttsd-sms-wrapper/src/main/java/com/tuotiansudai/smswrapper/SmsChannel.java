package com.tuotiansudai.smswrapper;

public enum SmsChannel {

    NETEASE((template, params) -> {
        for (String param : params) {
            template = template.replaceFirst("%s", param);
        }
        return template;
    }),


    ALIDAYU((template, params) -> {
        for (String param : params) {
            template = template.replaceFirst("\\$\\{param\\d+\\}", param);
        }
        return template;
    });

    private ReplaceStrategyFunction replaceStrategy;

    SmsChannel(ReplaceStrategyFunction replaceStrategy) {
        this.replaceStrategy = replaceStrategy;
    }

    public ReplaceStrategyFunction getReplaceStrategy() {
        return replaceStrategy;
    }
}

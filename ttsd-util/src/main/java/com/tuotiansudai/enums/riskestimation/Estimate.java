package com.tuotiansudai.enums.riskestimation;

import java.util.Arrays;
import java.util.Optional;

public enum Estimate {
    CONSERVATIVE(0, 15, "保守型", "经评估，您的出借偏好为保守型，风险承受能力较低", "您具有较低的风险承受能力，在出借的过程中比较关注本金的安全，属于风险厌恶型出借者。出借目标主要是保持出借的稳定性与资产的保值，并在此基础上尽可能使资产一定程度的增值，为了使本金免于受损愿意降低实现目标回报率的可能型。"),
    STEADY(16, 23, "稳健型", "经评估，您的出借偏好为稳健型，风险承受能力中等", "您具有中等的风险承受能力，在出借的过程中主要强调出借风险和资产增值之间的平衡关系，能承担出借带来的风险，同时可以接受出借带来的结果。出借目标主要是资产增值，为了实现资产的增值愿意承担一定程度的出借风险。"),
    POSITIVE(24, 32, "积极型", "经评估，您的出借偏好为积极型，风险承受能力较高", "您具有较高的风险承受能力，在出借的过程中可以非常好的认识好风险和收益之间的关系，能承担出借带来的风险，同时可以接受出借带来的结果。出借目标主要是取得超额的收益，为了实现出借目标愿意承担较大的出借风险。");

    private final int lower;
    private final int upper;
    private final String type;
    private final String summary;
    private final String description;

    Estimate(int lower, int upper, String type, String summary, String description) {
        this.lower = lower;
        this.upper = upper;
        this.type = type;
        this.summary = summary;
        this.description = description;
    }

    static public Estimate estimate(int score) {
        Optional<Estimate> optional = Arrays.stream(Estimate.values()).filter(estimate -> estimate.getLower() <= score && estimate.getUpper() >= score).findFirst();
        return optional.orElse(null);
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }

    public String getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }
}

package com.tuotiansudai.enums.riskestimation;

import java.util.Arrays;
import java.util.Optional;

public enum Estimate {
    CONSERVATIVE(0, 15, "保守型", "您具有较低的风险承受能力，在投资的过程中比较关注本金的安全，属于风险厌恶型投资者。投资目标主要是保持投资的稳定性与资产的保值，并在此基础上尽可能使资产一定程度的增值，为了使本金免于受损愿意降低实现目标回报率的可能型。"),
    STEADY(16, 23, "稳健型", "您具有中等的风险承受能力，在投资的过程中主要强调投资风险和资产增值之间的平衡关系，能承担投资带来的风险，同时可以接受投资带来的结果。投资目标主要是资产增值，为了实现资产的增值愿意承担一定程度的投资风险。"),
    POSITIVE(24, 32, "积极型", "您具有较高的风险承受能力，在投资的过程中可以非常好的认识好风险和收益之间的关系，能承担投资带来的风险，同时可以接受投资带来的结果。投资目标主要是取得超额的收益，为了实现投资目标愿意承担较大的投资风险。");

    private final int lower;
    private final int upper;
    private final String type;
    private final String description;

    Estimate(int lower, int upper, String type, String description) {
        this.lower = lower;
        this.upper = upper;
        this.type = type;
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

    public String getDescription() {
        return description;
    }
}

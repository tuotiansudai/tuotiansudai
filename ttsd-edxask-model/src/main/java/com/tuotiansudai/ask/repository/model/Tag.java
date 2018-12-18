package com.tuotiansudai.ask.repository.model;

public enum Tag {
    SECURITIES("证券"),
    BANK("银行"),
    FUTURES("期货"),
    P2P("P2P"),
    TRUST("信托"),
    LOAN("贷款"),
    FUND("基金"),
    CROWD_FUNDING("众筹"),
    INVEST("出借"),
    CREDIT_CARD("信用卡"),
    FOREX("外汇"),
    STOCK("股票"),
    OTHER("其他");

    private final String description;

    Tag(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

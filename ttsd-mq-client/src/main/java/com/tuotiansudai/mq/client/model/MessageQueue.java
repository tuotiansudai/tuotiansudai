package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageQueue {
    CouponAssigning("CouponAssigning"),
    InvestCallback("InvestCallback"),
    TransferInvestCallback("TransferInvestCallback"),
    UserRegistered_CompletePointTask("UserRegistered-CompletePointTask"),
    AccountRegistered_CompletePointTask("AccountRegistered-CompletePointTask"),
    InvestSuccess_CompletePointTask("InvestSuccess-CompletePointTask"),
    InvestSuccess_CouponUpdate("InvestSuccess-CouponUpdate"),
    InvestSuccess_MembershipUpdate("InvestSuccess-MembershipUpdate"),
    RechargeSuccess_CompletePointTask("RechargeSuccess-CompletePointTask"),
    BindBankCard_CompletePointTask("BindBankCard-CompletePointTask"),
    TurnOnNoPasswordInvest_CompletePointTask("TurnOnNoPasswordInvest-CompletePointTask"),
    InvestSuccess_ActivityReward("InvestSuccess-ActivityReward"),
    EventMessage("EventMessage"),
    ManualMessage("ManualMessage"),
    PushMessage("PushMessage"),
    NormalRepayCallback("NormalRepayCallback"),
    AdvanceRepayCallback("AdvanceRepayCallback"),
    AuditLog("AuditLog"),
    UserOperateLog("UserOperateLog"),
    LoginLog("LoginLog"),
    RepaySuccess_Coupon("RepaySuccess-Coupon"),
    RepaySuccess_Extra("RepaySuccess-Extra");

    private final String queueName;

    MessageQueue(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public static boolean contains(String queueName) {
        return Stream.of(MessageQueue.values()).anyMatch(q -> q.getQueueName().equals(queueName));
    }
}

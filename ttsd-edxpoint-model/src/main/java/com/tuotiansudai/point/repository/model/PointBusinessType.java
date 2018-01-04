package com.tuotiansudai.point.repository.model;

import com.google.common.collect.Lists;

import java.util.List;

public enum PointBusinessType {
    SIGN_IN("签到奖励", PointBillOperationType.IN),
    TASK("任务奖励", PointBillOperationType.IN),
    POINT_LOTTERY_AWARD("积分抽奖奖励", PointBillOperationType.IN),
    INVEST("投资奖励", PointBillOperationType.IN),
    ACTIVITY("活动奖励", PointBillOperationType.IN),
    CHANNEL_IMPORT("渠道积分导入", PointBillOperationType.IN),
    EXCHANGE("积分兑换", PointBillOperationType.OUT),
    LOTTERY("抽奖", PointBillOperationType.OUT),
    POINT_LOTTERY("积分抽奖", PointBillOperationType.OUT),
    POINT_CLEAR("积分到期清零", PointBillOperationType.OUT);

    private final String description;

    private final PointBillOperationType pointBillOperationType;

    PointBusinessType(String description, PointBillOperationType pointBillOperationType) {
        this.description = description;
        this.pointBillOperationType = pointBillOperationType;
    }

    public String getDescription() {
        return description;
    }

    public PointBillOperationType getPointBillOperationType() {
        return pointBillOperationType;
    }

    public static List<PointBusinessType> getPointConsumeBusinessType() {
        List<PointBusinessType> operationTypeOut = Lists.newArrayList();
        for (PointBusinessType pointBusinessType : PointBusinessType.values()) {
            if (pointBusinessType.getPointBillOperationType() == PointBillOperationType.OUT) {
                operationTypeOut.add(pointBusinessType);
            }

        }
        return operationTypeOut;
    }
}

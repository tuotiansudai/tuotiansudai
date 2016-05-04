package com.tuotiansudai.dto;

import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;

import java.io.Serializable;

public class TransferRuleDto implements Serializable {

    private double levelOneFee;

    private boolean updateLevelOneFee;

    private double levelTwoFee;

    private boolean updateLevelTwoFee;

    private double levelThreeFee;

    private boolean updateLevelThreeFee;

    private double discount;

    private boolean updateDiscount;

    private int daysLimit;

    private boolean updateDaysLimit;

    private boolean multipleTransferEnabled;

    private boolean updateMultipleTransferEnabled;

    private boolean hasTask;

    private String updatedBy;

    public TransferRuleDto() {
    }

    public TransferRuleDto(TransferRuleModel transferRuleModel, OperationTask<TransferRuleDto> task) {
        this.levelOneFee = task != null ? task.getObj().getLevelOneFee() : transferRuleModel.getLevelOneFee() * 100;
        this.updateLevelOneFee = task != null && task.getObj().getLevelOneFee() != transferRuleModel.getLevelOneFee() * 100;

        this.levelTwoFee = task != null ? task.getObj().getLevelTwoFee() : transferRuleModel.getLevelTwoFee() * 100;
        this.updateLevelTwoFee = task != null && task.getObj().getLevelTwoFee() != transferRuleModel.getLevelTwoFee() * 100;

        this.levelThreeFee = task != null ? task.getObj().getLevelThreeFee() : transferRuleModel.getLevelThreeFee() * 100;
        this.updateLevelThreeFee = task != null && task.getObj().getLevelThreeFee() != transferRuleModel.getLevelThreeFee() * 100;

        this.discount = task != null ? task.getObj().getDiscount() : transferRuleModel.getDiscount() * 100;
        this.updateDiscount = task != null && task.getObj().getDiscount() != transferRuleModel.getDiscount() * 100;

        this.daysLimit = task != null ? task.getObj().getDaysLimit() : transferRuleModel.getDaysLimit();
        this.updateDaysLimit = task != null && task.getObj().getDaysLimit() != transferRuleModel.getDaysLimit();

        this.multipleTransferEnabled = task != null ? task.getObj().isMultipleTransferEnabled() : transferRuleModel.isMultipleTransferEnabled();
        this.updateMultipleTransferEnabled = task != null && task.getObj().isMultipleTransferEnabled() != transferRuleModel.isMultipleTransferEnabled();

        this.updatedBy = task != null ? task.getSender() : null;

        this.hasTask = task != null;
    }

    public double getLevelOneFee() {
        return levelOneFee;
    }

    public void setLevelOneFee(double levelOneFee) {
        this.levelOneFee = levelOneFee;
    }

    public boolean isUpdateLevelOneFee() {
        return updateLevelOneFee;
    }

    public void setUpdateLevelOneFee(boolean updateLevelOneFee) {
        this.updateLevelOneFee = updateLevelOneFee;
    }

    public double getLevelTwoFee() {
        return levelTwoFee;
    }

    public void setLevelTwoFee(double levelTwoFee) {
        this.levelTwoFee = levelTwoFee;
    }

    public boolean isUpdateLevelTwoFee() {
        return updateLevelTwoFee;
    }

    public void setUpdateLevelTwoFee(boolean updateLevelTwoFee) {
        this.updateLevelTwoFee = updateLevelTwoFee;
    }

    public double getLevelThreeFee() {
        return levelThreeFee;
    }

    public void setLevelThreeFee(double levelThreeFee) {
        this.levelThreeFee = levelThreeFee;
    }

    public boolean isUpdateLevelThreeFee() {
        return updateLevelThreeFee;
    }

    public void setUpdateLevelThreeFee(boolean updateLevelThreeFee) {
        this.updateLevelThreeFee = updateLevelThreeFee;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isUpdateDiscount() {
        return updateDiscount;
    }

    public void setUpdateDiscount(boolean updateDiscount) {
        this.updateDiscount = updateDiscount;
    }

    public int getDaysLimit() {
        return daysLimit;
    }

    public void setDaysLimit(int daysLimit) {
        this.daysLimit = daysLimit;
    }

    public boolean isUpdateDaysLimit() {
        return updateDaysLimit;
    }

    public void setUpdateDaysLimit(boolean updateDaysLimit) {
        this.updateDaysLimit = updateDaysLimit;
    }

    public boolean isMultipleTransferEnabled() {
        return multipleTransferEnabled;
    }

    public void setMultipleTransferEnabled(boolean multipleTransferEnabled) {
        this.multipleTransferEnabled = multipleTransferEnabled;
    }

    public boolean isUpdateMultipleTransferEnabled() {
        return updateMultipleTransferEnabled;
    }

    public void setUpdateMultipleTransferEnabled(boolean updateMultipleTransferEnabled) {
        this.updateMultipleTransferEnabled = updateMultipleTransferEnabled;
    }

    public boolean isHasTask() {
        return hasTask;
    }

    public void setHasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}

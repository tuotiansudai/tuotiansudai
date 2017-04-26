package com.tuotiansudai.message;


import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

public class ExperienceAssigningMessage implements Serializable {

    private String loginName;
    private long experienceAmount;
    private ExperienceBillOperationType experienceBillOperationType;
    private ExperienceBillBusinessType experienceBillBusinessType;
    private String note;
    private Date currentDate;

    public ExperienceAssigningMessage() {
    }

    public ExperienceAssigningMessage(Date currentDate, String loginName) {
        this.currentDate = currentDate;
        this.loginName = loginName;
    }

    public ExperienceAssigningMessage(String loginName, long experienceAmount, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBillBusinessType, String note) {
        this.loginName = loginName;
        this.experienceAmount = experienceAmount;
        this.experienceBillOperationType = experienceBillOperationType;
        this.experienceBillBusinessType = experienceBillBusinessType;
        this.note = note;
        this.currentDate = DateTime.now().toDate();
    }

    public ExperienceAssigningMessage(String loginName, long experienceAmount, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBillBusinessType) {
        this(loginName, experienceAmount, experienceBillOperationType, experienceBillBusinessType,
                MessageFormat.format(experienceBillBusinessType.getContentTemplate(), AmountConverter.convertCentToString(experienceAmount), DateTime.now().toDate()));
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getExperienceAmount() {
        return experienceAmount;
    }

    public void setExperienceAmount(long experienceAmount) {
        this.experienceAmount = experienceAmount;
    }

    public ExperienceBillOperationType getExperienceBillOperationType() {
        return experienceBillOperationType;
    }

    public void setExperienceBillOperationType(ExperienceBillOperationType experienceBillOperationType) {
        this.experienceBillOperationType = experienceBillOperationType;
    }

    public ExperienceBillBusinessType getExperienceBillBusinessType() {
        return experienceBillBusinessType;
    }

    public void setExperienceBillBusinessType(ExperienceBillBusinessType experienceBillBusinessType) {
        this.experienceBillBusinessType = experienceBillBusinessType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

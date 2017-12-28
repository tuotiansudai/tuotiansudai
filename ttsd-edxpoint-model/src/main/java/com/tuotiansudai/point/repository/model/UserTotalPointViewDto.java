package com.tuotiansudai.point.repository.model;

import java.io.Serializable;

public class UserTotalPointViewDto implements Serializable {
    private String loginName;
    private long totalPoint;
    private long totalSudaiPoint;
    private long totalChannelPoint;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public long getTotalSudaiPoint() {
        return totalSudaiPoint;
    }

    public void setTotalSudaiPoint(long totalSudaiPoint) {
        this.totalSudaiPoint = totalSudaiPoint;
    }

    public long getTotalChannelPoint() {
        return totalChannelPoint;
    }

    public void setTotalChannelPoint(long totalChannelPoint) {
        this.totalChannelPoint = totalChannelPoint;
    }
}

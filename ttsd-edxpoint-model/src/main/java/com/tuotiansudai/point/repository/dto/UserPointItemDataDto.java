package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.point.repository.model.UserPointModel;
import com.tuotiansudai.point.repository.model.UserTotalPointViewDto;

import java.io.Serializable;

public class UserPointItemDataDto implements Serializable {
    private String loginName;
    private String userName;
    private String mobile;
    private long point;
    private long totalPoint;
    private long sudaiPoint;
    private long totalSudaiPoint;
    private String channel;
    private long channelPoint;
    private long totalChannelPoint;

    public UserPointItemDataDto(String loginName, String userName, String mobile, UserPointModel userPointModel, UserTotalPointViewDto userTotalPointViewDto) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        if (userPointModel != null) {
            this.point = userPointModel.getPoint();
            this.sudaiPoint = userPointModel.getSudaiPoint();
            this.channel = userPointModel.getChannel();
            this.channelPoint = userPointModel.getChannelPoint();
        }
        if (userTotalPointViewDto != null) {
            this.totalPoint = userTotalPointViewDto.getTotalPoint();
            this.totalSudaiPoint = userTotalPointViewDto.getTotalSudaiPoint();
            this.totalChannelPoint = userTotalPointViewDto.getTotalChannelPoint();
        }
    }

    public String getLoginName() {
        return loginName;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public long getPoint() {
        return point;
    }

    public long getTotalPoint() {
        return totalPoint;
    }

    public long getSudaiPoint() {
        return sudaiPoint;
    }

    public long getTotalSudaiPoint() {
        return totalSudaiPoint;
    }

    public String getChannel() {
        return channel;
    }

    public long getChannelPoint() {
        return channelPoint;
    }

    public long getTotalChannelPoint() {
        return totalChannelPoint;
    }
}


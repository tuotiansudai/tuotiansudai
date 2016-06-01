package com.tuotiansudai.jpush.dto;

import com.tuotiansudai.dto.BaseDataDto;

public class JpushReportDto extends BaseDataDto {

    private int iosReceived;

    private int androidReceived;

    public int getIosReceived() {
        return iosReceived;
    }

    public void setIosReceived(int iosReceived) {
        this.iosReceived = iosReceived;
    }

    public int getAndroidReceived() {
        return androidReceived;
    }

    public void setAndroidReceived(int androidReceived) {
        this.androidReceived = androidReceived;
    }
}

package com.tuotiansudai.console.jpush.service;


import com.tuotiansudai.console.jpush.dto.JPushAlertDto;

public interface JPushAlertService {
    void buildJPushAlert(String loginName,JPushAlertDto jPushAlertDto);
}

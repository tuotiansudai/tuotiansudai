package com.tuotiansudai.console.jpush.service.impl;


import com.tuotiansudai.console.jpush.dto.JPushAlertDto;
import com.tuotiansudai.console.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.console.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.console.jpush.service.JPushAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JPushAlertServiceImpl implements JPushAlertService{
    @Autowired
    private JPushAlertMapper jPushAlertMapper;

    @Override
    public void buildJPushAlert(String loginName, JPushAlertDto jPushAlertDto) {
        JPushAlertModel jPushAlertModel = new JPushAlertModel(jPushAlertDto);
        jPushAlertModel.setCreatedBy(loginName);
        jPushAlertMapper.create(jPushAlertModel);
    }
}

package com.tuotiansudai.console.jpush.service.impl;


import com.tuotiansudai.console.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.console.jpush.dto.JPushAlertDto;
import com.tuotiansudai.console.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.console.jpush.repository.model.*;
import com.tuotiansudai.console.jpush.service.JPushAlertService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class JPushAlertServiceImpl implements JPushAlertService{
    @Autowired
    private JPushAlertMapper jPushAlertMapper;

    @Autowired
    private MobileAppJPushClient mobileAppJPushClient;

    @Override
    @Transactional
    public void buildJPushAlert(String loginName, JPushAlertDto jPushAlertDto) {
        JPushAlertModel jPushAlertModel = new JPushAlertModel(jPushAlertDto);
        if(StringUtils.isNotEmpty(jPushAlertDto.getId())){
            jPushAlertModel.setUpdatedBy(loginName);
            jPushAlertModel.setUpdatedTime(new Date());
            jPushAlertMapper.update(jPushAlertModel);

        }else{
            jPushAlertModel.setCreatedBy(loginName);
            jPushAlertModel.setCreatedTime(new Date());
            jPushAlertModel.setIsAutomatic(false);
            jPushAlertMapper.create(jPushAlertModel);
        }
    }



    @Override
    public int findPushTypeCount(PushType pushType) {
        return jPushAlertMapper.findPushTypeCount(pushType);
    }

    @Override
    public int findPushAlertCount(String name,boolean isAutomatic) {
        return jPushAlertMapper.findPushAlertCount(name,false);
    }

    @Override
    public List<JPushAlertModel> findPushAlerts(int index, int pageSize, String name,boolean isAutomatic) {
        return jPushAlertMapper.findPushAlerts((index - 1) * pageSize, pageSize, name,isAutomatic);
    }

    @Override
    public JPushAlertModel findJPushAlertModelById(long id) {
        return jPushAlertMapper.findJPushAlertModelById(id);
    }

    @Override
    public void send(String loginName,long id) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertModelById(id);
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        PushSource pushSource = jPushAlertDto.getPushSource();
        String[] jumpToOrLink = chooseJumpToOrLink(jPushAlertDto);
        String alert = jPushAlertDto.getContent();
        boolean sendResult = false;
        List<String> pushObjects = jPushAlertDto.getPushObjects();

        if(pushSource == PushSource.ALL){

            if(CollectionUtils.isNotEmpty(pushObjects)){
                sendResult = mobileAppJPushClient.sendPushAlertByTags(jPushAlertDto.getId(), pushObjects, alert, jumpToOrLink[0], jumpToOrLink[1]);
            }else {
                sendResult =  mobileAppJPushClient.sendPushAlertByAll(jPushAlertDto.getId(), alert, jumpToOrLink[0], jumpToOrLink[1]);
            }

        }else if(pushSource == PushSource.ANDROID){
            if(CollectionUtils.isNotEmpty(pushObjects)){
                sendResult = mobileAppJPushClient.sendPushAlertByAndroidTags(jPushAlertDto.getId(), pushObjects, alert, jumpToOrLink[0], jumpToOrLink[1]);
            }else{
                sendResult = mobileAppJPushClient.sendPushAlertByAndroid(jPushAlertDto.getId(), alert, jumpToOrLink[0], jumpToOrLink[1]);
            }
        }else if(pushSource == PushSource.IOS){
            if(CollectionUtils.isNotEmpty(pushObjects)){
                sendResult = mobileAppJPushClient.sendPushAlertByIosTags(jPushAlertDto.getId(), pushObjects, alert, jumpToOrLink[0], jumpToOrLink[1]);
            }else{
                sendResult = mobileAppJPushClient.sendPushAlertByIos(jPushAlertDto.getId(), alert, jumpToOrLink[0], jumpToOrLink[1]);
            }
        }

        if(sendResult){
            jPushAlertModel.setUpdatedBy(loginName);
            jPushAlertModel.setUpdatedTime(new Date());
            jPushAlertModel.setStatus(PushStatus.SEND_SUCCESS);
            jPushAlertMapper.update(jPushAlertModel);
        }else{
            jPushAlertModel.setUpdatedBy(loginName);
            jPushAlertModel.setUpdatedTime(new Date());
            jPushAlertModel.setStatus(PushStatus.SEND_FAIL);
            jPushAlertMapper.update(jPushAlertModel);
        }
    }

    @Override
    @Transactional
    public void changeJPushAlertStatus(long id,PushStatus status) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertModelById(id);
        jPushAlertModel.setStatus(status);
        jPushAlertMapper.update(jPushAlertModel);
    }

    private String[] chooseJumpToOrLink(JPushAlertDto jPushAlertDto){
        String[] jumpToOrLink = new String[]{"",""};
        JumpTo jumpTo = jPushAlertDto.getJumpTo();
        String jumpToLink = jPushAlertDto.getJumpToLink();
        if(StringUtils.isNotEmpty(jumpToLink)){
            jumpToOrLink[0] = "jumpToLink";
            jumpToOrLink[1] = jumpToLink;
            return jumpToOrLink;
        }
        if(jumpTo != null){
            jumpToOrLink[0] = "jumpTo";
            jumpToOrLink[1] = jumpTo.getIndex();
            return jumpToOrLink;
        }

        return jumpToOrLink;

    }


}

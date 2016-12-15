package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class ConsoleAnnounceService {

    private static Logger logger = Logger.getLogger(ConsoleAnnounceService.class);

    @Autowired
    private AnnounceMapper announceMapper;

    @Autowired
    private MessageService messageService;

    public AnnounceModel findById(long id) {
        return announceMapper.findById(id);
    }

    public List<AnnounceModel> findAnnounce(Long id, String title, int startLimit, int endLimit) {
        return announceMapper.findAnnounce(id, title, startLimit, endLimit);
    }

    public void create(AnnounceDto announceDto, String createdBy) {
        AnnounceModel announceModel = new AnnounceModel(announceDto);
        announceMapper.create(announceModel);
        announceDto.setId(announceModel.getId());

        try {
            MessageCreateDto messageCreateDto = announceDtoToMessageCompleteDto(announceDto, createdBy);
            long messageId = messageService.createOrUpdateManualMessage(LoginUserInfo.getLoginName(), messageCreateDto);
            messageService.approveMessage(messageId, createdBy);
            logger.info(MessageFormat.format("[AnnounceConsoleService] announce message create success. announceId:{0}", announceDto.getId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[AnnounceConsoleService] announce message create fail. announceId:{0}", announceDto.getId()), e);
        }
    }

    public void update(AnnounceDto announceDto) {
        announceMapper.update(new AnnounceModel(announceDto));
    }

    public void delete(AnnounceDto announceDto) {
        announceMapper.delete(announceDto.getId());
    }

    public int findAnnounceCount(Long id, String title) {
        return announceMapper.findAnnounceCount(id, title);
    }

    private MessageCreateDto announceDtoToMessageCompleteDto(AnnounceDto announceDto, String createdBy) {
        MessageCreateDto messageCreateDto = new MessageCreateDto();

//        messageCreateDto.setTitle(announceDto.getTitle());
//        messageCreateDto.setTemplate(announceDto.getContent());
//        messageCreateDto.setTemplateTxt(announceDto.getContentText());
//        messageCreateDto.setType(MessageType.MANUAL);
//        messageCreateDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
//        messageCreateDto.setChannels(Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
//        messageCreateDto.setMessageCategory(MessageCategory.NOTIFY);
//        messageCreateDto.setWebUrl(MessageFormat.format("/announce/{0}", announceDto.getId()));
//        messageCreateDto.setAppUrl(AppUrl.NOTIFY);
//        messageCreateDto.setJpush(true);
//        messageCreateDto.setPushType(PushType.IMPORTANT_EVENT);
//        messageCreateDto.setPushSource(PushSource.ALL);
//        messageCreateDto.setStatus(MessageStatus.APPROVED);
//        messageCreateDto.setReadCount(0);
//        messageCreateDto.setActivatedBy(createdBy);
//        messageCreateDto.setActivatedTime(new Date());
//        messageCreateDto.setExpiredTime(new DateTime().withDate(9999, 12, 31).toDate());
//        messageCreateDto.setUpdatedBy(createdBy);
//        messageCreateDto.setUpdatedTime(new Date());
//        messageCreateDto.setCreatedBy(createdBy);
//        messageCreateDto.setCreatedTime(new Date());

        return messageCreateDto;
    }
}

package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.dto.MessageCompleteDto;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
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
            MessageCompleteDto messageCompleteDto = announceDtoToMessageCompleteDto(announceDto, createdBy);
            long messageId = messageService.createAndEditManualMessage(messageCompleteDto, 0);
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

    private MessageCompleteDto announceDtoToMessageCompleteDto(AnnounceDto announceDto, String createdBy) {
        MessageCompleteDto messageCompleteDto = new MessageCompleteDto();

        messageCompleteDto.setTitle(announceDto.getTitle());
        messageCompleteDto.setTemplate(announceDto.getContent());
        messageCompleteDto.setTemplateTxt(announceDto.getContentText());
        messageCompleteDto.setType(MessageType.MANUAL);
        messageCompleteDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
        messageCompleteDto.setChannels(Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
        messageCompleteDto.setMessageCategory(MessageCategory.NOTIFY);
        messageCompleteDto.setWebUrl(MessageFormat.format("/announce/{0}", announceDto.getId()));
        messageCompleteDto.setAppUrl(AppUrl.NOTIFY);
        messageCompleteDto.setJpush(true);
        messageCompleteDto.setPushType(PushType.IMPORTANT_EVENT);
        messageCompleteDto.setPushSource(PushSource.ALL);
        messageCompleteDto.setStatus(MessageStatus.APPROVED);
        messageCompleteDto.setReadCount(0);
        messageCompleteDto.setActivatedBy(createdBy);
        messageCompleteDto.setActivatedTime(new Date());
        messageCompleteDto.setExpiredTime(new DateTime().withDate(9999, 12, 31).toDate());
        messageCompleteDto.setUpdatedBy(createdBy);
        messageCompleteDto.setUpdatedTime(new Date());
        messageCompleteDto.setCreatedBy(createdBy);
        messageCompleteDto.setCreatedTime(new Date());

        return messageCompleteDto;
    }
}

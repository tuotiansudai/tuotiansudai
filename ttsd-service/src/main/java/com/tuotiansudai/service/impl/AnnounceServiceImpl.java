package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.repository.model.ManualMessageType;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
import com.tuotiansudai.service.AnnounceService;
import com.tuotiansudai.util.DistrictUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static com.tuotiansudai.enums.PushType.IMPORTANT_EVENT;
import static com.tuotiansudai.message.repository.model.AppUrl.NOTIFY;
import static com.tuotiansudai.message.repository.model.MessageType.MANUAL;

@Service
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    private AnnounceMapper announceMapper;

    @Autowired
    private MessageService messageService;

    @Override
    public int findAnnounceCount(Long id, String title) {
        return announceMapper.findAnnounceCount(id, title);
    }

    @Override
    public List<AnnounceModel> findAnnounce(Long id, String title, int startLimit, int endLimit) {
        return announceMapper.findAnnounce(id, title, startLimit, endLimit);
    }

    @Override
    public void create(AnnounceDto announceDto, String createdBy) {
        this.announceMapper.create(new AnnounceModel(announceDto));
        sendMessageAndJPush(announceDto, createdBy);
    }

    private void sendMessageAndJPush(AnnounceDto announceDto, String createdBy) {
        MessageCreateDto messageCreateDto = new MessageCreateDto();
        messageCreateDto.setTitle(announceDto.getTitle());
        messageCreateDto.setTemplate(announceDto.getContent());
        messageCreateDto.setTemplateTxt(announceDto.getContentText());
        messageCreateDto.setType(MANUAL);
        messageCreateDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
        messageCreateDto.setChannels(Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
        messageCreateDto.setManualMessageType(ManualMessageType.NOTIFY);
        messageCreateDto.setWebUrl(MessageFormat.format("/announce/{0}", announceDto.getId()));
        messageCreateDto.setAppUrl(NOTIFY);
        messageCreateDto.setJpush(true);
        messageCreateDto.setPushType(IMPORTANT_EVENT);
        messageCreateDto.setPushSource(PushSource.ALL);
        messageCreateDto.setPushDistricts(DistrictUtil.getAllCodes());
        messageCreateDto.setStatus(MessageStatus.APPROVED);
        messageCreateDto.setReadCount(0);
        messageCreateDto.setActivatedBy(createdBy);
        messageCreateDto.setActivatedTime(new Date());
        messageCreateDto.setExpiredTime(new DateTime().withDate(9999, 12, 31).toDate());
        messageCreateDto.setUpdatedBy(createdBy);
        messageCreateDto.setUpdatedTime(new Date());
        messageCreateDto.setCreatedBy(createdBy);
        messageCreateDto.setCreatedTime(new Date());

        long messageId = messageService.createAndEditManualMessage(messageCreateDto, 0);
        messageService.approveManualMessage(messageId, createdBy);
    }

    @Override
    public void update(AnnounceDto announceDto) {
        this.announceMapper.update(new AnnounceModel(announceDto));
    }

    @Override
    public void delete(AnnounceDto announceDto) {
        this.announceMapper.delete(announceDto.getId());
    }

    @Override
    public AnnounceModel findById(long id) {
        return this.announceMapper.findById(id);
    }

    @Override
    public AnnounceDto getDtoById(long id) {
        AnnounceModel model = this.findById(id);
        if (model == null) {
            return null;
        }
        return new AnnounceDto(model);
    }

    @Override
    public BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize) {
        List<AnnounceModel> announceModels = this.announceMapper.findAnnounce(null, null, (index - 1) * pageSize, pageSize);
        int count = this.announceMapper.findAnnounceCount(null, null);

        List<AnnounceDto> announceList = Lists.transform(announceModels, new Function<AnnounceModel, AnnounceDto>() {
            @Override
            public AnnounceDto apply(AnnounceModel input) {
                return new AnnounceDto(input);
            }
        });

        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<AnnounceDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, announceList);
        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }
}

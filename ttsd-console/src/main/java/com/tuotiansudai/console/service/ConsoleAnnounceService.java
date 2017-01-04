package com.tuotiansudai.console.service;

import com.tuotiansudai.message.dto.AnnounceCreateDto;
import com.tuotiansudai.message.repository.mapper.AnnounceMapper;
import com.tuotiansudai.message.repository.model.AnnounceModel;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleAnnounceService {

    private static Logger logger = Logger.getLogger(ConsoleAnnounceService.class);

    @Autowired
    private AnnounceMapper announceMapper;

    @Autowired
    private ConsoleMessageService consoleMessageService;

    public AnnounceModel findById(long id) {
        return announceMapper.findById(id);
    }

    public List<AnnounceModel> findAnnounce(String title, int index, int pageSize) {
        return announceMapper.findAnnounce(title,
                PaginationUtil.calculateOffset(index, pageSize, announceMapper.findAnnounceCount(title)),
                pageSize);
    }

    @Transactional
    public void create(AnnounceCreateDto announceCreateDto, String createdBy) {
        AnnounceModel announceModel = new AnnounceModel(announceCreateDto.getTitle(), announceCreateDto.getContent(), announceCreateDto.getContentText(), announceCreateDto.isShowOnHome());
        announceMapper.create(announceModel);
        announceCreateDto.setId(announceModel.getId());

        consoleMessageService.approveMessage(consoleMessageService.createOrUpdateManualMessage(createdBy, announceCreateDto.transferTo()), createdBy);
    }

    public void update(AnnounceCreateDto announceCreateDto) {
        AnnounceModel announceModel = announceMapper.findById(announceCreateDto.getId());
        announceModel.setTitle(announceCreateDto.getTitle());
        announceModel.setContent(announceCreateDto.getContent());
        announceModel.setContentText(announceCreateDto.getContentText());
        announceModel.setShowOnHome(announceCreateDto.isShowOnHome());
        announceModel.setUpdatedTime(new Date());

        announceMapper.update(announceModel);
    }

    public void delete(long id) {
        announceMapper.delete(id);
    }

    public int findAnnounceCount(String title) {
        return announceMapper.findAnnounceCount(title);
    }

}

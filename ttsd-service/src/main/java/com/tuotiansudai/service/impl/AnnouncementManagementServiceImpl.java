package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.AnnouncementManagementDto;
import com.tuotiansudai.repository.mapper.AnnouncementManagementMapper;
import com.tuotiansudai.repository.model.AnnouncementManagementModel;
import com.tuotiansudai.service.AnnouncementManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementManagementServiceImpl implements AnnouncementManagementService {

    @Autowired
    private AnnouncementManagementMapper announcementManagementMapper;

    @Override
    public int findAnnouncementManagementCount(long id,String title) {
        return announcementManagementMapper.findAnnouncementManagementCount(id, title);
    }

    @Override
    public List<AnnouncementManagementModel> findAnnouncementManagement(long id, String title, int startLimit, int endLimit) {
        return announcementManagementMapper.findAnnouncementManagement(id, title, startLimit, endLimit);
    }

    @Override
    public void create(AnnouncementManagementDto announcementManagementDto) {
        this.announcementManagementMapper.create(new AnnouncementManagementModel(announcementManagementDto));
    }

    @Override
    public void update(AnnouncementManagementDto announcementManagementDto) {
        this.announcementManagementMapper.update(new AnnouncementManagementModel(announcementManagementDto));
    }

    @Override
    public void delete(AnnouncementManagementDto announcementManagementDto) {
        this.announcementManagementMapper.delete(announcementManagementDto.getId());
    }

}

package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.AnnouncementManagementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestPaginationItemDto;
import com.tuotiansudai.repository.mapper.AnnouncementManagementMapper;
import com.tuotiansudai.repository.model.AnnouncementManagementModel;
import com.tuotiansudai.service.AnnouncementManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementManagementServiceImpl implements AnnouncementManagementService {

    @Autowired
    private AnnouncementManagementMapper announcementManagementMapper;

    @Override
    public int findAnnouncementManagementCount(Long id, String title) {
        return announcementManagementMapper.findAnnouncementManagementCount(id, title);
    }

    @Override
    public List<AnnouncementManagementModel> findAnnouncementManagement(Long id, String title, int startLimit, int endLimit) {
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

    @Override
    public AnnouncementManagementModel findById(long id) {
        return this.announcementManagementMapper.findById(id);
    }

    @Override
    public AnnouncementManagementDto getDtoById(long id){
        AnnouncementManagementModel model =  this.findById(id);
        return new AnnouncementManagementDto(model);
    }

    @Override
    public BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize) {
        List<AnnouncementManagementModel> announcementList = this.announcementManagementMapper.findAnnouncementManagement(null, null, (index-1)*pageSize, pageSize);
        int count = this.announcementManagementMapper.findAnnouncementManagementCount(null, null);

        List<AnnouncementManagementDto> announcementManagementDtos = new ArrayList<AnnouncementManagementDto>();

        for (AnnouncementManagementModel model : announcementList) {
            AnnouncementManagementDto dto = new AnnouncementManagementDto(model);
            announcementManagementDtos.add(dto);
        }
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<AnnouncementManagementDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, announcementManagementDtos);
        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }
}

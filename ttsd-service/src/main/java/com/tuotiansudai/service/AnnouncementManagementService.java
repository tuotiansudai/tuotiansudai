package com.tuotiansudai.service;

import com.tuotiansudai.dto.AnnouncementManagementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.AnnouncementManagementModel;

import java.util.List;

public interface AnnouncementManagementService {

    int findAnnouncementManagementCount(Long id,String title);

    List<AnnouncementManagementModel> findAnnouncementManagement(Long id, String title, int startLimit, int endLimit);

    void create(AnnouncementManagementDto announcementManagementDto);

    void update(AnnouncementManagementDto announcementManagementDto);

    void delete(AnnouncementManagementDto announcementManagementDto);

    AnnouncementManagementModel findById(long id);

    AnnouncementManagementDto getDtoById(long id);

    BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize);

}

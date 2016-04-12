package com.tuotiansudai.service;

import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.AnnounceModel;

import java.util.List;

public interface AnnounceService {

    int findAnnounceCount(Long id,String title);

    List<AnnounceModel> findAnnounce(Long id, String title, int startLimit, int endLimit);

    void create(AnnounceDto announceDto);

    void update(AnnounceDto announceDto);

    void delete(AnnounceDto announceDto);

    AnnounceModel findById(long id);

    AnnounceDto getDtoById(long id);

    BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize);

}

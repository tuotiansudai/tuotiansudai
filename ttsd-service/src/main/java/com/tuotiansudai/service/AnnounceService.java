package com.tuotiansudai.service;

import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;

public interface AnnounceService {

    AnnounceDto getDtoById(long id);

    BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize);
}

package com.tuotiansudai.message.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.AnnounceDto;

public interface AnnounceService {

    AnnounceDto getDtoById(long id);

    BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize);
}

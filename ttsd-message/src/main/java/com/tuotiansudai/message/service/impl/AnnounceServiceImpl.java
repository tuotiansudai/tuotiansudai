package com.tuotiansudai.message.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.AnnounceDto;
import com.tuotiansudai.message.repository.mapper.AnnounceMapper;
import com.tuotiansudai.message.repository.model.AnnounceModel;
import com.tuotiansudai.message.service.AnnounceService;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    private AnnounceMapper announceMapper;

    @Override
    public AnnounceDto getDtoById(long id) {
        AnnounceModel model = announceMapper.findById(id);
        if (model == null) {
            return null;
        }
        return new AnnounceDto(model);
    }

    @Override
    public BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize) {
        int count = this.announceMapper.findAnnounceCount(null);
        List<AnnounceModel> announceModels = this.announceMapper.findAnnounce(null, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<AnnounceDto> announceList = Lists.transform(announceModels, AnnounceDto::new);
        BasePaginationDataDto<AnnounceDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, announceList);
        dataDto.setStatus(true);
        return new BaseDto<>(new BasePaginationDataDto<>(index, pageSize, count, announceList));
    }
}

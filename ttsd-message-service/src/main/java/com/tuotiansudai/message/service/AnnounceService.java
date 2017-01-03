package com.tuotiansudai.message.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.AnnounceDto;
import com.tuotiansudai.message.repository.mapper.AnnounceMapper;
import com.tuotiansudai.message.repository.model.AnnounceModel;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnounceService {

    private final AnnounceMapper announceMapper;

    @Autowired
    public AnnounceService(AnnounceMapper announceMapper) {
        this.announceMapper = announceMapper;
    }

    public AnnounceDto getAnnounce(long id) {
        AnnounceModel model = announceMapper.findById(id);
        if (model == null) {
            return null;
        }
        return new AnnounceDto(model);
    }

    public BaseDto<BasePaginationDataDto> getAnnouncementList(int index, int pageSize) {
        int count = this.announceMapper.findAnnounceCount(null);
        List<AnnounceModel> announceModels = this.announceMapper.findAnnounce(null, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<AnnounceDto> announceList = announceModels.stream().map(AnnounceDto::new).collect(Collectors.toList());
        BasePaginationDataDto<AnnounceDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, announceList);
        dataDto.setStatus(true);
        return new BaseDto<>(dataDto);
    }
}

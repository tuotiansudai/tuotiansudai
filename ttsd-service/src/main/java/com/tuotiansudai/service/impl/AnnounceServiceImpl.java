package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.AnnounceDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
import com.tuotiansudai.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    private AnnounceMapper announceMapper;

    @Override
    public int findAnnounceCount(Long id, String title) {
        return announceMapper.findAnnounceCount(id, title);
    }

    @Override
    public List<AnnounceModel> findAnnounce(Long id, String title, int startLimit, int endLimit) {
        return announceMapper.findAnnounce(id, title, startLimit, endLimit);
    }

    @Override
    public void create(AnnounceDto announceDto) {
        this.announceMapper.create(new AnnounceModel(announceDto));
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

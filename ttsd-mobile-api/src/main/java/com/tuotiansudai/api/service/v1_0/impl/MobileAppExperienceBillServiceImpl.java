package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppExperienceBillService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.model.ExperienceBillModel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppExperienceBillServiceImpl implements MobileAppExperienceBillService {

    static Logger logger = Logger.getLogger(MobileAppExperienceBillServiceImpl.class);

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    @Override
    public BaseResponseDto<ExperienceBillResponseDataDto> queryExperienceBillList(ExperienceBillRequestDto experienceBillRequestDto) {
        String loginName = experienceBillRequestDto.getBaseParam().getUserId();
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = experienceBillRequestDto.getIndex();
        Integer pageSize = experienceBillRequestDto.getPageSize();
        String operationType = experienceBillRequestDto.getOperationType();
        if (index == null || index <= 0) {
            index = 1;
        }
        pageSize = pageValidUtils.validPageSizeLimit(pageSize);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        ExperienceBillResponseDataDto experienceBillResponseDataDto = new ExperienceBillResponseDataDto();
        experienceBillResponseDataDto.setIndex(index);
        experienceBillResponseDataDto.setPageSize(pageSize);
        experienceBillResponseDataDto.setExperienceBills(convertExperienceBillRecordDto(experienceBillMapper.findExperienceBillPagination(loginName, operationType, (index - 1) * pageSize, pageSize)));
        experienceBillResponseDataDto.setTotalCount(experienceBillMapper.findCountExperienceBillPagination(loginName, operationType));
        dto.setData(experienceBillResponseDataDto);
        return dto;
    }

    private List<ExperienceBillRecordResponseDataDto> convertExperienceBillRecordDto(List<ExperienceBillModel> experienceBillList) {
        return Lists.transform(experienceBillList, input -> {
            ExperienceBillRecordResponseDataDto experienceBillRecordResponseDataDto = new ExperienceBillRecordResponseDataDto();
            experienceBillRecordResponseDataDto.setExperienceBillId(String.valueOf(input.getId()));
            experienceBillRecordResponseDataDto.setOperationType(input.getOperationType().getDescription());
            experienceBillRecordResponseDataDto.setBusinessType(input.getBusinessType().getDescription());
            experienceBillRecordResponseDataDto.setAmount(String.valueOf(input.getAmount()));
            experienceBillRecordResponseDataDto.setCreatedDate(new DateTime(input.getCreatedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            return experienceBillRecordResponseDataDto;
        });
    }
}

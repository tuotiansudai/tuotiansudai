package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppPointService;
import com.tuotiansudai.point.dto.SignInPointDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.service.SignInService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppPointServiceImpl implements MobileAppPointService {

    static Logger logger = Logger.getLogger(MobileAppPointServiceImpl.class);

    @Autowired
    private SignInService signInService;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PointTaskMapper pointTaskMapper;

    public BaseResponseDto signIn(BaseParamDto baseParamDto) {
        String loginName = baseParamDto.getBaseParam().getUserId();
        SignInPointDto signInPointDto = signInService.signIn(loginName);

        SignInResponseDataDto dataDto = new SignInResponseDataDto();
        dataDto.setPoint(signInPointDto.getPoint());
        dataDto.setSignInTimes(signInPointDto.getSignInCount());

        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);

        return dto;
    }

    @Override
    public BaseResponseDto queryPointBillList(PointBillRequestDto pointBillRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String loginName = pointBillRequestDto.getBaseParam().getUserId();
        Integer index = pointBillRequestDto.getIndex();
        Integer pageSize = pointBillRequestDto.getPageSize();
        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        PointBillResponseDataDto pointBillResponseDataDto = new PointBillResponseDataDto();
        pointBillResponseDataDto.setIndex(index);
        pointBillResponseDataDto.setPageSize(pageSize);
        pointBillResponseDataDto.setPointBills(convertPointBillRecordDto(pointBillMapper.findPointBillPagination(loginName, (index - 1) * pageSize, pageSize, null, null, null)));
        pointBillResponseDataDto.setTotalCount(pointBillMapper.findCountPointBillPagination(loginName, null, null, null));
        dto.setData(pointBillResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto queryPointTaskList(PointTaskRequestDto pointTaskRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = pointTaskRequestDto.getIndex();
        Integer pageSize = pointTaskRequestDto.getPageSize();
        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        PointTaskListResponseDataDto pointTaskListResponseDataDto = new PointTaskListResponseDataDto();
        pointTaskListResponseDataDto.setIndex(index);
        pointTaskListResponseDataDto.setPageSize(pageSize);
        pointTaskListResponseDataDto.setPointTasks(convertPointTaskRecordDto(pointTaskMapper.findPointTaskPagination((index - 1) * pageSize, pageSize)));
        pointTaskListResponseDataDto.setTotalCount(pointTaskMapper.findCountPointTaskPagination());
        dto.setData(pointTaskListResponseDataDto);
        return dto;
    }

    private List<PointTaskRecordResponseDataDto> convertPointTaskRecordDto(List<PointTaskModel> pointTaskList) {
        if (CollectionUtils.isEmpty(pointTaskList)) {
            return Lists.newArrayList();
        }
        return Lists.transform(pointTaskList, new Function<PointTaskModel, PointTaskRecordResponseDataDto>() {
            @Override
            public PointTaskRecordResponseDataDto apply(PointTaskModel pointTaskModel) {
                PointTaskRecordResponseDataDto pointTaskRecordResponseDataDto = new PointTaskRecordResponseDataDto();
                pointTaskRecordResponseDataDto.setPointTaskId("" + pointTaskModel.getId());
                pointTaskRecordResponseDataDto.setPointTaskTitle(pointTaskModel.getName().getTitle());
                pointTaskRecordResponseDataDto.setPointTaskType(pointTaskModel.getName());
                pointTaskRecordResponseDataDto.setPointTaskDesc(pointTaskModel.getName().getDescription());
                pointTaskRecordResponseDataDto.setPoint("" + pointTaskModel.getPoint());
                return pointTaskRecordResponseDataDto;
            }
        });

    }

    private List<PointBillRecordResponseDataDto> convertPointBillRecordDto(List<PointBillModel> userBillList) {

        return Lists.transform(userBillList, new Function<PointBillModel, PointBillRecordResponseDataDto>() {
            @Override
            public PointBillRecordResponseDataDto apply(PointBillModel input) {
                PointBillRecordResponseDataDto pointBillRecordResponseDataDto = new PointBillRecordResponseDataDto();
                pointBillRecordResponseDataDto.setBusinessType(input.getBusinessType().name());
                pointBillRecordResponseDataDto.setBusinessTypeName(input.getBusinessType().getDescription());
                pointBillRecordResponseDataDto.setPointBillId(String.valueOf(input.getId()));
                pointBillRecordResponseDataDto.setPoint(String.valueOf(input.getPoint()));
                pointBillRecordResponseDataDto.setCreatedDate(new DateTime(input.getCreatedTime()).toString("yyyy-MM-dd HH:mm:ss"));
                return pointBillRecordResponseDataDto;
            }
        });
    }

    @Override
    public BaseResponseDto queryPoint(BaseParamDto baseParamDto) {
        String loginName = baseParamDto.getBaseParam().getUserId();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        PointResponseDataDto dataDto = new PointResponseDataDto();
        dataDto.setPoint(accountModel.getPoint());

        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);

        return dto;
    }
}

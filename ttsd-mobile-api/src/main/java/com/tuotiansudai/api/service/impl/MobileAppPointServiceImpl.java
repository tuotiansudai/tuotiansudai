package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppPointService;
import com.tuotiansudai.point.dto.SignInPointDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import freemarker.core.ReturnInstruction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
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

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;
    public BaseResponseDto signIn(BaseParamDto baseParamDto) {
        String loginName = baseParamDto.getBaseParam().getUserId();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if(accountModel == null){
            return new BaseResponseDto(ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode(),ReturnMessage.USER_IS_NOT_CERTIFICATED.getMsg());
        }
        SignInPointDto signInPointDto = signInService.signIn(loginName);

        SignInResponseDataDto dataDto = new SignInResponseDataDto();
        dataDto.setPoint(signInPointDto.getSignInPoint());
        dataDto.setSignInTimes(signInPointDto.getSignInCount());

        BaseResponseDto dto = new BaseResponseDto();
        if (signInPointDto.getStatus()) {
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        } else {
            dto.setCode(ReturnMessage.MULTIPLE_SIGN_IN.getCode());
            dto.setMessage(ReturnMessage.MULTIPLE_SIGN_IN.getMsg());
        }
        dto.setData(dataDto);

        return dto;
    }

    public BaseResponseDto getLastSignInTime(BaseParamDto baseParamDto) {
        String loginName = baseParamDto.getBaseParam().getUserId();
        SignInPointDto lastSignInPointDto = signInService.getLastSignIn(loginName);

        LastSignInTimeResponseDataDto dataDto = new LastSignInTimeResponseDataDto();
        dataDto.setSignIn(signInService.signInIsSuccess(loginName));
        dataDto.setSignInTimes(lastSignInPointDto == null ? 0 : lastSignInPointDto.getSignInCount());

        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);
        return dto;
    }

    @Override
    public BaseResponseDto queryPointBillList(PointBillRequestDto pointBillRequestDto) {

        String loginName = pointBillRequestDto.getBaseParam().getUserId();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if(accountModel == null){
            return new BaseResponseDto(ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode(),ReturnMessage.USER_IS_NOT_CERTIFICATED.getMsg());
        }
        BaseResponseDto dto = new BaseResponseDto();
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
        String loginName = pointTaskRequestDto.getBaseParam().getUserId();
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = pointTaskRequestDto.getIndex();
        Integer pageSize = pointTaskRequestDto.getPageSize();
        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        List<PointTaskRecordResponseDataDto> pointTaskRecordDtos = convertPointTaskRecordDto(pointTaskMapper.findPointTaskPagination((index - 1) * pageSize, pageSize), loginName);
        sortPointTaskRecord(pointTaskRecordDtos);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        PointTaskListResponseDataDto pointTaskListResponseDataDto = new PointTaskListResponseDataDto();
        pointTaskListResponseDataDto.setIndex(index);
        pointTaskListResponseDataDto.setPageSize(pageSize);
        pointTaskListResponseDataDto.setPointTasks(pointTaskRecordDtos);
        pointTaskListResponseDataDto.setTotalCount(pointTaskMapper.findCountPointTaskPagination());
        dto.setData(pointTaskListResponseDataDto);
        return dto;
    }

    private List<PointTaskRecordResponseDataDto> convertPointTaskRecordDto(List<PointTaskModel> pointTaskList, String loginName) {
        List<PointTaskRecordResponseDataDto> pointTaskRecords = Lists.newArrayList();
        for (PointTaskModel pointTaskModel : pointTaskList) {
            PointTaskRecordResponseDataDto pointTaskRecordResponseDataDto = new PointTaskRecordResponseDataDto();
            UserPointTaskModel userPointTaskModel = userPointTaskMapper.findByLoginNameAndId(pointTaskModel.getId(), loginName);
            pointTaskRecordResponseDataDto.setPointTaskId("" + pointTaskModel.getId());
            pointTaskRecordResponseDataDto.setPointTaskTitle(pointTaskModel.getName().getTitle());
            pointTaskRecordResponseDataDto.setPointTaskType(pointTaskModel.getName());
            pointTaskRecordResponseDataDto.setPointTaskDesc(pointTaskModel.getName().getDescription());
            pointTaskRecordResponseDataDto.setPoint("" + pointTaskModel.getPoint());
            pointTaskRecordResponseDataDto.setCompleted(userPointTaskModel != null ? true : false);
            pointTaskRecords.add(pointTaskRecordResponseDataDto);
        }
        return pointTaskRecords;

    }

    
    private void sortPointTaskRecord(List<PointTaskRecordResponseDataDto> pointTaskRecordDtoList) {
        Collections.sort(pointTaskRecordDtoList, new Comparator<PointTaskRecordResponseDataDto>() {
            @Override
            public int compare(PointTaskRecordResponseDataDto first, PointTaskRecordResponseDataDto second) {
                if (first.isCompleted() && !second.isCompleted()) {
                    return 1;
                }
                if (!first.isCompleted() && second.isCompleted()) {
                    return -1;
                }
                return 0;
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
        if(accountModel == null){
            return new BaseResponseDto(ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode(),ReturnMessage.USER_IS_NOT_CERTIFICATED.getMsg());
        }

        PointResponseDataDto dataDto = new PointResponseDataDto();
        dataDto.setPoint(accountModel.getPoint());

        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);

        return dto;
    }
}

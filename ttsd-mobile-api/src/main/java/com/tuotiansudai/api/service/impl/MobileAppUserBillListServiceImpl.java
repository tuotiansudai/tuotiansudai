package com.tuotiansudai.api.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppUserBillListService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class MobileAppUserBillListServiceImpl implements MobileAppUserBillListService {
    static Logger logger = Logger.getLogger(MobileAppUserBillListServiceImpl.class);
    @Autowired
    private UserBillMapper userBillMapper;

    @Override
    public BaseResponseDto queryUserBillList(UserBillDetailListRequestDto userBillDetailListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String loginName = userBillDetailListRequestDto.getUserId();
        Integer index = userBillDetailListRequestDto.getIndex();
        Integer pageSize = userBillDetailListRequestDto.getPageSize();

        if(index == null || index <= 0){
            index = 1;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
        List<UserBillModel> userBillModels = userBillMapper.findUserBills(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("loginName", loginName)
                .put("indexPage", (index - 1) * pageSize)
                .put("pageSize", pageSize).build()));

        int count = userBillMapper.findUserBillsCount(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("loginName", loginName)
                .build()));
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        UserBillDetailListResponseDataDto userBillDetailListResponseDataDto = new UserBillDetailListResponseDataDto();
        userBillDetailListResponseDataDto.setIndex(index);
        userBillDetailListResponseDataDto.setPageSize(pageSize);
        userBillDetailListResponseDataDto.setUserBillList(convertUserBillRecordDto(userBillModels));
        userBillDetailListResponseDataDto.setTotalCount(count);
        dto.setData(userBillDetailListResponseDataDto);
        return dto;
    }

    private List<UserBillRecordResponseDataDto> convertUserBillRecordDto(List<UserBillModel> userBillList) {
        List<UserBillRecordResponseDataDto> userBillRecords = Lists.newArrayList();
        for (UserBillModel userBill:userBillList){
            UserBillRecordResponseDataDto userBillRecordResponseDataDto = new UserBillRecordResponseDataDto();
            userBillRecordResponseDataDto.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userBill.getCreatedTime()));
            userBillRecordResponseDataDto.setMoney(CommonUtils.convertRealMoneyByType(userBill.getAmount(), userBill.getOperationType()));
            userBillRecordResponseDataDto.setTypeInfo(userBill.getBusinessType());
            userBillRecordResponseDataDto.setTypeInfoDesc(userBill.getBusinessType().getDescription());
            userBillRecordResponseDataDto.setFrozenMoney(AmountConverter.convertCentToString(userBill.getFreeze()));
            userBillRecordResponseDataDto.setDetail(userBill.getOperationType().getDescription());
            userBillRecords.add(userBillRecordResponseDataDto);
        }
        return userBillRecords;
    }
}

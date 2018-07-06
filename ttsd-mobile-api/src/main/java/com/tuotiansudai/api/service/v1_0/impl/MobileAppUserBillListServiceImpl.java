package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserBillListService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.enums.BankUserBillOperationType;
import com.tuotiansudai.repository.mapper.BankUserBillMapper;
import com.tuotiansudai.repository.model.BankUserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppUserBillListServiceImpl implements MobileAppUserBillListService {

    private static Logger logger = Logger.getLogger(MobileAppUserBillListServiceImpl.class);

    @Autowired
    private BankUserBillMapper bankUserBillMapper;

    @Autowired
    private PageValidUtils pageValidUtils;

    private final static Map<UserBillCategory, BankUserBillOperationType> OPERATION_TYPE = Maps.newHashMap(ImmutableMap.<UserBillCategory, BankUserBillOperationType>builder()
            .put(UserBillCategory.INCOMING, BankUserBillOperationType.IN)
            .put(UserBillCategory.EXPENSE, BankUserBillOperationType.OUT)
            .build());

    @Override
    public BaseResponseDto<UserBillDetailListResponseDataDto> queryUserBillList(UserBillDetailListRequestDto userBillDetailListRequestDto) {
        BaseResponseDto<UserBillDetailListResponseDataDto> dto = new BaseResponseDto<>();
        String loginName = userBillDetailListRequestDto.getUserId();
        Integer index = userBillDetailListRequestDto.getIndex();
        if (index == null || index <= 0) {
            index = 1;
        }
        Integer pageSize = pageValidUtils.validPageSizeLimit(userBillDetailListRequestDto.getPageSize());
        UserBillCategory userBillCategory = userBillDetailListRequestDto.getUserBillCategory();
        BankUserBillOperationType operationType = userBillCategory != null ? OPERATION_TYPE.get(userBillCategory) : null;

        List<BankUserBillModel> userBillModels = bankUserBillMapper.findBills(loginName, null, null, operationType, null, null, (index - 1) * pageSize, pageSize);

        long count = bankUserBillMapper.countBills(loginName, null, null, operationType, null, null);

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

    private List<UserBillRecordResponseDataDto> convertUserBillRecordDto(List<BankUserBillModel> bankUserBillModels) {
        List<UserBillRecordResponseDataDto> userBillRecords = Lists.newArrayList();
        for (BankUserBillModel userBill : bankUserBillModels) {
            UserBillRecordResponseDataDto userBillRecordResponseDataDto = new UserBillRecordResponseDataDto();
            userBillRecordResponseDataDto.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userBill.getCreatedTime()));
            userBillRecordResponseDataDto.setMoney(CommonUtils.convertRealMoneyByType(userBill.getAmount(), userBill.getOperationType()));
            userBillRecordResponseDataDto.setTypeInfo(userBill.getBusinessType());
            userBillRecordResponseDataDto.setTypeInfoDesc(userBill.getBusinessType().getDescription());
            userBillRecordResponseDataDto.setDetail(userBill.getOperationType().getDescription());
            userBillRecords.add(userBillRecordResponseDataDto);
        }
        return userBillRecords;
    }
}

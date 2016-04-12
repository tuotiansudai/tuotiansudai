package com.ttsd.api.service.impl;

import com.esoft.archer.system.controller.DictUtil;
import com.esoft.archer.user.model.UserBill;
import com.esoft.core.annotations.Logger;
import com.ttsd.api.dao.MobileAppUserBillListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppUserBillListService;
import com.ttsd.api.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppUserBillListServiceImpl implements MobileAppUserBillListService {
    @Logger
    static Log log;

    @Autowired
    private MobileAppUserBillListDao mobileAppUserBillListDao;

    @Override
    public BaseResponseDto queryUserBillList(UserBillDetailListRequestDto userBillDetailListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String userId = userBillDetailListRequestDto.getUserId();
        Integer index = userBillDetailListRequestDto.getIndex();
        Integer pageSize = userBillDetailListRequestDto.getPageSize();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        List<UserBillRecordResponseDataDto> userBillRecords = null;
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
            log.info("资金管理_交易纪录" + ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode() + ":" + ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        if (returnCode.equals(ReturnMessage.SUCCESS.getCode())){
            List<UserBill> userBills = mobileAppUserBillListDao.getUserBillList(index, pageSize, userId);

            userBillRecords = convertUserBillRecordDto(userBills);
        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (returnCode.equals(ReturnMessage.SUCCESS.getCode()) && userBillRecords != null){
            UserBillDetailListResponseDataDto userBillDetailListResponseDataDto = new UserBillDetailListResponseDataDto();
            userBillDetailListResponseDataDto.setIndex(index);
            userBillDetailListResponseDataDto.setPageSize(pageSize);
            userBillDetailListResponseDataDto.setTotalCount(mobileAppUserBillListDao.getTotalCount(userId));
            userBillDetailListResponseDataDto.setUserBillList(userBillRecords);
            dto.setData(userBillDetailListResponseDataDto);
        }

        return dto;

    }

    @Override
    public List<UserBillRecordResponseDataDto> convertUserBillRecordDto(List<UserBill> userBillList) {
        DictUtil dictUtil = new DictUtil();
        List<UserBillRecordResponseDataDto> userBillRecords = new ArrayList<UserBillRecordResponseDataDto>();
        for (UserBill userBill:userBillList){
            UserBillRecordResponseDataDto userBillRecordResponseDataDto = new UserBillRecordResponseDataDto();
            userBillRecordResponseDataDto.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userBill.getTime()));
            userBillRecordResponseDataDto.setMoney(CommonUtils.convertRealMoneyByType(userBill.getMoney(), userBill.getType()));
            userBillRecordResponseDataDto.setTypeInfo(userBill.getTypeInfo());
            userBillRecordResponseDataDto.setTypeInfoDesc(dictUtil.getValue("bill_operator", userBill.getTypeInfo()));
            userBillRecordResponseDataDto.setFrozenMoney(userBill.getFrozenMoney() + "");
            userBillRecordResponseDataDto.setDetail(userBill.getDetail());
            userBillRecords.add(userBillRecordResponseDataDto);
        }
        return userBillRecords;
    }
}

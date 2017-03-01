package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.MoneyTreePrizeService;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMoneyTreeService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppMoneyTreeServiceImpl implements MobileAppMoneyTreeService {

    @Autowired
    private MoneyTreePrizeService moneyTreePrizeService;

    @Autowired
    private UserMapper userMapper;

    private static final int INVITE_FRIENDS_MAX_COUNT_DAY = 3;

    @Override
    public BaseResponseDto<MoneyTreeLeftCountResponseDataDto> generateLeftCount(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        MoneyTreeLeftCountResponseDataDto moneyTreeLeftCountResponseDataDto = new MoneyTreeLeftCountResponseDataDto();
        moneyTreeLeftCountResponseDataDto.setLeftCount(moneyTreePrizeService.getLeftDrawPrizeTime(userModel.getMobile()));
        BaseResponseDto<MoneyTreeLeftCountResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(moneyTreeLeftCountResponseDataDto);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }

    @Override
    public BaseResponseDto<MoneyTreePrizeResponseDataDto> generatePrize(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        DrawLotteryResultDto drawLotteryResultDto = moneyTreePrizeService.drawLotteryPrize(userModel.getMobile());
        MoneyTreePrizeResponseDataDto moneyTreePrizeResponseDataDto = new MoneyTreePrizeResponseDataDto(drawLotteryResultDto);
        moneyTreePrizeResponseDataDto.setLeftCount(moneyTreePrizeService.getLeftDrawPrizeTime(userModel.getMobile()));
        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(new DateTime().withTimeAtStartOfDay().toDate(), new DateTime().withTimeAtStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59).toDate(), userModel.getLoginName());
        int leftInviteFriendsCount = userModels.size() > 3 ? 0 : (INVITE_FRIENDS_MAX_COUNT_DAY - userModels.size());
        moneyTreePrizeResponseDataDto.setLeftInviteFriendsCount(leftInviteFriendsCount);
        BaseResponseDto<MoneyTreePrizeResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(moneyTreePrizeResponseDataDto);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }

    @Override
    public BaseResponseDto<MoneyTreeResultListResponseDataDto> generatePrizeListTop10() {
        BaseResponseDto dto = new BaseResponseDto();
        List<UserLotteryPrizeView> userLotteryPrizeViewList = moneyTreePrizeService.findDrawLotteryPrizeRecordTop10();
        MoneyTreeResultListResponseDataDto moneyTreeResultListResponseDataDto = new MoneyTreeResultListResponseDataDto();
        List<MoneyTreeResultResponseDataDto> moneyTreeResultResponseDataList =
                userLotteryPrizeViewList.stream()
                        .filter(n -> n.getPrizeValue() != LotteryPrize.MONEY_TREE_1000_EXPERIENCE_GOLD_0.getDescription())
                        .map(n -> new MoneyTreeResultResponseDataDto(n))
                        .collect(Collectors.toList());
        moneyTreeResultListResponseDataDto.setPrizeList(moneyTreeResultResponseDataList);
        dto.setData(moneyTreeResultListResponseDataDto);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }

    @Override
    public BaseResponseDto<MoneyTreeResultListResponseDataDto> generateMyPrizeList(String loginName) {
        BaseResponseDto dto = new BaseResponseDto();
        UserModel userModel = userMapper.findByLoginName(loginName);
        List<UserLotteryPrizeView> userLotteryPrizeViewList = moneyTreePrizeService.findDrawLotteryPrizeRecordByMobile(userModel.getMobile());
        MoneyTreeResultListResponseDataDto moneyTreeResultListResponseDataDto = new MoneyTreeResultListResponseDataDto();
        List<MoneyTreeResultResponseDataDto> moneyTreeResultResponseDataList =
                userLotteryPrizeViewList.stream()
                        .filter(n -> n.getPrizeValue() != LotteryPrize.MONEY_TREE_1000_EXPERIENCE_GOLD_0.getDescription())
                        .map(n -> new MoneyTreeResultResponseDataDto(n))
                        .collect(Collectors.toList());
        moneyTreeResultListResponseDataDto.setPrizeList(moneyTreeResultResponseDataList);
        dto.setData(moneyTreeResultListResponseDataDto);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }
}

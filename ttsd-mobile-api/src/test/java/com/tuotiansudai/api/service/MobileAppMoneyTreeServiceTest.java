package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.MoneyTreePrizeService;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MoneyTreeLeftCountResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.MoneyTreeResultListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppMoneyTreeServiceImpl;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppMoneyTreeServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppMoneyTreeServiceImpl mobileAppMoneyTreeService;

    @Mock
    protected UserMapper userMapper;

    @Mock
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Mock
    private MoneyTreePrizeService moneyTreePrizeService;

    @Test
    public void shouldGenerateLeftCount() {
        UserModel userModel = getFakeUser("testMontyTree");
        userModel.setMobile("13888888888");

        UserModel userModelReferrer1 = getFakeUser("testMoneyTree2");
        userModelReferrer1.setRegisterTime(new DateTime().plusDays(1).plusHours(15).toDate());
        userModelReferrer1.setReferrer(userModel.getLoginName());

        UserModel userModelReferrer2 = getFakeUser("testMoneyTree3");
        userModelReferrer2.setRegisterTime(new DateTime().plusDays(1).plusHours(12).toDate());
        userModelReferrer2.setReferrer(userModel.getLoginName());

        UserModel userModelReferrer3 = getFakeUser("testMoneyTree3");
        userModelReferrer3.setRegisterTime(new DateTime().plusDays(1).plusHours(11).toDate());
        userModelReferrer3.setReferrer(userModel.getLoginName());

        List<UserModel> userModelList = Lists.newArrayList(userModelReferrer1, userModelReferrer2, userModelReferrer3);

        when(userMapper.findByLoginName(anyString())).thenReturn(userModel);
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(userMapper.findUsersByRegisterTimeOrReferrer(anyObject(), anyObject(), anyString())).thenReturn(userModelList);
        when(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(anyString(), anyObject(), anyObject(), anyObject(), anyObject())).thenReturn(0);
        when(moneyTreePrizeService.getLeftDrawPrizeTime(anyString())).thenReturn(4);

        BaseResponseDto<MoneyTreeLeftCountResponseDataDto> dataDto = mobileAppMoneyTreeService.generateLeftCount(userModel.getLoginName());

        assertThat("0000", is(dataDto.getCode()));
        assertThat(4, is(dataDto.getData().getLeftCount()));
    }

    @Test
    public void shouldGeneratePrizeListTop10() {
        UserLotteryPrizeView userLotteryPrizeView = this.getFakeUserLotteryPrizeView("testDrawMoneyTree1");
        UserLotteryPrizeView userLotteryPrizeView2 = this.getFakeUserLotteryPrizeView("testDrawMoneyTree2");
        UserLotteryPrizeView userLotteryPrizeView3 = this.getFakeUserLotteryPrizeView("testDrawMoneyTree3");

        List<UserLotteryPrizeView> userLotteryPrizeViewList = Lists.newArrayList(userLotteryPrizeView, userLotteryPrizeView2, userLotteryPrizeView3);
        when(moneyTreePrizeService.findDrawLotteryPrizeRecordTop10()).thenReturn(userLotteryPrizeViewList);
        BaseResponseDto<MoneyTreeResultListResponseDataDto> dataDto = mobileAppMoneyTreeService.generatePrizeListTop10();

        assertThat("0000", is(dataDto.getCode()));
        assertThat(3, is(dataDto.getData().getPrizeList().size()));

    }

    private UserLotteryPrizeView getFakeUserLotteryPrizeView(String loginName) {
        UserLotteryPrizeView userLotteryPrizeView = new UserLotteryPrizeView();
        userLotteryPrizeView.setLoginName(loginName);
        userLotteryPrizeView.setMobile(RandomStringUtils.randomNumeric(11));
        userLotteryPrizeView.setLotteryTime(new Date());
        userLotteryPrizeView.setPrize(LotteryPrize.MONEY_TREE_1000_EXPERIENCE_GOLD_50);
        userLotteryPrizeView.setPrizeValue(LotteryPrize.MONEY_TREE_1000_EXPERIENCE_GOLD_50.getDescription());
        return userLotteryPrizeView;
    }
}

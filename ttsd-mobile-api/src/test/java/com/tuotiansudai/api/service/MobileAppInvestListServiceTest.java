package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppInvestListServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppInvestListServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppInvestListServiceImpl mobileAppInvestListService;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestService investService;

    @Mock
    private RandomUtils randomUtils;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private InvestTransferService investTransferService;

    @Mock
    private PageValidUtils pageValidUtils;

    @Mock
    private CouponService couponService;

    @Mock
    private UserMapper userMapper;

    private static int INVEST_COUNT = 110;
    private static long INTEREST = 1100L;

    @Test
    public void shouldGenerateInvestListIsOk() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setAmount(1000000L);
        investModel1.setInvestTime(new Date());
        investModel1.setTradingTime(new Date());
        investModel1.setId(IdGenerator.generate());
        investModel1.setLoginName("loginName1");
        investModel1.setLoanId(IdGenerator.generate());
        investModel1.setSource(Source.ANDROID);
        investModel1.setStatus(InvestStatus.SUCCESS);
        investModel1.setAchievements(Lists.newArrayList(InvestAchievement.MAX_AMOUNT));

        InvestModel investModel2 = new InvestModel();
        investModel2.setAmount(1100000L);
        investModel2.setInvestTime(new Date());
        investModel2.setTradingTime(new Date());
        investModel2.setId(IdGenerator.generate());
        investModel2.setLoginName("loginName2");
        investModel2.setLoanId(IdGenerator.generate());
        investModel2.setSource(Source.WEB);
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setAchievements(Lists.newArrayList(InvestAchievement.LAST_INVEST));

        InvestModel investModel3 = new InvestModel();
        investModel3.setAmount(1200000L);
        investModel3.setInvestTime(new Date());
        investModel3.setTradingTime(new Date());
        investModel3.setId(IdGenerator.generate());
        investModel3.setLoginName("loginName3");
        investModel3.setLoanId(IdGenerator.generate());
        investModel3.setSource(Source.IOS);
        investModel3.setStatus(InvestStatus.SUCCESS);
        investModel3.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));

        List<InvestModel> investModels = Lists.newArrayList();
        investModels.add(investModel1);
        investModels.add(investModel2);
        investModels.add(investModel3);


        when(investMapper.findByStatus(anyLong(), anyInt(), anyInt(), any(InvestStatus.class))).thenReturn(investModels);

        when(investMapper.findCountByStatus(anyLong(), any(InvestStatus.class))).thenReturn(3L);

        when(randomUtils.encryptMobile(anyString(), anyString(), anyLong())).thenReturn("log***");
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);
        when(couponService.findCouponByUserGroup(anyList())).thenReturn(Lists.newArrayList());
        when(userMapper.findByLoginName(anyString())).thenReturn(new UserModel());
        when(loanMapper.findById(anyLong())).thenReturn(new LoanModel());

        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("");
        investListRequestDto.setBaseParam(baseParam);
        investListRequestDto.setLoanId("1111");
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        BaseResponseDto<InvestListResponseDataDto> baseResponseDto = mobileAppInvestListService.generateInvestList(investListRequestDto);


        assertEquals("10000.00", baseResponseDto.getData().getInvestRecord().get(0).getInvestMoney());
        assertEquals("log***", baseResponseDto.getData().getInvestRecord().get(0).getUserName());
        assertThat(baseResponseDto.getData().getInvestRecord().get(0).getAchievements(), Is.<List<InvestAchievement>>is(Lists.newArrayList(InvestAchievement.MAX_AMOUNT)));
        assertEquals("11000.00", baseResponseDto.getData().getInvestRecord().get(1).getInvestMoney());
        assertEquals("log***", baseResponseDto.getData().getInvestRecord().get(1).getUserName());
        assertThat(baseResponseDto.getData().getInvestRecord().get(1).getAchievements(), Is.<List<InvestAchievement>>is(Lists.newArrayList(InvestAchievement.LAST_INVEST)));
        assertEquals("12000.00", baseResponseDto.getData().getInvestRecord().get(2).getInvestMoney());
        assertEquals("log***", baseResponseDto.getData().getInvestRecord().get(2).getUserName());
        assertThat(baseResponseDto.getData().getInvestRecord().get(2).getAchievements(), Is.<List<InvestAchievement>>is(Lists.newArrayList(InvestAchievement.FIRST_INVEST)));
    }


    private List<InvestModel> generateMockedInvestList() {
        List<InvestModel> investModelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            InvestModel investModel = generateMockedInvestModel();
            investModelList.add(investModel);
        }
        return investModelList;
    }

    private InvestModel generateMockedInvestModel() {
        InvestModel investModel = new InvestModel();
        investModel.setId(IdGenerator.generate());
        investModel.setAmount(1000);
        investModel.setInvestTime(new Date());
        investModel.setTradingTime(new Date());
        investModel.setLoginName("loginName");
        investModel.setSource(Source.IOS);
        investModel.setLoanId(1213L);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.TRANSFERABLE);
        return investModel;
    }

    private LoanModel generateMockedLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setName("test loan name");
        loanModel.setCreatedTime(new Date());
        loanModel.setId(IdGenerator.generate());
        return loanModel;
    }

    @Test
    public void shouldGenerateUserInvestList() {
        when(investMapper.findByLoginNameExceptTransfer(anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(generateMockedInvestList());
        when(investMapper.findCountByLoginNameExceptTransfer(anyString())).thenReturn((long) INVEST_COUNT);
        when(loanMapper.findById(anyLong())).thenReturn(generateMockedLoanModel());
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(Lists.<InvestRepayModel>newArrayList());
        when(investService.estimateInvestIncome(anyLong(), anyDouble(), anyString(), anyLong(), any(Date.class))).thenReturn(INTEREST);
        when(investTransferService.isTransferable(anyLong())).thenReturn(true);
        when(loanRepayMapper.findEnabledLoanRepayByLoanId(anyLong())).thenReturn(null);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);


        UserInvestListRequestDto requestDto = new UserInvestListRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        requestDto.setIndex(1);
        requestDto.setPageSize(10);
        requestDto.setTransferStatus(Lists.newArrayList(TransferStatus.TRANSFERABLE, TransferStatus.TRANSFERRING, TransferStatus.SUCCESS));
        BaseResponseDto<UserInvestListResponseDataDto> responseDto = mobileAppInvestListService.generateUserInvestList(requestDto);
        UserInvestListResponseDataDto dataDto = responseDto.getData();

        assertEquals(INVEST_COUNT, dataDto.getTotalCount().intValue());
        assertEquals(10, dataDto.getInvestList().size());
        assertEquals(com.tuotiansudai.api.dto.v1_0.InvestStatus.BID_SUCCESS.getCode(), dataDto.getInvestList().get(0).getInvestStatus());
        assertEquals(com.tuotiansudai.api.dto.v1_0.LoanStatus.RAISING.getCode(), dataDto.getInvestList().get(0).getLoanStatus());
        assertThat(dataDto.getInvestList().get(0).getTransferStatus(), is(TransferStatus.TRANSFERABLE.name()));
    }


    @Test
    public void shouldLoanAchievementsIsOk() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setId(1);
        investModel1.setAmount(1000000L);
        investModel1.setInvestTime(new Date());
        investModel1.setInvestTime(new Date());
        investModel1.setId(IdGenerator.generate());
        investModel1.setLoginName("loginName1");
        investModel1.setLoanId(IdGenerator.generate());
        investModel1.setSource(Source.ANDROID);
        investModel1.setStatus(InvestStatus.SUCCESS);
        investModel1.setAchievements(Lists.newArrayList(InvestAchievement.MAX_AMOUNT));

        InvestModel investModel2 = new InvestModel();
        investModel2.setId(2);
        investModel2.setAmount(1100000L);
        investModel2.setInvestTime(new Date());
        investModel2.setInvestTime(new Date());
        investModel2.setId(IdGenerator.generate());
        investModel2.setLoginName("loginName2");
        investModel2.setLoanId(IdGenerator.generate());
        investModel2.setSource(Source.WEB);
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setAchievements(Lists.newArrayList(InvestAchievement.LAST_INVEST));

        InvestModel investModel3 = new InvestModel();
        investModel3.setId(3);
        investModel3.setAmount(1200000L);
        investModel3.setInvestTime(new Date());
        investModel3.setInvestTime(new Date());
        investModel3.setId(IdGenerator.generate());
        investModel3.setLoginName("loginName3");
        investModel3.setLoanId(IdGenerator.generate());
        investModel3.setSource(Source.IOS);
        investModel3.setStatus(InvestStatus.SUCCESS);
        investModel3.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));

        List<InvestModel> investModels = Lists.newArrayList();
        investModels.add(investModel1);
        investModels.add(investModel2);
        investModels.add(investModel3);

        LoanModel loanModel = new LoanModel();
        loanModel.setFirstInvestAchievementId(investModel1.getId());
        loanModel.setLastInvestAchievementId(investModel2.getId());
        loanModel.setMaxAmountAchievementId(investModel3.getId());

        CouponModel couponModel = new CouponModel();
        couponModel.setUserGroup(UserGroup.FIRST_INVEST_ACHIEVEMENT);
        couponModel.setCouponType(CouponType.RED_ENVELOPE);
        couponModel.setAmount(1000);
        CouponModel couponModel1 = new CouponModel();
        couponModel1.setUserGroup(UserGroup.FIRST_INVEST_ACHIEVEMENT);
        couponModel1.setCouponType(CouponType.INVEST_COUPON);
        couponModel1.setRate(0.002);

        UserModel userModel = new UserModel();
        userModel.setMobile("15210001111");

        List<CouponModel> couponModels = Lists.newArrayList(couponModel, couponModel1);

        when(investMapper.findByStatus(anyLong(), anyInt(), anyInt(), any(InvestStatus.class))).thenReturn(investModels);
        when(investMapper.findCountByStatus(anyLong(), any(InvestStatus.class))).thenReturn(3L);
        when(randomUtils.encryptMobile(anyString(), anyString(), anyLong())).thenReturn("log***");
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);
        when(couponService.findCouponByUserGroup(anyList())).thenReturn(couponModels);
        when(userMapper.findByLoginName(anyString())).thenReturn(userModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.findById(anyLong())).thenReturn(investModel1);
        when(randomUtils.encryptMobile(anyString(), anyString(), anyLong())).thenReturn("152**11");

        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("");
        investListRequestDto.setBaseParam(baseParam);
        investListRequestDto.setLoanId("1111");
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        BaseResponseDto<InvestListResponseDataDto> baseResponseDto = mobileAppInvestListService.generateInvestList(investListRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(baseResponseDto.getData().getAchievements()));
        assertTrue(baseResponseDto.getData().getAchievements().size() == 3);
    }

    @Test
    public void shouldLoanIsNullIsOk() {
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);
        when(investMapper.findCountByStatus(anyLong(), any(InvestStatus.class))).thenReturn(3L);
        when(investMapper.findByStatus(anyLong(), anyInt(), anyInt(), any(InvestStatus.class))).thenReturn(getAchievement());
        when(randomUtils.encryptMobile(anyString(), anyString(), anyLong())).thenReturn("log***");
        when(loanMapper.findById(anyLong())).thenReturn(null);

        InvestListRequestDto investListRequestDto = new InvestListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("");
        investListRequestDto.setBaseParam(baseParam);
        investListRequestDto.setLoanId("1111");
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        BaseResponseDto<InvestListResponseDataDto> baseResponseDto = mobileAppInvestListService.generateInvestList(investListRequestDto);

        assertTrue(baseResponseDto.getCode().equals(ReturnMessage.LOAN_NOT_FOUND.getCode()));
        assertTrue(baseResponseDto.getMessage().equals(ReturnMessage.LOAN_NOT_FOUND.getMsg()));
    }

    private List getAchievement() {
        InvestModel investModel1 = new InvestModel();
        investModel1.setAmount(1000000L);
        investModel1.setInvestTime(new Date());
        investModel1.setInvestTime(new Date());
        investModel1.setId(IdGenerator.generate());
        investModel1.setLoginName("loginName1");
        investModel1.setLoanId(IdGenerator.generate());
        investModel1.setSource(Source.ANDROID);
        investModel1.setStatus(InvestStatus.SUCCESS);
        investModel1.setAchievements(Lists.newArrayList(InvestAchievement.MAX_AMOUNT));

        InvestModel investModel2 = new InvestModel();
        investModel2.setAmount(1100000L);
        investModel2.setInvestTime(new Date());
        investModel2.setInvestTime(new Date());
        investModel2.setId(IdGenerator.generate());
        investModel2.setLoginName("loginName2");
        investModel2.setLoanId(IdGenerator.generate());
        investModel2.setSource(Source.WEB);
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setAchievements(Lists.newArrayList(InvestAchievement.LAST_INVEST));

        InvestModel investModel3 = new InvestModel();
        investModel3.setAmount(1200000L);
        investModel3.setInvestTime(new Date());
        investModel3.setInvestTime(new Date());
        investModel3.setId(IdGenerator.generate());
        investModel3.setLoginName("loginName3");
        investModel3.setLoanId(IdGenerator.generate());
        investModel3.setSource(Source.IOS);
        investModel3.setStatus(InvestStatus.SUCCESS);
        investModel3.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));

        List<InvestModel> investModels = Lists.newArrayList(investModel1, investModel2, investModel3);
        return investModels;
    }
}

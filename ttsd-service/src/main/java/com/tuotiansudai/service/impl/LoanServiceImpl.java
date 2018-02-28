package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.dto.LoanPaginationItemDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    static Logger logger = Logger.getLogger(LoanServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired

    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Value("#{'${web.random.investor.list}'.split('\\|')}")
    private List<String> showRandomLoginNameList;

    @Autowired
    private CouponService couponService;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Override
    public LoanModel findLoanById(long loanId) {
        return loanMapper.findById(loanId);
    }

    @Override
    public BaseDto<BasePaginationDataDto> getLoanerLoanData(String loginName, int index, int pageSize, LoanStatus status, Date startTime, Date endTime) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = CalculateUtil.calculateMaxDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusSeconds(1).toDate();
        }

        List<LoanModel> loanModels = Lists.newArrayList();
        long count = 0;
        if (LoanStatus.REPAYING == status) {
            count = loanMapper.findCountRepayingByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findRepayingPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.COMPLETE == status) {
            count = loanMapper.findCountCompletedByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = PaginationUtil.calculateMaxPage(count,pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCompletedPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.CANCEL == status) {
            count = loanMapper.findCountCanceledByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = PaginationUtil.calculateMaxPage(count,pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCanceledPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        List<LoanPaginationItemDataDto> records = Lists.transform(loanModels, new Function<LoanModel, LoanPaginationItemDataDto>() {
            @Override
            public LoanPaginationItemDataDto apply(LoanModel input) {
                return new LoanPaginationItemDataDto(input);
            }
        });

        BasePaginationDataDto<LoanPaginationItemDataDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dataDto.setStatus(true);

        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    @Override
    public List<LoanItemDto> findLoanItems(String name, LoanStatus status, double rateStart, double rateEnd, int durationStart, int durationEnd, int index) {
        index = (index - 1) * 10;

        List<LoanModel> loanModels = loanMapper.findLoanListWeb(name, status, rateStart, rateEnd, durationStart, durationEnd, index);

        final List<CouponModel> allActiveCoupons = couponMapper.findAllActiveCoupons();

        CouponModel newbieInterestCouponModel = null;
        for (CouponModel activeCoupon : allActiveCoupons) {
            if (activeCoupon.getCouponType() == CouponType.INTEREST_COUPON
                    && activeCoupon.getUserGroup() == UserGroup.NEW_REGISTERED_USER
                    && activeCoupon.getProductTypes().contains(ProductType._30)
                    && (newbieInterestCouponModel == null || activeCoupon.getRate() > newbieInterestCouponModel.getRate())) {
                newbieInterestCouponModel = activeCoupon;
            }
        }
        final double newbieInterestCouponRate = newbieInterestCouponModel != null ? newbieInterestCouponModel.getRate() : 0;

        return Lists.transform(loanModels, new Function<LoanModel, LoanItemDto>() {
            @Override
            public LoanItemDto apply(LoanModel loanModel) {
                LoanItemDto loanItemDto = new LoanItemDto();
                loanItemDto.setId(loanModel.getId());
                loanItemDto.setName(loanModel.getName());
                loanItemDto.setProductType(loanModel.getProductType());
                loanItemDto.setBaseRate(new BigDecimal(loanModel.getBaseRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                loanItemDto.setActivityRate(new BigDecimal(loanModel.getActivityRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                loanItemDto.setPeriods(loanModel.getPeriods());
                loanItemDto.setType(loanModel.getType());
                loanItemDto.setStatus(loanModel.getStatus());
                loanItemDto.setLoanAmount(loanModel.getLoanAmount());
                loanItemDto.setActivityType(loanModel.getActivityType());
                loanItemDto.setInterestCouponRate(new BigDecimal(String.valueOf(newbieInterestCouponRate)).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                loanItemDto.setMinInvestAmount(loanModel.getMinInvestAmount());
                BigDecimal loanAmountBigDecimal = new BigDecimal(loanModel.getLoanAmount());
                BigDecimal sumInvestAmountBigDecimal = new BigDecimal(investMapper.sumSuccessInvestAmount(loanModel.getId()));
                if (LoanStatus.PREHEAT == loanModel.getStatus()) {
                    loanItemDto.setAlert(MessageFormat.format("{0} 元", AmountConverter.convertCentToString(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()))));
                    loanItemDto.setAlertAmount(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()));
                    loanItemDto.setProgress(0.0);
                    loanItemDto.setFundraisingStartTime(loanModel.getFundraisingStartTime());
                    loanItemDto.setPreheatSeconds((loanModel.getFundraisingStartTime().getTime() - System.currentTimeMillis()) / 1000);
                }
                if (LoanStatus.RAISING == loanModel.getStatus()) {
                    loanItemDto.setAlert(MessageFormat.format("{0} 元", AmountConverter.convertCentToString(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()))));
                    loanItemDto.setAlertAmount(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()));
                    if(loanModel.getProductType() != ProductType.EXPERIENCE){
                        loanItemDto.setProgress(sumInvestAmountBigDecimal.divide(loanAmountBigDecimal, 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue());
                    }
                }
                if (LoanStatus.RECHECK == loanModel.getStatus()) {
                    loanItemDto.setAlert("放款审核");
                    loanItemDto.setProgress(100);
                }
                if (Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE, LoanStatus.COMPLETE).contains(loanModel.getStatus())) {
                    loanItemDto.setAlert(MessageFormat.format("还款进度：{0}/{1}期", loanRepayMapper.sumSuccessLoanRepayMaxPeriod(loanModel.getId()), loanModel.getPeriods()));
                    loanItemDto.setProgress(100);
                }
                if (loanItemDto.getProductType() == ProductType.EXPERIENCE) {
                    Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
                    Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
                    List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(loanModel.getId(), beginTime, endTime);
                    long investCount = investModelList.size() % 100;
                    long investAmount = couponService.findExperienceInvestAmount(investModelList);
                    loanItemDto.setAlert(AmountConverter.convertCentToString(loanModel.getLoanAmount() - investAmount));
                    loanItemDto.setProgress(investCount);
                }
                loanItemDto.setDuration(loanModel.getDuration());
                double rate = extraLoanRateMapper.findMaxRateByLoanId(loanModel.getId());
                List<Source> extraSource = Lists.newArrayList();
                boolean activity = false;
                String activityDesc = "";
                LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
                if (loanDetailsModel != null) {
                    extraSource = loanDetailsModel.getExtraSource();
                    activity = loanDetailsModel.isActivity();
                    activityDesc = loanDetailsModel.getActivityDesc();
                }
                if (rate > 0) {
                    loanItemDto.setExtraRate(rate * 100);
                    loanItemDto.setExtraSource((extraSource.size() == 1 && extraSource.contains(Source.MOBILE)) ? Source.MOBILE.name() : "");
                }
                loanItemDto.setActivity(activity);
                loanItemDto.setActivityDesc(activityDesc);
                return loanItemDto;
            }
        });
    }

    @Override
    public int findLoanListCountWeb(String name, LoanStatus status, double rateStart, double rateEnd, int durationStart, int durationEnd) {
        return loanMapper.findLoanListCountWeb(name, status, rateStart, rateEnd, durationStart, durationEnd);
    }
}

package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.SiteMapRedisWrapperClient;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.HomeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    static Logger logger = Logger.getLogger(HomeServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private SiteMapRedisWrapperClient siteMapRedisWrapperClient;

    private static final String CMS_CATEGORY = "cms:sitemap:category:{0}";

    public List<HomeLoanDto> getNormalLoans() {
        List<LoanModel> homePreferableLoans = loanMapper.findHomePreferableLoans();
        return homePreferableLoans.stream().map(loanModel -> {
            long investAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());
            List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());

            return new HomeLoanDto(null,
                    loanModel,
                    investAmount,
                    loanRepayModels,
                    extraLoanRateMapper.findMaxRateByLoanId(loanModel.getId()),
                    loanDetailsModel.getExtraSource(), loanDetailsModel.isActivity(), loanDetailsModel.getActivityDesc());
        }).collect(Collectors.toList());
    }

    public HomeLoanDto getNewbieLoan() {
        LoanModel homeNewbieLoan = loanMapper.findHomeNewbieLoan();
        long investAmount = investMapper.sumSuccessInvestAmount(homeNewbieLoan.getId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(homeNewbieLoan.getId());
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(homeNewbieLoan.getId());

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

        return new HomeLoanDto(newbieInterestCouponModel,
                homeNewbieLoan,
                investAmount,
                loanRepayModels,
                extraLoanRateMapper.findMaxRateByLoanId(homeNewbieLoan.getId()),
                loanDetailsModel.getExtraSource(), loanDetailsModel.isActivity(), loanDetailsModel.getActivityDesc());
    }

    @Override
    public List<HomeLoanDto> getEnterpriseLoans() {
        List<LoanModel> loanModels = loanMapper.findHomeEnterpriseLoan();
        return Lists.transform(loanModels, new Function<LoanModel, HomeLoanDto>() {
            @Override
            public HomeLoanDto apply(LoanModel loanModel) {
                long investAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());

                List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
                LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
                List<Source> extraSource = Lists.newArrayList();
                boolean activity = false;
                String activityDesc = "";
                if (loanDetailsModel != null) {
                    extraSource = loanDetailsModel.getExtraSource();
                    activity = loanDetailsModel.isActivity();
                    activityDesc = loanDetailsModel.getActivityDesc();
                }
                return new HomeLoanDto(null, loanModel, investAmount, loanRepayModels, extraLoanRateMapper.findMaxRateByLoanId(loanModel.getId()), extraSource, activity, activityDesc);
            }
        });
    }

    @Override
    public List<SiteMapDataDto> siteMapData() {
        List<SiteMapDataDto> cmsSiteMapDataDtoList = Lists.newArrayList();
        if (siteMapRedisWrapperClient.exists(MessageFormat.format(CMS_CATEGORY, LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)))) {
            //从redis中取值
            Map<String, String> cmsSiteMap = siteMapRedisWrapperClient.hgetAll(MessageFormat.format(CMS_CATEGORY, LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)));
            for (String key : cmsSiteMap.keySet()) {
                try {
                    SiteMapDataDto cmsSiteMapDataDto = new SiteMapDataDto();
                    cmsSiteMapDataDto.setName(key);
                    cmsSiteMapDataDto.setLinkUrl(cmsSiteMap.get(key));
                    cmsSiteMapDataDtoList.add(cmsSiteMapDataDto);
                } catch (Exception e) {
                    logger.error("read sitemap from cmsredis error " + e);
                }
            }
        }
        return cmsSiteMapDataDtoList;
    }
}

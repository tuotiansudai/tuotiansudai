package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.HomeService;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private OkHttpClient httpClient;

    @Value("${ask.server}")
    private String askServer;

    private static final String SITEMAP = "/question/getSiteMap";

    public List<HomeLoanDto> getNormalLoans() {
        return getLoans().stream().filter(loan -> !loan.getProductType().equals(ProductType._30) && !loan.getActivityType().equals(ActivityType.NEWBIE)).collect(Collectors.toList());
    }

    public List<HomeLoanDto> getNewbieLoans() {
        return getLoans().stream().filter(loan -> loan.getProductType().equals(ProductType._30) || loan.getActivityType().equals(ActivityType.NEWBIE)).collect(Collectors.toList());
    }

    private List<HomeLoanDto> getLoans() {
        final List<CouponModel> allActiveCoupons = couponMapper.findAllActiveCoupons();

        List<LoanModel> loanModels = loanMapper.findHomeLoan();

        loanModels.forEach(loanModel -> logger.info(MessageFormat.format("[home loan] loanId:{0}", loanModel.getId())));

        return Lists.transform(loanModels, new Function<LoanModel, HomeLoanDto>() {
            @Override
            public HomeLoanDto apply(LoanModel loan) {
                long investAmount = investMapper.sumSuccessInvestAmount(loan.getId());

                //TODO:fake
                if (loan.getId() == 41650602422768L && loan.getStatus() == LoanStatus.REPAYING) {
                    investAmount = loan.getLoanAmount();
                }

                CouponModel newbieInterestCouponModel = null;
                for (CouponModel activeCoupon : allActiveCoupons) {
                    if (activeCoupon.getCouponType() == CouponType.INTEREST_COUPON
                            && activeCoupon.getUserGroup() == UserGroup.NEW_REGISTERED_USER
                            && activeCoupon.getProductTypes().contains(ProductType._30)
                            && (newbieInterestCouponModel == null || activeCoupon.getRate() > newbieInterestCouponModel.getRate())) {
                        newbieInterestCouponModel = activeCoupon;
                    }
                }

                List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loan.getId());
                LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loan.getId());
                List<Source> extraSource = Lists.newArrayList();
                boolean activity = false;
                String activityDesc = "";
                if (loanDetailsModel != null) {
                    extraSource = loanDetailsModel.getExtraSource();
                    activity = loanDetailsModel.isActivity();
                    activityDesc = loanDetailsModel.getActivityDesc();
                }
                return new HomeLoanDto(newbieInterestCouponModel, loan, investAmount, loanRepayModels, extraLoanRateMapper.findMaxRateByLoanId(loan.getId()), extraSource, activity, activityDesc);
            }
        });
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
    public List<SiteMapDataDto> getSiteMapData(){
        String askJsonString = loadJSON(askServer + SITEMAP);
        return JsonToList(askJsonString);
    }

    public List<SiteMapDataDto> JsonToList(String json) {
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        if(json == null || "".equals(json)){
            return siteMapDataDtoList;
        }
        JSONArray jsonarray = JSONArray.fromObject(json);
        List list = (List)JSONArray.toCollection(jsonarray, SiteMapDataDto.class);
        Iterator it = list.iterator();
        while(it.hasNext()){
            siteMapDataDtoList.add((SiteMapDataDto)it.next());
        }

        Collections.sort(siteMapDataDtoList, (o1, o2) -> Integer.compare(o1.getSeq(),o2.getSeq()));

        return siteMapDataDtoList;
    }

    public String loadJSON (String url) {
        try {
            Request request = new Request.Builder().url(url).get().build();
            logger.info("send ask request ");
            httpClient.setConnectTimeout(3, TimeUnit.SECONDS);
            httpClient.setRetryOnConnectionFailure(false);
            Response response = httpClient.newCall(request).execute();
            String responseBodyString = response.body().string();
            logger.info("ask response, body: " + responseBodyString);
            return responseBodyString;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return "";
        }
    }

}

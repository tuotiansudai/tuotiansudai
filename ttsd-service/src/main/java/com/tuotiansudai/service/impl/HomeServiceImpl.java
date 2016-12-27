package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.util.JsonConverter;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.*;
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

    @Value("${ask.server}")
    private String askServer;

    @Value("${cms.server}")
    private String cmsServer;

    private static final String ASK_INTERFACE_URL = "/question/getSiteMap";


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
        String askJsonString = loadJSON(askServer + ASK_INTERFACE_URL);
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
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            logger.error("access ask url not exist, please check ask url + " + e);
        } catch (IOException e) {
            logger.error("access ask url not exist, please check ask url + " + e);
        }
        return json.toString();
    }

}

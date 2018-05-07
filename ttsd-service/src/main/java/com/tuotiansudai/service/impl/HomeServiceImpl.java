package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SiteMapRedisWrapperClient;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.util.DateConvertUtil;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
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

    private static final String ASK_SITE_MAP_KEY = "ask:sitemap";

    private static final String CMS_SITE_MAP_NEW_KEY = "cms:sitemap:new";
    private static final String CMS_SITE_MAP_BAIKE_KEY = "cms:sitemap:baike";
    private static final String CMS_SITE_MAP_COLUMN_KEY = "cms:sitemap:column";

    private static final String SITE_MAP_LAST_MODIFY_DATE_KEY = "sitemap:lastModifyDate";

    private static final int SITE_MAP_STANDARD_NUM = 20000;

    private enum SiteMapType {
        ASK(ASK_SITE_MAP_KEY),
        NEW(CMS_SITE_MAP_NEW_KEY),
        BAIKE(CMS_SITE_MAP_BAIKE_KEY),
        COLUMN(SITE_MAP_LAST_MODIFY_DATE_KEY);

        private String redisKey;

        public String getRedisKey() {
            return redisKey;
        }

        SiteMapType(String redisKey) {
            this.redisKey = redisKey;
        }

        public static SiteMapType getSiteMapType(String subSiteMapType) {
            if (subSiteMapType.indexOf("ask") > -1) {
                return ASK;
            } else if (subSiteMapType.indexOf("new") > -1) {
                return NEW;
            } else if (subSiteMapType.indexOf("baike") > -1) {
                return BAIKE;
            } else if (subSiteMapType.indexOf("column") > -1) {
                return COLUMN;
            }
            return ASK;

        }

    }

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
        if (homeNewbieLoan == null) {
            return null;
        }
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

    @Override
    public Map<String, String> siteMapIndex() {
        Map<String, String> siteMap = Maps.newHashMap();
        if (siteMapRedisWrapperClient.exists(CMS_SITE_MAP_COLUMN_KEY)) {
            String column = siteMapRedisWrapperClient.get(CMS_SITE_MAP_COLUMN_KEY);
            generateSiteMapByType(SiteMapType.COLUMN, column, siteMap);
        }
        if (siteMapRedisWrapperClient.exists(CMS_SITE_MAP_BAIKE_KEY)) {
            String baike = siteMapRedisWrapperClient.get(CMS_SITE_MAP_BAIKE_KEY);
            generateSiteMapByType(SiteMapType.BAIKE, baike, siteMap);
        }
        if (siteMapRedisWrapperClient.exists(CMS_SITE_MAP_NEW_KEY)) {
            String siteMapNew = siteMapRedisWrapperClient.get(CMS_SITE_MAP_BAIKE_KEY);
            generateSiteMapByType(SiteMapType.NEW, siteMapNew, siteMap);
        }
        if (siteMapRedisWrapperClient.exists(ASK_SITE_MAP_KEY)) {
            String ask = siteMapRedisWrapperClient.get(ASK_SITE_MAP_KEY);
            generateSiteMapByType(SiteMapType.ASK, ask, siteMap);
        }


        return siteMap;
    }

    @Override
    public List<String> subSiteMap(String subSiteMapType) {
        String[] paramArray = subSiteMapType.split("-");
        if (paramArray.length < 2 || !StringUtils.isNumeric(paramArray[paramArray.length - 1])) {
            logger.info(String.format("[site map:] subSiteMapType:%s param is error", subSiteMapType));
            return null;
        }
        String siteMapKey = SiteMapType.getSiteMapType(subSiteMapType).getRedisKey();
        if (siteMapRedisWrapperClient.exists(siteMapKey)) {
            String[] siteMapArray = siteMapRedisWrapperClient.get(siteMapKey).split("\n");
            int index = PaginationUtil.validateIndex(Integer.parseInt(paramArray[paramArray.length - 1]), SITE_MAP_STANDARD_NUM, siteMapArray.length);
            int fromIndex = (index - 1) * SITE_MAP_STANDARD_NUM;
            int toIndex = SITE_MAP_STANDARD_NUM * index > siteMapArray.length ? siteMapArray.length : SITE_MAP_STANDARD_NUM * index;

            return Arrays.stream(siteMapArray).collect(Collectors.toList()).subList(fromIndex, toIndex);


        }
        return null;
    }

    @Override
    public String getLastModifyDate(String lastModifyDateKey) {
        return getLastModifyDateByKey(lastModifyDateKey);

    }

    private String getLastModifyDateByKey(String lastModifyDateKey) {

        if (!siteMapRedisWrapperClient.hexists(SITE_MAP_LAST_MODIFY_DATE_KEY, lastModifyDateKey)) {
            siteMapRedisWrapperClient.hset(SITE_MAP_LAST_MODIFY_DATE_KEY, lastModifyDateKey, DateConvertUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }

        return siteMapRedisWrapperClient.hget(SITE_MAP_LAST_MODIFY_DATE_KEY, lastModifyDateKey);

    }

    private void generateSiteMapByType(SiteMapType type, String siteMapValue, Map<String, String> siteMap) {
        if (StringUtils.isEmpty(siteMapValue)) {
            logger.info(String.format("[site map:] type:%s value is null", type.name()));
            return;
        }
        String[] siteMapArray = siteMapValue.split("\n");
        int indexSize = siteMapArray.length % SITE_MAP_STANDARD_NUM == 0 ? siteMapArray.length / SITE_MAP_STANDARD_NUM : (siteMapArray.length / SITE_MAP_STANDARD_NUM) + 1;
        for (int i = 1; i < indexSize + 1; i++) {
            String lastModifyDateKey = String.format(type.name().equalsIgnoreCase(SiteMapType.ASK.name()) ? String.format("ask-%s", String.valueOf(i)) : String.format("cms-%s-%s", type.name().toLowerCase(), String.valueOf(i)));

            siteMap.put(lastModifyDateKey, getLastModifyDateByKey(lastModifyDateKey));
        }

    }

}

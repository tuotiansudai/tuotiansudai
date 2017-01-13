package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.SiteMapRedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.dto.SiteMapDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.HomeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${web.server}")
    private String webServer;

    @Autowired
    private SiteMapRedisWrapperClient redisWrapperClient;

    private static final String CMS_CATEGORY = "cms:sitemap:category";

    private static final String CMS_POSTS = "cms:sitemap:posts";

    private static final String CMS_DOMAIN = "http://content.tuotiansudai.com";

    private static final String WEB_DOMAIN = "/sitemap";

    private static final String CMS_COUNT_SEPARATOR = "\1";

    private static final String CMS_CONTENT_SEPARATOR = "\0";

    private static final int CMS_CATEGORY_FIRST_LEVEL = 1;

    private static final int CMS_CATEGORY_SECOND_LEVEL = 2;

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
    public List<SiteMapDataDto> siteMapData() {
        if (!redisWrapperClient.exists(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))) {
            this.setTestRedis();
        }
        if (!redisWrapperClient.exists(CMS_POSTS + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))) {
            this.setTestarticleRedis();
        }
        return this.cmsSiteMapData();
    }

    @Override
    public List<SiteMapDataDto> getCmsSiteMapCategory() {
        List<SiteMapDataDto> cmsSiteMapFirstCategoryList = Lists.newArrayList();
        if (redisWrapperClient.exists(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))) {
            //从redis中取值,找出所有的一级栏目
            Map<String, String> cmsSiteMapAllCategory = redisWrapperClient.hgetAll(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            for (String key : cmsSiteMapAllCategory.keySet()) {
                try {
                    if (this.findSubStringCount(cmsSiteMapAllCategory.get(key), "/") == 3) {
                        SiteMapDataDto cmsSiteMapDataDto = new SiteMapDataDto();
                        cmsSiteMapDataDto.setName(key);
                        cmsSiteMapDataDto.setLinkUrl(cmsSiteMapAllCategory.get(key));
                        cmsSiteMapDataDto.setSeq(CMS_CATEGORY_FIRST_LEVEL);
                        cmsSiteMapFirstCategoryList.add(cmsSiteMapDataDto);
                    }
                } catch (Exception e) {
                    logger.error("read sitemap from cms redis error " + e);
                }
            }
            return getCmsSiteMapCategoryByFirstCategory(cmsSiteMapFirstCategoryList, cmsSiteMapAllCategory);
        }
        return null;
    }

    private List<SiteMapDataDto> getCmsSiteMapCategoryByFirstCategory(List<SiteMapDataDto> cmsSiteMapFirstCategoryList, Map<String, String> cmsSiteMapAllCategory) {
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        for (SiteMapDataDto siteMapDataDto : cmsSiteMapFirstCategoryList) {
            siteMapDataDtoList.add(siteMapDataDto);
            cmsSiteMapAllCategory.keySet()
                    .stream()
                    .filter(key -> cmsSiteMapAllCategory.get(key).contains(siteMapDataDto.getLinkUrl()) && !cmsSiteMapAllCategory.get(key).equals(siteMapDataDto.getLinkUrl()))
                    .forEach(key -> {
                        SiteMapDataDto cmsSiteMapDataDto = new SiteMapDataDto();
                        cmsSiteMapDataDto.setName(key);
                        cmsSiteMapDataDto.setLinkUrl(cmsSiteMapAllCategory.get(key));
                        cmsSiteMapDataDto.setSeq(CMS_CATEGORY_SECOND_LEVEL);
                        siteMapDataDtoList.add(cmsSiteMapDataDto);
                    });
        }
        return siteMapDataDtoList;
    }

    @Override
    public List<SiteMapDataDto> getCmsSiteMapCategoryArticle(String parent, String category) {
        List<SiteMapDataDto> cmsSiteMapArticleList = Lists.newArrayList();
        if (redisWrapperClient.hexists(CMS_POSTS + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), category)) {
            //从redis中取值
            long startTime = System.currentTimeMillis();
            String cmsSiteMapAllArticleByCategory = redisWrapperClient.hget(CMS_POSTS + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), category);
            String[] cmsSiteMapArrayArticleByCategory = cmsSiteMapAllArticleByCategory.split(CMS_COUNT_SEPARATOR);
            for (int i = 0; i < cmsSiteMapArrayArticleByCategory.length; i++) {
                SiteMapDataDto cmsSiteMapDataDto = new SiteMapDataDto();
                cmsSiteMapDataDto.setName(cmsSiteMapArrayArticleByCategory[i].substring(0, cmsSiteMapArrayArticleByCategory[i].indexOf(CMS_CONTENT_SEPARATOR)));
                cmsSiteMapDataDto.setLinkUrl(cmsSiteMapArrayArticleByCategory[i].substring(cmsSiteMapArrayArticleByCategory[i].indexOf(CMS_CONTENT_SEPARATOR) + 1, cmsSiteMapArrayArticleByCategory[i].length()));
                cmsSiteMapDataDto.setSeq(CMS_CATEGORY_SECOND_LEVEL);
                cmsSiteMapArticleList.add(cmsSiteMapDataDto);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("read 200000 recods from redis total time : " + (endTime - startTime) + " millis");
        }
        return cmsSiteMapArticleList;
    }

    public List<SiteMapDataDto> cmsSiteMapData() {
        List<SiteMapDataDto> cmsSiteMapDataDtoList = Lists.newArrayList();
        if (redisWrapperClient.exists(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))) {
            //从redis中取值
            Map<String, String> cmsSiteMap = redisWrapperClient.hgetAll(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            for (String key : cmsSiteMap.keySet()) {
                try {
                    SiteMapDataDto cmsSiteMapDataDto = new SiteMapDataDto();
                    cmsSiteMapDataDto.setName(key);
                    cmsSiteMapDataDto.setLinkUrl(cmsSiteMap.get(key).replace(CMS_DOMAIN, webServer + WEB_DOMAIN));
                    cmsSiteMapDataDto.setSeq(CMS_CATEGORY_SECOND_LEVEL);
                    cmsSiteMapDataDtoList.add(cmsSiteMapDataDto);
                } catch (Exception e) {
                    logger.error("read sitemap from cmsredis error " + e);
                }
            }
        }
        return cmsSiteMapDataDtoList;
    }

    private static int findSubStringCount(String text, String sub) {
        int count = 0, start = 0;
        while ((start = text.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count;
    }

    public static void main(String args[]) {
        String a = "暂11现“上翘行情” 银行理财转向“不兜底”\0http://content.tuotiansudai.com/licai/baobenlicai/75492.html\1暂22现“上翘行情” 银行理财转向“不兜底”\0http://content.tuotiansudai.com/licai/baobenlicai/75492.html";

        String[] b = a.split("\1");
        System.out.println(b.length);
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i].substring(0, b[i].indexOf("\0")));
            System.out.println(b[i].substring(b[i].indexOf("\0") + 1, b[i].length()));

        }

    }


    public void setTestRedis() {
        redisWrapperClient.hset(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "理财", "http://content.tuotiansudai.com/licai");
        redisWrapperClient.hset(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "大学生理财", "http://content.tuotiansudai.com/licai/daxueshenglicai");
        redisWrapperClient.hset(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "短期理财", "http://content.tuotiansudai.com/licai/duanqilicai");
        redisWrapperClient.hset(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "银行", "http://content.tuotiansudai.com/bank");
        redisWrapperClient.hset(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "工商银行", "http://content.tuotiansudai.com/bank/icbc");
        redisWrapperClient.hset(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "建设银行", "http://content.tuotiansudai.com/bank/ccb");
        for (int i = 0; i < 1000; i++) {
            redisWrapperClient.hset(CMS_CATEGORY + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "工商银行" + i, "http://content.tuotiansudai.com/bank/icbc");
        }
    }

    public void setTestarticleRedis() {
        redisWrapperClient.hset(CMS_POSTS + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "baobenlicai", "暂11现“上翘行情” 银行理财转向“不兜底”\0http://content.tuotiansudai.com/licai/baobenlicai/75492.html\1暂22现“上翘行情” 银行理财转向“不兜底”\0http://content.tuotiansudai.com/licai/baobenlicai/75492.html");
        redisWrapperClient.hset(CMS_POSTS + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "daxueshenglicai", "暂11现“上翘行情” 银行理财转向“不兜底”\0http://content.tuotiansudai.com/licai/baobenlicai/75492.html");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 200000; i++) {
            stringBuilder.append("暂现上翘行情银行理财转向不兜底" + i + "\0http://content.tuotiansudai.com/licai/baobenlicai/75492.html");
            if (i != 200000) {
                stringBuilder.append("\1");
            }
        }
        redisWrapperClient.hset(CMS_POSTS + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE), "icbc", stringBuilder.toString());
    }

}

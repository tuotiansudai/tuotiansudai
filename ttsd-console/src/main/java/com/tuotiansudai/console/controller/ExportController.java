package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ExportService;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.point.repository.mapper.UserPointPrizeMapper;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.LoanRepayService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.service.SystemBillService;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController {

    private static Logger logger = Logger.getLogger(ExportController.class);

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserPointPrizeMapper userPointPrizeMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ExportService exportService;
    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private LoanRepayService loanRepayService;

    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public void exportCoupons(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.CouponHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");

        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findNewbieAndInvestCoupons(index, pageSize);
        List<List<String>> coupons = buildCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.CouponHeader, coupons, response.getOutputStream());
    }

    @RequestMapping(value = "/interest-coupons", method = RequestMethod.GET)
    public void exportInterestCoupons(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.InterestCouponsHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findInterestCoupons(index, pageSize);
        List<List<String>> interestCoupons = buildInterestCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.InterestCouponsHeader, interestCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/red-envelopes", method = RequestMethod.GET)
    public void exportRedEnvelopes(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.RedEnvelopesHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findRedEnvelopeCoupons(index, pageSize);
        List<List<String>> redEnvelopeCoupons = buildRedEnvelopeCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.RedEnvelopesHeader, redEnvelopeCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/birthday-coupons", method = RequestMethod.GET)
    public void exportBirthdayCoupons(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.BirthdayCouponsHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findBirthdayCoupons(index, pageSize);
        List<List<String>> birthdayCoupons = buildBirthdayCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.BirthdayCouponsHeader, birthdayCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/point-prize", method = RequestMethod.GET)
    public void exportPointPrize(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.PointPrizeHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");
        List<PointPrizeWinnerViewDto> records = userPointPrizeMapper.findAllPointPrizeGroupPrize();
        List<List<String>> pointPrize = buildPointPrize(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.PointPrizeHeader, pointPrize, response.getOutputStream());
    }

    @RequestMapping(value = "/user-point",method = RequestMethod.GET)
    public void exportUserPoint(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                @RequestParam(value = "loginName", required = false) String loginName,
                                @RequestParam(value = "userName", required = false) String userName,
                                @RequestParam(value = "mobile", required = false) String mobile,HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.UserPointHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        httpServletResponse.setContentType("application/csv");
        List<AccountItemDataDto> accountItemDataDtoList = accountService.findUsersAccountPoint(loginName, userName, mobile, 1, Integer.MAX_VALUE);

        List<List<String>> csvData = exportService.buildOriginListToCsvData(accountItemDataDtoList);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.UserPointHeader, csvData, httpServletResponse.getOutputStream());

    }
    @RequestMapping(value = "/coupon-exchange",method = RequestMethod.GET)
    public void exportCouponExchange(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.CouponExchangeHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        httpServletResponse.setContentType("application/csv");
        List<ExchangeCouponDto> exchangeCouponDtos = couponService.findCouponExchanges(1, Integer.MAX_VALUE);

        List<List<String>> csvData = exportService.buildOriginListToCsvData(exchangeCouponDtos);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.CouponExchangeHeader, csvData, httpServletResponse.getOutputStream());

    }
    @RequestMapping(value = "/system-bill", method = RequestMethod.GET)
    public void exportSystemBillList(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                          @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                          @RequestParam(value = "operationType", required = false) SystemBillOperationType operationType,
                                          @RequestParam(value = "businessType", required = false) SystemBillBusinessType businessType,HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.SystemBillHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        httpServletResponse.setContentType("application/csv");

        BaseDto<BasePaginationDataDto> baseDto = systemBillService.findSystemBillPagination(
                startTime,
                endTime,
                operationType,
                businessType,
                1,
                Integer.MAX_VALUE);

        List<List<String>> csvData = exportService.buildOriginListToCsvData(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.SystemBillHeader, csvData, httpServletResponse.getOutputStream());

    }

    @RequestMapping(value = "/transfer-list", method = RequestMethod.GET)
    public void exportTransferList(@RequestParam(name = "transferApplicationId", required = false) Long transferApplicationId,
                                                              @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                              @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                              @RequestParam(name = "status", required = false) TransferStatus status,
                                                              @RequestParam(name = "transferrerMobile", required = false) String transferrerMobile,
                                                              @RequestParam(name = "transfereeMobile", required = false) String transfereeMobile,
                                                              @RequestParam(name = "loanId", required = false) Long loanId,HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.TransferListHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        httpServletResponse.setContentType("application/csv");
        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = investTransferService.findTransferApplicationPaginationList(transferApplicationId, startTime, endTime, status, transferrerMobile, transfereeMobile, loanId, 1, Integer.MAX_VALUE);
        List<List<String>> csvData = exportService.buildOriginListToCsvData(basePaginationDataDto.getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.TransferListHeader, csvData, httpServletResponse.getOutputStream());
    }

    @RequestMapping(value = "/loan-repay",method = RequestMethod.GET)
    public void exportLoanRepay(@RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                                @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                @RequestParam(value = "loanId",required = false) Long loanId,
                                                @RequestParam(value = "loginName",required = false) String loginName,
                                                @RequestParam(value = "repayStatus",required = false) RepayStatus repayStatus,
                                                @RequestParam(value = "startTime",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                @RequestParam(value = "endTime",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.LoanRepayHeader.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        httpServletResponse.setContentType("application/csv");
        BaseDto<BasePaginationDataDto> baseDto = loanRepayService.findLoanRepayPagination(1, Integer.MAX_VALUE,
                loanId, loginName, startTime, endTime, repayStatus);

        List<List<String>> csvData = exportService.buildOriginListToCsvData(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.LoanRepayHeader, csvData, httpServletResponse.getOutputStream());

    }



    private List<List<String>> buildCoupons(List<CouponDto> records) {
        String activityTimeTemplate = "{0}至{1}";
        String useCondition = "投资满{0}元";
        List<List<String>> rows = Lists.newArrayList();
        for (CouponDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getCouponType().getName());
            row.add(record.getAmount());
            String investQuota = new BigDecimal(record.getTotalInvestAmount()).divide(new BigDecimal(100)).toString();
            row.add(investQuota);
            String startTime = new DateTime(record.getStartTime()).toString("yyyy-MM-dd");
            String endTime = new DateTime(record.getEndTime()).toString("yyyy-MM-dd");
            String activityTime = MessageFormat.format(activityTimeTemplate, startTime, endTime);
            row.add(activityTime);
            row.add(String.valueOf(record.getDeadline()));
            row.add(String.valueOf(record.getTotalCount()));
            row.add(String.valueOf(record.getIssuedCount()));
            row.add(String.valueOf(record.getUsedCount()));
            row.add(record.getUserGroup().getDescription());
            List<ProductType> types = record.getProductTypes();
            String productType = "";
            for (int i = 0; i < types.size(); i++) {
                if (i == 0) {
                    productType = productType + types.get(i).getName();
                } else {
                    productType = productType + "/" + types.get(i).getName();
                }
            }
            row.add(productType);
            row.add(MessageFormat.format(useCondition, record.getInvestLowerLimit()));
            row.add(new BigDecimal(record.getExpectedAmount()).divide(new BigDecimal(100)).toString());
            row.add(new BigDecimal(record.getActualAmount()).divide(new BigDecimal(100)).toString());
            rows.add(row);
        }
        return rows;
    }

    private List<List<String>> buildInterestCoupons(List<CouponDto> records) {
        String activityTimeTemplate = "{0}至{1}";
        String useCondition = "投资满{0}元";
        List<List<String>> rows = Lists.newArrayList();
        for (CouponDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getCouponType().getName());
            row.add(new BigDecimal(String.valueOf(record.getRate())).multiply(new BigDecimal(100)).toString());
            row.add(new BigDecimal(record.getTotalInvestAmount()).divide(new BigDecimal(100)).toString());
            String startTime = new DateTime(record.getStartTime()).toString("yyyy-MM-dd");
            String endTime = new DateTime(record.getEndTime()).toString("yyyy-MM-dd");
            String activityTime = MessageFormat.format(activityTimeTemplate, startTime, endTime);
            row.add(activityTime);
            row.add(String.valueOf(record.getDeadline()));
            row.add(record.getUserGroup().getDescription());
            row.add(String.valueOf(record.getTotalCount()));
            row.add(record.isSmsAlert() ? "是" : "否");
            row.add(String.valueOf(record.getIssuedCount()));
            row.add(String.valueOf(record.getUsedCount()));
            List<ProductType> types = record.getProductTypes();
            String productType = "";
            for (int i = 0; i < types.size(); i++) {
                if (i == 0) {
                    productType = productType + types.get(i).getName();
                } else {
                    productType = productType + "/" + types.get(i).getName();
                }
            }
            row.add(productType);
            row.add(MessageFormat.format(useCondition, record.getInvestLowerLimit()));
            row.add(new BigDecimal(record.getExpectedAmount()).divide(new BigDecimal(100)).toString());
            row.add(new BigDecimal(record.getActualAmount()).divide(new BigDecimal(100)).toString());
            rows.add(row);
        }
        return rows;
    }

    private List<List<String>> buildRedEnvelopeCoupons(List<CouponDto> records) {
        String activityTimeTemplate = "{0}至{1}";
        String useCondition = "投资满{0}元";
        List<List<String>> rows = Lists.newArrayList();
        for (CouponDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getCouponType().getName());
            row.add(record.getAmount());
            String investQuota = new BigDecimal(record.getTotalInvestAmount()).divide(new BigDecimal(100)).toString();
            row.add(investQuota);
            String startTime = new DateTime(record.getStartTime()).toString("yyyy-MM-dd");
            String endTime = new DateTime(record.getEndTime()).toString("yyyy-MM-dd");
            String activityTime = MessageFormat.format(activityTimeTemplate, startTime, endTime);
            row.add(activityTime);
            row.add(String.valueOf(record.getDeadline()));
            row.add(String.valueOf(record.getIssuedCount()));
            row.add(String.valueOf(record.getUsedCount()));
            row.add(record.getUserGroup().getDescription());

            List<ProductType> types = record.getProductTypes();
            String productType = "";
            for (int i = 0; i < types.size(); i++) {
                if (i == 0) {
                    productType = productType + types.get(i).getName();
                } else {
                    productType = productType + "/" + types.get(i).getName();
                }
            }
            row.add(productType);
            row.add(MessageFormat.format(useCondition, record.getInvestLowerLimit()));
            row.add(new BigDecimal(record.getExpectedAmount()).divide(new BigDecimal(100)).toString());
            row.add(new BigDecimal(record.getActualAmount()).divide(new BigDecimal(100)).toString());
            row.add(record.isShared() ? "是" : "否");
            rows.add(row);
        }
        return rows;
    }


    private List<List<String>> buildBirthdayCoupons(List<CouponDto> records) {
        String activityTimeTemplate = "{0}至{1}";
        List<List<String>> rows = Lists.newArrayList();
        for (CouponDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getCouponType().getName());
            row.add(String.valueOf(record.getBirthdayBenefit() + 1));
            String investQuota = new BigDecimal(record.getTotalInvestAmount()).divide(new BigDecimal(100)).toString();
            row.add(investQuota);
            String startTime = new DateTime(record.getStartTime()).toString("yyyy-MM-dd");
            String endTime = new DateTime(record.getEndTime()).toString("yyyy-MM-dd");
            String activityTime = MessageFormat.format(activityTimeTemplate, startTime, endTime);
            row.add(activityTime);
            row.add(record.getUserGroup().getDescription());

            List<ProductType> types = record.getProductTypes();
            String productType = "";
            for (int i = 0; i < types.size(); i++) {
                if (i == 0) {
                    productType = productType + types.get(i).getName();
                } else {
                    productType = productType + "/" + types.get(i).getName();
                }
            }
            row.add(productType);
            row.add(String.valueOf(record.getUsedCount()));
            row.add(new BigDecimal(record.getExpectedAmount()).divide(new BigDecimal(100)).toString());
            row.add(new BigDecimal(record.getActualAmount()).divide(new BigDecimal(100)).toString());
            row.add("是");
            rows.add(row);
        }
        return rows;
    }

    private List<List<String>> buildPointPrize(List<PointPrizeWinnerViewDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (PointPrizeWinnerViewDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getDescription());
            row.add(String.valueOf(record.getNum()));
            row.add(record.isActive() ? "已生效" : "未生效");
            rows.add(row);
        }
        return rows;
    }
}

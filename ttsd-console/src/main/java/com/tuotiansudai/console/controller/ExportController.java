package com.tuotiansudai.console.controller;

import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.dto.RemainUserDto;
import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.repository.model.UserMicroModelView;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.console.service.*;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.point.repository.dto.ChannelPointDetailPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.ProductOrderDto;
import com.tuotiansudai.point.repository.mapper.UserPointPrizeMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.point.service.ChannelPointServiceImpl;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.AskRestClient;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/export")
public class ExportController {

    private static Logger logger = Logger.getLogger(ExportController.class);

    @Autowired
    private ConsoleCouponService consoleCouponService;

    @Autowired
    private UserPointPrizeMapper userPointPrizeMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private ExportService exportService;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private ConsoleInvestTransferService consoleInvestTransferService;

    @Autowired
    private ConsoleLoanRepayService consoleLoanRepayService;

    @Autowired
    private ConsoleLoanService consoleLoanService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ConsoleUserService consoleUserService;

    @Autowired
    private ConsoleInvestService consoleInvestService;

    @Autowired
    private ConsoleRechargeService consoleRechargeService;

    @Autowired
    private ConsoleWithdrawService consoleWithdrawService;

    @Autowired
    private ConsoleUserBillService consoleUserBillService;

    @Autowired
    private ConsoleFeedbackService consoleFeedbackService;

    @Autowired
    private InvestAchievementService investAchievementService;

    @Autowired
    private ConsoleReferrerManageService consoleReferrerManageService;

    @Autowired
    private AskRestClient askRestClient;

    @Autowired
    private CreditLoanBillService creditLoanBillService;

    @Autowired
    private ChannelPointServiceImpl channelPointServiceImpl;

    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public void exportCoupons(HttpServletResponse response,
                              @RequestParam(value = "couponType", required = false) String couponType,
                              @RequestParam(value = "couponSource", required = false) String couponSource,
                              @RequestParam(value = "amount", required = false, defaultValue = "0") int amount) throws IOException {
        fillExportResponse(response, CsvHeaderType.CouponHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = consoleCouponService.findCouponsByTypeRedAndMoney(index, pageSize, couponType, amount, couponSource);
        List<List<String>> coupons = exportService.buildCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.CouponHeader, coupons, response.getOutputStream());
    }

    @RequestMapping(value = "/interest-coupons", method = RequestMethod.GET)
    public void exportInterestCoupons(HttpServletResponse response,
                                      @RequestParam(value = "couponType", required = false) String couponType,
                                      @RequestParam(value = "couponSource", required = false) String couponSource,
                                      @RequestParam(value = "amount", required = false, defaultValue = "0") int amount) throws IOException {
        fillExportResponse(response, CsvHeaderType.InterestCouponsHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = consoleCouponService.findCouponsByTypeRedAndMoney(index, pageSize, couponType, amount, couponSource);
        List<List<String>> interestCoupons = exportService.buildInterestCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.InterestCouponsHeader, interestCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/red-envelopes", method = RequestMethod.GET)
    public void exportRedEnvelopes(HttpServletResponse response,
                                   @RequestParam(value = "couponType", required = false) String couponType,
                                   @RequestParam(value = "couponSource", required = false) String couponSource,
                                   @RequestParam(value = "amount", required = false, defaultValue = "0") int amount) throws IOException {
        fillExportResponse(response, CsvHeaderType.RedEnvelopesHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = consoleCouponService.findCouponsByTypeRedAndMoney(index, pageSize, couponType, amount, couponSource);
        List<List<String>> redEnvelopeCoupons = exportService.buildRedEnvelopeCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.RedEnvelopesHeader, redEnvelopeCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/birthday-coupons", method = RequestMethod.GET)
    public void exportBirthdayCoupons(HttpServletResponse response,
                                      @RequestParam(value = "couponType", required = false) String couponType,
                                      @RequestParam(value = "couponSource", required = false) String couponSource,
                                      @RequestParam(value = "amount", required = false, defaultValue = "0") int amount) throws IOException {
        fillExportResponse(response, CsvHeaderType.BirthdayCouponsHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = consoleCouponService.findCouponsByTypeRedAndMoney(index, pageSize, couponType, amount, couponSource);
        List<List<String>> birthdayCoupons = exportService.buildBirthdayCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.BirthdayCouponsHeader, birthdayCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/point-prize", method = RequestMethod.GET)
    public void exportPointPrize(HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.PointPrizeHeader.getDescription());
        List<PointPrizeWinnerViewDto> records = userPointPrizeMapper.findAllPointPrizeGroupPrize();
        List<List<String>> pointPrize = exportService.buildPointPrize(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.PointPrizeHeader, pointPrize, response.getOutputStream());
    }

    @RequestMapping(value = "/coupon-exchange", method = RequestMethod.GET)
    public void exportCouponExchange(HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.CouponExchangeHeader.getDescription());
        List<ExchangeCouponDto> exchangeCouponDtos = productService.findCouponExchanges(1, Integer.MAX_VALUE);
        List<List<String>> csvData = exportService.buildCouponExchangeCsvData(exchangeCouponDtos);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.CouponExchangeHeader, csvData, response.getOutputStream());
    }

    @RequestMapping(value = "/system-bill", method = RequestMethod.GET)
    public void exportSystemBillList(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                     @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                     @RequestParam(value = "operationType", required = false) SystemBillOperationType operationType,
                                     @RequestParam(value = "businessType", required = false) SystemBillBusinessType businessType, HttpServletResponse httpServletResponse) throws IOException {
        fillExportResponse(httpServletResponse, CsvHeaderType.SystemBillHeader.getDescription());
        BaseDto<BasePaginationDataDto<SystemBillPaginationItemDataDto>> baseDto = systemBillService.findSystemBillPagination(
                startTime,
                endTime,
                operationType,
                businessType,
                1,
                Integer.MAX_VALUE);
        List<List<String>> csvData = exportService.buildSystemBillCsvData(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.SystemBillHeader, csvData, httpServletResponse.getOutputStream());

    }

    @RequestMapping(value = "/credit-loan-bill", method = RequestMethod.GET)
    public void exportSystemBillList(@RequestParam(value = "orderId", defaultValue = "") String orderId,
                                     @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                     @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                     @RequestParam(value = "operationType", required = false) CreditLoanBillOperationType operationType,
                                     @RequestParam(value = "businessType", required = false) CreditLoanBillBusinessType businessType, HttpServletResponse httpServletResponse) throws IOException {
        fillExportResponse(httpServletResponse, CsvHeaderType.CreditLoanBillHeader.getDescription());
        BaseDto<BasePaginationDataDto<CreditLoanBillPaginationItemDataDto>> baseDto = creditLoanBillService.findCreditLoanBillPagination(
                startTime,
                endTime,
                operationType,
                businessType,
                1,
                Integer.MAX_VALUE,
                orderId);
        List<List<String>> csvData = exportService.buildCreditLoanBillCsvData(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.CreditLoanBillHeader, csvData, httpServletResponse.getOutputStream());

    }

    @RequestMapping(value = "/transfer-list", method = RequestMethod.GET)
    public void exportTransferList(@RequestParam(name = "transferApplicationId", required = false) Long transferApplicationId,
                                   @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                   @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                   @RequestParam(name = "status", required = false) TransferStatus status,
                                   @RequestParam(name = "transferrerMobile", required = false) String transferrerMobile,
                                   @RequestParam(name = "transfereeMobile", required = false) String transfereeMobile,
                                   @RequestParam(name = "loanId", required = false) Long loanId,
                                   @RequestParam(name = "source", required = false) Source source, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.TransferListHeader.getDescription());
        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = consoleInvestTransferService.findTransferApplicationPaginationList(transferApplicationId, startTime, endTime, status, transferrerMobile, transfereeMobile, loanId, source, 1, Integer.MAX_VALUE);
        List<List<String>> csvData = exportService.buildTransferListCsvData(basePaginationDataDto.getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.TransferListHeader, csvData, response.getOutputStream());
    }

    @RequestMapping(value = "/loan-repay", method = RequestMethod.GET)
    public void exportLoanRepay(@RequestParam(value = "loanId", required = false) Long loanId,
                                @RequestParam(value = "loginName", required = false) String loginName,
                                @RequestParam(value = "repayStatus", required = false) RepayStatus repayStatus,
                                @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, HttpServletResponse httpServletResponse) throws IOException {
        fillExportResponse(httpServletResponse, CsvHeaderType.LoanRepayHeader.getDescription());
        BaseDto<BasePaginationDataDto<LoanRepayDataItemDto>> baseDto = consoleLoanRepayService.findLoanRepayPagination(1, Integer.MAX_VALUE,
                loanId, loginName, startTime, endTime, repayStatus);
        List<List<String>> csvData = exportService.buildLoanRepayCsvData(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.LoanRepayHeader, csvData, httpServletResponse.getOutputStream());

    }

    @RequestMapping(value = "/loan-list", method = RequestMethod.GET)
    public void consoleLoanList(@RequestParam(value = "status", required = false) LoanStatus status,
                                @RequestParam(value = "loanId", required = false) Long loanId,
                                @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                @RequestParam(value = "loanName", required = false) String loanName, HttpServletResponse httpServletResponse) throws IOException {

        fillExportResponse(httpServletResponse, CsvHeaderType.ConsoleLoanList.getDescription());
        List<LoanListDto> loanListDtos = consoleLoanService.findLoanList(status, loanId, loanName,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                index, Integer.MAX_VALUE);
        List<List<String>> csvData = exportService.buildConsoleLoanList(loanListDtos);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleLoanList, csvData, httpServletResponse.getOutputStream());
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public void exportUsers(String loginName, String email, String mobile,
                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                            RoleStage roleStage, String referrerMobile, String channel,
                            UserOperation userOperation,
                            @RequestParam(value = "source", required = false) Source source, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleUsers.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BaseDto<BasePaginationDataDto<UserItemDataDto>> baseDto = consoleUserService.findAllUser(loginName, email, mobile,
                beginTime, endTime, source, roleStage, referrerMobile, channel, userOperation, index, pageSize);
        List<List<String>> usersData = exportService.buildUsers(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleUsers, usersData, response.getOutputStream());
    }

    @RequestMapping(value = "/invests", method = RequestMethod.GET)
    public void exportInvests(@RequestParam(name = "loanId", required = false) Long loanId,
                              @RequestParam(name = "mobile", required = false) String investorMobile,
                              @RequestParam(name = "channel", required = false) String channel,
                              @RequestParam(name = "source", required = false) Source source,
                              @RequestParam(name = "role", required = false) Role role,
                              @RequestParam(name = "investStatus", required = false) InvestStatus investStatus,
                              @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                              @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                              @RequestParam(name = "usedPreferenceType", required = false) PreferenceType preferenceType, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleInvests.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        InvestPaginationDataDto investPagination = consoleInvestService.getInvestPagination(loanId, investorMobile, channel, source,
                role, startTime, endTime, investStatus, preferenceType, null, index, pageSize);
        List<InvestPaginationItemDataDto> records = investPagination.getRecords();
        List<List<String>> investsData = exportService.buildInvests(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleInvests, investsData, response.getOutputStream());
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public void exportRecharge(@RequestParam(value = "rechargeId", required = false) String rechargeId,
                               @RequestParam(value = "mobile", required = false) String mobile,
                               @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                               @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                               @RequestParam(value = "status", required = false) RechargeStatus status,
                               @RequestParam(value = "source", required = false) RechargeSource source,
                               @RequestParam(value = "channel", required = false) String channel,
                               @RequestParam(value = "role", required = false) String role,
                               HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleRecharge.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> baseDto = consoleRechargeService.findRechargePagination(rechargeId, mobile, source, status, channel, index, pageSize, startTime, endTime, role);
        List<List<String>> rechargeData = exportService.buildRecharge(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleRecharge, rechargeData, response.getOutputStream());
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public void exportWithdraw(@RequestParam(value = "withdrawId", required = false) Long withdrawId,
                               @RequestParam(value = "mobile", required = false) String mobile,
                               @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                               @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                               @RequestParam(value = "status", required = false) WithdrawStatus status,
                               @RequestParam(value = "source", required = false) Source source,
                               HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleWithdraw.getDescription());
        BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> baseDto = consoleWithdrawService.findWithdrawPagination(withdrawId, mobile, status, source, 1, startTime, endTime);
        List<List<String>> withdrawData = exportService.buildWithdraw(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleWithdraw, withdrawData, response.getOutputStream());
    }

    @RequestMapping(value = "/user-funds", method = RequestMethod.GET)
    public void exportUserFunds(@RequestParam(value = "userBillBusinessType", required = false) UserBillBusinessType userBillBusinessType,
                                @RequestParam(value = "userBillOperationType", required = false) UserBillOperationType userBillOperationType,
                                @RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleUserFundsCsvHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<UserBillPaginationView> userBillModels = consoleUserBillService.findUserFunds(userBillBusinessType, userBillOperationType, mobile, startTime, endTime, index, pageSize);
        List<List<String>> userFundsData = exportService.buildUserFunds(userBillModels);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleUserFundsCsvHeader, userFundsData, response.getOutputStream());
    }

    @RequestMapping(value = "/account-balance", method = RequestMethod.GET)
    public void exportAccountBalance(@RequestParam(value = "mobile", required = false) String mobile,
                                     @RequestParam(value = "balanceMin", required = false) String balanceMin,
                                     @RequestParam(value = "balanceMax", required = false) String balanceMax, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.AccountBalance.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<UserItemDataDto> dataDtos = consoleUserService.findUsersAccountBalance(mobile, balanceMin, balanceMax, index, pageSize);
        List<List<String>> accountBalanceData = exportService.buildAccountBalance(dataDtos);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.AccountBalance, accountBalanceData, response.getOutputStream());
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.GET)
    public void exportFeedBack(@RequestParam(value = "mobile", required = false) String mobile,
                               @RequestParam(value = "source", required = false) Source source,
                               @RequestParam(value = "type", required = false) FeedbackType type,
                               @RequestParam(value = "status", required = false) ProcessStatus status,
                               @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                               @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.Feedback.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BasePaginationDataDto<FeedbackModel> feedbackModels = consoleFeedbackService.getFeedbackPagination(mobile, source, type, status, startTime, endTime, index, pageSize);
        List<List<String>> feedbackData = exportService.buildFeedBack(feedbackModels.getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.Feedback, feedbackData, response.getOutputStream());
    }

    @RequestMapping(value = "/product-order-list", method = RequestMethod.GET)
    public void productOrderListExport(@RequestParam(value = "productId") long productId, HttpServletResponse
            httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.ProductOrderList.getDescription() + new DateTime().toString("yyyyMMdd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        httpServletResponse.setContentType("application/csv");
        List<ProductOrderDto> productOrderDtos = productService.findProductOrderList(productId, null, 1, Integer.MAX_VALUE);
        List<List<String>> csvData = exportService.buildProductOrderList(productOrderDtos);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ProductOrderList, csvData, httpServletResponse.getOutputStream());
    }

    @RequestMapping(value = "/invest-achievement", method = RequestMethod.GET)
    public void exportInvestAchievement(@RequestParam(value = "mobile", required = false) String
                                                mobile, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.InvestAchievementHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<LoanAchievementView> loanAchievementViews = investAchievementService.findInvestAchievement(index, pageSize, mobile);
        List<List<String>> investAchievementData = exportService.buildInvestAchievement(loanAchievementViews);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.InvestAchievementHeader, investAchievementData, response.getOutputStream());
    }

    @RequestMapping(value = "/referrer", method = RequestMethod.GET)
    public void exportReferrer(@RequestParam(value = "referrerMobile", required = false) String referrerMobile,
                               @RequestParam(value = "investMobile", required = false) String investMobile,
                               @RequestParam(value = "investStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date
                                       investStartTime,
                               @RequestParam(value = "investEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date
                                       investEndTime,
                               @RequestParam(value = "level", required = false) Integer level,
                               @RequestParam(value = "rewardStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date
                                       rewardStartTime,
                               @RequestParam(value = "rewardEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date
                                       rewardEndTime,
                               @RequestParam(value = "role", required = false) Role role,
                               @RequestParam(value = "source", required = false) Source source,
                               @RequestParam(value = "referrerRewardStatus", required = false) ReferrerRewardStatus referrerRewardStatus,
                               HttpServletResponse response) throws
            IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleReferrerManageCsvHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        DateTime investDateTime = new DateTime(investEndTime);
        DateTime rewardDateTime = new DateTime(rewardEndTime);
        List<ReferrerManageView> referrerManageViews = consoleReferrerManageService.findReferrerManage(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : null, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : null, role, source, referrerRewardStatus, index, pageSize);
        List<List<String>> referrerManageData = exportService.buildReferrer(referrerManageViews);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleReferrerManageCsvHeader, referrerManageData, response.getOutputStream());
    }

    private void fillExportResponse(HttpServletResponse httpServletResponse, String csvHeader) {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(csvHeader
                    + new DateTime().toString("yyyy-MM-dd") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        httpServletResponse.setContentType("application/csv");
    }

    @RequestMapping(value = "/channel-point-detail/{channelPointId:^\\d+$}", method = RequestMethod.GET)
    public void exportChannelPointDetail(@PathVariable long channelPointId,
                                         @RequestParam(value = "channel", required = false, defaultValue = "") String channel,
                                         @RequestParam(value = "userNameOrMobile", required = false) String userNameOrMobile,
                                         @RequestParam(value = "success", required = false) Boolean success,
                                         HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ChannelPointDetailHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BasePaginationDataDto<ChannelPointDetailPaginationItemDataDto> dataDto = channelPointServiceImpl.getChannelPointDetailList(channelPointId, channel, userNameOrMobile, success, index, pageSize);
        List<List<String>> channelPointDetailList = exportService.buildChannelPointDetailList(dataDto.getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ChannelPointDetailHeader, channelPointDetailList, response.getOutputStream());
    }

    @RequestMapping(value = "/point-consume", method = RequestMethod.GET)
    public void exportCouponDetails(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                    @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                    @RequestParam(value = "pointBusinessType", required = false) PointBusinessType businessType,
                                    @RequestParam(value = "channel", required = false) String channel,
                                    @RequestParam(value = "minPoint", required = false) Long minPoint,
                                    @RequestParam(value = "maxPoint", required = false) Long maxPoint,
                                    @RequestParam(value = "userNameOrMobile", required = false) String userNameOrMobile,
                                    HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.PointConsumeHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPaginationConsole(startTime, endTime, businessType, channel, minPoint, maxPoint, userNameOrMobile, index, pageSize);
        List<List<String>> pointBillList = exportService.buildPointConsumeList(dataDto.getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.PointConsumeHeader, pointBillList, response.getOutputStream());
    }

    @RequestMapping(value = "/coupons-details", method = RequestMethod.GET)
    public void exportCouponDetails(@RequestParam(value = "couponId", required = false) long couponId, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                    @RequestParam(value = "usedStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedStartTime,
                                    @RequestParam(value = "usedEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedEndTime,
                                    @RequestParam(value = "loginName", required = false) String loginName,
                                    @RequestParam(value = "mobile", required = false) String mobile,
                                    HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.BirthdayCouponsHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDetailsDto> userCoupons = consoleCouponService.findCouponDetail(couponId, isUsed, loginName, mobile, null, null, null, usedStartTime, usedEndTime, index, pageSize);
        List<List<String>> userCouponData = exportService.buildCouponDetailsDtoList(userCoupons);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.CouponDetailsHeader, userCouponData, response.getOutputStream());
    }


    @RequestMapping(value = "/user-micro-model", method = RequestMethod.GET)
    public void exportUserMicroModel(
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userRole", required = false) Role role,
            @RequestParam(value = "registerTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date registerTimeStart,
            @RequestParam(value = "registerTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date registerTimeEnd,
            @RequestParam(value = "hasCertify", required = false) String hasCertify,
            @RequestParam(value = "invested", required = false) String invested,
            @RequestParam(value = "totalInvestAmountStart", required = false) Long totalInvestAmountStart,
            @RequestParam(value = "totalInvestAmountEnd", required = false) Long totalInvestAmountEnd,
            @RequestParam(value = "totalWithdrawAmountStart", required = false) Long totalWithdrawAmountStart,
            @RequestParam(value = "totalWithdrawAmountEnd", required = false) Long totalWithdrawAmountEnd,
            @RequestParam(value = "userBalanceStart", required = false) Long userBalanceStart,
            @RequestParam(value = "userBalanceEnd", required = false) Long userBalanceEnd,
            @RequestParam(value = "investCountStart", required = false) Integer investCountStart,
            @RequestParam(value = "investCountEnd", required = false) Integer investCountEnd,
            @RequestParam(value = "loanCountStart", required = false) Integer loanCountStart,
            @RequestParam(value = "loanCountEnd", required = false) Integer loanCountEnd,
            @RequestParam(value = "transformPeriodStart", required = false) Integer transformPeriodStart,
            @RequestParam(value = "transformPeriodEnd", required = false) Integer transformPeriodEnd,
            @RequestParam(value = "invest1st2ndTimingStart", required = false) Integer invest1st2ndTimingStart,
            @RequestParam(value = "invest1st2ndTimingEnd", required = false) Integer invest1st2ndTimingEnd,
            @RequestParam(value = "invest1st3ndTimingStart", required = false) Integer invest1st3ndTimingStart,
            @RequestParam(value = "invest1st3ndTimingEnd", required = false) Integer invest1st3ndTimingEnd,
            @RequestParam(value = "lastInvestTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastInvestTimeStart,
            @RequestParam(value = "lastInvestTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastInvestTimeEnd,
            @RequestParam(value = "repayingAmountStart", required = false) Long repayingAmountStart,
            @RequestParam(value = "repayingAmountEnd", required = false) Long repayingAmountEnd,
            @RequestParam(value = "lastLoginTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastLoginTimeStart,
            @RequestParam(value = "lastLoginTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastLoginTimeEnd,
            @RequestParam(value = "lastLoginSource", required = false) Source lastLoginSource,
            @RequestParam(value = "lastRepayTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastRepayTimeStart,
            @RequestParam(value = "lastRepayTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastRepayTimeEnd,
            @RequestParam(value = "lastWithdrawTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastWithdrawTimeStart,
            @RequestParam(value = "lastWithdrawTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastWithdrawTimeEnd,
            HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.UserMicroModelHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;

        BaseDto<BasePaginationDataDto<UserMicroModelView>> baseDto = consoleUserService.queryUserMicroView(mobile,
                role,
                registerTimeStart,
                registerTimeEnd,
                hasCertify,
                invested,
                totalInvestAmountStart == null ? null : totalInvestAmountStart * 100,
                totalInvestAmountEnd == null ? null : totalInvestAmountEnd * 100,
                totalWithdrawAmountStart == null ? null : totalWithdrawAmountStart * 100,
                totalWithdrawAmountEnd == null ? null : totalWithdrawAmountEnd * 100,
                userBalanceStart == null ? null : userBalanceStart * 100,
                userBalanceEnd == null ? null : userBalanceEnd * 100,
                investCountStart,
                investCountEnd,
                loanCountStart,
                loanCountEnd,
                transformPeriodStart,
                transformPeriodEnd,
                invest1st2ndTimingStart,
                invest1st2ndTimingEnd,
                invest1st3ndTimingStart,
                invest1st3ndTimingEnd,
                lastInvestTimeStart,
                lastInvestTimeEnd,
                repayingAmountStart == null ? null : repayingAmountStart * 100,
                repayingAmountEnd == null ? null : repayingAmountEnd * 100,
                lastLoginTimeStart,
                lastLoginTimeEnd,
                lastLoginSource,
                lastRepayTimeStart,
                lastRepayTimeEnd,
                lastWithdrawTimeStart,
                lastWithdrawTimeEnd,
                index,
                pageSize);
        List<UserMicroModelView> userMicroModelViewList = baseDto.getData().getRecords();
        List<List<String>> userMicroModelDtoList = exportService.buildUserMicroModelDtoList(userMicroModelViewList);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.UserMicroModelHeader, userMicroModelDtoList, response.getOutputStream());
    }

    @RequestMapping(value = "/remain-users", method = RequestMethod.GET)
    public void remainUser(@RequestParam(value = "loginName", required = false) String loginName,
                           @RequestParam(value = "mobile", required = false) String mobile,
                           @RequestParam(value = "registerStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date registerStartTime,
                           @RequestParam(value = "registerEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date registerEndTime,
                           @RequestParam(value = "useExperienceCoupon", required = false) Boolean useExperienceCoupon,
                           @RequestParam(value = "experienceStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date experienceStartTime,
                           @RequestParam(value = "experienceEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date experienceEndTime,
                           @RequestParam(value = "investCountLowLimit", required = false) Integer investCountLowLimit,
                           @RequestParam(value = "investCountHighLimit", required = false) Integer investCountHighLimit,
                           @RequestParam(value = "investSumLowLimit", required = false) String investSumLowLimit,
                           @RequestParam(value = "investSumHighLimit", required = false) String investSumHighLimit,
                           @RequestParam(value = "firstInvestStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date firstInvestStartTime,
                           @RequestParam(value = "firstInvestEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date firstInvestEndTime,
                           @RequestParam(value = "secondInvestStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date secondInvestStartTime,
                           @RequestParam(value = "secondInvestEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date secondInvestEndTime,
                           HttpServletResponse response) throws IOException {
        final int index = 1;
        final int pageSize = 99999999;

        fillExportResponse(response, CsvHeaderType.UserRemainHeader.getDescription());

        BasePaginationDataDto<RemainUserDto> data = consoleUserService.findRemainUsers(loginName, mobile, registerStartTime,
                registerEndTime, useExperienceCoupon, experienceStartTime, experienceEndTime, investCountLowLimit, investCountHighLimit,
                StringUtils.isEmpty(investSumLowLimit) ? null : AmountConverter.convertStringToCent(investSumLowLimit),
                StringUtils.isEmpty(investSumHighLimit) ? null : AmountConverter.convertStringToCent(investSumHighLimit),
                firstInvestStartTime, firstInvestEndTime, secondInvestStartTime, secondInvestEndTime, index, pageSize);
        List<List<String>> csvData = new ArrayList<>();
        for (RemainUserDto dto : data.getRecords()) {
            csvData.add(ExportCsvUtil.dtoToStringList(dto));
        }
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.UserRemainHeader, csvData, response.getOutputStream());
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public void exportUserPoint(@RequestParam(value = "question", required = false) String question,
                                @RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "status", required = false) QuestionStatus status, HttpServletResponse response) throws IOException {
        final int index = 1;
        final int pageSize = Integer.MAX_VALUE;

        fillExportResponse(response, CsvHeaderType.QuestionsHeader.getDescription());
        BaseDto<BasePaginationDataDto<QuestionModel>> data = askRestClient.findQuestionsForConsole(question, mobile, status, index, pageSize);
        List<List<String>> csvData = exportService.buildQuestionsList(data.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.QuestionsHeader, csvData, response.getOutputStream());
    }
}

package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.service.ExportService;
import com.tuotiansudai.console.service.UserServiceConsole;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.point.repository.mapper.UserPointPrizeMapper;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private ExportService exportService;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private LoanRepayService loanRepayService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private UserServiceConsole userServiceConsole;

    @Autowired
    private InvestService investService;

    @Autowired
    RechargeService rechargeService;

    @Autowired
    WithdrawService withdrawService;

    @Autowired
    private UserBillService userBillService;

    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public void exportCoupons(HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.CouponHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findNewbieAndInvestCoupons(index, pageSize);
        List<List<String>> coupons = exportService.buildCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.CouponHeader, coupons, response.getOutputStream());
    }

    @RequestMapping(value = "/interest-coupons", method = RequestMethod.GET)
    public void exportInterestCoupons(HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.InterestCouponsHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findInterestCoupons(index, pageSize);
        List<List<String>> interestCoupons = exportService.buildInterestCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.InterestCouponsHeader, interestCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/red-envelopes", method = RequestMethod.GET)
    public void exportRedEnvelopes(HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.RedEnvelopesHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findRedEnvelopeCoupons(index, pageSize);
        List<List<String>> redEnvelopeCoupons = exportService.buildRedEnvelopeCoupons(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.RedEnvelopesHeader, redEnvelopeCoupons, response.getOutputStream());
    }

    @RequestMapping(value = "/birthday-coupons", method = RequestMethod.GET)
    public void exportBirthdayCoupons(HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.BirthdayCouponsHeader.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<CouponDto> records = couponService.findBirthdayCoupons(index, pageSize);
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

    @RequestMapping(value = "/user-point", method = RequestMethod.GET)
    public void exportUserPoint(@RequestParam(value = "loginName", required = false) String loginName,
                                @RequestParam(value = "userName", required = false) String userName,
                                @RequestParam(value = "mobile", required = false) String mobile, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.UserPointHeader.getDescription());
        List<AccountItemDataDto> accountItemDataDtoList = pointBillService.findUsersAccountPoint(loginName, userName, mobile, null, null);
        List<List<String>> csvData = exportService.buildUserPointToCsvData(accountItemDataDtoList);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.UserPointHeader, csvData, response.getOutputStream());
    }

    @RequestMapping(value = "/coupon-exchange", method = RequestMethod.GET)
    public void exportCouponExchange(HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.CouponExchangeHeader.getDescription());
        List<ExchangeCouponDto> exchangeCouponDtos = couponService.findCouponExchanges(1, Integer.MAX_VALUE);
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
        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = investTransferService.findTransferApplicationPaginationList(transferApplicationId, startTime, endTime, status, transferrerMobile, transfereeMobile, loanId, source, 1, Integer.MAX_VALUE);
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
        BaseDto<BasePaginationDataDto<LoanRepayDataItemDto>> baseDto = loanRepayService.findLoanRepayPagination(1, Integer.MAX_VALUE,
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
        List<LoanListDto> loanListDtos = loanService.findLoanList(status, loanId, loanName,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? new DateTime(9999, 12, 31, 0, 0, 0).toDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                index, Integer.MAX_VALUE);
        List<List<String>> csvData = exportService.buildConsoleLoanList(loanListDtos);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleLoanList, csvData, httpServletResponse.getOutputStream());

    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public void exportUsers(String loginName, String email, String mobile,
                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                            RoleStage roleStage, String referrerMobile, String channel,
                            @RequestParam(value = "source", required = false) Source source, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleUsers.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BaseDto<BasePaginationDataDto<UserItemDataDto>> baseDto = userServiceConsole.findAllUser(loginName, email, mobile,
                beginTime, endTime, source, roleStage, referrerMobile, channel, index, pageSize);
        List<List<String>> usersData = exportService.buildUsers(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleUsers, usersData, response.getOutputStream());
    }

    @RequestMapping(value = "/invests", method = RequestMethod.GET)
    public void exportInvests(@RequestParam(name = "loanId", required = false) Long loanId,
                              @RequestParam(name = "mobile", required = false) String investorMobile,
                              @RequestParam(name = "channel", required = false) String channel,
                              @RequestParam(name = "source", required = false) Source source,
                              @RequestParam(name = "role", required = false) String role,
                              @RequestParam(name = "investStatus", required = false) InvestStatus investStatus,
                              @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                              @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleInvests.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        InvestPaginationDataDto investPagination = investService.getInvestPagination(loanId, investorMobile, channel, source, role, index, pageSize, startTime, endTime, investStatus, null);
        List<InvestPaginationItemDataDto> records = investPagination.getRecords();
        List<List<String>> investsData = exportService.buildInvests(records);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleInvests, investsData, response.getOutputStream());
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public void exportRecharge(@RequestParam(value = "rechargeId", required = false) String rechargeId,
                               @RequestParam(value = "mobile", required = false) String mobile,
                               @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                               @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                               @RequestParam(value = "status", required = false) RechargeStatus status,
                               @RequestParam(value = "source", required = false) RechargeSource source,
                               @RequestParam(value = "channel", required = false) String channel, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleRecharge.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> baseDto = rechargeService.findRechargePagination(rechargeId, mobile, source, status, channel, index, pageSize, startTime, endTime);
        List<List<String>> rechargeData = exportService.buildRecharge(baseDto.getData().getRecords());
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleRecharge, rechargeData, response.getOutputStream());

    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public void exportWithdraw(@RequestParam(value = "withdrawId", required = false) String withdrawId,
                               @RequestParam(value = "mobile", required = false) String mobile,
                               @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                               @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                               @RequestParam(value = "status", required = false) WithdrawStatus status,
                               @RequestParam(value = "source", required = false) Source source, HttpServletResponse response) throws IOException {
        fillExportResponse(response, CsvHeaderType.ConsoleWithdraw.getDescription());
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> baseDto = withdrawService.findWithdrawPagination(withdrawId, mobile, status, source, index, pageSize, startTime, endTime);
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
        List<UserBillPaginationView> userBillModels = userBillService.findUserFunds(userBillBusinessType, userBillOperationType, mobile, startTime, endTime, index, pageSize);
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
        List<UserItemDataDto> dataDtos = userServiceConsole.findUsersAccountBalance(mobile, balanceMin, balanceMax, index, pageSize);
        List<List<String>> accountBalanceData = exportService.buildAccountBalance(dataDtos);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.AccountBalance, accountBalanceData, response.getOutputStream());
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

}

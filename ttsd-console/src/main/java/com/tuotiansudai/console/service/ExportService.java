package com.tuotiansudai.console.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.point.dto.ProductOrderDto;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.LoanAchievementView;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.UserBillPaginationView;

import java.util.List;

public interface ExportService {

    <T> List<List<String>> buildUserPointToCsvData(List<T> originList);

    List<List<String>> buildLoanRepayCsvData(List<LoanRepayDataItemDto> loanRepayDataItemDtos);

    List<List<String>> buildTransferListCsvData(List<TransferApplicationPaginationItemDataDto> transferApplicationPaginationItemDataDtos);

    List<List<String>> buildSystemBillCsvData(List<SystemBillPaginationItemDataDto> SystemBillPaginationItemDataDtos);

    List<List<String>> buildCouponExchangeCsvData(List<ExchangeCouponDto> exchangeCouponDtos);

    List<List<String>> buildCoupons(List<CouponDto> records);

    List<List<String>> buildInterestCoupons(List<CouponDto> records);

    List<List<String>> buildRedEnvelopeCoupons(List<CouponDto> records);

    List<List<String>> buildBirthdayCoupons(List<CouponDto> records);

    List<List<String>> buildPointPrize(List<PointPrizeWinnerViewDto> records);

    List<List<String>> buildConsoleLoanList(List<LoanListDto> records);

    List<List<String>> buildProductOrderList(List<ProductOrderDto> records);

    List<List<String>> buildUsers(List<UserItemDataDto> records);

    List<List<String>> buildInvests(List<InvestPaginationItemDataDto> records);

    List<List<String>> buildRecharge(List<RechargePaginationItemDataDto> records);

    List<List<String>> buildWithdraw(List<WithdrawPaginationItemDataDto> records);

    List<List<String>> buildUserFunds(List<UserBillPaginationView> records);

    List<List<String>> buildAccountBalance(List<UserItemDataDto> records);

    List<List<String>> buildFeedBack(List<FeedbackModel> records);

    List<List<String>> buildInvestAchievement(List<LoanAchievementView> records);

    List<List<String>> buildReferrer(List<ReferrerManageView> records);

    List<AccountItemDataDto> findUsersAccountPoint(String loginName, String userName, String mobile, int currentPageNo, int pageSize);
}

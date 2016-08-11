package com.tuotiansudai.console.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;

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

    List<AccountItemDataDto> findUsersAccountPoint(String loginName, String userName, String mobile, int currentPageNo, int pageSize);

}

package com.tuotiansudai.console.service;

import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.LoanRepayDataItemDto;
import com.tuotiansudai.dto.SystemBillPaginationItemDataDto;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;

import java.util.List;

public interface ExportService {

   <T> List<List<String>> buildOriginListToCsvData(List<T> originList);

   List<List<String>> buildLoanRepayCsvData(List<LoanRepayDataItemDto> loanRepayDataItemDtos);

   List<List<String>> buildTransferListCsvData(List<TransferApplicationRecordDto> transferApplicationRecordDtos);

   List<List<String>> buildSystemBillCsvData(List<SystemBillPaginationItemDataDto> SystemBillPaginationItemDataDtos);

   List<List<String>> buildCouponExchangeCsvData(List<ExchangeCouponDto> exchangeCouponDtos);
}

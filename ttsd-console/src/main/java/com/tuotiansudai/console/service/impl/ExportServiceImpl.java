package com.tuotiansudai.console.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ExportService;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.LoanRepayDataItemDto;
import com.tuotiansudai.dto.SystemBillPaginationItemDataDto;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.util.ExportCsvUtil;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
@Service
public class ExportServiceImpl implements ExportService{

    @Override
    public <T> List<List<String>> buildOriginListToCsvData(List<T> originList) {
        List<List<String>> csvData = new ArrayList<>();
        for (T item : originList){
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(item);
            csvData.add(dtoStrings);
        }
        return csvData;
    }

    @Override
    public List<List<String>> buildLoanRepayCsvData(List<LoanRepayDataItemDto> loanRepayDataItemDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for(LoanRepayDataItemDto loanRepayDataItemDto:loanRepayDataItemDtos){
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(loanRepayDataItemDto.getLoanId()));
            row.add(loanRepayDataItemDto.getLoanName());
            row.add(loanRepayDataItemDto.getAgentLoginName());
            row.add(loanRepayDataItemDto.getRepayDate() == null ?"-":loanRepayDataItemDto.getRepayDate().toString());
            row.add(loanRepayDataItemDto.getActualRepayDate() == null ? "-" : loanRepayDataItemDto.getActualRepayDate().toString());
            row.add(String.valueOf(loanRepayDataItemDto.getPeriod()));
            row.add(loanRepayDataItemDto.getCorpus());
            row.add(loanRepayDataItemDto.getExpectedInterest());
            row.add(loanRepayDataItemDto.getTotalAmount());
            row.add(loanRepayDataItemDto.getActualRepayAmount());
            row.add(loanRepayDataItemDto.getLoanRepayStatus().getDescription());
            if(loanRepayDataItemDto.getActualRepayDate() != null && loanRepayDataItemDto.getActualRepayDate().before(loanRepayDataItemDto.getRepayDate())){
                row.add("提前还款");
            }
            rows.add(row);

        }
        return rows;
    }

    @Override
    public List<List<String>> buildTransferListCsvData(List<TransferApplicationRecordDto> transferApplicationRecordDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for(TransferApplicationRecordDto transferApplicationRecordDto:transferApplicationRecordDtos){
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(transferApplicationRecordDto.getTransferApplicationId()));
            row.add(String.valueOf(transferApplicationRecordDto.getLoanId()));
            row.add(transferApplicationRecordDto.getTransferrerMobile());
            row.add(String.valueOf(transferApplicationRecordDto.getTransferAmount()));
            row.add(String.valueOf(transferApplicationRecordDto.getLeftPeriod()));
            row.add(transferApplicationRecordDto.getTransferStatus().getDescription());
            row.add(transferApplicationRecordDto.getTransferTime().toString());
            row.add(transferApplicationRecordDto.getTransfereeMobile());
            row.add(String.valueOf(transferApplicationRecordDto.getTransferFee()));
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<List<String>> buildSystemBillCsvData(List<SystemBillPaginationItemDataDto> systemBillPaginationItemDataDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for(SystemBillPaginationItemDataDto systemBillPaginationItemDataDto:systemBillPaginationItemDataDtos){
            List<String> row = Lists.newArrayList();
            row.add(systemBillPaginationItemDataDto.getCreatedTime().toString());
            row.add(systemBillPaginationItemDataDto.getOperationType());
            row.add(systemBillPaginationItemDataDto.getBusinessType());
            row.add(systemBillPaginationItemDataDto.getAmount());
            row.add(systemBillPaginationItemDataDto.getDetail());
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<List<String>> buildCouponExchangeCsvData(List<ExchangeCouponDto> exchangeCouponDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for(ExchangeCouponDto exchangeCouponDto:exchangeCouponDtos){
            List<String> row = Lists.newArrayList();
            row.add(exchangeCouponDto.getCouponType().getName());
            row.add(exchangeCouponDto.getCouponType() == CouponType.INVEST_COUPON?exchangeCouponDto.getAmount():"-");
            row.add(exchangeCouponDto.getCouponType() == CouponType.INTEREST_COUPON?String.valueOf(exchangeCouponDto.getRate() * 100) :"-");
            row.add(String.valueOf(exchangeCouponDto.getTotalCount()));
            row.add(String.valueOf(exchangeCouponDto.getIssuedCount()));
            row.add(String.valueOf(exchangeCouponDto.getExchangePoint()));
            row.add(MessageFormat.format("{0}至{1}", exchangeCouponDto.getStartTime().toString(), exchangeCouponDto.getEndTime().toString()));
            row.add(String.valueOf(exchangeCouponDto.getDeadline()));
            row.add(Joiner.on(",").join(exchangeCouponDto.getProductTypes()));
            row.add(exchangeCouponDto.getInvestLowerLimit());
            rows.add(row);
        }
        return rows;
    }
}

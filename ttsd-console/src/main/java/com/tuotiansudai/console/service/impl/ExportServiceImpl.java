package com.tuotiansudai.console.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ExportService;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.LoanRepayDataItemDto;
import com.tuotiansudai.dto.SystemBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {

    @Override
    public <T> List<List<String>> buildOriginListToCsvData(List<T> originList) {
        List<List<String>> csvData = new ArrayList<>();
        for (T item : originList) {
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(item);
            csvData.add(dtoStrings);
        }
        return csvData;
    }

    @Override
    public List<List<String>> buildLoanRepayCsvData(List<LoanRepayDataItemDto> loanRepayDataItemDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for (LoanRepayDataItemDto loanRepayDataItemDto : loanRepayDataItemDtos) {
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(loanRepayDataItemDto.getLoanId()));
            row.add(loanRepayDataItemDto.getLoanName());
            row.add(loanRepayDataItemDto.getAgentLoginName());
            row.add(loanRepayDataItemDto.getRepayDate() == null ? "-" : loanRepayDataItemDto.getRepayDate().toString());
            row.add(loanRepayDataItemDto.getActualRepayDate() == null ? "-" : loanRepayDataItemDto.getActualRepayDate().toString());
            row.add(String.valueOf(loanRepayDataItemDto.getPeriod()));
            row.add(loanRepayDataItemDto.getCorpus());
            row.add(loanRepayDataItemDto.getExpectedInterest());
            row.add(loanRepayDataItemDto.getTotalAmount());
            row.add(loanRepayDataItemDto.getActualRepayAmount());
            row.add(loanRepayDataItemDto.getLoanRepayStatus().getDescription());
            if (loanRepayDataItemDto.getActualRepayDate() != null && loanRepayDataItemDto.getActualRepayDate().before(loanRepayDataItemDto.getRepayDate())) {
                row.add("提前还款");
            }
            rows.add(row);

        }
        return rows;
    }

    @Override
    public List<List<String>> buildTransferListCsvData(List<TransferApplicationRecordDto> transferApplicationRecordDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for (TransferApplicationRecordDto transferApplicationRecordDto : transferApplicationRecordDtos) {
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
        for (SystemBillPaginationItemDataDto systemBillPaginationItemDataDto : systemBillPaginationItemDataDtos) {
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
        for (ExchangeCouponDto exchangeCouponDto : exchangeCouponDtos) {
            List<String> row = Lists.newArrayList();
            row.add(exchangeCouponDto.getCouponType().getName());
            row.add(exchangeCouponDto.getCouponType() == CouponType.INVEST_COUPON ? exchangeCouponDto.getAmount() : "-");
            row.add(exchangeCouponDto.getCouponType() == CouponType.INTEREST_COUPON ? String.valueOf(exchangeCouponDto.getRate() * 100) : "-");
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

    @Override
    public List<List<String>> buildCoupons(List<CouponDto> records) {
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

    @Override
    public List<List<String>> buildInterestCoupons(List<CouponDto> records) {
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

    @Override
    public List<List<String>> buildRedEnvelopeCoupons(List<CouponDto> records) {
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


    @Override
    public List<List<String>> buildBirthdayCoupons(List<CouponDto> records) {
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

    @Override
    public List<List<String>> buildPointPrize(List<PointPrizeWinnerViewDto> records) {
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

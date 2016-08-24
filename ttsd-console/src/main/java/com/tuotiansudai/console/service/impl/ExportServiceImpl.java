package com.tuotiansudai.console.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ExportService;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public <T> List<List<String>> buildUserPointToCsvData(List<T> originList) {
        List<List<String>> csvData = new ArrayList<>();
        for (T item : originList) {
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(item);
            csvData.add(dtoStrings);
        }
        return csvData;
    }

    @Override
    public List<AccountItemDataDto> findUsersAccountPoint(String loginName, String userName, String mobile, int currentPageNo, int pageSize) {
        List<AccountModel> accountModels = accountMapper.findUsersAccountPoint(loginName, userName, mobile, (currentPageNo - 1) * pageSize, pageSize);

        List<AccountItemDataDto> accountItemDataDtoList = new ArrayList<>();
        for (AccountModel accountModel : accountModels) {
            AccountItemDataDto accountItemDataDto = new AccountItemDataDto(accountModel);
            accountItemDataDto.setTotalPoint(accountMapper.findByLoginName(accountModel.getLoginName()).getPoint());
            accountItemDataDto.setMobile(userMapper.findByLoginName(accountModel.getLoginName()).getMobile());
            accountItemDataDtoList.add(accountItemDataDto);
        }
        return accountItemDataDtoList;
    }

    @Override
    public List<List<String>> buildLoanRepayCsvData(List<LoanRepayDataItemDto> loanRepayDataItemDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for (LoanRepayDataItemDto loanRepayDataItemDto : loanRepayDataItemDtos) {
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(loanRepayDataItemDto.getLoanId()));
            row.add(loanRepayDataItemDto.getLoanName());
            row.add(loanRepayDataItemDto.getAgentLoginName());
            row.add(loanRepayDataItemDto.getRepayDate() == null ? "-" : new DateTime(loanRepayDataItemDto.getRepayDate()).toString("yyyy-MM-dd"));
            row.add(loanRepayDataItemDto.getActualRepayDate() == null ? "-" : new DateTime(loanRepayDataItemDto.getActualRepayDate()).toString("yyyy-MM-dd"));
            row.add(String.valueOf("第" + loanRepayDataItemDto.getPeriod() + "期"));
            row.add(loanRepayDataItemDto.getCorpus());
            row.add(loanRepayDataItemDto.getExpectedInterest());
            row.add(loanRepayDataItemDto.getTotalAmount());
            row.add(loanRepayDataItemDto.getActualRepayAmount());
            if (loanRepayDataItemDto.getActualRepayDate() != null && loanRepayDataItemDto.getActualRepayDate().before(loanRepayDataItemDto.getRepayDate())) {
                row.add("提前还款");
            } else {
                row.add(loanRepayDataItemDto.getLoanRepayStatus().getDescription());
            }
            rows.add(row);

        }
        return rows;
    }

    @Override
    public List<List<String>> buildTransferListCsvData(List<TransferApplicationPaginationItemDataDto> transferApplicationPaginationItemDataDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for (TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto : transferApplicationPaginationItemDataDtos) {
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(transferApplicationPaginationItemDataDto.getTransferApplicationId()));
            row.add(String.valueOf(transferApplicationPaginationItemDataDto.getLoanId()));
            row.add(transferApplicationPaginationItemDataDto.getTransferrerMobile());
            row.add(transferApplicationPaginationItemDataDto.getInvestAmount());
            row.add(String.valueOf(transferApplicationPaginationItemDataDto.getTransferAmount()));
            row.add(String.valueOf(transferApplicationPaginationItemDataDto.getLeftPeriod()));
            row.add(transferApplicationPaginationItemDataDto.getTransferStatus());
            row.add(new DateTime(transferApplicationPaginationItemDataDto.getTransferTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(transferApplicationPaginationItemDataDto.getTransfereeMobile());
            row.add(transferApplicationPaginationItemDataDto.getSource() == null ? "" : transferApplicationPaginationItemDataDto.getSource().name());
            row.add(String.valueOf(transferApplicationPaginationItemDataDto.getTransferFee()));
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<List<String>> buildSystemBillCsvData(List<SystemBillPaginationItemDataDto> systemBillPaginationItemDataDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for (SystemBillPaginationItemDataDto systemBillPaginationItemDataDto : systemBillPaginationItemDataDtos) {
            List<String> row = Lists.newArrayList();
            row.add(new DateTime(systemBillPaginationItemDataDto.getCreatedTime()).toString("yyyy-MM-dd HH:mm"));
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
            row.add(MessageFormat.format("{0}至{1}", new DateTime(exchangeCouponDto.getStartTime()).toString("yyyy-MM-dd HH:mm"), new DateTime(exchangeCouponDto.getEndTime()).toString("yyyy-MM-dd HH:mm")));
            row.add(String.valueOf(exchangeCouponDto.getDeadline()));
            row.add(Joiner.on(" ").join(exchangeCouponDto.getProductTypes()));
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

    @Override
    public List<List<String>> buildConsoleLoanList(List<LoanListDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (LoanListDto loanListDto : records) {
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(loanListDto.getId()));
            row.add(loanListDto.getName());
            row.add(loanListDto.getProductType() == null ? "" : loanListDto.getProductType().getName());
            row.add(loanListDto.getLoanerUserName());
            row.add(loanListDto.getAgentLoginName());
            row.add(AmountConverter.convertCentToString(loanListDto.getLoanAmount()));
            row.add(loanListDto.getBasicRate() + "/" + loanListDto.getActivityRate());
            row.add("-");
            row.add(loanListDto.getStatus().getDescription());
            row.add(new DateTime(loanListDto.getCreatedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            rows.add(row);
        }
        return rows;
    }
}

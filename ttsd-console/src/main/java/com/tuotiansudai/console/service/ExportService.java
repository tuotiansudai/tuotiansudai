package com.tuotiansudai.console.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.repository.model.UserMicroModelView;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.point.repository.dto.ChannelPointDetailPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.ProductOrderDto;
import com.tuotiansudai.point.repository.model.PointPrizeWinnerViewDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportService {

    public <T> List<List<String>> buildUserPointToCsvData(List<T> originList) {
        List<List<String>> csvData = new ArrayList<>();
        for (T item : originList) {
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(item);
            csvData.add(dtoStrings);
        }
        return csvData;
    }

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

    public List<List<String>> buildCreditLoanBillCsvData(List<CreditLoanBillPaginationItemDataDto> CreditLoanBillPaginationItemDataDtos) {
        List<List<String>> rows = Lists.newArrayList();
        for (CreditLoanBillPaginationItemDataDto creditLoanBillPaginationItemDataDto : CreditLoanBillPaginationItemDataDtos) {
            List<String> row = Lists.newArrayList();
            row.add(new DateTime(creditLoanBillPaginationItemDataDto.getCreatedTime()).toString("yyyy-MM-dd HH:mm"));
            row.add(creditLoanBillPaginationItemDataDto.getOrderId());
            row.add(creditLoanBillPaginationItemDataDto.getOperationType());
            row.add(creditLoanBillPaginationItemDataDto.getBusinessType());
            row.add(creditLoanBillPaginationItemDataDto.getAmount());
            rows.add(row);
        }
        return rows;
    }

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

    public List<List<String>> buildUsers(List<UserItemDataDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (UserItemDataDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getLoginName());
            row.add(record.isBankCard() ? "是" : "否");
            row.add(record.getUserName());
            row.add(record.getMobile());
            row.add(record.getEmail());
            row.add(record.getReferrerMobile());
            row.add(record.isReferrerStaff() ? "是" : "否");
            row.add(record.getSource() != null ? record.getSource().name() : "");
            row.add(record.getChannel());
            row.add(new DateTime(record.getRegisterTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add("1".equals(record.getAutoInvestStatus()) ? "是" : "否");
            List<UserRoleModel> userRoleModels = record.getUserRoles();
            List<String> userRole = Lists.transform(userRoleModels, model -> model.getRole().getDescription());
            row.add(StringUtils.join(userRole, ";"));
            row.add(UserStatus.ACTIVE.equals(record.getStatus()) ? "正常" : "禁用");
            row.add(record.getBirthday());
            row.add(record.getProvince());
            row.add(record.getCity());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildInvests(List<InvestPaginationItemDataDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (InvestPaginationItemDataDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(record.getLoanId()));
            row.add(record.getLoanName());
            row.add(String.valueOf(record.getLoanPeriods()));
            row.add(record.getInvestorLoginName());
            row.add(record.isInvestorStaff() ? "是" : "否");
            row.add(record.getInvestorUserName());
            row.add(record.getInvestorMobile());
            row.add(record.getBirthday());
            row.add(record.getProvince());
            row.add(record.getCity());
            row.add(record.getReferrerLoginName());
            row.add(record.getReferrerLoginName() != null ? record.isReferrerStaff() ? "是" : "否" : "");
            row.add(record.getReferrerUserName());
            row.add(record.getReferrerMobile());

            row.add(record.getChannel() != null ? record.getChannel() : "");
            row.add(record.getSource().name());
            row.add(new DateTime(record.getInvestTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(record.getInvestAmount());

            row.add((record.getCouponDetail() == null ? "-" : record.getCouponDetail()) + "/" + (record.getCouponActualInterest() == null ? "-" : record.getCouponActualInterest()));
            row.add(record.getExtraDetail() + "/" + record.getExtraActualInterest());

            row.add(record.getInvestStatus().getDescription());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildRecharge(List<BankRechargePaginationView> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (BankRechargePaginationView record : records) {
            List<String> row = Lists.newArrayList();
            row.add(new BigDecimal(record.getId()).toString());
            row.add(new DateTime(record.getCreatedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(record.getLoginName());
            row.add(record.isStaff() ? "是" : "否");
            row.add(record.getUserName());
            row.add(record.getMobile());
            row.add(AmountConverter.convertCentToString(record.getAmount()));
            row.add("FAST_PAY".equals(record.getPayType()) ? "是" : "否");
            row.add(record.getStatus().getDescription());
            row.add(record.getSource().name());
            row.add(record.getChannel());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildWithdraw(List<WithdrawPaginationItemDataDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (WithdrawPaginationItemDataDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(new BigDecimal(record.getWithdrawId()).toString());
            row.add(new DateTime(record.getCreatedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(new DateTime(record.getApplyNotifyTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(new DateTime(record.getNotifyTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(record.getLoginName());
            row.add(record.isStaff() ? "是" : "否");
            row.add(record.getUserName());
            row.add(record.getMobile());
            row.add(record.getAmount());
            row.add(record.getFee());
            row.add(record.getBankCard());
            row.add(record.getStatus());
            row.add(record.getSource().name());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildUserFunds(List<UserBillPaginationView> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (UserBillPaginationView record : records) {
            List<String> row = Lists.newArrayList();
            DateTime dateTime = new DateTime(record.getCreatedTime());
            row.add(dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            row.add(String.valueOf(record.getId()));
            row.add(record.getLoginName());
            row.add(record.isStaff() ? "是" : "否");
            row.add(record.getUserName());
            row.add(record.getMobile());
            row.add(record.getOperationType().getDescription());
            row.add(record.getBusinessType().getDescription());
            row.add(String.valueOf(new BigDecimal(record.getAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
            row.add(String.valueOf(new BigDecimal(record.getBalance()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
            row.add(String.valueOf(new BigDecimal(record.getFreeze()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildAccountBalance(List<UserItemDataDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (UserItemDataDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getLoginName());
            row.add(record.getUserName());
            row.add(record.getMobile());
            row.add(record.getProvince() != null ? record.getProvince() : "");
            row.add(record.getLastBillTime() != null ? new DateTime(record.getLastBillTime()).toString("yyyy-MM-dd HH:mm:ss") : "");
            row.add(record.getBalance());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildFeedBack(List<FeedbackModel> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (FeedbackModel record : records) {
            List<String> row = Lists.newArrayList();
            row.add(String.valueOf(record.getId()));
            row.add(record.getLoginName());
            row.add(record.getContact());
            row.add(record.getSource().name());
            row.add(record.getType().getDesc());
            row.add(record.getContent());
            row.add(new DateTime(record.getCreatedTime()).toString("yyyy-MM-dd HH:mm"));
            row.add(record.getStatus().getDesc());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildInvestAchievement(List<LoanAchievementView> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (LoanAchievementView record : records) {

            List<String> row = Lists.newArrayList();
            row.add(record.getName());
            row.add(record.getLoanStatus().getDescription());
            row.add(String.valueOf(record.getPeriods()));
            row.add(AmountConverter.convertCentToString(record.getLoanAmount()));
            if (record.getMaxAmountLoginName() != null) {
                row.add(record.getMaxAmountLoginName() + "/" + AmountConverter.convertCentToString(record.getMaxAmount()));
            } else {
                row.add("/");
            }
            if (record.getFirstInvestLoginName() != null) {
                row.add(record.getFirstInvestLoginName() + "/" + AmountConverter.convertCentToString(record.getFirstInvestAmount()));
            } else {
                row.add("/");
            }
            if (record.getLastInvestLoginName() != null) {
                row.add(record.getLastInvestLoginName() + "/" + AmountConverter.convertCentToString(record.getLastInvestAmount()));
            } else {
                row.add("/");
            }
            row.add(record.getRaisingCompleteTime() != null ? new DateTime(record.getRaisingCompleteTime()).toString("yyyy-MM-dd HH:mm:ss") : "");
            row.add(record.getFirstInvestDuration());
            row.add(record.getCompleteInvestDuration());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildReferrer(List<ReferrerManageView> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (ReferrerManageView record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getLoanName());
            row.add(String.valueOf(record.getPeriods()));
            row.add(record.getInvestMobile());
            row.add(record.getInvestName());
            row.add(String.valueOf(new BigDecimal(record.getInvestAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
            row.add(new DateTime(record.getInvestTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(record.getSource().name());
            row.add(record.getReferrerMobile());
            row.add(record.getReferrerName());
            row.add(Lists.newArrayList(Role.SD_STAFF, Role.ZC_STAFF).contains(record.getRole()) ? "是" : "否");
            row.add(String.valueOf(record.getLevel()));
            row.add(String.valueOf(new BigDecimal(record.getRewardAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
            row.add(record.getStatus() == ReferrerRewardStatus.SUCCESS ? "已入账" : "入账失败");
            row.add(new DateTime(record.getRewardTime()).toString("yyyy-MM-dd HH:mm:ss"));
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildProductOrderList(List<ProductOrderDto> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (ProductOrderDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getLoginName());
            row.add(String.valueOf(record.getActualPoints()));
            row.add(new DateTime(record.getCreatedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(String.valueOf(record.getNum()));
            row.add(record.getContact());
            row.add(record.getMobile());
            row.add(record.getAddress());
            String consignment = record.isConsignment() ? "已发货" : "未发货";
            row.add(consignment);
            row.add(new DateTime(record.getConsignmentTime()).toString("yyyy-MM-dd HH:mm:ss"));
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildChannelPointDetailList(List<ChannelPointDetailPaginationItemDataDto> itemDatas) {
        List<List<String>> rows = Lists.newArrayList();

        for (ChannelPointDetailPaginationItemDataDto itemData : itemDatas) {
            List<String> row = Lists.newArrayList();
            row.add(itemData.getUserName());
            row.add(itemData.getMobile());
            row.add(itemData.getChannel());
            row.add(String.valueOf(itemData.getPoint()));
            row.add(itemData.isSuccess() ? "成功" : "失败");
            row.add(StringUtils.isEmpty(itemData.getRemark()) ? "" : itemData.getRemark());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildPointConsumeList(List<PointBillPaginationItemDataDto> itemDatas) {
        List<List<String>> rows = Lists.newArrayList();

        for (PointBillPaginationItemDataDto itemData : itemDatas) {
            List<String> row = Lists.newArrayList();
            row.add(new DateTime(itemData.getCreatedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(itemData.getOrderId() == null ? "" : String.valueOf(itemData.getOrderId()));
            row.add(StringUtils.isEmpty(itemData.getUserName()) ? "" : itemData.getUserName());
            row.add(StringUtils.isEmpty(itemData.getMobile()) ? "" : itemData.getMobile());
            row.add(String.valueOf(itemData.getPoint() * -1));
            row.add(StringUtils.isEmpty(itemData.getChannel()) ? "" : itemData.getChannel());
            row.add(String.valueOf(itemData.getChannelPoint() * -1));
            row.add(String.valueOf(itemData.getSudaiPoint() * -1));
            row.add(itemData.getBusinessType().getDescription());
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildCouponDetailsDtoList(List<CouponDetailsDto> couponDetailsDtoList) {
        List<List<String>> rows = Lists.newArrayList();
        int index = 1;

        long investAmount = 0l;
        long interest = 0l;
        for (CouponDetailsDto couponDetailsDto : couponDetailsDtoList) {
            investAmount += couponDetailsDto.getInvestAmount() != null ? couponDetailsDto.getInvestAmount() : 0l;
            interest += couponDetailsDto.getAnnualInterest() != null ? couponDetailsDto.getAnnualInterest() : 0l;
        }

        for (CouponDetailsDto couponDetailsDto : couponDetailsDtoList) {
            List<String> row = Lists.newArrayList();
            row.add(couponDetailsDto.getLoginName());
            row.add(new DateTime(couponDetailsDto.getUsedTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(couponDetailsDto.getInvestAmount() != null ? AmountConverter.convertCentToString(couponDetailsDto.getInvestAmount()) : "未使用");
            row.add(couponDetailsDto.getAnnualInterest() != null ? AmountConverter.convertCentToString(couponDetailsDto.getAnnualInterest()) : "未使用");
            row.add(couponDetailsDto.getProductType() != null ? couponDetailsDto.getProductType().getDuration() + "天" : "未使用");
            row.add(couponDetailsDto.getLoanId() != null ? String.valueOf(couponDetailsDto.getLoanId()) : "未使用");
            row.add(couponDetailsDto.getLoanName() != null ? String.valueOf(couponDetailsDto.getLoanName()) : "未使用");
            if (index == 1) {
                row.add(String.valueOf(investAmount));
                row.add(String.valueOf(interest));
            } else {
                row.add("0");
                row.add("0");
            }
            index++;
            rows.add(row);
        }
        return rows;
    }

    public List<List<String>> buildUserMicroModelDtoList(List<UserMicroModelView> userMicroModelViews) {
        List<List<String>> rows = Lists.newArrayList();

        for (UserMicroModelView userMicroModelView : userMicroModelViews) {
            List<String> row = Lists.newArrayList();
            row.add(userMicroModelView.getUserName());
            row.add(userMicroModelView.getMobile());
            row.add(userMicroModelView.getChannel());
            row.add(new DateTime(userMicroModelView.getRegisterTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(String.valueOf(userMicroModelView.getNoInvestPeriod()));
            row.add(userMicroModelView.isInvested() ? "是" : "否");
            row.add(String.valueOf(((double) userMicroModelView.getTotalInvestAmount()) / 100));
            row.add(String.valueOf(userMicroModelView.getInvestCount()));
            row.add(userMicroModelView.getAverageInvestAmount() != null ? String.format("%.2f", userMicroModelView.getAverageInvestAmount() / 100) : "-");
            row.add(String.valueOf(userMicroModelView.getLoanCount()));
            row.add(userMicroModelView.getAverageLoanInvestAmount() != null ? String.format("%.2f", userMicroModelView.getAverageLoanInvestAmount() / 100) : "-");
            row.add(userMicroModelView.getTransformPeriod() != null ? String.valueOf(userMicroModelView.getTransformPeriod()) : "-");
            row.add(userMicroModelView.getInvest1st2ndTiming() != null ? String.valueOf(userMicroModelView.getInvest1st2ndTiming()) : "-");
            row.add(userMicroModelView.getInvest1st3rdTiming() != null ? String.valueOf(userMicroModelView.getInvest1st3rdTiming()) : "-");
            row.add(new DateTime(userMicroModelView.getLastInvestTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(String.valueOf(((double) userMicroModelView.getTotalRepayingAmount()) / 100));
            row.add(new DateTime(userMicroModelView.getLastLoginTime()).toString("yyyy-MM-dd HH:mm:ss"));
            row.add(userMicroModelView.getLastLoginToNow() != null ? String.valueOf(userMicroModelView.getLastLoginToNow()) : "-");
            row.add(userMicroModelView.getLastLoginSource() != null ? userMicroModelView.getLastLoginSource().name() : "-");

            rows.add(row);
        }
        return rows;
    }


    public List<List<String>> buildQuestionsList(List<QuestionModel> records) {
        List<List<String>> rows = Lists.newArrayList();
        for (QuestionModel record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getQuestion());
            row.add("http://ask.tuotiansudai.com/question/" + record.getId());
            rows.add(row);
        }
        return rows;
    }
}

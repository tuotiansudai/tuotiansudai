package com.tuotiansudai.paywrapper.repository.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DailyValidationMapper {

    List<Map<String, String>> findInvestRepayTransactions();

    List<Map<String, String>> findRedEnvelopTransactions();

    List<Map<String, String>> findCouponRepayTransactions();

    List<Map<String, String>> findExtraRateTransactions();

    List<Map<String, String>> findInvestTransactions();

    List<Map<String, String>> findReferrerRewardTransactions();
}

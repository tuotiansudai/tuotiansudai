package com.tuotiansudai.diagnosis.bill;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.diagnosis.bill.diagnoses.InvestCouponFeeDiagnosis;
import com.tuotiansudai.diagnosis.config.DiagnosisConfig;
import com.tuotiansudai.diagnosis.repository.UserBillExtMapper;
import com.tuotiansudai.diagnosis.support.Diagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.DiagnosisResult;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.UserBillModel;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
class UserBillDiagnosis implements Diagnosis {
    private static Logger logger = LoggerFactory.getLogger(UserBillDiagnosis.class);

    private final UserBillMapper userBillMapper;
    private final UserBillExtMapper userBillExtMapper;
    private final long[] sortedKnownBadBills;
    private final Map<UserBillBusinessType, UserBillBusinessDiagnosis> diagnosisMap;
    private final BankAccountMapper bankAccountMapper;
    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    public UserBillDiagnosis(UserBillMapper userBillMapper,
                             Set<UserBillBusinessDiagnosis> diagnoses,
                             UserBillExtMapper userBillExtMapper,
                             DiagnosisConfig diagnosisConfig, BankAccountMapper bankAccountMapper) {
        this.userBillMapper = userBillMapper;
        this.userBillExtMapper = userBillExtMapper;
        this.sortedKnownBadBills = buildSortedKnownBadBills(diagnosisConfig);
        this.diagnosisMap = diagnoses.stream()
                .filter(d -> !(d instanceof InvestCouponFeeDiagnosis))
                .collect(Collectors.toMap(
                        UserBillBusinessDiagnosis::getSupportedBusinessType,
                        d -> d));
        this.bankAccountMapper = bankAccountMapper;
    }

    private long[] buildSortedKnownBadBills(DiagnosisConfig diagnosisConfig) {
        long[] knownBadBills = diagnosisConfig.getKnownBadBills();
        Arrays.sort(knownBadBills);
        return knownBadBills;
    }

    @Override
    public List<DiagnosisResult> diagnosis(LocalDateTime lastFireTime, String[] args) {
        List<String> specialsUsers;
        if (ArrayUtils.isNotEmpty(args)) {
            specialsUsers = extraDiagnosisUsers(args);
        } else {
            LocalDate sinceDate = lastFireTime == null ? null : lastFireTime.toLocalDate();
            specialsUsers = findNewlyActiveUsersSince(sinceDate);
        }
        return diagnosis(specialsUsers);
    }

    private List<DiagnosisResult> diagnosis(List<String> specialsUsers) {
        int userCount = specialsUsers.size();
        logger.info("diagnosis user bill for {} users", userCount);
        return IntStream.range(0, specialsUsers.size())
                .mapToObj(i -> this.diagnosisUser(i, userCount, specialsUsers.get(i)))
                .collect(Collectors.toList());
    }

    private List<String> findNewlyActiveUsersSince(LocalDate beginLocalDate) {
        Date endDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.ofHours(8)));
        if (beginLocalDate != null) {
            Date beginDate = Date.from(beginLocalDate.atStartOfDay().toInstant(ZoneOffset.ofHours(8)));
            logger.info("search users {} - {}", beginDate, endDate);
            return userBillExtMapper.findLoginNameByTime(beginDate, endDate);
        } else {
            logger.info("search users before {}", endDate);
            return userBillExtMapper.findLoginNameUntil(endDate);
        }
    }

    private DiagnosisResult diagnosisUser(int idx, int userCount, String loginName) {
        logger.info("[{}/{}] diagnosis user bill for {}", idx, userCount, loginName);
        DiagnosisContext diagnosisContext = new DiagnosisContext(loginName);
        if (checkUserBalance(loginName, diagnosisContext)) {
            List<UserBillModel> userBillModelList = userBillMapper.findByLoginName(loginName);
            userBillModelList.stream()
                    .filter(bill -> Arrays.binarySearch(sortedKnownBadBills, bill.getId()) < 0)
                    .filter(bill -> diagnosisMap.containsKey(bill.getBusinessType()) && bill.getOrderId() != null)
                    .forEach(bill -> {
                        UserBillBusinessDiagnosis diagnosis = diagnosisMap.get(bill.getBusinessType());
                        try {
                            diagnosis.diagnosis(bill, diagnosisContext);
                        } catch (Exception e) {
                            diagnosisContext.addProblem("diagnosis failed: " + e.getMessage());
                            logger.error("diagnosis failed", e);
                        }
                    });
        }
        return diagnosisContext.getResult();
    }

    private boolean checkUserBalance(String loginName, DiagnosisContext diagnosisContext) {
        BankAccountModel account = bankAccountMapper.findByLoginName(loginName);
        Map<String, String> balanceMap = payWrapperClient.getUserBalance(loginName);
        if (balanceMap == null) {
            diagnosisContext.addProblem(String.format("%s 用户对账服务连接不上。", loginName));
            logger.error("{} 用户对账服务连接不上。", loginName);
            return false;
        }
        long umpBalance = Long.parseLong(balanceMap.get("balance"));
        if (account.getBalance() != umpBalance) {
            diagnosisContext.addProblem(String.format("%s: 用户对账失败,余额-account:%s,ump:%s", loginName,
                    String.valueOf(account.getBalance()),
                    String.valueOf(umpBalance)));
            return false;
        }
        return true;

    }

    private List<String> extraDiagnosisUsers(String[] args) {
        String argumentPrefix = "--loginName=";
        Optional<String> loginNameArgument = Arrays.stream(args)
                .filter(arg -> arg.startsWith(argumentPrefix) && arg.length() > argumentPrefix.length())
                .findAny();
        List<String> userNameList = new ArrayList<>();
        if (loginNameArgument.isPresent()) {
            String loginNames = loginNameArgument.get().substring(argumentPrefix.length());
            if ("all".equalsIgnoreCase(loginNames)) {
                return findAllLoginName();
            }
            Collections.addAll(userNameList, loginNames.split(","));
        }
        return userNameList;
    }

    private List<String> findAllLoginName() {
        return userBillExtMapper.findAllLoginName();
    }
}

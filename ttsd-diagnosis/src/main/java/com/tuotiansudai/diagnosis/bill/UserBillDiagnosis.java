package com.tuotiansudai.diagnosis.bill;

import com.tuotiansudai.diagnosis.bill.diagnoses.InvestCouponFeeDiagnosis;
import com.tuotiansudai.diagnosis.repository.UserBillExtMapper;
import com.tuotiansudai.diagnosis.support.Diagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.DiagnosisResult;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
class UserBillDiagnosis implements Diagnosis {
    private static Logger logger = LoggerFactory.getLogger(UserBillDiagnosis.class);

    private final UserBillMapper userBillMapper;
    private final UserBillExtMapper userBillExtMapper;

    private final Map<UserBillBusinessType, UserBillBusinessDiagnosis> diagnosisMap;

    @Autowired
    public UserBillDiagnosis(UserBillMapper userBillMapper, Set<UserBillBusinessDiagnosis> diagnoses, UserBillExtMapper userBillExtMapper) {
        this.userBillMapper = userBillMapper;
        this.userBillExtMapper = userBillExtMapper;
        this.diagnosisMap = diagnoses.stream()
                .filter(d -> !(d instanceof InvestCouponFeeDiagnosis))
                .collect(Collectors.toMap(
                        UserBillBusinessDiagnosis::getSupportedBusinessType,
                        d -> d));
    }

    @Override
    public List<DiagnosisResult> diagnosis(String[] args) {
        List<String> specialsUsers;
        if (args.length == 0) {
            specialsUsers = findYesterdayActiveUsers();
        } else {
            specialsUsers = extraDiagnosisUsers(args);
        }
        int userCount = specialsUsers.size();
        logger.info("diagnosis user bill for {} users", userCount);
        return IntStream.range(0, specialsUsers.size())
                .mapToObj(i -> this.diagnosisUser(i, userCount, specialsUsers.get(i)))
                .collect(Collectors.toList());
    }

    private List<String> findYesterdayActiveUsers() {
        Date beginDate = Date.from(LocalDate.now().minusDays(1).atStartOfDay().toInstant(ZoneOffset.ofHours(8)));
        Date endDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.ofHours(8)));
        logger.info("search users {} - {}", beginDate, endDate);
        return userBillExtMapper.findLoginNameByTime(beginDate, endDate);
    }

    private DiagnosisResult diagnosisUser(int idx, int userCount, String loginName) {
        logger.info("[{}/{}] diagnosis user bill for {}", idx, userCount, loginName);
        List<UserBillModel> userBillModelList = userBillMapper.findByLoginName(loginName);
        DiagnosisContext diagnosisContext = new DiagnosisContext(loginName);
        userBillModelList.stream()
                .filter(bill -> diagnosisMap.containsKey(bill.getBusinessType()) && bill.getOrderId() != null)
                .forEach(bill -> diagnosisMap.get(bill.getBusinessType()).diagnosis(bill, diagnosisContext));
        return diagnosisContext.getResult();
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

package com.tuotiansudai.diagnosis.bill;

import com.tuotiansudai.diagnosis.bill.diagnoses.InvestCouponFeeDiagnosis;
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

import java.util.*;
import java.util.stream.Collectors;

@Component
class UserBillDiagnosis implements Diagnosis {
    private static Logger logger = LoggerFactory.getLogger(UserBillDiagnosis.class);

    private final UserBillMapper userBillMapper;

    private final Map<UserBillBusinessType, UserBillBusinessDiagnosis> diagnosisMap;

    @Autowired
    public UserBillDiagnosis(UserBillMapper userBillMapper, Set<UserBillBusinessDiagnosis> diagnoses) {
        this.userBillMapper = userBillMapper;
        diagnosisMap = diagnoses.stream()
                .filter(d -> !(d instanceof InvestCouponFeeDiagnosis))
                .collect(Collectors.toMap(
                        UserBillBusinessDiagnosis::getSupportedBusinessType,
                        d -> d));
    }

    @Override
    public List<DiagnosisResult> diagnosis(String[] args) {
        String[] specialsUsers = extraDiagnosisUsers(args);
        String allUser = String.join(",", Arrays.asList(specialsUsers));
        logger.info("diagnosis user bill for {} users [{}]", specialsUsers.length, allUser);
        return Arrays.stream(specialsUsers)
                .map(this::diagnosisUser)
                .collect(Collectors.toList());
    }

    private DiagnosisResult diagnosisUser(String loginName) {
        logger.info("diagnosis user bill for {}", loginName);
        List<UserBillModel> userBillModelList = userBillMapper.findByLoginName(loginName);
        DiagnosisContext diagnosisContext = new DiagnosisContext(loginName);
        userBillModelList.stream()
                .filter(bill -> diagnosisMap.containsKey(bill.getBusinessType()) && bill.getOrderId() != null)
                .forEach(bill -> diagnosisMap.get(bill.getBusinessType()).diagnosis(bill, diagnosisContext));
        return diagnosisContext.getResult();
    }

    private String[] extraDiagnosisUsers(String[] args) {
        String argumentPrefix = "--loginName:";
        Optional<String> loginNameArgs = Arrays.stream(args)
                .filter(arg -> arg.startsWith(argumentPrefix) && arg.length() > argumentPrefix.length())
                .findAny();
        return loginNameArgs.isPresent() ?
                loginNameArgs.get().substring(argumentPrefix.length()).split(",")
                : new String[]{};
    }
}

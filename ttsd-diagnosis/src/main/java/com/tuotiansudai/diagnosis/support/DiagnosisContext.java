package com.tuotiansudai.diagnosis.support;

import com.tuotiansudai.diagnosis.support.exception.DuplicateObjectException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DiagnosisContext {
    private final String loginName;
    private final Map<String, Long> tracedObject;
    private List<String> problems;

    public DiagnosisContext(String loginName) {
        this.loginName = loginName;
        this.tracedObject = new HashMap<>();
        this.problems = new LinkedList<>();
    }

    public void addTracedObject(long userBillId, String tracedObjectId) {
        if (tracedObject.containsKey(tracedObjectId)) {
            throw new DuplicateObjectException("traced object [{0}] for UserBill[{1}] has been already traced by UserBill[{2}]",
                    tracedObjectId, userBillId, tracedObject.get(tracedObjectId));
        }
        tracedObject.put(tracedObjectId, userBillId);
    }

    public boolean hasAlreadyTraced(String tracedObjectId) {
        return tracedObject.containsKey(tracedObjectId);
    }

    public long getUserBillId(String tracedObjectId) {
        return tracedObject.get(tracedObjectId);
    }

    public void addProblem(String problem) {
        problems.add(problem);
    }

    public DiagnosisResult getResult() {
        return problems.size() == 0 ? DiagnosisResult.Fine :
                new DiagnosisResult(DiagnosisResultStatus.Abnormal, problems);
    }
}

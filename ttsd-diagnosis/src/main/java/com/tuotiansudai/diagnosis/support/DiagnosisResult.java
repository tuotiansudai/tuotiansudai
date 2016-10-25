package com.tuotiansudai.diagnosis.support;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisResult {

    private final String loginName;
    private final DiagnosisResultStatus status;
    private final List<String> problems;

    public DiagnosisResult(String loginName, DiagnosisResultStatus status, List<String> problems) {
        this.loginName = loginName;
        this.status = status;
        this.problems = problems;
    }

    public static DiagnosisResult Fine(String loginName) {
        return new DiagnosisResult(loginName, DiagnosisResultStatus.Fine, new ArrayList<>());
    }

    public static DiagnosisResult Abnormal(String loginName, List<String> problems) {
        if (problems == null) {
            problems = new ArrayList<>();
        }
        return new DiagnosisResult(loginName, DiagnosisResultStatus.Abnormal, problems);
    }

    public String getLoginName() {
        return loginName;
    }

    public DiagnosisResultStatus getStatus() {
        return status;
    }

    public List<String> getProblems() {
        return problems;
    }

}

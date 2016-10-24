package com.tuotiansudai.diagnosis.support;

import java.util.List;

public class DiagnosisResult {
    public static final DiagnosisResult Fine = new DiagnosisResult(DiagnosisResultStatus.Fine, null);

    private DiagnosisResultStatus status;
    private List<String> problems;

    public DiagnosisResult(DiagnosisResultStatus status, List<String> problems) {
        this.status = status;
        this.problems = problems;
    }

    public DiagnosisResultStatus getStatus() {
        return status;
    }

    public void setStatus(DiagnosisResultStatus status) {
        this.status = status;
    }

    public List<String> getProblems() {
        return problems;
    }

    public void setProblems(List<String> problems) {
        this.problems = problems;
    }
}

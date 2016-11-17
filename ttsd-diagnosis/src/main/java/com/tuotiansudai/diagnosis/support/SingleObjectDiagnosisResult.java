package com.tuotiansudai.diagnosis.support;

public class SingleObjectDiagnosisResult {
    private boolean passed;
    private String problem;
    public static final SingleObjectDiagnosisResult Passed = new SingleObjectDiagnosisResult(true, null);

    public static SingleObjectDiagnosisResult Abnormal(String problem) {
        return new SingleObjectDiagnosisResult(false, problem);
    }

    public SingleObjectDiagnosisResult(boolean passed, String problem) {
        this.passed = passed;
        this.problem = problem;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getProblem() {
        return problem;
    }
}

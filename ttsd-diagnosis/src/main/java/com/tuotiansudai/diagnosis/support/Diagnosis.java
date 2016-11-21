package com.tuotiansudai.diagnosis.support;

import java.time.LocalDateTime;
import java.util.List;

public interface Diagnosis {
    List<DiagnosisResult> diagnosis(LocalDateTime lastFireTime, String[] args);
}

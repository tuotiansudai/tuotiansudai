package com.tuotiansudai.diagnosis;

import com.tuotiansudai.diagnosis.support.Diagnosis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
class DiagnosisApplication {

    private static Logger logger = LoggerFactory.getLogger(DiagnosisApplication.class);

    private final List<Diagnosis> diagnoses;

    public DiagnosisApplication(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    void start(String[] args) {
        logger.info("diagnosis begin, {} diagnoses will be executed", this.diagnoses.size());
        IntStream.of(this.diagnoses.size()).forEach(idx -> {
                    Diagnosis diagnosis = diagnoses.get(idx - 1);
                    logger.info("{}. {} begin", idx, diagnosis.getClass().getName());
                    diagnosis.diagnosis(args);
                    logger.info("{}. {} end", idx, diagnosis.getClass().getName());
                }
        );
    }
}

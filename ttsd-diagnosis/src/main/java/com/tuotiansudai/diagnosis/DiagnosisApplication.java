package com.tuotiansudai.diagnosis;

import com.tuotiansudai.diagnosis.support.Diagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
class DiagnosisApplication {

    private static Logger logger = LoggerFactory.getLogger(DiagnosisApplication.class);

    private final List<Diagnosis> diagnoses;

    @Autowired
    public DiagnosisApplication(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    void start(String[] args) {
        logger.info("diagnosis begin, {} diagnoses will be executed", this.diagnoses.size());
        List<List<DiagnosisResult>> totalResult = IntStream.of(this.diagnoses.size())
                .mapToObj(idx -> {
                    Diagnosis diagnosis = diagnoses.get(idx - 1);
                    logger.info("{}. {} begin", idx, diagnosis.getClass().getName());
                    List<DiagnosisResult> results = diagnosis.diagnosis(args);
                    logger.info("{}. {} end", idx, diagnosis.getClass().getName());
                    return results;
                }).collect(Collectors.toList());

        totalResult.forEach(resultList -> {
            logger.info("");
            logger.info("");
            logger.info("  Diagnosis Report: ");
            logger.info("");
            resultList.forEach(result -> {
                logger.info("    User {} have {} problems", result.getLoginName(), result.getProblems().size());
                result.getProblems().forEach(p -> logger.info("        {}", p));
            });
            logger.info("");
            logger.info("  Diagnosis complete!");
            logger.info("");
            logger.info("");
        });
    }
}

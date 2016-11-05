package com.tuotiansudai.diagnosis;

import com.tuotiansudai.diagnosis.support.Diagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
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
        if (args.length > 0) {
            runOnce(args, null);
        } else {
            scheduleDiagnosis();
        }
    }

    private void runOnce(String[] args, Consumer<List<List<DiagnosisResult>>> consumer) {
        logger.info("diagnosis begin, {} diagnoses will be executed", this.diagnoses.size());
        List<List<DiagnosisResult>> totalResult = IntStream.range(0, this.diagnoses.size())
                .mapToObj(idx -> {
                    Diagnosis diagnosis = diagnoses.get(idx);
                    logger.info("{}. {} begin", idx, diagnosis.getClass().getName());
                    List<DiagnosisResult> results = diagnosis.diagnosis(args);
                    logger.info("{}. {} end", idx, diagnosis.getClass().getName());
                    return results;
                }).collect(Collectors.toList());
        printReport(totalResult);
        if (consumer != null) {
            consumer.accept(totalResult);
        }
    }

    private void scheduleDiagnosis() {
        int schedule_hour = 0;
        int schedule_minutes = 35;
        LocalDateTime fireTime = LocalDate.now().atStartOfDay()
                .withHour(schedule_hour).withMinute(schedule_minutes);
        logger.info("diagnosis scheduled at {}", fireTime);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnce(new String[]{}, r -> smsReport(r));
            }
        }, Date.from(fireTime.toInstant(ZoneOffset.ofHours(8))), 1000 * 60 * 60 * 24);
    }

    private void printReport(List<List<DiagnosisResult>> totalResult) {
        totalResult.forEach(resultList -> {
            logger.info("");
            logger.info("");
            logger.info("  Diagnosis Report: ");
            logger.info("");
            resultList.stream()
                    .filter(result -> result.getProblems().size() > 0)
                    .forEach(result -> {
                        logger.info("    User {} have {} problems", result.getLoginName(), result.getProblems().size());
                        result.getProblems().forEach(p -> logger.info("        {}", p));
                    });
            logger.info("");
            logger.info("  Diagnosis complete!");
            logger.info("");
            logger.info("");
        });
    }

    private void smsReport(List<List<DiagnosisResult>> totalResult) {
        logger.info("send sms");
    }
}

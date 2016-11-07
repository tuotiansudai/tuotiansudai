package com.tuotiansudai.diagnosis;

import com.tuotiansudai.diagnosis.config.DiagnosisConfig;
import com.tuotiansudai.diagnosis.support.Diagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisResult;
import com.tuotiansudai.dto.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
class DiagnosisApplication {

    private static Logger logger = LoggerFactory.getLogger(DiagnosisApplication.class);

    private final List<Diagnosis> diagnoses;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private DiagnosisConfig diagnosisConfig;

    @Value("${common.environment}")
    private Environment environment;

    @Autowired
    public DiagnosisApplication(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    void start(String[] args) {
        if (args.length > 0) {
            String description = "diagnosis with args: " + String.join(" ", Arrays.asList(args));
            runOnce(args, r -> printReport(description, r));
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
        if (consumer != null) {
            consumer.accept(totalResult);
        }
    }

    private void scheduleDiagnosis() {
        LocalDateTime fireTime = LocalDate.now().atStartOfDay()
                .withHour(diagnosisConfig.getSchedule().getHour())
                .withMinute(diagnosisConfig.getSchedule().getMinute());
        logger.info("diagnosis scheduled at {}", fireTime);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String description = "diagnosis bills at " + LocalDate.now().minusDays(1).toString();
                logger.info(description);
                runOnce(new String[]{}, r -> mailReport(description, r));
            }
        }, Date.from(fireTime.toInstant(ZoneOffset.ofHours(8))), 1000 * 60 * 60 * 24);
    }

    private void printReport(String description, List<List<DiagnosisResult>> totalResult) {
        logger.info(buildDiagnosisReport(description, totalResult));
    }

    private void mailReport(String description, List<List<DiagnosisResult>> totalResult) {
        String reportMessage = buildDiagnosisReport(description, totalResult);
        logger.info(reportMessage);
        if (Arrays.asList(Environment.PRODUCTION, Environment.QA).contains(environment)) {
            try {
                sendReportMail(reportMessage);
            } catch (Exception e) {
                logger.error("send report mail fail", e);
            }
        }
    }

    private String buildDiagnosisReport(String description, List<List<DiagnosisResult>> totalResult) {
        List<String> reportLines = new LinkedList<>();
        totalResult.forEach(resultList -> {
            reportLines.add("");
            reportLines.add("");
            reportLines.add("  Diagnosis report ");
            reportLines.add("    " + description);
            reportLines.add("");
            long problemCount = resultList.stream()
                    .filter(result -> result.getProblems().size() > 0)
                    .map(result -> {
                        reportLines.add(String.format("    User %s have %d problems: ", result.getLoginName(), result.getProblems().size()));
                        reportLines.addAll(result.getProblems().stream().map(p -> "        " + p).collect(Collectors.toList()));
                        return result;
                    })
                    .count();
            if (problemCount == 0) {
                reportLines.add("    Congratulations, all bills are correct!");
            }
            reportLines.add("");
            reportLines.add("  Diagnosis complete.");
            reportLines.add("");
        });
        return String.join("\n", reportLines);
    }

    private void sendReportMail(String reportMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(diagnosisConfig.getReport().getNotifyMailFrom());
        if (Environment.isProduction(environment)) {
            message.setTo(diagnosisConfig.getReport().getNotifyMailAddress());
        } else {
            message.setTo(diagnosisConfig.getReport().getNotifyMailAddressQA());
        }
        message.setSubject("diagnosis report");
        message.setText(reportMessage.replace("\n", "\r\n"));
        mailSender.send(message);
    }
}

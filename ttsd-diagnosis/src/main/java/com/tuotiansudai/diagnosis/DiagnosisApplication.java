package com.tuotiansudai.diagnosis;

import com.tuotiansudai.diagnosis.config.DiagnosisConfig;
import com.tuotiansudai.diagnosis.support.Diagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisResult;
import com.tuotiansudai.dto.Environment;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
class DiagnosisApplication {

    private static Logger logger = LoggerFactory.getLogger(DiagnosisApplication.class);
    private static final String LAST_DIAGNOSIS_TIME_KEY = "diagnosis:last:execution:time";

    private final List<Diagnosis> diagnoses;

    @Autowired
    private Jedis jedis;

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
            runOnce(args, null, r -> printReport(description, r));
        } else {
            scheduleDiagnosis();
        }
    }

    private void runOnce(String[] args, LocalDateTime lastFireTime, Consumer<List<List<DiagnosisResult>>> consumer) {
        logger.info("diagnosis begin, {} diagnoses will be executed", this.diagnoses.size());
        List<List<DiagnosisResult>> totalResult = IntStream.range(0, this.diagnoses.size())
                .mapToObj(idx -> {
                    Diagnosis diagnosis = diagnoses.get(idx);
                    logger.info("{}. {} begin", idx, diagnosis.getClass().getName());
                    List<DiagnosisResult> results = diagnosis.diagnosis(lastFireTime, args);
                    logger.info("{}. {} end", idx, diagnosis.getClass().getName());
                    return results;
                }).collect(Collectors.toList());
        if (consumer != null) {
            consumer.accept(totalResult);
        }
    }

    private void scheduleDiagnosis() {
        LocalDateTime lastFireTime = getLastFireTime();
        LocalDateTime nextFireTime = getNextFireTime(lastFireTime);
        logger.info("diagnosis scheduled at {}", nextFireTime);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime fireTime = LocalDateTime.now();
                String description = "diagnosis bills at " + fireTime.toString();
                logger.info(description);
                runOnce(new String[]{}, getLastFireTime(), r -> mailReport(description, r));
                logFireTime(fireTime);
            }
        }, Date.from(nextFireTime.toInstant(ZoneOffset.ofHours(8))), 1000 * 60 * 60 * 24);
    }

    private LocalDateTime getLastFireTime() {
        String lastFireTimeString = jedis.get(LAST_DIAGNOSIS_TIME_KEY);
        if (lastFireTimeString != null) {
            try {
                return LocalDateTime.parse(lastFireTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private LocalDateTime getNextFireTime(LocalDateTime lastFireTime) {
        LocalDateTime fireTime = LocalDate.now().atStartOfDay()
                .withHour(diagnosisConfig.getSchedule().getHour())
                .withMinute(diagnosisConfig.getSchedule().getMinute());

        if (lastFireTime != null && fireTime.toLocalDate().equals(lastFireTime.toLocalDate())) {
            return fireTime.plusDays(1);
        }

        return fireTime;
    }

    private void logFireTime(LocalDateTime fireTime) {
        jedis.set(LAST_DIAGNOSIS_TIME_KEY, fireTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    private void printReport(String description, List<List<DiagnosisResult>> totalResult) {
        reportDiagnosis(description, totalResult, (count, report) -> logger.info(report));
    }

    private void mailReport(String description, List<List<DiagnosisResult>> totalResult) {
        reportDiagnosis(description, totalResult, (count, report) -> {
            logger.info(report);
            if (Arrays.asList(Environment.PRODUCTION, Environment.QA1,Environment.QA2,Environment.QA3,Environment.QA4,Environment.QA5).contains(environment)) {
                try {
                    logger.info("sending report mail");
                    if (count > 0) {
                        sendReportMail(report);
                    } else {
                        sendReportMail(report, diagnosisConfig.getReport().getNotifyMailAddressManager());
                    }
                    logger.info("send report mail done!");
                } catch (Exception e) {
                    logger.error("send report mail fail", e);
                }
            }
        });
    }

    private void reportDiagnosis(String description, List<List<DiagnosisResult>> totalResult, BiConsumer<Long, String> consumer) {
        List<String> reportLines = new LinkedList<>();
        long problemTotalCount = totalResult.stream()
                .map(resultList -> {
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
                    return problemCount;
                })
                .reduce(0L, (aLong, aLong2) -> aLong + aLong2);

        consumer.accept(problemTotalCount, String.join("\n", reportLines));
    }

    private void sendReportMail(String reportMessage) {
        sendReportMail(reportMessage, null);
    }

    private void sendReportMail(String reportMessage, String[] mailTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(diagnosisConfig.getReport().getNotifyMailFrom());
        if (ArrayUtils.isNotEmpty(mailTo)) {
            message.setTo(mailTo);
        } else {
            if (Environment.isProduction(environment)) {
                message.setTo(diagnosisConfig.getReport().getNotifyMailAddress());
            } else {
                message.setTo(diagnosisConfig.getReport().getNotifyMailAddressQa());
            }
        }
        message.setSubject(String.format("diagnosis report [%s]", environment));
        message.setText(reportMessage.replace("\n", "\r\n"));
        mailSender.send(message);
    }
}

package com.tuotiansudai.paywrapper.validation;

import com.tuotiansudai.dto.Environment;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Component
public class ValidationReportSender {

    private final JavaMailSender javaMailSender;

    @Value("${common.environment}")
    private Environment environment;

    @Autowired
    public ValidationReportSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(String emailBody) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");
            message.setSubject(MessageFormat.format("{0}{1}联动优势交易状态统计", environment, new DateTime().toString("yyyy-MM-dd")));
            message.setFrom("no-reply@tuotiansudai.com");
            message.setTo("dev@tuotiansudai.com");
            message.setSentDate(new Date());
            message.setText(emailBody, true);
        };

        javaMailSender.send(preparator);
    }
}

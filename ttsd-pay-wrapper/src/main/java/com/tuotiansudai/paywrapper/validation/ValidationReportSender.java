package com.tuotiansudai.paywrapper.validation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.List;

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
            message.setTo(Maps.newHashMap(ImmutableMap.<Environment, List<String>>builder()
                    .put(Environment.PRODUCTION, Lists.newArrayList("dev@tuotiansudai.com"))
                    .put(Environment.QA1, Lists.newArrayList("zhangfengxiao@tuotiansudai.com", "zhangkunlong@tuotiansudai.com"))
                    .put(Environment.QA2, Lists.newArrayList("zhangfengxiao@tuotiansudai.com", "zhangkunlong@tuotiansudai.com"))
                    .put(Environment.QA3, Lists.newArrayList("zhangfengxiao@tuotiansudai.com", "zhangkunlong@tuotiansudai.com"))
                    .put(Environment.QA4, Lists.newArrayList("zhangfengxiao@tuotiansudai.com", "zhangkunlong@tuotiansudai.com"))
                    .put(Environment.QA5, Lists.newArrayList("zhangfengxiao@tuotiansudai.com", "zhangkunlong@tuotiansudai.com"))
                    .put(Environment.DEV, Lists.newArrayList("gaoxiduan@tuotiansudai.com"))
                    .build()).get(environment).toArray(new String[0]));
            message.setSentDate(new Date());
            message.setText(emailBody, true);
        };

        javaMailSender.send(preparator);
    }
}

package com.tuotiansudai.paywrapper.validation;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Date;

@Component
public class ValidationReportSender {

    private String template;

    private final JavaMailSender javaMailSender;

    @Autowired
    public ValidationReportSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache compile = mf.compile("validation-report.html");
        StringWriter writer = new StringWriter();
//        compile.execute(writer, );
        writer.toString();
    }

    public void send() {
//        MimeMessagePreparator preparator = mimeMessage -> {
//            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//            message.setSubject("New suggested podcast");
//            message.setFrom("no-reply@tuotiansudai.com");
//            message.setTo("gaoxiduan@tuotiansuai.com");
//            message.setSentDate(new Date());
//            message.setText("1", true);
//        };
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("New suggested podcast");
        simpleMailMessage.setFrom("no-reply@tuotiansudai.com");
        simpleMailMessage.setTo("gaoxiduan@tuotiansuai.com");
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setText("1");

        javaMailSender.send(simpleMailMessage);
    }

    public String getTemplate() {
        return template;
    }
}

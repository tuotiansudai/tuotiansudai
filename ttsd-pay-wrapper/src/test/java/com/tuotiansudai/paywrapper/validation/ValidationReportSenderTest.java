package com.tuotiansudai.paywrapper.validation;


import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional(value = "payTransactionManager")
public class ValidationReportSenderTest {

    @Autowired
    private ValidationReportSender validationReportSender;

    @Autowired
    private InvestRepayDailyValidation investRepayDailyValidation;

    @Test
    public void name() throws Exception {
        ValidationReport report = investRepayDailyValidation.validate();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache compile = mf.compile("validation-report.html");
        StringWriter writer = new StringWriter();
        compile.execute(writer, Maps.newHashMap(ImmutableMap.<String, ValidationReport>builder()
                .put("investRepay", report)
                .build()));
        System.out.println(writer.toString());

//        validationReportSender.send();
    }
}

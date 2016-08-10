package com.tuotiansudai.web.ask.freemarker.directive;


import com.tuotiansudai.ask.repository.mapper.AnswerMapper;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class MyAnswersDirective implements TemplateDirectiveModel {

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (!params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        if (body != null) {
            body.render(new MyAnswersWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }
    }

    private class MyAnswersWriter extends Writer {

        private final Writer out;

        MyAnswersWriter(Writer out) {
            this.out = out;
        }

        @Override
        public void write(char[] chars, int off, int len) throws IOException {
            String loginName = new String(chars, off, len);
            long myAnswers = answerMapper.countByLoginName(loginName);
            out.write(String.valueOf(myAnswers));
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }

        @Override
        public void close() throws IOException {
            out.close();
        }
    }
}

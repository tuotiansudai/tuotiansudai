package com.tuotiansudai.web.util;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class PercentDirective implements TemplateDirectiveModel{

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (!params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        if (body != null) {
            body.render(new PercentFilterWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }
    }

    private static class PercentFilterWriter extends Writer {

        private final Writer out;

        PercentFilterWriter (Writer out) {
            this.out = out;
        }

        @Override
        public void write(char[] buf, int off, int len) throws IOException {

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

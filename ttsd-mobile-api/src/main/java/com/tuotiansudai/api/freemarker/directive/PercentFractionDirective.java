package com.tuotiansudai.api.freemarker.directive;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class PercentFractionDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (!params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        if (body != null) {
            body.render(new PercentFractionFilterWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }
    }

    private static class PercentFractionFilterWriter extends Writer {

        private final Writer out;

        PercentFractionFilterWriter(Writer out) {
            this.out = out;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (new String(cbuf, off, len).contains(".")) {
                String percent = new String(cbuf, off, len).split("\\.")[1].replaceAll("0+?$", "");
                out.write(!percent.equals("") ? "." + percent : percent);
            } else {
                out.write("");
            }
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

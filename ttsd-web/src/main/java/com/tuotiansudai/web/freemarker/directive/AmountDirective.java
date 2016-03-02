package com.tuotiansudai.web.freemarker.directive;


import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Map;

public class AmountDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (!params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        if (body != null) {
            body.render(new AmountFilterWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }
    }

    private static class AmountFilterWriter extends Writer {
        private final Writer out;

        private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

        private static final BigDecimal TEN_THOUSANDS = new BigDecimal(1000000);

        AmountFilterWriter (Writer out) {
            this.out = out;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            BigDecimal amount = new BigDecimal(new String(cbuf, off, len));
            String returnAmount;
            if (amount.compareTo(TEN_THOUSANDS) != -1){
                returnAmount = amount.divide(TEN_THOUSANDS, 2, BigDecimal.ROUND_HALF_UP).toString().replaceAll("0+?$", "").replaceAll("[.]$", "")+" 万";
            } else {
                returnAmount = amount.divide(ONE_HUNDRED, 2, BigDecimal.ROUND_HALF_UP).toString().replaceAll("0+?$", "").replaceAll("[.]$", "");
            }
            out.write(returnAmount);
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

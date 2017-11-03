package com.tuotiansudai.web.freemarker.directive;


import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class UnreadMessageCountDirective implements TemplateDirectiveModel {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMessageService userMessageService;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (!params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        if (body != null) {
            body.render(new UnreadMessageCountWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }
    }

    private class UnreadMessageCountWriter extends Writer {
        private final Writer out;

        UnreadMessageCountWriter(Writer out) {
            this.out = out;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            String mobile = new String(cbuf, off, len);
            long unreadMessageCount = userMessageService.getUnreadMessageCount(userMapper.findByMobile(mobile).getLoginName(), mobile, MessageChannel.WEBSITE);
            out.write(unreadMessageCount > 0 ? String.valueOf(unreadMessageCount) : "zero");
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

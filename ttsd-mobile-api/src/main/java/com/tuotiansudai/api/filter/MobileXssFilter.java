package com.tuotiansudai.api.filter;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.List;

public class MobileXssFilter implements Filter {
    private final static Logger log = Logger.getLogger(MobileXssFilter.class);
    private final static int DEFAULT_BUFFER_SIZE = 1024 * 4;

    FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!isEvilPost(request)) {
            chain.doFilter(request, response);
        }
    }

    private boolean isEvilPost(ServletRequest request) {
        String body;
        try {
            InputStream inputStream = request.getInputStream();
            body = getRequestBody(inputStream);
        } catch (IOException | NullPointerException e) {
            log.error(MessageFormat.format("[MobileXssFilter][isEvilPost] request:{0} exception:{1}", request, e));
            return true;
        }

        body = body.toLowerCase();
        if (containKeyWords(body)) {
            return true;
        }

        return false;
    }

    private String getRequestBody(InputStream inputStream) throws IOException {
        String body = "";
        Reader input = new InputStreamReader(inputStream);
        Writer output = new StringWriter();
        try {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            body = output.toString();
        } finally {
            input.close();
            output.close();
        }

        return body;
    }

    private boolean containKeyWords(String s) {
        List<String> keyWords = Lists.newArrayList("<", ">", "'", "\\(", "\\)", "eval", "alert", "javascript", "script",
                "prompt", "confirm", " src", "&#", "autofocus", "onerror", "onload", "onstart", "href");
        for (String keyWord : keyWords) {
            if (s.contains(keyWord)) {
                log.info(MessageFormat.format("[MobileXssFilter][containKeyWords] requestBody:{0} keyWords:{1}", s, keyWord));
                return true;
            }
        }
        return false;
    }
}

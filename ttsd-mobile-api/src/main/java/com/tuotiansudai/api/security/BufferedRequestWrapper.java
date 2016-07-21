package com.tuotiansudai.api.security;

import org.apache.log4j.Logger;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

    private static Logger logger = Logger.getLogger(BufferedRequestWrapper.class);

    ByteArrayInputStream byteArrayInputStream;
    ByteArrayOutputStream byteArrayOutputStream;
    BufferedServletInputStream bufferedServletInputStream;
    byte[] buffer;

    public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
        super(req);
        // Read InputStream and store its content in a buffer.
        InputStream is = req.getInputStream();
        byteArrayOutputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int letti;
        while ((letti = is.read(buf)) > 0) byteArrayOutputStream.write(buf, 0, letti);
        buffer = byteArrayOutputStream.toByteArray();
    }

    @Override
    public String getParameter(String name) {
        return super.getParameter(name);
    }

    @Override
    public ServletInputStream getInputStream() {
        try {
            // Generate a new InputStream by stored buffer
            byteArrayInputStream = new ByteArrayInputStream(buffer);
            // Instantiate a subclass of ServletInputStream
            // (Only ServletInputStream or subclasses of it are accepted by the servlet engine!)
            bufferedServletInputStream = new BufferedServletInputStream(byteArrayInputStream);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

        return bufferedServletInputStream;
    }

    public String getInputStreamString() throws IOException {
        InputStreamReader input = new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader bf = new BufferedReader(input);

        try {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            bf.close();
            input.close();
        }
    }

}

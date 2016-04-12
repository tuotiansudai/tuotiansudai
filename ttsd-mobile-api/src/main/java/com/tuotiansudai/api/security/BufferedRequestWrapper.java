package com.tuotiansudai.api.security;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

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

    public ServletInputStream getInputStream() {
        try {
            // Generate a new InputStream by stored buffer
            byteArrayInputStream = new ByteArrayInputStream(buffer);
            // Instantiate a subclass of ServletInputStream
            // (Only ServletInputStream or subclasses of it are accepted by the servlet engine!)
            bufferedServletInputStream = new BufferedServletInputStream(byteArrayInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return bufferedServletInputStream;
        }
    }

}

package com.tuotiansudai.api.security;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

/*
Subclass of ServletInputStream needed by the servlet engine.
All inputStream methods are wrapped and are delegated to 
the ByteArrayInputStream (obtained as constructor parameter)!
*/
public class BufferedServletInputStream extends ServletInputStream {

    ByteArrayInputStream byteArrayInputStream;

    public BufferedServletInputStream(ByteArrayInputStream byteArrayInputStream) {
        this.byteArrayInputStream = byteArrayInputStream;
    }

    @Override
    public int available() {
        return byteArrayInputStream.available();
    }

    @Override
    public int read() {
        return byteArrayInputStream.read();
    }

    @Override
    public int read(byte[] buff, int off, int len) {
        return byteArrayInputStream.read(buff, off, len);
    }

    @Override
    public boolean isFinished() {
        return byteArrayInputStream.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
    }
}
package com.ttsd.api.security;

import java.io.*;
import javax.servlet.ServletInputStream;

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

    public int available() {
        return byteArrayInputStream.available();
    }

    public int read() {
        return byteArrayInputStream.read();
    }

    public int read(byte[] buff, int off, int len) {
        return byteArrayInputStream.read(buff, off, len);
    }

}
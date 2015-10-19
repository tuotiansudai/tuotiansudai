package com.ttsd.api.security;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class LoginResponseWrapper extends HttpServletResponseWrapper {

    private int httpStatus;

    public LoginResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int sc) throws IOException {
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        super.sendError(sc, msg);
        httpStatus = sc;
    }


    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        httpStatus = sc;
    }

    public int getStatus() {
        return httpStatus;
    }
}

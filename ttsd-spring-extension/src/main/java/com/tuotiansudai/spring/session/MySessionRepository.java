package com.tuotiansudai.spring.session;

import org.springframework.session.Session;

public interface MySessionRepository<S extends Session> {

    S createSession();

    S createSession(String id);

    void save(S session);

    S getSession(String id);

    void delete(String id);
}

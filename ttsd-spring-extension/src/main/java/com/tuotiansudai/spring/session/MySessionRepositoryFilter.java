package com.tuotiansudai.spring.session;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.OnCommittedResponseWrapper;
import org.springframework.session.ExpiringSession;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;
import org.springframework.session.web.http.SessionRepositoryFilter;


@Order(SessionRepositoryFilter.DEFAULT_ORDER)
public class MySessionRepositoryFilter<S extends ExpiringSession> extends OncePerRequestFilter {
    private static final String SESSION_LOGGER_NAME = MySessionRepositoryFilter.class.getName().concat(".SESSION_LOGGER");

    private static final Log SESSION_LOGGER = LogFactory.getLog(SESSION_LOGGER_NAME);

    /**
     * The session repository request attribute name.
     */
    public static final String SESSION_REPOSITORY_ATTR = SessionRepository.class
            .getName();

    /**
     * Invalid session id (not backed by the session repository) request attribute name.
     */
    public static final String INVALID_SESSION_ID_ATTR = SESSION_REPOSITORY_ATTR
            + ".invalidSessionId";

    private final MySessionRepository<S> sessionRepository;

    private ServletContext servletContext;

    private MultiHttpSessionStrategy httpSessionStrategy = new CookieHttpSessionStrategy();

    private RedisWrapperClient redisWrapperClient;

    /**
     * Creates a new instance.
     *
     * @param sessionRepository the <code>SessionRepository</code> to use. Cannot be null.
     */
    public MySessionRepositoryFilter(MySessionRepository<S> sessionRepository) {
        if (sessionRepository == null) {
            throw new IllegalArgumentException("sessionRepository cannot be null");
        }
        this.sessionRepository = sessionRepository;
    }


    public void setHttpSessionStrategy(HttpSessionStrategy httpSessionStrategy) {
        if (httpSessionStrategy == null) {
            throw new IllegalArgumentException("httpSessionStrategy cannot be null");
        }
        this.httpSessionStrategy = new MultiHttpSessionStrategyAdapter(
                httpSessionStrategy);
    }

    public void setRedisWrapperClient(RedisWrapperClient redisWrapperClient) {
        this.redisWrapperClient = redisWrapperClient;
    }

    /**
     * Sets the {@link MultiHttpSessionStrategy} to be used. The default is a
     * {@link CookieHttpSessionStrategy}.
     *
     * @param httpSessionStrategy the {@link MultiHttpSessionStrategy} to use. Cannot be
     * null.
     */
    public void setHttpSessionStrategy(MultiHttpSessionStrategy httpSessionStrategy) {
        if (httpSessionStrategy == null) {
            throw new IllegalArgumentException("httpSessionStrategy cannot be null");
        }
        this.httpSessionStrategy = httpSessionStrategy;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        request.setAttribute(SESSION_REPOSITORY_ATTR, this.sessionRepository);

        SessionRepositoryRequestWrapper wrappedRequest = new SessionRepositoryRequestWrapper(
                request, response, this.servletContext);
        SessionRepositoryResponseWrapper wrappedResponse = new SessionRepositoryResponseWrapper(
                wrappedRequest, response);

        HttpServletRequest strategyRequest = this.httpSessionStrategy
                .wrapRequest(wrappedRequest, wrappedResponse);
        HttpServletResponse strategyResponse = this.httpSessionStrategy
                .wrapResponse(wrappedRequest, wrappedResponse);

        try {
            filterChain.doFilter(strategyRequest, strategyResponse);
        }
        finally {
            wrappedRequest.commitSession();
        }
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Allows ensuring that the session is saved if the response is committed.
     *
     * @author Rob Winch
     * @since 1.0
     */
    private final class SessionRepositoryResponseWrapper
            extends OnCommittedResponseWrapper {

        private final SessionRepositoryRequestWrapper request;

        /**
         * Create a new {@link SessionRepositoryResponseWrapper}.
         * @param request the request to be wrapped
         * @param response the response to be wrapped
         */
        SessionRepositoryResponseWrapper(SessionRepositoryRequestWrapper request,
                                         HttpServletResponse response) {
            super(response);
            if (request == null) {
                throw new IllegalArgumentException("request cannot be null");
            }
            this.request = request;
        }

        @Override
        protected void onResponseCommitted() {
            this.request.commitSession();
        }
    }

    /**
     * A {@link javax.servlet.http.HttpServletRequest} that retrieves the
     * {@link javax.servlet.http.HttpSession} using a
     * {@link org.springframework.session.SessionRepository}.
     *
     * @author Rob Winch
     * @since 1.0
     */
    private final class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {

        private final String CURRENT_SESSION_ATTR = HttpServletRequestWrapper.class.getName();
        private Boolean requestedSessionIdValid;
        private boolean requestedSessionInvalidated;
        private final HttpServletResponse response;
        private final ServletContext servletContext;

        private SessionRepositoryRequestWrapper(HttpServletRequest request,
                                                HttpServletResponse response, ServletContext servletContext) {
            super(request);
            this.response = response;
            this.servletContext = servletContext;
        }

        /**
         * Uses the HttpSessionStrategy to write the session id to the response and
         * persist the Session.
         */
        private void commitSession() {
            HttpSessionWrapper wrappedSession = getCurrentSession();
            if (wrappedSession == null) {
                if (isInvalidateClientSession()) {
                    MySessionRepositoryFilter.this.httpSessionStrategy
                            .onInvalidateSession(this, this.response);
                }
            }
            else {
                S session = wrappedSession.getSession();
                MySessionRepositoryFilter.this.sessionRepository.save(session);
                if (!isRequestedSessionIdValid() || !session.getId().equals(getRequestedSessionId())) {
                    MySessionRepositoryFilter.this.httpSessionStrategy.onNewSession(session, this, this.response);
                }

                SESSION_LOGGER.info(MessageFormat.format("[commit session] session id = {0}", session.getId()));
            }
        }

        @SuppressWarnings("unchecked")
        private HttpSessionWrapper getCurrentSession() {
            return (HttpSessionWrapper) getAttribute(this.CURRENT_SESSION_ATTR);
        }

        private void setCurrentSession(HttpSessionWrapper currentSession) {
            if (currentSession == null) {
                removeAttribute(this.CURRENT_SESSION_ATTR);
            }
            else {
                setAttribute(this.CURRENT_SESSION_ATTR, currentSession);
            }
        }

        @SuppressWarnings("unused")
        public String changeSessionId() {
            HttpSession session = getSession(false);

            if (session == null) {
                throw new IllegalStateException("Cannot change session ID. There is no session associated with this request.");
            }

            String originalSessionId = session.getId();

            // eagerly get session attributes in case implementation lazily loads them
            Map<String, Object> attrs = new HashMap<>();
            Enumeration<String> iAttrNames = session.getAttributeNames();
            while (iAttrNames.hasMoreElements()) {
                String attrName = iAttrNames.nextElement();
                Object value = session.getAttribute(attrName);

                attrs.put(attrName, value);
            }

            MySessionRepositoryFilter.this.sessionRepository.delete(originalSessionId);
            HttpSessionWrapper original = getCurrentSession();
            setCurrentSession(null);

            if (redisWrapperClient.get(originalSessionId) != null) {
                SESSION_LOGGER.info(MessageFormat.format("[change session id] before change session, original sessionId = {0}, new sessionId = {1}", originalSessionId, redisWrapperClient.get(originalSessionId)));
            }

            HttpSessionWrapper newSession = getSession();
            original.setSession(newSession.getSession());

            newSession.setMaxInactiveInterval(session.getMaxInactiveInterval());
            for (Map.Entry<String, Object> attr : attrs.entrySet()) {
                String attrName = attr.getKey();
                Object attrValue = attr.getValue();
                newSession.setAttribute(attrName, attrValue);
            }

            SESSION_LOGGER.info(MessageFormat.format("[change session id] after change session, original sessionId = {0}, new sessionId = {1}", originalSessionId, newSession.getId()));

            return newSession.getId();
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            if (this.requestedSessionIdValid == null) {
                String sessionId = getRequestedSessionId();
                S session = sessionId == null ? null : getSession(sessionId);
                return isRequestedSessionIdValid(session);
            }

            return this.requestedSessionIdValid;
        }

        private boolean isRequestedSessionIdValid(S session) {
            if (this.requestedSessionIdValid == null) {
                this.requestedSessionIdValid = session != null;
            }
            return this.requestedSessionIdValid;
        }

        private boolean isInvalidateClientSession() {
            return getCurrentSession() == null && this.requestedSessionInvalidated;
        }

        private S getSession(String sessionId) {
            S session = MySessionRepositoryFilter.this.sessionRepository.getSession(sessionId);
            if (session == null) {
                return null;
            }
            session.setLastAccessedTime(System.currentTimeMillis());
            return session;
        }

        @Override
        public HttpSessionWrapper getSession(boolean create) {
            String requestedSessionId = getRequestedSessionId();

            String signInNewSessionId = requestedSessionId != null ? redisWrapperClient.get(requestedSessionId) : null;

            if (signInNewSessionId != null) {
                if (getCurrentSession() != null) {
                    SESSION_LOGGER.debug(MessageFormat.format("current session ({0}), sign in new session id ({1})", signInNewSessionId, getCurrentSession().getId()));
                }
                S session = MySessionRepositoryFilter.this.sessionRepository.createSession(signInNewSessionId);
                redisWrapperClient.del(signInNewSessionId);
                session.setLastAccessedTime(System.currentTimeMillis());
                HttpSessionWrapper currentSession = new HttpSessionWrapper(session, getServletContext());
                setCurrentSession(currentSession);
                return currentSession;
            }

            HttpSessionWrapper currentSession = getCurrentSession();

            if (currentSession != null) {
                return currentSession;
            }

            if (requestedSessionId != null && getAttribute(INVALID_SESSION_ID_ATTR) == null) {
                S session = getSession(requestedSessionId);
                if (session != null) {
                    this.requestedSessionIdValid = true;
                    currentSession = new HttpSessionWrapper(session, getServletContext());
                    currentSession.setNew(false);
                    setCurrentSession(currentSession);
                    SESSION_LOGGER.debug(MessageFormat.format("current session is null, but requestedSessionId = {0}", requestedSessionId));
                    return currentSession;
                } else {
                    // This is an invalid session id. No need to ask again if
                    // request.getSession is invoked for the duration of this request
                    if (SESSION_LOGGER.isDebugEnabled()) {
                        SESSION_LOGGER.debug(
                                "No session found by id: Caching result for getSession(false) for this HttpServletRequest.");
                    }
                    setAttribute(INVALID_SESSION_ID_ATTR, "true");
                }
            }
            if (!create) {
                return null;
            }

            S session = MySessionRepositoryFilter.this.sessionRepository.createSession();
            session.setLastAccessedTime(System.currentTimeMillis());
            currentSession = new HttpSessionWrapper(session, getServletContext());
            setCurrentSession(currentSession);
            return currentSession;
        }

        @Override
        public ServletContext getServletContext() {
            if (this.servletContext != null) {
                return this.servletContext;
            }
            // Servlet 3.0+
            return super.getServletContext();
        }

        @Override
        public HttpSessionWrapper getSession() {
            return getSession(true);
        }

        @Override
        public String getRequestedSessionId() {
            return MySessionRepositoryFilter.this.httpSessionStrategy.getRequestedSessionId(this);
        }

        /**
         * Allows creating an HttpSession from a Session instance.
         *
         * @author Rob Winch
         * @since 1.0
         */
        private final class HttpSessionWrapper extends ExpiringSessionHttpSession<S> {

            HttpSessionWrapper(S session, ServletContext servletContext) {
                super(session, servletContext);
            }

            @Override
            public void invalidate() {
                super.invalidate();
                SessionRepositoryRequestWrapper.this.requestedSessionInvalidated = true;
                setCurrentSession(null);
                MySessionRepositoryFilter.this.sessionRepository.delete(getId());
            }
        }
    }

    /**
     * A delegating implementation of {@link MultiHttpSessionStrategy}.
     */
    static class MultiHttpSessionStrategyAdapter implements MultiHttpSessionStrategy {
        private HttpSessionStrategy delegate;

        /**
         * Create a new {@link MultiHttpSessionStrategyAdapter} instance.
         * @param delegate the delegate HTTP session strategy
         */
        MultiHttpSessionStrategyAdapter(HttpSessionStrategy delegate) {
            this.delegate = delegate;
        }

        public String getRequestedSessionId(HttpServletRequest request) {
            return this.delegate.getRequestedSessionId(request);
        }

        public void onNewSession(Session session, HttpServletRequest request,
                                 HttpServletResponse response) {
            this.delegate.onNewSession(session, request, response);
        }

        public void onInvalidateSession(HttpServletRequest request,
                                        HttpServletResponse response) {
            this.delegate.onInvalidateSession(request, response);
        }

        public HttpServletRequest wrapRequest(HttpServletRequest request,
                                              HttpServletResponse response) {
            return request;
        }

        public HttpServletResponse wrapResponse(HttpServletRequest request,
                                                HttpServletResponse response) {
            return response;
        }
    }
}

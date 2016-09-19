package com.tuotiansudai.spring.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class MyRedisOperationsSessionRepository implements MyFindByIndexNameSessionRepository<MyRedisOperationsSessionRepository.RedisSession>, MessageListener {

    private static final Log logger = LogFactory.getLog(MyRedisOperationsSessionRepository.class);

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    static PrincipalNameResolver PRINCIPAL_NAME_RESOLVER = new PrincipalNameResolver();

    static final String DEFAULT_SPRING_SESSION_REDIS_PREFIX = "spring:session:";

    static final String CREATION_TIME_ATTR = "creationTime";

    static final String MAX_INACTIVE_ATTR = "maxInactiveInterval";

    static final String LAST_ACCESSED_ATTR = "lastAccessedTime";

    static final String SESSION_ATTR_PREFIX = "sessionAttr:";

    private String keyPrefix = DEFAULT_SPRING_SESSION_REDIS_PREFIX;

    private final RedisOperations<Object, Object> sessionRedisOperations;

    private final RedisSessionExpirationPolicy expirationPolicy;

    private ApplicationEventPublisher eventPublisher = new ApplicationEventPublisher() {
        public void publishEvent(ApplicationEvent event) {
        }

        public void publishEvent(Object event) {
        }
    };

    private Integer defaultMaxInactiveInterval;

    private RedisSerializer<Object> defaultSerializer = new JdkSerializationRedisSerializer();

    private RedisFlushMode redisFlushMode = RedisFlushMode.ON_SAVE;

    public MyRedisOperationsSessionRepository(RedisConnectionFactory redisConnectionFactory) {
        this(createDefaultTemplate(redisConnectionFactory));
    }

    public MyRedisOperationsSessionRepository(RedisOperations<Object, Object> sessionRedisOperations) {
        Assert.notNull(sessionRedisOperations, "sessionRedisOperations cannot be null");
        this.sessionRedisOperations = sessionRedisOperations;
        this.expirationPolicy = new RedisSessionExpirationPolicy(sessionRedisOperations, this);
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        Assert.notNull(applicationEventPublisher, "applicationEventPublisher cannot be null");
        this.eventPublisher = applicationEventPublisher;
    }

    public void setDefaultMaxInactiveInterval(int defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    /**
     * Sets the redis flush mode. Default flush mode is {@link RedisFlushMode#ON_SAVE}.
     *
     * @param redisFlushMode the new redis flush mode
     */
    public void setRedisFlushMode(RedisFlushMode redisFlushMode) {
        Assert.notNull(redisFlushMode, "redisFlushMode cannot be null");
        this.redisFlushMode = redisFlushMode;
    }

    public void save(RedisSession session) {
        session.saveDelta();
        if (session.isNew()) {
            String sessionCreatedKey = getSessionCreatedChannel(session.getId());
            this.sessionRedisOperations.convertAndSend(sessionCreatedKey, session.delta);
            session.setNew(false);
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void cleanupExpiredSessions() {
        this.expirationPolicy.cleanExpiredSessions();
    }

    public   RedisSession getSession(String id) {
        return getSession(id, false);
    }

    public Map<String, RedisSession> findByIndexNameAndIndexValue(String indexName,
                                                                  String indexValue) {
        if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
            return Collections.emptyMap();
        }
        String principalKey = getPrincipalKey(indexValue);
        Set<Object> sessionIds = this.sessionRedisOperations.boundSetOps(principalKey)
                .members();
        Map<String, RedisSession> sessions = new HashMap<String, RedisSession>(
                sessionIds.size());
        for (Object id : sessionIds) {
            RedisSession session = getSession((String) id);
            if (session != null) {
                sessions.put(session.getId(), session);
            }
        }
        return sessions;
    }

    private RedisSession getSession(String id, boolean allowExpired) {
        Map<Object, Object> entries = getSessionBoundHashOperations(id).entries();
        if (entries.isEmpty()) {
            return null;
        }
        MapSession loaded = loadSession(id, entries);
        if (!allowExpired && loaded.isExpired()) {
            return null;
        }
        RedisSession result = new RedisSession(loaded);
        result.originalLastAccessTime = loaded.getLastAccessedTime();
        return result;
    }

    private MapSession loadSession(String id, Map<Object, Object> entries) {
        MapSession loaded = new MapSession(id);
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            String key = (String) entry.getKey();
            if (CREATION_TIME_ATTR.equals(key)) {
                loaded.setCreationTime((Long) entry.getValue());
            }
            else if (MAX_INACTIVE_ATTR.equals(key)) {
                loaded.setMaxInactiveIntervalInSeconds((Integer) entry.getValue());
            }
            else if (LAST_ACCESSED_ATTR.equals(key)) {
                loaded.setLastAccessedTime((Long) entry.getValue());
            }
            else if (key.startsWith(SESSION_ATTR_PREFIX)) {
                loaded.setAttribute(key.substring(SESSION_ATTR_PREFIX.length()),
                        entry.getValue());
            }
        }
        return loaded;
    }

    public void delete(String sessionId) {
        RedisSession session = getSession(sessionId, true);
        if (session == null) {
            return;
        }

        cleanupPrincipalIndex(session);
        this.expirationPolicy.onDelete(session);

        String expireKey = getExpiredKey(session.getId());
        this.sessionRedisOperations.delete(expireKey);

        session.setMaxInactiveIntervalInSeconds(0);
        save(session);
    }

    public RedisSession createSession() {
        RedisSession redisSession = new RedisSession();
        if (this.defaultMaxInactiveInterval != null) {
            redisSession.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval);
        }
        return redisSession;
    }

    public RedisSession createSession(String id) {
        RedisSession redisSession = new RedisSession(id);
        if (this.defaultMaxInactiveInterval != null) {
            redisSession.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval);
        }
        return redisSession;
    }

    @SuppressWarnings("unchecked")
    public void onMessage(Message message, byte[] pattern) {
        byte[] messageChannel = message.getChannel();
        byte[] messageBody = message.getBody();
        if (messageChannel == null || messageBody == null) {
            return;
        }

        String channel = new String(messageChannel);

        if (channel.startsWith(getSessionCreatedChannelPrefix())) {
            // TODO: is this thread safe?
            Map<Object, Object> loaded = (Map<Object, Object>) this.defaultSerializer
                    .deserialize(message.getBody());
            handleCreated(loaded, channel);
            return;
        }

        String body = new String(messageBody);
        if (!body.startsWith(getExpiredKeyPrefix())) {
            return;
        }

        boolean isDeleted = channel.endsWith(":del");
        if (isDeleted || channel.endsWith(":expired")) {
            int beginIndex = body.lastIndexOf(":") + 1;
            int endIndex = body.length();
            String sessionId = body.substring(beginIndex, endIndex);

            RedisSession session = getSession(sessionId, true);

            if (logger.isDebugEnabled()) {
                logger.debug("Publishing SessionDestroyedEvent for session " + sessionId);
            }

            cleanupPrincipalIndex(session);

            if (isDeleted) {
                handleDeleted(sessionId, session);
            }
            else {
                handleExpired(sessionId, session);
            }
        }
    }

    private void cleanupPrincipalIndex(RedisSession session) {
        if (session == null) {
            return;
        }
        String sessionId = session.getId();
        String principal = PRINCIPAL_NAME_RESOLVER.resolvePrincipal(session);
        if (principal != null) {
            this.sessionRedisOperations.boundSetOps(getPrincipalKey(principal))
                    .remove(sessionId);
        }
    }

    public void handleCreated(Map<Object, Object> loaded, String channel) {
        String id = channel.substring(channel.lastIndexOf(":") + 1);
        ExpiringSession session = loadSession(id, loaded);
        publishEvent(new SessionCreatedEvent(this, session));
    }

    private void handleDeleted(String sessionId, RedisSession session) {
        if (session == null) {
            publishEvent(new SessionDeletedEvent(this, sessionId));
        }
        else {
            publishEvent(new SessionDeletedEvent(this, session));
        }
    }

    private void handleExpired(String sessionId, RedisSession session) {
        if (session == null) {
            publishEvent(new SessionExpiredEvent(this, sessionId));
        }
        else {
            publishEvent(new SessionExpiredEvent(this, session));
        }
    }

    private void publishEvent(ApplicationEvent event) {
        try {
            this.eventPublisher.publishEvent(event);
        }
        catch (Throwable ex) {
            logger.error("Error publishing " + event + ".", ex);
        }
    }

    public void setRedisKeyNamespace(String namespace) {
        this.keyPrefix = DEFAULT_SPRING_SESSION_REDIS_PREFIX + namespace + ":";
    }

    String getSessionKey(String sessionId) {
        return this.keyPrefix + "sessions:" + sessionId;
    }

    String getPrincipalKey(String principalName) {
        return this.keyPrefix + "index:"
                + MyFindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":"
                + principalName;
    }

    String getExpirationsKey(long expiration) {
        return this.keyPrefix + "expirations:" + expiration;
    }

    private String getExpiredKey(String sessionId) {
        return getExpiredKeyPrefix() + sessionId;
    }

    private String getSessionCreatedChannel(String sessionId) {
        return getSessionCreatedChannelPrefix() + sessionId;
    }

    private String getExpiredKeyPrefix() {
        return this.keyPrefix + "sessions:" + "expires:";
    }

    public String getSessionCreatedChannelPrefix() {
        return this.keyPrefix + "event:created:";
    }

    private BoundHashOperations<Object, Object, Object> getSessionBoundHashOperations(
            String sessionId) {
        String key = getSessionKey(sessionId);
        return this.sessionRedisOperations.boundHashOps(key);
    }

    static String getSessionAttrNameKey(String attributeName) {
        return SESSION_ATTR_PREFIX + attributeName;
    }

    private static RedisTemplate<Object, Object> createDefaultTemplate(
            RedisConnectionFactory connectionFactory) {
        Assert.notNull(connectionFactory, "connectionFactory cannot be null");
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    final class RedisSession implements ExpiringSession {
        private final MapSession cached;
        private Long originalLastAccessTime;
        private Map<String, Object> delta = new HashMap<String, Object>();
        private boolean isNew;
        private String originalPrincipalName;

        RedisSession() {
            this(new MapSession());
            this.delta.put(CREATION_TIME_ATTR, getCreationTime());
            this.delta.put(MAX_INACTIVE_ATTR, getMaxInactiveIntervalInSeconds());
            this.delta.put(LAST_ACCESSED_ATTR, getLastAccessedTime());
            this.isNew = true;
            flushImmediateIfNecessary();
        }

        RedisSession(String id) {
            this(new MapSession(id));
            this.delta.put(CREATION_TIME_ATTR, getCreationTime());
            this.delta.put(MAX_INACTIVE_ATTR, getMaxInactiveIntervalInSeconds());
            this.delta.put(LAST_ACCESSED_ATTR, getLastAccessedTime());
            this.isNew = true;
            flushImmediateIfNecessary();
        }

        RedisSession(MapSession cached) {
            Assert.notNull("MapSession cannot be null");
            this.cached = cached;
            this.originalPrincipalName = PRINCIPAL_NAME_RESOLVER.resolvePrincipal(this);
        }

        public void setNew(boolean isNew) {
            this.isNew = isNew;
        }

        public void setLastAccessedTime(long lastAccessedTime) {
            this.cached.setLastAccessedTime(lastAccessedTime);
            this.delta.put(LAST_ACCESSED_ATTR, getLastAccessedTime());
            flushImmediateIfNecessary();
        }

        public boolean isExpired() {
            return this.cached.isExpired();
        }

        public boolean isNew() {
            return this.isNew;
        }

        public long getCreationTime() {
            return this.cached.getCreationTime();
        }

        public String getId() {
            return this.cached.getId();
        }

        public long getLastAccessedTime() {
            return this.cached.getLastAccessedTime();
        }

        public void setMaxInactiveIntervalInSeconds(int interval) {
            this.cached.setMaxInactiveIntervalInSeconds(interval);
            this.delta.put(MAX_INACTIVE_ATTR, getMaxInactiveIntervalInSeconds());
            flushImmediateIfNecessary();
        }

        public int getMaxInactiveIntervalInSeconds() {
            return this.cached.getMaxInactiveIntervalInSeconds();
        }

        @SuppressWarnings("unchecked")
        public Object getAttribute(String attributeName) {
            return this.cached.getAttribute(attributeName);
        }

        public Set<String> getAttributeNames() {
            return this.cached.getAttributeNames();
        }

        public void setAttribute(String attributeName, Object attributeValue) {
            this.cached.setAttribute(attributeName, attributeValue);
            this.delta.put(getSessionAttrNameKey(attributeName), attributeValue);
            flushImmediateIfNecessary();
        }

        public void removeAttribute(String attributeName) {
            this.cached.removeAttribute(attributeName);
            this.delta.put(getSessionAttrNameKey(attributeName), null);
            flushImmediateIfNecessary();
        }

        private void flushImmediateIfNecessary() {
            if (MyRedisOperationsSessionRepository.this.redisFlushMode == RedisFlushMode.IMMEDIATE) {
                saveDelta();
            }
        }

        private void saveDelta() {
            if (this.delta.isEmpty()) {
                return;
            }
            String sessionId = getId();
            getSessionBoundHashOperations(sessionId).putAll(this.delta);
            String principalSessionKey = getSessionAttrNameKey(MyFindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
            String securityPrincipalSessionKey = getSessionAttrNameKey(SPRING_SECURITY_CONTEXT);
            if (this.delta.containsKey(principalSessionKey) || this.delta.containsKey(securityPrincipalSessionKey)) {
                if (this.originalPrincipalName != null) {
                    String originalPrincipalRedisKey = getPrincipalKey(this.originalPrincipalName);
                    MyRedisOperationsSessionRepository.this.sessionRedisOperations.boundSetOps(originalPrincipalRedisKey).remove(sessionId);
                }
                String principal = PRINCIPAL_NAME_RESOLVER.resolvePrincipal(this);
                this.originalPrincipalName = principal;
                if (principal != null) {
                    String principalRedisKey = getPrincipalKey(principal);
                    MyRedisOperationsSessionRepository.this.sessionRedisOperations.boundSetOps(principalRedisKey).add(sessionId);
                }
            }

            this.delta = new HashMap<>(this.delta.size());

            Long originalExpiration = this.originalLastAccessTime == null ? null
                    : this.originalLastAccessTime + TimeUnit.SECONDS
                    .toMillis(getMaxInactiveIntervalInSeconds());
            MyRedisOperationsSessionRepository.this.expirationPolicy.onExpirationUpdated(originalExpiration, this);
        }
    }

    static class PrincipalNameResolver {
        private SpelExpressionParser parser = new SpelExpressionParser();

        public String resolvePrincipal(Session session) {
            String principalName = session.getAttribute(PRINCIPAL_NAME_INDEX_NAME);
            if (principalName != null) {
                return principalName;
            }
            Object authentication = session.getAttribute(SPRING_SECURITY_CONTEXT);
            if (authentication != null) {
                Expression expression = this.parser
                        .parseExpression("authentication?.name");
                return expression.getValue(authentication, String.class);
            }
            return null;
        }
    }
}

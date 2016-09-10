package com.tuotiansudai.spring.session;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
public class MyRedisHttpSessionConfiguration implements ImportAware {

    private CookieHttpSessionStrategy defaultHttpSessionStrategy = new CookieHttpSessionStrategy();

    private List<HttpSessionListener> httpSessionListeners = new ArrayList<>();

    private ServletContext servletContext;

    private Integer maxInactiveIntervalInSeconds = -1;

    private ConfigureRedisAction configureRedisAction = new ConfigureNotifyKeyspaceEventsAction();

    private String redisNamespace = "";

    private RedisFlushMode redisFlushMode = RedisFlushMode.ON_SAVE;

    @Bean
    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter() {
        return new SessionEventHttpSessionListenerAdapter(this.httpSessionListeners);
    }

    @Bean
    public <S extends ExpiringSession> MySessionRepositoryFilter<? extends ExpiringSession> springSessionRepositoryFilter(@Qualifier("mySessionRepository") MySessionRepository<S> sessionRepository) {
        MySessionRepositoryFilter<S> sessionRepositoryFilter = new MySessionRepositoryFilter<>(sessionRepository);
        sessionRepositoryFilter.setServletContext(this.servletContext);
        defaultHttpSessionStrategy.setCookieSerializer(new MyDefaultCookieSerializer());
        sessionRepositoryFilter.setHttpSessionStrategy(this.defaultHttpSessionStrategy);

        return sessionRepositoryFilter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MyRedisOperationsSessionRepository messageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListener, Arrays.asList(new PatternTopic("__keyevent@*:del"), new PatternTopic("__keyevent@*:expired")));
        container.addMessageListener(messageListener, Arrays.asList(new PatternTopic(messageListener.getSessionCreatedChannelPrefix() + "*")));
        return container;
    }

    @Bean
    public RedisTemplate<Object, Object> sessionRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public MyRedisOperationsSessionRepository mySessionRepository(@Qualifier("sessionRedisTemplate") RedisOperations<Object, Object> sessionRedisTemplate,
                                                                  ApplicationEventPublisher applicationEventPublisher) {
        MyRedisOperationsSessionRepository sessionRepository = new MyRedisOperationsSessionRepository(sessionRedisTemplate);
        sessionRepository.setApplicationEventPublisher(applicationEventPublisher);
        sessionRepository.setDefaultMaxInactiveInterval(this.maxInactiveIntervalInSeconds);

        String redisNamespace = getRedisNamespace();
        if (StringUtils.hasText(redisNamespace)) {
            sessionRepository.setRedisKeyNamespace(redisNamespace);
        }

        sessionRepository.setRedisFlushMode(this.redisFlushMode);
        return sessionRepository;
    }

    @Autowired(required = false)
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired(required = false)
    public void setHttpSessionListeners(List<HttpSessionListener> listeners) {
        this.httpSessionListeners = listeners;
    }

    private String getRedisNamespace() {
        if (StringUtils.hasText(this.redisNamespace)) {
            return this.redisNamespace;
        }
        return System.getProperty("spring.session.redis.namespace", "");
    }

    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> enableAttrMap = importMetadata.getAnnotationAttributes(EnableRedisHttpSession.class.getName());
        AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
        this.maxInactiveIntervalInSeconds = enableAttrs.getNumber("maxInactiveIntervalInSeconds");
        this.redisNamespace = enableAttrs.getString("redisNamespace");
        this.redisFlushMode = enableAttrs.getEnum("redisFlushMode");
    }

    @Bean
    public InitializingBean enableRedisKeyspaceNotificationsInitializer(RedisConnectionFactory connectionFactory) {
        return new EnableRedisKeyspaceNotificationsInitializer(connectionFactory, this.configureRedisAction);
    }

    /**
     * Ensures that Redis is configured to send keyspace notifications. This is important
     * to ensure that expiration and deletion of sessions trigger SessionDestroyedEvents.
     * Without the SessionDestroyedEvent resources may not get cleaned up properly. For
     * example, the mapping of the Session to WebSocket connections may not get cleaned
     * up.
     */
    static class EnableRedisKeyspaceNotificationsInitializer implements InitializingBean {
        private final RedisConnectionFactory connectionFactory;

        private ConfigureRedisAction configure;

        EnableRedisKeyspaceNotificationsInitializer(
                RedisConnectionFactory connectionFactory,
                ConfigureRedisAction configure) {
            this.connectionFactory = connectionFactory;
            this.configure = configure;
        }

        public void afterPropertiesSet() throws Exception {
            RedisConnection connection = this.connectionFactory.getConnection();
            this.configure.configure(connection);
        }
    }
}
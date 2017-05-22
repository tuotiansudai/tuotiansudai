package com.tuotiansudai.web.config.interceptors;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.util.WeChatClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.Formatter;
import java.util.Map;
import java.util.UUID;

public class WeChatJSAPISignInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = Logger.getLogger(WeChatJSAPISignInterceptor.class);

    private final WeChatClient weChatClient = WeChatClient.getClient();

    @Value("${common.environment}")
    private Environment environment;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        if (modelAndView == null) {
            return;
        }

        String queryString = request.getQueryString();
        String requestURL = request.getRequestURL().toString();

        String url = Strings.isNullOrEmpty(queryString) ? requestURL : MessageFormat.format("{0}?{1}", requestURL, queryString);

        if (Environment.PRODUCTION == environment) {
            url = url.replaceFirst("http", "https");
        }

        modelAndView.addObject("wxConfig", this.sign(url));
    }

    public Map<String, String> sign(String url) {
        Map<String, String> ret = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("debug", String.valueOf(environment == Environment.DEV))
                .put("appId", weChatClient.getAppid())
                .put("nonceStr", UUID.randomUUID().toString())
                .put("timestamp", String.valueOf(System.currentTimeMillis() / 1000))
                .build());

        //注意这里参数名必须全部小写，且必须有序
        String string1 = MessageFormat.format("jsapi_ticket={0}&noncestr={1}&timestamp={2}&url={3}",
                weChatClient.fetchJSAPITicket(),
                ret.get("nonceStr"),
                ret.get("timestamp"),
                url);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));

            String signature = byteToHex(crypt.digest());

            logger.info(MessageFormat.format("[WeChat JS API Sign] string1: {0}, signature: {1}", string1, signature));

            ret.put("signature", signature);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}

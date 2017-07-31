package com.tuotiansudai.spring.security;

import com.google.common.base.Strings;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

@Component
public class CaptchaHelper {

    static Logger logger = Logger.getLogger(CaptchaHelper.class);

    private final static String MOBILE_APP_IMAGE_CAPTCHA_IP_KEY = "api:image.captcha:ip:{0}";

    private final static String IMAGE_CAPTCHA_REDIS_KEY = "image.captcha:{0}";

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("${mobile.login.interval.seconds}")
    private int ipLeftSecond;

    public String getCaptcha(String sessionIdOrDeviceId) {
        if (Strings.isNullOrEmpty(sessionIdOrDeviceId)) {
            return null;
        }
        String captchaRedisKey = this.getCaptchaRedisKey(sessionIdOrDeviceId);
        return redisWrapperClient.get(captchaRedisKey);
    }

    public void storeCaptcha(String captcha, String sessionIdOrDeviceId) {
        if (Strings.isNullOrEmpty(sessionIdOrDeviceId)) {
            return;
        }
        redisWrapperClient.setex(this.getCaptchaRedisKey(sessionIdOrDeviceId), 180, captcha);
    }

    public boolean captchaVerify(String captcha, String sessionIdOrDeviceId, String ip) {
        if (!this.isImageCaptchaNecessary(ip)) {
            redisWrapperClient.setex(MessageFormat.format(MOBILE_APP_IMAGE_CAPTCHA_IP_KEY, ip), ipLeftSecond, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            return true;
        }

        String captchaRedisKey = this.getCaptchaRedisKey(sessionIdOrDeviceId);
        String actualCaptcha = redisWrapperClient.get(captchaRedisKey);
        redisWrapperClient.del(captchaRedisKey);

        boolean result = !Strings.isNullOrEmpty(captcha) && captcha.trim().equalsIgnoreCase(actualCaptcha);
        redisWrapperClient.setex(MessageFormat.format(MOBILE_APP_IMAGE_CAPTCHA_IP_KEY, ip), ipLeftSecond, new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        return result;
    }

    private String getCaptchaRedisKey(String sessionIdOrDeviceId) {
        return MessageFormat.format(IMAGE_CAPTCHA_REDIS_KEY, sessionIdOrDeviceId);
    }

    public String transferImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] data;
        InputStream in = null;
        try {
            ImageIO.write(bufferedImage, "png", os);
            in = new ByteArrayInputStream(os.toByteArray());
            data = new byte[in.available()];
            in.read(data);
            // 对字节数组Base64编码
            return DatatypeConverter.printBase64Binary(data);// 返回Base64编码过的字节数组字符串
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                os.close();
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                logger.error(ioe.getLocalizedMessage(), ioe);
            }
        }
        return null;

    }

    public boolean isImageCaptchaNecessary(String ip) {
        return redisWrapperClient.exists(MessageFormat.format(MOBILE_APP_IMAGE_CAPTCHA_IP_KEY, ip));
    }
}

package com.tuotiansudai.util;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CaptchaHelper {

    static Logger logger = Logger.getLogger(CaptchaHelper.class);

    public final static String LOGIN_CAPTCHA = "LOGIN_CAPTCHA";

    public final static String REGISTER_CAPTCHA = "REGISTER_CAPTCHA";

    public final static String RETRIEVE_PASSWORD_CAPTCHA = "RETRIEVE_PASSWORD_CAPTCHA";

    public final static String TURN_OFF_NO_PASSWORD_INVEST = "TURN_OFF_NO_PASSWORD_INVEST";

    public final static String MOBILE_APP_LOGIN_IMAGE_CAPTCHA_KEY = "api:{deviceId}:{type}";

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${image.captcha.interval.seconds}")
    private int second;

    public void storeCaptcha(String attributeKey, String captcha) {
        httpServletRequest.getSession().setAttribute(attributeKey, captcha);
    }

    public void storeCaptcha(String attributeKey,String captcha,String deviceId) {
        redisWrapperClient.setex(MOBILE_APP_LOGIN_IMAGE_CAPTCHA_KEY.replace("{deviceId}", deviceId).replace("{type}", attributeKey), second, captcha);
    }

    public boolean captchaVerify(String attributeKey, String captcha,String deviceId) {
        String loginImageCaptcha = MOBILE_APP_LOGIN_IMAGE_CAPTCHA_KEY.replace("{deviceId}", deviceId).replace("{type}",attributeKey);
        String actualCaptcha = redisWrapperClient.get(loginImageCaptcha);
        redisWrapperClient.del(loginImageCaptcha);
        return !Strings.isNullOrEmpty(captcha) && captcha.trim().equalsIgnoreCase(actualCaptcha);
    }
    public boolean captchaVerify(String attributeKey, String captcha) {
        String actualCaptcha;
        if (attributeKey.equalsIgnoreCase(CaptchaHelper.LOGIN_CAPTCHA)) {
            actualCaptcha = httpServletRequest.getHeader(attributeKey);
        } else {
            actualCaptcha = (String) httpServletRequest.getSession().getAttribute(attributeKey);
        }
        httpServletRequest.getSession().removeAttribute(attributeKey);
        return !Strings.isNullOrEmpty(captcha) && captcha.trim().equalsIgnoreCase(actualCaptcha);
    }


    public String transferImageToBase64(BufferedImage bufferedImage){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] data = null;
        InputStream in = null;
        try {
            ImageIO.write(bufferedImage, "png", os);
            in = new ByteArrayInputStream(os.toByteArray());
            data = new byte[in.available()];
            in.read(data);
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(data);// 返回Base64编码过的字节数组字符串
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(),e);
        }finally {
            try{
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                logger.error(ioe.getLocalizedMessage(),ioe);
            }
        }
        return null;

    }
}

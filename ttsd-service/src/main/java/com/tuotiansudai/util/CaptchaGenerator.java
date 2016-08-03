package com.tuotiansudai.util;

import com.google.common.collect.Lists;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.text.producer.NumbersAnswerProducer;

import java.awt.*;

public class CaptchaGenerator {

    public static Captcha generate(int captchaWidth, int captchaHeight) {
        Captcha.Builder captchaBuilder = new Captcha.Builder(captchaWidth, captchaHeight);
        CaptchaDefaultWordRenderer wordRenderer = new CaptchaDefaultWordRenderer(Lists.newArrayList(new Color(15, 61, 112)), Lists.newArrayList(new Font(Font.SANS_SERIF, Font.PLAIN, 24), new Font(Font.SANS_SERIF, Font.ITALIC, 24)));
        NumbersAnswerProducer numbersAnswerProducer = new NumbersAnswerProducer(5);
        CurvedLineNoiseProducer noiseProducer = new CurvedLineNoiseProducer(Color.black, 1.0f);
        return captchaBuilder.addText(numbersAnswerProducer, wordRenderer).addNoise(noiseProducer).addBackground(new GradiatedBackgroundProducer(new Color(235, 235, 235), new Color(235, 235, 235))).build();
    }

}

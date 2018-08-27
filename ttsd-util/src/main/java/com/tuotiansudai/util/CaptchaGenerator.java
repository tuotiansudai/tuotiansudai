package com.tuotiansudai.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.text.producer.NumbersAnswerProducer;
import nl.captcha.text.producer.TextProducer;
import nl.captcha.text.renderer.WordRenderer;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Random;

import static javax.swing.UIManager.getColor;

public class CaptchaGenerator {

    public static Captcha generate(int captchaWidth, int captchaHeight, String captcha) {
        Captcha.Builder captchaBuilder = new Captcha.Builder(captchaWidth, captchaHeight);
        CaptchaDefaultWordRenderer wordRenderer = new CaptchaDefaultWordRenderer(Lists.newArrayList(new Color(15, 61, 112)), Lists.newArrayList(new Font(Font.SANS_SERIF, Font.PLAIN, 24), new Font(Font.SANS_SERIF, Font.ITALIC, 24)));
        NumbersAnswerProducer numbersAnswerProducer = new NumbersAnswerProducer(5);
        CurvedLineNoiseProducer noiseProducer = new CurvedLineNoiseProducer(Color.black, 1.0f);
        return captchaBuilder.addText(Strings.isNullOrEmpty(captcha) ? numbersAnswerProducer : (TextProducer) () -> captcha,
                wordRenderer)
                .addNoise(noiseProducer)
                .addBackground(new GradiatedBackgroundProducer(new Color(235, 235, 235), new Color(235, 235, 235)))
                .build();
    }

    private static class CaptchaDefaultWordRenderer implements WordRenderer {
        private static final Random RAND = new SecureRandom();
        private final java.util.List<Color> _colors;
        private final java.util.List<Font> _fonts;

        public CaptchaDefaultWordRenderer(java.util.List<Color> colors, java.util.List<Font> fonts) {
            this._colors = Lists.newArrayList();
            this._fonts = Lists.newArrayList();
            this._colors.addAll(colors);
            this._fonts.addAll(fonts);
        }

        @Override
        public void render(String word, BufferedImage image) {
            Graphics2D g = image.createGraphics();
            RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g.setRenderingHints(hints);
            createRandomLine(image.getWidth(), image.getHeight(), g);
            FontRenderContext frc = g.getFontRenderContext();
            int xBaseline = (int) Math.round((double) image.getWidth() * 0.02D);
            int yBaseline = image.getHeight() - (int) Math.round((double) image.getHeight() * 0.25D);
            char[] chars = new char[1];
            char[] var12;
            int var11 = (var12 = word.toCharArray()).length;

            for (int var10 = 0; var10 < var11; ++var10) {
                char c = var12[var10];
                chars[0] = c;
                g.setColor(this._colors.get(RAND.nextInt(this._colors.size())));
                int choiceFont = RAND.nextInt(this._fonts.size());
                Font font = this._fonts.get(choiceFont);
                g.setFont(font);
                GlyphVector gv = font.createGlyphVector(frc, chars);
                g.drawChars(chars, 0, chars.length, xBaseline, yBaseline);
                int width = (int) (gv.getVisualBounds().getWidth() * 1.2);
                xBaseline += width;
            }
        }

        private void createRandomLine(int width, int height, Graphics g) {
            for (int i = 0; i < 4; i++) {
                int x1 = getIntRandom(0, (int) (width * 0.6));
                int y1 = getIntRandom(0, (int) (height * 0.6));
                int x2 = getIntRandom((int) (width * 0.4), width);
                int y2 = getIntRandom((int) (height * 0.2), height);
                g.setColor(Color.black);
                g.drawLine(x1, y1, x2, y2);
            }
        }

        private int getIntRandom(int start, int end) {
            if (end < start) {
                int t = end;
                end = start;
                start = t;
            }
            return start + (int) (Math.random() * (end - start));
        }
        }
}

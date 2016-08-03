package com.tuotiansudai.util;


import nl.captcha.text.renderer.WordRenderer;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class CaptchaDefaultWordRenderer implements WordRenderer {
    private static final Random RAND = new SecureRandom();
    private final java.util.List<Color> _colors;
    private final java.util.List<Font> _fonts;

    public CaptchaDefaultWordRenderer(java.util.List<Color> colors, java.util.List<Font> fonts) {
        this._colors = new ArrayList();
        this._fonts = new ArrayList();
        this._colors.addAll(colors);
        this._fonts.addAll(fonts);
    }

    @Override
    public void render(String word, BufferedImage image) {
        Graphics2D g = image.createGraphics();
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g.setRenderingHints(hints);
        FontRenderContext frc = g.getFontRenderContext();
        int xBaseline = (int) Math.round((double) image.getWidth() * 0.02D);
        int yBaseline = image.getHeight() - (int) Math.round((double) image.getHeight() * 0.25D);
        char[] chars = new char[1];
        char[] var12;
        int var11 = (var12 = word.toCharArray()).length;

        for (int var10 = 0; var10 < var11; ++var10) {
            char c = var12[var10];
            chars[0] = c;
            g.setColor((Color) this._colors.get(RAND.nextInt(this._colors.size())));
            int choiceFont = RAND.nextInt(this._fonts.size());
            Font font = (Font) this._fonts.get(choiceFont);
            g.setFont(font);
            GlyphVector gv = font.createGlyphVector(frc, chars);
            g.drawChars(chars, 0, chars.length, xBaseline, yBaseline);
            int width = (int) (gv.getVisualBounds().getWidth() * 1.2);
            xBaseline += width;
        }
    }
}

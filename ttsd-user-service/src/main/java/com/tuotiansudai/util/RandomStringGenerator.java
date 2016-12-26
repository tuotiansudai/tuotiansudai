package com.tuotiansudai.util;

import java.util.Random;

public class RandomStringGenerator {
    private final static char[] alphabet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private final static StringBuilder stringBuilder = new StringBuilder();
    private final static Random random = new Random();

    private RandomStringGenerator() {
    }

    public static String generate(int length) {
        stringBuilder.delete(0, stringBuilder.length());
        for (int i = 0; i < length; ++i) {
            stringBuilder.append(alphabet[random.nextInt(alphabet.length)]);
        }
        return stringBuilder.toString();
    }
}

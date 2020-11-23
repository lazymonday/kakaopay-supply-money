package com.kakaopay.supplymoney.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@RequiredArgsConstructor
@Slf4j
public class TokenUtil {
    private static final String baseCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int charsetLength = baseCharset.length();
    private static final Random random = new Random();

    public static String createRandomToken(int tokenLength) {
        StringBuilder sb = new StringBuilder(tokenLength);
        for (int i = 0; i < tokenLength; ++i) {
            sb.append(baseCharset.charAt(random.nextInt(charsetLength)));
        }

        return sb.toString();
    }
}

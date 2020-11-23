package com.kakaopay.supplymoney.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class TokenService {
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

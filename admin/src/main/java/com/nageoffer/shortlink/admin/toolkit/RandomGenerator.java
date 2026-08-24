package com.nageoffer.shortlink.admin.toolkit;

import java.security.SecureRandom;

/**
 * 分组id随机生成器
 */

/**
 * 随机字符串生成工具。
 */
public final class RandomGenerator {

    private static final int DEFAULT_LENGTH = 6;
    private static final char[] ALPHANUMERIC =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomGenerator() {
    }

    /**
     * 生成包含数字和英文字母的 6 位随机字符串。
     *
     * @return 6 位随机字符串
     */
    public static String generateRandom() {
        StringBuilder result = new StringBuilder(DEFAULT_LENGTH);
        for (int i = 0; i < DEFAULT_LENGTH; i++) {
            result.append(ALPHANUMERIC[RANDOM.nextInt(ALPHANUMERIC.length)]);
        }
        return result.toString();
    }


}

package com.cn.linka.common.utils;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 随机数生成
 */
public class RandomStringUtil {

    private RandomStringUtil() {}

    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter yyMMddHHmmssSSS = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    private static final DateTimeFormatter yyMMdd = DateTimeFormatter.ofPattern("yyMMdd");

    // 랜덤문자열(12자) + "_" + yyMMddHHmmssSSS
    public static String generateType1String() {
        StringBuilder sb = new StringBuilder();
        sb.append(createRandomString(12)).append("_").append(createFormatDate(yyMMddHHmmssSSS));
        return sb.toString();
    }

    // 랜덤문자열(12자) + "_" + yyMMdd + 랜덤숫자(9개)
    public static String generateType2String() {
        StringBuilder sb = new StringBuilder();
        sb.append(createRandomString(12)).append("_").append(createFormatDate(yyMMdd)).append(createRandomNumber(9));
        return sb.toString();
    }

    private static String createRandom(String data, int length) {
        if (length < 1) throw new IllegalArgumentException("length must be a positive number.");
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(data.charAt(
                    random.nextInt(data.length())
            ));
        }
        return sb.toString();
    }

    public static String createRandomString(int length) {
        final String ENGLISH_LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String ENGLISH_UPPER = ENGLISH_LOWER.toUpperCase();
        final String NUMBER = "0123456789";
        final String DATA_FOR_RANDOM_STRING = ENGLISH_LOWER + ENGLISH_UPPER + NUMBER;
        return createRandom(DATA_FOR_RANDOM_STRING, length);
    }

    public static String createRandomNumber(int length) {
        final String NUMBER = "0123456789";
        return createRandom(NUMBER, length);
    }

    public static String createFormatDate(DateTimeFormatter formatter) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        return now.format(formatter);
    }
}

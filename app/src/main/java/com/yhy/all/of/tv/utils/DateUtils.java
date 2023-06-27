package com.yhy.all.of.tv.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * <p>
 * Created on 2023-06-26 11:29
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public class DateUtils {

    /**
     * 自动识别格式
     *
     * @param text 原文
     * @return 格式
     */
    public static String autoPattern(String text) {
        boolean year = false;
        Pattern pattern = Pattern.compile("^[-\\+]?\\d*$");
        if (pattern.matcher(text.substring(0, 4)).matches()) {
            year = true;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (!year) {
            if (text.contains("月") || text.contains("-") || text.contains("/")) {
                if (Character.isDigit(text.charAt(0))) {
                    index = 1;
                }
            } else {
                index = 3;
            }
        }
        for (int i = 0; i < text.length(); i++) {
            char chr = text.charAt(i);
            if (Character.isDigit(chr)) {
                if (index == 0) {
                    sb.append("y");
                }
                if (index == 1) {
                    sb.append("M");
                }
                if (index == 2) {
                    sb.append("d");
                }
                if (index == 3) {
                    sb.append("H");
                }
                if (index == 4) {
                    sb.append("m");
                }
                if (index == 5) {
                    sb.append("s");
                }

                if (index == 6) {
                    sb.append("S");
                }

            } else {
                if (i > 0) {
                    char lastChar = text.charAt(i - 1);
                    if (Character.isDigit(lastChar)) {
                        index++;
                    }
                }
                sb.append(chr);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.autoPattern("2012/2/24"));
        System.out.println(LocalDate.parse("2012/2/24", DateTimeFormatter.ofPattern(DateUtils.autoPattern("2012/2/24"))));
    }
}

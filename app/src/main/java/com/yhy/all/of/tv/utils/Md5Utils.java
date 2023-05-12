package com.yhy.all.of.tv.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2023-05-12 15:58
 *
 * @author 颜洪毅
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Md5Utils {

    /**
     * 生成MD5摘要
     *
     * @param text 原文
     * @return MD摘要
     */
    static String gen(String text) {
        try {
            // 创建MessageDigest对象，用于MD5加密
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将字符串转换成字节数组
            byte[] input = text.getBytes();
            // 计算MD5摘要
            byte[] output = md.digest(input);
            // 将字节数组转换成字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : output) {
                sb.append(Integer.toHexString((b >> 4) & 0xf));
                sb.append(Integer.toHexString(b & 0xf));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 处理异常
            e.printStackTrace();
            return "";
        }
    }
}

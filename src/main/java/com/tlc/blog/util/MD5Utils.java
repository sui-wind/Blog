package com.tlc.blog.util;

import java.security.MessageDigest;

/**
 * MD5加密
 */
public class MD5Utils {

    /**
     * 加密算法
     * @param str 被加密的字符串
     * @return
     */
    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            // 32位加密
            return buf.toString();
            // 16位加密
            // return buf.toString().subString(8, 24);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(code("123456"));
    }
}

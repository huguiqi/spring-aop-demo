package com.example.demo.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2023, zaxh Group All Rights Reserved.
 * @since: 2023/03/31/17:01
 * @summary:
 */
public class Md5Util {


    public static byte[] compute(byte[] content) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(content);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String MD5(String inStr) {
        MessageDigest md = null;
        String outStr = null;

        try {
            md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(inStr.getBytes());
            outStr = bytetoString(digest);
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        }

        return outStr;
    }

    public static String MD5(String inStr, String seed) {
        MessageDigest md = null;
        String outStr = null;

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(seed.getBytes("utf-8"));
            byte[] digest = md.digest(inStr.getBytes());
            outStr = bytetoString(digest);
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

        return outStr;
    }

    public static String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";

        for(int i = 0; i < digest.length; ++i) {
            tempStr = Integer.toHexString(digest[i] & 255);
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            } else {
                str = str + tempStr;
            }
        }

        return str.toLowerCase();
    }
}

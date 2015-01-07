package com.ddling.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by lingdongdong on 15/1/4.
 */
public class MyBase64 {

    public static String decodeStr(String str) {
        Base64 base64 = new Base64();
        byte[] debytes = base64.decodeBase64(new String(str).getBytes());
        return new String(debytes);
    }

    public static String encodeStr(String str) {
        Base64 base64 = new Base64();
        byte[] enbytes = base64.encodeBase64Chunked(str.getBytes());
        return new String(enbytes);
    }
}

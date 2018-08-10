package com.ligen.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by ligen on 2017/6/1.
 */
public class CodecUtil {

    public static String encodeURL(String source) {

        String target = null;

        try {
            target = URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return target;
    }

    public static String decodeURL(String url) {
        String target = null;

        try {
            target = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return target;
    }
}

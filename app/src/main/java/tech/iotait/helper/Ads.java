package tech.iotait.helper;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class Ads {
    public static String generateKey() throws UnsupportedEncodingException {
        String secret = "27b5d55c367b56b22762769d455b2923";
        long time = System.currentTimeMillis()/1000;
        String build = secret+String.valueOf(time);
        String key = Base64.encodeToString(build.getBytes("UTF-8"),Base64.DEFAULT);

        Log.d("HHHHHHH",key);
        return key;
    }

    public static  String Int_ad = "";
    public static  String fb_Int_ad = "";
    public static  String fb_banner = "";
    public static  String fbNativeAdsCode = "";
    public static  String intial = "";
    public static  String googleBanner = "";

    public static String action= "";
}

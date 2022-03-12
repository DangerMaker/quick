package com.hundsun.zjfae.common.http.cookies;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.user.PhoneInfo;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieStore {
    private static final String LOG_TAG = "PersistentCookieStore";

    private static final Map<String, ConcurrentHashMap<String, Cookie>> mapCookie = new ConcurrentHashMap<>();


    private static PersistentCookieStore cookieStore = null;


    public static PersistentCookieStore getCookieStore() {

        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore();
        }
        return cookieStore;
    }

    public void addCookie(HttpUrl url, List<Cookie> cookieList) {

        if (cookieList == null || cookieList.isEmpty()) {

            return;
        }
        addCookie(cookieList);
    }


    public void addCookie(List<Cookie> cookieList) {
        CCLog.e(LOG_TAG, cookieList);
        for (Cookie cookie : cookieList) {
            String name = getCookieToken(cookie);
            if (!cookie.persistent()) {
                if (!mapCookie.containsKey(name)) {
                    mapCookie.put(name, new ConcurrentHashMap<String, Cookie>());
                }
                mapCookie.get(name).put(name, cookie);
            } else {
                if (mapCookie.containsKey(name)) {
                    mapCookie.get(name).remove(name);
                }
            }

        }

    }


    public List<Cookie> getCookies(HttpUrl url) {

        List<Cookie> cookies = getBaseCookie();

        for (String key : mapCookie.keySet()) {
            cookies.addAll(mapCookie.get(key).values());
        }
        return cookies;
    }


    private String getCookieToken(Cookie cookie) {

        return cookie.name() + "@" + cookie.domain();
    }


    private List<Cookie> getBaseCookie() {


        List<Cookie> baseCookies = new ArrayList<>();
        Cookie.Builder baseBuild = new Cookie.Builder();
        PhoneInfo phoneInfo = PhoneInfo.getPhoneInfo();

        baseBuild.domain(BasePresenter.DOMAIN);
        //安卓版本号
        baseBuild.name("clientOsver");
        baseBuild.value(phoneInfo.clientOsver);
        baseCookies.add(baseBuild.build());

        //获取手机的型号 设备名称
        baseBuild.name("deviceModal");
        baseBuild.value(phoneInfo.deviceModal);
        baseCookies.add(baseBuild.build());

        baseBuild.name("deviceNo");
        baseBuild.value(phoneInfo.deviceId);
        baseCookies.add(baseBuild.build());


        baseBuild.name("deviceUuid");
        baseBuild.value(phoneInfo.deviceId);
        baseCookies.add(baseBuild.build());


        //用户id
        baseBuild.name("userid");
        baseBuild.value(UserInfoSharePre.getUserName());
        baseCookies.add(baseBuild.build());
        //p平台
        baseBuild.name("p");
        baseBuild.value(phoneInfo.p);
        baseCookies.add(baseBuild.build());

        //p平台
        baseBuild.name("platform");
        baseBuild.value(phoneInfo.platform);
        baseCookies.add(baseBuild.build());

        //app版本
        baseBuild.name("appVersion");
        baseBuild.value(phoneInfo.appVersion);
        baseCookies.add(baseBuild.build());

        //渠道信息
        baseBuild.name("channel");
        baseBuild.value(phoneInfo.channel);
        baseCookies.add(baseBuild.build());

        //请求时间
        baseBuild.name("ReqTime");
        baseBuild.value(String.valueOf(Utils.getCurTimeLong()));
        baseCookies.add(baseBuild.build());


        return baseCookies;
    }


    public List<Cookie> getCookies() {
        List<Cookie> cookies = new ArrayList<>();

        for (String key : mapCookie.keySet()) {
            cookies.addAll(mapCookie.get(key).values());
        }

        return cookies;
    }


    public void cleanCookie() {
        mapCookie.clear();
    }


    public void putCooKie(String key, Cookie value) {

        CCLog.e("TAG-key", key);
        CCLog.e("TAG-value", value);
        mapCookie.get(key).put(key, value);

    }


//
//    public void add(HttpUrl url, Cookie cookie) {
//        String name = getCookieToken(cookie);
//
//        //将cookies缓存到内存中 如果缓存过期 就重置此cookie
//        if (!cookie.persistent()) {
//            if (!cookies.containsKey(url.host())) {
//                cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
//            }
//            cookies.get(url.host()).put(name, cookie);
//        } else {
//            if (cookies.containsKey(url.host())) {
//                cookies.get(url.host()).remove(name);
//            }
//        }
//
//
//    }
//
//    public List<Cookie> get(HttpUrl url) {
//        ArrayList<Cookie> ret = new ArrayList<>();
//        if (cookies.containsKey(url.host()))
//            ret.addAll(cookies.get(url.host()).values());
//        return ret;
//    }
//
//
//
//    public boolean remove(HttpUrl url, Cookie cookie) {
//        String name = getCookieToken(cookie);
//
//        if (cookies.containsKey(url.host()) && cookies.get(url.host()).containsKey(name)) {
//            cookies.get(url.host()).remove(name);
//
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public List<Cookie> getCookies() {
//        ArrayList<Cookie> ret = new ArrayList<>();
//        for (String key : cookies.keySet())
//            ret.addAll(cookies.get(key).values());
//
//        return ret;
//    }
//
//    /**
//     * cookies 序列化成 string
//     *
//     * @param cookie 要序列化的cookie
//     * @return 序列化之后的string
//     */
//    protected String encodeCookie(SerializableOkHttpCookies cookie) {
//        if (cookie == null)
//            return null;
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        try {
//            ObjectOutputStream outputStream = new ObjectOutputStream(os);
//            outputStream.writeObject(cookie);
//        } catch (IOException e) {
//            Log.d(LOG_TAG, "IOException in encodeCookie", e);
//            return null;
//        }
//
//        return byteArrayToHexString(os.toByteArray());
//    }
//
//    /**
//     * 将字符串反序列化成cookies
//     *
//     * @param cookieString cookies string
//     * @return cookie object
//     */
//    protected Cookie decodeCookie(String cookieString) {
//        byte[] bytes = hexStringToByteArray(cookieString);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//        Cookie cookie = null;
//        try {
//            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            cookie = ((SerializableOkHttpCookies) objectInputStream.readObject()).getCookies();
//        } catch (IOException e) {
//            Log.d(LOG_TAG, "IOException in decodeCookie", e);
//        } catch (ClassNotFoundException e) {
//            Log.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
//        }
//
//        return cookie;
//    }
//
//    /**
//     * 二进制数组转十六进制字符串
//     *
//     * @param bytes byte array to be converted
//     * @return string containing hex values
//     */
//    protected String byteArrayToHexString(byte[] bytes) {
//
//        StringBuilder sb = new StringBuilder(bytes.length * 2);
//
//        for (byte element : bytes) {
//            int v = element & 0xff;
//            if (v < 16) {
//                sb.append('0');
//            }
//            sb.append(Integer.toHexString(v));
//        }
//        return sb.toString().toUpperCase(Locale.US);
//    }
//
//    /**
//     * 十六进制字符串转二进制数组
//     *
//     * @param hexString string of hex-encoded values
//     * @return decoded byte array
//     */
//    protected byte[] hexStringToByteArray(String hexString) {
//        int len = hexString.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
//        }
//        return data;
//    }
//
//
//
//
//    public boolean isLogin(){
//
//
//        return cookies.isEmpty();
//
//    }
}

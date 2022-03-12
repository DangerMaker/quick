package com.hundsun.zjfae.common.http.cookies;

import com.hundsun.zjfae.common.utils.CCLog;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/** * 自动管理Cookies */
public class CookiesManager implements CookieJar {

    private final PersistentCookieStore cookieStore = PersistentCookieStore.getCookieStore();






    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {


        cookieStore.addCookie(url,cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.getCookies(url);
        return cookies;
    }
}


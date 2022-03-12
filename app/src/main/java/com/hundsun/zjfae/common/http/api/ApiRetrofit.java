package com.hundsun.zjfae.common.http.api;

import android.util.Log;

import com.hundsun.zjfae.common.base.BaseApplication;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.converter.CustomGsonConverterFactory;
import com.hundsun.zjfae.common.http.converter.CustomProtoConverterFactory;
import com.hundsun.zjfae.common.http.converter.RefreshException;
import com.hundsun.zjfae.common.http.cookies.CookiesManager;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 作者： ch 时间： 2016/12/27.13:56 描述： 来源：
 */
public class ApiRetrofit {


    private static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private OkHttpClient mOkHttpClient;
    private ApiServer apiServer;

    private String TAG = "ApiRetrofit";

    private static final int TIMEOUT_READ = 60;
    private static final int TIMEOUT_CONNECTION = 35;

    /**
     * 请求访问quest response拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            //  MediaType mediaType = response.body().contentType();
            //String content = response.body().string();
            Log.e(TAG, "----------Request Start----------------");
            Log.e(TAG, "|" + response.code());
            Log.e(TAG, "| " + request.toString() + request.headers().toString());
            Log.e(TAG, "----------Request End:" + duration + "毫秒----------");
            if (response.code() == ConstantCode.REFRESH_CODE) {
                throw new RefreshException(response.body().string());
            }
            return response;
        }
    };

    public ApiRetrofit() {
        mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(new ChuckInterceptor(BaseApplication.getInstance()))
                .addInterceptor(interceptor)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .cookieJar(new CookiesManager())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BasePresenter.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(CustomProtoConverterFactory.create())
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();

        apiServer = retrofit.create(ApiServer.class);
    }

    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public ApiServer getApiService() {
        return apiServer;
    }

}

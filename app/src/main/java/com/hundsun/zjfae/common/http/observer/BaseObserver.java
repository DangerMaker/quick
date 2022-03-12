package com.hundsun.zjfae.common.http.observer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hundsun.zjfae.common.base.BaseView;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.converter.RefreshException;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.CrashUtils;
import com.hundsun.zjfae.common.utils.RxCountdown;
import com.hundsun.zjfae.common.utils.useroperation.UserOperation;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.observers.DisposableObserver;


/**
 * @author moran
 * DisposableObserver 基类
 *
 * **/
public abstract class BaseObserver<T> extends DisposableObserver<T> {
    protected BaseView view;

    protected String  errorMsg = "网络不太顺畅哦~";
    protected String dialogMessage = "";

    private static final String TAG = BaseObserver.class.getName();


    public BaseObserver() {
    }

    public BaseObserver(BaseView view) {
        this.view = view;

    }

    public BaseObserver(BaseView view, String dialogMessage) {
        this.view = view;
        this.dialogMessage = dialogMessage;

    }

    @Override
    protected void onStart() {

        if (view != null) {
            if (!isNetworkConnected(view.onAttach())) {
                //dispose();
                onException(errorMsg);
            } else {
                if (view.isShowLoad()){
                    if (dialogMessage.equals("")) {
                        view.showLoading();
                    }
                    else {
                        view.showLoading(dialogMessage);
                    }
                }
                else {
                    CCLog.e(TAG,"start");
                    RxCountdown.getInstance().interval(new RxCountdown.LoadListener() {
                        @Override
                        public void isShowDialog(boolean isShow) {
                            CCLog.e(TAG,isShow);
                            if (isShow){
                                if (!isDisposed()){
                                    if (dialogMessage.equals("")) {
                                        view.showLoading();
                                    } else {
                                        view.showLoading(dialogMessage);
                                    }
                                }
                            }
                        }
                    });

                }
            }

        }


    }

    @Override
    public void onNext(T t) {

        try {
            if (view != null) {
                RxCountdown.getInstance().clean();
                view.hideLoading();
            }
            onSuccess(t);
        } catch (Exception e) {
            CCLog.e(TAG,e);
            onException(errorMsg);
        }


    }

    @Override
    public void onError(Throwable e) {
        if (e != null){
            CCLog.e(TAG,e.getMessage());
            RxCountdown.getInstance().clean();
            if (view != null){
                view.hideLoading();
            }
            try {
                CrashUtils.getInstance().saveCrashFile(e);
                if (e instanceof RefreshException) {
                    try {
                        JSONObject os = new JSONObject(e.getMessage());
                        JSONObject object = os.optJSONObject("body");
                        String returnCode = object.optString("returnCode");
                        errorMsg = object.optString("returnMsg").equals("") ? object.optString("returnMsg") : "您的访问太过频繁，请稍后重试";


                    } catch (JSONException e1) {
                        errorMsg = "您的访问太过频繁，请稍后重试";
                        e1.printStackTrace();
                    }

                }
                else {
                    onException(errorMsg);
                }
            }
            catch (Exception es){
                CCLog.e(TAG, e.getMessage() + "");
                onException(errorMsg);

            }
        }



    }

    public void onException(String msg) {
        //dispose();
        if (view != null) {
            view.hideLoading();
            view.showError(msg);
        } else {
            CCLog.e(TAG, msg);
        }

    }


    @Override
    public void onComplete() {
        try {
            RxCountdown.getInstance().clean();
            if (view != null) {
                view.hideLoading();
            }
        } catch (Exception e) {
            CCLog.e(TAG,e.getMessage());
            onException(errorMsg);
        }

    }


    public abstract void onSuccess(T t);



    /**
     * 记录业务错误code码
     * @param t 返回请求对象
     * @param returnCode 返回code码
     * @param returnMsg 返回信息
     * */
    protected void onCoreCodeError(T t,String returnCode,String returnMsg){
        if (!returnCode.equals(ConstantCode.RETURN_CODE)){
            UserOperation.getInstance().saveErrorToFile(t.getClass().getSimpleName(),  returnCode,  returnMsg);
        }


    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}

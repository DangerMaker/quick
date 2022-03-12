package com.ushare;

public interface ShareListener {

    void onStart();

    void onResult();

    void onError(Throwable throwable);

    void onCancel();
}

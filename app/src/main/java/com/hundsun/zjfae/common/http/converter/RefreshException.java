package com.hundsun.zjfae.common.http.converter;

import com.hundsun.zjfae.common.utils.CCLog;

public class RefreshException extends RuntimeException {


    public RefreshException(String e){
        super(e);
        CCLog.e("RefreshException",e);


    }


}

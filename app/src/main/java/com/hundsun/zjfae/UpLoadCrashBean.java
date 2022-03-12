package com.hundsun.zjfae;

/**
 * @Description:奔溃日志上送实体类
 * @Author: zhoujianyu
 * @Time: 2019/3/14 10:58
 */
public class UpLoadCrashBean {
    private String contents;
    private String user_id;
    private String client_os;
    private String app_vers;


    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getClient_os() {
        return client_os;
    }

    public void setClient_os(String client_os) {
        this.client_os = client_os;
    }

    public String getApp_vers() {
        return app_vers;
    }

    public void setApp_vers(String app_vers) {
        this.app_vers = app_vers;
    }
}

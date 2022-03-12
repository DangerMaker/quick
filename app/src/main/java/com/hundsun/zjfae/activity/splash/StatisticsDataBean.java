package com.hundsun.zjfae.activity.splash;

/**
 * @Description:点击事件本地保存实体类
 * @Author: zhoujianyu
 * @Time: 2019/1/22 13:53
 */
public class StatisticsDataBean {
    private String pbname;
    private String content;
    private String count;
    private String adsUuid;
    private String adsUrl;

    public String getPbname() {
        return pbname;
    }

    public void setPbname(String pbname) {
        this.pbname = pbname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAdsUuid() {
        return adsUuid;
    }

    public void setAdsUuid(String adsUuid) {
        this.adsUuid = adsUuid;
    }

    public String getAdsUrl() {
        return adsUrl;
    }

    public void setAdsUrl(String adsUrl) {
        this.adsUrl = adsUrl;
    }

    @Override
    public String toString() {
        return "StatisticsDataBean{" +
                "pbname='" + pbname + '\'' +
                ", content='" + content + '\'' +
                ", count='" + count + '\'' +
                ", adsUuid='" + adsUuid + '\'' +
                ", adsUrl='" + adsUrl + '\'' +
                '}';
    }
}

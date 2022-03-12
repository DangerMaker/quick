package com.hundsun.zjfae.fragment.more;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.UrlParams;

/**
 * @Description:更多presenter
 * @Author: zhoujianyu
 * @Time: 2019/1/15 10:52
 */
public class MorePresenter extends BasePresenter<MoreView> {
    public MorePresenter(MoreView baseView) {
        super(baseView);
    }

    /**
     * 获取更多列表数据
     * */
    public void getMoreList() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.MoreList, getRequestMap());
        addDisposable(
                apiServer.getMoreList(url),
                new ProtoBufObserver<UrlParams.Ret_PBAPP_urlparams>(baseView) {
            @Override
            public void onSuccess(UrlParams.Ret_PBAPP_urlparams body) {
                if (body.getReturnCode().equals("0000")) {
                    baseView.loadData(body);
                }
            }
        });
    }

    /**
     * 意见反馈跳转之前请求brokerNo
     * */
    public void getBrokerNo() {
        UserDetailInfo.REQ_PBIFE_userbaseinfo_getUserDetailInfo.Builder builder = UserDetailInfo.REQ_PBIFE_userbaseinfo_getUserDetailInfo.newBuilder();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.USERBASEINFO, getRequestMap());
        addDisposable(apiServer.getBrokerNo(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo body) {
                if (body.getReturnCode().equals("0000")) {
                    baseView.getBrokerNo(body.getData().getBrokerNo());
                } else {
                    baseView.getBrokerNo("");
                }
            }
        });
    }

    /**
     * 意见反馈跳转之前url
     * */
    public void getFeedbackUrl() {
        AllAzjProto.PEAGetUrl.Builder builder = AllAzjProto.PEAGetUrl.newBuilder();
        builder.setKeyNo("7");
        String url = parseUrl(AZJ, PBURL, VURLAZJ, getRequestMap());
        addDisposable(apiServer.getFeedbackUrl(url, getBody(builder.build().toByteArray())), new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof AllAzjProto.PEARetUrl) {
                    //用户消息
                    AllAzjProto.PEARetUrl retUrl = (AllAzjProto.PEARetUrl) o;
                    if (retUrl.getUrlsList().size() > 0) {
                        baseView.getFeedbackUrl(retUrl.getUrlsList().get(0).getBackendUrl(),retUrl.getUrlsList().get(0).getIsShare());
                    }
                }
            }
        });
    }


}

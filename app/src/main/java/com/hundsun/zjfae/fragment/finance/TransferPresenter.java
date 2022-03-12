package com.hundsun.zjfae.fragment.finance;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v4.TransferList;

public class TransferPresenter extends BasePresenter<TransferView> {

    private long transferOrderTime;

    private TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew transferListNew;
    private AllAzjProto.PEARetControl transferOrderControl;


    public TransferPresenter(TransferView baseView) {
        super(baseView);
    }


    /******************************转让列表**************************************/


    public void initTransfer(String uuids, int pageIndex) {

        addDisposable(transferList(uuids, pageIndex), new ProtoBufObserver<TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew>(baseView) {
            @Override
            public void onSuccess(TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew body) {
                baseView.initTransfer(body);
            }
        });

    }


    /**
     * 带条件的转让列表查询
     *
     * @param uuids 查询的uuid
     * @date: 2021/5/18 11:21
     * @author: moran
     */
    public void onQueryTransferList(String uuids) {

        CCLog.i("查询条件uuid",uuids);

        //转让专区
        TransferList.REQ_PBIFE_prdtransferquery_prdQueryTransferOrderListNew.Builder trans = TransferList.REQ_PBIFE_prdtransferquery_prdQueryTransferOrderListNew.newBuilder();
        trans.setPageIndex("1");
        trans.setPageSize("20");
        trans.setProductName("");
        trans.setUuids(uuids);
        Map<String, String> map = getRequestMap();
        map.put("version", FOUR_VERSION);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryTransferOrderListNew, map);
        addDisposable(apiServer.transferList(url, getBody(trans.build().toByteArray())), new ProtoBufObserver<TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew>(baseView) {
            @Override
            public void onSuccess(TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew body) {

                baseView.onQueryTransferList(body);
            }
        });

    }


    public void getUserData(final boolean isAuthentication) {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.onUserInfo(userDetailInfo, isAuthentication);
            }
        });
    }

    /**
     * 合格投资者申请失败原因
     */
    public void requestInvestorStatus(final String isRealInvestor) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo, getRequestMap());
        UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.Builder builder =
                UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder();
        builder.setDynamicType1("highNetWorthUpload");

        addDisposable(apiServer.investorStatus(url, getBody(builder.build().toByteArray())), new BaseObserver<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo>(baseView) {
            @Override
            public void onSuccess(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body) {
                baseView.requestInvestorStatus(body, isRealInvestor);
            }
        });
    }


    /**
     * 转让列表
     **/
    private Observable transferList(String uuids, int pageIndex) {

        //转让专区
        TransferList.REQ_PBIFE_prdtransferquery_prdQueryTransferOrderListNew.Builder trans = TransferList.REQ_PBIFE_prdtransferquery_prdQueryTransferOrderListNew.newBuilder();
        trans.setPageIndex(String.valueOf(pageIndex));
        trans.setPageSize("10");
        trans.setUuids(uuids);
        Map map = getRequestMap();
        map.put("version", FOUR_VERSION);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryTransferOrderListNew, map);
        return apiServer.transferList(url, getBody(trans.build().toByteArray()));

    }


    /**
     * 购买查询条件
     *
     * @param controlType 1 为筛选 ， 0为排序
     **/
    public void controlList(String controlType) {

        //转让列表查询条件

        String transferUrl = parseUrl(AZJ, PBCTV, VCTLAZJ);
        AllAzjProto.PEAGetControlVersion.Builder control = AllAzjProto.PEAGetControlVersion.newBuilder();
        control.setControlLocation("transferList");
        control.setInterVers("v4");
        control.setControlType(controlType);


        addDisposable(apiServer.subscribeProduct(transferUrl, getBody(control.build().toByteArray())), new ProtoBufObserver<AllAzjProto.PEARetControl>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetControl o) {


                baseView.onControl(o, controlType);

            }

        });


    }


}

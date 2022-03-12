package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.MyHoldingView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v3.PrdQueryTaUnitFinanceNewPb;

/**
 * @Description:我的持仓（presenter）
 * @Author: yangtianren
 */
public class MyHoldingPresenter extends BasePresenter<MyHoldingView> {
    public MyHoldingPresenter(MyHoldingView baseView) {
        super(baseView);
    }





    public void init(String pageIndex, String uuid){
        Observable observable = Observable.mergeDelayError(loadData( pageIndex, uuid),getQueryCriteria(),getImage());

        addDisposable(observable, new ProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {

                if (mClass instanceof PrdQueryTaUnitFinanceNewPb.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceNew){

                    PrdQueryTaUnitFinanceNewPb.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceNew financeNew = (PrdQueryTaUnitFinanceNewPb.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceNew) mClass;
                    String pageCount = financeNew.getData().getPageInfo().getPageCount();
                    String totalCount = financeNew.getData().getPageInfo().getTotalCount();
                    String sumPreProfit = financeNew.getData().getSumPreProfit();
                    baseView.onPrdQueryList(pageCount,totalCount,sumPreProfit,financeNew.getData().getTaUnitFinanceListList());
                }
                //筛选条件
                else if (mClass instanceof AllAzjProto.PEARetControl){

                    baseView.queryCriteriaList(((AllAzjProto.PEARetControl)mClass).getControlListList());

                }
                //列表图标
                else if (mClass instanceof AllAzjProto.PEARetIcons){
                    baseView.getImage(((AllAzjProto.PEARetIcons)mClass).getIconsListList());
                }

            }
        });
    }


    public void initProduct(String pageIndex, String uuid){

        addDisposable(loadData(pageIndex, uuid), new ProtoBufObserver<PrdQueryTaUnitFinanceNewPb.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceNew>(baseView) {
            @Override
            public void onSuccess(PrdQueryTaUnitFinanceNewPb.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceNew financeNew) {

                String pageCount = financeNew.getData().getPageInfo().getPageCount();
                String totalCount = financeNew.getData().getPageInfo().getTotalCount();
                String sumPreProfit = financeNew.getData().getSumPreProfit();
                baseView.onPrdQueryList(pageCount,totalCount,sumPreProfit,financeNew.getData().getTaUnitFinanceListList());
            }
        });
    }



    //获取数据
    private Observable loadData(String pageIndex, String uuid) {
        PrdQueryTaUnitFinanceNewPb.REQ_PBIFE_prdquery_prdQueryTaUnitFinanceNew.Builder builder = PrdQueryTaUnitFinanceNewPb.REQ_PBIFE_prdquery_prdQueryTaUnitFinanceNew.newBuilder();
        builder.setPageIndex(pageIndex);
        builder.setPageSize("10");
        builder.setVersion("1.0.0");
        builder.setUuids(uuid);
        Map<String, String> hashMap = getRequestMap();
        hashMap.put("version", version);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryTaUnitFinanceNew, hashMap);


        return apiServer.myHoldLoadData(url, getBody(builder.build().toByteArray()));

    }

    /**
     * 获取筛选条件
     * */
    private Observable getQueryCriteria() {
        AllAzjProto.PEAGetControl.Builder builder = AllAzjProto.PEAGetControl.newBuilder();
        builder.setControlLocation("myUnit");
        String url = parseUrl(AZJ, PBCTL, VCTLAZJ, getRequestMap());

        return apiServer.subscribeProduct(url, getBody(builder.build().toByteArray()));
    }

    /**
     * 获取右上角图片资源数据
     * */
    private Observable getImage() {
        AllAzjProto.PEAGetIcons.Builder iconBuilder = AllAzjProto.PEAGetIcons.newBuilder();
        iconBuilder.setIconsLocation("positionList");
        String iconUrl = parseUrl(AZJ, PBICO, VICOAZJ, getRequestMap());

        return apiServer.allIconImage(iconUrl, getBody(iconBuilder.build().toByteArray()));

    }
}

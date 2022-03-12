package com.hundsun.zjfae.activity.assetstream.presenter;

import com.hundsun.zjfae.activity.assetstream.view.AssetStreamView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gensazj.FundAccountLogPB;

/**
 * @Description:资金流水（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class AssetStreamPresenter extends BasePresenter<AssetStreamView> {

    public AssetStreamPresenter(AssetStreamView baseView) {
        super(baseView);
    }

    /**
     * 获取资金流水列表数据
     */
    public void getReserveListData(int pageIndex, String startDate, String endDate, String id) {
        FundAccountLogPB.REQ_PBIFE_fund_queryFundAccountLog.Builder builder = FundAccountLogPB.REQ_PBIFE_fund_queryFundAccountLog.newBuilder();
        builder.setPageIndex(pageIndex + "");
        builder.setPageSize("10");
        builder.setStartDate(startDate);
        builder.setEndDate(endDate);
        builder.setQuanDetailsId(id);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.AssetStream);
        addDisposable(apiServer.getReserveListData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<FundAccountLogPB.Ret_PBIFE_fund_queryFundAccountLog>(baseView) {
            @Override
            public void onSuccess(FundAccountLogPB.Ret_PBIFE_fund_queryFundAccountLog ret_pbife_fund_queryFundAccountLog) {
                baseView.loadData(ret_pbife_fund_queryFundAccountLog.getData().getFundAccountLogListList());
            }
        });
    }
}

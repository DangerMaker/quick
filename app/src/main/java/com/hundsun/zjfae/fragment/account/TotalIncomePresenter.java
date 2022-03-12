package com.hundsun.zjfae.fragment.account;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.QueryFundEarningsLog;
import onight.zjfae.afront.gens.UserAssetsInfo;

/**
 * @Description:总收益（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class TotalIncomePresenter extends BasePresenter<TotalIncomeView> {

    public TotalIncomePresenter(TotalIncomeView baseView) {
        super(baseView);
    }

    /**
     * 获取总收益数据(昨日收益以及累计收益)
     */
    public void getData() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TotalIncome, getRequestMap());
        addDisposable(apiServer.getData(url), new ProtoBufObserver<UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo>(baseView) {
            @Override
            public void onSuccess(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo ret_pbife_fund_loadUserAssetsInfo) {
                baseView.loadData(ret_pbife_fund_loadUserAssetsInfo);
            }

        });

    }
    /**
     * 获取总收益列表数据
     */
    public void getListData(int pageIndex) {
        QueryFundEarningsLog.REQ_PBIFE_statistic_queryFundEarningsLog.Builder builder = QueryFundEarningsLog.REQ_PBIFE_statistic_queryFundEarningsLog.newBuilder();
        builder.setPageIndex(pageIndex + "");
        builder.setPageSize("10");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TotalIncomeList, getRequestMap());
        addDisposable(apiServer.getListData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<QueryFundEarningsLog.Ret_PBIFE_statistic_queryFundEarningsLog>(baseView) {
            @Override
            public void onSuccess(QueryFundEarningsLog.Ret_PBIFE_statistic_queryFundEarningsLog data) {
                List<QueryFundEarningsLog.PBIFE_statistic_queryFundEarningsLog.FundEarningsLogList> list = new ArrayList<>(data.getData().getFundEarningsLogListList());
                baseView.loadListData(list);
            }

        });
    }
}

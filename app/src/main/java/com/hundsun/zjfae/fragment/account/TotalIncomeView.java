package com.hundsun.zjfae.fragment.account;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.QueryFundEarningsLog;
import onight.zjfae.afront.gens.UserAssetsInfo;

/**
 * @Description:总收益（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface TotalIncomeView extends BaseView {


     /**
      * 描述一下方法的作用
      * @method
      * @date: 2019-11-01 14:11
      * @author: moran
      * @param data 初始化数据
      * @return
      */
    void loadData(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo data);

    void loadListData(List<QueryFundEarningsLog.PBIFE_statistic_queryFundEarningsLog.FundEarningsLogList> list);

}

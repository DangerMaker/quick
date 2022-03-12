package com.hundsun.zjfae.activity.assetstream.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gensazj.FundAccountLogPB;

/**
 * @Description:资金流水（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface AssetStreamView extends BaseView {
    void loadData(List<FundAccountLogPB.PBIFE_fund_queryFundAccountLog.FundAccountLogList> list);

}

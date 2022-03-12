package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v3.PrdQueryTaUnitFinanceNewPb;

/**
 * @Description:我的持仓（View）
 * @Author: yangtianren
 */
public interface MyHoldingView extends BaseView {

    /**
     * 获取筛选条件
     * @param control_list 筛选条件集合
     *
     * */
    void queryCriteriaList(List<AllAzjProto.PBAPPSearchSortControl_l1> control_list);



    /**
     * 我的持仓详情列表
     * @param pageCount 总页数
     * @param totalCount 总条数
     * @param sumPreProfit sumPreProfit
     * @param list 我的持仓List
     * */
    void onPrdQueryList(String pageCount,String totalCount, String sumPreProfit, List<PrdQueryTaUnitFinanceNewPb.PBIFE_prdquery_prdQueryTaUnitFinanceNew.TaUnitFinanceList> list);

    /**
     * 获取我的持仓列表小图标
     * @param iconList 图标集合
     * */
    void getImage(List<AllAzjProto.PBAPPIcons> iconList);

}

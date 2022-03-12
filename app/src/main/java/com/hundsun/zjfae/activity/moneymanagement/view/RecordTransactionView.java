package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.PBIFEPrdqueryQueryUnifyPurchaseTradeList;

/**
 * @Description:交易记录（View）
 * @Author: yangtianren
 */
public interface RecordTransactionView extends BaseView {



     /**
      *
      * @method
      * @date: 2020/11/1 15:10
      * @author: moran
      * @param list 交易记录列表详情
      * @return
      */
    void getData(List<PBIFEPrdqueryQueryUnifyPurchaseTradeList.PBIFE_prdquery_queryUnifyPurchaseTradeList.MyTradeObject> list);


     /**
      * 撤单预检查接口请求回调
      * @method
      * @date: 2020/11/1 15:11
      * @author: moran
      * @param delegationCode 认购编号
      * @param code 请求返回code码
      * @param message 错误提示
      * @param data_message 弹框详情
      * @return
      */
    void cancelPre(String delegationCode, String code, String message,String data_message);

     /**
      * 撤单接口请求回调
      * @method
      * @date: 2020/11/1 15:14
      * @author: moran
      * @param code 请求返回code码
      * @param msg 请求返回提示
      * @return
      */
    void cancel(String code, String msg);
}

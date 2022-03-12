package com.hundsun.zjfae.activity.home.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**     
  *  @ProjectName:    
  * @Package:        com.hundsun.zjfae.activity.home.view
  * @ClassName:      WebView
  * @Description:     java类作用描述 
  * @Author:         moran
  * @CreateDate:     2019/8/6 16:24
  * @UpdateUser:     更新者：
  * @UpdateDate:     2019/8/6 16:24
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public interface WebView extends BaseView {


    /**
     * 获取用户详细信息
     * @param isCertification 是否实名认证
     * @param userbaseinfo_getUserDetailInfo 用户详细信息
     * */
    void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userbaseinfo_getUserDetailInfo,boolean isCertification);


    /**
     * 合格投资者申请失败原因
     * @param userHighNetWorthInfo 失败原因
     * @param isRealInvestor 是否真正合格投资者
     * */
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo userHighNetWorthInfo, String isRealInvestor);

}

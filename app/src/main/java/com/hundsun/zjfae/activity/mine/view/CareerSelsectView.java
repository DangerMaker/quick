package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.CareerEnumTypeCom;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine.view
 * @ClassName:      CareerSelsectView
 * @Description:     职业选择View
 * @Author:         moran
 * @CreateDate:     2019/7/31 17:17
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/31 17:17
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface CareerSelsectView extends BaseView {


    /**
     * 职业信息
     * @method
     * @description 描述一下方法的作用
     * @date: 2019/7/31 19:06
     * @author: moran
     * @param enumDataList 职业集合
     * @return null
     */
    void onCareerEnumTypeComList (List<CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData> enumDataList);


    /**
     * 职业回调结果
     * @method
     * @description 描述一下方法的作用
     * @date: 2019/7/31 20:18
     * @author: moran
     * @param returnMsg 结果信息
     * @return
     */
    void onCommitUpdateProfession(String returnMsg);
}

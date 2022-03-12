package com.hundsun.zjfae.activity.home.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.CareerEnumTypeCom;

public interface OccupationorEducationView extends BaseView {


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


}

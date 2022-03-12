package com.hundsun.zjfae.activity.home.presenter;

import com.hundsun.zjfae.activity.home.view.OccupationorEducationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.List;

import onight.zjfae.afront.gens.CareerEnumTypeCom;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.home.presenter
 * @ClassName:      OccupationorEducationPresenter
 * @Description:     职业信息
 * @Author:         moran
 * @CreateDate:     2019/8/5 10:25
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/8/5 10:25
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class OccupationorEducationPresenter extends BasePresenter<OccupationorEducationView> {
    public OccupationorEducationPresenter(OccupationorEducationView baseView) {
        super(baseView);
    }


    public void onCareerEnumTypeCom(){
        CareerEnumTypeCom.REQ_PBIFE_userbaseinfo_getEnumTypeCom.Builder careerBuild = CareerEnumTypeCom.REQ_PBIFE_userbaseinfo_getEnumTypeCom.newBuilder();
        careerBuild.setType("zy");

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.CAREER);

        addDisposable(apiServer.onCareerEnumTypeCom(url, getBody(careerBuild.build().toByteArray())), new ProtoBufObserver<CareerEnumTypeCom.Ret_PBIFE_userbaseinfo_getEnumTypeCom>(baseView) {
            @Override
            public void onSuccess(CareerEnumTypeCom.Ret_PBIFE_userbaseinfo_getEnumTypeCom enumTypeCom) {
                List<CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData> enumDatasList =enumTypeCom.getData().getEnumDatasList();
                baseView.onCareerEnumTypeComList(enumDatasList);
            }
        });
    }
}

package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.CareerSelsectView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;

import java.util.List;

import onight.zjfae.afront.gens.CareerEnumTypeCom;
import onight.zjfae.afront.gens.UpdateProfession;

/**
 *  @ProjectName:    浙金中心
 * @Package:        com.hundsun.zjfae.activity.mine.presenter
 * @ClassName:      CareerSelsectPresenter
 * @Description:     职业选择Presenter
 * @Author:         moran
 * @CreateDate:     2019/7/31 17:17
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/31 17:17
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class CareerSelsectPresenter extends BasePresenter<CareerSelsectView> {
    public CareerSelsectPresenter(CareerSelsectView baseView) {
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

    public void onCommitUpdateProfession(String careerName){
        UpdateProfession.REQ_PBIFE_userinfomanage_updateProfession.Builder fessionBuild = UpdateProfession.REQ_PBIFE_userinfomanage_updateProfession.newBuilder();
        fessionBuild.setProfession(careerName);
        fessionBuild.setAccount(UserInfoSharePre.getAccount());

        final String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.PROFESSION);

        addDisposable(apiServer.onCommitUpdateProfession(url, getBody(fessionBuild.build().toByteArray())), new ProtoBufObserver<UpdateProfession.Ret_PBIFE_userinfomanage_updateProfession>(baseView) {
            @Override
            public void onSuccess(UpdateProfession.Ret_PBIFE_userinfomanage_updateProfession updateProfession) {
                baseView.onCommitUpdateProfession(updateProfession.getReturnMsg());

            }
        });
    }


}

package com.hundsun.zjfae.activity.home.presenter;

import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.home.view.UserInfoView;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import onight.zjfae.afront.gens.ModifyUserBaseInfoPB;
import onight.zjfae.afront.gens.SetUserHeaderPB;
import onight.zjfae.afront.gens.UserBaseInfoPB;
import onight.zjfae.afront.gensazj.Dictionary;

/**
 * @Description:个人基本信息（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class UserInfoPresenter extends BasePresenter<UserInfoView> {

    public UserInfoPresenter(UserInfoView baseView) {
        super(baseView);
    }

    /**
     * 获取用户基本数据接口
     */
    public void getUserInfoData() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserInfo, getRequestMap());
        addDisposable(apiServer.getUserInfoData(url), new ProtoBufObserver<UserBaseInfoPB.Ret_PBIFE_userinfomanage_userBaseInfo>(baseView) {
            @Override
            public void onSuccess(UserBaseInfoPB.Ret_PBIFE_userinfomanage_userBaseInfo ret_pbife_userinfomanage_userBaseInfo) {
                baseView.getUserInfoData(ret_pbife_userinfomanage_userBaseInfo.getData());
            }
        });
    }

    /**
     * 修改用户基本数据接口
     */
    public void modifyUserInfoData(final int type, final String info) {
        ModifyUserBaseInfoPB.REQ_PBIFE_userinfomanage_modifyUserBaseInfo.Builder builder = ModifyUserBaseInfoPB.REQ_PBIFE_userinfomanage_modifyUserBaseInfo.newBuilder();
        switch (type) {
            case 1:
                //紧急联系人
                builder.setContent(info);
                break;
            case 2:
                //联系电话
                builder.setContentMobile(info);
                break;
            case 3:
                //居住地
                builder.setDomicile(info);
                break;
            case 4:
                //微信
                builder.setWechat(info);
                break;
            case 5:
                //职业
                builder.setProfession(info);
                break;
            case 6:
                //学历
                builder.setEducation(info);
                break;
            default:
                break;
        }
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ModifyUserInfo, getRequestMap());
        addDisposable(apiServer.modifyUserInfoData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ModifyUserBaseInfoPB.Ret_PBIFE_userinfomanage_modifyUserBaseInfo>(baseView) {
            @Override
            public void onSuccess(ModifyUserBaseInfoPB.Ret_PBIFE_userinfomanage_modifyUserBaseInfo ret_pbife_userinfomanage_modifyUserBaseInfo) {
                baseView.modifyUserInfoData(ret_pbife_userinfomanage_modifyUserBaseInfo.getReturnCode(), ret_pbife_userinfomanage_modifyUserBaseInfo.getReturnMsg(), type, info);
            }
        });
    }

    /**
     * 获取头像接口
     */
    public void getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("userHeader.urlPrefix");
        diction.addKeyNo("userHeader.urlSuffix");
        addDisposable(apiServer.getDictionary(url, getBody(diction.build().toByteArray())), new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>(baseView) {
            @Override
            public void onSuccess(Dictionary.Ret_PBAPP_dictionary dictionary) {
                List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                baseView.getPicDictionary(parms.get(0).getKeyCode(), parms.get(1).getKeyCode());
            }
        });

    }

    /**
     * 图片上传接口
     */
    public void uploadPic(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account ", UserInfoSharePre.getAccount());
//        File file = new File(compressPath);
        builder.addFormDataPart("imageFile", "Image" + 0 + ".png", RequestBody.create(MediaType.parse("image/*"), file));
        addDisposable(apiServer.updateUserPhoto(UpLoadPicImage_UserInfo, builder.build().parts()), new BaseObserver<ImageUploadBean>() {
            @Override
            public void onSuccess(ImageUploadBean uploadBean) {
                baseView.upLoadPicImage(uploadBean.getReturnCode(), uploadBean.getReturnMsg());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                baseView.upLoadPicImage(null,null);
            }
        });
    }

    /**
     * 图片上传成功以后还需通知后台截屏
     */
    public void setUserHeader() {
        SetUserHeaderPB.REQ_PBIFE_userheader_setUserHeader.Builder builder = SetUserHeaderPB.REQ_PBIFE_userheader_setUserHeader.newBuilder();
        builder.setX("0");
        builder.setY("0");
        builder.setW("375");
        builder.setH("375");
        builder.setWidth("375");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SetUserHeader, getRequestMap());
        addDisposable(apiServer.setUserHeader(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<SetUserHeaderPB.Ret_PBIFE_userheader_setUserHeader>(baseView) {
            @Override
            public void onSuccess(SetUserHeaderPB.Ret_PBIFE_userheader_setUserHeader ret_pbife_userheader_setUserHeader) {
                baseView.setUserHeader(ret_pbife_userheader_setUserHeader.getReturnCode(), ret_pbife_userheader_setUserHeader.getReturnMsg());
            }
        });
    }
}

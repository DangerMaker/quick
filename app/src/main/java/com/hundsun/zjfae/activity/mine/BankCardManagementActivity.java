package com.hundsun.zjfae.activity.mine;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.ConfigurationUtils;
import com.hundsun.zjfae.activity.mine.presenter.BankCardManagementPresenter;
import com.hundsun.zjfae.activity.mine.view.BankCardManagementView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.dialog.SendSmsCodeDialog;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;
import com.zjfae.captcha.face.TencentFaceCallBack;
import com.zjfae.captcha.face.TencentFaceError;
import com.zjfae.captcha.face.TencentFaceSDK;
import com.zjfae.captcha.face.TencentFaceStatus;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import onight.zjfae.afront.gens.CancelApplication;
import onight.zjfae.afront.gens.PBIFEUserinfomanageCheckTradePassword;
import onight.zjfae.afront.gens.UserChangeCardInfo;
import onight.zjfae.afront.gens.UserUnbindCardInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;
import onight.zjfae.afront.gensazj.TencentFace;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.mine
 * @ClassName: BankCardManagementActivity
 * @Description: 银行卡管理界面 ，1.人脸识别，2上传身份证资料
 * @Author: moran
 * @CreateDate: 2019/6/13 19:35
 * @UpdateUser: 更新者：moran
 * @UpdateDate: 2019/6/14 13:32
 * @UpdateRemark: 1.增加签署人脸协议回调, 1.2增加腾讯人脸识别SDK，增加字典表接口判断，修改调起逻辑
 * @Version: 1.2
 */
@RuntimePermissions
public class BankCardManagementActivity extends CommActivity<BankCardManagementPresenter> implements BankCardManagementView, View.OnClickListener {


    private TextView warning_content, tv_type;

    private LinearLayout face_layout, id_card_layout;

    private ConfigurationUtils faceConfiguration = null;


    private static final int REQUEST_FACE_CODE = 0x791;

    private static final int REQUEST_FACE_LIVE_CODE = 0x751;


    private String unbindCardStatus = "", changeCardStatus = "";

    private TextView title_type;

    private int failures = 0;

    private SendSmsCodeDialog.Builder bankSmsDialog;


    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        //会员解卡审核状态	String 不校验 是 null：未提出过申请；-1：待审核；0审核不通过；2：审核通过,缺审核状态
        faceConfiguration.setIdCard(userDetailInfo.getData().getCertificateCodeAll());
        faceConfiguration.setUserId(userDetailInfo.getData().getUserId());
        faceConfiguration.setIdCardName(userDetailInfo.getData().getName());
        //银行卡解绑申请状态
        unbindCardStatus = userDetailInfo.getData().getUnbindCardStatus();
        //银行卡换卡申请状态
        changeCardStatus = userDetailInfo.getData().getChangeCardStatus();
    }

    /**
     * 解绑失败原因
     */
    @Override
    public void onUserUnbindCardInfo(UserUnbindCardInfo.Ret_PBIFE_bankcardmanage_getUserUnbindCardInfo unbindCardInfo) {

        if (unbindCardInfo.getData().getFaceId().equals("1")) {

            showDialog(unbindCardInfo.getData().getRemark(), "重新申请", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //是解绑银行卡
                    faceConfiguration.setDynamicType("unbindBankCardUpload");
                    //材料解绑

                    isShowPassWord("", false);
                }
            });


        } else {
            List<UserUnbindCardInfo.DictDynamic2> dynamicList = unbindCardInfo.getData().getDictDynamicListList();
            StringBuffer buffer = new StringBuffer();
            for (UserUnbindCardInfo.DictDynamic2 dynamic : dynamicList) {
                if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {

                    buffer.append(dynamic.getTitle()).append("失败原因：").append(dynamic.getAuditComment()).append("\n");
                }


            }

            showDialog(buffer.toString(), "重新申请", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //是解绑银行卡
                    faceConfiguration.setDynamicType("unbindBankCardUpload");
                    CCLog.e("解绑卡操作");
                    //材料解绑
                    isShowPassWord("", false);
                }
            });

        }


    }


    /**
     * 换卡失败原因
     */
    @Override
    public void onUserChangeCardInfo(UserChangeCardInfo.Ret_PBIFE_bankcardmanage_getUserChangeCardInfo changeCardInfo) {

        List<UserChangeCardInfo.DictDynamic1> dynamicList = changeCardInfo.getData().getDictDynamicListList();

        StringBuffer buffer = new StringBuffer();
        for (UserChangeCardInfo.DictDynamic1 dynamic : dynamicList) {
            if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {
                buffer.append(dynamic.getTitle()).append("失败原因：").append(dynamic.getAuditComment()).append("\n");
            }

        }

        showDialog(buffer.toString(), "重新申请", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //是换卡
                faceConfiguration.setDynamicType("changeBankCardUpload");
                CCLog.e("换卡操作");
                //材料解绑
                isShowPassWord("", false);
            }
        });


    }

    /**
     * 取消解绑申请
     */
    @Override
    public void cleanUnbindCard(CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication cancelApplication, String type) {

        String returnMsg = "";
        if (type.equals("0")) {

            unbindCardStatus = "";
            returnMsg = "您已成功取消解绑卡申请";

        } else {
            changeCardStatus = "";
            returnMsg = "您已成功取消换卡申请";
        }


        showDialog(returnMsg, "知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish();
                if (faceConfiguration.getVerifyscene().equals("unbindCard")) {

                    unbindCardStatus = "";
                } else {
                    changeCardStatus = "";
                }
            }
        });

    }


    /**
     * 人脸识别协议
     **/
    @Override
    public void queryFaceAgreement(final Notices.Ret_PBAPP_notice notice) {
        //用户未签署人脸识别协议！
        String noticeContent = notice.getData().getNotice().getNoticeContent();
        String noticeTitle = notice.getData().getNotice().getNoticeTitle();
        String pageTitle = notice.getData().getNotice().getPageTitle();
        Intent intent = new Intent(BankCardManagementActivity.this, FaceAuthorisationActivity.class);
        faceConfiguration.setMobile(UserInfoSharePre.getMobile());
        faceConfiguration.setNoticeContent(noticeContent);
        faceConfiguration.setNoticeTitle(noticeTitle);
        faceConfiguration.setPageTitle(pageTitle);
        Bundle bundle = new Bundle();
        bundle.putParcelable("face", faceConfiguration);
        intent.putExtra("faceBundle", bundle);
        startActivityForResult(intent, REQUEST_FACE_CODE);

    }

    @Override
    public void onUserIsFaceAgreement() {
        network();
    }

    /**
     * 初始化face++人脸识别
     */
    private void network() {

        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                String uuid = UUID.randomUUID().toString();
                uuid = Base64.encodeToString(uuid.getBytes(), Base64.DEFAULT);
            }
        });

        BaseObserver<Boolean> observer = new BaseObserver<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

                if (aBoolean) {


                } else {
                    showDialog("联网授权失败");
                }
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }


    private static final int ERROR_CODE = 0X175;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FACE_CODE && resultCode == RESULT_OK) {
            network();
        }
        //失败次数
        else if (requestCode == REQUEST_FACE_LIVE_CODE && resultCode == ERROR_CODE) {
            failures = failures + 1;
            String errorInfo = "";

            if (failures >= 3) {
                showDialog("人脸识别未通过，您也可通过上传身份材料方式进行解绑操作", "取消", "去上传", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //isShowPassWord(false);

                        if (faceConfiguration.getVerifyscene().equals("unbindCard")) {
                            faceConfiguration.setDynamicType("unbindBankCardUpload");
                        }
                        //换卡操作
                        else {
                            faceConfiguration.setDynamicType("changeBankCardUpload");
                        }
                        //材料解绑
                        Intent intent = new Intent(BankCardManagementActivity.this, UnbindBankCardUploadActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("face", faceConfiguration);
                        intent.putExtra("faceBundle", bundle);

                        baseStartActivity(intent);


                    }
                });
            } else if (data != null) {

                errorInfo = data.getStringExtra("error");

                showDialog(errorInfo);
            }
        }


    }


    /***
     * 密码校检
     * */
    @Override
    public void checkTradePassword(PBIFEUserinfomanageCheckTradePassword.Ret_PBIFE_userinfomanage_checkTradePassword checkTradePassword, boolean isType) {
        String returnCode = checkTradePassword.getReturnCode();
        String returnMsg = checkTradePassword.getReturnMsg();
        //密码校验成功
        if (returnCode.equals("0000")) {
            //用户是否签署人脸识别协议
            if (isType) {

                BankCardManagementActivityPermissionsDispatcher.onBankCardManagementWithPermissionCheck(BankCardManagementActivity.this);

            } else {

                //是解绑银行卡
                if (faceConfiguration.getVerifyscene().equals("unbindCard")) {

                    faceConfiguration.setDynamicType("unbindBankCardUpload");
                }
                //换卡操作
                else {
                    faceConfiguration.setDynamicType("changeBankCardUpload");
                }
                //材料解绑
                Intent intent = new Intent(this, UnbindBankCardUploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("face", faceConfiguration);
                intent.putExtra("faceBundle", bundle);

                baseStartActivity(intent);
            }
        } else {
            showDialog(returnMsg);
        }
    }


    @Override
    public void onTencentFace(TencentFace.Ret_PBAPP_tencentface ret_pbapp_tencentface) {
        final TencentFace.PBAPP_tencentface tencentface = ret_pbapp_tencentface.getData();
        faceConfiguration.setTencentfaceOrder(tencentface.getOrderNO());
        TencentFaceSDK.getInstance().init(this).execute(tencentface.getFaceId(), tencentface.getOrderNO(), tencentface.getAppid(), tencentface.getNonce(), tencentface.getUserID(), tencentface.getSign(), tencentface.getLicense(), new TencentFaceCallBack() {

            @Override
            public void onSuccess(TencentFaceStatus faceStatus) {
                presenter.onTencentFaceCallback(tencentface.getOrderNO(), faceConfiguration.getVerifyscene());
//                CCLog.e("isSuccess",faceStatus.isSuccess());
//                CCLog.e("liveRate",faceStatus.getLiveRate());
//                CCLog.e("similarity",faceStatus.getSimilarity());
//                CCLog.e("userImageString",faceStatus.getUserImageString());
//
//                Intent intent = new Intent(BankCardManagementActivity.this,FaceDeleteBankActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("face",faceConfiguration);
//                intent.putExtra("faceBundle",bundle);
//                baseStartActivity(intent);
                if (faceConfiguration.isNeedIdCard()) {
                    Intent intent = new Intent(BankCardManagementActivity.this, BankCardCertificationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("face", faceConfiguration);
                    intent.putExtra("faceBundle", bundle);
                    baseStartActivity(intent);
                } else {
                    Intent intent = new Intent(BankCardManagementActivity.this, FaceDeleteBankActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("face", faceConfiguration);
                    intent.putExtra("faceBundle", bundle);
                    baseStartActivity(intent);
                }


            }

            @Override
            public void onError(TencentFaceError faceError) {
                presenter.onTencentFaceCallback(tencentface.getOrderNO(), faceConfiguration.getVerifyscene());
                failures = failures + 1;
                if (failures >= 3) {
                    showDialog("人脸识别未通过，您也可通过上传身份材料方式进行解绑操作", "取消", "去上传", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //isShowPassWord(false);
                            if (faceConfiguration.getVerifyscene().equals("unbindCard")) {
                                faceConfiguration.setDynamicType("unbindBankCardUpload");
                            }
                            //换卡操作
                            else {
                                faceConfiguration.setDynamicType("changeBankCardUpload");
                            }
                            //材料解绑
                            Intent intent = new Intent(BankCardManagementActivity.this, UnbindBankCardUploadActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("face", faceConfiguration);
                            intent.putExtra("faceBundle", bundle);

                            baseStartActivity(intent);

                        }
                    });
                } else {
                    //showDialog("人像采集失败，请按提示操作");

                    //ToastUtil.showToast(BankCardManagementActivity.this,faceError.getDesc());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showDialog(faceError.getDesc());
                        }
                    }, 800);

                    //showDialog(faceError.getDesc());


                }
            }

            @Override
            public void onError() {
                showDialog("人脸识别初始化失败，请重试", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.onTencentFaceCallback(tencentface.getOrderNO(), faceConfiguration.getVerifyscene());
                    }
                });

            }


            @Override
            public void onComplete() {
                hideLoading();

            }
        });
    }

    @Override
    public void onDeleteBankSms() {
        bankSmsDialog.setSmsButtonState(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_layout:

                //解绑卡
                if (faceConfiguration.getVerifyscene().equals("unbindCard")) {


                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {


                        String msg = "您还有待审核解绑卡流程，请取消申请后在进⾏此操作。";
                        showDialog("温馨提示", msg, "取消", "取消申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("unbindCard", "0");
                            }
                        });

                    } else if (!changeCardStatus.equals("") && changeCardStatus.equals("-1")) {

                        String msg = "您还有待审核换卡流程，请取消申请后在进⾏此操作。";
                        showDialog("温馨提示", msg, "取消", "取消换卡申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("changeCard", "1");
                            }
                        });
                    } else {
                        isShowPassWord("", true);
                    }


                }
                //换卡
                else {
                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {

                        String msg = "您还有待审核解绑卡流程，请取消申请后在进⾏此操作。";

                        showDialog("温馨提示", msg, "取消", "取消解绑申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("unbindCard", "0");
                            }
                        });


                    } else if (!changeCardStatus.equals("") && changeCardStatus.equals("-1")) {

                        String msg = "您还有待审核换卡流程，请取消申请后在进⾏此操作。";
                        showDialog("温馨提示", msg, "取消", "取消申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("changeCard", "1");
                            }
                        });
                    } else {

                        isShowPassWord("", true);

                    }

                }


                break;
            case R.id.id_card_layout:

                //解绑卡
                if (faceConfiguration.getVerifyscene().equals("unbindCard")) {

                    CCLog.e("解绑", "解绑");
                    // "" : 未提出过申请；-1：待审核；0审核不通过；1：审核通过
                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {

                        String msg = "解绑卡资料正在审核中,请耐心等待~";

                        showDialog("温馨提示", msg, "取消", "取消申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("unbindCard", "0");
                            }
                        });

                    }
                    //换卡资料正在审核中,请耐心等待~<
                    else if (!changeCardStatus.equals("") && changeCardStatus.equals("-1")) {
                        String msg = "换卡资料正在审核中，请耐⼼等待。";
                        showDialog("温馨提示", msg, "取消", "取消换卡申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("changeCard", "1");
                            }
                        });
                    }
                    //解绑资料审核失败
                    else if (!unbindCardStatus.equals("") && unbindCardStatus.equals("0")) {

                        presenter.getUserUnbindCardInfo();
                    } else {

                        if (faceConfiguration.isNeedSms() && faceConfiguration.isZjSendSms()) {

                            bankSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            bankSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                                @Override
                                public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                                    if (Utils.isStringEmpty(smsCode)) {
                                        showDialog("请输入短信验证码");
                                    } else {
                                        dialog.dismiss();
                                        isShowPassWord(smsCode, false);
                                    }
                                }

                                @Override
                                public void onSmsClick() {

                                    presenter.unBindBankCardSMS(faceConfiguration.getPayChannelNo(), "1");

                                }
                            });
                            bankSmsDialog.show();
                        } else {
                            isShowPassWord("", false);
                        }

                    }

                }
                //换卡
                else {


                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {

                        String msg = "解绑卡资料正在审核中，请耐⼼等待。";

                        showDialog("温馨提示", msg, "取消", "取消解绑申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("unbindCard", "0");
                            }
                        });

                    }

                    //换卡资料正在审核中,请耐心等待~<
                    else if (!changeCardStatus.equals("") && changeCardStatus.equals("-1")) {
                        String msg = "换卡资料正在审核中，请耐⼼等待。";
                        showDialog("温馨提示", msg, "取消", "取消申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.cleanUnbindBankCard("changeCard", "1");
                            }
                        });
                    }

                    //换卡资料审核失败
                    else if (!changeCardStatus.equals("") && changeCardStatus.equals("0")) {

                        presenter.getUserChangeCardInfo();
                    } else {
                        if (faceConfiguration.isNeedSms() && faceConfiguration.isZjSendSms()) {

                            bankSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            bankSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                                @Override
                                public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                                    if (Utils.isStringEmpty(smsCode)) {
                                        showDialog("请输入短信验证码");
                                    } else {
                                        dialog.dismiss();
                                        isShowPassWord(smsCode, false);
                                    }
                                }

                                @Override
                                public void onSmsClick() {

                                    presenter.unBindBankCardSMS(faceConfiguration.getPayChannelNo(), "1");

                                }
                            });
                            bankSmsDialog.show();
                        } else {
                            isShowPassWord("", false);
                        }

                    }
                }
                break;
            default:
                break;
        }
    }


    private void isShowPassWord(String smsCode, final boolean faceFlags) {
        PlayWindow id_card_play = new PlayWindow(this);
        id_card_play.setPayListener(new PlayWindow.OnPayListener() {
            @Override
            public void onSurePay(String password) {
                presenter.checkPlayPassWord(EncDecUtil.AESEncrypt(password), smsCode, faceFlags);
            }
        });
        id_card_play.showAtLocation(findViewById(R.id.bank_card_management_layout));
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getBundleExtra("faceBundle");

        if (bundle != null) {
            faceConfiguration = bundle.getParcelable("face");
        }

        intType();
        presenter.getUserDate();


    }

    @Override
    public void initView() {
        setTitle("银行卡管理");
        warning_content = findViewById(R.id.warning_content);
        face_layout = findViewById(R.id.face_layout);
        id_card_layout = findViewById(R.id.id_card_layout);
        tv_type = findViewById(R.id.tv_type);
        face_layout.setOnClickListener(this);
        id_card_layout.setOnClickListener(this);
        title_type = findViewById(R.id.title_type);
        bankSmsDialog = new SendSmsCodeDialog.Builder(this);

    }


    private void initUnBind() {
        final SpannableStringBuilder style = new SpannableStringBuilder();
        String str = "解绑当天需无资金进出记录。解绑成功后，当日将不支持提现，请您根据需要，提前做好安排。";
        style.append("解绑当天需无资金进出记录。解绑成功后，当日将不支持提现，请您根据需要，提前做好安排。如有疑问，请联系中心客服热线400-9999-000（工作日8:30-18:00）");
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorRed));
        style.setSpan(foregroundColorSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //配置给TextView
        warning_content.setMovementMethod(LinkMovementMethod.getInstance());
        warning_content.setHighlightColor(Color.TRANSPARENT);
        warning_content.setText(style);
        title_type.setText("请选择一种解绑方式");

    }

    private void initChange() {

        final SpannableStringBuilder style = new SpannableStringBuilder();
        StringBuffer buffer = new StringBuffer("尊敬的会员，请您在申请办理换卡业务前注意以下事项：");
        buffer.append("\n");
        buffer.append("1.如您当前绑定的工行卡卡片状态异常（包括作废卡、销户卡、挂失卡）可申请换卡(如无法确定卡片状态，请与发卡行联系)；");
        buffer.append("\n");
        buffer.append("2.该功能仅支持更换您名下其他工行借记卡，不支持信用卡、贷记卡。").append("\n");
        buffer.append("3.您当前在浙金中心预留的姓名、身份证号、手机号需与原绑定工行卡时在浙金中心预留的信息一致；").append("\n");
        buffer.append("4.如有疑问，请联系中心客服热线400-9999-000（工作日8:30-18:00）。");

        String title = "尊敬的会员，请您在申请办理换卡业务前注意以下事项：";
        String str = "1.如您当前绑定的工行卡卡片状态异常（包括作废卡、销户卡、挂失卡）可申请换卡(如无法确定卡片状态，请与发卡行联系)；";
        style.append(buffer.toString());
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorRed));
        style.setSpan(foregroundColorSpan, title.length(), (title.length() + str.length()), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //配置给TextView
        warning_content.setMovementMethod(LinkMovementMethod.getInstance());
        warning_content.setHighlightColor(Color.TRANSPARENT);
        warning_content.setText(style);

        title_type.setText("请选择一种换卡方式");
    }

    @Override
    protected BankCardManagementPresenter createPresenter() {
        return new BankCardManagementPresenter(this);
    }


    //文字显示
    private void intType() {

        //是解绑
        StringBuffer buffer = new StringBuffer("采集人脸图像进行");
        if (faceConfiguration.getVerifyscene().equals("unbindCard")) {
            buffer.append("解绑");
            initUnBind();
        } else {
            buffer.append("验证");
            initChange();
        }
        tv_type.setText(buffer.toString());
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_card_management;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.bank_card_management_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @NeedsPermission({Manifest.permission.CAMERA})
    void onBankCardManagement() {
//        if (presenter.isTencentFace()){
//            showLoading("人脸识别初始化");
//            presenter.onTencentFace(faceConfiguration.getIdCardName(),faceConfiguration.getIdCard());
//        }
//        else {
//            faceConfiguration.setTencentfaceOrder("");
//            presenter.queryFaceAgreement(faceConfiguration.getIdCard());
//
//        }

        presenter.onTencentFace(faceConfiguration.getIdCardName(), faceConfiguration.getIdCard());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BankCardManagementActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA})
    void onBankCardManagementDenied() {
        showDialog("你的相机暂未对浙金app授权，人脸识别功能将不能使用", "去授权", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BankCardManagementActivityPermissionsDispatcher.onBankCardManagementWithPermissionCheck(BankCardManagementActivity.this);
            }
        });
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA})
    void onBankCardManagementAgain() {
        showDialog("你的相机暂未对浙金app授权，人脸识别功能将不能使用", "去设置", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                }
                startActivity(intent);
            }
        });
    }

}

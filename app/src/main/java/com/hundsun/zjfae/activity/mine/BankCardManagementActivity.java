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
 * @Description: ????????????????????? ???1.???????????????2?????????????????????
 * @Author: moran
 * @CreateDate: 2019/6/13 19:35
 * @UpdateUser: ????????????moran
 * @UpdateDate: 2019/6/14 13:32
 * @UpdateRemark: 1.??????????????????????????????, 1.2????????????????????????SDK???????????????????????????????????????????????????
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
        //????????????????????????	String ????????? ??? null????????????????????????-1???????????????0??????????????????2???????????????,???????????????
        faceConfiguration.setIdCard(userDetailInfo.getData().getCertificateCodeAll());
        faceConfiguration.setUserId(userDetailInfo.getData().getUserId());
        faceConfiguration.setIdCardName(userDetailInfo.getData().getName());
        //???????????????????????????
        unbindCardStatus = userDetailInfo.getData().getUnbindCardStatus();
        //???????????????????????????
        changeCardStatus = userDetailInfo.getData().getChangeCardStatus();
    }

    /**
     * ??????????????????
     */
    @Override
    public void onUserUnbindCardInfo(UserUnbindCardInfo.Ret_PBIFE_bankcardmanage_getUserUnbindCardInfo unbindCardInfo) {

        if (unbindCardInfo.getData().getFaceId().equals("1")) {

            showDialog(unbindCardInfo.getData().getRemark(), "????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //??????????????????
                    faceConfiguration.setDynamicType("unbindBankCardUpload");
                    //????????????

                    isShowPassWord("", false);
                }
            });


        } else {
            List<UserUnbindCardInfo.DictDynamic2> dynamicList = unbindCardInfo.getData().getDictDynamicListList();
            StringBuffer buffer = new StringBuffer();
            for (UserUnbindCardInfo.DictDynamic2 dynamic : dynamicList) {
                if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {

                    buffer.append(dynamic.getTitle()).append("???????????????").append(dynamic.getAuditComment()).append("\n");
                }


            }

            showDialog(buffer.toString(), "????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //??????????????????
                    faceConfiguration.setDynamicType("unbindBankCardUpload");
                    CCLog.e("???????????????");
                    //????????????
                    isShowPassWord("", false);
                }
            });

        }


    }


    /**
     * ??????????????????
     */
    @Override
    public void onUserChangeCardInfo(UserChangeCardInfo.Ret_PBIFE_bankcardmanage_getUserChangeCardInfo changeCardInfo) {

        List<UserChangeCardInfo.DictDynamic1> dynamicList = changeCardInfo.getData().getDictDynamicListList();

        StringBuffer buffer = new StringBuffer();
        for (UserChangeCardInfo.DictDynamic1 dynamic : dynamicList) {
            if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {
                buffer.append(dynamic.getTitle()).append("???????????????").append(dynamic.getAuditComment()).append("\n");
            }

        }

        showDialog(buffer.toString(), "????????????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //?????????
                faceConfiguration.setDynamicType("changeBankCardUpload");
                CCLog.e("????????????");
                //????????????
                isShowPassWord("", false);
            }
        });


    }

    /**
     * ??????????????????
     */
    @Override
    public void cleanUnbindCard(CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication cancelApplication, String type) {

        String returnMsg = "";
        if (type.equals("0")) {

            unbindCardStatus = "";
            returnMsg = "?????????????????????????????????";

        } else {
            changeCardStatus = "";
            returnMsg = "??????????????????????????????";
        }


        showDialog(returnMsg, "?????????", new DialogInterface.OnClickListener() {
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
     * ??????????????????
     **/
    @Override
    public void queryFaceAgreement(final Notices.Ret_PBAPP_notice notice) {
        //????????????????????????????????????
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
     * ?????????face++????????????
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
                    showDialog("??????????????????");
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
        //????????????
        else if (requestCode == REQUEST_FACE_LIVE_CODE && resultCode == ERROR_CODE) {
            failures = failures + 1;
            String errorInfo = "";

            if (failures >= 3) {
                showDialog("?????????????????????????????????????????????????????????????????????????????????", "??????", "?????????", new DialogInterface.OnClickListener() {
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
                        //????????????
                        else {
                            faceConfiguration.setDynamicType("changeBankCardUpload");
                        }
                        //????????????
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
     * ????????????
     * */
    @Override
    public void checkTradePassword(PBIFEUserinfomanageCheckTradePassword.Ret_PBIFE_userinfomanage_checkTradePassword checkTradePassword, boolean isType) {
        String returnCode = checkTradePassword.getReturnCode();
        String returnMsg = checkTradePassword.getReturnMsg();
        //??????????????????
        if (returnCode.equals("0000")) {
            //????????????????????????????????????
            if (isType) {

                BankCardManagementActivityPermissionsDispatcher.onBankCardManagementWithPermissionCheck(BankCardManagementActivity.this);

            } else {

                //??????????????????
                if (faceConfiguration.getVerifyscene().equals("unbindCard")) {

                    faceConfiguration.setDynamicType("unbindBankCardUpload");
                }
                //????????????
                else {
                    faceConfiguration.setDynamicType("changeBankCardUpload");
                }
                //????????????
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
                    showDialog("?????????????????????????????????????????????????????????????????????????????????", "??????", "?????????", new DialogInterface.OnClickListener() {
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
                            //????????????
                            else {
                                faceConfiguration.setDynamicType("changeBankCardUpload");
                            }
                            //????????????
                            Intent intent = new Intent(BankCardManagementActivity.this, UnbindBankCardUploadActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("face", faceConfiguration);
                            intent.putExtra("faceBundle", bundle);

                            baseStartActivity(intent);

                        }
                    });
                } else {
                    //showDialog("???????????????????????????????????????");

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
                showDialog("???????????????????????????????????????", "?????????", new DialogInterface.OnClickListener() {
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

                //?????????
                if (faceConfiguration.getVerifyscene().equals("unbindCard")) {


                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {


                        String msg = "???????????????????????????????????????????????????????????????????????????";
                        showDialog("????????????", msg, "??????", "????????????", new DialogInterface.OnClickListener() {
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

                        String msg = "????????????????????????????????????????????????????????????????????????";
                        showDialog("????????????", msg, "??????", "??????????????????", new DialogInterface.OnClickListener() {
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
                //??????
                else {
                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {

                        String msg = "???????????????????????????????????????????????????????????????????????????";

                        showDialog("????????????", msg, "??????", "??????????????????", new DialogInterface.OnClickListener() {
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

                        String msg = "????????????????????????????????????????????????????????????????????????";
                        showDialog("????????????", msg, "??????", "????????????", new DialogInterface.OnClickListener() {
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

                //?????????
                if (faceConfiguration.getVerifyscene().equals("unbindCard")) {

                    CCLog.e("??????", "??????");
                    // "" : ?????????????????????-1???????????????0??????????????????1???????????????
                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {

                        String msg = "??????????????????????????????,???????????????~";

                        showDialog("????????????", msg, "??????", "????????????", new DialogInterface.OnClickListener() {
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
                    //???????????????????????????,???????????????~<
                    else if (!changeCardStatus.equals("") && changeCardStatus.equals("-1")) {
                        String msg = "????????????????????????????????????????????????";
                        showDialog("????????????", msg, "??????", "??????????????????", new DialogInterface.OnClickListener() {
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
                    //????????????????????????
                    else if (!unbindCardStatus.equals("") && unbindCardStatus.equals("0")) {

                        presenter.getUserUnbindCardInfo();
                    } else {

                        if (faceConfiguration.isNeedSms() && faceConfiguration.isZjSendSms()) {

                            bankSmsDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            bankSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                                @Override
                                public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                                    if (Utils.isStringEmpty(smsCode)) {
                                        showDialog("????????????????????????");
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
                //??????
                else {


                    if (!unbindCardStatus.equals("") && unbindCardStatus.equals("-1")) {

                        String msg = "???????????????????????????????????????????????????";

                        showDialog("????????????", msg, "??????", "??????????????????", new DialogInterface.OnClickListener() {
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

                    //???????????????????????????,???????????????~<
                    else if (!changeCardStatus.equals("") && changeCardStatus.equals("-1")) {
                        String msg = "????????????????????????????????????????????????";
                        showDialog("????????????", msg, "??????", "????????????", new DialogInterface.OnClickListener() {
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

                    //????????????????????????
                    else if (!changeCardStatus.equals("") && changeCardStatus.equals("0")) {

                        presenter.getUserChangeCardInfo();
                    } else {
                        if (faceConfiguration.isNeedSms() && faceConfiguration.isZjSendSms()) {

                            bankSmsDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            bankSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                                @Override
                                public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                                    if (Utils.isStringEmpty(smsCode)) {
                                        showDialog("????????????????????????");
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
        setTitle("???????????????");
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
        String str = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
        style.append("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????400-9999-000????????????8:30-18:00???");
        //????????????????????????
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorRed));
        style.setSpan(foregroundColorSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //?????????TextView
        warning_content.setMovementMethod(LinkMovementMethod.getInstance());
        warning_content.setHighlightColor(Color.TRANSPARENT);
        warning_content.setText(style);
        title_type.setText("???????????????????????????");

    }

    private void initChange() {

        final SpannableStringBuilder style = new SpannableStringBuilder();
        StringBuffer buffer = new StringBuffer("???????????????????????????????????????????????????????????????????????????");
        buffer.append("\n");
        buffer.append("1.????????????????????????????????????????????????????????????????????????????????????????????????????????????(???????????????????????????????????????????????????)???");
        buffer.append("\n");
        buffer.append("2.??????????????????????????????????????????????????????????????????????????????????????????").append("\n");
        buffer.append("3.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????").append("\n");
        buffer.append("4.??????????????????????????????????????????400-9999-000????????????8:30-18:00??????");

        String title = "???????????????????????????????????????????????????????????????????????????";
        String str = "1.????????????????????????????????????????????????????????????????????????????????????????????????????????????(???????????????????????????????????????????????????)???";
        style.append(buffer.toString());
        //????????????????????????
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorRed));
        style.setSpan(foregroundColorSpan, title.length(), (title.length() + str.length()), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //?????????TextView
        warning_content.setMovementMethod(LinkMovementMethod.getInstance());
        warning_content.setHighlightColor(Color.TRANSPARENT);
        warning_content.setText(style);

        title_type.setText("???????????????????????????");
    }

    @Override
    protected BankCardManagementPresenter createPresenter() {
        return new BankCardManagementPresenter(this);
    }


    //????????????
    private void intType() {

        //?????????
        StringBuffer buffer = new StringBuffer("????????????????????????");
        if (faceConfiguration.getVerifyscene().equals("unbindCard")) {
            buffer.append("??????");
            initUnBind();
        } else {
            buffer.append("??????");
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
//            showLoading("?????????????????????");
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
        showDialog("???????????????????????????app??????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BankCardManagementActivityPermissionsDispatcher.onBankCardManagementWithPermissionCheck(BankCardManagementActivity.this);
            }
        });
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA})
    void onBankCardManagementAgain() {
        showDialog("???????????????????????????app??????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
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

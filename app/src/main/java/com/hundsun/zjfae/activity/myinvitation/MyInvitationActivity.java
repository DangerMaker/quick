package com.hundsun.zjfae.activity.myinvitation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.myinvitation.presenter.MyInvitationPresenter;
import com.hundsun.zjfae.activity.myinvitation.view.MyInvitationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ApiRetrofit;
import com.hundsun.zjfae.common.http.api.ApiServer;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.ushare.UShare;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import onight.zjfae.afront.gensazj.UserRecommendInfoPB;
import onight.zjfae.afront.gensazj.v2.BannerProto;

import static com.hundsun.zjfae.common.base.BasePresenter.BASE_URL;

/**
 * @Description:我的邀请
 * @Author: zhoujianyu
 * @Time: 2018/9/18 10:42
 */
public class MyInvitationActivity extends CommActivity implements MyInvitationView {
    private MyInvitationPresenter mPresenter;
    private ImageView img_download;
    private String url = "";
    private LinearLayout share;
    private Button btn_invite;
    private Bitmap mBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_invitation;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_invitation);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MyInvitationPresenter(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("我的邀请");
        img_download = findViewById(R.id.img_download);
        share = findViewById(R.id.share);
        btn_invite = findViewById(R.id.btn_invite);
        findViewById(R.id.rl_invitation_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(MyInvitationActivity.this, InvitationListActivity.class);
            }
        });
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            return;
        } else {
            initShareBitmap();
            mPresenter.getMyInvitationData();
            mPresenter.getFuncURL();
        }
    }

    @Override
    public void loadData(UserRecommendInfoPB.PBIFE_friendsrecommend_userRecommendInfo bean) {
        ((TextView) findViewById(R.id.tv_registNum)).setText(bean.getRegistNum() + "位>>");
        ((TextView) findViewById(R.id.tv_bindCardNum)).setText("已有" + bean.getBindCardNum() + "位好友绑卡");
        ((TextView) findViewById(R.id.tv_investNum)).setText("已有" + bean.getInvestNum() + "位好友投资");
        ((TextView) findViewById(R.id.tv_info1)).setText(bean.getInfo1());
        ((TextView) findViewById(R.id.tv_info2)).setText(bean.getInfo2());
        //邀请好友按钮
        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UShare.openShareImage(MyInvitationActivity.this, mBitmap);
            }
        });
    }

    @Override
    public void getFuncURL(final BannerProto.Ret_PBAPP_ads ret_pbapp_ads) {
        CCLog.e("用户账号：" + UserInfoSharePre.getMobile());
        CCLog.e("资金账号：" + UserInfoSharePre.getFundAccount());
        if (ret_pbapp_ads != null && ret_pbapp_ads.getReturnCode().equals("0000")) {
            if (ret_pbapp_ads.getData() != null && ret_pbapp_ads.getData().getAdsList().size() > 0) {
                if (StringUtils.isNotBlank(ret_pbapp_ads.getData().getAdsList().get(0).getFuncUrl())) {
                    String funcURL = ret_pbapp_ads.getData().getAdsList().get(0).getFuncUrl();
                    url = funcURL + "&recommendMobile=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getMobile()) + "&inviter=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getFundAccount());
                } else {
                    url = "https://wx.zjfae.com/index.php/regist?marketingChannel=0013&ano=new_zjzx_wx&recommendMobile=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getMobile()) + "&inviter=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getFundAccount());
                }
            } else {
                url = "https://wx.zjfae.com/index.php/regist?marketingChannel=0013&ano=new_zjzx_wx&recommendMobile=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getMobile()) + "&inviter=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getFundAccount());
            }
        } else {
            url = "https://wx.zjfae.com/index.php/regist?marketingChannel=0013&ano=new_zjzx_wx&recommendMobile=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getMobile()) + "&inviter=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getFundAccount());
        }
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "";
                if (ret_pbapp_ads != null && ret_pbapp_ads.getData().getAdsList().size() > 0 && StringUtils.isNotBlank(ret_pbapp_ads.getData().getAdsList().get(0).getShareTitle())) {
                    title = ret_pbapp_ads.getData().getAdsList().get(0).getShareTitle();
                } else {
                    title = "邀请好友共同开启智慧理财之路";
                }
                String content = "";
                if (ret_pbapp_ads != null && ret_pbapp_ads.getData().getAdsList().size() > 0 && StringUtils.isNotBlank(ret_pbapp_ads.getData().getAdsList().get(0).getShareContent())) {
                    content = ret_pbapp_ads.getData().getAdsList().get(0).getShareContent();
                } else {
                    content = "邀请好友共同开启智慧理财之路";
                }
                UShare.openShareUrl(MyInvitationActivity.this, title, content, url);
            }
        });
        Bitmap bitmap = QrCodeUtil.createQRCode(url, 500);
        img_download.setImageBitmap(bitmap);

    }

    //获取图片地址
    private void initShareBitmap() {
        ApiServer apiServer = ApiRetrofit.getInstance().getApiService();
        String shareUrl = BASE_URL + "mzj/pbimg.do?fh=VIMGMZJ000000J00&type=6&p=android&source=app&recommendMobile=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getMobile()) + "&inviter=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getFundAccount());
        apiServer.downloadPicFromNet(shareUrl).subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody value) {
                        byte[] bys = new byte[0];
                        try {
                            bys = value.bytes();
                            mBitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                            if (mBitmap != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btn_invite.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        btn_invite.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }

                });
    }
}

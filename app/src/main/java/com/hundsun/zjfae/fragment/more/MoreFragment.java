package com.hundsun.zjfae.fragment.more;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.more.ContactUsActivity;
import com.hundsun.zjfae.activity.more.FollowUsActivity;
import com.hundsun.zjfae.activity.more.MyShareActivity;
import com.hundsun.zjfae.activity.scan.activity.ScanActivity;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.fragment.BaseFragment;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gensazj.UrlParams;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.fragment
 * @ClassName: MoreFragment
 * @Description: 更多Fragment
 * @Author: moran
 * @CreateDate: 2019/6/10 13:49
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/10 13:49
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MoreFragment extends BaseFragment implements MoreView, MoreAdapter.OnItemClickListener {
    private MorePresenter mPresenter;
    private RecyclerView recyclerView;
    private MoreAdapter mAdapter;
    private List<UrlParams.PBAPP_urlparams.listMore> mList = new ArrayList<>();
    private String brokerNo = "";


    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MorePresenter(this);
    }

    @Override
    protected void resetLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.more_layout);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_fragment_layout;
    }

    @Override
    protected void initWidget() {
        initView();
    }

    @Override
    public void initData() {

        if (mPresenter == null || !BaseActivity.isLogin) {
            return;
        }
        mPresenter.getMoreList();
    }

    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //打开侧边栏
        findViewById(R.id.img_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) getActivity()).showMenu();
            }
        });
        findViewById(R.id.message_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(getActivity(), ScanActivity.class);
            }
        });

    }

    @Override
    public void loadData(UrlParams.Ret_PBAPP_urlparams data) {
        mList = data.getData().getListList();
        mAdapter = new MoreAdapter(getActivity(), mList);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void getBrokerNo(String brokerNo) {
        this.brokerNo = brokerNo;
        mPresenter.getFeedbackUrl();
    }

    @Override
    public void getFeedbackUrl(String url, String isShare) {
        if (!brokerNo.equals("")) {
            url = url + "?brokerNo=" + brokerNo;
        }
        ShareBean shareBean = new ShareBean();
        shareBean.setFuncUrl(url);
        shareBean.setShareItem("0");
        shareBean.setIsShare(isShare);

        mActivity.startWebActivity(shareBean);
    }

    @Override
    public void onItemClick(int position) {
        //分享好友
        if (mList.get(position).getKeyWord().equals("FriendsShare")) {
            Intent intent = new Intent(getActivity(), MyShareActivity.class);
            startActivity(intent);
        }
        //联系我们
        else if (mList.get(position).getKeyWord().equals("ContactUs")) {
            Intent intent = new Intent(getActivity(), ContactUsActivity.class);
            startActivity(intent);
        }
        //给我鼓励
        else if (mList.get(position).getKeyWord().equals("GiveMeEncouragement")) {
            try {
                Intent comment = new Intent("android.intent.action.VIEW");
                comment.setData(Uri.parse("market://details?id=" + getActivity().getApplicationContext().getPackageName()));
                startActivity(comment);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                showToast("手机上未检测到应用商店，请先下载应用商店或用浏览器打开应用市场页面搜索浙金中心进行评论");
            }
        }
        //关注我们
        else if (mList.get(position).getKeyWord().equals("PayAttentionToOur")) {
            Intent intent = new Intent(getActivity(), FollowUsActivity.class);
            baseStartActivity(intent);

        }
        //当前版本
        else if (mList.get(position).getKeyWord().equals("TheCurrentersion")) {


        }
        //意见反馈feedback
        else if (mList.get(position).getKeyWord().equals("feedback")) {
            mPresenter.getBrokerNo();

        } else {

            if (StringUtils.isNotBlank(mList.get(position).getBackendUrl())) {
                ShareBean shareBean = new ShareBean();
                shareBean.setFuncUrl(mList.get(position).getBackendUrl());
                shareBean.setShareItem("0");
                shareBean.setIsShare(mList.get(position).getIsShare());
                mActivity.startWebActivity(shareBean);
            }
        }
    }

    @Override
    public boolean isShowLoad() {
        return false;
    }
}

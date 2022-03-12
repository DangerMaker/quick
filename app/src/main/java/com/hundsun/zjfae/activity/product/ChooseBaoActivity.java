package com.hundsun.zjfae.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.adapter.ChooseBaoAdapter;
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean;
import com.hundsun.zjfae.activity.product.bean.RadEnvelopeBean;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.activity.productreserve.bean.ReserveProductPlay;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.MoneyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.product
 * @ClassName:      ChooseBaoActivity
 * @Description:     红包选择界面
 * @Author:         moran
 * @CreateDate:     2019/6/19 16:26
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/19 16:26
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class ChooseBaoActivity extends BasicsActivity implements View.OnClickListener, ChooseBaoAdapter.ItemOnClick {
    private RecyclerView choose_recycler_view;

    private static final int RESULT_CODE = 0x759;

    private TextView number_tv;


    private List<RadEnvelopeBean> baoList;

    private ChooseBaoAdapter adapter;

    private HashSet<HashMap> hashSet;



    private ProductPlayBean playInfo;
    /**
     * 转让实体类
     * */
    private TransferDetailPlay transferDetailBean;
    /**
     * 卡券信息实体类
     * */
    private PlayBaoInfo playBaoInfo;
    /**
     * 预约购买实体类
     * */
    private ReserveProductPlay reserveProductPlay;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_bao;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.choose_bao_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    public void initView() {
        setTitle("选择红包");
        setNoBack();
        choose_recycler_view = findViewById(R.id.choose_recycler_view);
        findViewById(R.id.cancel).setOnClickListener(this);
        number_tv = findViewById(R.id.number_tv);
        findViewById(R.id.finish_bao).setOnClickListener(this);
    }

    @Override
    public void initData() {
        hashSet = new HashSet<>();
        Intent intent = getIntent();
        List<HashMap> playList = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra("baoBundle");
        Bundle transferBundle = intent.getBundleExtra("transferBaoBundle");
        Bundle reserveBundle = intent.getBundleExtra("reserveProductBaoBundle");
        baoList = new ArrayList<>();
        if (bundle != null) {
            playInfo = bundle.getParcelable("playBao");
            baoList = playInfo.getRadEnvelopeList();
            playBaoInfo = playInfo.getPlayBaoInfo();

        }
        else if (transferBundle != null) {
            transferDetailBean = transferBundle.getParcelable("playBao");
            baoList = transferDetailBean.getRadEnvelopeList();
            playBaoInfo = transferDetailBean.getPlayBaoInfo();

        }
        else if (reserveBundle != null) {
            reserveProductPlay = reserveBundle.getParcelable("playBao");
            baoList = reserveProductPlay.getRadEnvelopeList();
            playBaoInfo = reserveProductPlay.getPlayBaoInfo();
        }

        if (playBaoInfo != null) {
            playList.addAll(playBaoInfo.getPlayList());
        }
        else {
            playBaoInfo = new PlayBaoInfo();
        }
        adapter = new ChooseBaoAdapter(this, baoList);
        adapter.setItemOnClick(this);
        choose_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        choose_recycler_view.setAdapter(adapter);
        if (playList != null && !playList.isEmpty()) {
            //循环适配
            for (Map map : playList) {
                String quanDetailsId = (String) map.get("id");
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id", (String) map.get("id"));
                hashMap.put("type", (String) map.get("type"));
                hashMap.put("quanCanStack", (String) map.get("quanCanStack"));
                hashMap.put("value", (String) map.get("value"));
                hashSet.add(hashMap);
                adapter.setQuanDetailsId(quanDetailsId);
            }
            number_tv.setText("已选" + playList.size() + "张");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.finish_bao:
                finishQuan();
                break;
            default:
                break;
        }
    }


    private void finishQuan() {
        Intent data = new Intent();
        Bundle dataBundle = new Bundle();
        if (playInfo != null) {
            playInfo.setPlayBaoInfo(playBaoInfo);
            dataBundle.putParcelable("playBao", playInfo);
        } else if(transferDetailBean!=null){
            transferDetailBean.setPlayBaoInfo(playBaoInfo);
            dataBundle.putParcelable("playBao", transferDetailBean);
        }else if(reserveProductPlay!=null){
            reserveProductPlay.setPlayBaoInfo(playBaoInfo);
            dataBundle.putParcelable("playBao", reserveProductPlay);
        }

        data.putExtra("baoBundle", dataBundle);
        setResult(RESULT_CODE, data);
        finish();
    }

    @Override
    public void onItemClick(int position) {
        boolean selectedType = adapter.getSelectedType(baoList.get(position).getQuanDetailsId());


        //当前选择红包金额
        String positionAmount = baoList.get(position).getQuanValue();
        //卡券类型
        String type = baoList.get(position).getQuanType();
        //卡券id
        String id = baoList.get(position).getQuanDetailsId();
        //是否可叠加
        String quanCanStack = baoList.get(position).getQuanCanStack();

        HashMap<Object, Object> playMap = new HashMap();
        playMap.put("value", positionAmount);
        playMap.put("type", type);
        playMap.put("id", id);
        playMap.put("quanCanStack", quanCanStack);
        if (!selectedType) {
            //判断金额是否足够
            String playAmount = "0.00";
            if (playInfo != null) {
                playAmount = playInfo.getPlayAmount();
            } else if (transferDetailBean != null) {
                playAmount = transferDetailBean.getTotalAmount();
            } else if (reserveProductPlay != null) {
                playAmount = reserveProductPlay.getTotalAmount();
            }

            if (hashSet != null && !hashSet.isEmpty()) {
                //与上一个比对
                List<HashMap> playList = new ArrayList<>(hashSet);
                String canStack = (String) playList.get(playList.size() - 1).get("quanCanStack");
                if (!canStack.equals(quanCanStack) || canStack.equals("0")) {
                    showDialog("不可叠加红包只能单独使用");
                    return;
                }

                //已选红包+卡券金额
                String allBaoAmount =  PlayBaoInfo.all_quan_bao_Amount(playBaoInfo);;

                if (MoneyUtil.moneyCompare(positionAmount,playAmount)){

                    showDialog("您当前勾选的红包或卡券可足额抵扣支付，请先取消原有其他勾选红包或卡券，再进行操作。");
                    return;
                }


                if (MoneyUtil.moneyCompare(allBaoAmount,playAmount)){
                    showDialog("您当前已选择的红包或卡券可足额抵扣支付，无需再叠加红包或卡券使用；若需重新选择，请先取消当前已勾选红包或卡券。");
                    return;
                }




            }
            else {
                //当前红包金额与当前支付金额比较，true足够支付，false不够支付
                boolean isPlay = MoneyUtil.moneyCompare(positionAmount,playAmount);

                boolean quan_type = false;
                //获取选择卡券数量
                HashMap quanMap = playBaoInfo.getPlayMap();

                if (quanMap != null && !quanMap.isEmpty()){


                    String quanType = (String) quanMap.get("type");
                    //排除加息券
                    if (!quanType.equals("A") ) {
                        quan_type = true;
                    }
                    else {
                        quan_type = false;
                    }
                }
                else {
                    quan_type = false;
                }
                if (isPlay && quan_type){
                    showDialog("您当前勾选的红包或卡券可足额抵扣支付，请先取消原有其他勾选红包或卡券，再进行操作。");
                    return;
                }

            }
            adapter.setQuanDetailsId(id);
            adapter.notifyItemChanged(position);
            hashSet.add(playMap);

        }
        else {
            hashSet.remove(playMap);
            adapter.removeQuanDetailsId(id);
            adapter.notifyItemChanged(position);
        }
        List<HashMap> playList = new ArrayList<>(hashSet);
        playBaoInfo.setPlayList(playList);
        number_tv.setText("已选" + playList.size() + "张");

    }
}

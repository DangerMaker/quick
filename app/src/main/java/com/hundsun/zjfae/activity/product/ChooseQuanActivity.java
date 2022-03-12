package com.hundsun.zjfae.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.adapter.ChooseQuanAdapter;
import com.hundsun.zjfae.activity.product.bean.CardVoucherBean;
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.activity.productreserve.bean.ReserveProductPlay;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.MoneyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  @ProjectName:   浙金中心
 * @Package:        com.hundsun.zjfae.activity.product
 * @ClassName:      ChooseQuanActivity
 * @Description:     选择卡券界面
 * @Author:         moran
 * @CreateDate:     2019/6/10 13:40
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/10 13:40
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class ChooseQuanActivity extends BasicsActivity implements ChooseQuanAdapter.ItemOnClick, View.OnClickListener {


    private List<CardVoucherBean> cardVoucherList;

    private ChooseQuanAdapter adapter;

    private RecyclerView choose_recycler_view;

    private HashMap<String, Object> playMap;

    private static final int RESULT_CODE = 0x758;


    private TextView number_tv;

    /**
     * 购买实体类
     * */
    private ProductPlayBean playInfo;

    /**
     * 转让实体类
     * */
    private TransferDetailPlay transferDetailBean;

    /**
     * 卡券实体类
     * */
    private PlayBaoInfo playBaoInfo;

    /**
     * 预约购买实体类
     * */
    private ReserveProductPlay reserveProductPlay;


    private List<HashMap> playList;

    /**
     * 支付金额
     * */
    String playAmount = "0.00";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_quan;
    }

    @Override
    public void initView() {
        setTitle("选择优惠券");
        setNoBack();
        choose_recycler_view = findViewById(R.id.choose_recycler_view);
        findViewById(R.id.cancel).setOnClickListener(this);
        number_tv = findViewById(R.id.number_tv);
        findViewById(R.id.finish_quan).setOnClickListener(this);
    }

    @Override
    public void initData() {

        Intent intent = getIntent();
        //购买列表
        Bundle bundle = intent.getBundleExtra("quanBundle");
        Bundle transferBundle = intent.getBundleExtra("transferQuanBundle");
        Bundle reserveBundle = intent.getBundleExtra("reserveProductQuanBundle");
        cardVoucherList = new ArrayList<>();
        if (bundle != null) {
            playInfo = bundle.getParcelable("playQuan");
            cardVoucherList = playInfo.getCardVoucherList();
            playBaoInfo = playInfo.getPlayBaoInfo();
            playAmount = playInfo.getPlayAmount();
            if (playBaoInfo != null) {
                playMap = playInfo.getPlayBaoInfo().getPlayMap();
            }
            else {
                playBaoInfo = new PlayBaoInfo();
            }

        }
        else if (transferBundle != null) {
            transferDetailBean = transferBundle.getParcelable("playQuan");

            cardVoucherList = transferDetailBean.getCardVoucherList();
            playBaoInfo = transferDetailBean.getPlayBaoInfo();
            playAmount = transferDetailBean.getTotalAmount();
            if (playBaoInfo != null) {
                playMap = transferDetailBean.getPlayBaoInfo().getPlayMap();

            }
            else {
                playBaoInfo = new PlayBaoInfo();
            }
        }
        else if(reserveBundle!=null){
            reserveProductPlay = reserveBundle.getParcelable("playQuan");
            cardVoucherList = reserveProductPlay.getCardVoucherList();
            playBaoInfo = reserveProductPlay.getPlayBaoInfo();
            playAmount = reserveProductPlay.getTotalAmount();
            if (playBaoInfo != null) {
                playMap = reserveProductPlay.getPlayBaoInfo().getPlayMap();

            }
            else {
                playBaoInfo = new PlayBaoInfo();
            }
        }



        adapter = new ChooseQuanAdapter(this, cardVoucherList);
        adapter.setItemOnClick(this);
        choose_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        choose_recycler_view.setAdapter(adapter);
        if (playMap != null && !playMap.isEmpty()) {
            String quanDetailsId = (String) playMap.get("id");
            adapter.setQuanDetailsId(quanDetailsId);
            adapter.setCheck(true);
            number_tv.setText("已选1张");
        } else {
            playMap = new HashMap();
        }

    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.choose_quan_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onItemClick(int position, boolean isCheck) {

        //金额
        String positionAmount = cardVoucherList.get(position).getQuanValue();
        //卡券类型
        String type = cardVoucherList.get(position).getQuanType();
        //卡券id
        String id = cardVoucherList.get(position).getQuanDetailsId();
        String kqAddRatebj = cardVoucherList.get(position).getEnableIncreaseInterestAmount();
        if (isCheck) {
            String allBaoAmount =  PlayBaoInfo.all_quan_bao_Amount(playBaoInfo);
            //判断金额是否足够
            //"A":加息券,"L":抵用券,"F":满减券
            //已选红包+卡券

            if (!type.equals("A")){

                if (MoneyUtil.moneyCompare(positionAmount,playAmount)){

                    showDialog("您当前勾选的红包或卡券可足额抵扣支付，请先取消原有其他勾选红包或卡券，再进行操作。");

                }
                else if (MoneyUtil.moneyCompare(allBaoAmount,playAmount)){

                    showDialog("您当前已选择的红包或卡券可足额抵扣支付，无需再叠加红包或卡券使用；若需重新选择，请先取消当前已勾选红包或卡券。");
                }
                else {
                    //先减去选中的满减券或抵用券，判断是否足额，不足额加上
                    if (!playMap.isEmpty()){
                        String mapType = (String) playMap.get("type");
                        //"A":加息券,"L":抵用券,"F":满减券
                        if (mapType != null && !mapType.equals("A")){
                            //选取的抵用券金额或满减券金额
                            String mapAmount = (String) playMap.get("value");

                            String subAmount =  MoneyUtil.moneySub(playAmount,mapAmount);

                            if (MoneyUtil.moneyCompare(subAmount,playAmount)){

                                showDialog("您当前已选择的红包或卡券可足额抵扣支付，无需再叠加红包或卡券使用；若需重新选择，请先取消当前已勾选红包或卡券。");
                            }
                            else {
                                playMap.clear();
                                adapter.setQuanDetailsId(cardVoucherList.get(position).getQuanDetailsId());
                                number_tv.setText("已选1张");
                                playMap.put("value", positionAmount);
                                playMap.put("type", type);
                                playMap.put("id", id);
                                playMap.put("kqAddRatebj", kqAddRatebj);
                            }
                        }
                        else {
                            playMap.clear();
                            adapter.setQuanDetailsId(cardVoucherList.get(position).getQuanDetailsId());
                            number_tv.setText("已选1张");
                            playMap.put("value", positionAmount);
                            playMap.put("type", type);
                            playMap.put("id", id);
                            playMap.put("kqAddRatebj", kqAddRatebj);
                        }
                    }
                    else {
                        playMap.clear();
                        adapter.setQuanDetailsId(cardVoucherList.get(position).getQuanDetailsId());
                        number_tv.setText("已选1张");
                        playMap.put("value", positionAmount);
                        playMap.put("type", type);
                        playMap.put("id", id);
                        playMap.put("kqAddRatebj", kqAddRatebj);
                    }
                }
            }
            else {
                playMap.clear();
                playMap.put("value", positionAmount);
                playMap.put("type", type);
                playMap.put("id", id);
                playMap.put("kqAddRatebj", kqAddRatebj);
                adapter.setQuanDetailsId(cardVoucherList.get(position).getQuanDetailsId());
                number_tv.setText("已选1张");

            }

        } else {
            adapter.setQuanDetailsId("");
            number_tv.setText("已选0张");
            playMap.clear();

        }
        playBaoInfo.setPlayMap(playMap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.finish_quan:
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
            dataBundle.putParcelable("playQuan", playInfo);
        } else if(transferDetailBean!=null) {
            transferDetailBean.setPlayBaoInfo(playBaoInfo);
            dataBundle.putParcelable("playQuan", transferDetailBean);
        } else if(reserveProductPlay!=null) {
            reserveProductPlay.setPlayBaoInfo(playBaoInfo);
            dataBundle.putParcelable("playQuan", reserveProductPlay);
        }

        data.putExtra("quanBundle", dataBundle);
        setResult(RESULT_CODE, data);
        finish();
    }
}

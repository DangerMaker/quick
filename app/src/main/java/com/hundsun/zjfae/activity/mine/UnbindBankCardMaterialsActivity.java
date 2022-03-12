package com.hundsun.zjfae.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.adapter.SlideAdapter;
import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.home.bean.ImageUploadStateBean;
import com.hundsun.zjfae.activity.home.bean.UpLoadPicImageBean;
import com.hundsun.zjfae.activity.mine.adapter.UnbindReasonAdapter;
import com.hundsun.zjfae.activity.mine.bean.ConfigurationUtils;
import com.hundsun.zjfae.activity.mine.presenter.UnbindBankCardPresenter;
import com.hundsun.zjfae.activity.mine.view.UnbindBankCardView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.user.ChangeCard;
import com.hundsun.zjfae.common.user.UnBindCard;
import com.hundsun.zjfae.common.view.popwindow.MaterialsUploadedPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UnbindBankCardMaterialsActivity extends CommActivity<UnbindBankCardPresenter> implements View.OnClickListener,UnbindBankCardView {
    private RecyclerView slide_recycler;
    private SlideAdapter adapter;
    private CheckBox check_image;
    private Button part_button;
    private LinearLayout unbind_reason_layout;
    private TextView unbind_shade,instructions;

    private List<UpLoadPicImageBean> imageBeanList;

    private UnbindReasonAdapter mUnbindReasonAdapter;
    private BottomSheetDialog MaterialsUploadedDialog = null;


    ////cardReason = cancel
    //    //reasonDetails = 银行卡已注销
    private String cardReason =  "cancel", reasonDetails ="银行卡已注销";

    private EditText other_ed;
    private int index = 0;

    private static final int ITEM_INDEX = 0x701;

    private  String partValue = "";

    ConfigurationUtils faceUtils = null;

    private List<ImageUploadStateBean> stateBeanList = new ArrayList<>();
    @Override
    public void requestImageUpload(ImageUploadBean uploadBean, int index) {
        if (uploadBean.getReturnCode().equals("0000")){
            adapter.setIsSelect(index);
            adapter.setUploading(index);
            ImageUploadStateBean stateBean = new ImageUploadStateBean();
            stateBean.setFilePath(uploadBean.getFilePath());
            stateBean.setFileName(uploadBean.getFileNamesp());
            stateBean.setModel(imageBeanList.get(index).getModel());
            stateBeanList.add(stateBean);
            index = index +1;

            if (index == imageBeanList.size() ){

                //解绑
                if (faceUtils.getVerifyscene().equals("unbindCard")){

                    presenter.unbindBankCardUpload(stateBeanList,cardReason,reasonDetails,partValue);
                }
                //换卡
                else {
                    presenter.changeBankCardUpload(stateBeanList,partValue);
                }

            }
            else {
                File file = new File(imageBeanList.get(index).getImagePath());
                presenter.unbindBankCard(file,index,partValue);

            }
        }
        else {
            hideLoading();
            showDialog(uploadBean.getReturnMsg(), "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        }



    }



    //解绑卡
    @Override
    public void requestUnbindBankCard(String returnCode, String returnMsg) {
        hideLoading();
        if (returnCode.equals("0000")){

            returnMsg = "您的解绑申请已成功提交，中心将于3个工作日内为您处理。";

            UnBindCard.removeAll();
            FileUtil.deleteFile(FileUtil.getUnBindCard(this));

            showDialog(returnMsg, "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    HomeActivity.show(UnbindBankCardMaterialsActivity.this,HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                    finish();
                }
            });
        }
        else {
            showDialog(returnMsg);
        }
    }

    //换卡
    @Override
    public void requestChangeBankCard(String returnCode, String returnMsg) {
        hideLoading();
        if (returnCode.equals("0000")){
            returnMsg = "您的换卡申请已成功提交，中心将于3个工作日内为您处理。";
            ChangeCard.removeAll();
            FileUtil.deleteFile(FileUtil.getChangeCard(this));
            showDialog(returnMsg, "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    HomeActivity.show(UnbindBankCardMaterialsActivity.this,HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                    finish();
                }
            });
        }
        else {
            showDialog(returnMsg);
        }
    }


    @Override
    public void showLoading() {
        super.showLoading();
        adapter.setUploading(0);
        if (stateBeanList != null && !stateBeanList.isEmpty()){
            stateBeanList.clear();
        }
        else {
            stateBeanList = new ArrayList<>();
        }

    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        adapter.setUploading(-1);
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        adapter.setUploading(-1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back:
                finish();
                break;
            case R.id.part_button:
                if (check_image.isChecked()){
                    if (cardReason.equals("other")){
                        if (Utils.isViewEmpty(other_ed)){
                            showDialog("请输入您的解绑原因");
                            return;
                        }
                        else {
                            reasonDetails = other_ed.getText().toString();
                        }
                    }

                    File file = new File(imageBeanList.get(index).getImagePath());
                    showLoading();
                    presenter.unbindBankCard(file,index,partValue);
                }
                break;
            case R.id.check_layout:
                if (!check_image.isChecked()){
                    check_image.setChecked(true);
                    part_button.setBackgroundResource(R.drawable.product_buy_clickable);
                }
                else {
                    check_image.setChecked(false);
                    part_button.setBackgroundResource(R.drawable.product_buy);
                }
                break;
            case R.id.unbind_reason_layout://解绑原因
                MaterialsUploadedDialog.show();
                break;
        }
    }

    @Override
    public void initData() {


        Bundle bundle = getIntent().getBundleExtra("faceBundle");

        if (bundle != null){
            faceUtils = bundle.getParcelable("face");

        }

        if (faceUtils != null){
            partValue = faceUtils.getPartValue();
            CCLog.e("partValue",partValue);
            //申请解绑
            if (faceUtils.getVerifyscene().equals("unbindCard")){

                instructionsType("解绑");

                List<UnBindCard> unBindCardList = UnBindCard.getUnBindCardAllData();

                for (UnBindCard unBindCard : unBindCardList){
                    UpLoadPicImageBean upLoadPicImageBean = new UpLoadPicImageBean();
                    upLoadPicImageBean.setId(unBindCard.id);
                    upLoadPicImageBean.setModel(unBindCard.model);
                    upLoadPicImageBean.setDynamicKey(unBindCard.dynamicKey);
                    upLoadPicImageBean.setDynamicValue(unBindCard.dynamicValue);
                    upLoadPicImageBean.setImagePath(unBindCard.imagePath);
                    imageBeanList.add(upLoadPicImageBean);
                }

                adapter = new SlideAdapter(this,imageBeanList);
                slide_recycler.setAdapter(adapter);
                adapter.setItemDeleteClickListener(new SlideAdapter.ItemDeleteClickListener() {
                    @Override
                    public void onItemDelete(int i) {
                        imageBeanList.remove(i);
                        adapter.rest(imageBeanList);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onItemOnly() {
                        showDialog("不可删除，至少上传一个材料");
                    }
                });
                adapter.setItemOnClickListener(new SlideAdapter.ItemOnClickListener() {
                    @Override
                    public void onItemClick(int i) {

                        Intent intent = new Intent();
                        intent.putExtra("index",i);
                        setResult(ITEM_INDEX,intent);
                        finish();
                    }
                });
            }
            else {
                instructionsType("换卡");
                unbind_reason_layout.setVisibility(View.GONE);



                List<ChangeCard> changeCardList = ChangeCard.getChangeCardAllData();

                for (ChangeCard changeCard : changeCardList){
                    UpLoadPicImageBean upLoadPicImageBean = new UpLoadPicImageBean();
                    upLoadPicImageBean.setId(changeCard.id);
                    upLoadPicImageBean.setModel(changeCard.model);
                    upLoadPicImageBean.setDynamicKey(changeCard.dynamicKey);
                    upLoadPicImageBean.setDynamicValue(changeCard.dynamicValue);
                    upLoadPicImageBean.setImagePath(changeCard.imagePath);
                    imageBeanList.add(upLoadPicImageBean);
                }

                adapter = new SlideAdapter(this,imageBeanList);
                slide_recycler.setAdapter(adapter);
                adapter.setItemDeleteClickListener(new SlideAdapter.ItemDeleteClickListener() {
                    @Override
                    public void onItemDelete(int i) {
                        imageBeanList.remove(i);
                        adapter.rest(imageBeanList);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onItemOnly() {
                        showDialog("不可删除，至少上传一个材料");
                    }
                });
                adapter.setItemOnClickListener(new SlideAdapter.ItemOnClickListener() {
                    @Override
                    public void onItemClick(int i) {

                        Intent intent = new Intent();
                        intent.putExtra("index",i);
                        setResult(ITEM_INDEX,intent);
                        finish();
                    }
                });
                adapter.setItemOnClickListener(new SlideAdapter.ItemOnClickListener() {
                    @Override
                    public void onItemClick(int i) {

                        Intent intent = new Intent();
                        intent.putExtra("index",i);
                        setResult(ITEM_INDEX,intent);
                        finish();
                    }
                });

            }

        }







    }


    private void instructionsType(String type){

        StringBuffer buffer = new StringBuffer("本人郑重申明为申请“");
        buffer.append(type).append("”").append("所上传的银行卡相关证明为现在真实存在的材料，本人愿为上传的银行卡相关证明的真实性负责。");
        instructions.setText(buffer.toString());
    }


    @Override
    public void initView() {
        slide_recycler = findViewById(R.id.slide_recycler);
        slide_recycler.addItemDecoration(new DividerItemDecorations());
        slide_recycler.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.back).setOnClickListener(this);
        part_button = findViewById(R.id.part_button);
        part_button.setOnClickListener(this);
        findViewById(R.id.check_layout).setOnClickListener(this);
        check_image = findViewById(R.id.check_image);
        findViewById(R.id.unbind_reason_layout).setOnClickListener(this);
        unbind_reason_layout = findViewById(R.id.unbind_reason_layout);
        unbind_reason_layout.setOnClickListener(this);
        unbind_shade = findViewById(R.id.unbind_shade);
        other_ed = findViewById(R.id.other_ed);
        instructions = findViewById(R.id.instructions);
        final RecyclerView recyclerView = new RecyclerView(UnbindBankCardMaterialsActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUnbindReasonAdapter = new UnbindReasonAdapter(this);
        mUnbindReasonAdapter.setItemOnClickListener(new UnbindReasonAdapter.ItemOnClickListener() {
            @Override
            public void onItemClick(String unbind_cardReason,String unbind_reasonDetails) {
                cardReason = unbind_cardReason;
                reasonDetails = unbind_reasonDetails;

                if (cardReason.equals("other")){
                    other_ed.setVisibility(View.VISIBLE);
                }
                else {
                    other_ed.setVisibility(View.GONE);
                }

                unbind_shade.setText(reasonDetails);
                MaterialsUploadedDialog.dismiss();
            }
        });
        recyclerView.setAdapter(mUnbindReasonAdapter);

        MaterialsUploadedDialog = new BottomSheetDialog(this);
        MaterialsUploadedDialog.setContentView(recyclerView);



        imageBeanList = new ArrayList<>();
        mCustomProgressDialog = getCustomProgressDialog(this,"开始上传中");
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.unbind_card_layout);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected UnbindBankCardPresenter createPresenter() {
        return  new UnbindBankCardPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unbind_bank_card_materials;
    }

}

package com.hundsun.zjfae.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.adapter.SlideAdapter;
import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.home.bean.ImageUploadStateBean;
import com.hundsun.zjfae.activity.home.bean.UpLoadPicImageBean;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.user.AssetProof;
import com.hundsun.zjfae.activity.home.presenter.ImageUploadPresenter;
import com.hundsun.zjfae.activity.home.view.ImageUploadView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HighNetWorthUploadActivity extends CommActivity<ImageUploadPresenter> implements View.OnClickListener,ImageUploadView {

    private RecyclerView slide_recycler;
    private SlideAdapter adapter;
    private TextView compile_tv;
    private CheckBox check_image;
    private Button part_button;
    private int index = 0;

    private  List<UpLoadPicImageBean> loadPicImageBeanList;
    private static final int ITEM_INDEX = 0x701;
    @Override
    public void initView() {
        slide_recycler = findViewById(R.id.slide_recycler);
        slide_recycler.addItemDecoration(new DividerItemDecorations());
        slide_recycler.setLayoutManager(new LinearLayoutManager(this));
        compile_tv = findViewById(R.id.compile_tv);
        compile_tv.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        part_button = findViewById(R.id.part_button);
        part_button.setOnClickListener(this);
        findViewById(R.id.check_layout).setOnClickListener(this);
        findViewById(R.id.clean_button).setOnClickListener(this);
        check_image = findViewById(R.id.check_image);
        mCustomProgressDialog = getCustomProgressDialog(this,"上传中");
    }


    @Override
    protected ImageUploadPresenter createPresenter() {
        return  new ImageUploadPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_upload;
    }

    @Override
    public void initData() {
        List<AssetProof> proofList  = AssetProof.getAssetProofAllData();
        loadPicImageBeanList = new ArrayList<>();

        for (AssetProof assetProof : proofList){
            if (assetProof.imagePath != null && !assetProof.imagePath.equals("")){
                UpLoadPicImageBean upLoadPicImageBean = new UpLoadPicImageBean();
                upLoadPicImageBean.setId(assetProof.id);
                upLoadPicImageBean.setModel(assetProof.model);
                upLoadPicImageBean.setDynamicKey(assetProof.dynamicKey);
                upLoadPicImageBean.setDynamicValue(assetProof.dynamicValue);
                upLoadPicImageBean.setImagePath(assetProof.imagePath);
                loadPicImageBeanList.add(upLoadPicImageBean);
            }

        }

        adapter = new SlideAdapter(this,loadPicImageBeanList);
        slide_recycler.setAdapter(adapter);

        adapter.setItemDeleteClickListener(new SlideAdapter.ItemDeleteClickListener() {
            @Override
            public void onItemDelete(int i) {
                AssetProof assetProof = new AssetProof();
                assetProof.id = i +1;
                assetProof.dynamicKey = loadPicImageBeanList.get(i).getDynamicKey();
                assetProof.dynamicValue = loadPicImageBeanList.get(i).getDynamicValue();
                assetProof.model = loadPicImageBeanList.get(i).getModel();
                assetProof.imagePath = loadPicImageBeanList.get(i).getImagePath();
                adapter.notifyItemRemoved(i);
                AssetProof.remove(assetProof);
                loadPicImageBeanList.remove(i);
                adapter.setSelected(-2);
                upData();
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


    private void upData(){
        CCLog.e("loadPicImageBeanList",loadPicImageBeanList.size());
        AssetProof.removeAll();
        for (int i = 0; i < loadPicImageBeanList.size(); i++) {
            AssetProof assetProof = new AssetProof();
            assetProof.id = i;
            assetProof.dynamicKey = loadPicImageBeanList.get(i).getDynamicKey();
            assetProof.dynamicValue = loadPicImageBeanList.get(i).getDynamicValue();
            assetProof.model = loadPicImageBeanList.get(i).getModel();
            assetProof.imagePath = loadPicImageBeanList.get(i).getImagePath();
            AssetProof.putData(assetProof);
        }
    }



    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.image_upload_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.compile_tv:
                if (compile_tv.getText().toString().equals("编辑")){
                    compile_tv.setText("完成");
                    if (adapter != null){
                        adapter.setSelected(0);
                    }
                }
                else if (compile_tv.getText().toString().equals("完成")){
                    compile_tv.setText("编辑");
                    if (adapter != null){
                        adapter.setSelected(-1);
                    }
                }
                break;

            case R.id.back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.part_button:
                if (check_image.isChecked()){
                    showLoading();
                    File file = new File(loadPicImageBeanList.get(index).getImagePath());
                    presenter.highNetWorth(file,index);
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
            case R.id.clean_button:
                showDialog("您确定取消上传？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        AssetProof.removeAll();
                        FileUtil.deleteFile(FileUtil.getProof(HighNetWorthUploadActivity.this));
                        finish();
                    }
                });
                break;
            default:
                break;
        }
    }

    private List<ImageUploadStateBean> stateBeanList = new ArrayList<>();
    @Override
    public void requestImageUpload(ImageUploadBean uploadBean,int index) {
        if (uploadBean.getReturnCode().equals("0000")){
            adapter.setIsSelect(index);
            adapter.setUploading(index);
            ImageUploadStateBean stateBean = new ImageUploadStateBean();
            stateBean.setFilePath(uploadBean.getFilePath());
            stateBean.setFileName(uploadBean.getFileNamesp());
            stateBean.setModel(loadPicImageBeanList.get(index).getDynamicKey());
            stateBeanList.add(stateBean);
            index = index +1;
            if (index == loadPicImageBeanList.size() ){
                presenter.highNetWorthUpload(stateBeanList);
            }
            else {
                File file = new File(loadPicImageBeanList.get(index).getImagePath());
                presenter.highNetWorth(file,index);
            }
        }
        else {
            hideLoading();
            showDialog(uploadBean.getReturnMsg());
        }

    }

    @Override
    public void requestNetWorthUpload(String returnCode, String returnMsg) {
        hideLoading();
        if (returnCode.equals("m2119")){
            showDialog(returnMsg, "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        else if (returnCode.equals("0000")){
            showDialog("上传成功", "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AssetProof.removeAll();
                    FileUtil.deleteFile(FileUtil.getProof(HighNetWorthUploadActivity.this));
                    setResult(RESULT_OK);
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
        mCustomProgressDialog.show();
        adapter.setUploading(0);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        adapter.setUploading(-1);
        if (stateBeanList != null && !stateBeanList.isEmpty()){
            stateBeanList.clear();
        }
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        adapter.setUploading(-1);
        if (stateBeanList != null && !stateBeanList.isEmpty()){
            stateBeanList.clear();
        }
    }



}

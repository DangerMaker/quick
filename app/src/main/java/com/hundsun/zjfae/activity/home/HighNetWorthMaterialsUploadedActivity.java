package com.hundsun.zjfae.activity.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.adapter.MaterialsAdapter;
import com.hundsun.zjfae.activity.home.presenter.MaterialsUploadedPresenter;
import com.hundsun.zjfae.activity.home.view.MaterialsUploadedView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.AssetProof;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.UserAssetsInfo;
import onight.zjfae.afront.gensazj.DictDynamics;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 * @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.home
 * @ClassName:      HighNetWorthMaterialsUploadedActivity
 * @Description:     java类作用描述
 * @Author:         moran
 * @CreateDate:     2019/7/1 14:43
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/1 14:43
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
@RuntimePermissions
public class HighNetWorthMaterialsUploadedActivity extends CommActivity<MaterialsUploadedPresenter> implements View.OnClickListener,MaterialsUploadedView, TakePhoto.TakeResultListener, InvokeListener {

    private TextView amount;
    private ImageView photo_image;

    private WebView highNetWorthUpload;

    private TextView materials_tv;

    private TextView number_tv;

    private TextView required_tv;

    private TextView title;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private BottomSheetDialog bottomSheetDialog = null;

    private BottomSheetDialog MaterialsUploadedDialog = null;




    private Button bt_next;
    private TextView back_tv;

    private String isRealInvestor = "0";
    private boolean isRegister;

    private List<DictDynamics.PBAPP_dictDynamic.DictDynamic> dictDynamics;

    private  List<DictDynamics.PBAPP_dictDynamic.KeyAndValue> valueList;
    private int index = 1;

    private int position = 0;

    private MaterialsAdapter adapter ;
    private  RecyclerView recyclerView;

    private LinearLayout animation_layout;

    private  Animation translateAnimation;


    private static final int REQUEST_CODE = 0x7001;

    private static final int ITEM_INDEX = 0x701;

    private boolean isNext = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_tv:
                if (index > 1){
                    isNext = false;
                    animation_layout.startAnimation(translateAnimation);

                }
                else {
                    finish();
                }
                break;


            case R.id.photo_image:
                bottomSheetDialog.show();
                break;
            case R.id.photo:
                getTakePhoto().onPickMultiple(1);
                bottomSheetDialog.dismiss();
                break;
            case R.id.camera_image:

                HighNetWorthMaterialsUploadedActivityPermissionsDispatcher.onPickFromCaptureNeedWithPermissionCheck(this);


                bottomSheetDialog.dismiss();
                break;
            case R.id.dismiss_image:
                bottomSheetDialog.dismiss();
                break;

            case R.id.materials_tv:
                if (Utils.isViewEmpty(materials_tv)){
                    return;
                }
                MaterialsUploadedDialog.show();
                break;
            case R.id.bt_next:
                AssetProof proo = new AssetProof();
                proo.id = position;
                AssetProof proof = AssetProof.getAssetProofData(proo);

                if (proof.imagePath == null){
                    showDialog("请先选择照片");
                }
                else{
                    List<AssetProof> proofList =  AssetProof.getAssetProofAllData();
                    int assetsNumber = 0,assetsRela = 0;
                    for (AssetProof assetProof : proofList){
                        if (assetProof.imagePath == null || assetProof.imagePath.equals("null") || assetProof.imagePath.equals("")){
                            continue;
                        }

                        String rela = assetProof.dynamicKey;

                        if (rela.equals("rela")){
                            //关系证明+1
                            assetsRela = assetsRela+1;
                        }
                        else {
                            //资产证明+1
                            assetsNumber = assetsNumber+1;
                        }
                    }


                    if (bt_next.getText().toString().equals("完成")){

                        startActivityForResult(new Intent(this,HighNetWorthUploadActivity.class),REQUEST_CODE);
                    }

                    else if (assetsNumber >= 7 && index >= 7 ){
                        showDialog("您已上传了7张资产证明，请结束上传或者上传关系证明", "知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                back_tv.setText("上一步");
                                isNext = true;
                                animation_layout.startAnimation(translateAnimation);
                            }
                        });
                    }
                    else if (assetsRela >= 3 && index >= 3 ){
                        showDialog("您已上传了3张关系证明，请结束上传或者上传资产证明", "知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                back_tv.setText("上一步");
                                isNext = true;
                                animation_layout.startAnimation(translateAnimation);
                            }
                        });
                    }

                    else {
                        back_tv.setText("上一步");
                        isNext = true;
                        animation_layout.startAnimation(translateAnimation);
                    }

                }

                break;

            //完成
            case R.id.required_tv:
                startActivityForResult(new Intent(this,HighNetWorthUploadActivity.class),REQUEST_CODE);
                break;


            case R.id.explain:
                baseStartActivity(this,DescriptionActivity.class);
                break;
            default:
                break;

        }

    }



    @Override
    public void init(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo ret_pbife_fund_loadUserAssetsInfo, DictDynamics.Ret_PBAPP_dictDynamic dictDynamic) {
        String accredited = ret_pbife_fund_loadUserAssetsInfo.getData().getAccreditedDiff();
        if (!isRealInvestor.equals("1") && !accredited.equals("0")){
            amount.setVisibility(View.VISIBLE);
            amount.setText("您在中心的账户总额" + ret_pbife_fund_loadUserAssetsInfo.getData().getAmount() + "元，您还需要上传" + ret_pbife_fund_loadUserAssetsInfo.getData().getAccreditedDiff() + "元（含）以上的本人或家庭金融资产证明，才能成为合格投资者。");
        }
        else {
            amount.setVisibility(View.GONE);
        }
        dictDynamics = dictDynamic.getData().getDictDynamicListList();
        recyclerView = new RecyclerView(HighNetWorthMaterialsUploadedActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MaterialsAdapter(this);

        adapter.setItemOnClickListener(new MaterialsAdapter.ItemOnClickListener() {
            @Override
            public void onItemClick(int i) {

                materials_tv.setText(valueList.get(i).getDynamicValue());
                AssetProof proof = new AssetProof();
                proof.id = position;

                AssetProof assetProof = AssetProof.getAssetProofData(proof);
                assetProof.id = position;
                assetProof.model = dictDynamics.get(position).getModel();
                assetProof.dynamicKey =  valueList.get(i).getDynamicKey();
                assetProof.dynamicValue =  valueList.get(i).getDynamicValue();
                AssetProof.putData(assetProof);
                MaterialsUploadedDialog.dismiss();
            }
        });
        init();
        recyclerView.setAdapter(adapter);


        MaterialsUploadedDialog = new BottomSheetDialog(this);
        MaterialsUploadedDialog.setContentView(recyclerView);

    }


    private void init(){
        valueList = dictDynamics.get(position).getKeyValueListList();
        adapter.setValueList(valueList);
        number_tv.setText((index)+"");

        title.setText(dictDynamics.get(position).getTitle());


        String required = dictDynamics.get(position).getRequired();


        if (required.equals("1")) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }


        String bodyHTML = dictDynamics.get(position).getRemark();





        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);
        AssetProof proof = new AssetProof();
        proof.id = position;

        AssetProof assetProof = AssetProof.getAssetProofData(proof);

        if (assetProof.imagePath != null && assetProof.dynamicValue != null){
            //查到数据--展示数据内容
            materials_tv.setText(assetProof.dynamicValue);
            ImageLoad.getImageLoad().LoadImage(this,assetProof.imagePath,photo_image);
        }
        else {
            materials_tv.setText(valueList.get(0).getDynamicValue());
            ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
        }

    }



    private void nextButton(){
        ++index;
        ++position;
        if (index>= dictDynamics.size()){
            bt_next.setText("完成");
        }
        number_tv.setText((index)+"");
        String bodyHTML = dictDynamics.get(position).getRemark();
        String required =  dictDynamics.get(position).getRequired();
        valueList = dictDynamics.get(position).getKeyValueListList();
        adapter.setValueList(valueList);
        title.setText(dictDynamics.get(position).getTitle());
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);

        if (required.equals("1")) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }

        AssetProof proof = new AssetProof();
        proof.id = position;

        AssetProof assetProof = AssetProof.getAssetProofData(proof);

        if (assetProof.imagePath != null && assetProof.dynamicValue != null){
            //查到数据--展示数据内容
            materials_tv.setText(assetProof.dynamicValue);
            ImageLoad.getImageLoad().LoadImage(this,assetProof.imagePath,photo_image);
        }
        else {
            materials_tv.setText(valueList.get(0).getDynamicValue());
            ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
        }

    }






    public void backButton(){
        --index;
        --position;
        if (index <= 1){
            back_tv.setText("返回");
        }
        bt_next.setText("下一步");

        number_tv.setText(index+"");

        valueList = dictDynamics.get(position).getKeyValueListList();
        adapter.setValueList(valueList);
        String bodyHTML = dictDynamics.get(position).getRemark();
        String required =  dictDynamics.get(position).getRequired();
        title.setText(dictDynamics.get(position).getTitle());
        CCLog.e("required",required);
        if (required.equals("1")) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);



        AssetProof proof = new AssetProof();
        proof.id = position;

        AssetProof assetProof = AssetProof.getAssetProofData(proof);
        //没数据
        if (assetProof.imagePath == null && assetProof.dynamicValue == null){
            materials_tv.setText(valueList.get(0).getDynamicValue());
            ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
        }
        else {
            //查到数据--展示数据内容
            materials_tv.setText(assetProof.dynamicValue);
            ImageLoad.getImageLoad().LoadImage(this,assetProof.imagePath,photo_image);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED){
            rest();

        }
        //上传成功
        else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (isRegister){
                HomeActivity.show(this, HomeActivity.HomeFragmentType.HOME_FRAGMENT);
            }
            else {
                finish();
            }

        }
        else if (requestCode == REQUEST_CODE && resultCode == ITEM_INDEX){
            if (data != null){
                index = data.getIntExtra("index",0);
                position = index;
                index = index +1;
                item();
            }
        }

    }


    private void rest(){

        List<AssetProof> proofList = AssetProof.getAssetProofAllData();

        if (proofList.isEmpty()){

            index = 1;
            position = 0;
        }
        else {
            index = proofList.size();
            position = index - 1;
        }



        if (index>= dictDynamics.size()){

            bt_next.setText("完成");
        }
        else {
            bt_next.setText("下一步");
        }


        if (index <= 1){
            back_tv.setText("返回");
        }
        else {
            back_tv.setText("上一步");
        }
        number_tv.setText((index)+"");
        valueList = dictDynamics.get(position).getKeyValueListList();

        title.setText(dictDynamics.get(position).getTitle());

        adapter.setValueList(valueList);

        String bodyHTML = dictDynamics.get(position).getRemark();
        String required =  dictDynamics.get(position).getRequired();

        if (required.equals("1")) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);
        AssetProof proof = new AssetProof();
        proof.id = position;
        AssetProof assetProof = AssetProof.getAssetProofData(proof);
        //没数据
        if (assetProof.imagePath != null && assetProof.dynamicValue != null){

            //查到数据--展示数据内容
            materials_tv.setText(assetProof.dynamicValue);
            ImageLoad.getImageLoad().LoadImage(this,assetProof.imagePath,photo_image);

        }
        else {
            materials_tv.setText(valueList.get(0).getDynamicValue());
            ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
        }

    }


    private void item(){

        if (index <= 1){
            back_tv.setText("返回");
        }

        if (index>= dictDynamics.size()){

            bt_next.setText("完成");
        }
        else {
            bt_next.setText("下一步");
        }
        number_tv.setText((index)+"");
        valueList = dictDynamics.get(position).getKeyValueListList();

        adapter.setValueList(valueList);

        String bodyHTML = dictDynamics.get(position).getRemark();
        String required =  dictDynamics.get(position).getRequired();
        title.setText(dictDynamics.get(position).getTitle());
        if (required.equals("1")) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);


        AssetProof proof = new AssetProof();
        proof.id = position  ;

        AssetProof assetProof = AssetProof.getAssetProofData(proof);
        //没数据
        if (assetProof == null){
            materials_tv.setText(valueList.get(0).getDynamicValue());
            ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
        }
        else {
            //查到数据--展示数据内容
            materials_tv.setText(assetProof.dynamicValue);
            ImageLoad.getImageLoad().LoadImage(this,assetProof.imagePath,photo_image);
        }

    }




    @Override
    protected int getLayoutId() {
        return R.layout.activity_materials_uploaded;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected MaterialsUploadedPresenter createPresenter() {
        return new MaterialsUploadedPresenter(this);
    }

    @Override
    public void initView() {
        back_tv = findViewById(R.id.back_tv);
        back_tv.setOnClickListener(this);
        amount = findViewById(R.id.amount);
        title = findViewById(R.id.title);
        photo_image = findViewById(R.id.photo_image);
        number_tv = findViewById(R.id.number_tv);
        highNetWorthUpload = findViewById(R.id.highNetWorthUpload);
        materials_tv = findViewById(R.id.materials_tv);
        materials_tv.setOnClickListener(this);
        photo_image.setOnClickListener(this);
        required_tv = findViewById(R.id.required_tv);
        required_tv.setOnClickListener(this);
        findViewById(R.id.explain).setOnClickListener(this);
        bt_next = findViewById(R.id.bt_next);
        animation_layout = findViewById(R.id.animation_layout);
        bt_next.setOnClickListener(this);
        bottomSheetDialog = new BottomSheetDialog(this);
        View rootView = getLayoutInflater().inflate(R.layout.bottom_sheet_sialog_layout,null);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        rootView.findViewById(R.id.photo).setOnClickListener(this);
        rootView.findViewById(R.id.camera_image).setOnClickListener(this);
        rootView.findViewById(R.id.dismiss_image).setOnClickListener(this);
        SupportDisplay.resetAllChildViewParam((ViewGroup) rootView);
        bottomSheetDialog.setContentView(rootView);
        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.right_to_left_translate);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bt_next.setClickable(false);
                bt_next.setEnabled(false);
                back_tv.setClickable(false);
                back_tv.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                bt_next.setClickable(true);
                bt_next.setEnabled(true);
                back_tv.setClickable(true);
                back_tv.setEnabled(true);

                if (isNext){
                    nextButton();
                }
                else {
                    backButton();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dictDynamics = new ArrayList<>();
        valueList = new ArrayList<>();
    }

    @Override
    public void initData() {
        isRealInvestor = getIntent().getStringExtra("isRealInvestor");
        isRegister = getIntent().getBooleanExtra("isRegister",false);
        if (isRealInvestor == null){
            isRealInvestor = "0";
        }
        presenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.mater_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);

        HighNetWorthMaterialsUploadedActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        //设置压缩规则，最大500kb
        //takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(100 * 1024).create(), true);
        return takePhoto;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        highNetWorthUpload.destroy();
    }

    @Override
    public void takeSuccess(TResult result,TImage.FromType fromType) {

        TImage image =result.getImage();
        String originalPath = image.getOriginalPath();

        AssetProof proof = new AssetProof();
        proof.id = position;

        AssetProof assetProof = AssetProof.getAssetProofData(proof);
        assetProof.id = position;

        if (assetProof.dynamicValue == null){

            assetProof.dynamicValue = valueList.get(0).getDynamicValue();
        }

        if ( assetProof.dynamicKey == null){

            assetProof.dynamicKey = valueList.get(0).getDynamicKey();
        }
        if (assetProof.model == null){

            assetProof.model = dictDynamics.get(position).getModel();
        }
        //相册选取图片
        if (fromType != null && fromType == TImage.FromType.OTHER){
            assetProof.imagePath = originalPath;
            AssetProof.putData(assetProof);
            ImageLoad.getImageLoad().LoadImage(this,originalPath,photo_image);


        }
        //相机选取图片
        else {
            String photoPath = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.length());

            String paths = FileUtil.getProof(this);

            File file = new File(paths,photoPath);

            assetProof.imagePath = file.getPath();
            AssetProof.putData(assetProof);

            ImageLoad.getImageLoad().LoadImage(this,file.getPath(),photo_image);
        }

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (index > 1){
            isNext = false;
            animation_layout.startAnimation(translateAnimation);
        }
        else {
            finish();
        }

        return true;
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPickFromCaptureNeed() {

        File file = FileUtil.createFile(HighNetWorthMaterialsUploadedActivity.this,FileUtil.PROOF,FileUtil.getFileName()+ ".png");
        Uri uri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(uri);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPickFromCaptureDenied() {


        showDialog(R.string.camera_permission_hint, R.string.permission, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                HighNetWorthMaterialsUploadedActivityPermissionsDispatcher.onPickFromCaptureNeedWithPermissionCheck(HighNetWorthMaterialsUploadedActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPickFromCaptureAgain() {


        showDialog(R.string.camera_permission_hint, R.string.setting, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity(HighNetWorthMaterialsUploadedActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }
}

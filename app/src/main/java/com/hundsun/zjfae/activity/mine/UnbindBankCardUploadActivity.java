package com.hundsun.zjfae.activity.mine;

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

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.adapter.MaterialsAdapter;
import com.hundsun.zjfae.activity.mine.bean.ConfigurationUtils;
import com.hundsun.zjfae.activity.mine.presenter.UnbindBankCardUploadPresenter;
import com.hundsun.zjfae.activity.mine.view.UnbindBankCardUploadView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ChangeCard;
import com.hundsun.zjfae.common.user.UnBindCard;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
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
import java.util.List;

import onight.zjfae.afront.gensazj.DictDynamics;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class UnbindBankCardUploadActivity extends CommActivity<UnbindBankCardUploadPresenter> implements UnbindBankCardUploadView,View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {

    private TextView number_tv,materials_tv,back_tv,business_title;

    private ImageView photo_image;

    private WebView highNetWorthUpload;

    private Button bt_next;

    private TextView required_tv;

    private BottomSheetDialog bottomSheetDialog = null;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private MaterialsAdapter adapter ;


    private boolean isItem = true;



    private BottomSheetDialog MaterialsUploadedDialog = null;
    private static final int requestCodes = 0x754;

    private static final int ITEM_INDEX = 0x701;

    private List<DictDynamics.PBAPP_dictDynamic.DictDynamic> dictDynamics;

    private  List<DictDynamics.PBAPP_dictDynamic.KeyAndValue> valueList;

    private int index = 1;

    private int position = 0;


    private String dynamicType = "";

    private ConfigurationUtils faceUtils;


    private LinearLayout animation_layout;

    private Animation translateAnimation;

    private boolean isNext = false;

    @Override
    public void dictData(DictDynamics.Ret_PBAPP_dictDynamic dictDynamic) {
        dictDynamics = dictDynamic.getData().getDictDynamicListList();
        final RecyclerView recyclerView = new RecyclerView(UnbindBankCardUploadActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MaterialsAdapter(this);

        adapter.setItemOnClickListener(new MaterialsAdapter.ItemOnClickListener() {
            @Override
            public void onItemClick(int i) {


                CCLog.e("position",position);

                materials_tv.setText(valueList.get(i).getDynamicValue());

                //是解绑银行卡
                if (faceUtils.getVerifyscene().equals("unbindCard")){
                    UnBindCard und = new UnBindCard();
                    und.id = position;
                    UnBindCard unBindCard = UnBindCard.getUnBindCardData(und);
                    unBindCard.id = position;
                    unBindCard.model = dictDynamics.get(position).getModel();
                    unBindCard.dynamicValue = valueList.get(i).getDynamicValue();
                    unBindCard.dynamicKey = valueList.get(i).getDynamicKey();
                    UnBindCard.putData(unBindCard);
                }
                else {
                    ChangeCard card = new ChangeCard();
                    card.id = position;

                    ChangeCard changeCard = ChangeCard.getChangeCardData(card);
                    changeCard.id = position;
                    changeCard.model = dictDynamics.get(position).getModel();
                    changeCard.dynamicValue = valueList.get(i).getDynamicValue();
                    changeCard.dynamicKey = valueList.get(i).getDynamicKey();
                    ChangeCard.putData(changeCard);
                }
                MaterialsUploadedDialog.dismiss();
            }
        });
        init();
        recyclerView.addItemDecoration(new DividerItemDecorations());
        recyclerView.setAdapter(adapter);
        MaterialsUploadedDialog = new BottomSheetDialog(this);
        MaterialsUploadedDialog.setContentView(recyclerView);

    }


    private void init(){

        if (index>= dictDynamics.size()){
            bt_next.setText("完成");
        }
        business_title.setText(dictDynamics.get(position).getTitle());
        valueList = dictDynamics.get(position).getKeyValueListList();
        adapter.setValueList(valueList);
        String bodyHTML = dictDynamics.get(position).getRemark();


        String required = dictDynamics.get(position).getRequired();


        if (required.equals("1")) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }



        number_tv.setText(index+"");
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);


        //是解绑银行卡
        if (faceUtils.getVerifyscene().equals("unbindCard")){

            UnBindCard unBindCard = new UnBindCard();
            unBindCard.id = position;

            UnBindCard bindCard =  UnBindCard.getUnBindCardData(unBindCard);

            if (bindCard.imagePath != null && bindCard.dynamicValue != null){
                //有数据
                materials_tv.setText(bindCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,bindCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }

        }
        //申请换卡
        else {
            ChangeCard card = new ChangeCard();
            card.id = position;

            ChangeCard changeCard =  ChangeCard.getChangeCardData(card);

            if (changeCard.imagePath != null && changeCard.dynamicValue != null){
                //有数据
                materials_tv.setText(changeCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,changeCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }

        }

    }



    @Override
    public void initData() {
        Bundle bundle = getIntent().getBundleExtra("faceBundle");

        if (bundle != null){
            faceUtils = bundle.getParcelable("face");
            if (faceUtils != null){
                dynamicType = faceUtils.getDynamicType();
                CCLog.e("dynamicType",dynamicType);
            }
        }

        presenter.dictDynamic(dynamicType);
    }

    @Override
    public void initView() {
        back_tv = findViewById(R.id.back_tv);
        back_tv.setOnClickListener(this);
        photo_image = findViewById(R.id.photo_image);
        number_tv = findViewById(R.id.number_tv);
        business_title = findViewById(R.id.business_title);
        highNetWorthUpload = findViewById(R.id.highNetWorthUpload);
        materials_tv = findViewById(R.id.materials_tv);
        materials_tv.setOnClickListener(this);
        photo_image.setOnClickListener(this);
        bt_next = findViewById(R.id.bt_next);
        bt_next.setOnClickListener(this);
        required_tv = findViewById(R.id.required_tv);
        required_tv.setOnClickListener(this);
        animation_layout = findViewById(R.id.animation_layout);
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
    }


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


                UnbindBankCardUploadActivityPermissionsDispatcher.onPickFromCaptureNeedWithPermissionCheck(this);

                bottomSheetDialog.dismiss();
                break;
            case R.id.dismiss_image:
                bottomSheetDialog.dismiss();
                break;

            case R.id.materials_tv:
                if (Utils.isViewEmpty(materials_tv)){
                    return;
                }
                if (MaterialsUploadedDialog != null){
                    MaterialsUploadedDialog.show();
                }
                break;
            case R.id.bt_next:


                //是解绑银行卡
                if (faceUtils.getVerifyscene().equals("unbindCard")){

                    UnBindCard unBindCard = new UnBindCard();
                    unBindCard.id = position;
                    UnBindCard bindCard = UnBindCard.getUnBindCardData(unBindCard);
                    if (bindCard.imagePath == null){

                        showDialog("请先选择照片");
                    }
                    else {

                        List<UnBindCard> unBindCardList = UnBindCard.getUnBindCardAllData();
                        int assetsNumber = 0,assetsRela = 0;
                        for (UnBindCard card : unBindCardList){

                            if (card.imagePath == null || card.imagePath.equals("null") || card.imagePath.equals("")){
                                continue;
                            }
                            String rela = card.dynamicKey;

                            if (rela.equals("rela")){
                                //关系证明+1
                                assetsRela = assetsRela+1;
                            }
                            else {
                                //资产证明+1
                                assetsNumber = assetsNumber+1;
                            }
                        }

                        if (assetsNumber > 7 && index > 6 ){
                            showDialog("您已上传了7张资产证明，请结束上传或者上传关系证明", "知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        else if (assetsRela > 3 && index > 2 ){
                            showDialog("您已上传了3张关系证明，请结束上传或者上传资产证明", "知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }

                        else if (index == dictDynamics.size()){

                            Intent intent = new Intent(this,UnbindBankCardMaterialsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("face",faceUtils);
                            intent.putExtra("faceBundle",bundle);
                            startActivityForResult(intent,requestCodes);
                        }
                        else {
                            back_tv.setText("上一步");
                            isNext = true;
                            animation_layout.startAnimation(translateAnimation);
                        }
                    }



                }
                //换卡
                else {


                    ChangeCard card = new ChangeCard();
                    card.id = position;

                    ChangeCard changeCard =  ChangeCard.getChangeCardData(card);

                    if (changeCard.imagePath == null){

                        showDialog("请先选择照片");
                    }

                    else {

                        if (index == dictDynamics.size()){
                            Intent intent = new Intent(this,UnbindBankCardMaterialsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("face",faceUtils);
                            intent.putExtra("faceBundle",bundle);
                            startActivityForResult(intent,requestCodes);
                        }
                        else {
                            back_tv.setText("上一步");
                            isNext = true;
                            animation_layout.startAnimation(translateAnimation);
                        }
                    }

                }


                break;

            case R.id.required_tv://完成
                Intent intent = new Intent(this,UnbindBankCardMaterialsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("face",faceUtils);
                intent.putExtra("faceBundle",bundle);
                startActivityForResult(intent,requestCodes);
                break;

        }
    }


    private void nextButton(){
        ++index ;
        ++position;
        if (index>= dictDynamics.size()){
            bt_next.setText("完成");
        }
        business_title.setText(dictDynamics.get(position).getTitle());
        valueList = dictDynamics.get(position).getKeyValueListList();
        adapter.setValueList(valueList);
        String bodyHTML = dictDynamics.get(position).getRemark();

        number_tv.setText((index)+"");
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);

        String required = dictDynamics.get(position).getRequired();


        if (required.equals("1") && isItem) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }

        //是解绑银行卡
        if (faceUtils.getVerifyscene().equals("unbindCard")){


            UnBindCard unBindCard = new UnBindCard();
            unBindCard.id = position;

            UnBindCard bindCard =  UnBindCard.getUnBindCardData(unBindCard);

            if (bindCard.imagePath != null && bindCard.dynamicValue != null){
                //有数据
                materials_tv.setText(bindCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,bindCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }


        }
        //申请换卡
        else {
            ChangeCard card = new ChangeCard();
            card.id = position;

            ChangeCard changeCard =  ChangeCard.getChangeCardData(card);

            if (changeCard.imagePath != null && changeCard.dynamicValue != null){
                //有数据
                materials_tv.setText(changeCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,changeCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }

        }



    }


    public void backButton(){
        --index ;
        --position;
        if (index <= 1){
            back_tv.setText("返回");
        }
        bt_next.setText("下一步");

        number_tv.setText(index+"");
        business_title.setText(dictDynamics.get(position).getTitle());

        String required = dictDynamics.get(position).getRequired();


        if (required.equals("1") && isItem) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }

        String bodyHTML = dictDynamics.get(position).getRemark();
        valueList = dictDynamics.get(position).getKeyValueListList();
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);


        //是解绑银行卡
        if (faceUtils.getVerifyscene().equals("unbindCard")){


            UnBindCard unBindCard = new UnBindCard();
            unBindCard.id = position;

            UnBindCard bindCard =  UnBindCard.getUnBindCardData(unBindCard);

            if (bindCard.imagePath != null && bindCard.dynamicValue != null){
                //有数据
                materials_tv.setText(bindCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,bindCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }


        }
        //申请换卡
        else {
            ChangeCard card = new ChangeCard();
            card.id = position;

            ChangeCard changeCard =  ChangeCard.getChangeCardData(card);

            if (changeCard.imagePath != null && changeCard.dynamicValue != null){
                //有数据
                materials_tv.setText(changeCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,changeCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }

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


        business_title.setText(dictDynamics.get(position).getTitle());
        valueList = dictDynamics.get(position).getKeyValueListList();
        required_tv.setVisibility(View.VISIBLE);
        adapter.setValueList(valueList);
        String bodyHTML = dictDynamics.get(position).getRemark();
        String required = dictDynamics.get(position).getRequired();
        if (required.equals("1") && isItem) {
            required_tv.setVisibility(View.GONE);
        } else {
            required_tv.setVisibility(View.VISIBLE);
        }
        number_tv.setText((index)+"");
        highNetWorthUpload.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);

        //是解绑银行卡
        if (faceUtils.getVerifyscene().equals("unbindCard")){


            UnBindCard unBindCard = new UnBindCard();
            unBindCard.id = position;

            UnBindCard bindCard =  UnBindCard.getUnBindCardData(unBindCard);

            if (bindCard.imagePath != null && bindCard.dynamicValue != null){
                //有数据
                materials_tv.setText(bindCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,bindCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }


        }
        //申请换卡
        else {
            ChangeCard card = new ChangeCard();
            card.id = position;

            ChangeCard changeCard =  ChangeCard.getChangeCardData(card);

            if (changeCard.imagePath != null && changeCard.dynamicValue != null){
                //有数据
                materials_tv.setText(changeCard.dynamicValue);
                ImageLoad.getImageLoad().LoadImage(this,changeCard.imagePath,photo_image);
            }
            else {
                materials_tv.setText(valueList.get(0).getDynamicValue());
                ImageLoad.getImageLoad().LoadImage(this,R.drawable.add_photo,photo_image);
            }

        }
    }




    @Override
    protected UnbindBankCardUploadPresenter createPresenter() {
        return  new UnbindBankCardUploadPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestCodes && resultCode == ITEM_INDEX){
            if (data != null){
                index = data.getIntExtra("index",0);
                position = index;
                ++index;
                isItem = false;
                item();

                CCLog.e("position",position);


            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
        UnbindBankCardUploadActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        //设置压缩规则，最大500kb
        //takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
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
        String photoPath = "";


        //相册选取图片
        if (fromType != null && fromType == TImage.FromType.OTHER){

            photoPath = originalPath;

        }
        //相机拍照
        else {
            photoPath = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.length());

            //是解绑银行卡
            if (faceUtils.getVerifyscene().equals("unbindCard")){
                String savePath = FileUtil.getUnBindCard(this);
                File file = new File(savePath,photoPath);
                photoPath = file.getPath();
            }
            else {
                String savePath = FileUtil.getChangeCard(this);
                File file = new File(savePath,photoPath);
                photoPath = file.getPath();
            }
        }



        //是解绑银行卡
        if (faceUtils.getVerifyscene().equals("unbindCard")){

            UnBindCard unBindCard = new UnBindCard();
            unBindCard.id = position;
            unBindCard.dynamicValue = valueList.get(0).getDynamicValue();
            unBindCard.dynamicKey = valueList.get(0).getDynamicKey();
            unBindCard.model = dictDynamics.get(position).getModel();
            unBindCard.imagePath = photoPath;
            UnBindCard.putData(unBindCard);
            ImageLoad.getImageLoad().LoadImage(this,photoPath,photo_image);

        }
        //申请换卡
        else {

            ChangeCard card = new ChangeCard();
            card.id = position;

            ChangeCard changeCard = ChangeCard.getChangeCardData(card);
            changeCard.id = position;

            if ( changeCard.dynamicValue == null){
                changeCard.dynamicValue = valueList.get(0).getDynamicValue();
            }
            if ( changeCard.dynamicKey == null){
                changeCard.dynamicKey = valueList.get(0).getDynamicKey();
            }
            if (changeCard.model == null){

                changeCard.model = dictDynamics.get(position).getModel();
            }
            changeCard.imagePath = photoPath;
            ChangeCard.putData(changeCard);
            ImageLoad.getImageLoad().LoadImage(this,photoPath,photo_image);
        }


    }

    @Override
    public void takeFail(TResult result, String msg) {
        CCLog.e("msg",msg);
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
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.unbind_layout);
        SupportDisplay.resetAllChildViewParam(layout);
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unbind_bank_card_upload;
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPickFromCaptureNeed() {

        File file = null;
        if (faceUtils.getVerifyscene().equals("unbindCard")){

            file = FileUtil.createFile(UnbindBankCardUploadActivity.this,FileUtil.UN_BIND_CARD,FileUtil.getFileName()+ ".png");
        }
        else {

            file = FileUtil.createFile(UnbindBankCardUploadActivity.this,FileUtil.CHANGE_CARD,FileUtil.getFileName()+ ".png");
        }


        Uri uri = Uri.fromFile(file);
        takePhoto.onPickFromCapture(uri);

    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPickFromCaptureDenied() {

        showDialog(R.string.camera_permission_hint, R.string.permission, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UnbindBankCardUploadActivityPermissionsDispatcher.onPickFromCaptureNeedWithPermissionCheck(UnbindBankCardUploadActivity.this);


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
                startAppInfoActivity(UnbindBankCardUploadActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}

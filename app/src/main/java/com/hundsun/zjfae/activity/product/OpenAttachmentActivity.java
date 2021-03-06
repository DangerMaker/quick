package com.hundsun.zjfae.activity.product;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.bean.AttachmentEntity;
import com.hundsun.zjfae.activity.product.presenter.AttachmentPresenter;
import com.hundsun.zjfae.activity.product.view.AttachmentView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;

import org.json.JSONObject;


public class OpenAttachmentActivity extends CommActivity<AttachmentPresenter> implements AttachmentView, View.OnClickListener {


    private AttachmentEntity attachmentEntity = null;

    private static final int PLAY_CODE = 0x789;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_pdf;
    }

    private PDFView pdfView;

    @Override
    public void initView() {
        pdfView = findViewById(R.id.pdfView);
    }

    @Override
    public void initData() {

        attachmentEntity = getIntent().getParcelableExtra("attachmentEntity");



        CCLog.e("url", attachmentEntity.getOpenUrl());

        if (attachmentEntity.getTitle() != null) {
            if (attachmentEntity.getTitle().contains(".pdf")) {
                setTitle(attachmentEntity.getTitle().replace(".pdf", ""));
            }
            else{
                setTitle(attachmentEntity.getTitle());
            }
        }
        presenter.openPDF(attachmentEntity.getOpenUrl());
    }


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.open_pdf_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    protected AttachmentPresenter createPresenter() {
        return new AttachmentPresenter(this);
    }

    @Override
    public void responseBodyData(final byte[] bytes) {
        pdfView.fromBytes(bytes)
                .enableSwipe(true)
                //pdf???????????????????????????????????????????????????????????????
                .swipeHorizontal(false)
                //
                .enableDoubletap(false)
                //?????????????????????0???
                .defaultPage(0)
                .enableAnnotationRendering(true)
                //???????????????????????????????????????????????????????????????????????????
//                .onDraw(onDrawListener)
//                // ?????????????????????????????????????????????????????????????????????
//                .onDrawAll(onDrawListener)
                //??????????????????
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })
                //??????????????????
                .onPageChange(new OnPageChangeListener() {

                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                //????????????????????????
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
                // ??????????????????????????????
//                .onRender(onRenderListener)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {


                        try {
                            String data = new String(bytes, "UTF-8");

                            CCLog.e("data", data);

                            //{"zjsWebResponse":{"returnCode":"9997","returnMsg":"??????????????????"}}
                            //{"zjsWebResponse":{"returnCode":"0009","returnMsg":"?????????????????????"}}

                            JSONObject baseObject = new JSONObject(data);

                            String os = baseObject.optString("zjsWebResponse");

                            JSONObject object = new JSONObject(os);


                            String returnCode = object.optString("returnCode");

                            String returnMsg = object.optString("returnMsg");

                            if (returnCode.equals("9997")) {

                                showDialog(returnMsg);
                            } else {
                                showDialog("??????????????????");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            showDialog("??????????????????");
                        }


                    }
                })
                // ????????????????????????????????????????????????
                .enableAnnotationRendering(true)
                .password(null)
                .scrollHandle(null)
                // ????????????????????????????????????
                .enableAntialiasing(true)
                // ????????????????????????????????????????????????????????????
                .spacing(0)
                .load();
    }






    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @android.support.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}

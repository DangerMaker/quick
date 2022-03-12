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
                //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .swipeHorizontal(false)
                //
                .enableDoubletap(false)
                //设置默认显示第0页
                .defaultPage(0)
                .enableAnnotationRendering(true)
                //允许在当前页面上绘制一些内容，通常在屏幕中间可见。
//                .onDraw(onDrawListener)
//                // 允许在每一页上单独绘制一个页面。只调用可见页面
//                .onDrawAll(onDrawListener)
                //设置加载监听
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })
                //设置翻页监听
                .onPageChange(new OnPageChangeListener() {

                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                //设置页面滑动监听
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
                // 首次提交文档后调用。
//                .onRender(onRenderListener)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {


                        try {
                            String data = new String(bytes, "UTF-8");

                            CCLog.e("data", data);

                            //{"zjsWebResponse":{"returnCode":"9997","returnMsg":"文件不存在！"}}
                            //{"zjsWebResponse":{"returnCode":"0009","returnMsg":"用户信息不存在"}}

                            JSONObject baseObject = new JSONObject(data);

                            String os = baseObject.optString("zjsWebResponse");

                            JSONObject object = new JSONObject(os);


                            String returnCode = object.optString("returnCode");

                            String returnMsg = object.optString("returnMsg");

                            if (returnCode.equals("9997")) {

                                showDialog(returnMsg);
                            } else {
                                showDialog("加载附件失败");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            showDialog("加载附件失败");
                        }


                    }
                })
                // 渲染风格（就像注释，颜色或表单）
                .enableAnnotationRendering(true)
                .password(null)
                .scrollHandle(null)
                // 改善低分辨率屏幕上的渲染
                .enableAntialiasing(true)
                // 页面间的间距。定义间距颜色，设置背景视图
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

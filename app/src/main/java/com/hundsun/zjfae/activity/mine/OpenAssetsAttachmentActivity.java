package com.hundsun.zjfae.activity.mine;

import android.widget.LinearLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

/**
 * @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine
 * @ClassName:      SPDBankAttachmentActivity
 * @Description:     浦发商户快捷支付业务客户服务协议界面
 * @Author:         moran
 * @CreateDate:     2019/6/21 17:59
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/21 17:59
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class OpenAssetsAttachmentActivity extends BasicsActivity {
    private PDFView pdfView;


    @Override
    public void initData() {
        String assetsName = getIntent().getStringExtra("assetsName");
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        pdfView.fromAsset(assetsName)
                .enableSwipe(true)
                //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .swipeHorizontal(false)
                //
                .enableDoubletap(false)
                //设置默认显示第0页
                .defaultPage(0)
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
                // 渲染风格（就像注释，颜色或表单）
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                // 改善低分辨率屏幕上的渲染
                .enableAntialiasing(true)
                // 页面间的间距。定义间距颜色，设置背景视图
                .spacing(0)
                .load();

    }

    @Override
    public void initView() {
        pdfView = findViewById(R.id.pdfView);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_spdbank_attachment;
    }
    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.spdb_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }
}

package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;

public class AttachmentView extends FrameLayout {

    private TextView textView;

    private WebView webView;

    private PDFView pdfView;

    private ImageView imageView;



    private static final String WORD_URL = "https://view.officeapps.live.com/op/view.aspx?src=";

    public AttachmentView(Context context) {
        this(context,null);
    }

    public AttachmentView( Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AttachmentView( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView(){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        textView = new TextView(getContext());
        textView.setLayoutParams(params);


        webView = new WebView(getContext());
        webView.setLayoutParams(params);

        pdfView = new PDFView(getContext(),null);
        pdfView.setLayoutParams(params);

        imageView = new ImageView(getContext());
        imageView.setLayoutParams(params);

    }


    //Text
    public void openTextAttachment(byte [] textByte){
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(textByte);
            byte [] bs = new byte[1024];
            StringBuffer buffer = new StringBuffer();
            while ((inputStream.read(bs))!= -1){
                buffer.append(new String(bs,0,inputStream.read(bs)));
            }

            textView.setText(buffer.toString());

        }
        catch (IOException e){
            CCLog.e(e.getMessage());
            textView.setText("文件解析出错");
        }
        addView(textView);
    }

    //Image
    public void openImageAttachment(byte [] imageByte){

        ImageLoad.getImageLoad().LoadImage(getContext(),imageByte,imageView);
        addView(imageView);

    }

    //PDF
    public void openPDFAttachment(byte [] pdfByte){

        pdfView.fromBytes(pdfByte).enableDoubletap(true)
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                .enableAnnotationRendering(true)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {

                    }
                })
                .enableDoubletap(true)
                .load();

        addView(pdfView);
    }





    //word
    public void openWordAttachment(String url,List<Cookie> cookieStore){

        openAttachment(url,cookieStore);

    }

    //Excel
    private void openExcelAttachment(String url,List<Cookie> cookieStore){
        openAttachment(url,cookieStore);
    }


    //PPT
    private void openPPTAttachment(String url,List<Cookie> cookieStore){
        openAttachment(url,cookieStore);
    }


    private void openAttachment(String url,List<Cookie> cookieStore){
        setCookies(getContext(),cookieStore);
        webView.loadUrl(WORD_URL+url);
        addView(webView);
    }

    private void setCookies(Context context, List<Cookie> cookieStore) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        List<Cookie> cookies = cookieStore;
        for (Cookie cookie : cookies) {
            //注意这里为什么放肆的写了个cookie.getDomain()，而不是像api里边说的url,类似baidu.com如果是域名，直接设置“baidu.com“,
            cookieManager.setCookie(cookie.domain(),  cookie.name() + "=" + cookie.value() + "; domain=" + cookie.domain() + "; path=" + cookie.path());
        }
        CookieSyncManager.getInstance().sync();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

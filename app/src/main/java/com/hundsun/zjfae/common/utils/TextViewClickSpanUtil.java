package com.hundsun.zjfae.common.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextViewClickSpanUtil {
    public TextViewClickSpanUtil(TextView textView, String allRes, String[] clickResArray, int[] colors, ITextViewClickSpanListener spanListener){
        setTextViewClickSpan(textView, allRes, clickResArray, colors, spanListener);
    }

    /**
     *
     * @param textView TextView控件
     * @param allRes 完整的String
     * @param clickResArray String数组,需要点击的文字
     * @param colors int数组 点击文字变色
     * @param spanListener 监听点击接口,通过返回的position判断点击的位置
     */
    private void setTextViewClickSpan(TextView textView,String allRes,String[] clickResArray,int[] colors,ITextViewClickSpanListener spanListener){

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        stringBuilder.append(allRes);

        for (int i=0;i<clickResArray.length;i++){
            int finalI = i;
            Matcher matcher = getClickResPosition(allRes,clickResArray[i]);
            if (matcher!=null){
                stringBuilder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        spanListener.clickSpanListener(finalI);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        int color;
                        if (colors.length>0){
                            if (finalI >= colors.length){
                                color = colors[colors.length-1];
                            }else{
                                color = colors[finalI];
                            }
                            //设置颜色
                            ds.setColor(color);
                        }
                        //去掉下划线
                        ds.setUnderlineText(false);
                    }
                },matcher.start(),matcher.end(),0);
            }
        }

        //加上下面两句才有效果
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(stringBuilder,TextView.BufferType.SPANNABLE);
        //去掉点击后的背景颜色
        textView.setHighlightColor(Color.parseColor("#00000000"));

    }

    /**
     * 获取Matcher用来确定start和end的位置
     */
    private Matcher getClickResPosition(String allRes, String clickRes){
        SpannableString spannableString = new SpannableString(allRes);
        Pattern pattern = Pattern.compile(clickRes);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()){
            return matcher;
        }
        return null;
    }

    public interface ITextViewClickSpanListener{
        void clickSpanListener(int position);
    }

}

package com.hundsun.zjfae.common.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.RMBUtils;
import com.hundsun.zjfae.common.utils.StringUtils;

/**
 * @Description:长短预约详情 确认预约弹输入金额的框
 * @Author: zhoujianyu
 * @Time: 2018/10/23 14:30
 */
public class SellAmountDialog extends Dialog {
    public SellAmountDialog(Context context) {
        super(context);
    }

    public SellAmountDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String mExpectedMaxAnnualRate;//年华收益率
        private String mTransferFloat;//浮动幅度
        private String mFloatingRange;//浮动范围
        private String mLeastTranAmount;//最低装让金额
        @SuppressWarnings("unused")
        private View contentView; //对话框中间加载的其他布局界面
        /*按钮监听事件*/

        private PurchaseMoney purchaseMoney;
        private float alpha = 1.0f;
        private LinearLayout lin_top, lin_bottom;
        private View view_line;
        private String hidden = "";
        private TextView tv_lv_prompt;
        private String str_money = "", str_rate = "",targetRate = "";

        private boolean isHiddenExpectedMaxAnnualRate = false;

        public Builder(Context context, String expectedMaxAnnualRate, String transferFloat, String floatingRange, String leastTranAmount) {
            this.context = context;
            mExpectedMaxAnnualRate = expectedMaxAnnualRate;
            mTransferFloat = transferFloat;
            mFloatingRange = floatingRange;
            mLeastTranAmount = leastTranAmount;
        }

        public Builder(Context context, float alpha) {
            this.context = context;
            this.alpha = alpha;
        }

        public Builder setPurchaseMoney(PurchaseMoney purchaseMoney) {
            this.purchaseMoney = purchaseMoney;
            return this;
        }

        public void setMessageRate(String message, String rate) {
            str_money = message;
            str_rate = rate;
        }

//        public void setMessageRate(String message, String rate,String targetRate) {
//            str_money = message;
//            str_rate = rate;
//            this.targetRate = targetRate;
//        }

        public void setHidden(String hidden) {
            this.hidden = hidden;
        }

        public void setIsHiddenExpectedMaxAnnualRate(boolean isHiddenExpectedMaxAnnualRate){

            this.isHiddenExpectedMaxAnnualRate = isHiddenExpectedMaxAnnualRate;

        }
        @SuppressLint("InflateParams")
        public SellAmountDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final SellAmountDialog dialog = new SellAmountDialog(context, R.style.mystyle);
            final EditText money, rate;
            View layout = inflater.inflate(R.layout.dailog_sell_amount, null);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lin_top = layout.findViewById(R.id.lin_top);
            lin_bottom = layout.findViewById(R.id.lin_bottom);
            view_line = layout.findViewById(R.id.line);
            tv_lv_prompt = layout.findViewById(R.id.tv_lv_prompt);
            if (hidden.equals("AllSell")) {
                ((TextView) layout.findViewById(R.id.tv_title)).setText("请输入利率");
                lin_top.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
            } else if (hidden.equals("float")) {
                ((TextView) layout.findViewById(R.id.tv_title)).setText("请输入本金");
                lin_bottom.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                tv_lv_prompt.setVisibility(View.GONE);
            } else {
                ((TextView) layout.findViewById(R.id.tv_title)).setText("请输入本金和利率");
            }
            money = layout.findViewById(R.id.message);
            //第一位不能输入0
            money.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (money.getText().toString().matches("^0")) {//判断当前的输入第一个数是不是为0
                        money.setText("");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            rate = layout.findViewById(R.id.rate);
            // set the dialog title
            // set the confirm button
            if (StringUtils.isNotBlank(str_money)) {
                money.setText(str_money);
            } else {
                money.setHint("最低转让金额" + RMBUtils.formatWanNum(mLeastTranAmount).replace("元", ""));
            }
            if (StringUtils.isNotBlank(str_rate)) {
                rate.setText(str_rate);
            } else {
                rate.setText("");
                rate.setHint("请输入转让利率");
            }
           if (isHiddenExpectedMaxAnnualRate){

               ((TextView) layout.findViewById(R.id.expectedMaxAnnualRate)).setVisibility(View.GONE);
           }
           else {
               ((TextView) layout.findViewById(R.id.expectedMaxAnnualRate)).setVisibility(View.VISIBLE);
               ((TextView) layout.findViewById(R.id.expectedMaxAnnualRate)).setText("预期年化收益率" + mExpectedMaxAnnualRate);
           }
            if (hidden.equals("float")) {
                //固定利率隐藏浮动区间的布局
                layout.findViewById(R.id.rate_float).setVisibility(View.GONE);
            } else {
                ((TextView) layout.findViewById(R.id.rate_float)).setText("利率浮动区间为" + mFloatingRange + ",浮动步长为" + mTransferFloat);
                layout.findViewById(R.id.rate_float).setVisibility(View.VISIBLE);
            }
            layout.findViewById(R.id.confirm_btn)
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
//                                    confirm_btnClickListener.onClick(dialog,
//                                            DialogInterface.BUTTON_POSITIVE);
                            if (purchaseMoney != null) {
                                purchaseMoney.purchaseMoney(money.getText().toString(), rate.getText().toString());
                                dialog.dismiss();
                            }
                        }
                    });
            // set the cancel button


            layout.findViewById(R.id.cancel_btn)
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

            dialog.setCancelable(false);
            SupportDisplay.setChildViewParam((ViewGroup) layout, false);
            dialog.setContentView(layout);

            return dialog;
        }

    }

    public interface PurchaseMoney {

        void purchaseMoney(String money, String rate);

    }
}

package com.hundsun.zjfae.common.view.popwindow;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:日期选择popupwindow
 * @Author: zhoujianyu
 * @Time: 2018/9/19 10:32
 */
public class PopupWindowDate extends PopupWindow {

    private Activity activity;
    private LinearLayout lin_layout, lin_date_start, lin_date_end, lin_have_et, lin_no_et;
    private TextView tv_date_start, tv_date_end;
    private Button btn_search, btn_search_phone;
    private EditText editText;

    private SearchItemClick searchItemClick;
    private Boolean has_et;//用来判断是否显示手机输入框


    public PopupWindowDate(Activity activity, Boolean has_et, SearchItemClick searchItemClick) {
        this.activity = activity;
        this.has_et = has_et;
        this.searchItemClick = searchItemClick;
        init();
    }


    private void init() {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.product_window_date, null);
        lin_layout = rootView.findViewById(R.id.lin_layout);
        SupportDisplay.resetAllChildViewParam(lin_layout);
        lin_date_start = rootView.findViewById(R.id.lin_date_start);
        lin_date_end = rootView.findViewById(R.id.lin_date_end);
        lin_have_et = rootView.findViewById(R.id.lin_have_et);
        lin_no_et = rootView.findViewById(R.id.lin_no_et);
        tv_date_start = rootView.findViewById(R.id.tv_date_start);
        tv_date_end = rootView.findViewById(R.id.tv_date_end);
        btn_search = rootView.findViewById(R.id.btn_search);
        btn_search_phone = rootView.findViewById(R.id.btn_search_phone);
        editText = rootView.findViewById(R.id.et_phone);
        //是否显示手机号码填写框
        if (has_et) {
            lin_have_et.setVisibility(View.VISIBLE);
            lin_no_et.setVisibility(View.GONE);
        } else {
            lin_have_et.setVisibility(View.GONE);
            lin_no_et.setVisibility(View.VISIBLE);
        }
        final Calendar calendar = Calendar.getInstance();
        //选择开始时间
        lin_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int start_year;
                int start_month;
                int start_day;
                if (StringUtils.isNotBlank(tv_date_start.getText().toString())) {
                    String[] a = tv_date_start.getText().toString().split("-");
                    start_year = Integer.parseInt(a[0]);
                    start_month = Integer.parseInt(a[1]) - 1;
                    start_day = Integer.parseInt(a[2]);
                } else {
                    start_year = calendar.get(Calendar.YEAR);
                    start_month = calendar.get(Calendar.MONTH);
                    start_day = calendar.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(activity, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "";
                        if (monthOfYear < 9) {
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = "" + (monthOfYear + 1);
                        }
                        String day = "";
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = "" + dayOfMonth;
                        }
                        tv_date_start.setText(year + "-" + month + "-" + day);
//                        tv.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

                    }
                }, start_year, start_month, start_day);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!tv_date_start.getText().equals("")) {
                            tv_date_start.setText("");
                        }
                    }
                });
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if (dialog.isShowing()) {
                    return;
                }
                dialog.show();
            }
        });
        //选择结束时间
        lin_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int end_year;
                int end_month;
                int end_day;
                if (StringUtils.isNotBlank(tv_date_end.getText().toString())) {
                    String[] a = tv_date_end.getText().toString().split("-");
                    end_year = Integer.parseInt(a[0]);
                    end_month = Integer.parseInt(a[1]) - 1;
                    end_day = Integer.parseInt(a[2]);
                } else {
                    end_year = calendar.get(Calendar.YEAR);
                    end_month = calendar.get(Calendar.MONTH);
                    end_day = calendar.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(activity, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "";
                        if (monthOfYear < 9) {
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = "" + (monthOfYear + 1);
                        }
                        String day = "";
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = "" + dayOfMonth;
                        }
                        tv_date_end.setText(year + "-" + month + "-" + day);
//                        tv.setText("您选择了：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

                    }
                }, end_year, end_month, end_day);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!tv_date_end.getText().equals("")) {
                            tv_date_end.setText("");
                        }
                    }
                });
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if (dialog.isShowing()) {
                    return;
                }
                dialog.show();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isNotBlank(tv_date_start.getText().toString()) && StringUtils.isNotBlank(tv_date_end.getText().toString())) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date dt1 = df.parse(tv_date_start.getText().toString());
                        Date dt2 = df.parse(tv_date_end.getText().toString());
                        if (dt1.getTime() > dt2.getTime()) {
                            //开始时间大于结束时间 不查询
                            CustomDialog.Builder builder = new CustomDialog.Builder(activity);
                            builder.setTitle("温馨提示");
                            builder.setMessage("输入时间有误，请重新输入");
                            builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                            dismiss();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                searchItemClick.onSearchClick(tv_date_start.getText().toString(), tv_date_end.getText().toString(), "");
                dismiss();
            }
        });
        btn_search_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isNotBlank(tv_date_start.getText().toString()) && StringUtils.isNotBlank(tv_date_end.getText().toString())) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date dt1 = df.parse(tv_date_start.getText().toString());
                        Date dt2 = df.parse(tv_date_end.getText().toString());
                        if (dt1.getTime() > dt2.getTime()) {
                            //开始时间大于结束时间 不查询
                            CustomDialog.Builder builder = new CustomDialog.Builder(activity);
                            builder.setTitle("温馨提示");
                            builder.setMessage("输入时间有误，请重新输入");
                            builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                            dismiss();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                searchItemClick.onSearchClick(tv_date_start.getText().toString(), tv_date_end.getText().toString(), editText.getText().toString());
                dismiss();
            }
        });
        this.setContentView(rootView);
        //自定义基础，设置我们显示控件的宽，高，焦点，点击外部关闭PopupWindow操作
//        int height = activity.getResources().getDisplayMetrics().heightPixels;
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setOnDismissListener(new OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(lp);
            }
        });

    }


    @Override
    public void showAsDropDown(View anchor) {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.6f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        super.showAsDropDown(anchor);
    }


    public void show(View view) {
        if (Utils.isFastDoubleClick()) {
            return;
        }
        if (this.isShowing()) {
            return;
        }
        showAsDropDown(view);
    }

    public interface SearchItemClick {
        void onSearchClick(String start_time, String end_time, String phoneNumber);
    }
}

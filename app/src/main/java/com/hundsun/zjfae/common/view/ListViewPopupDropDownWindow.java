package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 一级列表弹窗
 */
public class ListViewPopupDropDownWindow extends PopupWindow {
    private View mContentView;
    private ListView mListView;
    private PopupDropDownWindowItemAdapter mAdapter;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClicklistener) {
        this.mItemClickListener = itemClicklistener;
    }

    public ListViewPopupDropDownWindow(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.layout_list_view_popup_window, null);
        this.setContentView(mContentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);

//        this.setOnDismissListener(new OnDismissListener() {
//
//            // 在dismiss中恢复透明度
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = ((BaseActivity) mContext).getWindow()
//                        .getAttributes();
//                lp.alpha = 1f;
//                ((BaseActivity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                ((BaseActivity) mContext).getWindow().setAttributes(lp);
//            }
//        });

        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);

        setOutsideTouchable(false);
        mListView = (ListView) mContentView.findViewById(R.id.lv_popup_window);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);//取消滑动时上下的阴影
        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return false;
            }
        });
    }

    //初始化数据

    public void initData(List<String> list, String org) {
        if (mAdapter == null) {
            mAdapter = new PopupDropDownWindowItemAdapter(mContext, list, org);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mItemClickListener.onItemClick(position);
                }
            });
        } else {
            mAdapter.updateData(list, org);
        }
        //动态计算listview高度
//        int totalHeight = 0;
//        int count = mAdapter.getCount();
//        if (count > 5) {
//            count = 5;
//        }
//        for (int i = 0; i < count; i++) {
//            View listItem = mAdapter.getView(i, null, mListView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = mListView.getLayoutParams();
//        params.height = totalHeight;
//        mListView.setLayoutParams(params);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public class PopupDropDownWindowItemAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mTypeList = new ArrayList<>();
        private String mOrg;//当前选中的组织


        public PopupDropDownWindowItemAdapter(Context context, List<String> typeList, String org) {
            this.mContext = context;
            this.mTypeList = typeList;
            this.mOrg = org;
        }

        //更新数据
        public void updateData(List<String> typeList, String org) {
            mTypeList = typeList;
            mOrg = org;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTypeList.size();
        }

        @Override
        public String getItem(int position) {
            return mTypeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_list_view_popup_window, null);
                viewHolder = new ViewHolder();
                viewHolder.mTvType = convertView.findViewById(R.id.tv_type);
                viewHolder.mPair = convertView.findViewById(R.id.iv_pair);
                convertView.setTag(viewHolder);
                SupportDisplay.resetAllChildViewParam((ViewGroup) convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (mOrg.equals(getItem(position))) {
                viewHolder.mTvType.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                viewHolder.mPair.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTvType.setTextColor(mContext.getResources().getColor(R.color.main_black));
                viewHolder.mPair.setVisibility(View.INVISIBLE);
            }
            viewHolder.mTvType.setText(getItem(position));
            return convertView;
        }

        private class ViewHolder {
            TextView mTvType;
            ImageView mPair;
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
//        WindowManager.LayoutParams lp = ((BaseActivity) mContext).getWindow()
//                .getAttributes();
//        lp.alpha = 0.6f;
//        ((BaseActivity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        ((BaseActivity) mContext).getWindow().setAttributes(lp);
        if (Build.VERSION.SDK_INT != 24) {
            //只有24这个版本有问题，好像是源码的问题
            super.showAsDropDown(anchor);
        } else {
            //7.0 showAsDropDown
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
            super.showAsDropDown(anchor);
        }
//        super.showAsDropDown(anchor);
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
}
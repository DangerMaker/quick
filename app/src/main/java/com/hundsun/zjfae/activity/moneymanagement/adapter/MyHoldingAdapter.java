package com.hundsun.zjfae.activity.moneymanagement.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v3.PrdQueryTaUnitFinanceNewPb;


/**
 * 我的持仓适配器
 */
public class MyHoldingAdapter extends RecyclerView.Adapter<MyHoldingAdapter.ViewHolder> {
    private List<AllAzjProto.PBAPPIcons> iconList = new ArrayList<>();
    private Context mContext;

    private List<PrdQueryTaUnitFinanceNewPb.PBIFE_prdquery_prdQueryTaUnitFinanceNew.TaUnitFinanceList> mList;

    public MyHoldingAdapter(Context context, List<PrdQueryTaUnitFinanceNewPb.PBIFE_prdquery_prdQueryTaUnitFinanceNew.TaUnitFinanceList> list) {
        mList = list;
        mContext = context;
    }

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos,String type);
    }


    private ItemClickCallBack clickCallBack;

    public void setIconList(List<AllAzjProto.PBAPPIcons> iconList) {
        this.iconList = iconList;
        notifyDataSetChanged();
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_holding, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.title.setText(mList.get(position).getProductName());
        if (mList.get(position).getProductType().equals("13")) {
            viewHolder.img_tips.setVisibility(View.VISIBLE);
            viewHolder.img_tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                    builder.setTitle("温馨提示");
                    builder.setMessage("本中心仅为投资人提供代为记录增值服务，并非该私募基金的募集、管理、登记机构。若您需要查看交易凭证，请与该私募基金管理人联系，联系方式：" + mList.get(position).getContactsNumber());
                    builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            viewHolder.expectedEarnings.setVisibility(View.GONE);
            viewHolder.lin_type1.setVisibility(View.GONE);
            viewHolder.lin_type2.setVisibility(View.VISIBLE);
            viewHolder.tv_jijin.setText(mList.get(position).getUnit());
            viewHolder.tv_person.setText(mList.get(position).getManagerShortname());
        } else {
            SpannableStringBuilder builder = new SpannableStringBuilder("持有期预计收益 ¥" + mList.get(position).getPreProfit());
            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
            builder.setSpan(redSpan, 7, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.expectedEarnings.setText(builder);
            viewHolder.expectedEarnings.setVisibility(View.VISIBLE);
            viewHolder.principal.setText(mList.get(position).getUnit());
            viewHolder.days_remaining.setText(mList.get(position).getLeftDays() + "/" + mList.get(position).getSurplusHoldDays());
            viewHolder.expected_earnings_date.setText(mList.get(position).getExpectedMaxAnnualRate() + "%");
            viewHolder.lin_type1.setVisibility(View.VISIBLE);
            viewHolder.lin_type2.setVisibility(View.GONE);
            viewHolder.img_tips.setVisibility(View.GONE);
        }

//        if ("true".equals(mList.get(position).getIfCanTransfer()) && !mList.get(position).getCanTransferAmount().equals("0")) {
//            viewHolder.transferable.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.transferable.setVisibility(View.GONE);
//        }
        if (mList.get(position).getIconsListList().size() > 0) {
            for (AllAzjProto.PBAPPIcons icons : iconList) {
                if (icons.getUuid().equals(mList.get(position).getIconsList(0).getUuid())) {
                    if (icons.getIconsPosition().equals("right_up")) {
                        ImageLoad.getImageLoad().LoadImage(mContext, icons.getIconsAddress(), viewHolder.transferable);
                        viewHolder.transferable.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.transferable.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            viewHolder.transferable.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.onItemClick(position,mList.get(position).getProductType());
                }

            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, expectedEarnings, principal, days_remaining, expected_earnings_date, tv_jijin, tv_person;
        ImageView transferable, img_tips;
        LinearLayout lin_type1, lin_type2;

        public ViewHolder(View view) {
            super(view);
            SupportDisplay.resetAllChildViewParam((RelativeLayout) view.findViewById(R.id.ll_item_my_holding));
            title = view.findViewById(R.id.title);
            expectedEarnings = view.findViewById(R.id.expected_earnings);
            principal = view.findViewById(R.id.principal);
            days_remaining = view.findViewById(R.id.days_remaining);
            expected_earnings_date = view.findViewById(R.id.expected_earnings_date);
            transferable = view.findViewById(R.id.iv_transferable);
            img_tips = view.findViewById(R.id.img_tips);

            lin_type1 = view.findViewById(R.id.lin_type1);
            lin_type2 = view.findViewById(R.id.lin_type2);
            tv_jijin = view.findViewById(R.id.tv_jijin);
            tv_person = view.findViewById(R.id.tv_person);
        }
    }
}






















package com.hundsun.zjfae.activity.moneymanagement.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.util.CancelTimerUtils;
import com.hundsun.zjfae.activity.product.util.ScheduledExecutorUtils;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import onight.zjfae.afront.gens.PBIFEPrdqueryQueryUnifyPurchaseTradeList;


/**
 * 交易记录适配器
 */
public class RecordTransactionAdapter extends RecyclerView.Adapter<RecordTransactionAdapter.ViewHolder> {

    private List<PBIFEPrdqueryQueryUnifyPurchaseTradeList.PBIFE_prdquery_queryUnifyPurchaseTradeList.MyTradeObject> mList;
    private SparseArray<Long> countDownArray = new SparseArray<>();

    public RecordTransactionAdapter() {
        this.mList = new ArrayList<>();
        startTimer();
    }

    /**
     * 启动倒计时
     */
    private void startTimer() {
        Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        for (int i = 0; i < mList.size(); i++) {
                            Long countDown = countDownArray.get(i);
                            if (countDown != null && countDown > 0) {
                                countDownArray.put(i, countDown - 1);

                                if (countDown == 1) {
                                    if (clickCallBack != null) {
                                        clickCallBack.onCountDownSuccess();
                                    }
                                }
                                CCLog.e("countDown", "countDown  " + i + "  " + (countDown - 1));
                            }
                        }
                        notifyDataSetChanged();
                    }
                });
    }

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {

        /**
         * 撤单按钮点击事件回调
         *
         * @param productCode    产品代码
         * @param delegationCode 认购编号
         * @return
         * @date: 2020/11/1 16:47
         * @author: moran
         */

        void onCancelFlagClick(String productCode, final String delegationCode);

        /**
         * 撤单倒计时结束事件回调
         *
         * @return
         * @date: 2020/11/1 16:47
         * @author: moran
         */

        void onCountDownSuccess();


        /**
         * item点击事件回调
         *
         * @param productCode        产品代码
         * @param delegationCode     认购编号
         * @param special            特约标志
         * @param tradeAmount        交易金额
         * @param delegateStatusName 成交状态中文显示
         * @param spvFlag            该产品是否是Svp产品，是隐藏购买详情页面挂牌开始/结束时间，起息日布局
         * @param cancelFlag         是否可撤单，true-可撤单，false-不可撤单
         * @param countDown          撤单倒计时
         * @param kqType             卡券类型
         * @param kqValue            卡券值
         * @param kqAddRateBj        卡券加息本金
         * @return
         * @date: 2020/11/1 16:49
         * @author: moran
         */
        void onItemClick(String productCode, String delegationCode, String special, String tradeAmount, String delegateStatusName, String spvFlag, boolean cancelFlag, long countDown, String kqType, String kqValue, String kqAddRateBj);
    }


    private ItemClickCallBack clickCallBack;


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_record_transaction, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        viewHolder.product_name.setText(mList.get(position).getProductName());

        viewHolder.transaction_fee.setText(MoneyUtil.formatMoney2(mList.get(position).getExtraCost()) + "元");

        if (mList.get(position).getSaleType().equals("buyer")) {

            viewHolder.play_type.setText("买方");
        } else {

            viewHolder.play_type.setText("卖方");
        }


        viewHolder.play_time.setText(mList.get(position).getTradeDate());

        viewHolder.play_status.setText(mList.get(position).getSaleStatusName());


        viewHolder.play_total_amount.setText(MoneyUtil.formatMoney2(mList.get(position).getTradeAmount()) + "元");


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickCallBack != null) {


                    clickCallBack.onItemClick(mList.get(position).getProductCode(), mList.get(position).getDelegationCode(), mList.get(position).getSpecialFlag(), mList.get(position).getTradeAmount(), mList.get(position).getSaleStatusName(), mList.get(position).getSpvFlag(), mList.get(position).getCancelFlag().equals("1"), countDownArray.get(position) == null ? 0 : countDownArray.get(position), mList.get(position).getKqType(), mList.get(position).getKqValue(), mList.get(position).getKqAddRateBj());
                }
            }
        });


        if (mList.get(position).getCancelFlag().equals("1") && countDownArray.get(position) > 0) {

            viewHolder.llCancelFlag.setVisibility(View.VISIBLE);

            CCLog.e("countDown", "bind  " + position + "  " + countDownArray.get(position));
            viewHolder.tv_count_down.setText(StringUtils.formatCancelCountDown(countDownArray.get(position)));
            viewHolder.cancelFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (clickCallBack != null) {

                        clickCallBack.onCancelFlagClick(mList.get(position).getProductCode(), mList.get(position).getDelegationCode());

                    }

                }
            });

        } else {
            viewHolder.llCancelFlag.setVisibility(View.GONE);
        }


    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<PBIFEPrdqueryQueryUnifyPurchaseTradeList.PBIFE_prdquery_queryUnifyPurchaseTradeList.MyTradeObject> list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getCancelFlag().equals("1")) {
                    countDownArray.put(i, Long.parseLong(list.get(i).getCancelCountdown()));
                    CCLog.e("countDown", i + "  " + Long.parseLong(list.get(i).getCancelCountdown()));

                }
            }
        }
        notifyDataSetChanged();
    }

    public void addData(List<PBIFEPrdqueryQueryUnifyPurchaseTradeList.PBIFE_prdquery_queryUnifyPurchaseTradeList.MyTradeObject> list) {
        mList.addAll(list);
        for (int i = mList.size() - list.size(); i < mList.size(); i++) {
            if (mList.get(i).getCancelFlag().equals("1")) {
                countDownArray.put(i, Long.parseLong(mList.get(i).getCancelCountdown()));
                CCLog.e("countDown", i + "  " + Long.parseLong(mList.get(i).getCancelCountdown()));

            }
        }
        notifyDataSetChanged();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, transaction_fee, play_type, play_status, cancelFlag, tv_count_down, tv_play_time, play_time, play_total_amount;
        LinearLayout llCancelFlag;

        public ViewHolder(View view) {
            super(view);

            product_name = view.findViewById(R.id.tv_product_name);

            tv_play_time = view.findViewById(R.id.tv_play_time);

            play_time = view.findViewById(R.id.play_time);

            play_total_amount = view.findViewById(R.id.play_total_amount);

            transaction_fee = view.findViewById(R.id.transaction_fee);

            play_type = view.findViewById(R.id.play_type);

            play_status = view.findViewById(R.id.play_status);

            cancelFlag = view.findViewById(R.id.tv_cancelFlag);

            tv_count_down = view.findViewById(R.id.tv_count_down);

            llCancelFlag = view.findViewById(R.id.ll_cancelFlag);

        }
    }
}






















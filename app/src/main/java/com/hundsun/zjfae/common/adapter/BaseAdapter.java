package com.hundsun.zjfae.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    protected List<T> mData;
    protected Context mContext;
    private LayoutInflater layoutInflater;

    protected  String noDataInfo;

    protected OnItemClickListener onItemClickListner;//单击事件

    protected  BaseViewHolder holder = null;



    /**
     * 控制空布局的显隐
     */
    private int mEmptyType = 1;


    public BaseAdapter(Context context, List<T> list){
        this.mData = list;
        this.mContext = context;
        layoutInflater =LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        BaseViewHolder  baseViewHolder = new BaseViewHolder(layoutInflater.inflate(getLayoutId(position),viewGroup,false));
        return baseViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseViewHolder, final int position) {
        this.holder = (BaseViewHolder) baseViewHolder;
        bindData(mData.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mData != null || mData.isEmpty() ? mData.size() : mEmptyType;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);


    }



    public void rest(List<T> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }


    public void setNoDataInfo(String noDataInfo){
        this.noDataInfo = noDataInfo;
    }


    protected abstract int getLayoutId(int position);


    protected abstract void bindData(T t,final int position);




    public void setOnItemClickListener(OnItemClickListener onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    protected  <T extends View> T getView(int viewId){

        return holder.getView(viewId);
    }

}

package com.hundsun.zjfae.activity.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.LoadProvince;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ProvinceAdapterViewHolder> {

    private Context context;
    private List<LoadProvince.PBIFE_chinacity_loadProvince.TmbprovList> tmbprovLists;
    private LayoutInflater inflater;
    private ProvinceNo provinceNo;
    public ProvinceAdapter(Context context,List<LoadProvince.PBIFE_chinacity_loadProvince.TmbprovList> tmbprovLists){
        this.context = context;
        this.tmbprovLists =tmbprovLists;
        inflater = LayoutInflater.from(context);
    }

    public void setProvinceNo(ProvinceNo provinceNo){
        this.provinceNo = provinceNo;
    }
    @NonNull
    @Override
    public ProvinceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.province_item_layout,viewGroup,false);
        ProvinceAdapterViewHolder viewHolder = new ProvinceAdapterViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProvinceAdapterViewHolder provinceAdapterViewHolder,final int i) {
        provinceAdapterViewHolder.province_name.setText(tmbprovLists.get(i).getPname());
        provinceAdapterViewHolder.province_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (provinceNo != null){
                    provinceNo.provinceNumber(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tmbprovLists.size();
    }

    public class ProvinceAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView province_name;
        LinearLayout province_layout;
        public ProvinceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            province_name = itemView.findViewById(R.id.province_name);
            province_layout = itemView.findViewById(R.id.province_layout);
            SupportDisplay.resetAllChildViewParam(province_layout);
        }
    }

    public interface ProvinceNo{

        void provinceNumber(int position);
    }



}

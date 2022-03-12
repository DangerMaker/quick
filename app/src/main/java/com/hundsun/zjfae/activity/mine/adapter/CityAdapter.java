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

import onight.zjfae.afront.gens.LoadCity;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private Context context;
    private List<LoadCity.PBIFE_chinacity_loadCity.TmbcityList> tmbcityList;
    private LayoutInflater inflater;
    private CityNo cityNo;

    public CityAdapter(Context context,List<LoadCity.PBIFE_chinacity_loadCity.TmbcityList> tmbcityList){
        this.context = context;
        this.tmbcityList = tmbcityList;
        inflater = LayoutInflater.from(context);
    }

    public void setCityNo(CityNo cityNo) {
        this.cityNo = cityNo;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.province_item_layout,viewGroup,false);
        CityViewHolder viewHolder = new CityViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder cityViewHolder, final int i) {
        cityViewHolder.city_name.setText(tmbcityList.get(i).getCname());
        cityViewHolder.city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityNo != null){
                    cityNo.cityNumber(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tmbcityList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder{
        TextView city_name;
        LinearLayout province_layout;
        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.province_name);
            province_layout = itemView.findViewById(R.id.province_layout);
            SupportDisplay.resetAllChildViewParam(province_layout);
        }
    }


    public interface CityNo{

        void cityNumber(int position);
    }
}

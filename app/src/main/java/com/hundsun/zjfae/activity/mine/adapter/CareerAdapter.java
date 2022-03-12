package com.hundsun.zjfae.activity.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.adapter.BaseAdapter;

import java.util.List;

import onight.zjfae.afront.gens.CareerEnumTypeCom;


/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine.adapter
 * @ClassName:      CareerAdapter
 * @Description:     职业适配器
 * @Author:         moran
 * @CreateDate:     2019/7/31 19:27
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/31 19:27
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class CareerAdapter extends BaseAdapter<CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData> {

    public CareerAdapter(Context context, List<CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData> list) {
        super(context, list);
    }

    @Override
    protected int getLayoutId(int position) {

        return R.layout.item_carrer_layout;
    }

    @Override
    protected void bindData(CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData enumData, final int position) {

        TextView careerName = getView(R.id.career_name);

        careerName.setText(enumData.getEnumName());

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListner != null){
                    onItemClickListner.onItemClickListener(v,mData.get(position),position);
                }
            }
        });
    }
}

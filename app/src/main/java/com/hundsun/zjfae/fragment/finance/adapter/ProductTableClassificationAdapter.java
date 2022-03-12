package com.hundsun.zjfae.fragment.finance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.view.DropDownMenu;

import java.util.List;

import onight.zjfae.afront.AllAzjProto;

public class ProductTableClassificationAdapter extends RecyclerView.Adapter<ProductTableClassificationAdapter.ConditionsTypeViewHolder> {


    private List<AllAzjProto.PBAPPSearchSortControl_l2> controls;
    private Context context;
    private LayoutInflater inflater;
    private int checkItemPosition = 0;
    private ProductDefaultFilterAdapter.ItemOnClick itemOnClick;


    //用来保存是否需要bottom
    private Boolean isNeedBottom = false;
    private ProductDefaultFilterAdapter.BtnOnClick btnOnClick;


    public ProductTableClassificationAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ProductTableClassificationAdapter(Context context, List<AllAzjProto.PBAPPSearchSortControl_l2> controls) {
        this.context = context;
        this.controls = controls;
        inflater = LayoutInflater.from(context);
    }

    public void setItemOnClick(ProductDefaultFilterAdapter.ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }

    public void setBtnOnClick(ProductDefaultFilterAdapter.BtnOnClick btnOnClick) {
        this.btnOnClick = btnOnClick;
    }






    public void  setKeyWordNamePosition(int index,DropDownMenu product_dropDownMenu,int position){
        if (controls != null && !controls.isEmpty()){
            product_dropDownMenu.setTabText(index,controls.get(position).getControlHName());
            this.checkItemPosition = position;
            if (itemOnClick != null){
                itemOnClick.setKeyWordName(controls.get(position).getControlHName());
            }
        }
        notifyDataSetChanged();
    }





    public void refresh(List<AllAzjProto.PBAPPSearchSortControl_l2> controls) {
        this.controls = controls;
        this.checkItemPosition = 0;
        notifyDataSetChanged();
    }

    public void refresh(List<AllAzjProto.PBAPPSearchSortControl_l2> controls,int index,DropDownMenu product_dropDownMenu,String keywordName){
        this.controls = controls;
        setKeyWordNamePosition(index,product_dropDownMenu,keywordName);
    }


    private void  setKeyWordNamePosition(int index, DropDownMenu product_dropDownMenu,String keywordName){


        if (keywordName == null || keywordName == "" || keywordName.equals("")){
            checkItemPosition = 0;
        }
        else {
            if (controls != null && !controls.isEmpty()){
                for (int i = 0; i < controls.size(); i++) {
                    if (keywordName .contains(controls.get(i).getControlHName())){
                        product_dropDownMenu.setTabText(index,controls.get(i).getControlHName());
                        this.checkItemPosition = i;
                        break;
                    }
                }
            }
        }


        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ConditionsTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.product_conditions_type_item, viewGroup, false);
        ConditionsTypeViewHolder typeViewHolder = new ConditionsTypeViewHolder(rootView);
        return typeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConditionsTypeViewHolder conditionsTypeViewHolder, final int i) {
        fillValue(i, conditionsTypeViewHolder);

    }

    private void fillValue(final int position, final ConditionsTypeViewHolder viewHolder) {

        if (isNeedBottom && position == getItemCount() - 1) {
            //如果需要底部布局  显示bottomview
            viewHolder.item_bottom.setVisibility(View.VISIBLE);
            viewHolder.item_layout.setVisibility(View.GONE);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnOnClick.onBtnClick(viewHolder.et_1.getText().toString(), viewHolder.et_2.getText().toString());
                }
            });

        } else {
            viewHolder.item_bottom.setVisibility(View.GONE);
            viewHolder.item_layout.setVisibility(View.VISIBLE);
            viewHolder.mText.setText(controls.get(position).getControlHName());


            if (checkItemPosition == position) {
                viewHolder.mText.setBackgroundResource(R.color.check_bg);
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.colorRed));
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.kqou), null);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.mText.setBackgroundResource(R.color.white);
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            viewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnClick != null) {
                        itemOnClick.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isNeedBottom) {
            return controls != null ? controls.size() + 1 : 0;
        } else {
            return controls != null ? controls.size() : 0;
        }
    }

    public class ConditionsTypeViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        LinearLayout item_layout, item_bottom;
        EditText et_1, et_2;
        Button button;

        public ConditionsTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.text);
            item_layout = itemView.findViewById(R.id.item_layout);
            item_bottom = itemView.findViewById(R.id.item_bottom);
            et_1 = itemView.findViewById(R.id.et_1);
            et_2 = itemView.findViewById(R.id.et_2);
            button = itemView.findViewById(R.id.btn);
        }
    }


    public interface ItemOnClick {

        void onItemClick(int position);
        void setKeyWordName(String keyWordName);

    }

    public interface BtnOnClick {
        void onBtnClick(String var1, String var2);
    }

    public void setIsNeedBottom(Boolean isNeedBottom) {
        this.isNeedBottom = isNeedBottom;
    }
}

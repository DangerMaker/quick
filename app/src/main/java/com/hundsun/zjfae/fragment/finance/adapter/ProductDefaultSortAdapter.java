package com.hundsun.zjfae.fragment.finance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.view.DropDownMenu;

import java.util.List;

import onight.zjfae.afront.AllAzjProto;

public class ProductDefaultSortAdapter extends RecyclerView.Adapter<ProductDefaultSortAdapter.ProductConditionsViewHolder> {
    private List<AllAzjProto.PBAPPSearchSortControl_l2> controls;
    private Context context;
    private LayoutInflater inflater;
    private int checkItemPosition = 0;
    private ItemOnClick itemOnClick;




    public ProductDefaultSortAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }






    public ProductDefaultSortAdapter(Context context, List<AllAzjProto.PBAPPSearchSortControl_l2> controls){
        this.context = context;
        this.controls = controls;
        inflater = LayoutInflater.from(context);
    }

    public void refresh(List<AllAzjProto.PBAPPSearchSortControl_l2> controls){
        this.controls = controls;
        this.checkItemPosition = 0;
        notifyDataSetChanged();
    }

    public void refresh(List<AllAzjProto.PBAPPSearchSortControl_l2> controls,int index,DropDownMenu product_dropDownMenu,String keywordName){
        this.controls = controls;
        setKeyWordNamePosition(index,product_dropDownMenu,keywordName);
    }

    private void  setKeyWordNamePosition(int index,DropDownMenu product_dropDownMenu,String keywordName ){

        if (keywordName == null || keywordName == "" || keywordName.equals("")){
            checkItemPosition = 0;
            product_dropDownMenu.setTabText(index,controls.get(0).getControlName());
        }
        else {
            if (controls != null && !controls.isEmpty()){
                for (int i = 0; i < controls.size(); i++) {
                    if (keywordName .contains(controls.get(i).getControlName())){
                        product_dropDownMenu.setTabText(index,controls.get(i).getControlName());
                        this.checkItemPosition = i;
                        break;
                    }
                    else {
                        checkItemPosition = 0;
                    }
                }
            }
        }


        notifyDataSetChanged();
    }

    public void setItemOnClick(ItemOnClick itemOnClick){
        this.itemOnClick = itemOnClick;
    }



    public void  setKeyWordNamePosition(int index,DropDownMenu product_dropDownMenu,int position){
        if (controls != null && !controls.isEmpty()){
            product_dropDownMenu.setTabText(index,controls.get(position).getControlName());
            this.checkItemPosition = position;
            if (itemOnClick != null){
                itemOnClick.setKeyWordName(controls.get(position).getControlName());
            }
        }
        notifyDataSetChanged();
    }



    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductConditionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.product_conditions_type_item,viewGroup,false);
        ProductConditionsViewHolder typeViewHolder = new ProductConditionsViewHolder(rootView);
        return typeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductConditionsViewHolder productConditionsViewHolder, int i) {
        fillValue(i,productConditionsViewHolder);
    }
    private void fillValue(final int position, final ProductConditionsViewHolder viewHolder) {
        viewHolder.mText.setText(controls.get(position).getControlName());

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
                if (itemOnClick != null){
                    itemOnClick.onItemClick(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return controls != null ? controls.size() :0;
    }

    public class ProductConditionsViewHolder extends RecyclerView.ViewHolder{
        TextView mText;
        LinearLayout item_layout;
        public ProductConditionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
            item_layout = itemView.findViewById(R.id.item_layout);
        }
    }

    public interface ItemOnClick{

        void onItemClick(int position);

        void setKeyWordName(String keyWordName);

    }
}

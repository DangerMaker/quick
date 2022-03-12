package com.hundsun.zjfae.common.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;


public class BottomProductLinearLayout extends LinearLayout {

    //设置item个数
    private int displayNum = 0;

    //设置时间
    private long duration = 1000;
    //设置动画间隔时间
    private int scrollTime = 1000;

    /**
     * 是否开启动画-默认不开启
     *
     * */
    private boolean isStartAnimator = false;



    private int scrollHeight,newScrollHeight ;   //滚动高度（控件高度）
    private int status;//产品状态

    private Context mContext;

    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> displayNumList;


    private OnItemClickListener onItemClickListener;

    public BottomProductLinearLayout(Context context) {
        this(context,null);
    }

    public BottomProductLinearLayout(Context context,  AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mContext = context;
//        TextView textView = new TextView(context);
//        textView.setText("1");
//        this.addView(textView);
//
//
//        TextView textView1 = new TextView(context);
//        textView.setText("2");
//        this.addView(textView1);
    }




    public void startAnimator(){

        //有数据
        if (displayNumList != null && !displayNumList.isEmpty()){

            if (displayNumList.size() < displayNum){

                displayNum = displayNumList.size();
            }

            else {
                isStartAnimator = true;
            }

            createView();
        }




    }






    //设置点击事件回调
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /*
     * 设置数据
     * */
    public void setDisplayNumList(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> displayNumList) {
        this.displayNumList = displayNumList;

        initProductState(displayNumList);
    }

    //设置显示个数
    public void setDisplayNum(String displayNum){

        this.displayNum = Integer.parseInt(displayNum);
    }



    //设置动画间隔时间
    public void setScrollTime(String scrollTime) {
        this.scrollTime = Integer.parseInt(scrollTime);
        this.scrollTime =  this.scrollTime * 1000;
    }

    private int layoutWidth = 0;
    private int layoutHeight = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int widthMode = MeasureSpec. getMode(widthMeasureSpec);
        int heightMode = MeasureSpec. getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec. getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec. getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec,heightMeasureSpec);

        CCLog.e("sizeWidth",sizeWidth);

        CCLog.e("sizeHeight",sizeHeight);

        int cWidth = 0;
        int cHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        int count = getChildCount();

        //固定宽度
        if (widthMode == MeasureSpec.EXACTLY){

            layoutWidth = sizeWidth;
        }
        else {
            View child = getChildAt(0);
            cWidth = child.getMeasuredWidth();

            layoutWidth = cWidth;
        }

        Log.i("layoutWidth",layoutWidth+"");

        if(heightMode == MeasureSpec. EXACTLY){

            layoutHeight = sizeHeight;
        }
        else {
            for (int i = 0; i < count ; i++) {

                View child = getChildAt(i);

                cHeight = cHeight+child.getMeasuredHeight();

                Log.i("cHeight",cHeight+"");
            }

            layoutHeight = cHeight;
        }

        LayoutParams params = (LayoutParams) getLayoutParams();

        params.height = layoutHeight;
        this.setLayoutParams(params);

        setMeasuredDimension(layoutWidth,layoutHeight);
    }




    /*
     *遍历产品状态
     * */
    private void initProductState(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> displayNumList){


        if (displayNumList != null && !displayNumList.isEmpty()){

            for (PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject detailList: displayNumList){
                if (!detailList.getSellingStatus().equals("1")) {
                    status = status + 1;

                }

                if (status == displayNumList.size()) {
                    //所有产品敬请期待
                    if (null != onItemClickListener) {
                        onItemClickListener.onProductStatus();
                    }
                }
            }

        }



    }


    private void createView(){
        mHandle.removeMessages(WHAT);
        if (isStartAnimator){

            int childView= getChildCount();


            if (childView == 0){

                for (int i = 0; i < displayNum ; i++) {

                    View rootView = LayoutInflater.from(mContext).inflate(R.layout.home_buottom_product_layout, null);
                    initView(rootView,getData());
                    this.addView(rootView);
                }

                mHandle.sendEmptyMessageDelayed(WHAT,scrollTime);

                return;
            }


            for (int i = 0; i < displayNum ; i++) {

                View rootView = LayoutInflater.from(mContext).inflate(R.layout.home_buottom_product_layout, null);
                initView(rootView,getData());
                this.addView(rootView);
            }
            ValueAnimator animator=ValueAnimator.ofInt(0,layoutHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value= (int) animation.getAnimatedValue();
                    BottomProductLinearLayout.this.scrollTo(0,value);
                }
            });
            animator.setDuration(duration);
            animator.start();
            mHandle.sendEmptyMessageDelayed(WHAT,scrollTime);

        }
        else {

            for (int i = 0; i < displayNum ; i++) {

                View rootView = LayoutInflater.from(mContext).inflate(R.layout.home_buottom_product_layout, null);
                initView(rootView,getData());
                this.addView(rootView);
            }

        }



    }


    private static final int WHAT = 0;

    private Handler mHandle = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case WHAT:

                    if (BottomProductLinearLayout.this.getChildCount() > displayNum){

                        for (int i = 0; i < displayNum; i++) {

                            BottomProductLinearLayout.this.removeViewAt(0);
                        }

                    }
                    createView();

                    break;

                default:
                    break;
            }
        }
    };


    public void start(){
        mHandle.sendEmptyMessageDelayed(WHAT, scrollTime);
    }

    public void stop(){
        mHandle.removeMessages(WHAT);
    }


    private TextView productName, expectedMaxAnnualRate, deadline, buyerSmallestAmount, mostHolderNum;
    private RoundProgressBar roundProgressBar;
    private LinearLayout item_layout;
    private ImageView product_left_top_icon, product_left_bottom_icon;
    private ImageView left_middle, center_middle, right_middle;
    private ImageView product_right_bottom, selling_image;
    private void initView(final View itemView,PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject financeDetailList){
        productName = itemView.findViewById(R.id.productName);
        expectedMaxAnnualRate = itemView.findViewById(R.id.expectedMaxAnnualRate);
        deadline = itemView.findViewById(R.id.deadline);
        buyerSmallestAmount = itemView.findViewById(R.id.buyerSmallestAmount);
        mostHolderNum = itemView.findViewById(R.id.mostHolderNum);
        roundProgressBar = itemView.findViewById(R.id.roundProgressBar);
        item_layout = itemView.findViewById(R.id.item_layout);
        product_right_bottom = itemView.findViewById(R.id.product_right_bottom);
        product_left_top_icon = itemView.findViewById(R.id.product_left_icon);
        product_left_bottom_icon = itemView.findViewById(R.id.product_left_bottom_icon);
        left_middle = itemView.findViewById(R.id.left_middle);
        center_middle = itemView.findViewById(R.id.center_middle);
        right_middle = itemView.findViewById(R.id.right_middle);
        selling_image = itemView.findViewById(R.id.selling_image);
        SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);


        setViewData(financeDetailList);
    }


    private void setViewData(final PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject financeDetailList){

        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList
                = financeDetailList.getIconsListList();
        setImage(iconsList);

        productName.setText(financeDetailList.getProductName());
        expectedMaxAnnualRate.setText(financeDetailList.getExpectedMaxAnnualRate() + "%");
        deadline.setText(financeDetailList.getDeadline());
        buyerSmallestAmount.setText(financeDetailList.getBuyerSmallestAmount() + "元起");
        if (financeDetailList.getMostHolderNum().length() < 15) {
            mostHolderNum.setText(financeDetailList.getMostHolderNum() + "人可购");
        } else {
            mostHolderNum.setText("不限人数");
        }
        float progress = Float.valueOf(financeDetailList.getProcess());
        productState(financeDetailList.getSellingStatus(),progress);


        item_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(financeDetailList.getProductCode(), financeDetailList.getSellingStatus());
                }
            }
        });

    }



    //设置产品状态
    private void productState(String sellingStatus,float progress) {
        //0-敬请期待，1-售卖中，2-已售罄，3-已结束
        switch (sellingStatus) {
            case "0":
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                Glide.with(getContext()).load(R.drawable.stay_tuned).into(selling_image);
                break;
            case "1":
                selling_image.setVisibility(View.GONE);
                roundProgressBar.setVisibility(View.VISIBLE);
                roundProgressBar.setProgress((int) progress);
                break;
            case "2":
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                Glide.with(getContext()).load(R.drawable.sold_out).into(selling_image);
                break;
            case "3":
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                Glide.with(getContext()).load(R.drawable.ended).into(selling_image);
                break;
            default:
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                break;
        }
    }



    private void setImage(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList) {

        if (iconsList.size() == 0 || iconsList.isEmpty()) {
            return;
        }

        List<String> leftList = new ArrayList<>();
        List<String> middleList = new ArrayList<>();
        List<String> rightList = new ArrayList<>();

        for (int i = 0; i < iconsList.size(); i++) {
            String type = iconsList.get(i).getIconsPosition();
            if (type.equals("left")) {
                leftList.add(iconsList.get(i).getUuid());
            } else if (type.equals("l_middle")) {
                middleList.add(iconsList.get(i).getUuid());
            } else if (type.equals("right_lower")) {
                rightList.add(iconsList.get(i).getUuid());
            }
        }
        setLeftImage(leftList);
        setMiddleImage(middleList);
        setRightImage(rightList);
    }


    //设置左边的角标
    private void setLeftImage(List<String> leftList) {

        if (leftList.size() == 1) {
            setImage(product_left_top_icon, leftList.get(0));
            setImage(product_left_bottom_icon);
        } else if (leftList.size() == 2) {
            setImage(product_left_top_icon, leftList.get(0));

            setImage(product_left_bottom_icon, leftList.get(1));
        } else {
            setImage(product_left_top_icon);
            setImage(product_left_bottom_icon);
        }


    }
    //设置中间的角标
    private void setMiddleImage(List<String> middleList) {
        //只有一张图
        if (middleList.size() == 1) {
            String leftImage_path = middleList.get(0);
            setImage(left_middle, leftImage_path);
            setImage(center_middle);
            setImage(right_middle);
        }
        //只有两张图
        else if (middleList.size() == 2) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            setImage(left_middle, leftImage_path);
            setImage(center_middle, centerImage_path);
            setImage(right_middle);
        }
        //三张图
        else if (middleList.size() == 3) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            String rightImage_path = middleList.get(2);
            setImage(left_middle, leftImage_path);
            setImage(center_middle, centerImage_path);
            setImage(right_middle, rightImage_path);
        } else {
            setImage(left_middle);
            setImage(center_middle);
            setImage(right_middle);
        }


    }




    //设置右下角角标
    private void setRightImage(List<String> rightLft) {

        if (rightLft.size() == 1) {
            String imagePath = rightLft.get(0);
            setImage(product_right_bottom, imagePath);
        } else {
            setImage(product_right_bottom);
        }


    }

    private void setImage(ImageView imageView, String fileName) {
        imageView.setVisibility(View.VISIBLE);
        if (ADSharePre.getListConfiguration(ADSharePre.homeProductIcon, BaseCacheBean.class) == null) {
            imageView.setVisibility(View.GONE);
            return;
        }
        List<BaseCacheBean> imageList = ADSharePre.getListConfiguration(ADSharePre.homeProductIcon, BaseCacheBean.class);
        if (imageList.isEmpty()) {
            imageView.setVisibility(View.GONE);
            return;
        }
        else {

            for (BaseCacheBean imageBean : imageList) {
                if (fileName.equals(imageBean.getUuid())) {


                    ImageLoad.getImageLoad().LoadImage(getContext(), imageBean.getIconsAddress(), imageView);

                }

            }

        }


    }

    private void setImage(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }






    private int index = 0;
    private  PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject getData() {

        if (index >= displayNumList.size()-1){
            index = 0;
        }

        PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject detailList = displayNumList.get(index);
        index = index +1;


        return detailList;
    }



    public interface OnItemClickListener {

        void onItemClick(String productCode, String sellingStatus);

        void onProductStatus();

    }

}

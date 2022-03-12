package com.hundsun.zjfae.common.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.hundsun.zjfae.common.utils.RxTimerUtil;
import com.hundsun.zjfae.fragment.home.adapter.HomeProductAdapter;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;


public class BottomProductTipView extends FrameLayout {


    private int scrollTime = 5;

    /**
     * view
     */
    private RecyclerView view_tip_out, view_tio_in;


    private int curTipIndex = 0;


    private boolean startAnimation = true;
    //显示的数量
    private int displayNum;


    private static final long duration = 1000;


    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> displayNumList;

    private OnItemClickListener onItemClickListener;

    public BottomProductTipView(@NonNull Context context) {
        super(context);
    }

    public BottomProductTipView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomProductTipView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setProductListNewHome(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome productListNewHome) {
        RxTimerUtil.cancel();
        this.displayNum = Integer.parseInt(productListNewHome.getDisplayNum());
        //时间
        this.scrollTime = Integer.parseInt(productListNewHome.getScrollTime());

        curTipIndex = 0;
        this.displayNumList = productListNewHome.getProductTradeInfoListList();
        if (getChildCount() != 0) {
            removeAllViews();
        }
        //是否开启动画
        if (displayNum >= displayNumList.size()) {
            view_tio_in = newRecyclerView();
            view_tio_in.setAdapter(getAdapter());
            addView(view_tio_in);
            view_tio_in.bringToFront();
        } else {
            view_tio_in = newRecyclerView();
            view_tip_out = newRecyclerView();
            addView(view_tio_in);
            addView(view_tip_out);
            view_tip_out.setVisibility(GONE);
            view_tio_in.setAdapter(getAdapter());
            view_tip_out.setAdapter(getAdapter());
            view_tio_in.bringToFront();
            view_tip_out.setY(view_tio_in.getHeight());
            sample(scrollTime);

        }


    }


    private void sample(long scrollTime) {


        RxTimerUtil.setPeriod(scrollTime);

        RxTimerUtil.setTimerListener(new RxTimerUtil.RxTimerListener() {
            @Override
            public void doNext() {
                updateTipAndPlayAnimation();
            }
        });

        RxTimerUtil.start();
    }






    private HomeProductAdapter adapter;

    public HomeProductAdapter getAdapter() {
        if (adapter != null) {
            adapter = null;
        }
        adapter = new HomeProductAdapter(getContext(), getData(displayNumList, displayNum));
        adapter.setOnItemClickListener(new HomeProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String productCode, String sellingStatus) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(productCode, sellingStatus);
                }
            }

            @Override
            public void onProductStatus() {
                if (null != onItemClickListener) {
                    onItemClickListener.onProductStatus();
                }
            }
        });

        return adapter;
    }


    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> getData(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> data, int displayNum) {

        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> arrList = new ArrayList(data);
        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> displayNumList = new ArrayList<>();

        if (displayNum >= arrList.size()) {
            displayNum = arrList.size();
        }


        for (int i = 0; i < displayNum; i++) {
            displayNumList.add(arrList.get(i));
        }
        //删除前面获取的数据
        for (int i = 0; i < displayNumList.size(); i++) {
            arrList.remove(0);
        }
        //把前面获取的数据放置最后
        for (int i = 0; i < displayNumList.size(); i++) {
            arrList.add(displayNumList.get(i));
        }
        this.displayNumList = arrList;
        return displayNumList;
    }


    //设置view_tio_in的数据
    private void initViewIn() {

        view_tio_in.setAdapter(getAdapter());
        view_tio_in.setVisibility(GONE);
//        CCLog.e("view_tio_in-height" + view_tio_in.getMeasuredHeight());
//        CCLog.e("view_tip_out-height" + view_tip_out.getMeasuredHeight());
//        CCLog.e("this-height111" + getMeasuredHeight());
        setMeasuredDimension(view_tip_out.getMeasuredWidth(), view_tip_out.getHeight());
//        CCLog.e("this-height222" + getMeasuredHeight());

    }

    //设置view_tip_out的数据
    private void initViewOut() {

        view_tip_out.setAdapter(getAdapter());
        view_tip_out.setVisibility(GONE);
//        CCLog.e("view_tio_in-height" + view_tio_in.getMeasuredHeight());
//        CCLog.e("view_tip_out-height" + view_tip_out.getMeasuredHeight());
//        CCLog.e("this-height111" + getMeasuredHeight());
        setMeasuredDimension(view_tio_in.getMeasuredWidth(), view_tio_in.getHeight());
//        CCLog.e("this-height222" + getMeasuredHeight());
    }


    private AnimatorSet view_tio_in_animatorSet, view_tip_out_animatorSet;

    private ObjectAnimator view_tio_in_animator, view_tio_in_alpha;

    private ObjectAnimator view_tip_out_animator, view_tip_out_alpha;


    /*
     * 重置动画
     * */
    public void restAnimator() {
        if (view_tio_in_animator != null) {
            if (view_tio_in_animator.isRunning()) {
                view_tio_in_animator.end();
            }

            view_tio_in_animator = null;
        }

        if (view_tio_in_alpha != null) {
            if (view_tio_in_alpha.isRunning()) {
                view_tio_in_alpha.end();
            }

            view_tio_in_alpha = null;
        }

        if (view_tio_in_animatorSet != null) {
            if (view_tio_in_animatorSet.isRunning()) {
                view_tio_in_animatorSet.end();
                view_tio_in_animatorSet = null;
            }
        }


        if (view_tip_out_animator != null) {
            if (view_tip_out_animator.isRunning()) {
                view_tip_out_animator.end();
                view_tip_out_animator = null;
            }
        }

        if (view_tip_out_alpha != null) {
            if (view_tip_out_alpha.isRunning()) {
                view_tip_out_alpha.end();
                view_tip_out_alpha = null;
            }
        }

        if (view_tip_out_animatorSet != null) {
            if (view_tip_out_animatorSet.isRunning()) {
                view_tip_out_animatorSet.end();
                view_tip_out_animatorSet = null;
            }
        }
    }


    private void updateTipAndPlayAnimation() {
        restAnimator();
        if (curTipIndex % 2 == 0) {
            //第一个view退出，第二个view进来
            view_tip_out.setVisibility(VISIBLE);
            view_tip_out.bringToFront();

            view_tio_in_animator = ObjectAnimator.ofFloat(view_tio_in, "translationY", 0, -view_tio_in.getHeight(), 0);

            view_tio_in_alpha = ObjectAnimator.ofFloat(view_tio_in, "alpha", 1f, 0);

            view_tio_in_alpha.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    initViewIn();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });


            view_tip_out_animator = ObjectAnimator.ofFloat(view_tip_out, "translationY", view_tip_out.getHeight(), 0, 0);
            view_tip_out_alpha = ObjectAnimator.ofFloat(view_tip_out, "alpha", 0, 1f);

            if (view_tio_in_animatorSet != null) {
                view_tio_in_animatorSet.end();
                view_tio_in_animatorSet = null;
            }
            view_tio_in_animatorSet = new AnimatorSet();
            view_tio_in_animatorSet.play(view_tio_in_alpha).with(view_tio_in_animator).with(view_tip_out_animator).with(view_tip_out_alpha);
            view_tio_in_animatorSet.setDuration(duration);
            view_tio_in_animatorSet.start();

        } else {
            view_tio_in.setVisibility(VISIBLE);
            view_tio_in.bringToFront();
            view_tip_out_animator = ObjectAnimator.ofFloat(view_tip_out, "translationY", 0, -view_tip_out.getHeight(), 0);
            view_tip_out_alpha = ObjectAnimator.ofFloat(view_tip_out, "alpha", 1f, 0);

            view_tip_out_alpha.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    initViewOut();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            view_tio_in_animator = ObjectAnimator.ofFloat(view_tio_in, "translationY", view_tio_in.getHeight(), 0, 0);
            view_tio_in_alpha = ObjectAnimator.ofFloat(view_tio_in, "alpha", 0, 1f);
            if (view_tip_out_animatorSet != null) {
                view_tip_out_animatorSet.end();
                view_tip_out_animatorSet = null;
            }
            view_tip_out_animatorSet = new AnimatorSet();
            view_tip_out_animatorSet.play(view_tip_out_animator).with(view_tip_out_alpha).with(view_tio_in_alpha).with(view_tio_in_animator);
            view_tip_out_animatorSet.setDuration(duration);
            view_tip_out_animatorSet.start();
        }

        curTipIndex++;




    }


    public interface OnItemClickListener {

        void onItemClick(String productCode, String sellingStatus);

        void onProductStatus();

    }


    private RecyclerView newRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return recyclerView;
    }

}

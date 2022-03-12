package com.hundsun.zjfae.fragment.finance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.activity.product.SpvProductDetailActivity;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.DropDownMenu;
import com.hundsun.zjfae.fragment.BaseFragment;
import com.hundsun.zjfae.fragment.finance.adapter.ProductDefaultFilterAdapter;
import com.hundsun.zjfae.fragment.finance.adapter.ProductDefaultSortAdapter;
import com.hundsun.zjfae.fragment.finance.adapter.ProductTableClassificationAdapter;
import com.hundsun.zjfae.fragment.finance.adapter.ProductsAdapter;
import com.hundsun.zjfae.fragment.finance.bean.ProductDate;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

public class ProductFragment extends BaseFragment<ProductPresenter> implements ProductView, OnRefreshListener, OnLoadMoreListener {

    private DropDownMenu product_dropDownMenu;

    private View product_contentView;
    private SmartRefreshLayout productLayout;
    private ImageView image_not_date;
    private TextView no_date_tv;
    private RecyclerView product_view;
    private static String headers[] = {"默认排序", "筛选", "分类"};
    private ProductsAdapter productsAdapter;//适配器
    protected String uuids = "";
    protected String quanDetailsId = "";
    protected String quanUsedProductCode = "";
    protected String quanUsedSeriesCode = "";
    protected String productName = "";
    protected String keywordName = "";
    private int pageIndex = 1;//默认初始化为第一页产品列表
    private boolean isLoadMore = false;//是否下拉加载

    private ProductDefaultSortAdapter defaultSortAdapter;
    private List<AllAzjProto.PBAPPSearchSortControl_l2> defaultSortList;

    private ProductDefaultFilterAdapter filterAdapter;
    private ProductTableClassificationAdapter sortAdapter;
    private List<AllAzjProto.PBAPPSearchSortControl_l2> defaultFilterList, sortList;
    private List<View> productViewList = new ArrayList<>();

    private static final int requestCodes = 1030;
    /**
     * 定位当前item所在的位置
     */
    private int mPosition = 0;
    /**
     * //筛选第一栏高度
     */
    private int mHeight = 0;


    @Override
    public void onProductList(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew productListNew) {


        String returnCode = productListNew.getReturnCode();

        if (returnCode.equals("0000")) {

            initProductList(new ArrayList(productListNew.getData().getProductTradeInfoListList()));
        }

        reset(isLoadMore);
    }


    @Override
    public void onControl(AllAzjProto.PEARetControl productControl) {
        initProductDownMenu(productControl.getControlListList());
    }

    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, boolean isAuthentication) {

        if (isAuthentication) {

            certificateStatusType(userDetailInfo.getData().getVerifyName(), userDetailInfo.getData().getCertificateStatusType());

        } else {
            final String highNetWorthStatus = userDetailInfo.getData().getHighNetWorthStatus();
            final String isRealInvestor = userDetailInfo.getData().getIsRealInvestor();
            String userType = userDetailInfo.getData().getUserType();
            if (userDetailInfo.getData().getIsBondedCard().equals("false") && userType.equals("personal")) {

                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(mActivity, AddBankActivity.class);
                    }
                });

            } else if (!userDetailInfo.getData().getIsAccreditedInvestor().equals("1")) {

                // 合格投资者审核中
                if (highNetWorthStatus.equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if (userDetailInfo.getData().getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }
                    showDialog(pmtInfo);
                }
                // 合格投资者审核不通过
                else if (highNetWorthStatus.equals("0")) {
                    //查询原因
                    presenter.requestInvestorStatus(isRealInvestor);
                } else {
                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                }
            }
        }

    }

    @Override
    public void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, final String isRealInvestor) {

        StringBuffer buffer = new StringBuffer();
        for (UserHighNetWorthInfo.DictDynamic dynamic : body.getData().getDictDynamicListList()) {
            if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {
                buffer.append(dynamic.getAuditComment()).append("\n");
            }
        }
        showDialog(buffer.toString() + "".trim(), "重新上传", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("isRealInvestor", isRealInvestor);
                intent.setClass(mActivity, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });

    }


    @Override
    public void onInvestmentState(String isjump, final String jumpurl, String returnMsg, final String isShare) {

        if (!uuids.equals("")) {
            //针对刚注册用户首页选择理财图标，前置返回666666条件选择不更新问题
            List<AllAzjProto.PBAPPSearchSortControl_l1> control_list = presenter.getProductControl().getControlListList();

            if (control_list != null && !control_list.isEmpty()) {

                initProductDownMenu(control_list);
            }

        }
        image_not_date.setVisibility(View.VISIBLE);
        no_date_tv.setVisibility(View.GONE);
        product_view.setVisibility(View.GONE);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度（像素）
        int width = dm.widthPixels;

        ViewGroup.LayoutParams params = image_not_date.getLayoutParams();
        params.width = width - 50;
        params.height = width - 50;
        image_not_date.setLayoutParams(params);
        image_not_date.setClickable(true);
        image_not_date.setEnabled(true);


        if (!returnMsg.equals("")) {
            RequestOptions options = new RequestOptions().error(R.drawable.product_no_list).skipMemoryCache(true);
            ImageLoad.getImageLoad().loadImage(this, returnMsg, options, image_not_date);
        } else {

            ImageLoad.getImageLoad().LoadImage(this, R.drawable.product_no_list, image_not_date);
        }

        //跳转
        if (isjump.equals("true")) {
            image_not_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //url跳转
                    if (jumpurl.contains("http")) {
                        ShareBean shareBean = new ShareBean();
                        shareBean.setFuncUrl(jumpurl);
                        shareBean.setIsShare(isShare);
                        startWebActivity(shareBean);
                    }
                    //身份认证
                    else if (jumpurl.equals("authentication")) {
                        presenter.getUserData(true);
                    }
                    //合格投资者
                    else if (jumpurl.equals("callPhoneQualifiedInvestor")) {

                        if (!Utils.isFastDoubleClick()) {
                            presenter.getUserData(false);
                        }
                    } else {

                        image_not_date.setClickable(false);
                        image_not_date.setEnabled(false);
                    }

                }
            });

        } else {
            image_not_date.setClickable(false);
            image_not_date.setEnabled(false);
        }
        reset(isLoadMore);
    }

    private void initProductDownMenu(List<AllAzjProto.PBAPPSearchSortControl_l1> control_list) {

        if (defaultSortList == null) {
            defaultSortList = new ArrayList<>();
        } else {
            defaultSortList.clear();
        }

        if (defaultFilterList == null) {
            defaultFilterList = new ArrayList<>();
        } else {
            defaultFilterList.clear();
        }
        if (sortList == null) {
            sortList = new ArrayList<>();
        } else {
            sortList.clear();
        }

        defaultSortList.addAll(control_list.get(0).getControlsList());
        defaultFilterList.addAll(control_list.get(1).getControlsList());
        sortList.addAll(control_list.get(2).getControlsList());

        defaultSortAdapter.refresh(defaultSortList);
        filterAdapter.refresh(defaultFilterList);
        sortAdapter.refresh(sortList);
        if (uuids.equals("")) {
            return;
        } else {
            String str[] = uuids.split("-", -1);
            if (StringUtils.isNotBlank(str[0])) {
                for (AllAzjProto.PBAPPSearchSortControl_l2 control_l2 : control_list.get(0).getControlsList()) {
                    if (control_l2.getUuid().equals(str[0])) {
                        default_conditions = control_l2.getUuid();
                        defaultSortAdapter.refresh(defaultSortList, 0, product_dropDownMenu, control_l2.getControlName());
                        break;
                    }

                }
            }

            if (StringUtils.isNotBlank(str[1])) {

                for (AllAzjProto.PBAPPSearchSortControl_l2 control_l2 : control_list.get(1).getControlsList()) {

                    if (control_l2.getUuid().equals(str[1])) {
                        conditions = control_l2.getUuid();
                        filterAdapter.refresh(defaultFilterList, 1, product_dropDownMenu, control_l2.getControlHName());
                        break;
                    }

                }
            }


            if (StringUtils.isNotBlank(str[2])) {

                for (AllAzjProto.PBAPPSearchSortControl_l2 control_l2 : control_list.get(2).getControlsList()) {

                    if (control_l2.getUuid().equals(str[2])) {
                        grouping_conditions = control_l2.getUuid();
                        sortAdapter.refresh(sortList, 2, product_dropDownMenu, control_l2.getControlHName());
                        break;
                    }

                }
            }
        }

    }

    private String default_conditions = "", conditions = "", grouping_conditions = "";//选择条件

    private void initProductDownMenuItemClick() {
        //默认分类
        defaultSortAdapter.setItemOnClick(new ProductDefaultSortAdapter.ItemOnClick() {
            @Override
            public void onItemClick(int position) {
                default_conditions = defaultSortList.get(position).getUuid();
                defaultSortAdapter.setKeyWordNamePosition(0, product_dropDownMenu, position);
                product_dropDownMenu.closeMenu();
                uuids = default_conditions + "-" + conditions + "-" + grouping_conditions;
//                productLayout.autoRefresh();
                initProduct();
            }

            @Override
            public void setKeyWordName(String keyWordName) {
                keywordName = keyWordName;
            }
        });
        //默认排序
        filterAdapter.setItemOnClick(new ProductDefaultFilterAdapter.ItemOnClick() {
            @Override
            public void onItemClick(int position) {
                conditions = defaultFilterList.get(position).getUuid();
                filterAdapter.setKeyWordNamePosition(1, product_dropDownMenu, position);
                product_dropDownMenu.closeMenu();
                uuids = default_conditions + "-" + conditions + "-" + grouping_conditions;
                initProduct();
//                productLayout.autoRefresh();
            }

            @Override
            public void setKeyWordName(String keyWordName) {
                keywordName = keyWordName;
            }
        });

        //分类
        sortAdapter.setItemOnClick(new ProductDefaultFilterAdapter.ItemOnClick() {
            @Override
            public void onItemClick(int position) {
                sortAdapter.setKeyWordNamePosition(2, product_dropDownMenu, position);
                grouping_conditions = sortList.get(position).getUuid();
                product_dropDownMenu.closeMenu();
                uuids = default_conditions + "-" + conditions + "-" + grouping_conditions;
                initProduct();
//                productLayout.autoRefresh();
            }

            @Override
            public void setKeyWordName(String keyWordName) {
                keywordName = keyWordName;
            }
        });
    }

    //    //购买列表
    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.ProductTradeInfoNewObject>
            productList = null;//数据装载

    /*
     * 产品列表数据初始化
     * */
    private void initProductList(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.ProductTradeInfoNewObject> productFinanceDetailList) {
        if (productList == null) {
            productList = new ArrayList<>();
        }


        //下拉加载无更多数据
        if (isLoadMore && productFinanceDetailList.isEmpty()) {
            productLayout.finishLoadMoreWithNoMoreData();
        }
        //刷新无数据
        else if (!isLoadMore && productFinanceDetailList.isEmpty()) {
            image_not_date.setVisibility(View.VISIBLE);
            no_date_tv.setVisibility(View.VISIBLE);
            product_view.setVisibility(View.GONE);
            productLayout.finishLoadMoreWithNoMoreData();
            image_not_date.setClickable(false);
            image_not_date.setEnabled(false);
            if (!uuids.equals("")) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("当前的筛选条件");
                if (!product_dropDownMenu.getTabText(0).equals(headers[0])) {
                    buffer.append("\"" + product_dropDownMenu.getTabText(0) + "\"").append("、");
                }
                if (!product_dropDownMenu.getTabText(1).equals(headers[1])) {
                    buffer.append("\"" + product_dropDownMenu.getTabText(1) + "\"").append("、");
                }
                if (!product_dropDownMenu.getTabText(2).equals(headers[2])) {

                    buffer.append("\"" + product_dropDownMenu.getTabText(2) + "\"");
                }

                buffer.append("下没有产品，请清除筛选条件下搜索产品试试");
                no_date_tv.setText(buffer.toString());

                ViewGroup.LayoutParams params = image_not_date.getLayoutParams();
                params.width = 591;
                params.height = 546;
                image_not_date.setLayoutParams(params);
                ImageLoad.getImageLoad().LoadImage(this, R.drawable.bg_kong, image_not_date);
            } else {
                no_date_tv.setVisibility(View.GONE);

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                // 屏幕宽度（像素）
                int width = dm.widthPixels;

                ViewGroup.LayoutParams params = image_not_date.getLayoutParams();
                params.width = width - 50;
                params.height = width - 50;
                image_not_date.setLayoutParams(params);
                ImageLoad.getImageLoad().LoadImage(this, R.drawable.product_no_list, image_not_date);
            }


            //如果头部筛选布局不显示的话 显示布局
            if (product_dropDownMenu.getChildAt(0).getVisibility() != View.VISIBLE) {
                startShowAnimation(product_dropDownMenu.getChildAt(0));
            }
        } else {
            image_not_date.setVisibility(View.GONE);
            no_date_tv.setVisibility(View.GONE);
            productLayout.setNoMoreData(false);
            product_view.setVisibility(View.VISIBLE);

            if (isLoadMore) {
                productList.addAll(productFinanceDetailList);
                productList = removeDuplicateKeepOrder(productList);
                if (productsAdapter != null) {
                    productsAdapter.refresh(productList);
                }

            } else {
                if (productList != null && !productList.isEmpty()) {
                    productList.clear();
                }

                productList.addAll(productFinanceDetailList);
                if (productsAdapter != null) {
                    productsAdapter.refresh(productList);
                    if (mPosition != 0) {
                        if (mPosition >= 20) {
                            product_view.scrollToPosition(productsAdapter.getItemCount() - 1);
                        }
                    } else {
                        product_view.scrollToPosition(0);
                        //如果头部筛选布局不显示的话 显示布局
                        if (product_dropDownMenu.getChildAt(0).getVisibility() != View.VISIBLE) {
                            startShowAnimation(product_dropDownMenu.getChildAt(0));
                        }
                    }
                } else {
                    productsAdapter = new ProductsAdapter(mActivity, productList);
                    product_view.setAdapter(productsAdapter);
                    productsAdapter.setOnItemClickListener(new ProductsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            if (!Utils.isFastDoubleClick()) {
                                mPosition = position;
                                Intent intent = new Intent();
                                intent.putExtra("productCode", productList.get(position).getProductCode());
                                intent.putExtra("quanDetailsId", quanDetailsId);
                                intent.putExtra("sellingStatus", productList.get(position).getSellingStatus());
                                if (!"05".equals(productList.get(position).getProductSpecialArea())) {
                                    intent.putExtra("delegationCode", productList.get(position).getDelegationCode());
                                    intent.putExtra("leftDays", productList.get(position).getLeftDays());
                                    intent.putExtra("ifAllBuy", productList.get(position).getIfAllBuy());
                                    intent.putExtra("delegationId", productList.get(position).getDelegationId());
                                    intent.putExtra("ifAllBuy", productList.get(position).getIfAllBuy());
                                    intent.putExtra("delegateNum", productList.get(position).getDelegateNum());
                                    intent.setClass(getContext(), SpvProductDetailActivity.class);
                                } else {
                                    intent.setClass(getContext(), ProductCodeActivity.class);
                                }


                                startActivityForResult(intent, requestCodes);

                            }

                        }
                    });
                }
            }
        }

    }

    private void reset(boolean isLoadMore) {
        if (isLoadMore) {
            productLayout.finishLoadMore();
        } else {
            productLayout.finishRefresh();
        }
        this.isLoadMore = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCodes == requestCode && resultCode == RESULT_OK) {
            ProductDate.rest();
            product_dropDownMenu.resetSetTabText();
            this.uuids = ProductDate.uuids;
            this.keywordName = ProductDate.keywordName;
            this.productName = ProductDate.productName;
            this.quanDetailsId = ProductDate.quanDetailsId;
            this.quanUsedProductCode = ProductDate.quanUsedProductCode;
            this.quanUsedSeriesCode = ProductDate.quanUsedSeriesCode;
//            product_view.setVisibility(View.GONE);
//            productLayout.autoRefresh();
            initProduct();
        } else if (requestCodes == requestCode && resultCode != RESULT_OK) {
            initProduct(mPosition);
        }

    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onLoadMoreData();
    }


    private void onLoadMoreData() {
        isLoadMore = true;
        pageIndex = pageIndex + 1;
        presenter.initProduct(uuids, productName, quanDetailsId, quanUsedProductCode, quanUsedSeriesCode, pageIndex, "20");
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initProduct();
    }

    @Override
    protected void onRefresh() {
        initProduct();
    }

    @Override
    public void initData() {
        if (presenter != null && BaseActivity.isLogin) {
            product_dropDownMenu.resetSetTabText();
            product_dropDownMenu.closeMenu();
            this.uuids = ProductDate.uuids;
            this.default_conditions = "";
            this.conditions = "";
            this.grouping_conditions = "";
            this.keywordName = ProductDate.keywordName;
            this.productName = ProductDate.productName;
            this.quanDetailsId = ProductDate.quanDetailsId;
            this.quanUsedProductCode = ProductDate.quanUsedProductCode;
            this.quanUsedSeriesCode = ProductDate.quanUsedSeriesCode;
            initProduct();

        }

    }

    private void initProduct() {
        pageIndex = 1;
        mPosition = 0;
        presenter.initProduct(uuids, productName, quanDetailsId, quanUsedProductCode, quanUsedSeriesCode, pageIndex, "20");
    }

    private void initProduct(int mPosition) {

        productLayout.setNoMoreData(false);
        pageIndex = 1;

        int count = 0;
        int size = mPosition + 1;
        if (size % 20 == 0) {
            count = size / 20 * 20;
        } else {
            count = (size / 20 + 1) * 20;
        }
        count = 20;
        presenter.initProduct(uuids, productName, quanDetailsId, quanUsedProductCode, quanUsedSeriesCode, pageIndex, String.valueOf(count));
    }


    public void selectNavigation() {
        if (presenter != null) {
            product_dropDownMenu.closeMenu();
        }
        initProduct();
//        productLayout.autoRefresh();
    }


    @Override
    protected void initWidget() {
        product_dropDownMenu = (DropDownMenu) findViewById(R.id.product_dropDownMenu);
        product_contentView = mActivity.getLayoutInflater().inflate(R.layout.product_content_layout, null);
        productLayout = product_contentView.findViewById(R.id.smartLayout);
        image_not_date = product_contentView.findViewById(R.id.image_not_date);
        no_date_tv = product_contentView.findViewById(R.id.no_date_tv);
        productLayout.setOnRefreshListener(this);
        productLayout.setOnLoadMoreListener(this);
        product_view = product_contentView.findViewById(R.id.recyclerView);
        product_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //当停止滑动的时候
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                        //获取第一个完全可见view的位置
                        int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                            if (firstItemPosition > 0) {
                                if (product_dropDownMenu.getChildAt(0).getVisibility() == View.VISIBLE) {
                                    mHeight = product_dropDownMenu.getChildAt(0).getHeight();
                                    startHiddenAnimation(product_dropDownMenu.getChildAt(0));
                                }
                            } else {
                                if (product_dropDownMenu.getChildAt(0).getVisibility() == View.GONE) {
                                    startShowAnimation(product_dropDownMenu.getChildAt(0));
                                }
                            }
                        }
                    }
                }
            }
        });
        product_view.setLayoutManager(new

                LinearLayoutManager(mActivity));

        initDropDownMenu();
    }


    private void initDropDownMenu() {

        ProductSort productSort = new ProductSort();
        //默认排序
        RecyclerView defaultSort = productSort.getRecyclerView(mActivity);
        //默认筛选
        RecyclerView defaultFilter = productSort.getRecyclerView(mActivity);
        //分类
        RecyclerView sort = productSort.getRecyclerView(mActivity);
        defaultSortAdapter = new ProductDefaultSortAdapter(mActivity, defaultSortList);
        defaultSort.setAdapter(defaultSortAdapter);

        filterAdapter = new ProductDefaultFilterAdapter(mActivity, defaultFilterList);
        defaultFilter.setAdapter(filterAdapter);

        sortAdapter = new ProductTableClassificationAdapter(mActivity, sortList);
        sort.setAdapter(sortAdapter);
        productViewList.add(defaultSort);
        productViewList.add(defaultFilter);
        productViewList.add(sort);
        product_dropDownMenu.setDropDownMenu(Arrays.asList(headers), productViewList, product_contentView);
        product_dropDownMenu.closeMenu();
        initProductDownMenuItemClick();
    }


    @Override
    protected ProductPresenter createPresenter() {
        return new ProductPresenter(this);
    }

    @Override
    protected void resetLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.product_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_layout;
    }

    private void startShowAnimation(final View view) {
        // 显示动画
        view.setVisibility(View.VISIBLE);


    }

    private void startHiddenAnimation(final View view) {
        // 隐藏动画
        view.setVisibility(View.GONE);

    }

    @Override
    public boolean isShowLoad() {
        return false;
    }
}

package com.hundsun.zjfae.activity.product

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.hundsun.zjfae.R
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity
import com.hundsun.zjfae.activity.mine.AddBankActivity
import com.hundsun.zjfae.activity.moneymanagement.MyHoldingActivity
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay
import com.hundsun.zjfae.activity.product.config.TransferConfiguration.AMOUNT_NEW_SELECT_ENTITY
import com.hundsun.zjfae.activity.product.config.TransferConfiguration.CONDITION_NAME
import com.hundsun.zjfae.activity.product.config.TransferConfiguration.TIME_NEW_SELECT_ENTITY
import com.hundsun.zjfae.activity.product.config.TransferConfiguration.TRANSFER_ENTITY
import com.hundsun.zjfae.activity.product.config.TransferConfiguration.TransferRequestCodeConfiguration.SELECT_TRANSFER_RESULT_CODE
import com.hundsun.zjfae.activity.product.presenter.SelectConditionTransferListPresenter
import com.hundsun.zjfae.activity.product.view.SelectConditionTransferListView
import com.hundsun.zjfae.common.base.CommActivity
import com.hundsun.zjfae.common.user.UserInfoSharePre
import com.hundsun.zjfae.common.utils.CCLog
import com.hundsun.zjfae.common.utils.Utils
import com.hundsun.zjfae.common.utils.gilde.ImageLoad
import com.hundsun.zjfae.common.view.SolveClickTouchConflictLayout
import com.hundsun.zjfae.fragment.finance.adapter.TransferAdapter
import com.hundsun.zjfae.fragment.finance.bean.AmountNewSelectConditionEntity
import com.hundsun.zjfae.fragment.finance.bean.TimeNewSelectConditionEntity
import com.hundsun.zjfae.fragment.finance.bean.TransferSelectEntity
import com.hundsun.zjfae.fragment.finance.widget.ConditionLinearLayout
import com.hundsun.zjfae.fragment.finance.widget.ConditionLinearLayoutChildClickListener
import com.hundsun.zjfae.fragment.finance.widget.CustomSelectorLayout
import com.hundsun.zjfae.fragment.finance.widget.SelectConditionListener
import com.hundsun.zjfae.fragment.home.bean.ShareBean
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import onight.zjfae.afront.gens.UserHighNetWorthInfo
import onight.zjfae.afront.gens.v3.UserDetailInfo
import onight.zjfae.afront.gens.v4.TransferList
import onight.zjfae.afront.gens.v4.TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.properties.Delegates


/**
 * @ClassName:      SelectConditionTransferListActivity
 * @Description:    选择筛选条件后的转让列表
 * @Author:         moran
 * @CreateDate:     2021/5/13 14:13
 * @UpdateUser:     更新者：moran
 * @UpdateDate:     2021/5/13 14:13
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class SelectConditionTransferListActivity : CommActivity<SelectConditionTransferListPresenter>(),
    SelectConditionTransferListView, OnRefreshLoadMoreListener {

    private val requestCodes = 1031
    private val transfer_success_requestCodes = 1032
    private var condition_layout: ConditionLinearLayout by Delegates.notNull()

    private var ll_condition_layout: LinearLayout by Delegates.notNull()

    private var transferLayout: SmartRefreshLayout by Delegates.notNull()

    private var selector_amount_status: CustomSelectorLayout by Delegates.notNull()

    private var selector_time_status: CustomSelectorLayout by Delegates.notNull()


    //是否下拉加载
    private var isLoadMore = false


    private var image_not_date: ImageView by Delegates.notNull()

    private var no_date_tv: TextView by Delegates.notNull()

    //我要卖图标
    private var layout: SolveClickTouchConflictLayout by Delegates.notNull()
    private var transfer_view: RecyclerView by Delegates.notNull()

    private var btn_top: Button by Delegates.notNull()

    /**
     * 转让列表适配器
     */
    private var transferAdapter: TransferAdapter? = null


    private var tradeInfoList =
        mutableListOf<PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList>()


    override fun initView() {
        mTopDefineCancel = true
        condition_layout = findViewById(R.id.condition_layout)

        ll_condition_layout = findViewById(R.id.ll_condition_layout)
        transferLayout = findViewById(R.id.smartLayout)
        transferLayout.setNoMoreData(false)
        transferLayout.setOnRefreshLoadMoreListener(this)

        selector_amount_status = findViewById(R.id.selector_amount_status)
        selector_amount_status.defaultName = "起投金额"
        selector_time_status = findViewById(R.id.selector_time_status)
        selector_time_status.defaultName = "剩余期限"

        image_not_date = findViewById(R.id.image_not_date)
        no_date_tv = findViewById(R.id.no_date_tv)
        transfer_view = findViewById(R.id.recyclerView)
        layout = findViewById(R.id.layout)
        btn_top = findViewById(R.id.btn_up)


        val contentView = layoutInflater.inflate(R.layout.child_layout, null)
        contentView.findViewById<View>(R.id.text_view).setOnClickListener {
            val intent = Intent(
                this,
                MyHoldingActivity::class.java
            )
            //startActivityForResult(intent, TransferFragment.transfer_success_requestCodes)
            startActivity(intent)
        }
        layout.setContentView(contentView)


        transfer_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position > 1) {
                        btn_top.visibility = View.VISIBLE
                        btn_top.setOnClickListener {
                            transfer_view.scrollToPosition(0)
                            btn_top.visibility = View.GONE
                            //init()
                        }
                    } else {
                        if (btn_top.visibility == View.VISIBLE) {
                            btn_top.visibility = View.GONE
                        }
                    }

                }
            }
        })

    }

    /**
     * 剩余期限选择的新条件数据封装
     * */
    val amountNewSelectConditionEntity = AmountNewSelectConditionEntity()

    /**
     * 起投金额筛选的新条件数据封装
     * */
    val timeNewSelectConditionEntity = TimeNewSelectConditionEntity()

    override fun initData() {

        val json = intent.getStringExtra(TRANSFER_ENTITY)

        CCLog.i("TransferSelectEntity", json)

        val entity = Gson().fromJson(json, TransferSelectEntity::class.java)

        entity.transferIncomeList?.let {
            ll_condition_layout.visibility = View.VISIBLE
            condition_layout.addConditionView(it, entity.selectEntity, object :
                ConditionLinearLayoutChildClickListener {
                override fun onChildClick(uuid: String, selectConditionName: String) {
                    pageIndex = 1
                    presenter.incomeUUid = uuid
                    presenter.transferList()

                }
            })
        }

        presenter.amountUuid = entity.amountUUids

        presenter.timeUuid = entity.timeUUids

        amountNewSelectConditionEntity.amountUuid = entity.amountUUids

        amountNewSelectConditionEntity.amountSelectConditionName =
            ArrayList(entity.amountSelectConditionName)

        amountNewSelectConditionEntity.amountSelectMap = HashMap(entity.amountSelectMap)


        timeNewSelectConditionEntity.timeUuid = entity.timeUUids

        timeNewSelectConditionEntity.timeSelectConditionName =
            ArrayList(entity.timeSelectConditionName)

        timeNewSelectConditionEntity.timeSelectMap = HashMap(entity.timeSelectMap)


        entity.selectEntity.let {

            if (!it.selectUUID.isNullOrEmpty()) {

                presenter.incomeUUid = it.selectUUID[0]
            }

        }

        selector_amount_status.setSelectText(entity.amountSelectConditionName)
        selector_amount_status.updateSelectorData(
            entity.amountTransferSortList,
            entity.amountSelectMap
        )

        selector_amount_status.setSelectConditionListener(object : SelectConditionListener {
            /**
             * @描述一下方法的作用
             * @method
             * @date: 2021/5/19 19:46
             * @author: moran
             * @param uuidList 选中条件的uuid集合
             * @param selectMap 选中条件状态map集合
             * @param conditionNameList 选中条件名字集合
             */
            override fun onItemOnClick(
                uuidList: MutableList<String>,
                selectMap: MutableMap<Int, Boolean>,
                conditionNameList: MutableList<String>
            ) {
                selector_amount_status.setSelectText(conditionNameList)
                var amountUUids = ""

                if (uuidList.isNotEmpty()) {
                    val uuidBuilder = StringBuilder()
                    uuidList.forEach {
                        uuidBuilder.append(it)
                        uuidBuilder.append("|")
                    }
                    amountUUids = uuidBuilder.substring(0, uuidBuilder.toString().length - 1)

                    CCLog.i("amountuuids", amountUUids)
                }
                amountNewSelectConditionEntity.amountUuid = amountUUids

                amountNewSelectConditionEntity.amountSelectConditionName =
                    ArrayList(conditionNameList)

                amountNewSelectConditionEntity.amountSelectMap = HashMap(selectMap)

                presenter.amountUuid = amountUUids


                presenter.transferList()
            }
        })

        selector_time_status.setSelectText(entity.timeSelectConditionName)

        selector_time_status.updateSelectorData(entity.timeTransferSortList, entity.timeSelectMap)

        selector_time_status.setSelectConditionListener(object : SelectConditionListener {
            /**
             * @描述一下方法的作用
             * @method
             * @date: 2021/5/19 19:46
             * @author: moran
             * @param uuidList 选中条件的uuid集合
             * @param selectMap 选中条件状态map集合
             * @param conditionNameList 选中条件名字集合
             */
            override fun onItemOnClick(
                uuidList: MutableList<String>,
                selectMap: MutableMap<Int, Boolean>,
                conditionNameList: MutableList<String>
            ) {
                selector_time_status.setSelectText(conditionNameList)

                var timeUUids = ""

                if (uuidList.isNotEmpty()) {

                    val uuidBuilder = StringBuilder()
                    uuidList.forEach {
                        uuidBuilder.append(it)
                        uuidBuilder.append("|")
                    }

                    timeUUids = uuidBuilder.substring(0, uuidBuilder.toString().length - 1)

                    CCLog.i("timetuuids", timeUUids)
                }
                presenter.timeUuid = timeUUids
                timeNewSelectConditionEntity.timeUuid = timeUUids

                timeNewSelectConditionEntity.timeSelectConditionName = ArrayList(conditionNameList)

                timeNewSelectConditionEntity.timeSelectMap = HashMap(selectMap)



                presenter.transferList()
            }

        })

        presenter.transferList()


    }


    /**
     * 获取布局ID
     *
     * @param
     * @return int 布局ID
     * @description 获取布局Id
     * @date: 2019/6/10 13:42
     * @author: moran
     */
    override fun getLayoutId(): Int {

        return R.layout.activity_select_condition_transfer_list
    }

    override fun createPresenter(): SelectConditionTransferListPresenter {


        return SelectConditionTransferListPresenter(this)
    }

    /**
     * 转让列表
     * @param listNew 转让列表
     */
    override fun initTransfer(listNew: TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew) {
        initTransferList(listNew.data.productTradeInfoListList)
        reset(isLoadMore)
    }

    /**
     * 用户详细信息
     * @param userDetailInfo 用户详细信息
     */
    override fun onUserInfo(
        userDetailInfo: UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?,
        isAuthentication: Boolean
    ) {
        if (isAuthentication) {
            certificateStatusType(
                userDetailInfo!!.data.verifyName,
                userDetailInfo.data.certificateStatusType
            )
        } else {
            val highNetWorthStatus = userDetailInfo!!.data.highNetWorthStatus
            val isRealInvestor = userDetailInfo.data.isRealInvestor
            val userType = userDetailInfo.data.userType
            if (userDetailInfo.data.isBondedCard == "false" && userType == "personal") {
                showDialog(
                    "为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消"
                ) { dialog, which -> //去绑卡
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else if (userDetailInfo.data.isAccreditedInvestor != "1") {

                // 合格投资者审核中
                if (highNetWorthStatus == "-1") {
                    var pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。"
                    if (userDetailInfo.data.isAccreditedInvestor == "1") {
                        pmtInfo = "您的合格投资者认定材料正在审核中"
                    }
                    showDialog(pmtInfo)
                } else if (highNetWorthStatus == "0") {
                    //查询原因
                    presenter.requestInvestorStatus(isRealInvestor)
                } else {
                    if (userType == "personal") {
                        showUserLevelDialog("000", isRealInvestor)
                    } else {
                        showUserLevelDialog("020", isRealInvestor)
                    }
                }
            }
        }
    }

    /**
     * 失败原因
     * @param body 用户详细信息
     * @param isRealInvestor  是否真正合格投资者
     */
    override fun requestInvestorStatus(
        body: UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?,
        isRealInvestor: String?
    ) {
        val buffer = StringBuffer()
        for (dynamic in body!!.data.dictDynamicListList) {
            if (dynamic.auditComment != "0" && dynamic.auditComment != "1") {
                buffer.append(dynamic.auditComment).append("\n")
            }
        }
        showDialog(
            buffer.toString() + "".trim { it <= ' ' }, "重新上传", "取消"
        ) { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            intent.putExtra("isRealInvestor", isRealInvestor)
            intent.setClass(this, HighNetWorthMaterialsUploadedActivity::class.java)
            baseStartActivity(intent)
        }

    }

    /**
     * 列表为空时展示的图片
     *
     * @param isjump = 是否跳转
     * @param jumpurl  跳转形式，url，合格投资者，身份认证
     * @param returnMsg 图片链接
     * @param isShare 是否分享
     *
     */
    override fun onInvestmentState(
        isjump: String?,
        jumpurl: String?,
        returnMsg: String?,
        isShare: String?
    ) {

        image_not_date.isClickable = true
        image_not_date.isEnabled = true
        image_not_date.visibility = View.VISIBLE
        no_date_tv.visibility = View.GONE
        transfer_view.visibility = View.GONE

        val wm = this.getSystemService(WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        // 屏幕宽度（像素）
        // 屏幕宽度（像素）
        val width = dm.widthPixels

        val params = image_not_date.layoutParams
        params.width = width - 50
        params.height = width - 50
        image_not_date.layoutParams = params
        if (returnMsg != "") {
            ImageLoad.getImageLoad().LoadImage(this, returnMsg, image_not_date)
        } else {
            // 屏幕宽度（像素）
            val layoutParams = image_not_date.layoutParams
            layoutParams.width = 640
            layoutParams.height = 438
            image_not_date.visibility = View.VISIBLE
            image_not_date.isClickable = false
            image_not_date.isEnabled = false
            image_not_date.layoutParams = layoutParams
            ImageLoad.getImageLoad().LoadImage(this, R.drawable.transfer_bg_kong, image_not_date)
        }

        //跳转

        //跳转
        if (isjump == "true") {
            image_not_date.setOnClickListener { //url跳转
                if (jumpurl!!.contains("http")) {
                    val shareBean = ShareBean()
                    shareBean.funcUrl = jumpurl
                    shareBean.isShare = isShare
                    startWebActivity(shareBean)
                } else if (jumpurl == "authentication") {
                    presenter.getUserData(true)
                } else if (jumpurl == "callPhoneQualifiedInvestor") {
                    if (!Utils.isFastDoubleClick()) {
                        presenter.getUserData(false)
                    }
                } else {
                    image_not_date.isClickable = false
                    image_not_date.isEnabled = false
                }
            }
        } else {
            image_not_date.isClickable = false
            image_not_date.isEnabled = false
        }
        reset(isLoadMore)
    }


    private fun initTransferList(productTradeInfoList: List<PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList>) {

        //加载更多无数据
        if (isLoadMore && productTradeInfoList.isEmpty()) {
            //初始化为空
            transferLayout.finishLoadMoreWithNoMoreData()
        } else if (!isLoadMore && productTradeInfoList.isEmpty()) {
            no_date_tv.visibility = View.GONE
            val wm = this.getSystemService(WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            // 屏幕宽度（像素）
            val layoutParams = image_not_date.layoutParams
            layoutParams.width = 640
            layoutParams.height = 438
            image_not_date.visibility = View.VISIBLE
            image_not_date.isClickable = false
            image_not_date.isEnabled = false
            image_not_date.layoutParams = layoutParams
            ImageLoad.getImageLoad().LoadImage(this, R.drawable.transfer_bg_kong, image_not_date)
            transfer_view.visibility = View.GONE
            transferLayout.finishLoadMoreWithNoMoreData()

        } else {
            image_not_date.visibility = View.GONE
            no_date_tv.visibility = View.GONE
            transferLayout.setNoMoreData(false)
            transfer_view.visibility = View.VISIBLE
            if (!isLoadMore && tradeInfoList.isNotEmpty()) {
                tradeInfoList.clear()
            }
            tradeInfoList.addAll(productTradeInfoList)
            if (isLoadMore) {
                transferAdapter?.refresh(tradeInfoList)
            } else {
                transferAdapter = TransferAdapter(this, tradeInfoList)
                transfer_view.adapter = transferAdapter
                transferAdapter?.setClickCallBack(TransferAdapter.ItemClickCallBack { position ->
                    if (Utils.isFastDoubleClick()) {
                        return@ItemClickCallBack
                    }
                    val intent = Intent(
                        this,
                        TransferDetailActivity::class.java
                    )
                    val bundle = Bundle()
                    val playBean = TransferDetailPlay()
                    playBean.delegationId = tradeInfoList[position].delegationId
                    playBean.ifAllBuy = tradeInfoList[position].ifAllBuy
                    playBean.isMyEntry =
                        UserInfoSharePre.getTradeAccount() == tradeInfoList[position].tradeAccount
                    bundle.putParcelable("playInfo", playBean)
                    intent.putExtra("playBundle", bundle)
                    //baseStartActivity(intent)
                    startActivityForResult(intent, requestCodes)
                })
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCodes == requestCode && resultCode == RESULT_OK) {
            topDefineCancel()
        }
    }

    override fun topDefineCancel() {
        val intent = Intent()
        intent.putExtra(CONDITION_NAME, condition_layout.getTransferIncomeNewSelectEntity())

        intent.putExtra(AMOUNT_NEW_SELECT_ENTITY, Gson().toJson(amountNewSelectConditionEntity))

        intent.putExtra(TIME_NEW_SELECT_ENTITY, Gson().toJson(timeNewSelectConditionEntity))

        setResult(SELECT_TRANSFER_RESULT_CODE, intent)
        finish()
    }

    private fun reset(isLoadMore: Boolean) {
        if (isLoadMore) {
            transferLayout.finishLoadMore()
        } else {
            transferLayout.finishRefresh()
        }
        this.isLoadMore = false
    }


    private var pageIndex = 1
    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageIndex = 1
        presenter.transferList(pageIndex)

    }


    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isLoadMore = true
        pageIndex += 1
        presenter.transferList(pageIndex)


    }


}
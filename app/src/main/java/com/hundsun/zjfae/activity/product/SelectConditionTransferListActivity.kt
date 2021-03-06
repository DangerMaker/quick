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
 * @Description:    ????????????????????????????????????
 * @Author:         moran
 * @CreateDate:     2021/5/13 14:13
 * @UpdateUser:     ????????????moran
 * @UpdateDate:     2021/5/13 14:13
 * @UpdateRemark:   ???????????????
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


    //??????????????????
    private var isLoadMore = false


    private var image_not_date: ImageView by Delegates.notNull()

    private var no_date_tv: TextView by Delegates.notNull()

    //???????????????
    private var layout: SolveClickTouchConflictLayout by Delegates.notNull()
    private var transfer_view: RecyclerView by Delegates.notNull()

    private var btn_top: Button by Delegates.notNull()

    /**
     * ?????????????????????
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
        selector_amount_status.defaultName = "????????????"
        selector_time_status = findViewById(R.id.selector_time_status)
        selector_time_status.defaultName = "????????????"

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
     * ??????????????????????????????????????????
     * */
    val amountNewSelectConditionEntity = AmountNewSelectConditionEntity()

    /**
     * ??????????????????????????????????????????
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
             * @???????????????????????????
             * @method
             * @date: 2021/5/19 19:46
             * @author: moran
             * @param uuidList ???????????????uuid??????
             * @param selectMap ??????????????????map??????
             * @param conditionNameList ????????????????????????
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
             * @???????????????????????????
             * @method
             * @date: 2021/5/19 19:46
             * @author: moran
             * @param uuidList ???????????????uuid??????
             * @param selectMap ??????????????????map??????
             * @param conditionNameList ????????????????????????
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
     * ????????????ID
     *
     * @param
     * @return int ??????ID
     * @description ????????????Id
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
     * ????????????
     * @param listNew ????????????
     */
    override fun initTransfer(listNew: TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew) {
        initTransferList(listNew.data.productTradeInfoListList)
        reset(isLoadMore)
    }

    /**
     * ??????????????????
     * @param userDetailInfo ??????????????????
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
                    "??????????????????????????????????????????????????????", "?????????", "??????"
                ) { dialog, which -> //?????????
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else if (userDetailInfo.data.isAccreditedInvestor != "1") {

                // ????????????????????????
                if (highNetWorthStatus == "-1") {
                    var pmtInfo = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????"
                    if (userDetailInfo.data.isAccreditedInvestor == "1") {
                        pmtInfo = "????????????????????????????????????????????????"
                    }
                    showDialog(pmtInfo)
                } else if (highNetWorthStatus == "0") {
                    //????????????
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
     * ????????????
     * @param body ??????????????????
     * @param isRealInvestor  ???????????????????????????
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
            buffer.toString() + "".trim { it <= ' ' }, "????????????", "??????"
        ) { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            intent.putExtra("isRealInvestor", isRealInvestor)
            intent.setClass(this, HighNetWorthMaterialsUploadedActivity::class.java)
            baseStartActivity(intent)
        }

    }

    /**
     * ??????????????????????????????
     *
     * @param isjump = ????????????
     * @param jumpurl  ???????????????url?????????????????????????????????
     * @param returnMsg ????????????
     * @param isShare ????????????
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
        // ????????????????????????
        // ????????????????????????
        val width = dm.widthPixels

        val params = image_not_date.layoutParams
        params.width = width - 50
        params.height = width - 50
        image_not_date.layoutParams = params
        if (returnMsg != "") {
            ImageLoad.getImageLoad().LoadImage(this, returnMsg, image_not_date)
        } else {
            // ????????????????????????
            val layoutParams = image_not_date.layoutParams
            layoutParams.width = 640
            layoutParams.height = 438
            image_not_date.visibility = View.VISIBLE
            image_not_date.isClickable = false
            image_not_date.isEnabled = false
            image_not_date.layoutParams = layoutParams
            ImageLoad.getImageLoad().LoadImage(this, R.drawable.transfer_bg_kong, image_not_date)
        }

        //??????

        //??????
        if (isjump == "true") {
            image_not_date.setOnClickListener { //url??????
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

        //?????????????????????
        if (isLoadMore && productTradeInfoList.isEmpty()) {
            //???????????????
            transferLayout.finishLoadMoreWithNoMoreData()
        } else if (!isLoadMore && productTradeInfoList.isEmpty()) {
            no_date_tv.visibility = View.GONE
            val wm = this.getSystemService(WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            // ????????????????????????
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
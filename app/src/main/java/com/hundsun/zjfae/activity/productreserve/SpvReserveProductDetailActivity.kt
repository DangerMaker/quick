package com.hundsun.zjfae.activity.productreserve

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import com.hundsun.zjfae.R
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity
import com.hundsun.zjfae.activity.mine.AddBankActivity
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity
import com.hundsun.zjfae.activity.product.AgeFragmentDialog
import com.hundsun.zjfae.activity.product.DisclaimerActivity
import com.hundsun.zjfae.activity.product.HighActivity
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean
import com.hundsun.zjfae.activity.product.listener.LockScreenBroadcastReceiver
import com.hundsun.zjfae.activity.product.listener.LockScreenListener
import com.hundsun.zjfae.activity.product.presenter.SpvProductDetailPresenter
import com.hundsun.zjfae.activity.product.util.ScheduledExecutorUtils
import com.hundsun.zjfae.activity.product.util.ScheduledExecutorUtils.ScheduledExecutorListener
import com.hundsun.zjfae.activity.product.view.SpvProductDetailView
import com.hundsun.zjfae.common.base.CommActivity
import com.hundsun.zjfae.common.user.ADSharePre
import com.hundsun.zjfae.common.user.BaseCacheBean
import com.hundsun.zjfae.common.user.UserInfoSharePre
import com.hundsun.zjfae.common.utils.CCLog
import com.hundsun.zjfae.common.utils.MoneyUtil
import com.hundsun.zjfae.common.utils.StringUtils
import com.hundsun.zjfae.common.utils.Utils
import com.hundsun.zjfae.common.view.dialog.EarningsDialog
import com.hundsun.zjfae.common.view.dialog.PurchaseDialog
import kotlinx.android.synthetic.main.activity_spv_product_detail.*
import onight.zjfae.afront.gens.Attachment
import onight.zjfae.afront.gens.UserHighNetWorthInfo
import onight.zjfae.afront.gens.v2.PBIFEPrdtransferqueryPrdDeliveryInfoDetail
import onight.zjfae.afront.gens.v2.TransferOrderSec
import onight.zjfae.afront.gens.v3.UserDetailInfo
import onight.zjfae.afront.gens.v3.UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo
import onight.zjfae.afront.gens.v3.UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo

class SpvReserveProductDetailActivity : CommActivity<SpvProductDetailPresenter>(),
    SpvProductDetailView,
    View.OnClickListener {
    private val requestCodes = 0x701

    /**
     *???????????????
     * */
    private var playInfo: ProductPlayBean = ProductPlayBean()

    /**
     * ????????????????????????
     * */
    private lateinit var productAmountDialog: PurchaseDialog.Builder

    /**
     * ???????????????
     * */
    private lateinit var earBuilder: EarningsDialog.Builder
    private var rate = "0"
    private var amount = "0"
    private var timeLimit = "0"
    private var sellingStatus: String = ""
    private var isHideButton: Boolean = false  //????????????????????????


    /**
     * ??????????????????????????????,???????????????????????????
     */
    private var userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo? = null

    override fun createPresenter(): SpvProductDetailPresenter {
        return SpvProductDetailPresenter(this)
    }

    override fun topDefineCancel() {
        ScheduledExecutorUtils.cancel()
        finish()
    }

    override fun getLayoutId(): Int {

        return R.layout.activity_spv_product_detail
    }

    override fun initView() {
        product_buy.setOnClickListener(this)
        calculator.setOnClickListener(this)
    }


    override fun initData() {
        isHideButton = intent.getBooleanExtra("isHideButton", false)

        presenter.productCode = intent.getStringExtra("productCode")

        presenter.delegationCode = intent.getStringExtra("delegationCode")

        presenter.delegationId = intent.getStringExtra("delegationId")

        playInfo.ifAllBuy = intent.getStringExtra("ifAllBuy")

        //?????????????????????????????????????????????
        playInfo.delegationCode = presenter.delegationCode

        productAmountDialog = PurchaseDialog.Builder(this)
        productAmountDialog.setTitle("?????????????????????")
        productAmountDialog.setMessage("0")
        productAmountDialog.setNegativeButton("??????") { dialog, which -> dialog.dismiss() }
        productAmountDialog.setPurchaseMoney { dialog, money ->
            dialog.dismiss()
            if (Utils.isStringEmpty(money)) {
                showDialog("????????????????????????")
            } else {
                playInfo.playAmount = money
                presenter.getTransferOrderSec(money)
            }
        }

        setDisclaimer()

        if (isHideButton) {
            attach_layout.visibility = View.GONE
            lin_product_buy.visibility = View.GONE
        } else {
            attach_layout.visibility = View.VISIBLE
            lin_product_buy.visibility = View.VISIBLE
        }

        presenter.initProductData()

    }

    //?????????????????????????????????????????????????????????????????????????????????
    fun setDisclaimer() {
        if (ADSharePre.getConfiguration(ADSharePre.disclaimer, BaseCacheBean::class.java) == null) {
            val clicktext = "?????????????????????????????????"
            tv_agreement.text = clicktext
            attach_layout.setOnClickListener(this)
            product_check.isClickable = false
            return
        }
        val baseCacheBean =
            ADSharePre.getConfiguration(ADSharePre.disclaimer, BaseCacheBean::class.java)
        if (StringUtils.isNotBlank(baseCacheBean.button_title)) {
            CCLog.e("??????????????????" + baseCacheBean.button_title)
            val full = SpannableStringBuilder()
            val clickText1 = "????????????????????????????????????"
            val clickText2 = baseCacheBean.button_title
            val before = SpannableStringBuilder()
            val after = SpannableStringBuilder()
            before.append(clickText1)
            before.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    product_check.isChecked = !product_check.isChecked
                }

                override fun updateDrawState(ds: TextPaint) {
                    //super.updateDrawState(ds); --> ?????????????????????????????????
                }
            }, 0, before.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // 1. click
            before.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.black)), 0, before.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            after.append(clickText2)
            after.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    baseStartActivity(
                        this@SpvReserveProductDetailActivity,
                        DisclaimerActivity::class.java
                    )
                }

                override fun updateDrawState(ds: TextPaint) {}
            }, 0, after.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) // 1. click
            after.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.blue)), 0, after.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            ) // 2. color
            full.append(before).append(after).append("\u200b")
            tv_agreement.movementMethod = LinkMovementMethod.getInstance()
            tv_agreement.text = full
            product_check.isClickable = true
        } else {
            val clicktext = "?????????????????????????????????"
            tv_agreement.text = clicktext
            attach_layout.setOnClickListener(this)
            product_check.isClickable = false
        }
    }

    /**
     * ??????????????????
     * @param orderSecBean ??????????????????
     */
    override fun getTransferOrderSecBean(orderSecBean: TransferOrderSec.Ret_PBIFE_trade_transferOrderSec?) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("product_info", playInfo)
        intent.putExtra("product_bundle", bundle)
        intent.putExtra("orderType", "4");
        intent.setClass(this, SpvReserveProductPlayActivity::class.java)
        startActivityForResult(intent, requestCodes)

    }

    /**
     * ????????????
     * @param body ???????????????????????????
     * @param isRealInvestor ????????????????????????
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
        showDialog(buffer.toString() + "".trim { it <= ' ' }, "????????????", "??????") { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            intent.putExtra("isRealInvestor", isRealInvestor)
            intent.setClass(this, HighNetWorthMaterialsUploadedActivity::class.java)
            baseStartActivity(intent)
        }
    }

    /**
     * ??????????????????????????????
     * @param userDetailInfo ??????????????????
     */
    override fun getUserDetailInfo(userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo?) {
        this.userDetailInfo = userDetailInfo
        val userType = userDetailInfo!!.data.userType
        if (userDetailInfo.data.isFundPasswordSet == "false") {
            showDialog("?????????????????????????????????", "??????????????????", "??????") { dialog, which -> //?????????????????????
                dialog.dismiss()
                baseStartActivity(this, FirstPlayPassWordActivity::class.java)
            }
        } else if (userDetailInfo.data.verifyName != "1" && userType == "personal") {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                dialog.dismiss()
                baseStartActivity(this, AddBankActivity::class.java)
            }
        } else if (userDetailInfo.data.isRiskTest == "false" && userType == "personal") {
            showDialog("????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                dialog.dismiss()
                baseStartActivity(this, RiskAssessmentActivity::class.java)
            }
        } else if (userDetailInfo.data.isRiskExpired == "true" && userType == "personal") {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????") { dialog, which -> //?????????
                dialog.dismiss()
                baseStartActivity(this, RiskAssessmentActivity::class.java)
            }
        } else {


            productAmountDialog.create().show()
        }
    }

    /**
     * ????????????????????????????????????
     * @param userDetailInfo ??????????????????
     * @param accreditedBuyIs ???????????????????????????
     * @param attachmentList ????????????
     * @param riskLevelLabelName ??????????????????
     * @param riskLevelLabelUrl ????????????Url
     * @param isAttachment ????????????????????????
     */
    override fun getAttachmentUserInfo(
        userDetailInfo: UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?,
        accreditedBuyIs: String?,
        attachmentList: Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList?,
        riskLevelLabelName: String?,
        riskLevelLabelUrl: String?,
        isAttachment: Boolean
    ) {
        val isAccreditedInvestor = userDetailInfo!!.data.isAccreditedInvestor
        val isRealInvestor = userDetailInfo!!.data.isRealInvestor
        val highNetWorthStatus = userDetailInfo!!.data.highNetWorthStatus
        val userType = userDetailInfo!!.data.userType
        if (accreditedBuyIs == "1" && isAccreditedInvestor != "1") {
            if (userDetailInfo!!.data.isBondedCard == "false" && userType == "personal") {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else if (isAccreditedInvestor != "1") {
                // ????????????????????????
                if (highNetWorthStatus == "-1") {
                    var pmtInfo = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????"
                    if (userDetailInfo!!.data.isAccreditedInvestor == "1") {
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
            } else {
                if (isAttachment) {
                    //????????????
                    val id = attachmentList!!.id
                    val title = attachmentList!!.title
                    openAttachment(id, title, "9")
                } else {
                    //????????????
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl)
                }
            }
        } else {
            if (userDetailInfo!!.data.verifyName != "1" && userType == "personal") {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else {
                if (isAttachment) {
                    //????????????
                    val id = attachmentList!!.id
                    val title = attachmentList!!.title
                    openAttachment(id, title, "9")
                } else {
                    //????????????
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl)
                }
            }
        }
    }

    /**
     * ????????????
     * @param productDetails ??????????????????
     */
    override fun onProductData(
        productDetails: PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail,
        requestTime: Long
    ) {
        val data = productDetails.data.taProductFinanceDetail

        playInfo.buyRemainAmount = productDetails.data.tcDelegation.delegateNum
        sellingStatus = data.sellingStatus
        val leftDays = productDetails.data.leftDays
        playInfo.deadline = leftDays
        deadline.setText(leftDays)
        timeLimit = leftDays

        //??????ID
        val quanDetailsId = intent.getStringExtra("quanDetailsId")
        playInfo.quanDetailsId = quanDetailsId
        playInfo.serialNoStr = data.serialNo
        playInfo.productCode = data.productCode
        productName.setText(data.productName)
        playInfo.productName = data.productName
        playInfo.expectedMaxAnnualRate = data.expectedMaxAnnualRate
        playInfo.buyerSmallestAmount = data.leastTranAmount
        playInfo.isWholeTransfer = data.isWholeTransfer
        //???????????????????????????
        if ("1" == data.isWholeTransfer) {
            ll_isWholeTransfer.visibility = View.VISIBLE
        } else {
            ll_isWholeTransfer.visibility = View.GONE
        }

        //playInfo.serialNoStr = data.serialNo
        playInfo.isTransfer = data.isTransferStr
        //????????????
        if (playInfo.ifAllBuy == "1") {
            productAmountDialog.setMessage(productDetails.data.tcDelegation.delegateNum)
            productAmountDialog.setEdit(false)
        } else {
            productAmountDialog.setMessage(data.leastTranAmount)
            productAmountDialog.setEdit(true)
        }
        expectedMaxAnnualRate.text = data.expectedMaxAnnualRate + "%"

        buyerSmallestAmount.text = MoneyUtil.formatMoney2(data.leastTranAmount)


        rate = data.expectedMaxAnnualRate

        amount = data.leastTranAmount
        //????????????????????????
        if (StringUtils.isNotBlank(data.riskLevelLabelValue)) {
            riskLevel.setText(data.riskLevelLabelValue)
            riskLevel.setVisibility(View.VISIBLE)
            riskLevel_view.setVisibility(View.VISIBLE)
        } else {
            riskLevel.setVisibility(View.GONE)
            riskLevel_view.setVisibility(View.GONE)
        }
        if (StringUtils.isNotBlank(data.riskLevelLabelURL)) {
            riskLevel.setTextColor(resources.getColor(R.color.blue))
            riskLevel.setOnClickListener(View.OnClickListener {
                val accreditedBuyIs = data.accreditedBuyIs
                presenter.attachmentUserInfo(
                    accreditedBuyIs,
                    null,
                    productDetails.data.ratingTitle,
                    data.riskLevelLabelURL,
                    false
                )
            })
        }
        buyTotalAmount.text = MoneyUtil.formatMoney2(data.buyTotalAmount) + "???"
        buyRemainAmount.text = MoneyUtil.formatMoney2(playInfo.buyRemainAmount) + "???"
        isTransferStr.text = data.isTransferStr
        manageEndDate.text = data.manageEndDate
        tradeIncrease.text = MoneyUtil.formatMoney2(data.tradeIncrease) + "???"
        if (data.isTransferStr == "?????????") {
            tradeLeastHoldDay.text = data.tradeLeastHoldDay + "????????????"
        } else {
            tradeLeastHoldDay.text = "--"
        }
        payStyle.text = data.payStyle
        playInfo.payStyle = data.payStyle
        canBuyNum.setText(data.canBuyNum)
        tradeStartDate_start_time.setText(data.tradeStartDate)
        tradeStartDate_end_time.setText(data.tradeEndDate)
        if (data.isTransferStr == "?????????") {
            lin_transfer_start.setVisibility(View.VISIBLE)
            lin_transfer_end.setVisibility(View.VISIBLE)
        } else {
            lin_transfer_start.setVisibility(View.GONE)
            lin_transfer_end.setVisibility(View.GONE)
        }
        if (data.remark != null && data.remark != "") {
            remark_layout.setVisibility(View.VISIBLE)
            remark.setText(data.remark)
        }

        val taProductFinanceDetail: PBIFEPrdtransferqueryPrdDeliveryInfoDetail.PBIFE_prdtransferquery_prdDeliveryInfoDetail.TaProductFinanceDetail =
            productDetails.data.taProductFinanceDetail
        productStatus(taProductFinanceDetail, requestTime)
    }

    //??????????????????
    private fun productStatus(
        taProductFinanceDetail: PBIFEPrdtransferqueryPrdDeliveryInfoDetail.PBIFE_prdtransferquery_prdDeliveryInfoDetail.TaProductFinanceDetail,
        requestTime: Long
    ) {
        //0-???????????????1-????????????2-????????????3-?????????
        //countDownSec?????????
        val countDownSec: String = taProductFinanceDetail.getCountDownSec()
//        val countDownSec: String = "100"
//        val remain = taProductFinanceDetail.buyRemainAmount
//        if (remain == "0") {
//            sellingStatus = "2"
//        }
        when (sellingStatus) {
            "0" -> {
                if (countDownSec.isBlank()) {
                    return
                }
                product_buy.isEnabled = false
                product_buy.isClickable = false
                //product_buy.setText("????????????");
                val countTime = countDownSec.trim { it <= ' ' }.toLong()
                CCLog.e("countTime", countTime)
                CCLog.e("requestTime", requestTime)
                val baseCountTime = countTime - requestTime
                ScheduledExecutorUtils.setCountTime(baseCountTime)
                ScheduledExecutorUtils.setExecutorListener(object : ScheduledExecutorListener {
                    override fun onSuccess() {
                        product_buy.isClickable = true
                        product_buy.isEnabled = true
                        product_buy.text = "????????????"
                        product_buy.setBackgroundResource(R.drawable.product_buy_clickable)
                    }

                    override fun onNoClick(tv: String) {
                        product_buy.isClickable = false
                        product_buy.isEnabled = false
                        product_buy.text = tv
                        product_buy.setBackgroundResource(R.drawable.product_buy)
                    }
                })
                ScheduledExecutorUtils.startTime()
                registerReceiver()
            }
            "1" -> {
                product_buy.isEnabled = true
                product_buy.isClickable = true
                product_buy.text = "????????????"
                product_buy.setBackgroundResource(R.drawable.product_buy_clickable)
            }
            "2" -> {
                product_buy.isEnabled = false
                product_buy.isClickable = false
                product_buy.text = "?????????"
                product_buy.setBackgroundResource(R.drawable.product_buy)
            }
            "3" -> {
                product_buy.isEnabled = false
                product_buy.isClickable = false
                product_buy.text = "?????????"
                product_buy.setBackgroundResource(R.drawable.product_buy)
            }
            "4" -> {
                product_buy.isEnabled = false
                product_buy.isClickable = false
                product_buy.text = "?????????"
                product_buy.setBackgroundResource(R.drawable.product_buy)
            }
            else -> {
                product_buy.isEnabled = false
                product_buy.isClickable = false
                product_buy.text = "????????????"
                product_buy.setBackgroundResource(R.drawable.product_buy)
            }
        }
    }

    private var lockScreenBroadcastReceiver: LockScreenBroadcastReceiver? = null

    private fun registerReceiver() {
        if (lockScreenBroadcastReceiver == null) {
            lockScreenBroadcastReceiver = LockScreenBroadcastReceiver(object : LockScreenListener {
                override fun onScreenOn() {
                    // ??????
                    CCLog.e("TAG", "??????")
                }

                override fun onScreenOff() {
                    // ??????
                    CCLog.e("TAG", "??????")
                }

                override fun onUserPresent() {
                    // ??????
                    CCLog.e("TAG", "??????")
                    presenter.initProductData()
                }
            })
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
            intentFilter.addAction(Intent.ACTION_SCREEN_ON)
            intentFilter.addAction(Intent.ACTION_USER_PRESENT)
            registerReceiver(lockScreenBroadcastReceiver, intentFilter)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        ScheduledExecutorUtils.cancel()
        finish()
        return true
    }

    override fun onDestroy() {
        unregisterReceiver()
        super.onDestroy()
    }

    private fun unregisterReceiver() {
        if (lockScreenBroadcastReceiver != null) {
            unregisterReceiver(lockScreenBroadcastReceiver)
        }
    }

    /**
     * ??????????????????
     * @param userDetailInfo ??????????????????
     * @param accreditedBuyIs ???????????????????????????
     */
    override fun onUserDetailInfo(
        userDetailInfo: UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?,
        accreditedBuyIs: String?
    ) {

        val userDetailInfo: PBIFE_userbaseinfo_getUserDetailInfo = userDetailInfo!!.getData()
        val isRealInvestor = userDetailInfo.isRealInvestor
        val userType = userDetailInfo.userType
        if (userDetailInfo.isFundPasswordSet == "false") {
            showDialog("?????????????????????????????????", "??????????????????", "??????") { dialog, which -> //?????????????????????
                dialog.dismiss()
                baseStartActivity(this, FirstPlayPassWordActivity::class.java)
            }
        } else if (userDetailInfo.verifyName != "1" && userType == "personal") {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                dialog.dismiss()
                baseStartActivity(this, AddBankActivity::class.java)
            }
        } else if (accreditedBuyIs == "1" && userDetailInfo.isAccreditedInvestor != "1") {
            if (userDetailInfo.isBondedCard == "false" && userType == "personal") {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else {
                // 0???????????????
                if (userDetailInfo.highNetWorthStatus == "0") {
                    presenter.requestInvestorStatus(isRealInvestor)
                } else if (userDetailInfo.highNetWorthStatus != "-1") {
                    if (userType == "personal") {
                        showUserLevelDialog("000", isRealInvestor)
                    } else {
                        showUserLevelDialog("020", isRealInvestor)
                    }
                    //showUserLevelDialog("000",isRealInvestor);
                }
            }
        } else if (userDetailInfo.isRiskTest == "false" && userType == "personal") {
            showDialog("????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                dialog.dismiss()
                baseStartActivity(this, RiskAssessmentActivity::class.java)
            }
        } else if (userDetailInfo.isRiskExpired == "true" && userType == "personal") {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????") { dialog, which -> //?????????
                dialog.dismiss()
                baseStartActivity(this, RiskAssessmentActivity::class.java)
            }
        }

    }

    /**
     * ??????????????????
     * @param attachmentList ??????????????????
     * @param accreditedBuyIs ???????????????????????????
     */
    override fun onAttachmentInfo(
        attachmentList: Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment?,
        accreditedBuyIs: String?
    ) {

        val list = attachmentList!!.data.taProductAttachmentListList

        if (list == null || list.isEmpty()) {
            attach_layout.visibility = View.GONE
            return
        }
        val adapter = AttachmentAdapter(this, list)
        adapter.setOnItemClickListener { position ->
            presenter.attachmentUserInfo(
                accreditedBuyIs,
                list[position],
                "",
                "",
                true
            )
        }
        attachment.adapter = adapter
    }

    /**
     * 65??????
     */
    override fun onHighAge() {

        val ageFragmentDialog = AgeFragmentDialog()

        ageFragmentDialog.setOnClickListener(object : AgeFragmentDialog.OnClickListener {
            override fun isReadProtocol(isRead: Boolean) {
                ageFragmentDialog.dismissDialog()
                if (isRead) {
                    presenter.ageRequest()
                }
            }

            override fun cancel() {
                ageFragmentDialog.dismissDialog()
                finish()
            }
        })
        ageFragmentDialog.show(supportFragmentManager)
    }

    /**
     * ????????????????????????
     * @param returnMsg ????????????
     */
    override fun onNovicePrompt(returnMsg: String?) {

        showDialog(returnMsg, "??????", "??????") { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            val bundle = Bundle()
            bundle.putParcelable("product_info", playInfo)
            intent.putExtra("product_bundle", bundle)
            intent.putExtra("orderType", "4");
            intent.setClass(this, SpvReserveProductPlayActivity::class.java)
            startActivityForResult(intent, requestCodes)
        }
    }

    /**
     * ???????????????????????????
     * @param subscribeProductSec
     */
    override fun onUpDataUserPurchaseAmount(subscribeProductSec: TransferOrderSec.Ret_PBIFE_trade_transferOrderSec?) {
        buyRemainAmount.text = subscribeProductSec!!.data.remainBuyAmount
        showDialog(subscribeProductSec.returnMsg)
    }

    /**
     * ?????????????????????
     * @param returnMsg ????????????
     */
    override fun onQualifiedMember(returnMsg: String?) {

        val userType = UserInfoSharePre.getUserType()
        if (userDetailInfo?.getData()?.getIsBondedCard() == "false" && userType == "personal") {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                dialog.dismiss()
                baseStartActivity(this, AddBankActivity::class.java)
            }
        } else {
            showDialog(returnMsg, "????????????", "??????") { dialog, which ->
                dialog.dismiss()
                if (userType == "personal") {
                    showUserLevelDialog("000", "")
                } else {
                    showUserLevelDialog("020", "")
                }

                //showUserLevelDialog("000","");
            }
        }
    }

    /**
     *
     * ????????????????????????
     * @param returnMsg ????????????
     */
    override fun onPlayNoTransfer(returnMsg: String?) {

        showDialog(
            returnMsg,
            "????????????",
            "????????????",
            { dialog, which -> dialog.dismiss() }) { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            val bundle = Bundle()
            bundle.putParcelable("product_info", playInfo)
            intent.putExtra("product_bundle", bundle)
            intent.putExtra("orderType", "4")
            intent.setClass(this, SpvReserveProductPlayActivity::class.java)
            startActivityForResult(intent, requestCodes)
        }
    }

    /**
     * ????????????
     * @param returnMsg ????????????
     */
    override fun onRiskAssessment(returnMsg: String?) {

        showDialog(returnMsg, "??????", "??????") { dialog, which -> //?????????
            dialog.dismiss()
            baseStartActivity(this, RiskAssessmentActivity::class.java)
        }
    }

    /**
     * ???????????????
     * @param returnMsg ????????????
     */
    override fun onSeniorMember(returnMsg: String?) {

        showDialog(returnMsg, "??????", "??????") { dialog, which ->
            dialog.dismiss()
            baseStartActivity(this, HighActivity::class.java)
        }
    }

    override fun isShowLoad(): Boolean {

        return false
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.attach_layout -> {
                product_check.isChecked = !product_check.isChecked
            }


            //?????????
            R.id.calculator -> {
                earBuilder = EarningsDialog.Builder(this)
                earBuilder.setAmount(amount)
                earBuilder.setRate(rate)
                earBuilder.setTimeLimit(timeLimit)
                val earningsDialog: EarningsDialog = earBuilder.create()
                earningsDialog.show()
                val windowManager = windowManager
                val display = windowManager.defaultDisplay
                val lp = earningsDialog.window.attributes
                //????????????
                //????????????
                lp.width = (display.width * 0.75).toInt()
                earningsDialog.window.attributes = lp

            }

            R.id.product_buy -> {
                if (attach_layout.visibility != View.GONE) {
                    if (!product_check.isChecked) {
                        product_scroll.fullScroll(NestedScrollView.FOCUS_DOWN)
                        showDialog("??????????????????????????????????????????")
                        return
                    }
                }
                if (playInfo.buyRemainAmount?.isNullOrEmpty() == true || playInfo.buyRemainAmount == "0") {
                    showDialog("???????????????????????????????????????")
                    return
                }
                presenter.getUserDate()
            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodes && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
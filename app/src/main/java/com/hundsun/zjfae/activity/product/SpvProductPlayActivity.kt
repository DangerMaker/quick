package com.hundsun.zjfae.activity.product

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hundsun.zjfae.R
import com.hundsun.zjfae.activity.mine.AddBankActivity
import com.hundsun.zjfae.activity.mine.CompanyOfflineRechargeActivity
import com.hundsun.zjfae.activity.mine.OfflineRechargeActivity
import com.hundsun.zjfae.activity.mine.RechargeActivity
import com.hundsun.zjfae.activity.product.bean.CardVoucherBean
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean
import com.hundsun.zjfae.activity.product.bean.RadEnvelopeBean
import com.hundsun.zjfae.activity.product.presenter.SpvProductPlayPresenter
import com.hundsun.zjfae.activity.product.view.SpvProductPlayView
import com.hundsun.zjfae.common.base.CommActivity
import com.hundsun.zjfae.common.user.UserInfoSharePre
import com.hundsun.zjfae.common.utils.CCLog
import com.hundsun.zjfae.common.utils.MoneyUtil
import com.hundsun.zjfae.common.view.dialog.CardDictionaryDialog
import com.hundsun.zjfae.common.view.dialog.CustomDialog
import com.hundsun.zjfae.common.view.popwindow.PlayWindow
import kotlinx.android.synthetic.main.activity_spv_product_play.*
import onight.zjfae.afront.gens.FundBankInfo
import onight.zjfae.afront.gens.QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan
import onight.zjfae.afront.gens.QueryPayInit
import onight.zjfae.afront.gensazj.Dictionary
import java.text.DecimalFormat
import java.util.*

class SpvProductPlayActivity : CommActivity<SpvProductPlayPresenter>(),SpvProductPlayView, View.OnClickListener {

    private val QUAN_LIST_REQUEST_CODE = 0x7071
    private val BAO_LIST_REQUEST_CODE = 0x7072
    private val QUAN_LIST_RESULT_CODE = 0x758
    private val BAO_LIST_RESULT_CODE = 0x759

    private val PLAY_CODE = 0x789


    private val RECHARGE_CODE = 0x790


    //支付信息类
    private lateinit var playInfo: ProductPlayBean

    override fun createPresenter(): SpvProductPlayPresenter {

        return  SpvProductPlayPresenter(this)
    }

    override fun getLayoutId(): Int {

        return R.layout.activity_spv_product_play
    }

    override fun initView() {
        play.setOnClickListener(this)


    }


    override fun initData() {
        val bundle = intent.getBundleExtra("product_bundle")
        playInfo = bundle.getParcelable("product_info")
        expectedMaxAnnualRate.setText(playInfo.expectedMaxAnnualRate + "%")
        tv_play_type.text = playInfo.payStyle
        tv_isTransfer.text = playInfo.isTransfer
        deadline.setText(playInfo.deadline + "天")
        productName.setText(playInfo.productName + "")
        presenter.init(playInfo.productCode, playInfo.playAmount, playInfo.serialNoStr,playInfo.delegationCode)
    }


    override fun onClick(v: View?) {

        when(v?.id){

            R.id.choose_quan_layout ->{
                val intent = Intent(this, ChooseQuanActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("playQuan", playInfo)
                intent.putExtra("quanBundle", bundle)
                startActivityForResult(intent, QUAN_LIST_REQUEST_CODE)
            }

            R.id.choose_bao ->{

                val baoIntent = Intent(this, ChooseBaoActivity::class.java)
                val baoBundle = Bundle()
                baoBundle.putParcelable("playBao", playInfo)
                baoIntent.putExtra("baoBundle", baoBundle)
                startActivityForResult(baoIntent, BAO_LIST_REQUEST_CODE)
            }

            R.id.play -> {

                //余额
                val balanceAmount = playInfo.balanceY
                //支付总额
                val totalPayAmount = playInfo.totalPayAmount

                val isComp = PlayBaoInfo.moneyComp(totalPayAmount, balanceAmount)

                CCLog.e("isComp", isComp)

                if (PlayBaoInfo.moneyComp(totalPayAmount, balanceAmount)) {
                    val userType = UserInfoSharePre.getUserType()
                    if (userType == "personal") {
                        if (presenter.getIsBondedCard() == "true") {
                            showDialog("您的账户余额不足，请先对账户进行充值！", "去充值", "取消") { dialog, which ->
                                dialog.dismiss()
                                presenter.queryBankInfo()
                            }
                        } else {
                            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消") { dialog, which ->
                                dialog.dismiss()
                                val intent = Intent(this, AddBankActivity::class.java)
                                intent.putExtra("isPlay", true)
                                baseStartActivity(intent)
                            }
                        }
                    } else {
                        baseStartActivity(this, CompanyOfflineRechargeActivity::class.java)
                    }
                } else {
                    val cardVoucherList = playInfo.cardVoucherList
                    //红包集合
                    val radEnvelopeList = playInfo.radEnvelopeList
                    val playBaoInfo = playInfo.playBaoInfo


                    //卡券或者红包不为空
                    if (cardVoucherList != null && !cardVoucherList.isEmpty() || radEnvelopeList != null && !radEnvelopeList.isEmpty()) {
                        var playMap: HashMap<String?, Any?>? = null
                        var playList: List<HashMap<*, *>?>? = null
                        if (playBaoInfo != null) {
                            playMap = playBaoInfo.playMap
                            playList = playBaoInfo.playList
                        }
                        if ((playMap == null || playMap.isEmpty()) && (playList == null || playList.isEmpty())) {
                            val builder = CustomDialog.Builder(this)
                            builder.setTitle("温馨提示")
                            builder.setMessage("您确定不使用优惠券/红包吗？")
                            builder.setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
                            builder.setPositiveButton("确认") { dialog, which ->
                                dialog.dismiss()
                                isPlay()
                            }
                            builder.create().show()
                        } else {
                            isPlay()
                        }
                    } else {
                        isPlay()
                    }
                }
            }
        }



    }

    private fun isPlay() {
        val play = PlayWindow(this)
        play.showAtLocation(findViewById(R.id.content_layout))
        play.setPayListener { password -> //支付
            playInfo.playPassWord = password
            presenter.playTransferDetail(playInfo)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLAY_CODE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        } else if (requestCode == QUAN_LIST_REQUEST_CODE || requestCode ==  BAO_LIST_REQUEST_CODE) {
            //卡券选择
            if (resultCode == QUAN_LIST_RESULT_CODE) {
                if (data != null) {
                    val bundle = data.getBundleExtra("quanBundle")
                    if (bundle != null) {
                        playInfo = bundle.getParcelable("playQuan")
                        calculatePlayMoney()
                    }
                }
            } else if (resultCode == BAO_LIST_RESULT_CODE) {
                if (data != null) {
                    val bundle = data.getBundleExtra("baoBundle")
                    if (bundle != null) {
                        playInfo = bundle.getParcelable("playBao")
                        calculatePlayMoney()
                    }
                }
            }
        } else if (requestCode == RECHARGE_CODE && resultCode == Activity.RESULT_OK) {
            presenter.rechargePlayAmount()
        }
    }



    /**
     * 支付信息初始化
     * @param payInit 支付信息
     */
    override fun onPlayInit(payInit: QueryPayInit.Ret_PBIFE_trade_queryPayInit?) {

        payAmount.text = payInit!!.data.payInitWrap.payAmount + "元"
        balanceY.text = payInit.data.payInitWrap.balanceY
        total_payAmount.text = MoneyUtil.fmtMicrometer(payInit.data.payInitWrap.payAmount)
        playInfo.balanceY = payInit.data.payInitWrap.balanceY
    }

    /**
     * 查询用户卡券信息
     * @param myKqQuan 用户卡券
     */
    override fun onUserKquanInfo(myKqQuan: Ret_PBIFE_trade_queryMyKqQuan?) {
        initCoupon(myKqQuan!!)
    }

    //初始化卡券信息
    private fun initCoupon(queryQuanInfo: Ret_PBIFE_trade_queryMyKqQuan) {
        quan_tv_info.text = ""
        quan_value.text = "0.00元"
        bao_tv_info.text = ""
        bao_value.text = "0.00元"
        val voucherSize = queryQuanInfo.data.quanSize
        val cardSize = queryQuanInfo.data.baoSize
        quan_number.text = "($voucherSize)"
        bao_number.text = "($cardSize)"
        val cardVoucherList: MutableList<CardVoucherBean> = ArrayList()

        //卡券遍历
        if (!queryQuanInfo.data.quanListList.isEmpty()) {
            for (quanList in queryQuanInfo.data.quanListList) {
                val cardVoucherBean = CardVoucherBean()
                cardVoucherBean.quanDetailsId = quanList.quanDetailsId
                cardVoucherBean.quanType = quanList.quanType
                cardVoucherBean.quanCatalogRemark = quanList.quanCatalogRemark
                cardVoucherBean.quanTypeName = quanList.quanTypeName
                cardVoucherBean.quanValue = quanList.quanValue
                cardVoucherBean.quanCanStack = quanList.quanCanStack
                cardVoucherBean.quanValidityEnd = quanList.quanValidityEnd
                cardVoucherBean.quanIncreaseInterestAmount = quanList.quanIncreaseInterestAmount
                cardVoucherBean.enableIncreaseInterestAmount = quanList.enableIncreaseInterestAmount
                cardVoucherBean.percentValue = quanList.percentValue
                cardVoucherBean.quanName = quanList.quanName
                cardVoucherBean.quanFullReducedAmount = quanList.quanFullReducedAmount
                cardVoucherBean.catalogRemark = quanList.quanCatalogRemark
                cardVoucherBean.quanArrivalPriceLadder = quanList.quanArrivalPriceLadder
                cardVoucherBean.mostFineFlag = quanList.mostFineFlag
                cardVoucherList.add(cardVoucherBean)
            }
        }
        val radEnvelopeList: MutableList<RadEnvelopeBean> = ArrayList()
        //红包遍历
        if (!queryQuanInfo.data.baoListList.isEmpty()) {
            for (baoList in queryQuanInfo.data.baoListList) {
                val radEnvelopeBean = RadEnvelopeBean()
                radEnvelopeBean.quanCanStack = baoList.quanCanStack
                radEnvelopeBean.quanDetailsId = baoList.quanDetailsId
                radEnvelopeBean.quanName = baoList.quanName
                radEnvelopeBean.quanType = baoList.quanType
                radEnvelopeBean.quanTypeName = baoList.quanTypeName
                radEnvelopeBean.quanValue = baoList.quanValue
                radEnvelopeBean.quanValidityEnd = baoList.quanValidityEnd
                radEnvelopeBean.quanCatalogRemark = baoList.quanCatalogRemark
                radEnvelopeBean.quanFullReducedAmount = baoList.quanFullReducedAmount
                radEnvelopeBean.mostFineFlag = baoList.mostFineFlag
                radEnvelopeList.add(radEnvelopeBean)
            }
        }
        playInfo.voucherSize = voucherSize
        playInfo.cardVoucherList = cardVoucherList
        playInfo.cardSize = cardSize
        playInfo.radEnvelopeList = radEnvelopeList

        //卡券
        if (!cardVoucherList.isEmpty()) {
            choose_quan_layout.setOnClickListener(this)
            for (cardVoucherBean in cardVoucherList) {
                if (cardVoucherBean.mostFineFlag == "1") {
                    defaultQuan(cardVoucherBean)
                    break
                }
            }
        }
        //红包
        if (!radEnvelopeList.isEmpty()) {
            choose_bao.setOnClickListener(this)
            for (radEnvelopeBean in radEnvelopeList) {
                if (radEnvelopeBean.mostFineFlag == "1") {
                    defaultBao(radEnvelopeBean)
                }
            }
        }
    }

    val playBaoInfo = PlayBaoInfo()

    private fun defaultQuan(cardVoucherBean: CardVoucherBean) {
        val playMap: HashMap<String?, Any?> = HashMap()


        //金额
        val value = cardVoucherBean.quanValue
        //卡券类型
        val type = cardVoucherBean.quanType
        //卡券id
        val id = cardVoucherBean.quanDetailsId
        val kqAddRatebj = cardVoucherBean.enableIncreaseInterestAmount
        playMap["value"] = value
        playMap["type"] = type
        playMap["id"] = id
        playMap["kqAddRatebj"] = kqAddRatebj
        playBaoInfo.playMap = playMap
        playInfo.playBaoInfo = playBaoInfo
        calculatePlayMoney()
    }

    private val playList: MutableList<HashMap<*, *>> = ArrayList()

    private fun defaultBao(radEnvelopeBean: RadEnvelopeBean) {
        val playMap: HashMap<String?, Any?> = HashMap()
        //金额
        val value = radEnvelopeBean.quanValue
        //卡券类型
        val type = radEnvelopeBean.quanType
        //卡券id
        val id = radEnvelopeBean.quanDetailsId
        //是否可叠加
        val quanCanStack = radEnvelopeBean.quanCanStack
        playMap["value"] = value
        playMap["type"] = type
        playMap["id"] = id
        playMap["quanCanStack"] = quanCanStack
        playList.add(playMap)
        playBaoInfo.playList = playList
        playInfo.playBaoInfo = playBaoInfo
        calculatePlayMoney()
    }


    //计算金额
    private fun calculatePlayMoney() {

        //红包信息
        var playList: List<HashMap<*, *>?>? = ArrayList()
        //卡券
        var playMap: HashMap<String?, Any?>? = HashMap()
        val playBaoInfo = playInfo.playBaoInfo
        if (playBaoInfo != null) {
            playList = playBaoInfo.playList
            playMap = playBaoInfo.playMap
        }
        if (playList == null) {
            if (!playInfo.radEnvelopeList.isEmpty()) {
                bao_tv_info.text = "未选择"
                bao_value.text = "0.00元"
            } else {
                bao_tv_info.text = ""
                bao_value.text = "0.00元"
            }
        } else {
            //未选择红包
            if (playList.isEmpty() && !playInfo.radEnvelopeList.isEmpty()) {
                bao_tv_info.text = "未选择"
                bao_value.text = "0.00元"
            } else if (!playList.isEmpty() && !playInfo.radEnvelopeList.isEmpty()) {
                bao_tv_info.text = if (playList != null && !playList.isEmpty()) "已选" + playList.size + "个红包" else "未选择"
                val baoAmount = PlayBaoInfo.allBaoAmount(playBaoInfo!!.playList)
                bao_value.text = "-" + baoAmount + "元"
            } else if (playList.isEmpty() && playInfo.radEnvelopeList.isEmpty()) {
                bao_tv_info.text = ""
                bao_value.text = "0.00元"
            }
        }


        //卡券
        if (playMap == null) {
            if (!playInfo.cardVoucherList.isEmpty()) {
                quan_tv_info.text = "未选择"
                quan_value.text = "0.00元"
            } else {
                quan_tv_info.text = ""
                quan_value.text = "0.00元"
            }
        } else {
            if (playMap.isEmpty()) {
                quan_tv_info.text = "未选择"
                quan_value.text = "0.00元"
            } else {
                //金额
                val value = playMap["value"] as String?
                //卡券类型
                val type = playMap["type"] as String?
                val kqAddRatebj = playMap["kqAddRatebj"] as String?
                when (type) {
                    "A" -> {
                        val percentage = value!!.toDouble()
                        val df = DecimalFormat("0.00")
                        val percent = df.format(percentage * 100)
                        playInfo.playBaoInfo.kqAddRatebj = kqAddRatebj
                        quan_tv_info.text = "已选 加息券$percent%"
                        quan_value.text = "0.00元"
                    }
                    "L" -> {
                        quan_tv_info.text = "已选 抵价券$value"
                        quan_value.text = "-" + value + "元"
                    }
                    "F" -> {
                        quan_tv_info.text = "已选 满减券" + value + "元"
                        quan_value.text = "-" + value + "元"
                    }
                    else -> {
                    }
                }
            }
        }
        var totalPayAmount = playInfo.playAmount
        totalPayAmount = PlayBaoInfo.allAmount(playBaoInfo!!.playMap, playBaoInfo.playList, totalPayAmount)
        playInfo.totalPayAmount = totalPayAmount
        total_payAmount.text = MoneyUtil.fmtMicrometer(totalPayAmount)
    }

    override fun playProduct(returnCode: String?, returnMsg: String?) {

        if (returnCode == "0000") {
            val intent = Intent(this, ProductPlayStateActivity::class.java)
            intent.putExtra("playState", "交易成功")
            startActivityForResult(intent, PLAY_CODE)
        } else if (returnCode == "0012") {
            showDialog(returnMsg)
        } else {
            showDialog(returnMsg)
        }
    }


    private var bankName = ""
    private  var bankCard = ""
    /**
     * 银行卡信息
     * @param bankName 银行名字
     * @param bankCard 银行卡号
     * @param bankNo 银行编号
     * @param showTips 渠道是否关闭/暂停 0，不是，1是
     */
    override fun queryRechargeBankInfo(bankName: String?, bankCard: String?, bankNo: String?, showTips: String?) {
        this.bankName = bankName!!
        this.bankCard = bankCard!!
        //充值渠道关闭
        if (showTips == "1") {
            val intent = Intent(this, OfflineRechargeActivity::class.java)
            intent.putExtra("bankName", bankName)
            intent.putExtra("bankCard", bankCard)
            intent.putExtra("type", "301")
            baseStartActivity(intent)
        } else {
            //查询银行卡限额
            presenter.queryFundBankInfo(bankNo)
        }
    }

    /**
     * 银行卡信息
     * @param bankName 银行名字
     * @param bankCard 银行卡号
     * @param bankNo 银行编号
     * @param showTips 渠道是否关闭/暂停 0，不是，1是
     * @param payChannelNo 支付渠道
     */
    override fun queryRechargeBankInfo(bankName: String?, bankCard: String?, bankNo: String?, showTips: String?, payChannelNo: String?) {
        presenter.setPayChannelNo(payChannelNo)
        queryRechargeBankInfo(bankName, bankCard, bankNo, showTips)
    }

    /**
     * 限额信息
     */
    override fun bankCardManage(fundBankInfo: FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo?) {
        if (fundBankInfo!!.data.showTips == "1") {
            val intent = Intent(this, OfflineRechargeActivity::class.java)
            val totalPayAmount = total_payAmount.text.toString().replace(",", "")
            val balance = balanceY.text.toString()
            val payBanlance = MoneyUtil.moneySub(totalPayAmount, balance)
            intent.putExtra("payAmount", payBanlance)
            intent.putExtra("bankName", bankName)
            intent.putExtra("bankCard", bankCard)
            intent.putExtra("type", "302")
            intent.putExtra("tag", "isProduct")
            startActivityForResult(intent, RECHARGE_CODE)
        } else {
            val totalPayAmount = total_payAmount.text.toString().replace(",", "")
            val balance = balanceY.text.toString()
            val payBanlance = MoneyUtil.moneySub(totalPayAmount, balance)
            val intent = Intent(this, RechargeActivity::class.java)
            intent.putExtra("payAmount", payBanlance)
            intent.putExtra("tag", "isProduct")
            startActivityForResult(intent, RECHARGE_CODE)
        }
    }

    /**
     * 卡券使用说明
     * @param dictionary 卡券使用说明
     */
    override fun onKQDescription(dictionary: Dictionary.Ret_PBAPP_dictionary?) {

        findViewById<View>(R.id.kq_description_layout).setOnClickListener { // showDialog(dictionary.getData().getParms(0).getKeyCode());
            val dialog = CardDictionaryDialog(this)
            dialog.contextStr = dictionary!!.data.getParms(0).keyCode
            dialog.createDialog().show()
        }
    }

    override fun rechargePlayAmount(queryPayInit: QueryPayInit.Ret_PBIFE_trade_queryPayInit?) {
        balanceY.text = queryPayInit!!.data.payInitWrap.balanceY
        playInfo.balanceY = queryPayInit.data.payInitWrap.balanceY
    }
}
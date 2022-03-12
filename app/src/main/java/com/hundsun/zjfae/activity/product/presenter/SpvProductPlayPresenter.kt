package com.hundsun.zjfae.activity.product.presenter

import com.hundsun.zjfae.activity.product.bean.ProductPlayBean
import com.hundsun.zjfae.activity.product.view.SpvProductPlayView
import com.hundsun.zjfae.common.base.BasePresenter
import com.hundsun.zjfae.common.http.api.ConstantName
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver
import com.hundsun.zjfae.common.utils.CCLog
import io.reactivex.Observable
import onight.zjfae.afront.gens.FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo
import onight.zjfae.afront.gens.FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo
import onight.zjfae.afront.gens.QueryMyKqQuan.REQ_PBIFE_trade_queryMyKqQuan
import onight.zjfae.afront.gens.QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan
import onight.zjfae.afront.gens.QueryPayInit.REQ_PBIFE_trade_queryPayInit
import onight.zjfae.afront.gens.QueryPayInit.Ret_PBIFE_trade_queryPayInit
import onight.zjfae.afront.gens.RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo
import onight.zjfae.afront.gens.v3.UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo
import onight.zjfae.afront.gens.v5.PBIFETradeTransferOrder
import onight.zjfae.afront.gensazj.Dictionary.REQ_PBAPP_dictionary
import onight.zjfae.afront.gensazj.Dictionary.Ret_PBAPP_dictionary


class SpvProductPlayPresenter(view : SpvProductPlayView) : BasePresenter<SpvProductPlayView>(view) {


    /**
     * 支付渠道
     */
    private var payChannelNo = ""

    /**
     * 用户是否绑卡
     */
    private var isBondedCard = "false"

    /**
     * 重复提交码
     */
    private var repeatCommitCheckCode = ""


    private var productCode = ""
    private  var delegateNum = ""

    fun init(productCode: String, delegateNum: String, serialNo: String,delegationCode : String) {
        this.productCode = productCode
        this.delegateNum = delegateNum
        val observable: Observable<*> = Observable.mergeDelayError(userInfo, queryPayInit(productCode, delegateNum), queryMyKqQuan(productCode, delegateNum, serialNo,delegationCode), getDictionary())
        addDisposable(observable, object : BaseProductPlayProtoBufObserver<Any?>(baseView) {
            override fun onSuccess(o: Any?) {
                if (o is Ret_PBIFE_trade_queryPayInit) {
                    val queryPayInit = o
                    repeatCommitCheckCode = queryPayInit.data.payInitWrap.repeatCommitCheckCode
                    baseView.onPlayInit(queryPayInit)
                } else if (o is Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    //用户信息
                    isBondedCard = o.data.isBondedCard
                } else if (o is Ret_PBIFE_trade_queryMyKqQuan) {
                    baseView.onUserKquanInfo(o)
                } else if (o is Ret_PBAPP_dictionary) {
                    baseView.onKQDescription(o)
                }
            }
        })
    }


    /***
     * 购买金额初始化
     */
    private fun queryPayInit(productCode: String?, delegateNum: String): Observable<*>? {
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryPayInit)
        val querInit = REQ_PBIFE_trade_queryPayInit.newBuilder()
        querInit.productCode = productCode
        querInit.delegateNum = delegateNum
        querInit.payType = "subscribePay"
        return apiServer.queryPayInit(url, getBody(querInit.build().toByteArray()))
    }


    /**
     * 查询卡券
     */
    private fun queryMyKqQuan(productCode: String?, delegateNum: String, serialNo: String, delegationCode : String): Observable<*>? {
        CCLog.e("productCode", productCode)
        CCLog.e("delegateNum", delegateNum)
        CCLog.e("serialNo", serialNo)
        CCLog.e("delegationCode", delegationCode)

        val queryQuan = REQ_PBIFE_trade_queryMyKqQuan.newBuilder()
        queryQuan.productCode = productCode
        queryQuan.delegateNum = delegateNum
        queryQuan.serialNo = serialNo
        queryQuan.bussType = "01002"
        //queryQuan.kqType = "F|L|R"
        //queryQuan.delegationCode = delegationCode
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryMyKqQuan, getRequestMap())

        return apiServer.queryMyKqQuan(url, getBody(queryQuan.build().toByteArray()))
    }


    /**
     * 转让购买
     */
    fun playTransferDetail(playInfo: ProductPlayBean) {
        val playBaoInfo = playInfo.playBaoInfo
        var kqAddRatebj: String? = ""
        var kqCode: String? = ""
        var kqType: String? = ""
        var kqValue: String? = ""
        if (playBaoInfo != null) {
            kqAddRatebj = playInfo.playBaoInfo.kqAddRatebj
            kqCode = playBaoInfo.allType(playBaoInfo.playMap, playBaoInfo.playList, "id")
            kqType = playBaoInfo.allType(playBaoInfo.playMap, playBaoInfo.playList, "type")
            kqValue = playBaoInfo.allValue(playBaoInfo.playMap, playBaoInfo.playList, "value")
        }
        val transPlay = PBIFETradeTransferOrder.REQ_PBIFE_trade_transferOrder.newBuilder()


        transPlay.channelNo = "12"
        transPlay.kqAddRatebj = kqAddRatebj
        transPlay.buyNum = playInfo.playAmount
        transPlay.delegationCode = playInfo.delegationCode
        transPlay.kqCode = kqCode
        transPlay.kqType = kqType
        transPlay.kqValue = kqValue
        transPlay.password = playInfo.playPassWord
        transPlay.payType = "subscribePaySPV"
        transPlay.repeatCommitCheckCode = repeatCommitCheckCode

        val map: MutableMap<String,String> = getRequestMap()
        map["version"] = "v4"
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferOrder, map)
        addDisposable(apiServer.playTransferDetail(url, getBody(transPlay.build().toByteArray())), object : BaseProductPlayProtoBufObserver<PBIFETradeTransferOrder.Ret_PBIFE_trade_transferOrder?>(baseView) {
           override fun onSuccess(transferOrder: PBIFETradeTransferOrder.Ret_PBIFE_trade_transferOrder?) {

               baseView.playProduct(transferOrder?.returnCode, transferOrder?.returnMsg)

            }
        })
    }




    /**
     * 查询渠道关闭或查询银行卡信息
     */
    fun queryBankInfo() {
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadRechargeBankCardInfo, getRequestMap())
        addDisposable(apiServer.queryBankInfo(url), object : BaseBankProtoBufObserver<Ret_PBIFE_fund_loadRechargeBankCardInfo?>(baseView) {
           override fun onSuccess(bankCardInfo: Ret_PBIFE_fund_loadRechargeBankCardInfo?) {
                payChannelNo = bankCardInfo!!.data.payChannelNo
                val bankName = bankCardInfo.data.bankName
                val bankCard = bankCardInfo.data.bankCardNo
                val bankNo = bankCardInfo.data.bankNo
                val showTips = bankCardInfo.data.showTips
                baseView.queryRechargeBankInfo(bankName, bankCard, bankNo, showTips)
            }
        })
    }

    /**
     * 查询银行限额信息
     */
    fun queryFundBankInfo(bankNo: String?) {
        val fundBank = REQ_PBIFE_bankcardmanage_queryFundBankInfo.newBuilder()
        fundBank.bankCode = bankNo
        fundBank.payChannel = payChannelNo
        fundBank.transType = "1"
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryFundBankInfo, getRequestMap())
        addDisposable(apiServer.queryFundBankInfo(url, getBody(fundBank.build().toByteArray())), object : BaseBankProtoBufObserver<Ret_PBIFE_bankcardmanage_queryFundBankInfo?>(baseView) {
           override fun onSuccess(fundBankInfo: Ret_PBIFE_bankcardmanage_queryFundBankInfo?) {
                baseView.bankCardManage(fundBankInfo)
            }
        })
    }


    /**
     * 余额不足时充值成功回调请求
     */
    fun rechargePlayAmount() {
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryPayInit, getRequestMap())
        val querInit = REQ_PBIFE_trade_queryPayInit.newBuilder()
        querInit.productCode = productCode
        querInit.delegateNum = delegateNum
        querInit.payType = "subscribePay"
        addDisposable(apiServer.queryPayInit(url, getBody(querInit.build().toByteArray())), object : BaseProductPlayProtoBufObserver<Ret_PBIFE_trade_queryPayInit?>(baseView) {
           override fun onSuccess(ret_pbife_trade_queryPayInit: Ret_PBIFE_trade_queryPayInit?) {
                baseView.rechargePlayAmount(ret_pbife_trade_queryPayInit)
            }
        })
    }


    /**
     * 卡券使用说明
     */
    private fun getDictionary(): Observable<*>? {
        val url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap())
        val diction = REQ_PBAPP_dictionary.newBuilder()
        diction.addKeyNo("cardUse.rule")
        return apiServer.getDictionary(url, getBody(diction.build().toByteArray()))
    }


    fun getIsBondedCard(): String? {
        return isBondedCard
    }


    fun setPayChannelNo(payChannelNo: String?) {
        this.payChannelNo = payChannelNo!!
    }

}
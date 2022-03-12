package com.hundsun.zjfae.activity.moneymanagement

import android.content.Intent
import android.text.Html
import android.view.View
import com.hundsun.zjfae.R
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity
import com.hundsun.zjfae.activity.mine.AddBankActivity
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity
import com.hundsun.zjfae.activity.moneymanagement.adapter.EntrustedDetailsAdapter
import com.hundsun.zjfae.activity.moneymanagement.presenter.LookSpvProductDetailPresenter
import com.hundsun.zjfae.activity.moneymanagement.view.LookSpvProductDetailView
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter
import com.hundsun.zjfae.common.base.CommActivity
import com.hundsun.zjfae.common.utils.DividerItemDecorations
import com.hundsun.zjfae.common.utils.MoneyUtil
import com.hundsun.zjfae.common.utils.StringUtils
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.*
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.attachment
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.buyRemainAmount
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.buyTotalAmount
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.buyerSmallestAmount
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.canBuyNum
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.expectedMaxAnnualRate
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.isTransferStr
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.leastHoldAmount
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.lin_transfer_end
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.lin_transfer_start
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.manageEndDate
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.payStyle
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.productName
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.riskLevel
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.riskLevel_view
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.tradeLeastHoldDay
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.tradeStartDate_end_time
import kotlinx.android.synthetic.main.activity_look_spv_product_detail.tradeStartDate_start_time
import onight.zjfae.afront.gens.Attachment
import onight.zjfae.afront.gens.EntrustedDetails
import onight.zjfae.afront.gens.UserHighNetWorthInfo
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails
import onight.zjfae.afront.gens.v3.UserDetailInfo
import onight.zjfae.afront.gensazj.v2.Notices

class LookSpvProductDetailActivity : CommActivity<LookSpvProductDetailPresenter>(),
    LookSpvProductDetailView {
    override fun createPresenter(): LookSpvProductDetailPresenter {
        return LookSpvProductDetailPresenter(this)

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

        return R.layout.activity_look_spv_product_detail
    }

    override fun initData() {

        presenter.productCode = intent.getStringExtra("productCode")
        presenter.id = intent.getStringExtra("id")
        presenter.initProductData()

    }

    /**
     * 产品详情
     * @param productDetails 产品详情信息
     * @param requestTime 请求时间差
     */
    override fun onProductData(productDetails: PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails?) {

        val data = productDetails!!.data.taProductFinanceDetail

        productName.text = data.productName

        deadline.text = data.deadline

        expectedMaxAnnualRate.text = data.expectedMaxAnnualRate + "%"

        buyerSmallestAmount.text = MoneyUtil.formatMoney2(data.buyerSmallestAmount)

        //设置风险等级数据
        if (StringUtils.isNotBlank(data.riskLevelLabelValue)) {
            riskLevel.setText(data.riskLevelLabelValue)
            riskLevel.visibility = View.VISIBLE
            riskLevel_view.visibility = View.VISIBLE
        } else {
            riskLevel.visibility = View.GONE
            riskLevel_view.visibility = View.GONE
        }
        if (StringUtils.isNotBlank(data.riskLevelLabelUrl)) {
            riskLevel.setTextColor(resources.getColor(R.color.blue))
            riskLevel.setOnClickListener(View.OnClickListener {
                val accreditedBuyIs = data.accreditedBuyIs
                presenter.attachmentUserInfo(
                    accreditedBuyIs,
                    null,
                    productDetails.data.ratingTitle,
                    data.riskLevelLabelUrl,
                    false
                )
            })
        }
        buyTotalAmount.text = MoneyUtil.formatMoney2(data.buyTotalAmount) + "元"
        buyRemainAmount.text = MoneyUtil.formatMoney2(data.buyRemainAmount) + "元"
        isTransferStr.text = data.isTransferStr
        manageEndDate.text = data.manageEndDate
        leastHoldAmount.text = MoneyUtil.formatMoney2(data.unActualPriceIncreases) + "元"
        if (data.isTransferStr == "可转让") {
            tradeLeastHoldDay.text = data.tradeLeastHoldDay + "个交易日"
        } else {
            tradeLeastHoldDay.text = "--"
        }

        payStyle.text = data.payStyle

        canBuyNum.text = data.canBuyNum
        tradeStartDate_start_time.text = data.tradeStartDate
        tradeStartDate_end_time.text = data.tradeEndDate
        if (data.isTransferStr == "可转让") {
            lin_transfer_start.visibility = View.VISIBLE
            lin_transfer_end.visibility = View.VISIBLE
        } else {
            lin_transfer_start.visibility = View.GONE
            lin_transfer_end.visibility = View.GONE
        }

    }

    /**
     * 点击附件查询用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     * @param attachmentList 附件列表
     * @param riskLevelLabelName 附加文件名称
     * @param riskLevelLabelUrl 附件加载Url
     * @param isAttachment 附件还是信用评级
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
        val isRealInvestor = userDetailInfo.data.isRealInvestor
        val highNetWorthStatus = userDetailInfo.data.highNetWorthStatus
        val userType = userDetailInfo.data.userType
        if (accreditedBuyIs == "1" && isAccreditedInvestor != "1") {
            if (userDetailInfo.data.isBondedCard == "false" && userType == "personal") {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消") { dialog, which -> //去绑卡
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else if (isAccreditedInvestor != "1") {
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
            } else {
                if (isAttachment) {
                    //附件单击
                    val id = attachmentList!!.id
                    val title = attachmentList.title
                    openAttachment(id, title, "9")
                } else {
                    //信用评级
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl)
                }
            }
        } else {
            if (userDetailInfo.data.verifyName != "1" && userType == "personal") {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消") { dialog, which -> //去绑卡
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else {
                if (isAttachment) {
                    //附件单击
                    val id = attachmentList!!.id
                    val title = attachmentList.title
                    openAttachment(id, title, "9")
                } else {
                    //信用评级
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl)
                }
            }
        }
    }


    /**
     * 失败原因
     * @param body 合格投资者失败原因
     * @param isRealInvestor 是否是合格投资者
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
        showDialog(buffer.toString() + "".trim { it <= ' ' }, "重新上传", "取消") { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            intent.putExtra("isRealInvestor", isRealInvestor)
            intent.setClass(this, HighNetWorthMaterialsUploadedActivity::class.java)
            baseStartActivity(intent)
        }
    }

    /**
     * 受托管理报告列表
     * @method
     * @date: 2020/10/29 14:41
     * @author: moran
     * @param entrustedDetails 受托管理报告列表实体类
     * @return
     */
    override fun getEntrustedDetailsAttachment(entrustedDetails: EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails) {

        val entrustedList = entrustedDetails.data.tcProductSeriesListList

        if (entrustedList.isNotEmpty()) {


            val adapter = EntrustedDetailsAdapter(this, entrustedList)

            adapter.setOnItemClickListener {

                //受托管理报告单击
                val id: String = entrustedList[it].entrustedPath
                val title: String = entrustedList[it].entrustedReport
                openAttachment(id, title, "8", presenter.productCode)

            }
            rv_entrustedDetails.adapter = adapter
//            rv_entrustedDetails.addItemDecoration(DividerItemDecorations())
            lin_entrustedDetails.visibility = View.VISIBLE
        } else lin_entrustedDetails.visibility = View.GONE

    }

    override fun getEntrustedDetailsNotice(notice: Notices.Ret_PBAPP_notice) {
        entrustedTips.text = Html.fromHtml(notice.data.notice.noticeContent)
    }

    /**
     * 附件详细信息
     * @param attachmentList 附件列表信息
     * @param accreditedBuyIs 是否合格投资者购买
     */
    override fun onAttachmentInfo(
        attachmentList: Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment?,
        accreditedBuyIs: String?
    ) {

        val list = attachmentList!!.data.taProductAttachmentListList

        if (list == null || list.isEmpty()) {
            attachment.visibility = View.GONE
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
     * 用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     */
    override fun onUserDetailInfo(
        userDetailInfo: UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?,
        accreditedBuyIs: String?
    ) {

        val userDetailInfo: UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo =
            userDetailInfo!!.getData()
        val isRealInvestor = userDetailInfo.isRealInvestor
        val userType = userDetailInfo.userType
        if (userDetailInfo.isFundPasswordSet == "false") {
            showDialog("您还尚未设置交易密码哦", "设置交易密码", "取消") { dialog, which -> //去设置交易密码
                dialog.dismiss()
                baseStartActivity(this, FirstPlayPassWordActivity::class.java)
            }
        } else if (userDetailInfo.verifyName != "1" && userType == "personal") {
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消") { dialog, which -> //去绑卡
                dialog.dismiss()
                baseStartActivity(this, AddBankActivity::class.java)
            }
        } else if (accreditedBuyIs == "1" && userDetailInfo.isAccreditedInvestor != "1") {
            if (userDetailInfo.isBondedCard == "false" && userType == "personal") {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消") { dialog, which -> //去绑卡
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else {
                // 0审核不通过
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
            showDialog("还未进行风险评测，先去评测?", "去风评", "取消") { dialog, which -> //去风评
                dialog.dismiss()
                baseStartActivity(this, RiskAssessmentActivity::class.java)
            }
        } else if (userDetailInfo.isRiskExpired == "true" && userType == "personal") {
            showDialog("您的风险评测已经过期，请重新进行评测~", "去风评", "取消") { dialog, which -> //去风评
                dialog.dismiss()
                baseStartActivity(this, RiskAssessmentActivity::class.java)
            }
        }

    }


}
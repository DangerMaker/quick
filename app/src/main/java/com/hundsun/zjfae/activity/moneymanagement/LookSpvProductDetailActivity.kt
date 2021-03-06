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
     * ????????????ID
     *
     * @param
     * @return int ??????ID
     * @description ????????????Id
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
     * ????????????
     * @param productDetails ??????????????????
     * @param requestTime ???????????????
     */
    override fun onProductData(productDetails: PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails?) {

        val data = productDetails!!.data.taProductFinanceDetail

        productName.text = data.productName

        deadline.text = data.deadline

        expectedMaxAnnualRate.text = data.expectedMaxAnnualRate + "%"

        buyerSmallestAmount.text = MoneyUtil.formatMoney2(data.buyerSmallestAmount)

        //????????????????????????
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
        buyTotalAmount.text = MoneyUtil.formatMoney2(data.buyTotalAmount) + "???"
        buyRemainAmount.text = MoneyUtil.formatMoney2(data.buyRemainAmount) + "???"
        isTransferStr.text = data.isTransferStr
        manageEndDate.text = data.manageEndDate
        leastHoldAmount.text = MoneyUtil.formatMoney2(data.unActualPriceIncreases) + "???"
        if (data.isTransferStr == "?????????") {
            tradeLeastHoldDay.text = data.tradeLeastHoldDay + "????????????"
        } else {
            tradeLeastHoldDay.text = "--"
        }

        payStyle.text = data.payStyle

        canBuyNum.text = data.canBuyNum
        tradeStartDate_start_time.text = data.tradeStartDate
        tradeStartDate_end_time.text = data.tradeEndDate
        if (data.isTransferStr == "?????????") {
            lin_transfer_start.visibility = View.VISIBLE
            lin_transfer_end.visibility = View.VISIBLE
        } else {
            lin_transfer_start.visibility = View.GONE
            lin_transfer_end.visibility = View.GONE
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
        val isRealInvestor = userDetailInfo.data.isRealInvestor
        val highNetWorthStatus = userDetailInfo.data.highNetWorthStatus
        val userType = userDetailInfo.data.userType
        if (accreditedBuyIs == "1" && isAccreditedInvestor != "1") {
            if (userDetailInfo.data.isBondedCard == "false" && userType == "personal") {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else if (isAccreditedInvestor != "1") {
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
            } else {
                if (isAttachment) {
                    //????????????
                    val id = attachmentList!!.id
                    val title = attachmentList.title
                    openAttachment(id, title, "9")
                } else {
                    //????????????
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl)
                }
            }
        } else {
            if (userDetailInfo.data.verifyName != "1" && userType == "personal") {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????") { dialog, which -> //?????????
                    dialog.dismiss()
                    baseStartActivity(this, AddBankActivity::class.java)
                }
            } else {
                if (isAttachment) {
                    //????????????
                    val id = attachmentList!!.id
                    val title = attachmentList.title
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
     * ????????????????????????
     * @method
     * @date: 2020/10/29 14:41
     * @author: moran
     * @param entrustedDetails ?????????????????????????????????
     * @return
     */
    override fun getEntrustedDetailsAttachment(entrustedDetails: EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails) {

        val entrustedList = entrustedDetails.data.tcProductSeriesListList

        if (entrustedList.isNotEmpty()) {


            val adapter = EntrustedDetailsAdapter(this, entrustedList)

            adapter.setOnItemClickListener {

                //????????????????????????
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
     * ??????????????????
     * @param userDetailInfo ??????????????????
     * @param accreditedBuyIs ???????????????????????????
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


}
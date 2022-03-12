package com.hundsun.zjfae.activity.productreserve

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.hundsun.zjfae.R
import com.hundsun.zjfae.common.base.BaseActivity
import com.hundsun.zjfae.common.base.BasicsActivity

class SpecialReserveRuleActivity : BasicsActivity() {


    private val text = """ 
1、12：00前可预约次日(含)后三个交易日内即将发行的产品，12：00后可预约后日（含）三个交易日内即将发行的产品。
 
2、预约时，系统将冻结预约份额相应的保证金，请在预约前打入足额保证金，交易时将抵扣本金。
 
3、预约后，客服会尽快联系您，确认个性化产品的预约是否审核通过。
 
4、因额度及排期的原因，个性化预约的产品并不能保证足额、按时上线，中心会尽可能地为各位安排。
 
5、如审核通过，系统将自动确认您的预约申请。如审核未通过（如无法足够的额度、排期冲突等原因），系统将取消您的预约申请。
 
6、预约成功后须于产品上线当日中午12点前交易。
 
7、产品上线前一交易日中午12点后取消预约、以及产品上线后未及时交易、不足额交易则视为违约，产品上线前一交易日中午12点后取消预约的保证金即时解冻，未及时交易、不足额交易的保证金于次日解冻。
 
8、连续6个自然月内累计出现3次违约，后6个月（180天）内将禁用预约系统（含产品预约以及个性化预约）。 示例：客户在1月20日、1月22日、6月1日分别有三次违约（系统判定1月份至6月份为连续6个自然月），则从6月1日开始的6个月内系统将禁用预约系统。
 
"""

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


        return R.layout.activity_special_reserve_rule
    }

    override fun initView() {
        val tv_rule_text = findViewById<TextView>(R.id.tv_rule_text)

        tv_rule_text.text = text


    }
}
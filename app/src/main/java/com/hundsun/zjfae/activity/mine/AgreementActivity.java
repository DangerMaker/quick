package com.hundsun.zjfae.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.AgreementInfo;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class AgreementActivity extends CommActivity {


    private TextView agreement_t1,agreement_t2,agreement_t3,agreement_t4;

    @Override
    public void initView() {
        agreement_t1 = findViewById(R.id.agreement_t1);
        agreement_t2 = findViewById(R.id.agreement_t2);
        agreement_t3 = findViewById(R.id.agreement_t3);
        agreement_t4 = findViewById(R.id.agreement_t4);
    }

    @Override
    public void initData() {
        setTitle("充值授权书");
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        if (bundle == null){
            return;
        }
        AgreementInfo agreementInfo = bundle.getParcelable("agreementInfo");
        initAgreement(agreementInfo);

    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.agreement_layout);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_agreement;
    }



    private void initAgreement(AgreementInfo agreementInfo){

        if (agreementInfo == null){

            return ;
        }


        StringBuffer buffer1 = new StringBuffer("姓名：");
        buffer1.append(agreementInfo.getName()).append("\n").append("\n");
        buffer1.append("证件类型：身份证").append("\n").append("\n");
        buffer1.append("证件号码：").append(agreementInfo.getCertificateCode()).append("\n").append("\n");
        buffer1.append("浙金中心会员账户：").append(agreementInfo.getAccount()).append("\n").append("\n");
        buffer1.append("日期：").append(agreementInfo.getYear() + "年"+ agreementInfo.getMonth() + "月"+ agreementInfo.getDay() + "日").append("\n");

        agreement_t1.setText(buffer1.toString());



        StringBuffer buffer2 = new StringBuffer("本人授权浙江金融资产交易中心股份有限公司（以下简称“浙金中心”）向本人在浙金中心开立的会员账户（以下简称“会员账户”）充值，授权事宜如下：\n");
        buffer2.append(" 一、本人授权浙金中心根据充值指令从绑定的银行账户通过银行充值，用于向会员账户充值。").append("\n");
        buffer2.append(" 二、本人绑定的银行账户如下：").append("\n");
        agreement_t2.setText(buffer2.toString());

        StringBuffer buffer3 = new StringBuffer();
        buffer3.append("账户名：").append(agreementInfo.getName()).append("\n").append("\n");
        buffer3.append("账  号：").append(agreementInfo.getBankCard()).append("\n").append("\n");
        buffer3.append("开户行：").append(agreementInfo.getBankName()).append("\n");
        agreement_t3.setText(buffer3.toString());

        StringBuffer buffer4 = new StringBuffer();
        buffer4.append(" 三、本人知晓并确认，本授权书系使用本人浙金中心会员账户、以网络在线点击确认的方式向浙金中心发出。本人通过绑卡流程确认的银行账户信息，在充值过程中，浙金中心根据本人充值指令进行相关操作，无需再向本人确认银行账户信息。").append("\n");

        buffer4.append("四、本人知晓并承诺，浙金中心根据本授权书所采取的全部行动和措施的法律后果均由本人承担。若因本人的银行账户余额不足、挂失、冻结、注销等原因造成无法扣款，由此产生的责任由本人承担，浙金中心无需对充值服务的资金到账时间做任何承诺。浙金中心仅需根据本授权书所述授权事宜进行相关操作，无义务对其根据本授权书所采取的全部行动和措施的时效性和结果承担责任。").append("\n");
        buffer4.append("五、本人知晓并确认，银行仅根据授权书约定，依照浙金中心发出的指令，对本人银行账户资金进行划付和核算，银行不承担本人投资风险，且不介入本人与浙金中心之间因交易引起的纠纷。").append("\n");
        buffer4.append("六、本人知晓并确认，本授权书自本人在浙金中心网站绑卡成功时生效，至本人解绑时终止。").append("\n");
        buffer4.append("特此授权。").append("\n").append("\n");

        agreement_t4.setText(buffer4.toString());



    }


}

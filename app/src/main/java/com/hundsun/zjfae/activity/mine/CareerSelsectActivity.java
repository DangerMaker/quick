package com.hundsun.zjfae.activity.mine;

import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.CareerAdapter;
import com.hundsun.zjfae.activity.mine.presenter.CareerSelsectPresenter;
import com.hundsun.zjfae.activity.mine.view.CareerSelsectView;
import com.hundsun.zjfae.common.adapter.OnItemClickListener;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.CareerEnumTypeCom;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine
 * @ClassName:      职业选择Activity
 * @Description:     java类作用描述
 * @Author:         moran
 * @CreateDate:     2019/7/31 17:18
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/31 17:18
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class CareerSelsectActivity extends CommActivity<CareerSelsectPresenter> implements View.OnClickListener, CareerSelsectView {


    private BottomSheetDialog bottomSheetDialog;

    private RecyclerView careerRecyclerView;
    private TextView careerName;
    private Button career_commit,career_cancel;


    @Override
    public void onCareerEnumTypeComList(List<CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData> enumDataList) {

        CareerAdapter adapter = new CareerAdapter(this,enumDataList);

        adapter.setOnItemClickListener(new OnItemClickListener<CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData>() {
            @Override
            public void onItemClickListener(View v, CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData enumData, int position) {
                careerName.setText(enumData.getEnumName());
                bottomSheetDialog.dismiss();
            }
        });
        careerRecyclerView.setAdapter(adapter);

        bottomSheetDialog.setContentView(careerRecyclerView);

    }


    @Override
    protected void initData() {
        setTitle("选择职业");
        presenter.onCareerEnumTypeCom();
    }

    @Override
    protected CareerSelsectPresenter createPresenter() {
        return new CareerSelsectPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.custom_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected void initView() {
        findViewById(R.id.career_layout).setOnClickListener(this);
        career_cancel = findViewById(R.id.career_cancel);
        career_cancel.setOnClickListener(this);
        career_commit = findViewById(R.id.career_commit);
        career_commit.setOnClickListener(this);
        careerName = findViewById(R.id.careerName);
        bottomSheetDialog = new BottomSheetDialog(this);
        careerRecyclerView = new RecyclerView(this);
        careerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_career_selsect;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.career_layout:
                bottomSheetDialog.show();
                break;
            case R.id.career_cancel:
                finish();
                break;
            case R.id.career_commit:
                if (!careerName.getText().toString().equals(getString(R.string.select_career))){
                    presenter.onCommitUpdateProfession(careerName.getText().toString());
                }
                else {
                    showDialog("请先选择职业");
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onCommitUpdateProfession(String returnMsg) {
        showDialog(returnMsg, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }
}

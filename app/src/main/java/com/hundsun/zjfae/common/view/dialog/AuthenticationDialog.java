package com.hundsun.zjfae.common.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.SettingSecurityIssuesDialog;

import java.util.List;

import onight.zjfae.afront.gens.LoadMySecurityQuestionPB;

public class AuthenticationDialog  extends Dialog {
    public AuthenticationDialog( Context context) {
        super(context);
    }

    public AuthenticationDialog( Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context; //上下文对象
        private List<LoadMySecurityQuestionPB.PBIFE_securityquestionmanage_loadMySecurityQuestion.TcSecurityQuestionAnswerList> mLProblem;
        @SuppressWarnings("unused")
        private View contentView; //对话框中间加载的其他布局界面
        private ItemClick itemClick;
        private float alpha = 1f;

        public Builder(Context context) {
            this.context = context;

        }

        public Builder(Context context,List<LoadMySecurityQuestionPB.PBIFE_securityquestionmanage_loadMySecurityQuestion.TcSecurityQuestionAnswerList> list) {
            this.context = context;
            mLProblem = list;

        }



        public void setItemClick(ItemClick itemClick) {
            this.itemClick = itemClick;
        }

        public Builder(Context context, float alpha) {
            this.context = context;
            this.alpha = alpha;
        }


        /**
         * 设置对话框界面
         *
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        @SuppressLint("InflateParams")
        public CustomDialog create(final TextView textView) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.dialog_setting_security_issues, null);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ListView listView = layout.findViewById(R.id.lv_problem);
            ProblemAdapter problemAdapter = new ProblemAdapter();
            listView.setAdapter(problemAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    textView.setText(mLProblem.get(i).getQuestion());
                    if (itemClick != null){
                        itemClick.onItemClick(mLProblem.get(i).getId());
                    }
                    dialog.dismiss();

                }
            });
            SupportDisplay.setChildViewParam((ViewGroup) layout, false);
            dialog.setContentView(layout);

            return dialog;
        }

        class ProblemAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return mLProblem.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup viewGroup) {
               ViewHolder viewHolder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_setting_security_issues, null);
                    SupportDisplay.resetAllChildViewParam((ViewGroup) convertView);
                    viewHolder = new ViewHolder();
                    viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_problem);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvTitle.setText(mLProblem.get(position).getQuestion());
                return convertView;
            }

            private class ViewHolder {
                TextView tvTitle;
            }
        }



        public interface ItemClick{
            void onItemClick(String id);
        }
    }
}

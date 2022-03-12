package com.hundsun.zjfae.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogUtil {

	private Activity activity;
	private AlertDialog alertDialog;
	public DialogUtil(Activity activity) {
		this.activity = activity;
	}

	public void showDialog(String message) {
		alertDialog = new AlertDialog.Builder(activity)
				.setTitle(message)
				.setNegativeButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
					}
				}).setCancelable(false).create();

		if (!alertDialog.isShowing()){
			alertDialog.show();
		}

	}

	public void showDialog(){
		final AlertDialog alertDialog  = new AlertDialog.Builder(activity)
				.setTitle("提示")
				.setMessage("你的相机暂未对浙金app授权，人脸识别功能将不能使用")
				.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).setCancelable(false).create();
		alertDialog.show();
	}

	public void onDestory() {
		activity = null;
	}
}
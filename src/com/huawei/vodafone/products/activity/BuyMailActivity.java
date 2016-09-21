package com.huawei.vodafone.products.activity;

import com.huawei.vodafone.R;
import com.huawei.vodafone.ui.activity.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BuyMailActivity extends BaseActivity {

	private TextView tv_top;
	private TextView tv_mail_center;
	private TextView tv_mail_yes;
	private TextView tv_mail_no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_mail);
		initSecondTitle("", false, true);
		initView();
	}

	private void initView() {
		tv_top = (TextView) findViewById(R.id.tv_top);
		tv_mail_center = (TextView) findViewById(R.id.tv_mail_center);
		tv_mail_yes = (TextView) findViewById(R.id.tv_mail_yes);
		tv_mail_no = (TextView) findViewById(R.id.tv_mail_no);
		tv_mail_yes.setOnClickListener(this);
		tv_mail_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_mail_yes:
			Intent intent = new Intent();
			intent.putExtra("isbuy", 1);
			setResult(RESULT_OK, intent);
			finish();
			activityAnimationDown();
			break;
		case R.id.tv_mail_no:
			finish();
			activityAnimationDown();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		activityAnimationDown();
	}

}

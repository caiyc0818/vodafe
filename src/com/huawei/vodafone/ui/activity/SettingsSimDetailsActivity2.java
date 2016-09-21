package com.huawei.vodafone.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.huawei.vodafone.R;

public class SettingsSimDetailsActivity2 extends BaseActivity implements
		OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_sim_detail);
		initSecondTitle(getString(R.string.settings_SIM_details));
		initView();
	}

	private void initView() {

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}
	}
}

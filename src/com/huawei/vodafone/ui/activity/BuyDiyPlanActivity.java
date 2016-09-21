package com.huawei.vodafone.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.huawei.vodafone.R;
import com.huawei.vodafone.net.IRequest;
import com.huawei.vodafone.net.RequestJSon;
import com.huawei.vodafone.net.RequestListener;
import com.huawei.vodafone.net.URLs;
import com.huawei.vodafone.ui.myview.dialog.WaitDialog;
import com.huawei.vodafone.util.PreferenceUtils;

/**
 * @author kanl
 *
 * @create 2016年7月29日 下午5:06:58
 */
public class BuyDiyPlanActivity extends BaseActivity implements RequestListener {
	private TextView tv_yes, tv_no, tv_center, text1, text2, tv1, tv2, tv3, text3, text4;
	private int dataValue;
	private String costValue;
	private int callValue;
	private int dataValue2;
	private int costValue2;
	private int callValue2;
	private String money;
	private int callId;
	private int dataId;
	private WaitDialog wait;
	private String tag;
	private LinearLayout ll2;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.plans_buy_diy_activity);
		initSecondTitle(getString(R.string.we_are), false, true);
		dataValue = getIntent().getIntExtra("dataValue", 0);
		costValue = getIntent().getStringExtra("costValue");
		callValue = getIntent().getIntExtra("callValue", 0);
		callId = getIntent().getIntExtra("callId", 0);
		dataId = getIntent().getIntExtra("dataId", 0);
		money = getIntent().getStringExtra("money");
		tag = getIntent().getStringExtra("Tag");
		initView();
		text1.setText(dataValue + "MB " + getResources().getString(R.string.you_data) + "," + callValue
				+ getResources().getString(R.string.you_calls) + "," + costValue + " "
				+ getResources().getString(R.string.texts) + "");
		text2.setText(getResources().getString(R.string.you_cover) + " € " + money + " "
				+ getResources().getString(R.string.settings_per_month2) + "");
		if ("buy".equals(tag)) {
			tv1.setText(getString(R.string.you_are));
			tv3.setText(getString(R.string.you_can));
			tv2.setVisibility(View.GONE);
			ll2.setVisibility(View.GONE);
			tv_yes.setText(getString(R.string.you_confirm));
		} else {
			tv2.setVisibility(View.VISIBLE);
			ll2.setVisibility(View.VISIBLE);
			tv1.setText(getString(R.string.you_are2));
			tv2.setText(getString(R.string.you_but));
			tv3.setText(getString(R.string.you_can_only));
			tv_yes.setText(getString(R.string.you_swap));
			isDIYShowOrHint();
		}
	}

	private void initView() {
		tv_yes = (TextView) findViewById(R.id.tv_yes);
		tv_no = (TextView) findViewById(R.id.tv_no);
		tv_center = (TextView) findViewById(R.id.tv_center);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		text4 = (TextView) findViewById(R.id.text4);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		tv_yes.setOnClickListener(this);
		tv_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_yes:
			if ("buy".equals(tag)) {
				IRequest.post(5, URLs.ADDOPTIONAL,
						RequestJSon.Optionaloffering(String.valueOf(dataId), String.valueOf(callId), true),
						BuyDiyPlanActivity.this);
				wait = new WaitDialog(BuyDiyPlanActivity.this);
				wait.show();
			} else if ("change".equals(tag)) {
				IRequest.post(6, URLs.MODIFYOPTIONAL,
						RequestJSon.modify(String.valueOf(dataId), String.valueOf(callId),
								PreferenceUtils.getString(BuyDiyPlanActivity.this, "OfferInstId"), "10102"),
						BuyDiyPlanActivity.this);
				wait = new WaitDialog(BuyDiyPlanActivity.this);
				wait.show();
			}

			break;
		case R.id.tv_no:
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

	@Override
	public void requestSuccess(Object tag, String json) {
		// TODO Auto-generated method stub
		JSONObject jsonObj;
		switch ((Integer) tag) {
		case 5:
			try {
				jsonObj = new JSONObject(json);
				JSONObject obj = jsonObj.getJSONObject("header");
				String code = obj.getString("resultCode");
				if ("0".equals(code) || "1".equals(code)) {
					Intent intent1 = new Intent();
					intent1.putExtra("dataId", dataId);
					intent1.putExtra("callId", callId);
					intent1.putExtra("tag", "buy");
					setResult(-1, intent1);
					Intent intent = new Intent("offer_diy_successed");
					sendBroadcast(intent);
				}
				finish();
				activityAnimationDown();
				wait.cancel();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case 6:
			try {
				jsonObj = new JSONObject(json);
				JSONObject obj = jsonObj.getJSONObject("header");
				String code = obj.getString("resultCode");
				if ("0".equals(code) || "1".equals(code)) {
					Intent intent1 = new Intent();
					intent1.putExtra("dataId", dataId);
					intent1.putExtra("callId", callId);
					intent1.putExtra("tag", "change");
					setResult(-1, intent1);
					Intent intent = new Intent("offer_diy_successed");
					sendBroadcast(intent);
				}
				finish();
				activityAnimationDown();
				wait.cancel();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void requestError(Object tag, VolleyError e) {
		// TODO Auto-generated method stub

	}

	private void isDIYShowOrHint() {
		double money = 0;
		int unit = Integer.valueOf(PreferenceUtils.getString2(BuyDiyPlanActivity.this, "moneyUnit"));
		money = money + PreferenceUtils.getLong(BuyDiyPlanActivity.this, "money", 01) * Math.pow(10, -unit);
		String datava = PreferenceUtils.getString(BuyDiyPlanActivity.this, "C_DATA_LEVEL");
		String callva = PreferenceUtils.getString(BuyDiyPlanActivity.this, "C_UNIT_LEVEL");
		if ("100000".equals(datava)) {
			dataValue2 = 300;
		} else if ("100001".equals(datava)) {
			dataValue2 = 500;
		} else if ("100002".equals(datava)) {
			dataValue2 = 800;
		}
		if ("100006".equals(callva)) {
			callValue2 = 100;
		} else if ("100007".equals(callva)) {
			callValue2 = 300;
		} else if ("100008".equals(callva)) {
			callValue2 = 500;
		}

		text3.setText(dataValue2 + "MB " + getResources().getString(R.string.you_data) + "," + callValue2
				+ getResources().getString(R.string.you_calls) + "," + "unlimited "
				+ getResources().getString(R.string.texts));
		text4.setText(getResources().getString(R.string.you_cover) + " € " + money + " "
				+ getResources().getString(R.string.settings_per_month2) + "");
	}

}

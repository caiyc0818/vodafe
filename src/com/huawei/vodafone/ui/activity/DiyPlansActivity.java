package com.huawei.vodafone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.huawei.vodafone.R;
import com.huawei.vodafone.bean.QuickCheckInfo;
import com.huawei.vodafone.bean.UserInfo;
import com.huawei.vodafone.listener.Listener.Click;
import com.huawei.vodafone.net.IRequest;
import com.huawei.vodafone.net.RequestJSon;
import com.huawei.vodafone.net.RequestListener;
import com.huawei.vodafone.net.URLs;
import com.huawei.vodafone.ui.myview.ProgressTextView;
import com.huawei.vodafone.ui.myview.dialog.ErrorDialog;
import com.huawei.vodafone.ui.myview.dialog.SuccessDialog;
import com.huawei.vodafone.ui.myview.dialog.WaitDialog;
import com.huawei.vodafone.util.JsonUtils;
import com.huawei.vodafone.util.PreferenceUtils;
import com.huawei.vodafone.util.StringUtils;
import com.huawei.vodafone.util.TAGUtil;
import com.huawei.vodafone.util.UnitUtil;

public class DiyPlansActivity extends BaseActivity implements
		OnSeekBarChangeListener, OnClickListener, RequestListener {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.settings_offers_and_extras_for_you_plans_diy);
		initSecondTitle(getString(R.string.settings_diy_plan));
		initViews();
	}

	private SeekBar costSb;
	private ProgressTextView costTv;
	private SeekBar dataSb;
	private ProgressTextView dataTv;
	private SeekBar callSb;
	private ProgressTextView callTv;
	private RelativeLayout buy;
	private static final float ratio = 1 / 4.0f;
	private static final int maxCost = 4;
	private static final int maxData = 5;
	private static final int maxCall = 4;
	private TextView data;
	private TextView call;
	private TextView text;
	private TextView text_buy;
	private TextView money;
	private TextView tv_success;
	private String Tag;
	private static final int WHAT_TAG = 0;

	/**
	 * 初始化组件
	 */
	private void initViews() {
		costSb = (SeekBar) findViewById(R.id.cost_sb);
		costTv = (ProgressTextView) findViewById(R.id.cost_tv);
		dataSb = (SeekBar) findViewById(R.id.data_sb);
		dataTv = (ProgressTextView) findViewById(R.id.data_tv);
		callSb = (SeekBar) findViewById(R.id.call_sb);
		callTv = (ProgressTextView) findViewById(R.id.call_tv);
		buy = (RelativeLayout) findViewById(R.id.buy);
		buy.setOnClickListener(this);
		data = (TextView) findViewById(R.id.text1);
		call = (TextView) findViewById(R.id.text2);
		text = (TextView) findViewById(R.id.text3);
		text_buy = (TextView) findViewById(R.id.text);
		money = (TextView) findViewById(R.id.text4);
		tv_success = (TextView) findViewById(R.id.tv_success);
		costTv.setmMaxProgress(maxCost);
		dataTv.setmMaxProgress(maxData);
		callTv.setmMaxProgress(maxCall);
		costSb.setOnSeekBarChangeListener(this);
		dataSb.setOnSeekBarChangeListener(this);
		callSb.setOnSeekBarChangeListener(this);
		Thread costThread = new InitThread(1);
		Thread dataThread = new InitThread(2);
		Thread callThread = new InitThread(3);
		costThread.start();
		dataThread.start();
		callThread.start();
		// 判断 是否有diy
		isShoworHint(PreferenceUtils.getString(DiyPlansActivity.this,
				"C_DATA_LEVEL"), PreferenceUtils.getString(
				DiyPlansActivity.this, "C_UNIT_LEVEL"));

	}

	private void isShoworHint(String data, String calls) {
		if (PreferenceUtils.getBoolean(DiyPlansActivity.this, "haveDiy")) {
			// buy.setEnabled(false);
			// buy.setBackgroundResource(R.drawable.settings_resetpin_back2);
			int costValue = costSb.getProgress() * 100;
			int dataValue = dataSb.getProgress() * 100;
			int callValue = callSb.getProgress() * 100;
			int callId = 0;
			if (callValue == 0) {
				callId = 100006;
			} else if (callValue == 200) {
				callId = 100007;
			} else if (callValue == 400) {
				callId = 100008;
			}

			int dataId = 0;
			if (dataValue == 0) {
				dataId = 100000;
			} else if (dataValue == 200) {
				dataId = 100001;
			} else if (dataValue == 500) {
				dataId = 100002;
			}
			if (Integer.parseInt(data) == dataId
					&& Integer.parseInt(calls) == callId) {
				text_buy.setText(getString(R.string.settings_change_diy_plan));
				buy.setEnabled(false);
				buy.setBackgroundResource(R.drawable.settings_resetpin_back2);
			} else {
				text_buy.setText(getString(R.string.settings_change_diy_plan));
				buy.setEnabled(true);
				buy.setBackgroundResource(R.drawable.settings_resetpin_back_red);
				Tag = "change";
			}
		} else {
			text_buy.setText(getString(R.string.settings_buy_diy_plan));
			buy.setEnabled(true);
			buy.setBackgroundResource(R.drawable.settings_resetpin_back_red);
			Tag = "buy";
		}
	}

	class InitThread extends Thread {
		private int type;
		private int i = 1;

		public InitThread(int type) {
			this.type = type;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int max;
			if (type == 1)
				max = maxCost;
			else if (type == 2)
				max = maxData;
			else
				max = maxCall;
			while (i <= 1) {
				Bundle bundle = new Bundle();
				bundle.putInt("type", type);
				bundle.putInt("progress", i);
				Message msg = handler.obtainMessage();
				msg.setData(bundle);
				handler.sendMessage(msg);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				++i;
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int i = msg.getData().getInt("progress");
			int type = msg.getData().getInt("type");
			switch (type) {
			case 1:
				costSb.setProgress(0);
				costTv.setProgress(0, (0 + 1) * 100 + "");
				text.setText((0 + 1) * 100 + "");
				break;
			case 2:
				dataSb.setProgress(0);
				dataTv.setProgress(0, (0 + 3) * 100 + "MB");
				data.setText((0 + 3) * 100 + "MB");
				break;
			case 3:
				callSb.setProgress(0);
				callTv.setProgress(0, (0 + 1) * 100 + "Min");
				call.setText((0 + 1) * 100 + "Min");
				break;
			default:
				break;
			}
		};
	};
	private WaitDialog wait;

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {
		case R.id.cost_sb:
			if (progress <= 0) {
				costTv.setProgress(progress, (progress + 1) * 100 + "");
				text.setText((progress + 1) * 100 + "");
				costSb.setProgress(progress);
			} else if (progress > 0 && progress <= 2) {
				progress = 2;
				costTv.setProgress(progress, (progress + 1) * 100 + "");
				text.setText((progress + 1) * 100 + "");
				costSb.setProgress(progress);
			} else if (progress > 2 && progress <= 4) {
				progress = 4;
				// costTv.setProgress(progress, (progress + 1) * 100 + "");
				costTv.setProgress(progress, "unlimited");
				// text.setText((progress + 1) * 100 + "");
				text.setText("unlimited");
				costSb.setProgress(progress);
			}

			break;
		case R.id.data_sb:
			// 计算钱
			int callValue = callSb.getProgress();

			if (progress <= 0) {
				progress = 0;
				dataTv.setProgress(progress, (progress + 3) * 100 + "MB");
				data.setText((progress + 3) * 100 + "MB");
				dataSb.setProgress(progress);
				setMoney(callValue, 4.99);
			} else if (progress > 0 && progress <= 2) {
				progress = 2;
				dataTv.setProgress(progress, (progress + 3) * 100 + "MB");
				data.setText((progress + 3) * 100 + "MB");
				dataSb.setProgress(progress);
				setMoney(callValue, 6.99);
			} else if (progress > 2 && progress <= 5) {
				progress = 5;
				dataTv.setProgress(progress, (progress + 3) * 100 + "MB");
				data.setText((progress + 3) * 100 + "MB");
				dataSb.setProgress(progress);
				setMoney(callValue, 9.99);
			}

			break;
		case R.id.call_sb:
			int dataValue = dataSb.getProgress();

			if (progress <= 0) {
				progress = 0;
				callTv.setProgress(progress, (progress + 1) * 100 + "Min");
				call.setText((progress + 1) * 100 + "Min");
				callSb.setProgress(progress);
				setMoney2(dataValue, 1.99);
			} else if (progress > 0 && progress <= 2) {
				progress = 2;
				callTv.setProgress(progress, (progress + 1) * 100 + "Min");
				call.setText((progress + 1) * 100 + "Min");
				callSb.setProgress(progress);
				setMoney2(dataValue, 4.99);
			} else if (progress > 2 && progress <= 4) {
				progress = 4;
				callTv.setProgress(progress, (progress + 1) * 100 + "Min");
				call.setText((progress + 1) * 100 + "Min");
				callSb.setProgress(progress);
				setMoney2(dataValue, 7.99);
			}

			break;
		default:
			break;
		}
	}

	private void setMoney(int callValue, double d) {
		if (callValue == 0) {
			money.setText("" + (d + 1.99));
		} else if (callValue == 2) {
			money.setText("" + (d + 4.99));
		} else if (callValue == 4) {
			money.setText("" + (d + 7.99));
		}
		isShoworHint(PreferenceUtils.getString(DiyPlansActivity.this,
				"C_DATA_LEVEL"), PreferenceUtils.getString(
				DiyPlansActivity.this, "C_UNIT_LEVEL"));
	}

	private void setMoney2(int dataValue, double d) {
		if (dataValue == 0) {
			money.setText("" + (d + 4.99));
		} else if (dataValue == 2) {
			money.setText("" + (d + 6.99));
		} else if (dataValue == 5) {
			money.setText("" + (d + 9.99));
		}
		isShoworHint(PreferenceUtils.getString(DiyPlansActivity.this,
				"C_DATA_LEVEL"), PreferenceUtils.getString(
				DiyPlansActivity.this, "C_UNIT_LEVEL"));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.close_iv:
			break;
		case R.id.buy:
			if (wait == null)
				wait = new WaitDialog(DiyPlansActivity.this);
			wait.show();
			Request(2);
			break;
		default:
			break;
		}
	}

	private void jump() {
		String moneyValue = (String) money.getText();
		if (Float.parseFloat(UserInfo.getBalance().replace(",", "")) >= Float
				.parseFloat(moneyValue)) {
			Intent intent = new Intent(DiyPlansActivity.this,
					BuyDiyPlanActivity.class);
			int costValue = costSb.getProgress() * 100;
			int dataValue = dataSb.getProgress() * 100;
			int callValue = callSb.getProgress() * 100;
			int callId = 0;
			if (callValue == 0) {
				callId = 100006;
			} else if (callValue == 200) {
				callId = 100007;
			} else if (callValue == 400) {
				callId = 100008;
			}

			int dataId = 0;
			if (dataValue == 0) {
				dataId = 100000;
			} else if (dataValue == 200) {
				dataId = 100001;
			} else if (dataValue == 500) {
				dataId = 100002;
			}

			if (costValue == 400) {
				intent.putExtra("costValue", "unlimited");
			} else {
				intent.putExtra("costValue", costValue + 100 + "");
			}
			if (dataId == 100000) {
				intent.putExtra("dataValue", 300);
			} else {
				intent.putExtra("dataValue", dataValue + 300);
			}
			intent.putExtra("callValue", callValue + 100);
			intent.putExtra("money", moneyValue);
			intent.putExtra("callId", callId);
			intent.putExtra("dataId", dataId);
			intent.putExtra("Tag", Tag);
			startActivityForResult(intent, 1001);
		} else {
			mydialog(2);
		}
		activityAnimationUp();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1001:
			if (resultCode == -1) {
				if ("buy".equals(data.getStringExtra("tag"))) {
					tv_success.setText(getString(R.string.confirm_success));
				} else {
					tv_success.setText(getString(R.string.swap_success));
				}
				buy.setVisibility(View.GONE);
				tv_success.setVisibility(View.VISIBLE);
				text_buy.setText(getString(R.string.settings_change_diy_plan));
				buy.setEnabled(false);
				buy.setBackgroundResource(R.drawable.settings_resetpin_back2);
				// 查询DIY详情
				Request(3);
			}

			break;
		case TAGUtil.tag7:
			if (resultCode == TAGUtil.tag8) {
				SuccessDialog dialog = new SuccessDialog(this);
				dialog.settext("Your payment was successful");
				dialog.show();
				// 查询钱
				Request(TAGUtil.BALANCEINFO);
			}
		default:
			break;
		}
	}

	private void Request(int num) {
		switch (num) {
		case 2:
		case TAGUtil.BALANCEINFO:
			IRequest.get(num, URLs.BALANCEINFO, RequestJSon.ReferInfo(), this);
			break;
		case 3:
			IRequest.get(num, URLs.QUICKCHECK, RequestJSon.ReferInfo(), this);
			break;
		}
	}

	private Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WHAT_TAG:
				buy.setVisibility(View.VISIBLE);
				tv_success.setVisibility(View.GONE);
				QuickCheck();
				break;
			}
		};
	};
	private QuickCheckInfo quickcheckinfo;
	private QuickCheckInfo quickcheckinfo2;

	@Override
	public void requestSuccess(Object tag, String json) {
		// TODO Auto-generated method stub
		switch ((Integer) tag) {
		case 3:
			if (JsonUtils.getHeadCode(json).equals("0")) {
				PreferenceUtils.delString(this, "haveDiy");
				PreferenceUtils.setLong(this, "money", 1l);
				PreferenceUtils.delString(this, "moneyUnit");
				PreferenceUtils.delString(this, "C_DATA_LEVEL");
				PreferenceUtils.delString(this, "C_UNIT_LEVEL");
				PreferenceUtils.delString(this, "OfferInstId");
				quickcheckinfo = JsonUtils.getBodyObject(json,
						QuickCheckInfo.class);
				new Thread(new Runnable() {
					public void run() {
						Message msg = new Message();
						msg.what = WHAT_TAG;
						handler1.sendMessageDelayed(msg, 2000);
					}
				}).start();

			}
			break;
		case TAGUtil.BALANCEINFO:
			quickcheckinfo2 = JsonUtils.getBodyObject(json,
					QuickCheckInfo.class);
			UserInfo.setBalance(UnitUtil.getBalance(
					quickcheckinfo2.getBalanceInfoList(), false));
			break;
		case 2:
			wait.cancel();
			if (JsonUtils.getHeadCode(json).equals("0")) {
				QuickCheckInfo quickcheckinfo = JsonUtils.getBodyObject(json,
						QuickCheckInfo.class);
				if (quickcheckinfo == null) {
					mydialog(1);
				} else {
					UserInfo.setBalance(UnitUtil.getBalance(
							quickcheckinfo.getBalanceInfoList(), false));
					jump();
				}
			} else {
				mydialog(1);
			}
			break;
		}
	}

	private void QuickCheck() {
		// TODO Auto-generated method stub
		// 判断是否有DIY基础套餐 蔡雨成
		if (quickcheckinfo.getOfferInstList().size() > 0) {
			for (int i = 0; i < quickcheckinfo.getOfferInstList().size(); i++) {
				if (!StringUtils.isEmpty(quickcheckinfo.getOfferInstList()
						.get(i).getOfferId())) {
					if ("10102".equals(quickcheckinfo.getOfferInstList().get(i)
							.getOfferId())) {
						PreferenceUtils.setBoolean(this, "haveDiy", true);
						PreferenceUtils.setLong(this, "money", quickcheckinfo
								.getOfferInstList().get(i).getPeriodicFee()
								.getCurrencyValue());
						PreferenceUtils.setString(this, "moneyUnit",
								quickcheckinfo.getOfferInstList().get(i)
										.getPeriodicFee().getCurrencyUnit());
						PreferenceUtils.setString(this, "OfferInstId",
								quickcheckinfo.getOfferInstList().get(i)
										.getOfferInstId());
						for (int j = 0; j < quickcheckinfo.getOfferInstList()
								.get(i).getInstParameterValueList().size(); j++) {
							if ("C_DATA_LEVEL".equals(quickcheckinfo
									.getOfferInstList().get(i)
									.getInstParameterValueList().get(j)
									.getParameterCode())) {
								PreferenceUtils.setString(this, "C_DATA_LEVEL",
										quickcheckinfo.getOfferInstList()
												.get(i)
												.getInstParameterValueList()
												.get(j).getParameterId());
							}
							if ("C_UNIT_LEVEL".equals(quickcheckinfo
									.getOfferInstList().get(i)
									.getInstParameterValueList().get(j)
									.getParameterCode())) {
								PreferenceUtils.setString(this, "C_UNIT_LEVEL",
										quickcheckinfo.getOfferInstList()
												.get(i)
												.getInstParameterValueList()
												.get(j).getParameterId());
							}
						}
					}
				}
			}

		}

	}

	@Override
	public void requestError(Object tag, VolleyError e) {
		if (tag.equals(2)) {
			wait.cancel();
			mydialog(1);
		}
	}

	private void mydialog(int num) {
		ErrorDialog dialog = new ErrorDialog(DiyPlansActivity.this);
		dialog.show();
		dialog.setTag(num);
		switch (num) {
		case 1:

			break;
		case 2:
			dialog.setMessage(
					"Dear customer your balance is low.If you want to buy the DIY plan.Please Top-UP.",
					"We've noticed...", "Top-up", "No,thanks");
			break;
		default:
			break;
		}
		dialog.setClick(new Click() {

			@Override
			public void onClicked(Object num, int num1, String num2,
					boolean num3) {
				switch ((Integer) num) {
				case 1:
					if (num3) {
						if (wait == null)
							wait = new WaitDialog(DiyPlansActivity.this);
						wait.show();
						Request(2);
					}
					break;
				case 2:
					if (num3) {
						Intent intent3 = new Intent(DiyPlansActivity.this,
								RechargeActivity.class);
						startActivityForResult(intent3, TAGUtil.tag7);
						activityAnimationUp();
					}
					break;
				default:
					break;
				}
			}
		});
	}

}

package com.huawei.vodafone.products.activity;

import java.util.Calendar;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.vodafone.R;
import com.huawei.vodafone.ui.activity.ArgeementActivity;
import com.huawei.vodafone.ui.activity.BaseActivity;
import com.huawei.vodafone.util.DateUtil;
import com.huawei.vodafone.util.PreferenceUtils;

public class ContractDetailsActivity extends BaseActivity {

	private ImageView back_details;
	private ImageView imageView2;
	private TextView textView1;
	private RelativeLayout details_terms;
	private HashMap<String, Object> dataMap;
	private TextView offerNameTv;
	private TextView priceTv;
	private TextView durationTv;
	private TextView fromTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contract_details);
		initSecondTitle(getString(R.string.settings_terms));
		Intent it = getIntent();
		if (it.hasExtra("dataMap"))
			dataMap = (HashMap<String, Object>) getIntent()
					.getSerializableExtra("dataMap");
		initView();
	}

	public void initView() {
		back_details = (ImageView) findViewById(R.id.back);
		textView1 = (TextView) findViewById(R.id.title);
		durationTv = (TextView) findViewById(R.id.duration_tv);
		fromTv = (TextView) findViewById(R.id.from_tv);
		details_terms = (RelativeLayout) findViewById(R.id.details_terms);
		back_details.setOnClickListener(this);
		details_terms.setOnClickListener(this);
		offerNameTv = (TextView) findViewById(R.id.offerName_tv);
		priceTv = (TextView) findViewById(R.id.price_tv);
		if (dataMap != null) {
			offerNameTv.setText(dataMap.get("offerName").toString());
			priceTv.setText("€" + dataMap.get("offerPrice").toString());
			String openDate = PreferenceUtils.getString(this, "effectiveDate");
			String endDate = PreferenceUtils.getString(this, "expireDate");
			if (openDate != null && endDate != null) {
				Calendar curr = Calendar.getInstance();
				curr.setTime(DateUtil.ConverToDate(endDate,
						"yyyy-MM-dd HH:mm:ss"));
				Calendar join = Calendar.getInstance();
				join.setTime(DateUtil.ConverToDate(openDate,
						"yyyy-MM-dd HH:mm:ss"));
				int day = curr.get(Calendar.DAY_OF_MONTH)
						- join.get(Calendar.DAY_OF_MONTH);
				int month = curr.get(Calendar.MONTH) - join.get(Calendar.MONTH);
				int year = curr.get(Calendar.YEAR) - join.get(Calendar.YEAR);

				String fromDate = join.get(Calendar.DAY_OF_MONTH)
						+ " "
						+ DateUtil
								.getEnglishMonth(join.get(Calendar.MONTH) + 1)
						+ " " + join.get(Calendar.YEAR);
				String toDate1 = curr.get(Calendar.DAY_OF_MONTH)
						+ " "
						+ DateUtil
								.getEnglishMonth(curr.get(Calendar.MONTH) + 1)
						+ " " + curr.get(Calendar.YEAR);

				if (day < 0) {
					month -= 1;
					curr.add(Calendar.MONTH, -1);
					day = day + curr.getActualMaximum(Calendar.DAY_OF_MONTH);
				}
				if (month < 0) {
					month = (month + 12) % 12;
					year--;
				}
				if (year > 0) {
					durationTv.setText(year + " years");
				} else if (month > 0) {
					durationTv.setText(month + " months");
				} else if (day > 0) {
					durationTv.setText(day + " days");
				}

				// String fromDate = DateUtil.formateDateToUSADate(openDate);
				//
				fromTv.setText("From " + fromDate + " to " + toDate1);
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		case R.id.details_terms:
			Intent it = new Intent(ContractDetailsActivity.this,
					ArgeementActivity.class);
			it.putExtra("from", "tc");
			startActivity(it);
			break;
		default:
			break;
		}
	}

}

package com.huawei.vodafone.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.huawei.vodafone.R;
import com.huawei.vodafone.bean.PlansItem;
import com.huawei.vodafone.ui.activity.SettingsOffersAndExtrarsPlansActivity;
import com.huawei.vodafone.ui.activity.SettingsOffersAndExtrarsPlansDetailsActivity;
import com.huawei.vodafone.util.PreferenceUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author weich
 * @date 2016-1-25 下午3:31:31 个人中心
 */
public class Offers_Fragment1 extends BaseFragment implements OnClickListener {
	private int mCurIndex;
	private List<PlansItem> plansList=new ArrayList<PlansItem>() ;
	private TextView title,data,call,money;
	private ImageView image;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment1, null);
		initView(view);
		return view;
	}

	@SuppressWarnings("unchecked")
	private void initView(View view) {
		Bundle bundle = getArguments();
		mCurIndex = bundle.getInt("type");
		plansList.addAll((ArrayList<PlansItem>) bundle.getSerializable("DiyList"));
		
		title = (TextView) view.findViewById(R.id.title);
		data = (TextView) view.findViewById(R.id.data);
		call = (TextView) view.findViewById(R.id.call);
		money = (TextView) view.findViewById(R.id.money);
		image = (ImageView) view.findViewById(R.id.image);
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						SettingsOffersAndExtrarsPlansDetailsActivity.class);
				Bundle extras = new Bundle();
				extras.putSerializable("plans_details", plansList.get(mCurIndex));
				intent.putExtras(extras);
				getActivity().startActivity(intent);
			}
		});
		for (int i = 0; i < plansList.get(mCurIndex).getLevelList().size(); i++) {
			
		
		if ("100000".equals(plansList.get(mCurIndex).getLevelList().get(i).getLevelId())) {
			data.setText("300MB"+" UK data");
			title.setText(Html.fromHtml("Red "+"<b>300MB</b>"+ " Bundle"));
		} else if ("100001".equals(plansList.get(mCurIndex).getLevelList().get(i).getLevelId())) {
			data.setText("500MB"+" UK data");
			title.setText(Html.fromHtml("Red "+"<b>500MB</b>"+ " Bundle"));
		} else if ("100002".equals(plansList.get(mCurIndex).getLevelList().get(i).getLevelId())) {
			data.setText("800MB"+" UK data");
			title.setText(Html.fromHtml("Red "+"<b>800MB</b>"+ " Bundle"));
		}

		if ("100006".equals(plansList.get(mCurIndex).getLevelList().get(i).getLevelId())) {
			call.setText("100 "+"calls & unlimited texts");
		} else if ("100007".equals(plansList.get(mCurIndex).getLevelList().get(i).getLevelId())) {
			call.setText("300 "+"calls & unlimited texts");
		} else if ("100008".equals(plansList.get(mCurIndex).getLevelList().get(i).getLevelId())) {
			call.setText("500 "+"calls & unlimited texts");
		}
		}
		
		double money = 0;
		int unit = Integer
				.valueOf(plansList.get(mCurIndex).getFee().getCurrencyUnit());
		money = money
				+ plansList.get(mCurIndex).getFee().getCurrencyValue() * Math.pow(10, -unit);
		this.money.setText("€" + money + " " + getResources().getString(R.string.settings_per_month2));
		
		
		
	}

	@Override
	public void onClick(View v) {

	}
}

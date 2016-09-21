package com.huawei.vodafone.products.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huawei.vodafone.R;
import com.huawei.vodafone.bean.BookingInfo;
import com.huawei.vodafone.bean.OfferInstValue;
import com.huawei.vodafone.bean.UserInfo;
import com.huawei.vodafone.net.IRequest;
import com.huawei.vodafone.net.RequestJSon;
import com.huawei.vodafone.net.RequestListener;
import com.huawei.vodafone.net.URLs;
import com.huawei.vodafone.ui.activity.BaseActivity;
import com.huawei.vodafone.ui.adapter.ProductsServiceApapter;
import com.huawei.vodafone.ui.adapter.ProductsServiceThreeAdapter;
import com.huawei.vodafone.ui.myview.MyListview;
import com.huawei.vodafone.ui.myview.NewViewPager;
import com.huawei.vodafone.util.DateUtil;
import com.huawei.vodafone.util.PreferenceUtils;

public class ProductsServiceActivity extends BaseActivity {

	private ImageView back_services;
	private ImageView imageView2;
	private TextView servicePhone;
	private TextView serviceName;
	private ImageView img_left;
	private NewViewPager service_vp;
	private LinearLayout ll_left;
	private LinearLayout ll_right;
	private ImageView img_right;
	private ProductsServiceApapter productsServiceAdapter;
	private ProductsServiceThreeAdapter productsThreeServiceAdapter;
	private ProductsServiceThreeAdapter productsThreeServiceAdapter1;
	private MyListview serices_listview;
	private MyListview serices_listview1;
	private List<ImageView> images;
	private ViewPagerAdapter adapter;
	private ScrollView scrollView;
	private int currentItem;
	private int[] imageIds = new int[] {
			Integer.valueOf(UserInfo.getHeadFaceImage()),
			Integer.valueOf(UserInfo.getHeadFaceImage()) };
	private ArrayList<HashMap<String, Object>> data;
	private ArrayList<HashMap<String, Object>> data1;
	private ArrayList<HashMap<String, Object>> data2;
	private ArrayList<HashMap<String, Object>> data3;
	private HashMap<String, Object> dataMap;
	private TextView subscribedOfferingsTv;
	private TextView bookingTv;
	private ImageView tipIv;
	private TextView ordersTv;
	private LinearLayout ordersLl;
	private RelativeLayout sericesRl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products_service);
		initSecondTitle(getString(R.string.services_context));
		Intent it = getIntent();
		if (it.hasExtra("dataMap"))
			dataMap = (HashMap<String, Object>) getIntent()
					.getSerializableExtra("dataMap");
		initView();
		initListener();
		registerBoradcastReceiver();
	}

	public void initView() {
		back_services = (ImageView) findViewById(R.id.back);
		img_left = (ImageView) findViewById(R.id.service_img_left);
		service_vp = (NewViewPager) findViewById(R.id.service_vp);
		img_right = (ImageView) findViewById(R.id.service_img_right);
		serices_listview = (MyListview) findViewById(R.id.serices_listview);
		serices_listview1 = (MyListview) findViewById(R.id.serices_listview1);
		scrollView = (ScrollView) findViewById(R.id.service_scrollView);
		servicePhone = (TextView) findViewById(R.id.service_phone);
		serviceName = (TextView) findViewById(R.id.service_name);
		ll_left = (LinearLayout) findViewById(R.id.ll_left);
		ll_right = (LinearLayout) findViewById(R.id.ll_right);
		subscribedOfferingsTv = (TextView) findViewById(R.id.subscribed_offerings_tv);
		bookingTv = (TextView) findViewById(R.id.booking_tv);
		tipIv = (ImageView) findViewById(R.id.tip_iv);
		scrollView.smoothScrollTo(0, 20);
		serviceName.setText(UserInfo.getUserName() + "'s phone");
		servicePhone.setText(UserInfo.getUserMobile());
		data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 2; i++) {
			if (i == 1 && !PreferenceUtils.getBoolean(this, "haveDiy"))
				continue;
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("aa", "aa");
			data.add(map);
		}
		productsServiceAdapter = new ProductsServiceApapter(this, data, 1,
				dataMap);
		serices_listview.setAdapter(productsServiceAdapter);

		data1 = new ArrayList<HashMap<String, Object>>();
		data2 = new ArrayList<HashMap<String, Object>>();
		data3 = new ArrayList<HashMap<String, Object>>();
		// for (int i = 0; i < 2; i++) {
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("aa", "aa");
		// data1.add(map);
		// }
		productsThreeServiceAdapter = new ProductsServiceThreeAdapter(this,
				data1);
		serices_listview1.setAdapter(productsThreeServiceAdapter);
		productsThreeServiceAdapter1 = new ProductsServiceThreeAdapter(this,
				data2);
		getData();
		images = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);
			images.add(imageView);
		}
		adapter = new ViewPagerAdapter();
		service_vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				currentItem = position;
				img_left.setBackgroundResource(R.drawable.img_left_light);
				img_right.setBackgroundResource(R.drawable.img_right);

				if (position == 0) {
					img_left.setBackgroundResource(R.drawable.img_left);
					img_right.setBackgroundResource(R.drawable.img_right);
					serviceName.setText("David's phone");
					servicePhone.setText(UserInfo.getUserMobile());
					img_left.setEnabled(false);
					img_right.setEnabled(true);
					data.clear();
					for (int i = 0; i < 2; i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("aa", "aa");
						data.add(map);
					}
					productsServiceAdapter = new ProductsServiceApapter(
							ProductsServiceActivity.this, data, 1, dataMap);
					serices_listview.setAdapter(productsServiceAdapter);

				}

				if (position == imageIds.length - 1) {
					img_right.setBackgroundResource(R.drawable.img_right_gray);
					img_left.setBackgroundResource(R.drawable.img_left_light);
					serviceName.setText("Jen's phone");
					servicePhone.setText("4915298006705");
					img_left.setEnabled(true);
					img_right.setEnabled(false);
					data.clear();
					for (int i = 0; i < 2; i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("aa", "aa");
						data.add(map);
					}
					productsServiceAdapter = new ProductsServiceApapter(
							ProductsServiceActivity.this, data, 2, dataMap);
					serices_listview.setAdapter(productsServiceAdapter);
				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		service_vp.setAdapter(adapter);
		subscribedOfferingsTv.setOnClickListener(this);
		bookingTv.setOnClickListener(this);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tipIv.getLayoutParams().width = subscribedOfferingsTv
						.getWidth();
			}
		}, 100);
		subscribedOfferingsTv.getPaint().setFakeBoldText(true);
		ordersTv = (TextView) findViewById(R.id.orders_tv);
		ordersLl = (LinearLayout) findViewById(R.id.orders_ll);
		sericesRl = (RelativeLayout) findViewById(R.id.serices_rl);
	}

	private void initListener() {
		back_services.setOnClickListener(this);
		img_left.setOnClickListener(this);
		img_right.setOnClickListener(this);
		ll_left.setOnClickListener(this);
		ll_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_left:
			service_vp.setCurrentItem(currentItem - 1);
			break;
		case R.id.ll_right:
			service_vp.setCurrentItem(currentItem + 1);
			break;
		case R.id.back:
			onBackPressed();
			break;
		case R.id.subscribed_offerings_tv:
			if (tipIv.getX() != 0) {
				subscribedOfferingsTv.getPaint().setFakeBoldText(true);
				bookingTv.getPaint().setFakeBoldText(false);
				subscribedOfferingsTv.invalidate();
				bookingTv.invalidate();
				tipAnimate(0);
				data1.clear();
				data1.addAll(data3);
				// serices_listview1.setAdapter(productsThreeServiceAdapter);
				productsThreeServiceAdapter.notifyDataSetChanged();
			}

			break;
		case R.id.booking_tv:
			if (tipIv.getX() != subscribedOfferingsTv.getWidth()) {
				bookingTv.getPaint().setFakeBoldText(true);
				subscribedOfferingsTv.getPaint().setFakeBoldText(false);
				subscribedOfferingsTv.invalidate();
				bookingTv.invalidate();
				tipAnimate(subscribedOfferingsTv.getWidth());
				data1.clear();
				data1.addAll(data2);
				// serices_listview1.setAdapter(productsThreeServiceAdapter1);
				productsThreeServiceAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			view.addView(images.get(position));
			return images.get(position);
		}

	}

	private void getData() {
		IRequest.get(3, URLs.OFFERINSTLIST, RequestJSon.ReferInfo(),
				new RequestListener() {

					@Override
					public void requestSuccess(Object tag, String json) {
						// TODO Auto-generated method stub
						data1.clear();
						data3.clear();
						JsonObject jsonObject = new JsonParser().parse(json)
								.getAsJsonObject();
						if(jsonObject.get("body")==null||jsonObject.get("body").equals("null"))return;
//						if(jsonObject.get("body").getAsJsonObject().get("offerInstList")==null)return;
						if(!jsonObject.get("body").isJsonObject())return;

						JsonArray jsonArray = jsonObject.get("body")
								.getAsJsonObject().get("offerInstList")
								.getAsJsonArray();

						for (int i = 0; i < jsonArray.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							OfferInstValue field = new Gson().fromJson(
									jsonArray.get(i), OfferInstValue.class);
							if (!field.getOfferType().equals("A"))
								continue;
							map.put("offName", field.getOfferName());
							map.put("offDesc", field.getOfferDesc());
							if (field.getPeriodicFee() != null) {
								if (field.getPeriodicFee().getCurrencyValue() != 0) {
									float price = field.getPeriodicFee()
											.getCurrencyValue() / 10000;
									DecimalFormat decimalFormat = new DecimalFormat(
											".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
									String p = decimalFormat.format(price);// format
																			// 返回的是字符串
									map.put("offPrice", p);
								} else {
									map.put("offPrice", "0");
								}
							} else
								map.put("offPrice", "0");
							map.put("date", DateUtil
									.formateDateToTimeStr1(field
											.getEffectiveDate()
											.getUtcDateTime()));
							data1.add(map);
							data3.add(map);
						}
						// HashMap<String, Object> map = new HashMap<String,
						// Object>();
						// map.put("aa", "aa");
						// data1.add(map);
						productsThreeServiceAdapter.notifyDataSetChanged();
					}

					@Override
					public void requestError(Object tag, VolleyError e) {
						// TODO Auto-generated method stub

					}
				});
		IRequest.get(15, URLs.ALLBOOKINFO, RequestJSon.BookingHistory(),
				new RequestListener() {

					@Override
					public void requestSuccess(Object tag, String json) {
						// TODO Auto-generated method stub
						JsonObject jsonObject = new JsonParser().parse(json)
								.getAsJsonObject();
						if(jsonObject.get("body")==null||jsonObject.get("body").equals("null"))return;
						if(!jsonObject.get("body").isJsonObject()){return;}
						JsonArray jsonArray = jsonObject.get("body")
								.getAsJsonObject().get("BookingInfo")
								.getAsJsonArray();

						for (int i = 0; i < jsonArray.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							BookingInfo field = new Gson().fromJson(
									jsonArray.get(i), BookingInfo.class);
							if (!field.getOfferType().equals("A"))
								continue;
							map.put("offName", field.getOfferName());
							map.put("offDesc", null);
							map.put("offPrice", "0");
							map.put("offeringInstStatus",
									field.getOfferingInstStatus());
							map.put("date", field.getCreateTime().getUtcDate()
									.replaceAll("-", "."));
							data2.add(map);
						}
					}

					@Override
					public void requestError(Object tag, VolleyError e) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.vodafone.refreshDIYData");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void unRegisterBoradcastReceiver() {
		unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.vodafone.refreshDIYData".equals(action)) {
				dataMap = (HashMap<String, Object>) intent
						.getSerializableExtra("dataMap");
				productsServiceAdapter.notifyDataSetChanged();
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unRegisterBoradcastReceiver();
	};

	private void tipAnimate(int end) {
		ValueAnimator animator = ValueAnimator.ofFloat(tipIv.getX(), end);
		animator.setTarget(tipIv);
		animator.setDuration(200).start();
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				float value = (Float) animation.getAnimatedValue();
				tipIv.setX(value);
			}
		});
	}
}

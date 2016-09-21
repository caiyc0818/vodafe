package com.huawei.vodafone.ui.activity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.huawei.vodafone.R;
import com.huawei.vodafone.bean.PlansBean;
import com.huawei.vodafone.bean.PlansItem;
import com.huawei.vodafone.bean.QuickCheckInfo;
import com.huawei.vodafone.net.IRequest;
import com.huawei.vodafone.net.RequestJSon;
import com.huawei.vodafone.net.RequestListener;
import com.huawei.vodafone.net.URLs;
import com.huawei.vodafone.ui.adapter.ChatWhatAdapter;
import com.huawei.vodafone.ui.adapter.PlansAdapter;
import com.huawei.vodafone.ui.fragment.FragmentPagerAdapter;
import com.huawei.vodafone.ui.fragment.Offers_Fragment1;
import com.huawei.vodafone.ui.myview.MyListview;
import com.huawei.vodafone.util.DensityUtil;
import com.huawei.vodafone.util.JsonUtils;
import com.huawei.vodafone.util.PreferenceUtils;
import com.huawei.vodafone.util.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

public class SettingsOffersAndExtrarsPlansActivity extends BaseFragmentActivity
		implements OnClickListener, RequestListener {

	private ViewPager viewPager;
	private LinearLayout dotsLayout;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<View> dots;
	private FragmentPagerAdapter mAdapetr;
	private ArrayList<android.support.v4.app.Fragment> pushResourceList = new ArrayList<android.support.v4.app.Fragment>();
	/**
	 * 当前图片的索引号
	 */
	private int currentItem = 0;
	private int count;
	private TextView updateTv;
	private MyListview chatWhatLv;
	private MyListview plans_listView;
	private LinearLayout chatWhatRl;
	private TextView chatWhatTv;
	private TextView text1, data, call, texts;
	private List<String> chatWhatStrs = new ArrayList<String>();
	private ChatWhatAdapter chatWhatAdapter;
	private ImageView chatWhatIv;
	private PlansAdapter PlansAdapter;
	private LinearLayout go_down;
	private int tag = 0;
	private ScrollView mScrollView;
	private RelativeLayout rela_plans;
	private RelativeLayout rela;
	private LinearLayout tips;
	private TextView Filter;
	private TextView un3;
	private TextView money;
	private PlansBean plansBean;
	private List<PlansItem> plansList;
	private QuickCheckInfo quickcheckinfo;
	private LinearLayout ll1;
	private ImageView image_line;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_offers_and_extras_for_you_plans);
		initSecondTitle(getString(R.string.settings_plans));
		initView();
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.carousel_vp);
		dotsLayout = (LinearLayout) findViewById(R.id.carousel_dots);
		chatWhatRl = (LinearLayout) findViewById(R.id.chat_what_rl);
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		image_line = (ImageView) findViewById(R.id.image_line);
		rela = (RelativeLayout) findViewById(R.id.rela);
		rela.setOnClickListener(this);
		chatWhatRl.setOnClickListener(this);
		chatWhatTv = (TextView) findViewById(R.id.chat_what_tv);
		text1 = (TextView) findViewById(R.id.text1);
		data = (TextView) findViewById(R.id.uk_date);
		texts = (TextView) findViewById(R.id.texts);
		call = (TextView) findViewById(R.id.call);
		money = (TextView) findViewById(R.id.un3);
		chatWhatIv = (ImageView) findViewById(R.id.chat_what_iv);
		chatWhatLv = (MyListview) findViewById(R.id.chat_what_lv);
		plans_listView = (MyListview) findViewById(R.id.plans_listView);
		chatWhatIv = (ImageView) findViewById(R.id.chat_what_iv);
		chatWhatAdapter = new ChatWhatAdapter(chatWhatStrs, SettingsOffersAndExtrarsPlansActivity.this);
		chatWhatLv.setAdapter(chatWhatAdapter);
		plans_listView.setFocusable(false);
		plans_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsOffersAndExtrarsPlansActivity.this,
						SettingsOffersAndExtrarsPlansDetailsActivity.class);
				Bundle extras = new Bundle();
				extras.putSerializable("plans_details", plansList.get(position));
				intent.putExtras(extras);
				SettingsOffersAndExtrarsPlansActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.activity_new, R.anim.activity_out);

			}
		});
		chatWhatLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				chatWhatTv.setText(chatWhatStrs.get(arg2));
				chatWhatStrs.clear();
				chatWhatAdapter.notifyDataSetChanged();
				chatWhatIv.animate().rotation(0);
				chatWhatRl.setBackgroundResource(R.drawable.shape_faqs_et_bg);
			}
		});
		go_down = (LinearLayout) findViewById(R.id.go_down);
		rela_plans = (RelativeLayout) findViewById(R.id.rela_plans);
		go_down.setOnClickListener(this);
		rela_plans.setOnClickListener(this);
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		tips = (LinearLayout) findViewById(R.id.tips);
		Filter = (TextView) findViewById(R.id.Filter);
		Filter.setOnClickListener(this);
		// mScrollView.setOnTouchListener(new TouchListenerImpl());
		IRequest.get(16, URLs.RANK_COMBINATION_AND_PRICE, RequestJSon.rankCombinationAndPrice(),
				SettingsOffersAndExtrarsPlansActivity.this);
		IRequest.get(3, URLs.OFFERINSTLIST, RequestJSon.ReferInfo(), SettingsOffersAndExtrarsPlansActivity.this);
		registerBoradcastReceiver();
	}

	private void isDIYShowOrHint() {
		if (PreferenceUtils.getBoolean(SettingsOffersAndExtrarsPlansActivity.this, "haveDiy")) {
			tips.setVisibility(View.VISIBLE);
			double money = 0;
			int unit = Integer
					.valueOf(PreferenceUtils.getString2(SettingsOffersAndExtrarsPlansActivity.this, "moneyUnit"));
			money = money + PreferenceUtils.getLong(SettingsOffersAndExtrarsPlansActivity.this, "money", 01)
					* Math.pow(10, -unit);
			text1.setText(String.valueOf(money));
			String datava = PreferenceUtils.getString(SettingsOffersAndExtrarsPlansActivity.this, "C_DATA_LEVEL");
			String callva = PreferenceUtils.getString(SettingsOffersAndExtrarsPlansActivity.this, "C_UNIT_LEVEL");
			if ("100000".equals(datava)) {
				data.setText("300MB");
				text1.setText("300MB");
			} else if ("100001".equals(datava)) {
				data.setText("500MB");
				text1.setText("500MB");
			} else if ("100002".equals(datava)) {
				data.setText("800MB");
				text1.setText("800MB");
			}

			if ("100006".equals(callva)) {
				call.setText("100 ");
				texts.setText("unlimited ");
			} else if ("100007".equals(callva)) {
				call.setText("300 ");
				texts.setText("unlimited ");
			} else if ("100008".equals(callva)) {
				call.setText("500 ");
				texts.setText("unlimited ");
			}
			this.money.setText("€" + money + " " + getResources().getString(R.string.settings_per_month));
		} else {
			tips.setVisibility(View.GONE);
		}
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("offer_diy_successed");
		// 注册广播
		SettingsOffersAndExtrarsPlansActivity.this.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void unRegisterBoradcastReceiver() {
		SettingsOffersAndExtrarsPlansActivity.this.unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("offer_diy_successed".equals(action)) {
				IRequest.get(3, URLs.OFFERINSTLIST, RequestJSon.ReferInfo(),
						SettingsOffersAndExtrarsPlansActivity.this);
			}
		}
	};

	private class TouchListenerImpl implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = mScrollView.getChildAt(0).getMeasuredHeight();
				if (scrollY == 0) {
					System.out.println("滑动到了顶端 view.getScrollY()=" + scrollY);
				}
				if ((scrollY + height) == scrollViewMeasuredHeight) {
					tips.setVisibility(View.GONE);
					System.out.println("滑动到了底部 scrollY=" + scrollY);
					System.out.println("滑动到了底部 height=" + height);
					System.out.println("滑动到了底部 scrollViewMeasuredHeight=" + scrollViewMeasuredHeight);
				} else {

					tips.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}
			return false;
		}

	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rela_plans:
			break;
		case R.id.rela:
		case R.id.go_down:
			tag++;
			if (tag % 2 == 0) {
				rela_plans.setVisibility(View.GONE);
				((ImageView) go_down.getChildAt(0)).setImageResource(R.drawable.plan_hidden);
			} else {
				rela_plans.setVisibility(View.VISIBLE);
				((ImageView) go_down.getChildAt(0)).setImageResource(R.drawable.plan_show);
			}
			break;
		case R.id.Filter:
			Intent intent = new Intent(SettingsOffersAndExtrarsPlansActivity.this, FilterPlansActivity.class);
			startActivityForResult(intent, 1002);
			activityAnimationUp();
			break;

		default:
			break;
		}
	}

	private void addCarousel(List<Fragment> pushResourceList) {
		// count = tempList.size();
		dots = new ArrayList<View>();
		if (dotsLayout != null) {
			dotsLayout.removeAllViews();
		}
		count = pushResourceList.size();
		fragments.addAll(pushResourceList);
		// 添加Fragment
		for (int i = 0; i < count; i++) {
			// TODO 添加标题下面的点********当滚动主题有时再放开下面代码******
			ImageView dotsView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 0, 5, 5);
			dotsView.setLayoutParams(params);
			dotsView.setBackgroundResource(R.drawable.banner1);
			if (0 == i) {
				dotsView.setBackgroundResource(R.drawable.banner_icon);
			}
			dotsView.setTag(i);
			dotsLayout.addView(dotsView);
			dots.add(dotsView);
			dotsLayout.getChildAt(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					viewPager.setCurrentItem((Integer) v.getTag());
				}

			});

		}
		mAdapetr = new FragmentPagerAdapter(SettingsOffersAndExtrarsPlansActivity.this.getSupportFragmentManager(),
				fragments);
		// 去除过度动画
		// setViewPagerScrollSpeed();
		// 控制加载的页数
		// viewPager.setOffscreenPageLimit(1);
		// viewPager.setCurrentItem(0, true);
		viewPager.setAdapter(mAdapetr);
		// 设置有内容显示
		ll1.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, DensityUtil.dip2px(this, -15), 0, 0);
		image_line.setLayoutParams(params);
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		if (count > 0) {
			handler.postDelayed(new ScrollTask(), 1000);

		}
	}

	// 解决viewpager切换动画时间
	private void setViewPagerScrollSpeed() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
			mScroller.set(viewPager, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

	public class FixedSpeedScroller extends Scroller {
		private int mDuration = 0;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
			super(context, interpolator, flywheel);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author zhangbingkang
	 * @version [2013-6-18]
	 * @see [相关类/方法]
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			int max = fragments.size();
			if (position == max) { // 最后一页时回到第一页
				viewPager.setCurrentItem(0, false);
				position = 0;
			}
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.banner1);
			dots.get(position).setBackgroundResource(R.drawable.banner_icon);
			oldPosition = position;

			// if (!isScrolling) {
			// int max = imageViews.size() + 1;
			// int position = (currentPosition + 1) % imageViews.size();
			// viewPager.setCurrentItem(position, true);
			// if (position == max) { // 最后一页时回到第一页
			// viewPager.setCurrentItem(1, false);
			// }
			// }
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	// 定时器
	Handler handler = new Handler();

	/**
	 * 换行切换任务
	 * 
	 * @author zhangbingkang
	 * @version [2013-6-18]
	 * @see [相关类/方法]
	 */
	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % fragments.size();
				viewPager.setCurrentItem(currentItem);
				// 通过Handler切换图片
				handler.postDelayed(this, 5000);
			}
		}
	}

	private void addChatWhatData() {
		String[] chatWhats = getResources().getStringArray(R.array.offer_array);
		for (int i = 0; i < chatWhats.length; i++) {
			chatWhatStrs.add(chatWhats[i]);
		}
		chatWhatAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 1002:
			if (arg1 == -1) {

			}
			break;

		default:
			break;
		}
	}

	@Override
	public void requestSuccess(Object tag, String json) {
		// TODO Auto-generated method stub
		switch ((Integer) tag) {
		case 16:
			JSONObject jsonObj;
			try {
				jsonObj = new JSONObject(json);
				JSONObject obj = jsonObj.getJSONObject("header");
				String code = obj.getString("resultCode");
				if ("0".equals(code)) {
					plansBean = JsonUtils.getBodyObject(json, PlansBean.class);
					plansList = plansBean.getItemList();
					PlansAdapter = new PlansAdapter(plansList, SettingsOffersAndExtrarsPlansActivity.this);
					plans_listView.setAdapter(PlansAdapter);
					for (int i = 0; i < plansList.size(); i++) {
						if (i <= 2) {
							Bundle data = new Bundle();
							data.putInt("type", i);
							data.putSerializable("DiyList", (Serializable) plansList);
							Offers_Fragment1 OfferFragment = new Offers_Fragment1();
							OfferFragment.setArguments(data);
							pushResourceList.add(OfferFragment);
						} else {
							break;
						}
					}
					addCarousel(pushResourceList);
				} else {

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			if (JsonUtils.getHeadCode(json).equals("0")) {
				PreferenceUtils.delString(this, "haveDiy");
				PreferenceUtils.setLong(this, "money", 1l);
				PreferenceUtils.delString(this, "moneyUnit");
				PreferenceUtils.delString(this, "C_DATA_LEVEL");
				PreferenceUtils.delString(this, "C_UNIT_LEVEL");
				PreferenceUtils.delString(this, "OfferInstId");
				quickcheckinfo = JsonUtils.getBodyObject(json, QuickCheckInfo.class);
				QuickCheck();
			}
			break;
		}
	}

	private void QuickCheck() {
		// TODO Auto-generated method stub
		// 判断是否有DIY基础套餐 蔡雨成
		if (quickcheckinfo.getOfferInstList().size() > 0) {
			for (int i = 0; i < quickcheckinfo.getOfferInstList().size(); i++) {
				if (!StringUtils.isEmpty(quickcheckinfo.getOfferInstList().get(i).getOfferId())) {
					if ("10102".equals(quickcheckinfo.getOfferInstList().get(i).getOfferId())) {
						PreferenceUtils.setBoolean(this, "haveDiy", true);
						PreferenceUtils.setLong(this, "money",
								quickcheckinfo.getOfferInstList().get(i).getPeriodicFee().getCurrencyValue());
						PreferenceUtils.setString(this, "moneyUnit",
								quickcheckinfo.getOfferInstList().get(i).getPeriodicFee().getCurrencyUnit());
						PreferenceUtils.setString(this, "OfferInstId",
								quickcheckinfo.getOfferInstList().get(i).getOfferInstId());
						for (int j = 0; j < quickcheckinfo.getOfferInstList().get(i).getInstParameterValueList()
								.size(); j++) {
							if ("C_DATA_LEVEL".equals(quickcheckinfo.getOfferInstList().get(i)
									.getInstParameterValueList().get(j).getParameterCode())) {
								PreferenceUtils.setString(this, "C_DATA_LEVEL", quickcheckinfo.getOfferInstList().get(i)
										.getInstParameterValueList().get(j).getParameterId());
							}
							if ("C_UNIT_LEVEL".equals(quickcheckinfo.getOfferInstList().get(i)
									.getInstParameterValueList().get(j).getParameterCode())) {
								PreferenceUtils.setString(this, "C_UNIT_LEVEL", quickcheckinfo.getOfferInstList().get(i)
										.getInstParameterValueList().get(j).getParameterId());
							}
						}
					}
				}
			}

		}

		isDIYShowOrHint();
	}

	@Override
	public void requestError(Object tag, VolleyError e) {
		// TODO Auto-generated method stub

	}

}

package com.huawei.vodafone.ui.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.huawei.vodafone.MyApplication;
import com.huawei.vodafone.R;
import com.huawei.vodafone.bean.ConsumeAndQuota;
import com.huawei.vodafone.bean.DialogInfo;
import com.huawei.vodafone.bean.PersonalInfo;
import com.huawei.vodafone.bean.QuickCheckInfo;
import com.huawei.vodafone.bean.UserInfo;
import com.huawei.vodafone.bills.activity.BillsActivity;
import com.huawei.vodafone.bills.activity.ItemisedBillActivity;
import com.huawei.vodafone.bills.activity.NetWorkUsageActivity;
import com.huawei.vodafone.broadcast.PushReceiver;
import com.huawei.vodafone.db.DBAdapter;
import com.huawei.vodafone.db.DBSignInAdapter;
import com.huawei.vodafone.db.JokeMsg;
import com.huawei.vodafone.listener.Listener.Click;
import com.huawei.vodafone.listener.Listener.MyReceiver;
import com.huawei.vodafone.net.IRequest;
import com.huawei.vodafone.net.RequestJSon;
import com.huawei.vodafone.net.RequestListener;
import com.huawei.vodafone.net.URLs;
import com.huawei.vodafone.products.activity.ProductsServiceActivity;
import com.huawei.vodafone.ui.adapter.CurrentSpendListAdapter;
import com.huawei.vodafone.ui.adapter.MyPagerAdapter;
import com.huawei.vodafone.ui.myview.CircleImageView;
import com.huawei.vodafone.ui.myview.RoundProgressBar;
import com.huawei.vodafone.ui.myview.dialog.ErrorDialog;
import com.huawei.vodafone.ui.myview.dialog.LogoutDialog;
import com.huawei.vodafone.ui.myview.dialog.LogoutDialog.OnClickLogOutListener;
import com.huawei.vodafone.ui.myview.dialog.SuccessDialog;
import com.huawei.vodafone.util.DateUtil;
import com.huawei.vodafone.util.JsonUtils;
import com.huawei.vodafone.util.PreferenceUtils;
import com.huawei.vodafone.util.StringUtils;
import com.huawei.vodafone.util.TAGUtil;
import com.huawei.vodafone.util.UnitUtil;
import com.huawei.vodafone.zxing.CaptureActivity;

public class MainActivity extends BaseActivity implements OnClickListener,
		OnClickLogOutListener, RequestListener, OnLongClickListener {
	/**
	 * 侧滑按钮
	 */
	private ImageView menuBtn;
	private TextView notification_marks;
	private DrawerLayout DrawerLayout;
	private ImageView backMenu;
	private ImageView networkChooseIcon;
	private LinearLayout networkLayout;
	private RelativeLayout networkChoose;
	private TextView currentSpend;
	private TextView usage_history;
	private TextView notifications;
	private TextView version;
	private TextView products_text;
	private ArrayList<View> dots;
	private List<JokeMsg> data_notification = new ArrayList<JokeMsg>();
	private int count = 0;
	/**
	 * 点的父布局
	 */
	private LinearLayout dotsLayout;
	/**
	 * 消息数量
	 */
	private List<List<DialogInfo>> dialoglist;

	private ViewPager newsPager;

	private RoundProgressBar progreessbar;
	private Handler handler;
	private Runnable run;
	private int proall, proadd;
	private TextView surplusFlow, whichFlow, allFlow;
	private ImageView addCircle;

	private TextView addGb, addMore, refreshed;
	private RelativeLayout currentSpentRela;
	private CircleImageView userPortrait, user_three, user_two;
	private ImageView refreshedImage;
	private Animation rotate, rotateout, rotatein, alpha, alout, alphadelaye,
			aloutdelaye, aloutdelayemore;
	private ImageView speakSelect, callSelect, smsSelect;
	private LinearLayout mGoneImage;
	private View mainPop;
	private FrameLayout fl;
	private FrameLayout circleFrameLayout;
	private LinearLayout for_tip_three;
	private RelativeLayout ll_support;
	private ScrollView scrollView;
	public static MainActivity currentActivity;
	private TextView settings;
	private ListView spendList, allspendList;
	private CurrentSpendListAdapter SpendListAdapter, allSpendListAdapter;
	private ImageView spendChoose, allspendChoose;
	private LinearLayout spendLayout, allspendLayout;
	private int select = 0;
	private TextView addPlus;
	private TextView log_out;
	private MyPagerAdapter newsAdapter;
	private TextView balanceText;
	private TextView userName;
	private TextView offer_extras;
	private ImageView search;
	private ImageView capture;
	private TextView support_text;
	private LogoutDialog logout_dialog;
	private Button itemised_spend;
	private Button billing_history;
	private TextView lastBillTitle;
	private TextView pointsNum;
	private TextView messageNumTv;
	private QuickCheckInfo quickcheckinfo;
	private TextView dayLeftText;
	private String WhichSelect = "Data", WhichUnit = "GB";
	private float left, all, add, allangle, addangle;
	private ImageView cancel_diy;
	private boolean showdiynoti = true, showadddata = false, proscenium,
			manualRefresh = false;
	private Click click;
	private ErrorDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		currentActivity = this;
		initView();
		isFirst();

	}

	/** 第一次弹出功能引导 **/
	private void isFirst() {
		if (!PreferenceUtils.getBoolean(getApplicationContext(), "isFirstOne")) {
			newsPager.setVisibility(View.INVISIBLE);
			showPop();
			PreferenceUtils.setBoolean(getApplicationContext(), "isFirstOne",
					true);
		}
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		DrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
		// DrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LsOCKED_CLOSED);//禁止手势滑动
		newsPager = (ViewPager) findViewById(R.id.card_discount_vp);
		menuBtn = (ImageView) findViewById(R.id.menu_btn);
		notification_marks = (TextView) findViewById(R.id.notification_marks);
		backMenu = (ImageView) findViewById(R.id.back_menu);
		search = (ImageView) findViewById(R.id.search);
		capture = (ImageView) findViewById(R.id.capture);
		RelativeLayout signInRela = (RelativeLayout) findViewById(R.id.sign_in_rela);
		RelativeLayout refreshed_rl = (RelativeLayout) findViewById(R.id.refreshed_rl);
		dayLeftText = (TextView) findViewById(R.id.day_left_text);
		itemised_spend = (Button) findViewById(R.id.itemised_spend);
		billing_history = (Button) findViewById(R.id.billing_history);
		userPortrait = (CircleImageView) findViewById(R.id.user_portrait);
		user_two = (CircleImageView) findViewById(R.id.user_two);
		networkChooseIcon = (ImageView) findViewById(R.id.network_choose_icon);
		networkLayout = (LinearLayout) findViewById(R.id.network_layout);
		networkChoose = (RelativeLayout) findViewById(R.id.network_choose);
		currentSpend = (TextView) findViewById(R.id.current_spend);
		usage_history = (TextView) findViewById(R.id.usage_history);
		lastBillTitle = (TextView) findViewById(R.id.last_bill_title);
		notifications = (TextView) findViewById(R.id.notifications);
		version = (TextView) findViewById(R.id.version);
		offer_extras = (TextView) findViewById(R.id.offer_extras);
		dotsLayout = (LinearLayout) findViewById(R.id.card_discount_dots);
		progreessbar = (RoundProgressBar) findViewById(R.id.id_flow_progreessbar);
		settings = (TextView) findViewById(R.id.settings);
		whichFlow = (TextView) findViewById(R.id.which_flow);
		surplusFlow = (TextView) findViewById(R.id.surplus_flow);
		allFlow = (TextView) findViewById(R.id.all_flow);
		addCircle = (ImageView) findViewById(R.id.add_circle);
		userName = (TextView) findViewById(R.id.user_name);
		addPlus = (TextView) findViewById(R.id.add_plus);
		products_text = (TextView) findViewById(R.id.products_text);
		addGb = (TextView) findViewById(R.id.add_gb);
		addMore = (TextView) findViewById(R.id.add_more);
		refreshed = (TextView) findViewById(R.id.refreshed);
		refreshedImage = (ImageView) findViewById(R.id.refreshed_image);
		speakSelect = (ImageView) findViewById(R.id.speak_select);
		callSelect = (ImageView) findViewById(R.id.call_select);
		smsSelect = (ImageView) findViewById(R.id.sms_select);
		user_three = (CircleImageView) findViewById(R.id.user_three);
		fl = (FrameLayout) findViewById(R.id.main_pop);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		mGoneImage = (LinearLayout) findViewById(R.id.image_gone);
		circleFrameLayout = (FrameLayout) findViewById(R.id.prog_frame);
		for_tip_three = (LinearLayout) findViewById(R.id.for_tip_three);
		ll_support = (RelativeLayout) findViewById(R.id.ll_support);
		support_text = (TextView) findViewById(R.id.support_text);
		spendChoose = (ImageView) findViewById(R.id.spend_choose_icon);
		allspendChoose = (ImageView) findViewById(R.id.all_spend_choose_icon);
		spendLayout = (LinearLayout) findViewById(R.id.spend_layout);
		allspendLayout = (LinearLayout) findViewById(R.id.all_spend_layout);
		spendList = (ListView) findViewById(R.id.current_spend_list);
		currentSpentRela = (RelativeLayout) findViewById(R.id.current_spent_rela);
		pointsNum = (TextView) findViewById(R.id.points_num);
		RelativeLayout allCurrentSpentRela = (RelativeLayout) findViewById(R.id.all_current_spent_rela);
		log_out = (TextView) findViewById(R.id.log_out);
		balanceText = (TextView) findViewById(R.id.balance_tv);
		messageNumTv = (TextView) findViewById(R.id.message_num_tv);
		logout_dialog = new LogoutDialog(this);
		logout_dialog.setOnClickLogOutListener(MainActivity.this);
		cancel_diy = (ImageView) findViewById(R.id.cancel_diy);
		cancel_diy.setOnLongClickListener(this);
		menuBtn.setOnClickListener(this);
		backMenu.setOnClickListener(this);
		networkChoose.setOnClickListener(this);
		userPortrait.setOnClickListener(this);
		addCircle.setOnClickListener(this);
		settings.setOnClickListener(this);
		currentSpentRela.setOnClickListener(this);
		notifications.setOnClickListener(this);
		version.setOnClickListener(this);
		allCurrentSpentRela.setOnClickListener(this);
		refreshed_rl.setOnClickListener(this);
		signInRela.setOnClickListener(this);
		addGb.setOnClickListener(this);
		addMore.setOnClickListener(this);
		log_out.setOnClickListener(this);
		callSelect.setOnClickListener(this);
		for_tip_three.setOnClickListener(this);
		products_text.setOnClickListener(this);
		offer_extras.setOnClickListener(this);
		search.setOnClickListener(this);
		capture.setOnClickListener(this);
		ll_support.setOnClickListener(this);
		support_text.setOnClickListener(this);
		itemised_spend.setOnClickListener(this);
		billing_history.setOnClickListener(this);
		speakSelect.setOnClickListener(this);
		smsSelect.setOnClickListener(this);
		currentSpend.setOnClickListener(this);
		usage_history.setOnClickListener(this);

		SpendListAdapter = new CurrentSpendListAdapter(getBaseContext(), 1);
		spendList.setAdapter(SpendListAdapter);
		spendList.setSelector(new ColorDrawable(Color.TRANSPARENT));
		allspendList = (ListView) findViewById(R.id.all_current_spend_list);
		allSpendListAdapter = new CurrentSpendListAdapter(getBaseContext(), 4);
		allspendList.setAdapter(allSpendListAdapter);
		int date = DateUtil.MonthLeftDate() + 1;
		dayLeftText.setText(Html.fromHtml("<b>" + date + " days</b> to reset"));

		UserInfo();
		setanim();
		fake(0);
		changeUser(true, UserInfo.getSelect());
		registerBoradcastReceiver();
		Request(TAGUtil.QUICK_CHECK);
		jump(getIntent());
		startTask();
	}

	@Override
	protected void onResume() {
		super.onResume();
		marks();
		proscenium = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		proscenium = false;
	}

	/** 显示notification未读个数 **/
	private void marks() {
		count = 0;
		DBAdapter db = new DBAdapter(this);
		db.openDb();
		if (db.queryAllRecord() != null) {
			data_notification = db.queryAllRecord();
		}
		for (int i = 0; i < data_notification.size(); i++) {
			if (data_notification.get(i).getCode().equals("0")) {
				count++;
			}
		}
		db.closeDb();
		if (count == 0) {
			notification_marks.setVisibility(View.GONE);
		} else {
			notification_marks.setVisibility(View.VISIBLE);
			notification_marks.setText(count + "");
		}
	}

	/**
	 * 切换账户
	 */
	private void changeUser(boolean firstin, int select) {
		UserInfo.setSelect(select);
		setempty(false);
		userName.setText(UserInfo.getUserName() + "'s");
		UserInfo.setTime(System.currentTimeMillis() + "");
		userPortrait.setImageResource(Integer.valueOf(UserInfo
				.getHeadFaceImage()));
		itemised_spend.setText("See " + UserInfo.getUserName()
				+ "'s itemised spend");
		lastBillTitle.setText(UserInfo.getUserName() + "'s current spend");
		addAds();
		if (!firstin) {
			Request(TAGUtil.GETUI);
		}
		switch (UserInfo.getListsize()) {
		case 0:
		case 1:
			user_two.setVisibility(View.GONE);
			user_three.setVisibility(View.GONE);
			break;
		case 2:
			user_two.setVisibility(View.VISIBLE);
			user_three.setVisibility(View.GONE);
			if (UserInfo.getSelect() == 0) {
				user_two.setImageResource(Integer.valueOf(UserInfo
						.getHeadFaceImage(1)));
			} else {
				user_two.setImageResource(Integer.valueOf(UserInfo
						.getHeadFaceImage(0)));
			}
			break;
		default:
			user_two.setVisibility(View.VISIBLE);
			user_three.setVisibility(View.VISIBLE);
			ArrayList<PersonalInfo> list = UserInfo.orderByTime();
			user_two.setImageResource(Integer.valueOf(list.get(1)
					.getHeadFaceImage()));
			user_three.setImageResource(Integer.valueOf(list.get(2)
					.getHeadFaceImage()));
			break;
		}
	}

	/**
	 * 账户数据
	 */
	private void UserInfo() {
		PersonalInfo loginitem = new PersonalInfo();
		String phone = getIntent().getStringExtra("name");
		if (StringUtils.isEmpty(phone)) {
			phone = StringUtils.isEmpty(UserInfo.getUserMobile()) ? "4915298006711"
					: UserInfo.getUserMobile();
		}
		loginitem.setUserMobile(phone);
		switch (phone.substring(phone.length() - 1)) {
		case "0":
		case "5":
			loginitem.setUserName("David");
			loginitem.setHeadFaceImage(String.valueOf(R.drawable.david_ic));
			break;
		case "1":
		case "6":
			loginitem.setUserName("Peter Hunt");
			loginitem
					.setHeadFaceImage(String.valueOf(R.drawable.peter_hunt_ic));
			break;
		case "2":
		case "7":
			loginitem.setUserName("Martha Cozza");
			loginitem.setHeadFaceImage(String
					.valueOf(R.drawable.martha_cozza_ic));
			break;
		case "3":
		case "8":
			loginitem.setUserName("Lucy Lin");
			loginitem.setHeadFaceImage(String.valueOf(R.drawable.lucy_ic));
			break;
		case "4":
		case "9":
			loginitem.setUserName("Hanna");
			loginitem.setHeadFaceImage(String.valueOf(R.drawable.hanna_ic));
			break;
		default:
			break;
		}
		// AllPersonalInfo all = new AllPersonalInfo();
		// ArrayList<PersonalInfo> list = new ArrayList<PersonalInfo>();
		// PersonalInfo loginitem2 = new PersonalInfo();
		// loginitem2.setUserName("Jen");
		// loginitem2.setUserMobile("4915298006706");
		// loginitem2.setHeadFaceImage(String.valueOf(R.drawable.jen));
		// list.add(loginitem);
		// list.add(loginitem2);
		// all.setPersonalInfos(list);
		// all.setListsize("2");
		// all.setSelect("0");
		// UserInfo.getInstance().SaveUserInfo(all);

		int num = -1;
		for (int i = 0; i < UserInfo.getListsize(); i++) {
			String aa = UserInfo.getUserMobile(i);
			if (phone.equals(aa)) {
				num = i;
				break;
			}
		}
		if (num != -1) {
			UserInfo.setSelect(num);
			UserInfo.setTime(System.currentTimeMillis() + "");
		} else {
			UserInfo.getInstance().SavePersonalInfo(UserInfo.getListsize(),
					loginitem);
			UserInfo.setSelect(UserInfo.getListsize());
			UserInfo.setListsize(UserInfo.getListsize() + 1);
			UserInfo.setTime(System.currentTimeMillis() + "");
		}
	}

	/**
	 * 假数据
	 */
	private void fake(int num) {
		PreferenceUtils.setString(getBaseContext(), "Addbalance", "0");
		PreferenceUtils.setBoolean(getBaseContext(), "BugDataOrUnit", false);
		PreferenceUtils.setInt(getBaseContext(), "SignInPoints", 1050);
		PreferenceUtils.setInt(getBaseContext(), "SignInDays", 9);
		PreferenceUtils.setInt(getBaseContext(), "huaweimate8", 399);
		PreferenceUtils.setInt(getBaseContext(), "1GData", 4997);
		PreferenceUtils.setInt(getBaseContext(), "2GData", 4999);

		PreferenceUtils.setLong(getBaseContext(), "SignInTime",
				System.currentTimeMillis() - 86400000);

		DBSignInAdapter db = new DBSignInAdapter(this);
		db.openDb();
		db.delete();
		db.insert("Huawei Mate8", "2016-03-28", -500000, 2);
		db.insert("2GB Data", "2016-06-05", -200, 2);
		db.insert("Huawei Mate8", "2016-03-28", 399, 3);
		db.insert("2GData", "2016-03-28", 4997, 3);
		for (int i = 1; i < 10; i++) {
			db.insert("sign in", DateUtil.getPreviousDate(-i), i, 5, 1);
		}

		pointsNum.setText("1,050 Points");
		if (dialoglist == null) {
			dialoglist = new ArrayList<List<DialogInfo>>();
		} else {
			dialoglist.clear();
		}
		addnotification("", "");
		PushReceiver.MyReceiver(new MyReceiver() {

			@Override
			public void onClicked(String channel, String context) {
				addnotification(channel, context);
			}
		});

		click = new Click() {

			@Override
			public void onClicked(Object num, int num1, String num2,
					boolean num3) {
				int position = (Integer) num;
				switch (num1) {
				case 1:
					if (num3) {
						Intent intent1 = new Intent(MainActivity.this,
								MainAddDataActivity.class);
						intent1.putExtra("type", 0);
						startActivityForResult(intent1, TAGUtil.tag5);
						activityAnimationUp();
					}
					break;
				case 2:
					if (num3) {
						Intent intent2 = new Intent(MainActivity.this,
								RechargeActivity.class);
						startActivityForResult(intent2, TAGUtil.tag7);
						activityAnimationUp();
					}
					break;
				case 3:
					if (num3) {
						Intent intent1 = new Intent(MainActivity.this,
								MainAddDataActivity.class);
						intent1.putExtra("type", 2);
						startActivityForResult(intent1, TAGUtil.tag5);
						activityAnimationUp();
					}
					break;
				case 4:
					if (num3) {
						Intent intent4 = new Intent(MainActivity.this,
								MainAddDataActivity.class);
						intent4.putExtra("add", 2);
						intent4.putExtra("type", 0);
						startActivityForResult(intent4, TAGUtil.tag5);
						activityAnimationUp();
					}
					break;
				case 5:
					if (num3) {
						Intent intent2 = new Intent(MainActivity.this,
								RechargeActivity.class);
						intent2.putExtra("number", 10);
						startActivityForResult(intent2, TAGUtil.tag7);
						activityAnimationUp();
					}
					break;
				case 6:
					break;
				case 7:
					if (num3) {
						Intent intent = new Intent(MainActivity.this,
								DiyPlansActivity.class);
						startActivity(intent);
						activityAnimationOpen();
					}
					break;
				default:
					break;
				}
				removelist(position);
			}
		};
	}

	/**
	 * 消息数据
	 * 
	 * @param num
	 */
	private void addnotification(String channel, String context) {
		// DialogInfo item1 = new DialogInfo();
		// item1.setId(1);
		// item1.setTitle("Your new bill is ready");
		// item1.setContent("This month your bill was a bit higher than usual.would you like to review your out of plan spend?");
		// item1.setConfirm("Yes,please");
		// item1.setCancel("No, thanks");
		// DialogInfo item2 = new DialogInfo();
		// item2.setId(2);
		// item2.setTitle("<b>2GB</b> Data add-on added");
		// item2.setContent("This will expire on the 22nd March.<b>You can always tap the gauge to see the details</b>");
		// item2.setConfirm("Ok,got it!");
		// item2.setCancel("View details");

		if (dialoglist.size() <= UserInfo.getListsize()) {
			for (int i = dialoglist.size(); i <= UserInfo.getListsize(); i++) {
				dialoglist.add(new ArrayList<DialogInfo>());
			}
		}

		switch (channel) {
		case "":
			break;
		case "Data Add-on":
			DialogInfo item0 = new DialogInfo();
			item0.setId(0);
			item0.setTitle("Congratulations!");
			item0.setContent(context);
			item0.setCancel("Ok,got it!");
			item0.setConfirm("View details");
			dialoglist.get(UserInfo.getSelect()).add(item0);
			break;
		case "DataUsgNtfctnThrshld1":
			DialogInfo item1 = new DialogInfo();
			item1.setId(1);
			item1.setTitle("Want more data?");
			item1.setContent(context);
			item1.setConfirm("Yes, please");
			item1.setCancel("No, thanks");
			dialoglist.get(UserInfo.getSelect()).add(item1);
			break;
		case "RchrgReminder":
			DialogInfo item2 = new DialogInfo();
			item2.setId(2);
			item2.setTitle("We've noticed...");
			item2.setContent(context);
			item2.setConfirm("Yes, please");
			item2.setCancel("No, thanks");
			dialoglist.get(UserInfo.getSelect()).add(item2);
			break;
		case "Content Bundle":
			DialogInfo item3 = new DialogInfo();
			item3.setId(3);
			item3.setTitle("We've noticed...");
			item3.setContent(context);
			item3.setConfirm("Yes, please");
			item3.setCancel("No, thanks");
			dialoglist.get(UserInfo.getSelect()).add(item3);
			break;
		case "UpSell Bundle":
			DialogInfo item4 = new DialogInfo();
			item4.setId(4);
			item4.setTitle("We've noticed...");
			item4.setContent(context);
			item4.setConfirm("Yes, please");
			item4.setCancel("No, thanks");
			dialoglist.get(UserInfo.getSelect()).add(item4);
			break;
		case "LowBalance":
			DialogInfo item5 = new DialogInfo();
			item5.setId(5);
			item5.setTitle("We've noticed...");
			item5.setContent(context);
			item5.setConfirm("Yes, please");
			item5.setCancel("No, thanks");
			dialoglist.get(UserInfo.getSelect()).add(item5);
			break;
		case "TopUp":
			DialogInfo item6 = new DialogInfo();
			item6.setId(6);
			item6.setTitle("Congratulations!");
			item6.setContent("You topped up <b>€"
					+ PreferenceUtils.getString(getBaseContext(), "Addbalance")
					+ "</b>.<br/> Your credit is now <b>"
					+ UserInfo.getBalanceWithSign() + "</>");
			item6.setConfirm("View details");
			item6.setCancel("Ok,got it!");
			dialoglist.get(UserInfo.getSelect()).add(item6);
			break;
		case "DIYNotHave":
			DialogInfo item7 = new DialogInfo();
			item7.setId(7);
			item7.setTitle("Plan Configuration");
			item7.setContent("Welcome to Smart Pricing, please configure your plan first.");
			item7.setConfirm("Yes, please");
			item7.setCancel("No, thanks");
			dialoglist.get(UserInfo.getSelect()).add(item7);
			break;
		default:
			break;
		}
		addAds();
	}

	private void removelist(int position) {
		try {
			dialoglist.get(UserInfo.getSelect()).remove(position);
			addAds();
		} catch (IndexOutOfBoundsException e) {

		}
	}

	/**
	 * 初始化动画
	 */
	private void setanim() {
		// 必须初始化不同实例，否者同一个实例一次只能出现一种状态，改了时间没用
		rotate = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
		rotateout = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.rotateout);
		rotatein = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.rotatein);
		alpha = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha);
		alout = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alout);
		alphadelaye = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.alpha);
		aloutdelaye = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.alout);
		aloutdelayemore = AnimationUtils.loadAnimation(getBaseContext(),
				R.anim.alout);

		rotateout.setFillAfter(true);// 这句写在xml中不起作用
		rotatein.setFillAfter(true);// 这句写在xml中不起作用
		alphadelaye.setStartOffset(300);
		aloutdelaye.setStartOffset(300);
		aloutdelayemore.setStartOffset(600);
		speakSelect.startAnimation(alout);
		callSelect.startAnimation(aloutdelaye);
		smsSelect.startAnimation(aloutdelayemore);
	}

	/**
	 * 进度条
	 */
	private void initBar() {
		if (!isNeedRefresh())
			return;
		if (select == 0) {
			left = Float.valueOf(UserInfo.getLeftData().replace(",", ""));
			all = Float.valueOf(UserInfo.getAllData().replace(",", ""));
			add = Float.valueOf(UserInfo.getAddData().replace(",", ""));
			WhichSelect = "Data";
			WhichUnit = "GB";
			surplusFlow.setTextSize(32);
		} else if (select == 1) {
			left = Float.valueOf(UserInfo.getLeftUnit().replace(",", ""));
			all = Float.valueOf(UserInfo.getAllUnit().replace(",", ""));
			add = Float.valueOf(UserInfo.getAddUnit().replace(",", ""));
			WhichSelect = "Calls";
			WhichUnit = "Min";
			surplusFlow.setTextSize(32);
		} else if (select == 2) {
			WhichSelect = "Texts";
			surplusFlow.setTextSize(22);
			surplusFlow.setText("Unlimited");
		}
		progreessbar.setProgressth(0, 0);
		if (all == 0) {
			allangle = addangle = 0;
		} else {
			allangle = left * 100 / all;
			addangle = add * 100 / all;
		}
		// 新需求,修改的斑马线
		if (!showadddata) {
			addangle = 0;
		} else {
			showadddata = false;
		}

		proadd = proall = 0;
		if (handler == null) {
			handler = new Handler();
			run = new Runnable() {

				@Override
				public void run() {
					if (select == 0) {
						if (proall <= allangle) {
							whichFlow.setText(WhichSelect);
							surplusFlow.setText(StringUtils.formatDecimalFloat(
									proall * all / 100, 2));
							allFlow.setText("of " + all + WhichUnit + " left");
							progreessbar.setProgress(proall++);
						} else if (proadd <= addangle) {
							surplusFlow.setText(left + "");
							progreessbar.setProgressth(0, proadd++);
						} else {
							surplusFlow.setText(left + "");
							progreessbar.setProgressth(proadd - addangle,
									proadd++);
						}
						if (proall <= allangle
								|| (addangle > 0 && proadd <= allangle)) {
							handler.postDelayed(run, 5);
						} else if (addangle > 0) {
							addangle = 0;
							handler.postDelayed(run, 5000);
						}
					} else if (select == 1) {
						if (proall <= allangle) {
							whichFlow.setText(WhichSelect);
							surplusFlow.setText(StringUtils.formatDecimalFloat(
									proall * all / 100, 0));
							allFlow.setText("of " + UserInfo.getAllUnit()
									+ WhichUnit + " left");
							progreessbar.setProgress(proall++);
						} else if (proadd <= addangle) {
							surplusFlow.setText(UserInfo.getLeftUnit());
							progreessbar.setProgressth(0, proadd++);
						} else {
							surplusFlow.setText(UserInfo.getLeftUnit());
							progreessbar.setProgressth(proadd - addangle,
									proadd++);
						}
						if (proall <= allangle
								|| (addangle > 0 && proadd <= allangle)) {
							handler.postDelayed(run, 5);
						} else if (addangle > 0) {
							addangle = 0;
							handler.postDelayed(run, 5000);
						}
					} else {
						whichFlow.setText(WhichSelect);
						surplusFlow.setText("Unlimited");
						allFlow.setText("");
						progreessbar.setProgress(proall++);
						if (proall <= 100)
							handler.postDelayed(run, 5);
					}
				}
			};
		} else {
			handler.removeCallbacks(run);
		}
		handler.postDelayed(run, 5);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_btn:
			DrawerLayout.openDrawer(Gravity.RIGHT);
			break;
		case R.id.back_menu:
			DrawerLayout.closeDrawers();
			break;
		case R.id.network_choose:
			if (networkLayout.isShown()) {
				networkLayout.setVisibility(View.GONE);
				networkChooseIcon.setImageResource(R.drawable.down_arrows);
			} else {
				networkLayout.setVisibility(View.VISIBLE);
				networkChooseIcon.setImageResource(R.drawable.up_arrows);
			}
			break;
		case R.id.add_circle:
			showdata(true);
			break;
		case R.id.user_portrait:
			Intent intent0 = new Intent(this, MainSelectServiceActivity.class);
			intent0.putExtra("select", UserInfo.getSelect());
			startActivityForResult(intent0, TAGUtil.tag2);
			activityAnimationUp();
			break;
		case R.id.refreshed_rl:
			manualRefresh = true;
			Request(TAGUtil.QUICK_CHECK);
			refreshedImage.startAnimation(rotate);
			break;
		case R.id.settings:
			Intent intent = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivityForResult(intent, 1001);
			activityAnimationOpen();
			break;
		case R.id.current_spent_rela:
			if (spendLayout.isShown()) {
				spendLayout.setVisibility(View.GONE);
				spendChoose.setImageResource(R.drawable.down_arrows);
			} else {
				spendLayout.setVisibility(View.VISIBLE);
				spendChoose.setImageResource(R.drawable.up_arrows);
			}
			break;
		case R.id.all_current_spent_rela:
			// if (allspendLayout.isShown()) {
			// allspendLayout.setVisibility(View.GONE);
			// allspendChoose.setImageResource(R.drawable.down_arrows);
			// } else {
			// allspendLayout.setVisibility(View.VISIBLE);
			// allspendChoose.setImageResource(R.drawable.up_arrows);
			// }
			break;
		case R.id.add_gb:
			if (!PreferenceUtils.getBoolean(this, "haveDiy")) {
				mydialog(3);
				return;
			}
			Intent intent1 = new Intent(this, MainAddDataActivity.class);
			intent1.putExtra("add", 2);
			intent1.putExtra("type", select);
			startActivityForResult(intent1, TAGUtil.tag5);
			activityAnimationUp();
			break;
		case R.id.add_more:
			if (!PreferenceUtils.getBoolean(this, "haveDiy")) {
				mydialog(4);
				return;
			}
			Intent intent2 = new Intent(this, MainAddDataActivity.class);
			intent2.putExtra("type", select);
			startActivityForResult(intent2, TAGUtil.tag5);
			activityAnimationUp();
			break;
		case R.id.sign_in_rela:
			Intent intent4 = new Intent(this, SignMainActivity.class);
			startActivityForResult(intent4, TAGUtil.tag9);
			activityAnimationOpen();
			break;
		case R.id.capture:
			Intent intent10 = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent10, TAGUtil.tag10);
			activityAnimationOpen();
			break;
		case R.id.search:
			Intent intent11 = new Intent(this, SearchMainActivity.class);
			startActivityForResult(intent11, TAGUtil.tag11);
			activityAnimationUp();
			break;
		case R.id.for_tip_three:
			Intent intent3 = new Intent(this, RechargeActivity.class);
			startActivityForResult(intent3, TAGUtil.tag7);
			activityAnimationUp();
			break;
		case R.id.log_out:
			DrawerLayout.closeDrawers();
			logout_dialog.show();
			break;

		case R.id.current_spend:
			Intent intent5 = new Intent(this, BillsActivity.class);
			startActivity(intent5);
			activityAnimationOpen();
			break;
		case R.id.notifications:
			Intent intent6 = new Intent(this, NotificationActivity.class);
			startActivity(intent6);
			activityAnimationOpen();
			break;
		case R.id.version:
			Intent intent7 = new Intent(this, SettingsVersionActivity.class);
			startActivity(intent7);
			activityAnimationOpen();
			break;
		case R.id.products_text:
			if (!PreferenceUtils.getBoolean(this, "haveDiy")) {
				mydialog(2);
				return;
			}
			Intent intent9 = new Intent(this, ProductsServiceActivity.class);

			// if (quickcheckinfo != null) {
			// List<OfferInstValue> offerInstValues = quickcheckinfo
			// .getOfferInstList();
			// for (OfferInstValue offerInstValue : offerInstValues) {
			// if (offerInstValue.getOfferType().equals("D")) {
			// float price = offerInstValue.getPeriodicFee()
			// .getCurrencyValue() / 10000;
			// DecimalFormat decimalFormat = new DecimalFormat(".00");//
			// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			// String p = decimalFormat.format(price);// format
			// HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("offerName", offerInstValue.getOfferName());
			// map.put("offerPrice", p);
			// intent9.putExtra("dataMap", map);
			// break;
			// }
			// }
			// }
			if (PreferenceUtils.getBoolean(this, "haveDiy")) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				double money = 0;
				int unit = Integer.valueOf(PreferenceUtils.getString2(this,
						"moneyUnit"));
				money = money + PreferenceUtils.getLong(this, "money", 01)
						* Math.pow(10, -unit);
				map.put("offerName", Double.toString(money));
				String datava = PreferenceUtils.getString(this, "C_DATA_LEVEL");
				if ("100000".equals(datava)) {
					map.put("offerName", "Red 300MB Bundle");
				} else if ("100001".equals(datava)) {
					map.put("offerName", "Red 500MB Bundle");
				} else if ("100002".equals(datava)) {
					map.put("offerName", "Red 800MB Bundle");
				}

				map.put("offerPrice", Double.toString(money));
				intent9.putExtra("dataMap", map);
			}
			startActivity(intent9);

			break;
		case R.id.offer_extras:
			Intent intent8 = new Intent(this,
					SettingsOffersAndExtrarsActivity.class);
			startActivity(intent8);
			activityAnimationOpen();
			break;
		case R.id.support_text:
		case R.id.ll_support:
			Intent supportIntent = new Intent(this, SupportActivity.class);
			String num = messageNumTv.getText().toString();
			if (!StringUtils.isEmpty(num) && StringUtils.isNumber(num)) {
				supportIntent.putExtra("messageNum", Integer.parseInt(num));
				messageNumTv.setText("");
				messageNumTv.setVisibility(View.GONE);
			}
			startActivity(supportIntent);
			activityAnimationUp();
			break;
		case R.id.itemised_spend:
			Intent intent13 = new Intent(this, ItemisedBillActivity.class);
			startActivity(intent13);
			activityAnimationOpen();
			break;
		case R.id.billing_history:
			Intent intent12 = new Intent(this, BillsActivity.class);
			startActivity(intent12);
			activityAnimationOpen();
			break;
		case R.id.speak_select:
			if (select == 0)
				return;
			if (select == 2) {
				addCircle.setVisibility(View.VISIBLE);
				addPlus.setVisibility(View.VISIBLE);
				addCircle.startAnimation(alout);
				addPlus.startAnimation(alout);
			}
			speakSelect.setImageResource(R.drawable.speak_select2);
			callSelect.setImageResource(R.drawable.call_select);
			smsSelect.setImageResource(R.drawable.sms_select);
			addGb.setText("1GB");
			select = 0;
			initBar();
			break;
		case R.id.call_select:
			if (select == 1)
				return;
			if (select == 2) {
				addCircle.setVisibility(View.VISIBLE);
				addPlus.setVisibility(View.VISIBLE);
				addCircle.startAnimation(alout);
				addPlus.startAnimation(alout);
			}
			speakSelect.setImageResource(R.drawable.speak_select);
			callSelect.setImageResource(R.drawable.call_select2);
			smsSelect.setImageResource(R.drawable.sms_select);
			addGb.setText("500Min");
			select = 1;
			initBar();
			break;
		case R.id.sms_select:
			if (select != 2) {
				addPlus.clearAnimation();
				addCircle.setVisibility(View.GONE);
				addPlus.setVisibility(View.GONE);
				addGb.setVisibility(View.GONE);
				addMore.setVisibility(View.GONE);
				select = 2;
				initBar();
			}
			speakSelect.setImageResource(R.drawable.speak_select);
			callSelect.setImageResource(R.drawable.call_select);
			smsSelect.setImageResource(R.drawable.sms_select2);
			break;
		case R.id.usage_history:
			if (!PreferenceUtils.getBoolean(this, "haveDiy")) {
				mydialog(5);
				return;
			}
			Intent intent14 = new Intent(this, NetWorkUsageActivity.class);
			startActivity(intent14);
			activityAnimationOpen();
			break;

		default:
			break;
		}
	}

	private void showdata(boolean isclose) {
		if (addGb.isShown()) {
			if (isclose) {
				addGb.setVisibility(View.GONE);
				addMore.setVisibility(View.GONE);
				addGb.startAnimation(alphadelaye);
				addMore.startAnimation(alpha);
				addPlus.startAnimation(rotatein);
			}
		} else {
			addGb.setVisibility(View.VISIBLE);
			addMore.setVisibility(View.VISIBLE);
			addGb.startAnimation(alout);
			addMore.startAnimation(aloutdelaye);
			addPlus.startAnimation(rotateout);
		}
	}

	private void addAds() {
		dots = new ArrayList<View>();
		dots.clear();
		dotsLayout.removeAllViews();

		for (int i = 0; i < dialoglist.get(UserInfo.getSelect()).size(); i++) {
			ImageView dotsView = new ImageView(getBaseContext());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 0, 5, 5);
			dotsView.setLayoutParams(params);
			dotsView.setBackgroundResource(R.drawable.banner);
			if (0 == i) {
				dotsView.setBackgroundResource(R.drawable.banner_icon);
			}
			dotsLayout.addView(dotsView);
			dots.add(dotsView);
		}
		if (newsAdapter == null) {
			newsAdapter = new MyPagerAdapter(MainActivity.this,
					dialoglist.get(UserInfo.getSelect()), newsPager);
		} else {
			newsAdapter.setList(dialoglist.get(UserInfo.getSelect()));
		}
		newsPager.setAdapter(newsAdapter);
		newsAdapter.setListener(click);
		newsPager.setOnPageChangeListener(new MyPageChangeListener());
		newsPager.setCurrentItem(dialoglist.size() - 1);
	}

	/**
	 * 功能引导pop
	 */
	private void showPop() {
		if (mainPop == null) {
			mainPop = View.inflate(MainActivity.this, R.layout.activity_tip,
					null);
			mainPop.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					return true;
				}
			});
			Button btnNext = (Button) mainPop.findViewById(R.id.ntxt_tip);
			Button close = (Button) mainPop.findViewById(R.id.close);
			final TextView tv_tip_title = (TextView) mainPop
					.findViewById(R.id.tv_tip_title);
			final RelativeLayout linea_dialog = (RelativeLayout) mainPop
					.findViewById(R.id.dialog_one);
			final LinearLayout tip_one_linear = (LinearLayout) mainPop
					.findViewById(R.id.tip_one_linear);
			final LinearLayout tip_two_image = (LinearLayout) mainPop
					.findViewById(R.id.image_linear);
			final LinearLayout tip_two_linear = (LinearLayout) mainPop
					.findViewById(R.id.tip_two_linear);
			final RelativeLayout tip_three_circle = (RelativeLayout) mainPop
					.findViewById(R.id.prog_frame1);
			final LinearLayout tip_three_linear = (LinearLayout) mainPop
					.findViewById(R.id.tip_three_linear);
			final LinearLayout tip_four_linear = (LinearLayout) mainPop
					.findViewById(R.id.tip_four_linear);
			final RelativeLayout tip_four_item = (RelativeLayout) mainPop
					.findViewById(R.id.tip_four_item);
			Button tip_two_next = (Button) mainPop
					.findViewById(R.id.tip_two_next);
			Button tip_two_close = (Button) mainPop
					.findViewById(R.id.tip_two_close);
			Button tip_three_next = (Button) mainPop
					.findViewById(R.id.tip_three_next);
			Button tip_three_close = (Button) mainPop
					.findViewById(R.id.tip_three_close);
			Button tip_four_start = (Button) mainPop
					.findViewById(R.id.tip_four_start);

			// ------------tip four onClick start-----------------------------

			tip_four_start.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currentSpentRela.setVisibility(View.VISIBLE);
					fl.removeView(mainPop);
				}
			});

			// ------------tip three onClick start-----------------------------

			tip_three_next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tv_tip_title.setText(R.string.tip_two_oneee);
					for_tip_three.setVisibility(View.VISIBLE);
					circleFrameLayout.setVisibility(View.VISIBLE);
					speakSelect.setVisibility(View.VISIBLE);
					callSelect.setVisibility(View.VISIBLE);
					smsSelect.setVisibility(View.VISIBLE);
					ll_support.setVisibility(View.VISIBLE);
					tip_four_linear.setVisibility(View.VISIBLE);
					tip_four_item.setVisibility(View.VISIBLE);
					tip_three_circle.setVisibility(View.GONE);
					tip_three_linear.setVisibility(View.GONE);
					currentSpentRela.setVisibility(View.INVISIBLE);
					userPortrait.setVisibility(View.VISIBLE);
					mGoneImage.setVisibility(View.VISIBLE);
				}
			});
			tip_three_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for_tip_three.setVisibility(View.VISIBLE);
					circleFrameLayout.setVisibility(View.VISIBLE);
					speakSelect.setVisibility(View.VISIBLE);
					callSelect.setVisibility(View.VISIBLE);
					smsSelect.setVisibility(View.VISIBLE);
					ll_support.setVisibility(View.VISIBLE);
					userPortrait.setVisibility(View.VISIBLE);
					mGoneImage.setVisibility(View.VISIBLE);
					fl.removeView(mainPop);
				}
			});

			// ------------tip two onClick start-----------------------------
			tip_two_next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tv_tip_title.setText(R.string.tip_two_onee);
					circleFrameLayout.setVisibility(View.GONE);
					for_tip_three.setVisibility(View.GONE);
					speakSelect.setVisibility(View.GONE);
					callSelect.setVisibility(View.GONE);
					smsSelect.setVisibility(View.GONE);
					ll_support.setVisibility(View.GONE);
					tip_two_image.setVisibility(View.GONE);
					tip_two_linear.setVisibility(View.GONE);
					tip_three_circle.setVisibility(View.VISIBLE);
					tip_three_linear.setVisibility(View.VISIBLE);

				}
			});
			tip_two_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mGoneImage.setVisibility(View.VISIBLE);
					for_tip_three.setVisibility(View.VISIBLE);
					circleFrameLayout.setVisibility(View.VISIBLE);
					userPortrait.setVisibility(View.VISIBLE);

					fl.removeView(mainPop);
				}
			});
			// ------tip one onClick start-----------------------------
			btnNext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tv_tip_title.setText(R.string.tip_two_one);
					mGoneImage.setVisibility(View.GONE);
					userPortrait.setVisibility(View.INVISIBLE);
					tip_one_linear.setVisibility(View.GONE);
					linea_dialog.setVisibility(View.GONE);
					tip_two_image.setVisibility(View.VISIBLE);
					tip_two_linear.setVisibility(View.VISIBLE);
					newsPager.setVisibility(View.VISIBLE);
					scrollView.setScrollY(getResources().getDimensionPixelSize(
							R.dimen.view_scrollx));
				}
			});
			close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					newsPager.setVisibility(View.VISIBLE);
					fl.removeView(mainPop);
				}
			});
			// -----------tip one onClick start-----------------------------
			fl.addView(mainPop);
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		@Override
		public void onPageSelected(int position) {
			dots.get(oldPosition).setBackgroundResource(R.drawable.banner);
			dots.get(position).setBackgroundResource(R.drawable.banner_icon);
			oldPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAGUtil.tag9) {

			String text = StringUtils
					.formatDecimalFloat(PreferenceUtils.getInt(
							getBaseContext(), "SignInPoints"), 0)
					+ " Points";
			if (!pointsNum.getText().equals(text)) {
				pointsNum.setText(text);
				Request(TAGUtil.QUICK_CHECK);
			}
		} else if (requestCode == TAGUtil.tag11) {
			if (PreferenceUtils.getBoolean(getBaseContext(), "BugDataOrUnit")) {
				Request(TAGUtil.QUICK_CHECK);
				PreferenceUtils.setBoolean(getBaseContext(), "BugDataOrUnit",
						false);
			}
		} else if (requestCode == TAGUtil.tag5) {
			if (resultCode == TAGUtil.tag3) {
				SuccessDialog dialog = new SuccessDialog(this);
				dialog.settext(data.getStringExtra("message"));
				dialog.show();
				Request(TAGUtil.QUICK_CHECK);
				int type = data.getIntExtra("type", -1);
				int data1 = data.getIntExtra("data", -1);
				String text = null;
				float getadd = -1;
				if (type == 0) {
					if (data1 == 1) {
						text = "You've now got an extra <b>500M</b> of data to keep you going.";
					} else if (data1 == 2) {
						text = "You've now got an extra <b>1G</b> of data to keep you going.";
					} else {
						text = "You've now got an extra <b>10G</b> of data to keep you going.";
					}
					getadd = Float.valueOf(UserInfo.getAddData().replace(",",
							""));
				} else if (type == 1) {
					text = "You've now got an extra <b>500Min</b> of calls to keep you going.";
					getadd = Float.valueOf(UserInfo.getAddUnit().replace(",",
							""));
				} else if (type == 2) {
					text = "You've now got an extra <b>5GB</b> of data to keep you going.";
					getadd = Float.valueOf(UserInfo.getAddData().replace(",",
							""));
				}
				float all = Float.valueOf(UserInfo.getAllData()
						.replace(",", ""));
				if (all != 0 && getadd == 0) {
					showadddata = true;
				}
				if (text != null) {
					addnotification("Data Add-on", text);
				}
			} else {
				if (!PreferenceUtils.getString(getBaseContext(), "Addbalance")
						.equals("0")) {
					Request(TAGUtil.QUICK_CHECK);
				}
			}
		}

		if (resultCode == TAGUtil.tag1) {
			changeUser(false, data.getIntExtra("select", 0));
			Request(TAGUtil.QUICK_CHECK);
		} else if (resultCode == TAGUtil.tag8) {
			SuccessDialog dialog = new SuccessDialog(this);
			dialog.settext("Your payment was successful");
			dialog.show();
			Request(TAGUtil.QUICK_CHECK);
		}
		if (resultCode == 1001) {
			userName.setText(UserInfo.getUserName() + "'s");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean exiteFlag = false;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// 监听物理返回键
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			if (!exiteFlag) {
				exiteFlag = true;
				Toast.makeText(MainActivity.this,
						R.string.again_press_exite_str, Toast.LENGTH_LONG)
						.show();
				timeHandler.sendEmptyMessageDelayed(0, 3 * 1000);
			} else {
				// 退出软件
				System.exit(0);
			}
		}
		return true;
	}

	private final Handler timeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			exiteFlag = false;
			super.handleMessage(msg);
		}
	};

	@Override
	public void onclickYes() {
		Intent it = new Intent(MainActivity.this, LoginActivity.class);
		it.putExtra("code", 1);
		startActivity(it);
		logout_dialog.dismiss();
		finish();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.vodafone.set_pic_sucess");
		myIntentFilter.addAction("offer_diy_successed");
		// 注册广播
		MainActivity.this.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void unRegisterBoradcastReceiver() {
		MainActivity.this.unregisterReceiver(mBroadcastReceiver);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.vodafone.set_pic_sucess".equals(action)) {
				Bitmap bitmap = intent.getParcelableExtra("bitmap");
				userPortrait.setImageBitmap(bitmap);
			} else if ("offer_diy_successed".equals(action)) {
				Request(TAGUtil.QUICK_CHECK);
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegisterBoradcastReceiver();
		taskTimer.cancel();
	}

	private void Request(int num) {
		switch (num) {
		case TAGUtil.QUICK_CHECK:
			IRequest.get(num, URLs.QUICKCHECK, RequestJSon.ReferInfo(), this);
			break;
		case TAGUtil.DELETE:
			IRequest.post(
					TAGUtil.DELETE,
					URLs.REMOVEOPTIONAL,
					RequestJSon.removeOptional("10102",
							PreferenceUtils.getString(this, "OfferInstId")),
					this);
			break;
		case TAGUtil.GETUI:
			if (!StringUtils.isEmpty(PreferenceUtils.getString(
					getBaseContext(), "CID"))) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("mobile", UserInfo.getUserMobile());
				params.put("deviceToken",
						PreferenceUtils.getString(getBaseContext(), "CID"));
				params.put("fromType", 0);
				IRequest.get(TAGUtil.GETUI, URLs.GETUI, params, this);
			}
			break;
		}
	}

	@Override
	public void requestSuccess(Object tag, String json) {
		switch ((Integer) tag) {
		case TAGUtil.QUICK_CHECK:
			initLastData();// 记录上次数据
			// 清空DIY套餐 本地数据 蔡雨成
			clearData();
			refreshedImage.clearAnimation();
			refreshed.setText("Refreshed at "
					+ DateUtil.ConverToString(new Date(), "HH:mm"));
			if (JsonUtils.getHeadCode(json).equals("0")) {
				quickcheckinfo = JsonUtils.getBodyObject(json,
						QuickCheckInfo.class);
				if (quickcheckinfo == null) {
					setempty(false);
				} else {
					DealQuickCheck();
				}
			} else {
				setempty(false);
			}
			if (!PreferenceUtils.getString(getBaseContext(), "Addbalance")
					.equals("0")) {
				addnotification("TopUp", "");
				PreferenceUtils.setString(getBaseContext(), "Addbalance", "0");
			}
			refreshDIYdata();
			break;

		case TAGUtil.DELETE:
			if (JsonUtils.getHeadCode(json).equals("0")) {
				// ToastUtil.showToast(this, "退订成功");
				// 清空DIY套餐 本地数据 蔡雨成
				clearData();
				Request(TAGUtil.QUICK_CHECK);
			} else {
				// ToastUtil.showToast(this, "退订失败");
			}
			break;
		}
	}

	@Override
	public void requestError(Object tag, VolleyError e) {
		switch ((Integer) tag) {
		case TAGUtil.QUICK_CHECK:
			refreshedImage.clearAnimation();
			refreshed.setText("Refreshed at "
					+ DateUtil.ConverToString(new Date(), "HH:mm"));
			setempty(true);
			break;
		}

	}

	private void clearData() {
		// 清除所有存的数据 蔡雨成
		PreferenceUtils.delString(this, "haveDiy");
		PreferenceUtils.setLong(this, "money", 1l);
		PreferenceUtils.delString(this, "moneyUnit");
		PreferenceUtils.delString(this, "C_DATA_LEVEL");
		PreferenceUtils.delString(this, "C_UNIT_LEVEL");
		PreferenceUtils.delString(this, "OfferInstId");
		initdata();
	}

	private void setempty(boolean showdialog) {
		initdata();
		UserInfo.setBalance("0.00");
		balanceText.setText(UserInfo.getBalanceWithSign());
		initBar();
		if (showdialog) {
			mydialog(1);
		}
	}

	private void initdata() {
		UserInfo.setAllData("0");
		UserInfo.setLeftData("0");
		UserInfo.setAddData("0");
		UserInfo.setAllUnit("0");
		UserInfo.setLeftUnit("0");
		UserInfo.setAddUnit("0");
		UserInfo.setAddleftData("0");
		UserInfo.setDiydataall("0.00");
		UserInfo.setDiydataleft("0.00");
		UserInfo.setDiyunitall("0");
		UserInfo.setDiyunitleft("0");
	}

	private void DealQuickCheck() {
		UserInfo.setBalanceWithSign(UnitUtil.getBalance(quickcheckinfo
				.getBalanceInfoList()));
		balanceText.setText(UserInfo.getBalanceWithSign());
		double alldata = 0, leftdata = 0, adddata = 0, diydataall = 0, diydataleft = 0, addleftdata = 0;
		double allunit = 0, leftunit = 0, addunit = 0, diyunitall = 0, diyunitleft = 0;

		for (ConsumeAndQuota item : quickcheckinfo.getComsumeAndQuotaList()) {
			if (item.getId().contains("Data")) {
				alldata = alldata
						+ UnitUtil.getValue(item.getTotalValue(), "GB");
				leftdata = leftdata
						+ UnitUtil.getValue(item.getLeftValue(), "GB");
				if (item.getId().contains("C_Data")) {
					adddata = adddata
							+ UnitUtil.getValue(item.getTotalValue(), "GB");
					addleftdata = addleftdata
							+ UnitUtil.getValue(item.getLeftValue(), "GB");
				}
				if (item.getId().contains("DIY_Data")) {
					diydataall = diydataall
							+ UnitUtil.getValue(item.getTotalValue(), "GB");
					diydataleft = diydataleft
							+ UnitUtil.getValue(item.getLeftValue(), "GB");
				}
			}
			if (item.getId().contains("DIY_Units")) {
				allunit = allunit
						+ UnitUtil.getValue(item.getTotalValue(), "One");
				leftunit = leftunit
						+ UnitUtil.getValue(item.getLeftValue(), "One");
				diyunitall = diyunitall
						+ UnitUtil.getValue(item.getTotalValue(), "One");
				diyunitleft = diyunitleft
						+ UnitUtil.getValue(item.getLeftValue(), "One");
			}
			if (item.getId().contains("C_National_Voie")) {
				allunit = allunit
						+ UnitUtil.getValue(item.getTotalValue(), "Minute");
				leftunit = leftunit
						+ UnitUtil.getValue(item.getLeftValue(), "Minute");
				addunit = addunit
						+ UnitUtil.getValue(item.getTotalValue(), "Minute");
			}
		}
		veryChange(alldata, leftdata, allunit, leftunit);
		UserInfo.setAllData(StringUtils.formatDecimalFloat(alldata, 2));
		UserInfo.setLeftData(StringUtils.formatDecimalFloat(leftdata, 2));

		UserInfo.setAddData(StringUtils.formatDecimalFloat(adddata, 2));

		UserInfo.setAllUnit(StringUtils.formatDecimalFloat(allunit, 0));
		UserInfo.setLeftUnit(StringUtils.formatDecimalFloat(leftunit, 0));
		UserInfo.setAddUnit(StringUtils.formatDecimalFloat(addunit, 0));

		UserInfo.setDiydataall(StringUtils.formatDecimalFloat(diydataall, 2));
		UserInfo.setDiydataleft(StringUtils.formatDecimalFloat(diydataleft, 2));

		UserInfo.setAddleftData(StringUtils.formatDecimalFloat(addleftdata, 2));

		UserInfo.setDiyunitall(StringUtils.formatDecimalFloat(diyunitall, 0));
		UserInfo.setDiyunitleft(StringUtils.formatDecimalFloat(diyunitleft, 0));
		initBar();

		// 判断是否有DIY基础套餐 蔡雨成
		if (quickcheckinfo.getOfferInstList().size() != 0) {
			for (int i = 0; i < quickcheckinfo.getOfferInstList().size(); i++) {
				if (!StringUtils.isEmpty(quickcheckinfo.getOfferInstList()
						.get(i).getOfferId())) {
					if ("10102".equals(quickcheckinfo.getOfferInstList().get(i)
							.getOfferId())) {
						PreferenceUtils.setBoolean(this, "haveDiy", true);
						PreferenceUtils.setLong(this, "money", quickcheckinfo
								.getOfferInstList().get(i).getPeriodicFee()
								.getCurrencyValue());
						PreferenceUtils.setString(this, "effectiveDate",
								quickcheckinfo.getOfferInstList().get(i)
										.getEffectiveDate().getUtcDateTime());
						PreferenceUtils.setString(this, "expireDate",
								quickcheckinfo.getOfferInstList().get(i)
										.getExpireDate().getUtcDateTime());

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

	private void refreshDIYdata() {
		Intent it = new Intent("com.vodafone.refreshDIYData");
		// if (quickcheckinfo != null) {
		// List<OfferInstValue> offerInstValues = quickcheckinfo
		// .getOfferInstList();
		// for (OfferInstValue offerInstValue : offerInstValues) {
		// if (offerInstValue.getOfferType().equals("D")) {
		// float price = (float) (offerInstValue.getPeriodicFee()
		// .getCurrencyValue()) / 10000;
		// DecimalFormat decimalFormat = new DecimalFormat(".00");//
		// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		// String p = decimalFormat.format(price);// format
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("offerName", offerInstValue.getOfferName());
		// map.put("offerPrice", p);
		// it.putExtra("dataMap", map);
		// break;
		// }
		// }
		// }
		if (PreferenceUtils.getBoolean(this, "haveDiy")) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			double money = 0;
			int unit = Integer.valueOf(PreferenceUtils.getString2(this,
					"moneyUnit"));
			money = money + PreferenceUtils.getLong(this, "money", 01)
					* Math.pow(10, -unit);
			map.put("offerName", Double.toString(money));
			String datava = PreferenceUtils.getString(this, "C_DATA_LEVEL");
			if ("100000".equals(datava)) {
				map.put("offerName", "Red 300MB Bundle");
			} else if ("100001".equals(datava)) {
				map.put("offerName", "Red 500MB Bundle");
			} else if ("100002".equals(datava)) {
				map.put("offerName", "Red 800MB Bundle");

			}

			map.put("offerPrice", Double.toString(money));
			it.putExtra("dataMap", map);
		} else if (showdiynoti) {
			addnotification("DIYNotHave", "");
			showdiynoti = false;
		}
		sendBroadcast(it);
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_diy:
			Request(TAGUtil.DELETE);
		default:
			break;
		}
		return false;
	}

	private void mydialog(int num) {
		if (!proscenium) {
			return;
		}
		if (dialog == null) {
			dialog = new ErrorDialog(MainActivity.this);
			dialog.setClick(new Click() {

				@Override
				public void onClicked(Object num, int num1, String num2,
						boolean num3) {
					switch ((Integer) num) {
					case 1:
						Request(TAGUtil.QUICK_CHECK);
						break;
					case 2:
						if (num3) {
							Intent intent = new Intent(MainActivity.this,
									DiyPlansActivity.class);
							startActivity(intent);
							activityAnimationOpen();
						} else {
							Intent intent9 = new Intent(MainActivity.this,
									ProductsServiceActivity.class);

							if (PreferenceUtils.getBoolean(MainActivity.this,
									"haveDiy")) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								double money = 0;
								int unit = Integer.valueOf(PreferenceUtils
										.getString2(MainActivity.this,
												"moneyUnit"));
								money = money
										+ PreferenceUtils.getLong(
												MainActivity.this, "money", 01)
										* Math.pow(10, -unit);
								map.put("offerName", Double.toString(money));
								String datava = PreferenceUtils.getString(
										MainActivity.this, "C_DATA_LEVEL");
								if ("100000".equals(datava)) {
									map.put("offerName", "Red 300MB Bundle");
								} else if ("100001".equals(datava)) {
									map.put("offerName", "Red 500MB Bundle");
								} else if ("100002".equals(datava)) {
									map.put("offerName", "Red 800MB Bundle");
								}

								map.put("offerPrice", Double.toString(money));
								intent9.putExtra("dataMap", map);
							}
							startActivity(intent9);
						}
						break;
					case 3:
						if (num3) {
							Intent intent = new Intent(MainActivity.this,
									DiyPlansActivity.class);
							startActivity(intent);
							activityAnimationOpen();
						} else {
							Intent intent1 = new Intent(MainActivity.this,
									MainAddDataActivity.class);
							intent1.putExtra("add", 2);
							intent1.putExtra("type", select);
							startActivityForResult(intent1, TAGUtil.tag5);
							activityAnimationUp();
						}
						break;
					case 4:
						if (num3) {
							Intent intent = new Intent(MainActivity.this,
									DiyPlansActivity.class);
							startActivity(intent);
							activityAnimationOpen();
						} else {
							Intent intent2 = new Intent(MainActivity.this,
									MainAddDataActivity.class);
							intent2.putExtra("type", select);
							startActivityForResult(intent2, TAGUtil.tag5);
							activityAnimationUp();
						}
						break;
					case 5:
						if (num3) {
							Intent intent = new Intent(MainActivity.this,
									DiyPlansActivity.class);
							startActivity(intent);
							activityAnimationOpen();
						} else {
							Intent intent14 = new Intent(MainActivity.this,
									NetWorkUsageActivity.class);
							startActivity(intent14);
							activityAnimationOpen();
						}
						break;
					default:
						break;
					}
				}
			});
		}
		if (!dialog.isShowing()) {
			dialog.setTag(num);
			switch (num) {
			case 1:
				dialog.setrestore();
				break;
			case 2:
			case 3:
			case 4:
			case 5:
				dialog.setMessage(
						"Welcome to Smart Pricing, please configure your plan first.",
						"Plan Configuration", "Yes, please", "No, thanks");
				break;
			default:
				break;
			}
			dialog.show();
		}
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		jump(intent);
	}

	private void jump(Intent intent) {
		String channel = intent.getStringExtra("channel") + "";
		Intent resultIntent = null;
		switch (channel) {
		case "DataUsgNtfctnThrshld1":
			resultIntent = new Intent(getBaseContext(),
					MainAddDataActivity.class);
			resultIntent.putExtra("type", 0);
			startActivityForResult(resultIntent, TAGUtil.tag5);
			break;
		case "RchrgReminder":
			resultIntent = new Intent(getBaseContext(), RechargeActivity.class);
			startActivityForResult(resultIntent, TAGUtil.tag7);
			break;
		case "Content Bundle":
			resultIntent = new Intent(getBaseContext(),
					MainAddDataActivity.class);
			resultIntent.putExtra("type", 2);
			startActivityForResult(resultIntent, TAGUtil.tag5);
			break;
		case "UpSell Bundle":
			resultIntent = new Intent(getBaseContext(),
					MainAddDataActivity.class);
			resultIntent.putExtra("add", 2);
			resultIntent.putExtra("type", 0);
			startActivityForResult(resultIntent, TAGUtil.tag5);
			break;
		case "LowBalance":
			resultIntent = new Intent(getBaseContext(), RechargeActivity.class);
			resultIntent.putExtra("number", 10);
			startActivityForResult(resultIntent, TAGUtil.tag7);
			break;
		default:
			return;
		}
		addnotification(channel, intent.getStringExtra("content"));
	}

	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!MainActivity.this.isFinishing()
					&& MyApplication.getInstance().isNetworkConnected())
				taskHandler.sendEmptyMessage(1);
		}
	};

	private final Timer taskTimer = new Timer();
	Handler taskHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 要做的事情
			Request(TAGUtil.QUICK_CHECK);
			super.handleMessage(msg);

		}
	};
	private boolean isDataChange = true;

	private boolean isUnitChange = true;

	private String lastAllData;

	private String lastLeftData;

	private String lastAllUnit;

	private String lastLeftUnit;

	private void startTask() {// 定时任务
		// TODO Auto-generated method stub
		taskTimer.schedule(task, 120 * 1000, 60 * 1000);
	}

	private void initLastData() {
		lastAllData = UserInfo.getAllData();
		lastLeftData = UserInfo.getLeftData();
		lastAllUnit = UserInfo.getAllUnit();
		lastLeftUnit = UserInfo.getLeftUnit();
	}

	private void veryChange(double allData, double leftData, double allUnit,
			double leftUnit) {
		if (lastAllData != null
				&& lastAllData.equals(StringUtils
						.formatDecimalFloat(allData, 2))
				&& lastLeftData != null
				&& lastLeftData.equals(StringUtils.formatDecimalFloat(leftData,
						2))) {
			isDataChange = false;
		}
		if (lastAllUnit != null
				&& lastAllUnit.equals(StringUtils
						.formatDecimalFloat(allUnit, 2))
				&& lastLeftUnit != null
				&& lastLeftUnit.equals(StringUtils.formatDecimalFloat(leftUnit,
						2))) {
			isUnitChange = false;
		}
	}

	private boolean isNeedRefresh() {
		if (manualRefresh) {
			manualRefresh = false;
			return true;
		}
		if (StringUtils.isEmpty(surplusFlow.getText().toString()))
			return true;
		if (select == 0) {
			if (!isDataChange) {
				isDataChange = true;
				return false;
			}
		} else if (select == 1) {
			if (!isUnitChange) {
				isUnitChange = true;
				return false;
			}
		} else if (select == 2) {
			if (surplusFlow.getText().equals("Unlimited"))
				return false;
		}
		return true;
	}
}

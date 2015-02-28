package com.maomao.app.citybuy.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.activity.accounts.AccountingActivity;
import com.maomao.app.citybuy.adapter.HomeSlideViewPagerAdapter;
import com.maomao.app.citybuy.entity.Banner;
import com.maomao.app.citybuy.entity.UpdateInfo;
import com.maomao.app.citybuy.task.BaseTask;
import com.maomao.app.citybuy.task.HomeBannerTask;
import com.maomao.app.citybuy.task.UpdateVersionTask;
import com.maomao.app.citybuy.util.CommonUtil;
import com.maomao.app.citybuy.view.MyHorizontalScrollView;

/**
 * 首页
 * 
 * 2014-08-27
 * 
 * @author peng
 * 
 */
public class HomeActivity extends BaseActivity {

	private MyHorizontalScrollView horizontalScrollView;
	private ViewPager vpSlide;
	private List<ImageView> slideIndexImageViews = new ArrayList<ImageView>();
	private List<Banner> banners;
	private HomeBannerTask homeBannerTask;
	private UpdateVersionTask updateVersionTask;

	private static final int BACK_PRESSED_INTERVAL = 2000;
	private static final int SLIDE_PLAY_INTERVAL = 5000;
	private static final int MESSAGE_SLIDE_PLAY = 4;
	private int currentSlidePosition = 0;
	private long currentBackPressedTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		initView();
		getBannerData();
		checkVersion();
	}

	@Override
	protected void onDestroy() {
		if (homeBannerTask != null && !homeBannerTask.isCancelled())
			homeBannerTask.cancel(true);
		if (updateVersionTask != null && !updateVersionTask.isCancelled())
			updateVersionTask.cancel(true);
		super.onDestroy();
	}

	private void initView() {
		initTitleView("", false, true);
		tvTitle.setBackgroundResource(R.drawable.home_title);
		tvTitle.setTextSize(0);
		ibMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (horizontalScrollView.getTag() == null) {
					horizontalScrollView.setTag("1");
					horizontalScrollView.smoothScrollTo(
							CommonUtil.dip2px(HomeActivity.this, 220), 0);
				} else {
					horizontalScrollView.setTag(null);
					horizontalScrollView.smoothScrollTo(0, 0);
				}
			}
		});
		findViewById(R.id.ll_home_pictures).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						toPicturesActivity();
					}
				});
		findViewById(R.id.ll_home_experience).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						toExperienceActivity();
					}
				});
		findViewById(R.id.ll_home_company).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						toCompanyActivity();
					}
				});
		findViewById(R.id.ll_home_building_materiials).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						toBuildingMaterialsActivity();
					}
				});
		findViewById(R.id.ll_home_accounts).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						toAccountingActivity();
					}
				});
		findViewById(R.id.ll_home_consult).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						toConsultActivity();
					}
				});
		findViewById(R.id.ll_setting_feedback).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						toFeedbackActivity();
					}
				});
		findViewById(R.id.ll_setting_update).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showProgressDialog();
						checkVersion();
					}
				});
		findViewById(R.id.ll_setting_about).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						toAboutUsActivity();
					}
				});
		findViewById(R.id.ll_setting_clear).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showClearDialog();
					}
				});
		findViewById(R.id.ll_setting_exit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						horizontalScrollView.setTag(null);
						horizontalScrollView.smoothScrollTo(0, 0);
					}
				});
		initHorizontalScrollView();
	}

	private Integer getScreenWidth() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}

	private void initHorizontalScrollView() {
		findViewById(R.id.ll_home_content_wrapper).getLayoutParams().width = getScreenWidth();
		horizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.hsv);
	}

	private void initViewPager() {
		initSlideIndex(banners.size());
		vpSlide = (ViewPager) findViewById(R.id.vp_home_slide);
		vpSlide.setAdapter(new HomeSlideViewPagerAdapter(this, banners));
		vpSlide.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				slideIndexChanged(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initSlideIndex(final Integer count) {
		LinearLayout llSlideIndex = (LinearLayout) findViewById(R.id.ll_home_slide_index);
		LinearLayout.LayoutParams slideIndexImageViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		slideIndexImageViewParams.leftMargin = CommonUtil.dip2px(this, 5);
		for (int i = 0; i < count; i++) {
			ImageView slideIndexImageView = new ImageView(this);
			slideIndexImageView.setLayoutParams(slideIndexImageViewParams);
			if (i == 0) {
				slideIndexImageView
						.setImageResource(R.drawable.home_slide_index_select);
			} else {
				slideIndexImageView
						.setImageResource(R.drawable.home_slide_index_normal);
			}
			llSlideIndex.addView(slideIndexImageView);
			slideIndexImageViews.add(slideIndexImageView);
		}
	}

	private void slideIndexChanged(final Integer position) {
		for (ImageView slideIndexImageView : slideIndexImageViews) {
			slideIndexImageView
					.setImageResource(R.drawable.home_slide_index_normal);
		}
		slideIndexImageViews.get(position).setImageResource(
				R.drawable.home_slide_index_select);
	}

	private void toPicturesActivity() {
		startActivity(new Intent(this, PicturesActivity.class));
	}

	private void toExperienceActivity() {
		startActivity(new Intent(this, ExperienceActivity.class));
	}

	private void toCompanyActivity() {
		startActivity(new Intent(this, CompanyActivity.class));
	}

	private void toBuildingMaterialsActivity() {
		startActivity(new Intent(this, BuildingMaterialsActivity.class));
	}

	private void toAccountingActivity() {
		startActivity(new Intent(this, AccountingActivity.class));
	}

	private void toConsultActivity() {
		startActivity(new Intent(this, ConsultActivity.class));
	}

	private void toFeedbackActivity() {
		startActivity(new Intent(this, FeedbackActivity.class));
	}

	private void toAboutUsActivity() {
		startActivity(new Intent(this, AboutUsActivity.class));
	}

	private void showClearDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.setting_clear_dialog_title);
		builder.setMessage(R.string.setting_clear_dialog_message);
		builder.setPositiveButton(R.string.common_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						toast(R.string.setting_clear_dialog_success);
					}
				});
		builder.setNegativeButton(R.string.common_cancel, null);
		builder.show();
	}

	private void showUpdateVersionDialog(final UpdateInfo updateInfo) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("发现新版本");
		builder.setMessage(updateInfo.getUpdateInfo());
		builder.setPositiveButton(R.string.common_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						toDownloadNewVersion(updateInfo.getDownloadUrl());
					}
				});
		builder.setNegativeButton(R.string.common_cancel, null);
		builder.show();
	}

	private void toDownloadNewVersion(final String url) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}

	private void getBannerData() {
		homeBannerTask = new HomeBannerTask(this, handler);
		homeBannerTask.execute();
	}

	private void checkVersion() {
		updateVersionTask = new UpdateVersionTask(this, handler);
		updateVersionTask.execute();
	}

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
			currentBackPressedTime = System.currentTimeMillis();
			toast("再按一次返回键退出程序");
		} else {
			finish();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isFinishing())
				return;
			closeProgressDialog();
			switch (msg.what) {
			case BaseTask.RESPONSE_ERROR:
				toast(msg.obj.toString());
				break;
			case HomeBannerTask.RESPONSE_OK:
				banners = (List<Banner>) msg.obj;
				initViewPager();
				handler.sendEmptyMessageDelayed(MESSAGE_SLIDE_PLAY,
						SLIDE_PLAY_INTERVAL);
				break;
			case UpdateVersionTask.RESPONSE_OK:
				UpdateInfo updateInfo = (UpdateInfo) msg.obj;
				if (updateInfo != null)
					showUpdateVersionDialog(updateInfo);
				break;
			case MESSAGE_SLIDE_PLAY:
				currentSlidePosition++;
				if (currentSlidePosition >= banners.size())
					currentSlidePosition = 0;
				vpSlide.setCurrentItem(currentSlidePosition, true);
				handler.sendEmptyMessageDelayed(MESSAGE_SLIDE_PLAY,
						SLIDE_PLAY_INTERVAL);
				break;
			}
		}
	};

}

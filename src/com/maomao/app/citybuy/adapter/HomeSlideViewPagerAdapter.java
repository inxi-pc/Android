package com.maomao.app.citybuy.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.maomao.app.citybuy.entity.Banner;
import com.maomao.app.citybuy.util.DecorateApplication;

public class HomeSlideViewPagerAdapter extends PagerAdapter implements
		OnClickListener {

	private Context context;
	private List<Banner> banners;
	private List<View> views;

	public HomeSlideViewPagerAdapter(final Context context,
			final List<Banner> banners) {
		this.context = context;
		this.banners = banners;
		this.views = new ArrayList<View>();
	}

	@Override
	public int getCount() {
		return banners.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(context);
		Banner banner = banners.get(position);
		imageView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setTag(banner.getHref());
		imageView.setOnClickListener(this);

		DecorateApplication.getImageLoader().displayImage(
				banner.getImagePath(), imageView,
				DecorateApplication.getDisplayImageOptions());

		views.add(imageView);
		container.addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null && !TextUtils.isEmpty(v.getTag().toString())
				&& !v.getTag().toString().equals("#"))
			openActionView(v.getTag().toString());
	}

	private void openActionView(final String url) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}

}

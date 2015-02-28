package com.maomao.app.citybuy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.maomao.app.citybuy.R;
import com.maomao.app.citybuy.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 父类活动
 * 
 * 2014-08-27
 * 
 * @author peng
 * 
 */
public class BaseActivity extends Activity {

	protected TextView tvTitle;
	protected ImageButton ibBack;
	protected ImageButton ibMore;
	protected WebView webView;
	protected ProgressDialog progressDialog;

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	protected void initWebView(final String url) {
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				LogUtil.D("webview onPageFinished");
				findViewById(R.id.loading).setVisibility(View.GONE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				LogUtil.D("webview onReceivedError");
				findViewById(R.id.loading).setVisibility(View.GONE);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		LogUtil.D("webview loading url..." + url);
		webView.loadUrl(url);
	}

	@Override
	public void onBackPressed() {
		if (webView != null && webView.canGoBack())
			webView.goBack();
		else
			super.onBackPressed();
	}

	protected void initTitleView(final Integer titleId,
			final Boolean isShowBack, final Boolean isShowMore) {
		initTitleView(getString(titleId), isShowBack, isShowMore);
	}

	protected void initTitleView(final String title, final Boolean isShowBack,
			final Boolean isShowMore) {
		ibBack = (ImageButton) findViewById(R.id.ib_title_back);
		ibMore = (ImageButton) findViewById(R.id.ib_title_more);
		tvTitle = (TextView) findViewById(R.id.tv_title_text);
		tvTitle.setText(title);
		if (isShowBack) {
			ibBack.setVisibility(View.VISIBLE);
			ibBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		} else {
			ibBack.setVisibility(View.GONE);
		}
		if (isShowMore) {
			ibMore.setVisibility(View.VISIBLE);
		} else {
			ibMore.setVisibility(View.GONE);
		}
	}

	protected void toast(final Integer msgId) {
		toast(getString(msgId));
	}

	protected void toast(final String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	protected void loading() {
		findViewById(R.id.loading).setVisibility(View.VISIBLE);
		findViewById(R.id.content).setVisibility(View.GONE);
	}

	protected void loaded() {
		findViewById(R.id.loading).setVisibility(View.GONE);
		findViewById(R.id.content).setVisibility(View.VISIBLE);
	}

	protected void showProgressDialog() {
		if (progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("处理中，请稍候");
			progressDialog.show();
		}
	}

	protected void closeProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}

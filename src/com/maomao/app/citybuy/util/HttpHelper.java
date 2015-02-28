package com.maomao.app.citybuy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class HttpHelper {

	public static String doGet(final String uri, final Map<String, String> args)
			throws Exception {
		HttpGet httpGet = new HttpGet(uri);
		if (args != null && !args.isEmpty()) {
			HttpParams httpParams = new BasicHttpParams();
			for (Map.Entry<String, String> entry : args.entrySet()) {
				httpParams.setParameter(entry.getKey(), entry.getValue());
			}
			httpGet.setParams(httpParams);
		}
		return doHttp(httpGet);
	}

	public static String doPost(final String uri, final Map<String, String> args)
			throws Exception {
		HttpPost httpPost = new HttpPost(uri);
		if (args != null && !args.isEmpty()) {
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : args.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			HttpEntity httpEntity = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
		}
		return doHttp(httpPost);
	}

	private static String doHttp(final HttpUriRequest request) throws Exception {
		AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");
		HttpResponse response = androidHttpClient.execute(request);
		return EntityUtils.toString(response.getEntity());
	}

}

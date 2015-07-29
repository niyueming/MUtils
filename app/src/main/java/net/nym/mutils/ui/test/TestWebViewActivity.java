/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.mutils.ui.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import net.nym.library.cookie.PersistentCookieStore;
import net.nym.library.util.ContextUtils;
import net.nym.library.util.StringUtils;
import net.nym.library.webservice.MethodNames;
import net.nym.library.webview.JSCall;
import net.nym.library.webview.MWebChromeClient;
import net.nym.library.webview.MWebViewClient;
import net.nym.mutils.R;

import org.apache.http.cookie.Cookie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


//	调用js方法：mWebView.loadUrl("javascript:myfunction()");
public class TestWebViewActivity extends ActionBarActivity {

    WebView mWebView;
    private ValueCallback<Uri> mUploadMessage;	//webView上传文件
    private static final int FILE_CHOOSER_RESULT_CODE = 1;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_test_web_view);
//        url = "http://wsq.qq.com/reflow/255443386";
        url = "http://www.52kids.com.cn/school/share/feedback";
        mWebView = (WebView) findViewById(R.id.webView);
        initWebView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initWebView() {
        mWebView.canGoBackOrForward(10);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            mWebView.getSettings().setAllowContentAccess(true);
        }
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        /**
         * Sets whether the WebView loads pages in overview mode, that is,
         * zooms out the content to fit on screen by width. This setting is
         * taken into account when the content width is greater than the width
         * of the WebView control, for example, when {@link #getUseWideViewPort}
         * is enabled. The default is false.
         */
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //启用数据库
        mWebView.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //设置数据库路径
        mWebView.getSettings().setDatabasePath(dir);
        //启用地理定位
        // <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        // <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        mWebView.getSettings().setGeolocationEnabled(true);		//(定位)
        //设置定位的数据库路径
        mWebView.getSettings().setGeolocationDatabasePath(dir);	//(定位)
        mWebView.getSettings().setDomStorageEnabled(true);		//支持Dom
        // mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        // mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setWebChromeClient(new MWebChromeClient(new MWebChromeClient.UploadFileInvoke(this,FILE_CHOOSER_RESULT_CODE){
            @Override
            public void onCallBack(ValueCallback<Uri> uploadFile) {
                mUploadMessage = uploadFile;
            }
        }));
        mWebView.setWebViewClient(new MWebViewClient() );

        mWebView.addJavascriptInterface(new JSCall(this), "Game");
        loadUrl();
    }

    private void loadUrl() {
        mWebView.clearHistory();
        mWebView.loadUrl(url);
    }

    /**
     * 同步一下cookie
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void synCookies(Context context, String url) {
        // 转换成URI
        URL uri = null;
        try {
            uri = new URL(url);
        } catch (MalformedURLException e) {
            try {
                // 默认用官网地址
                uri = new URL(MethodNames.HOST_ADDRESS);
            } catch (MalformedURLException e1) {
                return;
            }
        }

        CookieManager cookieManager = CookieManager.getInstance();
        // cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        if (null != cookieStore) {
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie c : cookies) {
                if (!StringUtils.isNullOrEmpty(c.getName()) && !StringUtils.isNullOrEmpty(c.getValue())) {
                    // cookies是在HttpClient中获得的cookie
                    StringBuilder cookie = new StringBuilder(c.getName() + "=" + c.getValue());
                    cookieManager.setCookie(uri.getHost(), cookie.toString());//
                }
            }
        }
        if(!ContextUtils.isLollipopOrLater()){

            CookieSyncManager.getInstance().sync();
        }else {
            cookieManager.flush();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE)
        {
            //webView上传文件
            if(mUploadMessage != null)
            {
                Uri result = data == null ? null : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
            return;
        }
        mWebView.stopLoading();
        mWebView.clearCache(true);
        mWebView.destroy();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

/*
Copyright (c) 2017-2025 Divested Computing Group

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package app.udderance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

public class MainActivity extends Activity {

    private WebView mWebView = null;
    private WebSettings mWebSettings = null;
    private CookieManager mCookieManager = null;
    private static final String TAG = "UdderanceAAC";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setTheme(android.R.style.Theme_DeviceDefault_DayNight);
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the WebView
        mWebView = findViewById(R.id.mWebView);

        //Allow local asset loading
        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        mWebView.setWebViewClient(new WebViewClientCompat() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.d(TAG, "Trying to load " + request.getUrl());
                if (request.getUrl().toString().startsWith("https://appassets.androidplatform.net/assets/")) {
                    String baseUrl = "https://appassets.androidplatform.net/assets/";
                    String path = request.getUrl().toString().split("https://appassets.androidplatform.net/assets/")[1];
                    String joiner = "";
                    if (!path.contains("images") && !path.contains("pages")) {
                        joiner = "assets/";
                    }
                    String newUrl = baseUrl + joiner + path;
                    Log.d(TAG, "rewriting to " + newUrl);
                    return assetLoader.shouldInterceptRequest(Uri.parse(newUrl));
                }
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });

        //Set cookie options
        mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(false);
        mCookieManager.setAcceptThirdPartyCookies(mWebView, false);

        //Set more options
        mWebSettings = mWebView.getSettings();
        //Enable some WebView features
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setGeolocationEnabled(false);
        //Disable some WebView features
        mWebSettings.setAllowFileAccessFromFileURLs(false);
        mWebSettings.setAllowUniversalAccessFromFileURLs(false);
        mWebSettings.setAllowContentAccess(false);
        mWebSettings.setAllowFileAccess(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setDatabaseEnabled(false);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setSaveFormData(false);
        //Load the AAC
        mWebView.loadUrl("https://appassets.androidplatform.net/assets/pages/aac.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

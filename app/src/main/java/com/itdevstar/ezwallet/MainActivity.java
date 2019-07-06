package com.itdevstar.ezwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView m_wvMain;
    JavaScriptInterface JSInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_wvMain = (WebView) findViewById(R.id.wv_main);

      /*  m_wvMain = (WebView) findViewById(R.id.wv_main);
        m_wvMain.getSettings().setJavaScriptEnabled(true);
        m_wvMain.getSettings().setAppCacheEnabled(true);
        m_wvMain.getSettings().setDomStorageEnabled(true);

        m_wvMain.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            m_wvMain.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            m_wvMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        String myURL = "https://111.datatrium.com";

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        WebView webview = new WebView(this);
        WebSettings ws = webview.getSettings();
        ws.setSaveFormData(true);
        ws.setSavePassword(true); // Not needed for API level 18 or greater (deprecated)


        m_wvMain.loadUrl(myURL);
        m_wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               // view.loadUrl(url);
               // return true;
                return false  ;
            }
        });*/

       /* m_wvMain.requestFocus();

        m_wvMain.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            m_wvMain.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            m_wvMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        m_wvMain.getSettings().setJavaScriptEnabled(true);
        m_wvMain.getSettings().setDomStorageEnabled(true);
        m_wvMain.getSettings().setSavePassword(true);
        m_wvMain.getSettings().setJavaScriptEnabled(true);
        m_wvMain.getSettings().setAppCacheEnabled(true);
        m_wvMain.getSettings().setSaveFormData(true);

        m_wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        //m_wvMain.loadUrl("https://111.datatrium.com/fmi/webd");

        m_wvMain.loadUrl("https://111.datatrium.com");*/

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        m_wvMain = findViewById(R.id.wv_main);
        m_wvMain.requestFocus();
        m_wvMain.getSettings().setJavaScriptEnabled(true);
        m_wvMain.getSettings().setSavePassword(false);
        m_wvMain.getSettings().setSaveFormData(false);

        JSInterface = new JavaScriptInterface(this);
        m_wvMain.addJavascriptInterface(JSInterface, "JSInterface");

        m_wvMain.loadUrl("https://111.datatrium.com");
        m_wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        m_wvMain.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public class JavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @android.webkit.JavascriptInterface
        public void showToast(String message){
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }

        @android.webkit.JavascriptInterface
        public void SavePreferences(String key, String value){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }

        @android.webkit.JavascriptInterface
        public String load_prefs(String key){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sharedPreferences.getString(key, "");
        }
    }
}

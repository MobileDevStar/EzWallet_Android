package com.itdevstar.ezwallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private final String                        SAVE_PASSWORD_KEY = "save_password";
    private final String                        USERNAME_KEY = "username";
    private final String                        PASSWORD_KEY = "password";

    private final int                           SAVE_PASSWORD_STATE_NOTNOW = 0;
    private final int                           SAVE_PASSWORD_STATE_NEVER = 1;
    private final int                           SAVE_PASSWORD_STATE_SAVED = 2;

    private JavaScriptInterface                 JSInterface;

    private WebView                             m_wvMain;
    private String                              m_strCurUserName;
    private String                              m_strCurPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_wvMain = (WebView) findViewById(R.id.wv_main);

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.setCancelable(false);
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
//        m_wvMain.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress < 100) {
//                    progressDialog.show();
//                }
//                if (progress == 100) {
//                    progressDialog.dismiss();
//                }
//            }
//        });

    }

    private void checkState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int state = sharedPreferences.getInt(SAVE_PASSWORD_KEY, SAVE_PASSWORD_STATE_NOTNOW);

        if (state == SAVE_PASSWORD_STATE_NOTNOW) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Would you like to save this password for the app?").setPositiveButton("Save", dialogClickListener)
                    .setNegativeButton("Never", dialogClickListener).setNeutralButton("Not Now", dialogClickListener).show();
        }

    }

    private void saveState(int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SAVE_PASSWORD_KEY, value);
        editor.commit();
    }

    private void saveUserInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, m_strCurUserName);
        editor.putString(PASSWORD_KEY, m_strCurPassword);
        editor.commit();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    saveState(SAVE_PASSWORD_STATE_SAVED);
                    saveUserInfo();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    saveState(SAVE_PASSWORD_STATE_NEVER);
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    saveState(SAVE_PASSWORD_STATE_NOTNOW);
                    break;
            }
        }
    };

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
        public void savePreferences(String username, String password){
            m_strCurUserName = username;
            m_strCurPassword = password;
            checkState();
        }

        @android.webkit.JavascriptInterface
        public String loadPreferences(String key){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sharedPreferences.getString(key, "");
        }
    }
}

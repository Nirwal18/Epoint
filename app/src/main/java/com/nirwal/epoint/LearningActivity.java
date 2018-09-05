package com.nirwal.epoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nirwal.epoint.services.HtmlDownloadTask;

import java.io.Console;

public class LearningActivity extends AppCompatActivity {

    WebView _learningArea;
    Context _context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_activity);

        _context = this;

        _learningArea = findViewById(R.id.learningPanel);
/* android WebView Settings */
       // _learningArea.getSettings().setUserAgentString("Android WebView");
      //  _learningArea.getSettings().setLoadWithOverviewMode(true);
       // _learningArea.getSettings().setUseWideViewPort(true);
        //_learningArea.getSettings().setDomStorageEnabled(true);
       // _learningArea.getSettings().setBuiltInZoomControls(true);
       // _learningArea.getSettings().setPluginState(WebSettings.PluginState.ON);
        //
        _learningArea.getSettings().setJavaScriptEnabled(true);
        //webview client must be called after load url

       // _learningArea.loadUrl("file:///android_asset/www/index.html");
        _learningArea.loadUrl("https://www.google.com");
        _learningArea.setWebChromeClient(new WebChromeClient());
        _learningArea.setWebViewClient(new myWebClient()); // it enable it to open net page in it
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // dispose object here
        if(_learningArea==null) {
        _learningArea.destroy();
        }

    }



    public void downloadFile(View v){
        HtmlDownloadTask task = new HtmlDownloadTask();
        task.execute("https://www.google.com");
    }



    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            Log.v("web","page started");
            Log.v("web",url);
        }



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.v("web",view.toString()+ "url :" +url);
            view.loadUrl(url);
            return true;
            }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.v("web",error.toString());
            Log.v("web",request.toString());
            Log.v("web",view.toString());
        }
    }




}





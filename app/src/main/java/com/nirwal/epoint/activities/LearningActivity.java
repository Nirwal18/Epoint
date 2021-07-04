package com.nirwal.epoint.activities;

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
import android.widget.Toast;

import com.nirwal.epoint.MyApp;
import com.nirwal.epoint.R;
import com.nirwal.epoint.customViews.CustomCrd;
import com.nirwal.epoint.models.ParentChildListItem;
import com.nirwal.epoint.services.HtmlDownloadTask;

import java.io.Console;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LearningActivity extends AppCompatActivity {


    WeakReference<Context> _context;
    ArrayList<ParentChildListItem> _items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_activity);
        _context = new WeakReference<Context>(this);

        _items = ((MyApp)getApplication()).getSqlDb().readAllMainCard();

        CustomCrd crd = findViewById(R.id.learning_cust);
        crd.setList(_items);

        crd.setOnClickSectionItemListener(new CustomCrd.OnClickSectionItemListener() {
            @Override
            public void onClick(ParentChildListItem item) {
                Toast.makeText(_context.get(),item.Title,Toast.LENGTH_LONG).show();
            }
        });

    }

//
//    public void initWebView(){
//        WebView _learningArea;
//        _learningArea = findViewById(R.id.learningPanel);
//        _learningArea.getSettings().setJavaScriptEnabled(true);
//        _learningArea.loadUrl("https://epoint.gq");
//        _learningArea.setWebChromeClient(new WebChromeClient());
//        _learningArea.setWebViewClient(new myWebClient()); // it enable it to open net page in it
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



//    public class myWebClient extends WebViewClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            // TODO sss
//            super.onPageStarted(view, url, favicon);
//            Log.v("web","page started");
//            Log.v("web",url);
//        }
//
//
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            Log.v("web",view.toString()+ "url :" +url);
//            view.loadUrl(url);
//            return true;
//            }
//
//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//            Log.v("web",error.toString());
//            Log.v("web",request.toString());
//            Log.v("web",view.toString());
//        }
//    }
//



}





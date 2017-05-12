package comp4900.bcit.ca.washaf.userpage;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import comp4900.bcit.ca.washaf.R;

public class Chatbox extends AppCompatActivity {

    ProgressBar pbar;
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        pbar = (ProgressBar)findViewById(R.id.progressBar1);

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://tawk.to/chat/5910a7d64ac4446b24a6de09/default/?$_tawk_popout=true");

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbar.setVisibility(View.VISIBLE);
                pbar.bringToFront();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

    }
}


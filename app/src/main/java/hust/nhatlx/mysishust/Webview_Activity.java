package hust.nhatlx.mysishust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class Webview_Activity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hust.nhatlx.mysishust.R.layout.activity_webview);

        Toolbar toolbar = (Toolbar) findViewById(hust.nhatlx.mysishust.R.id.toolbarWebView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Webview_Activity.this, TongQuan_Activity.class);
                startActivity(intent);
            }
        });

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        String urlThongTin = getIntent().getStringExtra("urlThongTin");
        webView.loadUrl("http://sis.hust.edu.vn/"+urlThongTin);
    }
}

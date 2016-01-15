package com.dam.profesor.navigationdrawer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.cuasmex.saec.R;

import Clases.URLServices;

public class Explorador extends AppCompatActivity {

    URLServices ur = new URLServices();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorador);
        WebView webView;

        webView = (WebView) findViewById(R.id.ExploradorView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.google.com");

        try {

            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                if (bundle.containsKey(ur.VIENEPAGOS) ) {
                       String _url = bundle.getString("_url");
                      _url = ur.Clear_URL(_url);

                    webView.loadUrl(_url);

                }
            }
        }
        catch (Exception ex){

        }

    }

}

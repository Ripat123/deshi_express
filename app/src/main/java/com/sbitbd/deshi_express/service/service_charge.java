package com.sbitbd.deshi_express.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;

import java.util.HashMap;
import java.util.Map;

public class service_charge extends AppCompatActivity {

    private ImageView back;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_charge);
        initView();
        config();
    }

    private void initView(){
        try {
            back = findViewById(R.id.service_back);
            webView = findViewById(R.id.service_webview);
        }catch (Exception e){
        }
    }

    private void config(){
        try {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            loadData();
        }catch (Exception e){
        }
    }

    private void loadData(){
        try {

            String sql = "SELECT description AS 'id' FROM `ourservice`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.trim().equals("")){
                                String encodedHtml = Base64.encodeToString(response.trim().getBytes(),
                                        Base64.NO_PADDING);
                                webView.loadData(encodedHtml, "text/html", "base64");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(service_charge.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(service_charge.this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
}
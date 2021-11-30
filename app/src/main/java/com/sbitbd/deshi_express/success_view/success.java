package com.sbitbd.deshi_express.success_view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.parcel.parcel;
import com.sbitbd.deshi_express.search.search;
import com.sbitbd.deshi_express.website.web_view;

public class success extends AppCompatActivity {

    private TextView invid,cust_name,cust_phone,cust_address;
    private Button view_btn,close,link_btn,parcel_btn;
    private String id,cus_name,cus_phone,cus_address;
    private config config = new config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        initview();
    }

    private void initview(){
        try {
            invid = findViewById(R.id.invid);
            cust_name = findViewById(R.id.cust_name);
            cust_phone = findViewById(R.id.cust_phone);
            cust_address = findViewById(R.id.cust_address);
            view_btn = findViewById(R.id.view_btn);
            close = findViewById(R.id.close);
            parcel_btn = findViewById(R.id.parcel_btn);
            link_btn = findViewById(R.id.link_btn);

            id = getIntent().getStringExtra("invoice");
            cus_name = getIntent().getStringExtra("cus_name");
            cus_phone = getIntent().getStringExtra("cus_phone");
            cus_address = getIntent().getStringExtra("cus_address");

            invid.setText(id);
            cust_name.setText(cus_name);
            cust_phone.setText(cus_phone);
            cust_address.setText(cus_address);

            view_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(success.this, search.class);
                    intent.putExtra("position","2");
                    startActivity(intent);
                    finish();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            link_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(success.this, web_view.class);
                    intent.putExtra("link",config.getUser(success.this));
                    startActivity(intent);
                }
            });
            parcel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(success.this, parcel.class));
                    finish();
                }
            });
        }catch (Exception e){
        }
    }
}
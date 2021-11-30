package com.sbitbd.deshi_express.suggest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sbitbd.deshi_express.R;

public class suggest extends AppCompatActivity {

    private ImageView back;
    private EditText name,phone,address,suggest;
    private Button suggest_btn;
    private suggest_controller suggest_controller =new suggest_controller();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        initView();
    }

    private void initView(){
        try {
            back = findViewById(R.id.suggest_back);
            name = findViewById(R.id.name_e);
            phone = findViewById(R.id.phone_e);
            address = findViewById(R.id.address_e);
            suggest = findViewById(R.id.suggest_e);
            suggest_btn = findViewById(R.id.suggest_submit);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            suggest_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = ProgressDialog.show(suggest.this,"","Sending...",false,false);
                    suggest_controller.suggest(suggest.this,"INSERT INTO customer_subscribe " +
                            "(name,email,phone,sms) VALUES ('"+name.getText().toString().trim()+"'," +
                            "'"+address.getText().toString().trim()+"','"+phone.getText().toString().trim()+"'," +
                            "'"+suggest.getText().toString().trim()+"')",v,progressDialog);
                }
            });
        }catch (Exception e){
        }
    }
}
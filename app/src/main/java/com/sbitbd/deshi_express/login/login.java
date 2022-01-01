package com.sbitbd.deshi_express.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.Main_dashboard;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.register.register;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private ImageView close;
    private Button login,reg;
    private EditText user, pass;
    private login_controller login_controller = new login_controller();
    private BeautifulProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    private void initview() {
        try {
            close = findViewById(R.id.login_back);
            login = findViewById(R.id.login);
            reg = findViewById(R.id.reg);
            user = findViewById(R.id.user);
            pass = findViewById(R.id.pass);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Login();
//                    startActivity(new Intent(login.this, Main_dashboard.class));
//                    finish();
                }
            });
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(login.this, register.class));
                }
            });
        } catch (Exception e) {
        }
    }

    private void Login() {
        try {
            if (user.getText().toString().trim().equals("")) {
                user.setError("Empty Phone or Email!");
                user.requestFocus();
                Toast.makeText(login.this, "Empty Phone or Email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.getText().toString().trim().equals("")) {
                pass.setError("Empty Password!");
                pass.requestFocus();
                Toast.makeText(login.this, "Empty Password!", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = new BeautifulProgressDialog(login.this,
                    BeautifulProgressDialog.withLottie, null);
            progressDialog.setLottieLocation("json_lottie/loading-pickupposaja.json");
            progressDialog.setLayoutColor(Color.WHITE);
            progressDialog.setLayoutRadius(13f);
            progressDialog.setMessage("Loading...");
            progressDialog.setLayoutElevation(8f);
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response != null && !response.trim().equals("") && !response.trim().equals("{\"result\":[]}")) {
                                login_controller.showJson(response.trim(), user.getText()
                                        .toString().trim(), login.this);
                                startActivity(new Intent(login.this, Main_dashboard.class));
                                finish();
                            } else {
                                Toast.makeText(login.this, "Wrong user or not approved by admin!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(login.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, user.getText().toString().trim());
                    params.put(config.PASS, pass.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(login.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

//            boolean check = login_controller.login(login.this,user.getText().toString().trim(),pass.getText().toString().trim());
//            if (check)
//                Toast.makeText(login.this, "successful", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(login.this, "Unsuccessful!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

}
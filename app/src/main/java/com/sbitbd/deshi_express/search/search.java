package com.sbitbd.deshi_express.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.deshi_express.Adapter.pay_adapter;
import com.sbitbd.deshi_express.Adapter.shipment_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.return_model;
import com.sbitbd.deshi_express.model.six_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class search extends AppCompatActivity {

    private ImageView imageView10;
    private RecyclerView recyclerView;
    private EditText editText;
    private shipment_adapter dash_adapter;
    private config config = new config();
    private ConstraintLayout constraintLayout;
    private String position = "";
    private pay_adapter return_adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initview();
    }

    private void initview() {
        try {
            imageView10 = findViewById(R.id.imageView10);
            recyclerView = findViewById(R.id.search_rec);
            editText = findViewById(R.id.search);
            constraintLayout = findViewById(R.id.not_lay);
            position = getIntent().getStringExtra("position");

            imageView10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });

            GridLayoutManager manager = new GridLayoutManager(search.this, 1);
            recyclerView.setLayoutManager(manager);
            dash_adapter = new shipment_adapter(search.this);

            if (position != null && position.equals("2")) {
                initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                        "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                        " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                        getUser(search.this) + "' and position = '2' or position = '1' order by delivery.id desc LIMIT 20");
            }
            else if (position != null && position.equals("3")){
                String sql = "SELECT sum(merchant_invoice_charge.coll_amount - merchant_invoice_charge." +
                        "total_amount) as 'one',merchant_invoice_charge.payment_id as 'two',merchant_pay." +
                        "collection as 'three',merchant_pay.payment_type as 'four',merchant_pay.due as 'five'," +
                        "merchant_pay.date as 'six',merchant_pay.comment as 'seven' from merchant_invoice_charge " +
                        "inner join merchant_pay on merchant_invoice_charge.payment_id = merchant_pay.id where " +
                        "merchant_invoice_charge.merchant_id = '"+config.getUser(search.this)+"' and merchant_invoice_charge.payment_req_status " +
                        "= '2' and merchant_invoice_charge.paid_status = '1' order by delivery.id desc";

                GridLayoutManager manager1 = new GridLayoutManager(search.this, 1);
                recyclerView.setLayoutManager(manager1);
                return_adapter = new pay_adapter(search.this);
                setAutoComplete(sql);
            }else {
                editText.requestFocus();
                initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                        "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                        " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                        getUser(search.this) + "' order by delivery.id desc LIMIT 20");
            }

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (position != null && position.equals("2")) {
                        initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                                "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                                " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                                getUser(search.this) + "' and (position = '2' or position = '1') and delivery.id " +
                                "like '%" + editText.getText().toString().trim() + "%' LIMIT 20");
                    }
                    else {
                        initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                                "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                                " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                                getUser(search.this) + "' and (delivery.id like '%" + editText.getText().toString().trim() + "%'" +
                                " or delivery.customer_name like '%"+editText.getText().toString().trim()+"%'" +
                                " or delivery.customer_phone like '%"+editText.getText().toString().trim()+"%') LIMIT 20");
                    }

                }
            };
            editText.addTextChangedListener(textWatcher);

            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        View view = getCurrentFocus();
                        hideKeyboardFrom(search.this, view);
                        if (position != null && position.equals("2")) {
                            initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                                    "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                                    " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                                    getUser(search.this) + "' and (position = '2' or position = '1') and " +
                                    "delivery.id like '%" + editText.getText().toString().trim() + "%' LIMIT 20");
                        }else {
                            initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                                    "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                                    " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                                    getUser(search.this) + "' and (delivery.id like '%" + editText.getText().toString().trim() + "%' " +
                                    "or delivery.customer_name like '%"+editText.getText().toString().trim()+"%'" +
                                    " or delivery.customer_phone like '%"+editText.getText().toString().trim()+"%') LIMIT 20");
                        }
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
        }
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setAutoComplete(String sql) {
        try {
            progressDialog = ProgressDialog.show(search.this,"","Loading...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.EIGHT_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showAuto(response.trim());
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(search.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(search.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
        recyclerView.setAdapter(return_adapter);
    }

    private void initData(String sql) {
        try {
            constraintLayout.setVisibility(View.GONE);
            dash_adapter.Clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.EIGHT_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                show_data(response.trim());
                            else
                                constraintLayout.setVisibility(View.VISIBLE);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(search.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(search.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
        recyclerView.setAdapter(dash_adapter);
    }

    private void show_data(String response) {
        String one = "", two = "", three = "", four = "", five = "", six = "";
        six_model six_model;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    one = collegeData.getString(config.ONE);
                    two = collegeData.getString(config.TWO);
                    three = collegeData.getString(config.THREE);
                    four = collegeData.getString(config.FOUR);
                    five = collegeData.getString(config.FIVE);
                    six = collegeData.getString(config.SIX);
                    six_model = new six_model(one, two, three, four, five, six);
                    dash_adapter.addUser(six_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
    protected void showAuto(String response) {
        String one = "";
        String two = "";
        return_model cat_models;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    two = collegeData.getString(config.TWO);
                    one = collegeData.getString(config.ONE);
                    cat_models = new return_model(two, collegeData.getString(config.SIX),one,two);
                    return_adapter.addUser(cat_models);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
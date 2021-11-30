package com.sbitbd.deshi_express.balance_details;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class balance_details extends AppCompatActivity {

    private TextView business_name,merchant_name,merchant_address,merchant_mobile,tm_deliverd,ts_charge,
            tc_charge,tp_charge,st_charge,t_discount,g_total,p_bill,d_bill,bank_name,branch_name,
            account_holder,bank_ac_no,bkash_n,nagad_n,note,bank_method;
    private ProgressDialog progressDialog;
    private config config = new config();
    private String bal_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_details);
        initview();
    }

    private void initview(){
        try {
            business_name = findViewById(R.id.business_name);
            merchant_name = findViewById(R.id.merchant_name);
            merchant_address = findViewById(R.id.merchant_address);
            merchant_mobile = findViewById(R.id.merchant_mobile);
            tm_deliverd = findViewById(R.id.text2);
            ts_charge = findViewById(R.id.text4);
            tc_charge = findViewById(R.id.text6);
            tp_charge = findViewById(R.id.text8);
            st_charge = findViewById(R.id.text10);
            t_discount = findViewById(R.id.text12);
            g_total = findViewById(R.id.text14);
            p_bill = findViewById(R.id.text16);
            d_bill = findViewById(R.id.text18);
            bank_name = findViewById(R.id.text21);
            branch_name = findViewById(R.id.text23);
            account_holder = findViewById(R.id.text25);
            bank_ac_no = findViewById(R.id.text27);
            bkash_n = findViewById(R.id.text29);
            nagad_n = findViewById(R.id.text31);
            note = findViewById(R.id.text33);
            bank_method = findViewById(R.id.bank_method);

            bal_id = getIntent().getStringExtra("id");
            get_merchant_info();
            get_balance();
            getBank_info();
        }catch (Exception e){
        }
    }

    private void get_merchant_info(){
        try {
            String sql = "SELECT Businessname as 'one',ownername as 'two',Address as 'three',contactnumber " +
                    "as 'four' from merchant_acc where userid = '"+config.getUser(balance_details.this)+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    business_name.setText(collegeData.getString(config.ONE));
                                    merchant_name.setText(collegeData.getString(config.TWO));
                                    merchant_address.setText(collegeData.getString(config.THREE));
                                    merchant_mobile.setText(collegeData.getString(config.FOUR));
                                }catch (Exception e){
                                }
                            } else {
                                Toast.makeText(balance_details.this, "Not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(balance_details.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(balance_details.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void getBank_info(){
        try {
            String sql = "SELECT bank_name as 'one',branch_name as 'two',holder_name as 'three',bank_acc " +
                    "as 'four',bkash as 'five',rocket as 'six' from merchant_acc where userid = '"+config.getUser(balance_details.this)+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.EIGHT_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    bank_name.setText(collegeData.getString(config.ONE));
                                    branch_name.setText(collegeData.getString(config.TWO));
                                    account_holder.setText(collegeData.getString(config.THREE));
                                    bank_ac_no.setText(collegeData.getString(config.FOUR));
                                    bkash_n.setText(collegeData.getString(config.FIVE));
                                    nagad_n.setText(collegeData.getString(config.SIX));
                                }catch (Exception e){
                                }
                            } else {
                                Toast.makeText(balance_details.this, "Not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(balance_details.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(balance_details.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void get_balance(){
        try {
            String sql = "SELECT merchant_invoice_charge.coll_amount as 'one',merchant_invoice_charge." +
                    "shipmentcharges as 'two',merchant_invoice_charge.codchargeits as 'three',merchant_invoice_charge.packchrgs " +
                    "as 'four',merchant_invoice_charge.total_charge as 'five',merchant_invoice_charge." +
                    "discount as 'six',merchant_invoice_charge.coll_amount-merchant_invoice_charge.total_amount as 'seven'," +
                    "merchant_pay.collection as 'eight',merchant_pay.due as 'nine',merchant_pay.payment_type" +
                    " as 'ten',merchant_pay.comment as 'eleven' from merchant_invoice_charge inner join " +
                    "merchant_pay on merchant_invoice_charge.payment_id = merchant_pay.id where merchant_id = '"+config.getUser(balance_details.this)+"'" +
                    " and payment_id = '"+bal_id+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.TWELVE_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    tm_deliverd.setText(collegeData.getString(config.ONE));
                                    ts_charge.setText(collegeData.getString(config.TWO));
                                    tc_charge.setText(collegeData.getString(config.THREE));
                                    tp_charge.setText(collegeData.getString(config.FOUR));
                                    st_charge.setText(collegeData.getString(config.FIVE));
                                    t_discount.setText(collegeData.getString(config.SIX));
                                    g_total.setText(collegeData.getString(config.SEVEN));
                                    p_bill.setText(collegeData.getString(config.EIGHT));
                                    d_bill.setText(collegeData.getString(config.NINE));
                                    note.setText(collegeData.getString(config.ELEVEN));
                                    if (collegeData.getString(config.TEN).equals("1"))
                                        bank_method.setText(getString(R.string.request_withdraw_method)+"Cash");
                                    else if (collegeData.getString(config.TEN).equals("2"))
                                        bank_method.setText(getString(R.string.request_withdraw_method)+"Bank");
                                    else if (collegeData.getString(config.TEN).equals("3"))
                                        bank_method.setText(getString(R.string.request_withdraw_method)+"Bkash");
                                    else
                                        bank_method.setText(getString(R.string.request_withdraw_method)+"Rocket");
                                }catch (Exception e){
                                }
                            } else {
                                Toast.makeText(balance_details.this, "Not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(balance_details.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(balance_details.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }
}
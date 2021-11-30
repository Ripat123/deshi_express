package com.sbitbd.deshi_express.view_form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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
import com.sbitbd.deshi_express.Adapter.return_adapter;
import com.sbitbd.deshi_express.Adapter.shipment_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.return_model;
import com.sbitbd.deshi_express.model.six_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class view_form extends AppCompatActivity {

    private ImageView back;
    private TextView title, footer;
    private RecyclerView recyclerView;
    private String view_title, status;
    private shipment_adapter dash_adapter;
    private six_model six_model;
    private config config =new config();
    private return_adapter return_adapter;
    private return_model return_model;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_form);
        initview();
    }

    private void initview() {
        try {
            back = findViewById(R.id.about_back);
            title = findViewById(R.id.materialTextView);
            footer = findViewById(R.id.footer_text);
            recyclerView = findViewById(R.id.view_rec);
            constraintLayout = findViewById(R.id.not_lay);

            view_title = getIntent().getStringExtra("title");
            status = getIntent().getStringExtra("status");
            title.setText(view_title);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    finish();
                }
            });

            if (status.equals("ship")) {
                GridLayoutManager manager = new GridLayoutManager(view_form.this, 1);
                recyclerView.setLayoutManager(manager);
                dash_adapter = new shipment_adapter(view_form.this);


                initData("SELECT id AS 'one',created_at AS 'two',customer_name AS 'three',agent_info." +
                        "branch_name AS 'four',totalamounts AS 'five',coll_amount AS 'six' FROM delivery" +
                        " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                        getUser(view_form.this) + "' and position = '4'");
            }
            else if (status.equals("return")){
                GridLayoutManager manager = new GridLayoutManager(view_form.this, 1);
                recyclerView.setLayoutManager(manager);
                return_adapter = new return_adapter(view_form.this,0);
                initReturn("SELECT id AS 'one',updated_at AS 'two',customer_name AS 'three' FROM delivery" +
                        "  where merchantId = '" + config.getUser(view_form.this) + "'" +
                        "   and position = '11'");

                recyclerView.setAdapter(return_adapter);
            }
        } catch (Exception e) {
        }
    }

    private void initData(String sql) {
        try {

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
                    Toast.makeText(view_form.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(view_form.this);
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

    private void initReturn(String sql) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                show_return_data(response.trim());
                            else
                                constraintLayout.setVisibility(View.VISIBLE);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(view_form.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(view_form.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
        recyclerView.setAdapter(dash_adapter);
    }

    private void show_return_data(String response) {
        String one = "", two = "", three = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    one = collegeData.getString(config.ONE);
                    two = collegeData.getString(config.TWO);
                    three = collegeData.getString(config.THREE);
                    return_model = new return_model(one, two, three, one);
                    return_adapter.addUser(return_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
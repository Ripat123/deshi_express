package com.sbitbd.deshi_express.ui.consignments_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.deshi_express.Adapter.shipment_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.six_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class delivered extends Fragment {

    private View root;
    private RecyclerView all_rec;
    private EditText search;
    private config config =new config();
    private shipment_adapter dash_adapter;
    private ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_delivered, container, false);
        initView();
        return root;
    }

    private void initView(){
        try {
            all_rec = root.findViewById(R.id.delivered_rec);
            search = root.findViewById(R.id.search_delivered);
            constraintLayout = root.findViewById(R.id.not_lay);

            GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1);
            all_rec.setLayoutManager(manager);
            dash_adapter = new shipment_adapter(root.getContext().getApplicationContext());

            initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                    "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                    " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                    getUser(root.getContext().getApplicationContext()) + "' and position = '7' order by delivery.id desc LIMIT 20");

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                            "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                            " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                            getUser(root.getContext().getApplicationContext()) + "' and position = '7' and (delivery.id like '%"+search.getText().toString().trim()+"%'" +
                            " or delivery.customer_name like '%"+search.getText().toString().trim()+"%'" +
                            " or delivery.customer_phone like '%"+search.getText().toString().trim()+"%') LIMIT 20");
                }
            };
            search.addTextChangedListener(textWatcher);
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        View view = getView();
                        hideKeyboardFrom(root.getContext().getApplicationContext(), view);
                        initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                                "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                                " INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = '" + config.
                                getUser(root.getContext().getApplicationContext()) + "' and position = '7' and (delivery.id like '%"+search.getText().toString().trim()+"%'" +
                                " or delivery.customer_name like '%"+search.getText().toString().trim()+"%'" +
                                " or delivery.customer_phone like '%"+search.getText().toString().trim()+"%') LIMIT 20");
                        return true;
                    }
                    return false;
                }
            });
        }catch (Exception e){
        }
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
        all_rec.setAdapter(dash_adapter);
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
}
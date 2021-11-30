package com.sbitbd.deshi_express.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.sbitbd.deshi_express.Adapter.dash_adapter;
import com.sbitbd.deshi_express.Adapter.shipment_adapter;
import com.sbitbd.deshi_express.Adapter.state_rep_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.home_model;
import com.sbitbd.deshi_express.model.six_model;
import com.sbitbd.deshi_express.model.state_rep_model;
import com.sbitbd.deshi_express.search.search;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private dash_adapter dash_adapter;
    private shipment_adapter shipment_adapter;
    private home_model home_model;
    private RecyclerView dash_rec, rep_rec,invoice_rec;
    private View root1;
    private state_rep_adapter state_rep_adapter;
    private state_rep_model state_rep_model;
    private MaterialCardView search_card, dash_card1, dash_card2;
    private config config = new config();
    private List<String> report_type = new ArrayList<>();
    private AutoCompleteTextView all_rep_t;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        root1 = root;
        initview(root);
        return root;
    }

    private void initview(View root) {
        try {
            dash_rec = root.findViewById(R.id.dash_rec);
            rep_rec = root.findViewById(R.id.rep_rec);
            search_card = root.findViewById(R.id.search_card);
            dash_card1 = root.findViewById(R.id.dash_card1);
            dash_card2 = root.findViewById(R.id.dash_card2);
            all_rep_t = root.findViewById(R.id.all_rep_t);
            invoice_rec = root.findViewById(R.id.home_order_rec);

            search_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(root.getContext().getApplicationContext(), search.class));
                }
            });

            dash_card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(root.getContext().getApplicationContext(), search.class);
                    intent.putExtra("position", "2");
                    startActivity(intent);
                }
            });
            dash_card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(root.getContext().getApplicationContext(), search.class);
                    intent.putExtra("position", "3");
                    startActivity(intent);
                }
            });
            report_type.clear();
            report_type.add("All");
            report_type.add("24 Hours");
            report_type.add("Last 7 days");
            report_type.add("Last Month");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root1.getContext().getApplicationContext(),
                    R.layout.item_name, report_type);
            all_rep_t.setAdapter(dataAdapter);
            all_rep_t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    initReport(all_rep_t.getText().toString());
                }
            });
            shipment_adapter = new shipment_adapter(root.getContext().getApplicationContext());
            GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1);
            invoice_rec.setLayoutManager(manager);
            initData("SELECT delivery.id AS 'one',delivery.created_at AS 'two',delivery.customer_name AS 'three',agent_info." +
                    "branch_name AS 'four',delivery.totalamounts AS 'five',delivery.coll_amount AS 'six' FROM delivery" +
                    "  INNER JOIN agent_info ON delivery.pickup_agent_id = agent_info.id where merchantId = " +
                    "'" + config. getUser(root.getContext().getApplicationContext()) + "' order by delivery.id desc LIMIT 5",root.getContext().getApplicationContext());
        } catch (Exception e) {
        }
    }

    private void config() {
        try {

            GridLayoutManager manager = new GridLayoutManager(root1.getContext().getApplicationContext(), 2);
            dash_rec.setLayoutManager(manager);
            dash_rec.setNestedScrollingEnabled(false);
            dash_adapter = new dash_adapter(root1.getContext().getApplicationContext());
            home_model = new home_model("1", "Pickup", "", "");
            dash_adapter.addUser(home_model);
            home_model = new home_model("2", "Pay Request", "", "");
            dash_adapter.addUser(home_model);
            home_model = new home_model("3", "Shipment", "", "");
            dash_adapter.addUser(home_model);
            home_model = new home_model("4", "Balance", "", "");
            dash_adapter.addUser(home_model);
            home_model = new home_model("5", "Return", "", "");
            dash_adapter.addUser(home_model);
            home_model = new home_model("6", "Success", "", "");
            dash_adapter.addUser(home_model);
            dash_rec.setAdapter(dash_adapter);


            GridLayoutManager manager1 = new GridLayoutManager(root1.getContext().getApplicationContext(), 2);
            rep_rec.setLayoutManager(manager1);
            rep_rec.setNestedScrollingEnabled(false);
            state_rep_adapter = new state_rep_adapter(root1.getContext().getApplicationContext());
            state_rep_model = new state_rep_model(R.drawable.ic_checking_boxes_amico, "Total Parcel", "0");
            state_rep_adapter.addUser(state_rep_model);
            homeViewModel.update_home_data(root1.getContext().getApplicationContext(), state_rep_adapter,
                    state_rep_model, "SELECT COUNT(id) AS 'id' FROM delivery WHERE merchantId = '" +
                            "" + config.getUser(root1.getContext().getApplicationContext()) + "'");
            state_rep_model = new state_rep_model(R.drawable.ic_credit_card_payment_cuate, "Total COD", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_coronavirus_delivery_preventions_amico, "Total Delivered", "0");
            state_rep_adapter.addUser(state_rep_model);
            homeViewModel.update_home_data(root1.getContext().getApplicationContext(), state_rep_adapter,
                    state_rep_model, "SELECT COUNT(id) AS 'id' FROM delivery WHERE merchantId = '" +
                            "" + config.getUser(root1.getContext().getApplicationContext()) + "' and position = '7'");
            state_rep_model = new state_rep_model(R.drawable.ic_investment_data_amico, "Delivered Amount", "0");
            state_rep_adapter.addUser(state_rep_model);
            homeViewModel.update_home_data(root1.getContext().getApplicationContext(), state_rep_adapter,
                    state_rep_model, "SELECT sum(coll_amount) AS 'id' FROM delivery WHERE merchantId = '" +
                            "" + config.getUser(root1.getContext().getApplicationContext()) + "' and position = '7'");
            state_rep_model = new state_rep_model(R.drawable.ic_plain_credit_card_pana, "Total Cost", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_credit_card_payment_cuate, "Delivery Charge", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_credit_card_payment_cuate, "COD Charge", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_plain_credit_card_pana, "Other Cost", "0");
            state_rep_adapter.addUser(state_rep_model);
            rep_rec.setAdapter(state_rep_adapter);
        } catch (Exception e) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        config();
    }

    private void initReport(String date) {
        try {
            String ext = "";
            if (date.equals("All")) {
            } else if (date.equals("24 Hours"))
                ext = " AND created_at BETWEEN DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 1 DAY) and CURRENT_TIMESTAMP()";
            else if (date.equals("Last 7 days"))
                ext = " AND created_at BETWEEN DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 1 WEEK) and CURRENT_TIMESTAMP()";
            else
                ext = " AND created_at BETWEEN DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 1 MONTH) and CURRENT_TIMESTAMP()";
            state_rep_adapter.Clear();
            state_rep_model = new state_rep_model(R.drawable.ic_checking_boxes_amico, "Total Parcel", "0");
            state_rep_adapter.addUser(state_rep_model);
            homeViewModel.update_home_data(root1.getContext().getApplicationContext(), state_rep_adapter,
                    state_rep_model, "SELECT COUNT(id) AS 'id' FROM delivery WHERE merchantId = '" +
                            "" + config.getUser(root1.getContext().getApplicationContext()) + "'"+ext);
            state_rep_model = new state_rep_model(R.drawable.ic_credit_card_payment_cuate, "Total COD", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_coronavirus_delivery_preventions_amico, "Total Delivered", "0");
            state_rep_adapter.addUser(state_rep_model);
            homeViewModel.update_home_data(root1.getContext().getApplicationContext(), state_rep_adapter,
                    state_rep_model, "SELECT COUNT(id) AS 'id' FROM delivery WHERE merchantId = '" +
                            "" + config.getUser(root1.getContext().getApplicationContext()) + "' and position = '7'"+ext);
            state_rep_model = new state_rep_model(R.drawable.ic_investment_data_amico, "Delivered Amount", "0");
            state_rep_adapter.addUser(state_rep_model);
            homeViewModel.update_home_data(root1.getContext().getApplicationContext(), state_rep_adapter,
                    state_rep_model, "SELECT sum(coll_amount) AS 'id' FROM delivery WHERE merchantId = '" +
                            "" + config.getUser(root1.getContext().getApplicationContext()) + "' and position = '7'"+ext);
            state_rep_model = new state_rep_model(R.drawable.ic_plain_credit_card_pana, "Total Cost", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_credit_card_payment_cuate, "Delivery Charge", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_credit_card_payment_cuate, "COD Charge", "0");
            state_rep_adapter.addUser(state_rep_model);
            state_rep_model = new state_rep_model(R.drawable.ic_plain_credit_card_pana, "Other Cost", "0");
            state_rep_adapter.addUser(state_rep_model);
            rep_rec.setAdapter(state_rep_adapter);
        } catch (Exception e) {
        }
    }

    private void initData(String sql, Context context) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.EIGHT_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                show_data(response.trim());
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
        invoice_rec.setAdapter(shipment_adapter);
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
                    shipment_adapter.addUser(six_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
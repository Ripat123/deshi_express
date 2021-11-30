package com.sbitbd.deshi_express.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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
import com.sbitbd.deshi_express.Adapter.state_rep_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.state_rep_model;
import com.sbitbd.deshi_express.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private state_rep_adapter state_rep_adapter;
    private state_rep_model state_rep_model;
    private RecyclerView recyclerView;
    private View root1;
    private HomeViewModel homeViewModel = new HomeViewModel();
    private config config = new config();
    private List<String> report_type = new ArrayList<>();
    private TextView delivery_count, cancal, balance, total_pending, last_day, pending_amount,req_count;
    private AutoCompleteTextView all_rep_t;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        root1 = root;
        initView(root);
        return root;
    }

    private void initView(View root) {
        try {
            recyclerView = root.findViewById(R.id.last_rec);
            delivery_count = root.findViewById(R.id.all_count);
            cancal = root.findViewById(R.id.cancel_count);
            balance = root.findViewById(R.id.bal_count);
            last_day = root.findViewById(R.id.day_count);
            pending_amount = root.findViewById(R.id.amount_count);
            total_pending = root.findViewById(R.id.pen_count);
            req_count = root.findViewById(R.id.req_count);
            all_rep_t = root.findViewById(R.id.all_rep_t);

        } catch (Exception e) {
        }
    }

    private void config() {
        try {
            GridLayoutManager manager = new GridLayoutManager(root1.getContext().getApplicationContext(), 2);
            recyclerView.setLayoutManager(manager);
            recyclerView.setNestedScrollingEnabled(false);
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
            recyclerView.setAdapter(state_rep_adapter);

            get_data(delivery_count,root1.getContext().getApplicationContext(),"SELECT COUNT(id) AS 'id' FROM delivery WHERE merchantId = '" +
                     config.getUser(root1.getContext().getApplicationContext()) + "' and position = '7'");
            get_data(cancal,root1.getContext().getApplicationContext(),"SELECT COUNT(id) AS 'id' FROM delivery WHERE merchantId = '" +
                    config.getUser(root1.getContext().getApplicationContext()) + "' and position = '8'");
            get_data(balance,root1.getContext().getApplicationContext(),"select sum(coll_amount - total_amount) as 'id'" +
                    " from merchant_invoice_charge where merchant_id = '"+config.getUser(root1.getContext().getApplicationContext())+"' and paid_status != '1'");
            get_data(total_pending,root1.getContext().getApplicationContext(),"SELECT COUNT(id) as 'id' from delivery " +
                    "where merchantId = '"+config.getUser(root1.getContext().getApplicationContext())+"' and (position = '1' or position = '0')");
            get_data(req_count,root1.getContext().getApplicationContext(),"select COUNT(id) as 'id' " +
                    "from delivery where merchantId = '"+config.getUser(root1.getContext().getApplicationContext())+"' and pickup_req_status is null");

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
        } catch (Exception e) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        config();
    }

    private void get_data(TextView editText, Context context1,String sql) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals(""))
                                editText.setText("0");
                            else
                                editText.setText(response.trim());
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context1, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context1);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
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
            recyclerView.setAdapter(state_rep_adapter);
        } catch (Exception e) {
        }
    }
}
package com.sbitbd.deshi_express.ui.pricing;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.identity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pricing extends Fragment {

    private PricingViewModel mViewModel;
    private View root;
    private MaterialCardView cardView;
    private AutoCompleteTextView district, thana, service, weight;
    private Button btn;
    private TextView dash_t;
    private List<identity> district_list = new ArrayList<>();
    private List<String> district_name = new ArrayList<>();
    private List<identity> thana_list = new ArrayList<>();
    private List<String> thana_name = new ArrayList<>();
    private List<String> service_name = new ArrayList<>();
    private List<identity> weight_list = new ArrayList<>();
    private List<String> weight_name = new ArrayList<>();
    private identity identity;
    private String district_id, thana_id, weight_id;
    private config config = new config();
    private ProgressBar progressBar;
    private String sql = "";

    public static pricing newInstance() {
        return new pricing();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PricingViewModel.class);
        root = inflater.inflate(R.layout.pricing_fragment, container, false);
        initview();
        return root;
    }

    private void initview() {
        try {
            cardView = root.findViewById(R.id.price_card);
            district = root.findViewById(R.id.district);
            thana = root.findViewById(R.id.thana);
            service = root.findViewById(R.id.service);
            weight = root.findViewById(R.id.weight_s);
            btn = root.findViewById(R.id.show_btn);
            dash_t = root.findViewById(R.id.dash_t);
            progressBar = root.findViewById(R.id.prog);

            setAutoComplete("SELECT id AS 'one',districtname AS 'two'" +
                    " FROM districtinfo", district, district_name, district_list);
            setAutoComplete("SELECT id AS 'one',title AS 'two'" +
                    " FROM weight_title order by sl asc", weight, weight_name, weight_list);

            district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    identity = district_list.get(position);
                    district_id = identity.getId();
                    setAutoComplete("SELECT id AS 'one',upozillaname AS 'two'" +
                            " FROM upozillainfo WHERE " +
                            "fk_district_id = '" + district_id + "'", thana, thana_name, thana_list);
                    thana.setText("");
                }
            });
            thana.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    identity = thana_list.get(position);
                    thana_id = identity.getId();
                }
            });
            weight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    identity = weight_list.get(position);
                    weight_id = identity.getId();
                }
            });

            service_name.add("Regular Day");
            service.setText(R.string.regular);
            service_name.add("Quick Day");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root.getContext().getApplicationContext(),
                    R.layout.item_name, service_name);
            service.setAdapter(dataAdapter);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go();
                }
            });
        } catch (Exception e) {
        }
    }

    private void setAutoComplete(String sql, AutoCompleteTextView autoCompleteTextView, List<String> name, List<identity> list) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.TWO_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showAuto(response.trim(), root.getContext().getApplicationContext(), autoCompleteTextView,
                                        name, list);
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
    }

    protected void showAuto(String response, Context context, AutoCompleteTextView autoCompleteTextView,
                            List<String> strings, List<identity> identities) {
        String id = "";
        String name = "";
        identity cat_models;
        try {
            strings.clear();
            identities.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    name = collegeData.getString(config.TWO);
                    id = collegeData.getString(config.ONE);
                    cat_models = new identity(id, name);
                    identities.add(cat_models);
                    strings.add(name);
                } catch (Exception e) {
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                    R.layout.item_name, strings);
            autoCompleteTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    private void go() {
        try {
            if (district.getText().toString().trim().equals("")) {
                district.setError("Select District");
                return;
            }
            if (thana.getText().toString().trim().equals("")) {
                thana.setError("Select Thana");
                return;
            }
            if (service.getText().toString().trim().equals("")) {
                service.setError("Select Service");
                return;
            }
            if (weight.getText().toString().trim().equals("")) {
                weight.setError("Select Weight");
                return;
            }
            dash_t.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);


            if (service.getText().toString().equals("Quick Day"))
                sql = "SELECT quick_charge AS 'id' FROM delivery_charge_area WHERE fk_district_id = '" + district_id + "' AND" +
                        " fk_upozilla_id = '" + thana_id + "' AND weight_title = '" + weight_id + "' AND quick_day = '1-2'";
            else
                sql = "SELECT regular_cahrge AS 'id' FROM delivery_charge_area WHERE fk_district_id = '" + district_id + "' AND" +
                        " fk_upozilla_id = '" + thana_id + "' AND weight_title = '" + weight_id + "' AND regular_day = '1-3'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);
                            dash_t.setVisibility(View.VISIBLE);
                            if (response != null && !response.trim().equals("")) {
                                dash_t.setText(response.trim() + " TK");
                            }else
                                dash_t.setText("Not Found");
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    dash_t.setVisibility(View.VISIBLE);
                    dash_t.setText("Internet Error");
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
    }

}
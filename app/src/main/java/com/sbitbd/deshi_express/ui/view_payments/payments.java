package com.sbitbd.deshi_express.ui.view_payments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.deshi_express.Adapter.pay_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.return_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class payments extends Fragment {

    private View root;
    private RecyclerView all_rec;
    private EditText search;
    private pay_adapter return_adapter;
    private return_model return_model;
    private config config = new config();
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_payments, container, false);
        initView();
        return root;
    }

    private void initView(){
        try {
            all_rec = root.findViewById(R.id.pay_rec);
            search = root.findViewById(R.id.search_pay);

            String sql = "SELECT sum(merchant_invoice_charge.coll_amount - merchant_invoice_charge." +
                    "total_amount) as 'one',merchant_invoice_charge.payment_id as 'two',merchant_pay." +
                    "collection as 'three',merchant_pay.payment_type as 'four',merchant_pay.due as 'five'," +
                    "merchant_pay.date as 'six',merchant_pay.comment as 'seven' from merchant_invoice_charge " +
                    "inner join merchant_pay on merchant_invoice_charge.payment_id = merchant_pay.id where " +
                    "merchant_invoice_charge.merchant_id = ''"+config.getUser(root.getContext().getApplicationContext())+"' and merchant_invoice_charge.payment_req_status " +
                    "= '2' and merchant_invoice_charge.paid_status = '1'";

            GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1);
            all_rec.setLayoutManager(manager);
            return_adapter = new pay_adapter(root.getContext().getApplicationContext());
            setAutoComplete(sql);

        }catch (Exception e){
        }
    }

    private void setAutoComplete(String sql) {
        try {progressDialog = ProgressDialog.show(root.getContext().getApplicationContext(),"","Loading...",false,false);
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
        all_rec.setAdapter(return_adapter);
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
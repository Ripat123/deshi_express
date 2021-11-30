package com.sbitbd.deshi_express.ui.settings.settings_pragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;


public class company_info extends Fragment {

    private EditText company;
    private Button change_btn_com;
    private View root;
    private ProgressDialog progressDialog;
    private config config =new config();
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_company_info, container, false);
        initview();
        return root;
    }

    private void initview(){
        try {
            company = root.findViewById(R.id.comp);
            change_btn_com = root.findViewById(R.id.change_btn_com);

            change_btn_com.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (company.getText().toString().trim().equals("")){
                        company.setError("Empty Company Name");
                        return;
                    }
                    submit(root.getContext().getApplicationContext(),v);
                }
            });
            getdata(root.getContext().getApplicationContext());
        }catch (Exception e){
        }
    }

    private void submit(Context context,View v){
        try {
            progressDialog = ProgressDialog.show(root.getContext(),"","Loading...",false,false);
            String sql = "UPDATE merchant_acc SET Businessname = '"+company.getText().toString().trim()+"'" +
                    " WHERE userid = '"+config.getUser(context)+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {
                                config.regularSnak(v,"Successfully Updated");
                            } else {
                                config.regularSnak(v,"Unsuccessful");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
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
    }

    private void getdata(Context context){
        try {
            String sql = "SELECT Businessname AS 'id' FROM merchant_acc WHERE userid = '"+config.getUser(context)+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals(""))
                                company.setText(response.trim());
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
        }catch (Exception e){
        }
    }
}
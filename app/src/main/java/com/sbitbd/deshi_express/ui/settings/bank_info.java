package com.sbitbd.deshi_express.ui.settings;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class bank_info extends Fragment {

    private View root;
    private config config = new config();
    private ProgressDialog progressDialog;
    private EditText bank_name,branch_name,ac_holder,bank_ac;
    private Button change_btn_com;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_bank_info, container, false);
        initview();
        return root;
    }

    private void initview(){
        try {
            bank_name = root.findViewById(R.id.bank_name);
            branch_name = root.findViewById(R.id.branch_name);
            ac_holder = root.findViewById(R.id.ac_holder);
            bank_ac = root.findViewById(R.id.bank_ac);
            change_btn_com = root.findViewById(R.id.change_btn_b);

            change_btn_com.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(root.getContext().getApplicationContext(),v);
                }
            });
            getdata();
        }catch (Exception e){
        }
    }

    private void submit(Context context, View v){
        try {
            progressDialog = ProgressDialog.show(root.getContext(),"","Loading...",false,false);
            String sql = "UPDATE merchant_acc SET bank_name = '"+bank_name.getText().toString().trim()+"'" +
                    ",branch_name='"+branch_name.getText().toString().trim()+"',holder_name = " +
                    "'"+ac_holder.getText().toString().trim()+"',bank_acc='"+bank_ac.getText().toString().trim()+"'" +
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

    private void getdata(){
        try {
            String sql = "SELECT bank_name AS 'one',branch_name AS 'two',holder_name AS 'three'," +
                    "bank_acc AS 'four' FROM merchant_acc WHERE userid = '"+config.getUser(root.getContext().getApplicationContext())+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showdata(response.trim());
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
        }catch (Exception e){
        }
    }

    protected void showdata(String response) {
        String one="",two="",three="",four="";
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
                    bank_name.setText(one);
                    branch_name.setText(two);
                    ac_holder.setText(three);
                    bank_ac.setText(four);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
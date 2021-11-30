package com.sbitbd.deshi_express.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.login.login;
import com.sbitbd.deshi_express.model.identity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reg_controller {

    private EditText business, name, address, contact, email_r, pass, con_pass;
    private Context context;
    private String generated_id,encripted_pass;

    protected boolean validation(Context context, EditText business, EditText name, EditText address, EditText contact,
                                 EditText email_r, EditText pass, EditText con_pass, AutoCompleteTextView division
            , AutoCompleteTextView district, AutoCompleteTextView thana) {
        this.business = business;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.email_r = email_r;
        this.pass = pass;
        this.con_pass = con_pass;
        this.context = context;
        try {
            if (business.getText().toString().trim().equals("")) {
                business.setError("Empty Business Name");
                business.requestFocus();
                Toast.makeText(context, "Empty Business Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (name.getText().toString().trim().equals("")) {
                name.setError("Empty Name");
                name.requestFocus();
                Toast.makeText(context, "Empty Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (address.getText().toString().trim().equals("")) {
                address.setError("Empty Address");
                address.requestFocus();
                Toast.makeText(context, "Empty Address", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (contact.getText().toString().trim().equals("")) {
                contact.setError("Empty Contact");
                contact.requestFocus();
                Toast.makeText(context, "Empty Contact", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (email_r.getText().toString().trim().equals("")) {
                email_r.setError("Empty Email");
                email_r.requestFocus();
                Toast.makeText(context, "Empty Email", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (division.getText().toString().trim().equals("")) {
                division.setError("Select Division");
                division.requestFocus();
                Toast.makeText(context, "Select Division", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (district.getText().toString().trim().equals("")) {
                district.setError("Select District");
                district.requestFocus();
                Toast.makeText(context, "Select District", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (thana.getText().toString().trim().equals("")) {
                thana.setError("Select Thana");
                thana.requestFocus();
                Toast.makeText(context, "Select Thana", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (pass.getText().toString().trim().equals("")) {
                pass.setError("Empty Password");
                pass.requestFocus();
                Toast.makeText(context, "Empty Password", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (pass.getText().toString().length() < 5) {
                pass.setError("Password must be >= 5 character");
                pass.requestFocus();
                Toast.makeText(context, "Password must be >= 5 character", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!pass.getText().toString().equals(con_pass.getText().toString())) {
                con_pass.setError("Password didn't matched");
                con_pass.requestFocus();
                Toast.makeText(context, "Password didn't matched", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }

    protected String generate_submit(String uid) {
        String prefix;
        int id;
        try {
            prefix = "M";
            id = Integer.parseInt(uid);
                id++;
                if (id <= 9) {
                    return (prefix + "0000" + "" + Long.toString(id));

                } else if (id <= 99) {
                    return (prefix + "000" + "" + Long.toString(id));
                } else if (id <= 999) {
                    return (prefix + "00" + "" + Long.toString(id));
                } else if (id <= 9999) {
                    return (prefix + "0" + "" + Long.toString(id));
                } else if (id <= 99999) {
                    return (prefix + "" + "" + Long.toString(id));
                }

        }catch (Exception e){
        }
        return null;
    }

    protected void initUser(ProgressDialog progressDialog,String division,String district,String thana){
        try {
            String sql="SELECT SUBSTR(MAX(userid),2) AS 'id' FROM merchant_acc";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("")){
                                generated_id = generate_submit(response.trim());
                                pass_gen(progressDialog,division,district,thana);
//                                encripted_pass =
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
        }catch (Exception e){
        }
    }

    protected void showAuto(String response,Context context, AutoCompleteTextView autoCompleteTextView,
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
                    R.layout.item_name,strings);
            autoCompleteTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    protected void check_phone_email(ProgressDialog progressBar,String division,String district,String thana) {
        try {
            String sql = "SELECT id FROM merchant_acc WHERE contactnumber = '"+contact.getText()
                    .toString().trim()+"' OR email = '"+email_r.getText().toString().trim()+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                Toast.makeText(context, "Phone or Email Already taken!", Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            } else {
                                initUser(progressBar,division,district,thana);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.dismiss();
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

    protected void pass_gen(ProgressDialog progressBar,String division,String district,String thana) {
        try {
            String sql = pass.getText().toString().trim();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PASS_HASH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                insertUser(progressBar,division,district,thana,response.trim());
                            } else {
                                progressBar.dismiss();
                                Toast.makeText(context, "Password generation failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.dismiss();
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

    protected void insertUser(ProgressDialog progressBar,String division,String district,String thana,String en_pass) {
        try {
            String sql = "INSERT INTO merchant_acc (userid,Businessname,ownername,contactnumber,Address," +
                    "country,Division,District,thana,email,`password`,set_password,`status`," +
                    "companynumber,created_at,updated_at,fixed_zone_status) VALUES ('"+generated_id+"'" +
                    ",'"+business.getText().toString().trim()+"','"+name.getText().toString().trim()+"'" +
                    ",'"+contact.getText().toString().trim()+"','"+address.getText().toString().trim()+"'," +
                    "'1','"+division+"','"+district+"','"+thana+"','"+email_r.getText().toString().trim()+"'" +
                    ",'"+en_pass+"','"+pass.getText().toString()+"','0','',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0')";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.dismiss();
                            if (response.trim().equals("")) {
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                                dialogBuilder.setIcon(R.drawable.ic_check);
                                dialogBuilder.setTitle("Registration Successful");
                                dialogBuilder.setMessage("Please wait for Approve.");
                                dialogBuilder.setCancelable(false);
                                dialogBuilder.setPositiveButton("OK",(dialog, which) -> {
                                    Intent intent = new Intent(context, login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                });
                                dialogBuilder.show();
                            } else {
                                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.dismiss();
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

}

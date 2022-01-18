package com.sbitbd.deshi_express.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.deshi_express.Adapter.state_rep_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.state_rep_model;
import com.sbitbd.deshi_express.model.two_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private config config = new config();
    ProgressDialog progressDialog;
    private List<two_model> request_data = new ArrayList<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public void update_home_data(Context context, state_rep_adapter state_rep_adapter, state_rep_model state_rep_model, String sql) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                state_rep_model.setCount(response.trim());
                                state_rep_adapter.updateUser(state_rep_model);
                            } else {
                                state_rep_model.setCount("0");
                                state_rep_adapter.updateUser(state_rep_model);
                            }
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
    }

    public void Update_address(String data, Context context) {
        try {
            String sql = "UPDATE merchant_acc set Address ='" + data + "' WHERE userid = '" + config.getUser(context) + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

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

    }

    public void getInfo(Context context, String did, String note, String estimate) {
        try {

            String sql = "SELECT SUBSTR(MAX(id),4) AS 'id' FROM pickup_req";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.trim().equals("")) {
                                String pid = config.generatePickup_id(response.trim(), "PU-");
                                pickup_insert(context, did, pid, note, estimate);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
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

    public void generate_pickup_agent(Context context, String did, String pid, String note, String estimate) {
        try {

            String sql = "SELECT SUBSTR(MAX(id),5) AS 'id' FROM pickup_req_agent";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.trim().equals("")) {
                                pickup_agent_insert(context, did, pid, config.generatePickup_agent_id(response.trim()), note, estimate);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
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

    public void request(Context context, String note, String estimate) {
        try {
            progressDialog = ProgressDialog.show(context, "", "Loading...", false, false);
            String sql = "select id from pickup_req_agent where req_date = current_date and merchant_id" +
                    " = '"+config.getUser(context)+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("")) {
                                save_data(response.trim(), context, note, estimate);
                            } else {
                                progressDialog.dismiss();
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context, R.style.RoundShapeTheme);
                                dialogBuilder.setTitle("Pickup Request already sent");
                                dialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {

                                });
                                dialogBuilder.show();
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

    protected void save_data(String response, Context context, String note, String estimate) {
//        String id = "";
//        JSONObject jsonObject1 = new JSONObject();
//        two_model cat_models;
        try {
//            request_data.clear();
//            JSONObject jsonObject = new JSONObject(response);
//            JSONArray result = jsonObject.getJSONArray(config.RESULT);
//            for (int i = 0; i <= result.length(); i++) {
//                try {
//                    JSONObject collegeData = result.getJSONObject(i);
//                    id = collegeData.getString(config.ONE);
////                    cat_models = new two_model(id,"");
////                    request_data.add(cat_models);
//                    jsonObject1.put("" + i, "UPDATE delivery SET pickup_req_status = '0' " +
//                            "where merchantId = '"+config.getUser(context)+"' and id = '"+id+"'");
//                } catch (Exception e) {
//                }
//            }
            getInfo(context, "", note, estimate);
        } catch (Exception e) {
        }
    }

    private void add_json(JSONObject jsonObject, Context context) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ARRAY_ADD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if (response.trim().equals("")) {
//                                Toast.makeText(context, "Successful!", Toast.LENGTH_LONG).show();
//
//                            } else {
//                                Toast.makeText(context, response.trim(), Toast.LENGTH_LONG).show();
//                            }
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {

                            } else {

                                Toast.makeText(context, "Failed delivery update", Toast.LENGTH_SHORT).show();
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
                    params.put(config.QUERY, jsonObject.toString());
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

    protected void pickup_insert(Context context, String did, String pid, String note, String estimate) {
        try {
            String sql = "INSERT INTO pickup_req (id,merchant_id,invoice_id,note,estimated,status," +
                    "created_at,updated_at) VALUES ('" + pid + "','" + config.getUser(context) + "','" + did + "'," +
                    "'" + note + "','" + estimate + "','1',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("")) {
                                generate_pickup_agent(context, did, pid, note, estimate);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Failed Pickup insert", Toast.LENGTH_SHORT).show();
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

    protected void pickup_agent_insert(Context context, String did, String pid, String paid, String note, String estimate) {
        try {

            String sql = "INSERT INTO pickup_req_agent (id,pickup_id,agent_id,merchant_id,invoice_id,note," +
                    "estimated,req_date,status,created_at,updated_at) VALUES ('" + paid + "','" + pid + "'," +
                    "'" + config.getAgent(context) + "','" + config.getUser(context) + "','" + did + "'," +
                    "'" + note + "','" + estimate + "',current_date,'0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {
//                                delivery_update(context, did);
//                                add_json(jsonObject,context);
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                                dialogBuilder.setTitle("Pickup Request Sent!");
                                dialogBuilder.setIcon(R.drawable.ic_check);
                                dialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {
                                    dialogInterface.cancel();
                                });
                                dialogBuilder.show();
                            } else {
                                Toast.makeText(context, "Failed Pickup agent insert", Toast.LENGTH_SHORT).show();
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

    protected void delivery_update(Context context, String did) {
        try {

            String sql = "UPDATE delivery SET pickup_req_status = '0' WHERE id = '" + did + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                                dialogBuilder.setTitle("Pickup Request Sent!");
                                dialogBuilder.setIcon(R.drawable.ic_check);
                                dialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {
                                    dialogInterface.cancel();
                                });
                                dialogBuilder.show();
                            } else {

                                Toast.makeText(context, "Failed delivery update", Toast.LENGTH_SHORT).show();
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

    protected void payment_insert(Context context, String id, String amount, int method) {
        try {

            String sql = "INSERT INTO payment_req (id,merchant_id,payment_type,req_amount,status," +
                    "created_at,updated_at)VALUES('" + id + "','" + config.getUser(context) + "','" + method + "'" +
                    ",'" + amount + "','0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.trim().equals("")) {
                                merchant_invoice_update(context);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Failed Payment insert", Toast.LENGTH_SHORT).show();
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

    protected void generate_payment(Context context, String amount, int method) {
        try {

            String sql = "SELECT SUBSTR(MAX(id),4) AS 'id' FROM payment_req";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.trim().equals("")) {
                                payment_insert(context, config.generatePickup_id(response.trim(), "PR-"), amount, method);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
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

    public void get_pay_amount(Context context, int method) {
        try {
            progressDialog = ProgressDialog.show(context, "", "Loading...", false, false);
            String sql = "SELECT SUM(coll_amount-total_charge) as 'id' from merchant_invoice_charge " +
                    "WHERE merchant_id = '" + config.getUser(context) + "' and paid_status = '0' and payment_req_status ='0'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.trim().equals("")) {
                                generate_payment(context, response.trim(), method);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Balance not found!", Toast.LENGTH_SHORT).show();
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

    protected void merchant_invoice_update(Context context) {
        try {

            String sql = "UPDATE merchant_invoice_charge SET payment_req_status = '1' WHERE " +
                    "merchant_id = '" + config.getUser(context) + "' and paid_status = '0' and payment_req_status ='0'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                                dialogBuilder.setTitle("Payment Request Sent!");
                                dialogBuilder.setIcon(R.drawable.ic_check);
                                dialogBuilder.setPositiveButton("Ok", (dialogInterface, i) -> {
                                    dialogInterface.cancel();
                                });
                                dialogBuilder.show();
                            } else {

                                Toast.makeText(context, "Failed merchant invoice update", Toast.LENGTH_SHORT).show();
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

    public void get_shipment(Context context, String text, TextView textView, String sql) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.trim().equals("")) {
                                if (text.equals("Success"))
                                    textView.setText(text + " - " + response.trim() + "%");
                                else
                                    textView.setText(text + " - " + response.trim());
                            } else {
                                if (text.equals("Success"))
                                    textView.setText(text + " - 0%");
                                else
                                    textView.setText(text + " - 0");
                            }

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
    }

    public void get_balance(Context context, String text, TextView textView) {
        try {
            String sql = "SELECT SUM(coll_amount-total_amount) as 'one',count(id) as 'two' from merchant_invoice_charge " +
                    "WHERE merchant_id = '" + config.getUser(context) + "' and paid_status = '0' and payment_req_status ='0'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.TWO_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (!response.trim().equals("")) {
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    if (collegeData.getString(config.ONE).trim().equals("null") || collegeData.getString(config.ONE).trim().equals("")) {
                                        textView.setText(text + " - 0(0)");
                                    } else
                                        textView.setText(text + " - " + collegeData.getString(config.ONE)
                                                + "(" + collegeData.getString(config.TWO) + ")");
                                } else {
                                    textView.setText(text + " - 0(0)");
                                }
                            } catch (Exception e) {
                            }
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
    }


    public LiveData<String> getText() {
        return mText;
    }
}
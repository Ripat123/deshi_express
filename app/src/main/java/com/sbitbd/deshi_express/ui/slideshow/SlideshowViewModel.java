package com.sbitbd.deshi_express.ui.slideshow;

import android.content.Context;
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
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.model.four_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SlideshowViewModel extends ViewModel {

    private MutableLiveData<four_model> view_data;
    private MutableLiveData<four_model> rider_data;
    private MutableLiveData<four_model> track_date;
    private MutableLiveData<String> data;
    private MutableLiveData<Context> context;

    public SlideshowViewModel() {
        data = new MutableLiveData<String>();
    }

    private void getTextData() {
        try {
            String sql = "SELECT delivery.created_at as 'one', delivery.updated_at as 'two'," +
                    "delivery.position as 'three',delivery.pickup_req_status as 'four' from delivery" +
                    " where delivery.id = '" + data.getValue() + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if ((response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))) {
                                load(response.trim());
                            } else {
                                load("not found");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    load("not found");
                    Toast.makeText(context.getValue(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context.getValue());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void getassignData() {
        try {
            String sql = "SELECT delivery_reg.name as 'one',delivery_reg.mobile as 'two',districtinfo." +
                    "districtname as 'three' FROM delivery inner join delivery_reg on delivery." +
                    "deliveryman_id = delivery_reg.userid inner join districtinfo on delivery_reg." +
                    "District=districtinfo.id where delivery.id = '" + data.getValue() + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if ((response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))) {
                                rider_info(response.trim());
                            } else {
                                rider_info("not found");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    rider_info("not found");
                    Toast.makeText(context.getValue(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context.getValue());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void gettrackDate() {
        try {
            String sql = "SELECT created_at as 'one',pickup_date as 'two',success_date as 'three' from delivery where delivery.id = '" + data.getValue() + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if ((response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))) {
                                date_info(response.trim());
                            } else {
                                date_info("not found");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    date_info("not found");
                    Toast.makeText(context.getValue(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context.getValue());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    protected MutableLiveData<String> getData() {
        data = new MutableLiveData<String>();
        return data;
    }

    protected MutableLiveData<Context> getcontext() {
        context = new MutableLiveData<Context>();
        return context;
    }

    protected LiveData<four_model> getViewData() {
        view_data = new MutableLiveData<four_model>();
        getTextData();

        return view_data;
    }

    protected LiveData<four_model> getRiderData() {
        rider_data = new MutableLiveData<four_model>();
        getassignData();

        return rider_data;
    }

    protected LiveData<four_model> gettrack_date() {
        track_date = new MutableLiveData<four_model>();
        gettrackDate();

        return track_date;
    }

    private void load(String response) {
        String one = "", two = "", three = "", four = "";
        four_model six_model;
        try {
            if (!response.equals("not found")) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(config.RESULT);
                for (int i = 0; i <= result.length(); i++) {
                    try {
                        JSONObject collegeData = result.getJSONObject(i);
                        one = collegeData.getString(config.ONE);
                        two = collegeData.getString(config.TWO);
                        three = collegeData.getString(config.THREE);
                        four = collegeData.getString(config.FOUR);
                        six_model = new four_model(one, two, three, four);
                        view_data.setValue(six_model);

                    } catch (Exception e) {
                    }
                }
            } else {
                view_data.setValue(null);
            }
        } catch (Exception e) {
        }
    }

    private void rider_info(String response) {
        String one = "", two = "", three = "";
        four_model six_model;
        try {
            if (!response.equals("not found")) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(config.RESULT);
                for (int i = 0; i <= result.length(); i++) {
                    try {
                        JSONObject collegeData = result.getJSONObject(i);
                        one = collegeData.getString(config.ONE);
                        two = collegeData.getString(config.TWO);
                        three = collegeData.getString(config.THREE);
                        six_model = new four_model(one, two, three, "");
                        rider_data.setValue(six_model);

                    } catch (Exception e) {
                    }
                }
            } else {
                rider_data.setValue(null);
            }
        } catch (Exception e) {
        }
    }

    private void date_info(String response) {
        String one = "", two = "", three = "";
        four_model six_model;
        try {
            if (!response.equals("not found")) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(config.RESULT);
                for (int i = 0; i <= result.length(); i++) {
                    try {
                        JSONObject collegeData = result.getJSONObject(i);
                        one = collegeData.getString(config.ONE);
                        two = collegeData.getString(config.TWO);
                        three = collegeData.getString(config.THREE);
                        six_model = new four_model(one, two, three, "");
                        track_date.setValue(six_model);

                    } catch (Exception e) {
                    }
                }
            } else {
                track_date.setValue(null);
            }
        } catch (Exception e) {
        }
    }
}
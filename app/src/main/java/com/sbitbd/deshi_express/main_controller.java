package com.sbitbd.deshi_express;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sbitbd.deshi_express.Adapter.sheba_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.model.home_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class main_controller {

    public void inithome(Context context, sheba_adapter sheba_adapter, RecyclerView recyclerView,String sql) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                showJSON(response.trim(),sheba_adapter);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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
            recyclerView.setAdapter(sheba_adapter);
        } catch (Exception e) {
        }
    }

    public void showJSON(String response, sheba_adapter cart_adapter) {
        String name = "";
        String img = "";
        String title = "";
        String id = "";
        home_model cart_model;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.DESCRIPTION);
                img = collegeData.getString(config.PRO_IMAGE);
                title = collegeData.getString(config.TITLE);
                id = collegeData.getString(config.ID);
                cart_model = new home_model(id,title,name,img);
                cart_adapter.addUser(cart_model);
            }
        } catch (Exception e) {
        }
    }

    public void regularSnak(String msg,View v){
        Snackbar snackbar = Snackbar.make(v,msg, Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }
}

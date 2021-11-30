package com.sbitbd.deshi_express.ui.return_order;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.deshi_express.Adapter.return_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.return_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class return_order extends Fragment {

    private View root;
    private return_adapter return_adapter;
    private return_model return_model;
    private RecyclerView recyclerView;
    private config config = new config();
    private ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_return_order, container, false);
        initview();
        return root;
    }
    private void initview(){
        try {
            recyclerView = root.findViewById(R.id.return_rec);
            constraintLayout = root.findViewById(R.id.not_lay);

            GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1);
            recyclerView.setLayoutManager(manager);
            return_adapter = new return_adapter(root.getContext().getApplicationContext(),0);

            initReturn("SELECT id AS 'one',updated_at AS 'two',customer_name AS 'three' FROM delivery" +
                    "  where merchantId = '" + config.getUser(root.getContext().getApplicationContext()) + "'" +
                    "   and position = '11'");
            recyclerView.setAdapter(return_adapter);
        }catch (Exception e){
        }
    }

    private void initReturn(String sql) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                show_return_data(response.trim());
                            else
                                constraintLayout.setVisibility(View.VISIBLE);
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

    private void show_return_data(String response) {
        String one = "", two = "", three = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    one = collegeData.getString(config.ONE);
                    two = collegeData.getString(config.TWO);
                    three = collegeData.getString(config.THREE);
                    return_model = new return_model(one, two, three, one);
                    return_adapter.addUser(return_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
}
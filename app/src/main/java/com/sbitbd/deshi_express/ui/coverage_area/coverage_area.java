package com.sbitbd.deshi_express.ui.coverage_area;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.deshi_express.Adapter.single_adapter;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.home_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class coverage_area extends Fragment {

    private CoverageAreaViewModel mViewModel;
    private View root;
    private RecyclerView recyclerView;
    private EditText search;
    private single_adapter single_adapter;
    private home_model home_model;
    private ProgressDialog progressDialog;

    public static coverage_area newInstance() {
        return new coverage_area();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CoverageAreaViewModel.class);
        root = inflater.inflate(R.layout.coverage_area_fragment, container, false);
        initview();
        return root;
    }
    private void initview(){
        try {
            recyclerView = root.findViewById(R.id.area_rec);
            search = root.findViewById(R.id.dis_search);

            GridLayoutManager manager = new GridLayoutManager(root.getContext().getApplicationContext(), 1);
            recyclerView.setLayoutManager(manager);
            recyclerView.setNestedScrollingEnabled(false);
            single_adapter = new single_adapter(root.getContext().getApplicationContext());

            setAutoComplete("SELECT id AS 'one',districtname AS 'two' FROM districtinfo");
            recyclerView.setAdapter(single_adapter);

            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        View view = getActivity().getCurrentFocus();
                        hideKeyboardFrom(root.getContext().getApplicationContext(), view);

                        single_adapter.Clear();
                        setAutoComplete("SELECT id AS 'one',districtname AS 'two' FROM districtinfo " +
                                "where districtname like '%"+search.getText().toString().trim()+"%'");
                        return true;
                    }
                    return false;
                }
            });
        }catch (Exception e){
        }
    }

    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setAutoComplete(String sql) {
        progressDialog = ProgressDialog.show(root.getContext(),"","Loading...",false,false);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.TWO_DIMENSION,
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
    }

    protected void showAuto(String response) {
        String id = "";
        String name = "";
        home_model home_model;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    name = collegeData.getString(config.TWO);
                    id = collegeData.getString(config.ONE);
                    home_model = new home_model(id, name,"","");
                    single_adapter.addUser(home_model);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

}
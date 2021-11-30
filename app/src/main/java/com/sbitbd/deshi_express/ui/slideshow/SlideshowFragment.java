package com.sbitbd.deshi_express.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.four_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private TextView order_id,order_date,last_up,assign_name,assign_phone,assign_zone,
            track_first_date, track_second_date,track_six_date, track_eight_date;
    private ConstraintLayout tracking_layout;
    protected ImageView track_checkimg_1, track_checkimg_2, track_checkimg_3, track_checkimg_4, track_checkimg_5,
            track_checkimg_6, track_checkimg_7, track_checkimg_8;
    private ProgressDialog progressDialog;
    private View root;
    private Button track_btn;
    private AutoCompleteTextView track_id;
    private TextInputLayout textInputLayout;
    private config config = new config();
    private List<String> strings = new ArrayList<>();
    private List<String> tempstrings = new ArrayList<>();
    private List<four_model> four_list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        initview();
        return root;
    }

    private void initview(){
        try {
            order_id = root.findViewById(R.id.o_id);
            order_date = root.findViewById(R.id.order_date);
            last_up = root.findViewById(R.id.last_up);
            assign_name = root.findViewById(R.id.assign_name);
            assign_phone = root.findViewById(R.id.assign_phone);
            assign_zone = root.findViewById(R.id.assign_zone);
            track_first_date = root.findViewById(R.id.first_date);
            track_second_date = root.findViewById(R.id.second_date);
            track_six_date = root.findViewById(R.id.sixth_date);
            track_eight_date = root.findViewById(R.id.eighth_date);
            tracking_layout = root.findViewById(R.id.tracking_layout);
            track_checkimg_1 = root.findViewById(R.id.checked_img);
            track_checkimg_2 = root.findViewById(R.id.checked_img1);
            track_checkimg_3 = root.findViewById(R.id.checked_img2);
            track_checkimg_4 = root.findViewById(R.id.checked_img3);
            track_checkimg_5 = root.findViewById(R.id.checked_img4);
            track_checkimg_6 = root.findViewById(R.id.checked_img5);
            track_checkimg_7 = root.findViewById(R.id.checked_img6);
            track_checkimg_8 = root.findViewById(R.id.checked_img7);
            track_btn = root.findViewById(R.id.reg_submit);
            track_id = root.findViewById(R.id.invoice);
            textInputLayout = root.findViewById(R.id.invoice_lay);

            track_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (track_id.getText().toString().trim().equals("")){
                        textInputLayout.setError("Empty Order ID.");
                        return;
                    }
                    else
                        textInputLayout.setErrorEnabled(false);
                    CONFIG(track_id.getText().toString().trim());
                }
            });
            setAutoComplete("select customer_name AS 'one',customer_phone as 'two',id as 'three' " +
                    "from delivery where merchantId = " +
                    "'"+config.getUser(root.getContext().getApplicationContext())+"'",track_id,strings);
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tempstrings.clear();
                    getPosition(track_id.getText().toString().trim().toLowerCase());
                    track_id.setThreshold(1);
                }
            };
            track_id.addTextChangedListener(textWatcher);
        }catch (Exception e){
        }
    }

    public void getPosition(String s){
        try {
            for (four_model x:four_list){
                if(x.getOne().toLowerCase().equals(s) || x.getTwo().toLowerCase().equals(s) || x.getThree().toLowerCase().equals(s)){
                    tempstrings.add(x.getThree());
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(root.getContext().getApplicationContext(),
                    R.layout.item_name, tempstrings);
            track_id.setAdapter(dataAdapter);
            track_id.setThreshold(1);
            track_id.showDropDown();
//            track_id.setValidator();
        }catch (Exception e){
        }
    }

    private void CONFIG(String id) {
        try {
            progressDialog = ProgressDialog.show(root.getContext(), "", "Loading", false, false);
            tracking_layout.setVisibility(View.GONE);
            reset();
            slideshowViewModel.getData().setValue(id);
            slideshowViewModel.getcontext().setValue(root.getContext().getApplicationContext());
            slideshowViewModel.getViewData().observe(getViewLifecycleOwner(), new Observer<four_model>() {
                @Override
                public void onChanged(@Nullable four_model four_model) {
                    progressDialog.dismiss();
                    if (four_model != null) {
                        String position = "";
                        order_id.setText("Order ID: "+id);
                        order_date.setText("Order Date: "+four_model.getOne());
                        last_up.setText("Last Update: "+four_model.getTwo());

                        position = four_model.getThree();
                        position_setup(position);
                        pickup_setup(four_model.getFour());
                        tracking_layout.setVisibility(View.VISIBLE);
                    }
                    else
                        Toast.makeText(root.getContext().getApplicationContext(),"not found",Toast.LENGTH_SHORT).show();
                }
            });
            slideshowViewModel.getRiderData().observe(getViewLifecycleOwner(), new Observer<four_model>() {
                @Override
                public void onChanged(four_model four_model) {
                    if (four_model != null) {
                        assign_name.setText("Rider Name: "+four_model.getOne());
                        assign_phone.setText("Rider Phone: "+four_model.getTwo());
                        assign_zone.setText("Rider Zone: "+four_model.getThree());
                    }
                }
            });

            slideshowViewModel.gettrack_date().observe(getViewLifecycleOwner(), new Observer<four_model>() {
                @Override
                public void onChanged(four_model four_model) {
                    if (four_model != null) {
                        if (!four_model.getOne().equals("") && !four_model.getOne().equals("null")) {
                            track_first_date.setText(four_model.getOne());
                            track_second_date.setText(four_model.getOne());
                        }
                        if (!four_model.getTwo().equals("") && !four_model.getTwo().equals("null"))
                            track_six_date.setText(four_model.getTwo());
                        if (!four_model.getThree().equals("") && !four_model.getThree().equals("null"))
                            track_eight_date.setText(four_model.getThree());
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private void position_setup(String position) {
        try {
            switch (position) {
                case "2":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    break;
                case "5":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    track_checkimg_6.setVisibility(View.VISIBLE);
                    break;
                case "6":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    track_checkimg_6.setVisibility(View.VISIBLE);
                    track_checkimg_7.setVisibility(View.VISIBLE);
                    break;
                case "7":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    track_checkimg_6.setVisibility(View.VISIBLE);
                    track_checkimg_7.setVisibility(View.VISIBLE);
                    track_checkimg_8.setVisibility(View.VISIBLE);
                    break;
                case "8":
                    track_checkimg_1.setVisibility(View.GONE);
                    break;
            }
        } catch (Exception e) {
        }
    }

    private void pickup_setup(String position) {
        try {
            if (position.equals("1")) {
                track_checkimg_3.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }
    }

    private void reset(){
        try {
            order_id.setText("");
            order_date.setText("");
            last_up.setText("");
            track_first_date.setText("");
            track_second_date.setText("");
            track_six_date.setText("");
            track_eight_date.setText("");
            assign_name.setText("");
            assign_phone.setText("");
            assign_zone.setText("");
            track_checkimg_1.setVisibility(View.GONE);
            track_checkimg_2.setVisibility(View.GONE);
            track_checkimg_3.setVisibility(View.GONE);
            track_checkimg_4.setVisibility(View.GONE);
            track_checkimg_5.setVisibility(View.GONE);
            track_checkimg_6.setVisibility(View.GONE);
            track_checkimg_7.setVisibility(View.GONE);
            track_checkimg_8.setVisibility(View.GONE);
        }catch (Exception e){
        }
    }

    private void setAutoComplete(String sql, AutoCompleteTextView autoCompleteTextView, List<String> name) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showAuto(response.trim(), root.getContext().getApplicationContext(), autoCompleteTextView,
                                        name);
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
                            List<String> strings) {
        String name = "";
        try {
            strings.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    name = collegeData.getString(config.THREE);
                    strings.add(name);
                    four_list.add(new four_model(collegeData.getString(config.ONE),collegeData
                            .getString(config.TWO),name,collegeData.getString(config.FOUR)));
                } catch (Exception e) {
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                    R.layout.item_name, strings);
            autoCompleteTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }
}
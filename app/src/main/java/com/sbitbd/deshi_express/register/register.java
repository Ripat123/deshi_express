package com.sbitbd.deshi_express.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sbitbd.deshi_express.login.login;
import com.sbitbd.deshi_express.model.identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class register extends AppCompatActivity {
    private ImageView close;
    private Button login_s, reg;
    private EditText business, name, address, contact, email_r, pass, con_pass;
    private AutoCompleteTextView division, district, thana;
    private reg_controller reg_controller = new reg_controller();
    private List<identity> division_list = new ArrayList<>();
    private List<String> division_name = new ArrayList<>();
    private List<identity> district_list = new ArrayList<>();
    private List<String> district_name = new ArrayList<>();
    private List<identity> thana_list = new ArrayList<>();
    private List<String> thana_name = new ArrayList<>();
    private identity identity;
    private String division_id,district_id,thana_id;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initview();
    }

    private void initview() {
        try {
            close = findViewById(R.id.reg_back);
            login_s = findViewById(R.id.login_s);
            reg = findViewById(R.id.reg_submit);
            business = findViewById(R.id.business);
            name = findViewById(R.id.name);
            address = findViewById(R.id.address);
            contact = findViewById(R.id.contact);
            email_r = findViewById(R.id.email_r);
            pass = findViewById(R.id.pass);
            con_pass = findViewById(R.id.con_pass);
            division = findViewById(R.id.division);
            district = findViewById(R.id.district);
            thana = findViewById(R.id.thana);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            login_s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(register.this, login.class));
                    finish();
                }
            });
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();
                }
            });
            setAutoComplete("SELECT id AS 'one',divisioname AS 'two'"+
                    " FROM divisioninfo",division,division_name,division_list);

            division.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    identity = division_list.get(position);
                    division_id = identity.getId();
                    setAutoComplete("SELECT id AS 'one',districtname AS 'two'" +
                            " FROM districtinfo WHERE `fk_division_id` = '"+division_id+"'",district,district_name,district_list);
                    district.setText("");
                }
            });
            district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    identity = district_list.get(position);
                    district_id = identity.getId();
                    setAutoComplete("SELECT id AS 'one',upozillaname AS 'two'" +
                            " FROM upozillainfo WHERE `fk_division_id` = '"+division_id+"' AND " +
                            "fk_district_id = '"+district_id+"'",thana,thana_name,thana_list);
                    thana.setText("");
                }
            });
            thana.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    identity = thana_list.get(position);
                    thana_id = identity.getId();
                }
            });
        } catch (Exception e) {
        }
    }

    private void submit() {
        try {
            if (reg_controller.validation(register.this, business, name, address, contact,
                    email_r, pass, con_pass, division, district, thana)) {
                progressDialog = ProgressDialog.show(register.this, "", "Processing...", false, false);
                reg_controller.check_phone_email(progressDialog,division_id,district_id,thana_id);
            }
        } catch (Exception e) {
        }
    }

    private void setAutoComplete(String sql,AutoCompleteTextView autoCompleteTextView,List<String> name,List<identity> list) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.TWO_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                reg_controller.showAuto(response.trim(),register.this,autoCompleteTextView,
                                        name,list);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(register.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(register.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }
}
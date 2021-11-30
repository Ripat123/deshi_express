package com.sbitbd.deshi_express.ui.settings.settings_pragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.sbitbd.deshi_express.model.identity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class owner_info extends Fragment {

    private EditText owner_name_id,address,contact,email_r;
    private AutoCompleteTextView division,district,thana;
    private View root;
    private Button change_btn;
    private config config =new config();
    private List<identity> division_list = new ArrayList<>();
    private List<String> division_name = new ArrayList<>();
    private List<identity> district_list = new ArrayList<>();
    private List<String> district_name = new ArrayList<>();
    private List<identity> thana_list = new ArrayList<>();
    private List<String> thana_name = new ArrayList<>();
    private identity identity;
    private String division_id,district_id,thana_id;
    private ProgressDialog progressDialog;
    private String sql;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_owner_info, container, false);
        initview();
        return root;
    }
    private void initview(){
        try {
            owner_name_id = root.findViewById(R.id.owner_name_id);
            address = root.findViewById(R.id.address);
            contact = root.findViewById(R.id.contact);
            email_r = root.findViewById(R.id.email_r);
            division = root.findViewById(R.id.division);
            district = root.findViewById(R.id.district);
            thana = root.findViewById(R.id.thana);
            change_btn = root.findViewById(R.id.change_btn);

            change_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (owner_name_id.getText().toString().trim().equals("")) {
                        owner_name_id.setError("Empty Name");
                        owner_name_id.requestFocus();
                        Toast.makeText(root.getContext().getApplicationContext(), "Empty Name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (address.getText().toString().trim().equals("")) {
                        address.setError("Empty Address");
                        address.requestFocus();
                        Toast.makeText(root.getContext().getApplicationContext(), "Empty Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (contact.getText().toString().trim().equals("")) {
                        contact.setError("Empty Contact");
                        contact.requestFocus();
                        Toast.makeText(root.getContext().getApplicationContext(), "Empty Contact", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (email_r.getText().toString().trim().equals("")) {
                        email_r.setError("Empty Email");
                        email_r.requestFocus();
                        Toast.makeText(root.getContext().getApplicationContext(), "Empty Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (division.getText().toString().trim().equals("")) {
                        division.setError("Select Division");
                        division.requestFocus();
                        Toast.makeText(root.getContext().getApplicationContext(), "Select Division", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (district.getText().toString().trim().equals("")) {
                        district.setError("Select District");
                        district.requestFocus();
                        Toast.makeText(root.getContext().getApplicationContext(), "Select District", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (thana.getText().toString().trim().equals("")) {
                        thana.setError("Select Thana");
                        thana.requestFocus();
                        Toast.makeText(root.getContext().getApplicationContext(), "Select Thana", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (division_id == null || division_id.equals("")){
                        sql = "UPDATE merchant_acc SET ownername = '"+owner_name_id.getText().toString().trim()+"'," +
                                "Address='"+address.getText().toString().trim()+"',contactnumber = " +
                                "'"+contact.getText().toString().trim()+"'" +
                                ",email='"+email_r.getText().toString().trim()+"' WHERE userid = " +
                                "'"+config.getUser(root.getContext().getApplicationContext())+"'";
                    }
                    else {
                        sql = "UPDATE merchant_acc SET ownername = '"+owner_name_id.getText().toString().trim()+"'," +
                                "Address='"+address.getText().toString().trim()+"',contactnumber = " +
                                "'"+contact.getText().toString().trim()+"'" +
                                ",email='"+email_r.getText().toString().trim()+"', Division='"+division_id+"'" +
                                ",District='"+district_id+"',thana='"+thana_id+"' WHERE userid = " +
                                "'"+config.getUser(root.getContext().getApplicationContext())+"'";
                    }

                    submit(root.getContext().getApplicationContext(),v);
                }
            });

            getdata(root.getContext().getApplicationContext());
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
                    thana.setText("");
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
        }catch (Exception e){
        }
    }

    private void getdata(Context context){
        try {
            String sql = "SELECT ownername AS 'two',contactnumber AS 'three',Address AS 'four',email AS 'five'," +
                    "`districtinfo`.`districtname` AS 'one',`divisioninfo`.`divisioname` AS 'six',`upozillainfo`." +
                    "`upozillaname` AS 'seven' FROM merchant_acc INNER JOIN divisioninfo ON merchant_acc." +
                    "`Division`=divisioninfo.`id` INNER JOIN districtinfo ON `merchant_acc`.`District`=`districtinfo`.`id` " +
                    "INNER JOIN upozillainfo ON `merchant_acc`.`thana`=`upozillainfo`.`id` WHERE userid = '"+config.getUser(context)+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.EIGHT_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals(""))
                                showdata(response.trim());
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

    protected void showdata(String response) {
        String one="",two="",three="",four="",five="",six="",seven="";
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
                    five = collegeData.getString(config.FIVE);
                    six = collegeData.getString(config.SIX);
                    seven = collegeData.getString(config.SEVEN);

                } catch (Exception e) {
                }
            }
            owner_name_id.setText(two);
            district.setText(one);
            contact.setText(three);
            address.setText(four);
            email_r.setText(five);
            division.setText(six);
            thana.setText(seven);
        } catch (Exception e) {e.printStackTrace();
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
                                showAuto(response.trim(), root.getContext().getApplicationContext(),autoCompleteTextView,
                                        name,list);
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

    private void submit(Context context,View v){
        try {
            progressDialog = ProgressDialog.show(root.getContext(),"","Loading...",false,false);

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
}
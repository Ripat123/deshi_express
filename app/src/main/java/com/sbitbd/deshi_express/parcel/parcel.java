package com.sbitbd.deshi_express.parcel;

import static androidx.core.content.FileProvider.getUriForFile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.home_model;
import com.sbitbd.deshi_express.model.identity;
import com.sbitbd.deshi_express.success_view.success;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class parcel extends AppCompatActivity {

    private ImageView back, arrow, info_img;
    private ConstraintLayout constraintLayout;
    private MaterialCardView cardView;
    private TextView arrow_text, net_total_amount, ship_a, cod_a, pack_a, dis_a, cod_t;
    private AutoCompleteTextView district, thana, weight, packaging, service;
    private List<home_model> district_list = new ArrayList<>();
    private List<String> district_name = new ArrayList<>();
    private List<home_model> thana_list = new ArrayList<>();
    private List<String> thana_name = new ArrayList<>();
    private List<identity> weight_list = new ArrayList<>();
    private List<String> weight_name = new ArrayList<>();
    private List<home_model> package_list = new ArrayList<>();
    private List<String> package_name = new ArrayList<>();
    private String district_id, thana_id, weight_id, package_id;
    private config config = new config();
    private Button parcel_submit, select_btn;
    private identity identity;
    private home_model home_model;
    private EditText customer, phone, cod, address, note, customer_in;
    private double net_total, cod_p = 0, cod_amount;
    private List<String> service_name = new ArrayList<>();
    private String sql, delivery_id, file_name;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout refreshLayout;
    private int PIC_CROP = 1;
    private int CODE_GALLERY_REQUEST = 555;
    private Bitmap bitmap;
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    public final String APP_TAG = "external_files";
    File photoFile;
    private List<String> tempstrings = new ArrayList<>();
    private List<String> temp_thana_strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);
        initveiw();
    }

    private void initveiw() {
        try {
            back = findViewById(R.id.parcel_back);
            arrow = findViewById(R.id.arrow_p);
            constraintLayout = findViewById(R.id.expend);
            cardView = findViewById(R.id.menu_id);
            arrow_text = findViewById(R.id.net_total);
            district = findViewById(R.id.district);
            thana = findViewById(R.id.thana);
            weight = findViewById(R.id.weight);
            packaging = findViewById(R.id.package1);
            service = findViewById(R.id.service);
            customer = findViewById(R.id.customer);
            phone = findViewById(R.id.phone);
            cod = findViewById(R.id.cod);
            address = findViewById(R.id.address);
            note = findViewById(R.id.note);
            parcel_submit = findViewById(R.id.parcel_submit);
            net_total_amount = findViewById(R.id.net_total_amount);
            ship_a = findViewById(R.id.ship_a);
            cod_a = findViewById(R.id.cod_a);
            pack_a = findViewById(R.id.pack_a);
            dis_a = findViewById(R.id.dis_a);
            cod_t = findViewById(R.id.cod_t);
            info_img = findViewById(R.id.info_img);
            select_btn = findViewById(R.id.select_btn);
            customer_in = findViewById(R.id.customer_in);
            refreshLayout = findViewById(R.id.refresh);

//            final ImagePopup imagePopup = new ImagePopup(this);
////            imagePopup.setWindowHeight(800); // Optional
////            imagePopup.setWindowWidth(800); // Optional
//            imagePopup.setBackgroundColor(Color.BLACK);  // Optional
//            imagePopup.setFullScreen(true); // Optional
//            imagePopup.setImageOnClickClose(true);  // Optional
//
////            ImageView imageView = findViewById(R.id.imageView);
//
//            imagePopup.initiatePopup(info_img.getDrawable());
////            imagePopup.initiatePopupWithGlide(DELIVERY_ADDRESS_IMG + id + ".png");
//
//            info_img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    /** Initiate Popup view **/
//                    imagePopup.viewPopup();
//
//                }
//            });

            config.resetSession(parcel.this);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    clean();
                    refreshLayout.setRefreshing(false);
                }
            });
            select_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.R)
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(
                            parcel.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.
                                    CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
//                    selectImage();

                }
            });
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (constraintLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
                    }
                }
            });
            arrow_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (constraintLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_round_keyboard_arrow_up_24);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        constraintLayout.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_round_keyboard_arrow_down_24);
                    }
                }
            });
            district_name.clear();
            weight_name.clear();
            package_name.clear();
            service_name.clear();
            setthrid_info("SELECT id AS 'one',districtname AS 'two',districtname_bangla as 'three'" +
                    " FROM districtinfo order by districtname_bangla asc", district, district_name, district_list, 1);
            setAutoComplete("SELECT id AS 'one',title AS 'two'" +
                    " FROM weight_title order by sl desc", weight, weight_name, weight_list, 2);
            setpackage(" SELECT id AS 'one', title AS 'two', cost AS 'three' FROM " +
                    "package ORDER BY sl ASC", packaging, package_name, package_list);
            service_name.add("Regular Day");
            service.setText(R.string.regular);
            service_name.add("Quick Day");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(parcel.this,
                    R.layout.item_name, service_name);
            service.setAdapter(dataAdapter);

            district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    thana_name.clear();
                    temp_thana_strings.clear();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(parcel.this,
                            R.layout.item_name, district_name);
                    district.setAdapter(dataAdapter);

                    int ind = getIndex(district.getText().toString().trim().toLowerCase(), district_name);
                    if (ind != -1) {
                        district_id = district_list.get(ind).getId();
                        setthrid_info("SELECT id AS 'one',upozillaname AS 'two',upozillaname_bangla as 'three'" +
                                " FROM upozillainfo WHERE " +
                                "fk_district_id = '" + district_id + "' order by upozillaname_bangla asc", thana, thana_name, thana_list, 3);
                    }
                    thana.setText("");
                }
            });

            thana.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    home_model = thana_list.get(position);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(parcel.this,
                            R.layout.item_name, thana_name);
                    thana.setAdapter(dataAdapter);

                    int ind = getIndex(thana.getText().toString().trim().toLowerCase(), thana_name);
                    if (ind != -1)
                        thana_id = thana_list.get(ind).getId();
                    ship_charge();
                }
            });
            weight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    identity = weight_list.get(position);
                    weight_id = identity.getId();
                    ship_charge();
                }
            });
            packaging.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    home_model home_model = package_list.get(position);
                    package_id = home_model.getId();
                    net_total = Double.parseDouble(net_total_amount.getText().toString());
                    net_total_amount.setText(String.valueOf(net_total - Double.parseDouble(pack_a.getText().toString())));
                    pack_a.setText(home_model.getText());
                    net_total = Double.parseDouble(net_total_amount.getText().toString());
                    net_total_amount.setText(String.valueOf(net_total + Double.parseDouble(home_model.getText().trim())));
                }
            });
            discount();
            cod();
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        double amount = 0;
                        net_total = Double.parseDouble(net_total_amount.getText().toString());
                        net_total_amount.setText(String.valueOf(net_total - Double.parseDouble(cod_a.getText().toString())));

                        if (cod.getText().toString().equals(""))
                            amount = 0;
                        else
                            amount = Double.parseDouble(cod.getText().toString().trim());
                        cod_amount = (amount / 100) * cod_p;
                        cod_a.setText(String.valueOf(cod_amount));
                        net_total = Double.parseDouble(net_total_amount.getText().toString());
                        net_total_amount.setText(String.valueOf(net_total + cod_amount));
                    } catch (Exception e) {
                    }
                }
            };
            cod.addTextChangedListener(textWatcher);

            TextWatcher textWatcher1 = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tempstrings.clear();
                    getPosition(district.getText().toString().trim().toLowerCase(), district_list, tempstrings, district);
                    district.setThreshold(1);
                }
            };
            district.addTextChangedListener(textWatcher1);
            TextWatcher textWatcher2 = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    temp_thana_strings.clear();
                    getPosition(thana.getText().toString().trim().toLowerCase(), thana_list, temp_thana_strings, thana);
                    thana.setThreshold(1);
                }
            };
            thana.addTextChangedListener(textWatcher2);


            parcel_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit(view);
                }
            });

        } catch (Exception e) {
        }
    }

    public void getPosition(String s, List<home_model> list, List<String> tempstring, AutoCompleteTextView autoCompleteTextView) {
        try {
            for (home_model x : list) {
                if (x.getName().toLowerCase().matches("(.*)"+s+"(.*)") || x.getText().toLowerCase().matches("(.*)"+s+"(.*)")) {
                    tempstring.add(x.getName() + "(" + x.getText() + ")");
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(parcel.this,
                    R.layout.item_name, tempstring);
            autoCompleteTextView.setAdapter(dataAdapter);
            autoCompleteTextView.setThreshold(1);

            autoCompleteTextView.showDropDown();
//            track_id.setValidator();
        } catch (Exception e) {
        }
    }

    public int getIndex(String s, List<String> strings) {
        try {
            for (String x : strings) {
                if (x.toLowerCase().equals(s)) {
                    return strings.indexOf(x);
                }
            }
//            track_id.setValidator();
        } catch (Exception e) {
        }
        return -1;
    }

    private void setAutoComplete(String sql, AutoCompleteTextView autoCompleteTextView, List<String> name, List<identity> list, int check) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.TWO_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showAuto(response.trim(), parcel.this, autoCompleteTextView,
                                        name, list, check);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void setthrid_info(String sql, AutoCompleteTextView autoCompleteTextView, List<String> name, List<home_model> list, int check) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showthrid(response.trim(), parcel.this, autoCompleteTextView,
                                        name, list, check);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void setpackage(String sql, AutoCompleteTextView autoCompleteTextView, List<String> name, List<home_model> list) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showpackage(response.trim(), parcel.this, autoCompleteTextView,
                                        name, list);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    protected void showAuto(String response, Context context, AutoCompleteTextView autoCompleteTextView,
                            List<String> strings, List<identity> identities, int check) {
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
                    if (check == 2 && i == 0) {
                        autoCompleteTextView.setText(name);
                        weight_id = id;
                    }
                } catch (Exception e) {
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                    R.layout.item_name, strings);
            autoCompleteTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    protected void showthrid(String response, Context context, AutoCompleteTextView autoCompleteTextView,
                             List<String> strings, List<home_model> identities, int check) {
        String id = "";
        String name = "";
        String bangla = "";
        home_model cat_models;
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
                    bangla = collegeData.getString(config.THREE);
                    cat_models = new home_model(id, name, bangla, "");
                    identities.add(cat_models);
                    strings.add(name + "(" + bangla + ")");
                    if (check == 1 && id.equals("7")) {
                        autoCompleteTextView.setText(name);
                        if (autoCompleteTextView.isPopupShowing())
                            autoCompleteTextView.dismissDropDown();
                        district_id = id;
                        setthrid_info("SELECT id AS 'one',upozillaname AS 'two',upozillaname_bangla as 'three'" +
                                " FROM upozillainfo WHERE " +
                                "fk_district_id = '" + district_id + "' order by upozillaname_bangla asc", thana, thana_name, thana_list, 3);
                        thana.setText("");
                    }
                } catch (Exception e) {
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                    R.layout.item_name, strings);
            autoCompleteTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    protected void showpackage(String response, Context context, AutoCompleteTextView autoCompleteTextView,
                               List<String> strings, List<home_model> identities) {
        String id = "";
        String name = "";
        String cost = "";
        home_model cat_models;
        try {
            strings.clear();
            identities.clear();
            identities.add(new home_model("", "None", "0", ""));
            strings.add("None");
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                try {
                    JSONObject collegeData = result.getJSONObject(i);
                    name = collegeData.getString(config.TWO);
                    id = collegeData.getString(config.ONE);
                    cost = collegeData.getString(config.THREE);
                    cat_models = new home_model(id, name, cost, "");
                    identities.add(cat_models);
                    strings.add(name);
                } catch (Exception e) {
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                    R.layout.item_name, strings);
            autoCompleteTextView.setAdapter(dataAdapter);
        } catch (Exception e) {
        }
    }

    private void ship_charge() {
        try {
            if (district.getText().toString().trim().equals("")) {
                Toast.makeText(parcel.this, "At first you should select District", Toast.LENGTH_SHORT).show();
                weight.setText("");
                return;
            }
            if (thana.getText().toString().trim().equals("")) {
                Toast.makeText(parcel.this, "At first you should select Select Thana", Toast.LENGTH_SHORT).show();
                weight.setText("");
                return;
            }
            if (service.getText().toString().trim().equals("")) {
                Toast.makeText(parcel.this, "At first you should select Select Service", Toast.LENGTH_SHORT).show();
                weight.setText("");
                return;
            }

            if (service.getText().toString().equals("Quick Day"))
                sql = "SELECT quick_charge AS 'id' FROM delivery_charge_area WHERE fk_district_id = '" + district_id + "' AND" +
                        " fk_upozilla_id = '" + thana_id + "' AND weight_title = '" + weight_id + "' AND quick_day = '1-2'";
            else
                sql = "SELECT regular_cahrge AS 'id' FROM delivery_charge_area WHERE fk_district_id = '" + district_id + "' AND" +
                        " fk_upozilla_id = '" + thana_id + "' AND weight_title = '" + weight_id + "' AND regular_day = '1-3'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("")) {
                                net_total = Double.parseDouble(net_total_amount.getText().toString());
                                net_total_amount.setText(String.valueOf(net_total - Double.parseDouble(ship_a.getText().toString())));
                                ship_a.setText(response.trim());
                                net_total = Double.parseDouble(net_total_amount.getText().toString());
                                net_total_amount.setText(String.valueOf(net_total + Double.parseDouble(ship_a.getText().toString())));
                            } else {
                                net_total = Double.parseDouble(net_total_amount.getText().toString());
                                net_total_amount.setText(String.valueOf(net_total - Double.parseDouble(ship_a.getText().toString())));
                                ship_a.setText("0");
                                net_total = Double.parseDouble(net_total_amount.getText().toString());
                                net_total_amount.setText(String.valueOf(net_total + Double.parseDouble(ship_a.getText().toString())));
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    net_total = Double.parseDouble(net_total_amount.getText().toString());
                    net_total_amount.setText(String.valueOf(net_total - Double.parseDouble(ship_a.getText().toString())));
                    ship_a.setText("0");
                    net_total = Double.parseDouble(net_total_amount.getText().toString());
                    net_total_amount.setText(String.valueOf(net_total + Double.parseDouble(ship_a.getText().toString())));
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (Exception e) {
        }
    }

    private void discount() {
        try {
            String sql = "SELECT discount as 'id' FROM `merchant_acc` where userid = '" + config.getUser(parcel.this) + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("")) {
                                net_total = Double.parseDouble(net_total_amount.getText().toString());
                                net_total_amount.setText(String.valueOf(net_total - Double.parseDouble(response.trim())));
                                dis_a.setText(response.trim());
                            } else
                                dis_a.setText("0");
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    dis_a.setText("0");
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (Exception e) {
        }
    }

    private void cod() {
        try {
            String sql = "SELECT percentage as 'id' FROM `cod_cost` order by id DESC";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("")) {
                                cod_t.setText("COD + Risk Charge" + "(" + response.trim() + "%):");
                                cod_p = Double.parseDouble(response.trim());
                            } else {
                                cod_t.setText("COD + Risk Charge" + "(0%):");
                                cod_p = 0;
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    cod_t.setText("COD + Risk Charge" + "(0%):");
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (Exception e) {
        }
    }

    private void submit(View v) {
        try {
//            if (customer_in.getText().toString().trim().equals("")) {
//                customer_in.setError("Empty Customer invoice");
//                customer_in.requestFocus();
//                return;
//            }
//            if (customer.getText().toString().trim().equals("")) {
//                customer.setError("Empty Customer");
//                customer.requestFocus();
//                return;
//            }
            if (phone.getText().toString().trim().equals("")) {
                phone.setError("Empty Phone");
                phone.requestFocus();
                return;
            }
            if (district.getText().toString().trim().equals("")) {
                district.setError("Select District");
                district.requestFocus();
                return;
            }
            if (thana.getText().toString().trim().equals("")) {
                thana.setError("Select Thana");
                thana.requestFocus();
                return;
            }
            if (weight.getText().toString().trim().equals("")) {
                weight.setError("Select Weight");
                weight.requestFocus();
                return;
            }
            progressDialog = ProgressDialog.show(parcel.this, "", "Loading...", false, false);
            String sql = "SELECT SUBSTR(MAX(id),3) AS 'id' FROM delivery";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                delivery_id = generate_id(response.trim());
                                check_pick(v, delivery_id, progressDialog);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(parcel.this, "not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (Exception e) {
        }
    }

    private void check_pick(View v, String delivery_id, ProgressDialog progressDialog) {
        try {
            String sql = "select id from pickup_req_agent where req_date = current_date";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                insert(delivery_id, progressDialog, v, 1);
                            } else {
                                insert(delivery_id, progressDialog, v, 2);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private String generate_id(String uid) {
        String prefix;
        int id;
        try {
            prefix = "ID";
            id = Integer.parseInt(uid);
            id++;
            if (id <= 9) {
                return (prefix + "000000" + "" + Long.toString(id));

            } else if (id <= 99) {
                return (prefix + "00000" + "" + Long.toString(id));
            } else if (id <= 999) {
                return (prefix + "0000" + "" + Long.toString(id));
            } else if (id <= 9999) {
                return (prefix + "000" + "" + Long.toString(id));
            } else if (id <= 99999) {
                return (prefix + "00" + "" + Long.toString(id));
            } else if (id <= 999999) {
                return (prefix + "0" + "" + Long.toString(id));
            } else if (id <= 9999999) {
                return (prefix + "" + "" + Long.toString(id));
            }

        } catch (Exception e) {
        }
        return null;
    }


    private void insert(String id, ProgressDialog progressDialog, View v, int check) {
        String session = "";

        try {
            if (package_id == null || package_id.equals(""))
                package_id = "0";
            session = config.getSession(parcel.this);

            if (check == 1) {
                sql = "insert into delivery (id,customer_invoice,merchantId,type,customer_name," +
                        "customer_phone,customer_disctrict,customer_thana," +
                        "  customer_address,note,deliveryonlyornot,product_weight," +
                        "package,coll_amount,shipmentcharges,codchargeits," +
                        "  packchrgs,discount,totalamounts,session_id,status," +
                        "position,pickup_req_status,created_at,pickup_agent_id,updated_at) VALUES ('" + id + "','" + customer_in.getText().toString().trim() + "'" +
                        ",'" + config.getUser(parcel.this) + "','merchant','" + customer.getText().toString().trim() + "'" +
                        ",'" + phone.getText().toString().trim() + "','" + district_id + "','" + thana_id + "'," +
                        "'" + address.getText().toString().trim() + "','" + note.getText().toString().trim() + "','2'," +
                        "'" + weight_id + "','" + package_id + "','" + cod.getText().toString().trim() + "'," +
                        "'" + ship_a.getText().toString().trim() + "','" + cod_a.getText().toString().trim() + "'," +
                        "'" + pack_a.getText().toString() + "','" + dis_a.getText().toString().trim() + "'," +
                        "'" + net_total_amount.getText().toString().trim() + "','" + session + "','ordered'," +
                        "'2','0',CURRENT_TIMESTAMP,'" + config.getAgent(parcel.this) + "',CURRENT_TIMESTAMP)";
            } else {
                sql = "insert into delivery (id,customer_invoice,merchantId,type,customer_name," +
                        "customer_phone,customer_disctrict,customer_thana," +
                        "  customer_address,note,deliveryonlyornot,product_weight," +
                        "package,coll_amount,shipmentcharges,codchargeits," +
                        "  packchrgs,discount,totalamounts,session_id,status," +
                        "position,created_at,pickup_agent_id,updated_at) VALUES ('" + id + "','" + customer_in.getText().toString().trim() + "'" +
                        ",'" + config.getUser(parcel.this) + "','merchant','" + customer.getText().toString().trim() + "'" +
                        ",'" + phone.getText().toString().trim() + "','" + district_id + "','" + thana_id + "'," +
                        "'" + address.getText().toString().trim() + "','" + note.getText().toString().trim() + "','2'," +
                        "'" + weight_id + "','" + package_id + "','" + cod.getText().toString().trim() + "'," +
                        "'" + ship_a.getText().toString().trim() + "','" + cod_a.getText().toString().trim() + "'," +
                        "'" + pack_a.getText().toString() + "','" + dis_a.getText().toString().trim() + "'," +
                        "'" + net_total_amount.getText().toString().trim() + "','" + session + "','ordered'," +
                        "'2',CURRENT_TIMESTAMP,'" + config.getAgent(parcel.this) + "',CURRENT_TIMESTAMP)";
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("")) {
                                agent_insert(progressDialog, v);
                                if (bitmap != null)
                                    uploadBitmap(bitmap, delivery_id);
                            } else {
                                Toast.makeText(parcel.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception ne) {
        }
    }

    private void agent_insert(ProgressDialog progressDialog, View v) {
        try {
            String sql = "insert into agentassign (agent_id,merchant_id,invoice_id,assign_date,status," +
                    "created_at,updated_at,noteforagent) VALUES ('" + config.getAgent(parcel.this) + "','" + config.getUser(parcel.this) + "'" +
                    ",'" + delivery_id + "',CURRENT_TIMESTAMP,'0'" +
                    ",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'" + note.getText().toString().trim() + "')";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {
//                                config.regularSnak(v, "Successful");
                                Intent intent = new Intent(parcel.this, success.class);
                                intent.putExtra("invoice", delivery_id);
                                intent.putExtra("cus_name", customer.getText().toString().trim());
                                intent.putExtra("cus_phone", phone.getText().toString().trim());
                                intent.putExtra("cus_address", address.getText().toString().trim());
                                startActivity(intent);
                                finish();
                                clean();
                            } else
                                Toast.makeText(parcel.this, response, Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(parcel.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(parcel.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception ne) {
        }
    }

    private void clean() {
        try {
            district.setText("");
            customer.setText("");
            customer_in.setText("");
            thana.setText("");
            note.setText("");
            cod.setText("");
            phone.setText("");
            address.setText("");
            packaging.setText("");
            customer_in.requestFocus();
            net_total_amount.setText("0");
            ship_a.setText("0");
            cod_a.setText("0");
            pack_a.setText("0");
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                selectImage();

//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent, "Select Image"),CODE_GALLERY_REQUEST);

            }
            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            // gallery
            Uri filepath = data.getData();
//            String d = getRealPathFromURI(filepath);
            UCrop.of(filepath, Uri.fromFile(new File(getCacheDir(), UUID.randomUUID() + ".jpg")))
                    .withMaxResultSize(1000, 1000)
                    .start(parcel.this);
//            performCrop(filepath);
            try {
//

//                imagev.setImageBitmap(bitmap);
            } catch (Exception e) {
            }
        } else if (requestCode == PIC_CROP && data != null) {
            try {
                Uri filepath = data.getData();
                // get the returned data
//                Bundle extras = data.getExtras();
                // get the cropped bitmap
//                bitmap = extras.getParcelable("data");
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                info_img.setImageBitmap(bitmap);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        } else if (requestCode == 0 && data != null) {
            // camera
            if (resultCode == RESULT_OK) {
//                Uri filepath = data.getData();
//                performCrop(filepath);

                try {
//                    Uri takenPhotoUri;
//                Uri uri = data.getParcelableExtra("path");
//                    cropImage(getCacheImagePath(file_name));
//                    InputStream inputStream = getContentResolver().openInputStream(filepath);
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
////                    UCrop.of(takenPhotoUri, Uri.fromFile(new File(getCacheDir(), file_name)))
////                            .withMaxResultSize(1000, 1000)
////                            .start(parcel.this);
                    info_img.setImageBitmap(bitmap);
                } catch (Exception e) {
//                    e.printStackTrace();
                }

            }
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // Ucrop
            try {
                final Uri resultUri = UCrop.getOutput(data);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                info_img.setImageBitmap(bitmap);
            } catch (Exception e) {
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void takeCameraImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                file_name = System.currentTimeMillis() + ".jpg";
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(file_name));
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, 0);
                                }
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }

                    }).check();
        }
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(parcel.this, getPackageName() + ".provider", image);
    }

    private void cropImage(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void selectImage() {
//        final CharSequence[] options = {"Take Image", "Choose from Gallery", "Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view1 = LayoutInflater.from(parcel.this).inflate(R.layout.image_chooser, null);
        MaterialCardView camera = view1.findViewById(R.id.camera_card);
        MaterialCardView gallery = view1.findViewById(R.id.image_card);
        Button cancel = view1.findViewById(R.id.close);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(parcel.this, R.style.RoundShapeTheme);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file_name = UUID.randomUUID() + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
//                takeCameraImage();
                alertDialog.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CODE_GALLERY_REQUEST);
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Image")) {
//
//                } else if (options[item].equals("Choose from Gallery")) {
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
        alertDialog.show();
    }

    private void uploadBitmap(final Bitmap bitmap, String id) {
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, config.FILE_UPLOAD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", "");
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(id + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


}
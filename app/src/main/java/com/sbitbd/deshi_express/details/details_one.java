package com.sbitbd.deshi_express.details;

import static com.sbitbd.deshi_express.Config.config.DELIVERY_ADDRESS_IMG;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.four_model;
import com.sbitbd.deshi_express.model.twelve_model;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class details_one extends AppCompatActivity {

    private ImageView imageView, address_img;
    protected TextView delivery_id, status, main_amount, date, delivery_cost, customer_name, address, phone,
            tracking_code, assign_name, assign_phone, assign_zone, note_t, track_first_date, track_second_date,
            track_six_date, track_eight_date;
    protected ImageView track_checkimg_1, track_checkimg_2, track_checkimg_3, track_checkimg_4, track_checkimg_5,
            track_checkimg_6, track_checkimg_7, track_checkimg_8;
    protected Button reject, edit_btn;
    protected String id = "";
    private details_controller details_controller;
    private ProgressDialog progressDialog;
    private config config = new config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_one);
        initview();
    }

    private void initview() {
        try {
            imageView = findViewById(R.id.details_back);
            delivery_id = findViewById(R.id.id_de);
            status = findViewById(R.id.status);
            main_amount = findViewById(R.id.main_amount);
            date = findViewById(R.id.textView9);
            delivery_cost = findViewById(R.id.textView10);
            customer_name = findViewById(R.id.textView11);
            address = findViewById(R.id.textView12);
            phone = findViewById(R.id.textView13);
            tracking_code = findViewById(R.id.tracking_code);
            assign_name = findViewById(R.id.assign_name);
            assign_phone = findViewById(R.id.assign_phone);
            assign_zone = findViewById(R.id.assign_zone);
            note_t = findViewById(R.id.note_t);
            track_first_date = findViewById(R.id.first_date);
            track_second_date = findViewById(R.id.second_date);
            track_six_date = findViewById(R.id.sixth_date);
            track_eight_date = findViewById(R.id.eighth_date);
            track_checkimg_1 = findViewById(R.id.checked_img);
            track_checkimg_2 = findViewById(R.id.checked_img1);
            track_checkimg_3 = findViewById(R.id.checked_img2);
            track_checkimg_4 = findViewById(R.id.checked_img3);
            track_checkimg_5 = findViewById(R.id.checked_img4);
            track_checkimg_6 = findViewById(R.id.checked_img5);
            track_checkimg_7 = findViewById(R.id.checked_img6);
            track_checkimg_8 = findViewById(R.id.checked_img7);
            reject = findViewById(R.id.reject);
            edit_btn = findViewById(R.id.edit_btn);
            address_img = findViewById(R.id.address_img);

            id = getIntent().getStringExtra("id");

            details_controller = new ViewModelProvider(this).get(details_controller.class);

            delivery_id.setText(id);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            Picasso.get().load(DELIVERY_ADDRESS_IMG + id + ".jpg").transform(new RoundedCornersTransformation(15, 0))
                    .fit().centerInside()
                    .into(address_img);

//            Picasso.setSingletonInstance(new Picasso.Builder(this).build()); // Only needed if you are using Picasso

            final ImagePopup imagePopup = new ImagePopup(this);
//            imagePopup.setWindowHeight(800); // Optional
//            imagePopup.setWindowWidth(800); // Optional
            imagePopup.setBackgroundColor(Color.BLACK);  // Optional
            imagePopup.setFullScreen(true); // Optional
            imagePopup.setImageOnClickClose(true);  // Optional

//            ImageView imageView = findViewById(R.id.imageView);

//            imagePopup.initiatePopup(address_img.getDrawable()); // Load Image from Drawable
            imagePopup.initiatePopupWithGlide(DELIVERY_ADDRESS_IMG + id + ".jpg");

            address_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Initiate Popup view **/
                    imagePopup.viewPopup();

                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setRejection();
                }
            });
            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText name, phone, address, note;
                    Button update, close;
                    View view1 = LayoutInflater.from(details_one.this).inflate(R.layout.edit_customer, null);
                    name = view1.findViewById(R.id.note_p);
                    phone = view1.findViewById(R.id.phone_u);
                    address = view1.findViewById(R.id.cus_address);
                    note = view1.findViewById(R.id.cus_note);
                    update = view1.findViewById(R.id.update_submit);
                    close = view1.findViewById(R.id.close);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(details_one.this, R.style.RoundShapeTheme);
                    dialogBuilder.setView(view1);
                    get_update_details(name, phone, address, note, update);
                    AlertDialog alertDialog = dialogBuilder.create();
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            update_details(name.getText().toString().trim(), phone.getText().toString()
                                    .trim(), address.getText().toString().trim(), note.getText().toString().trim(), alertDialog);
                        }
                    });
                    alertDialog.show();
                }
            });
        } catch (Exception e) {
        }
    }

    private void CONFIG() {
        try {
            progressDialog = ProgressDialog.show(details_one.this, "", "Loading", false, false);
            details_controller.getData().setValue(id);
            details_controller.getcontext().setValue(details_one.this);
            final Observer<twelve_model> data_observer = new Observer<twelve_model>() {
                @Override
                public void onChanged(twelve_model twelve_model) {
                    progressDialog.dismiss();
                    if (twelve_model != null) {
                        String position = "";
                        date.setText(twelve_model.getOne());
                        delivery_cost.setText(twelve_model.getTwo() + " TK");
                        customer_name.setText(twelve_model.getThree());
                        address.setText(twelve_model.getFour() + "," + twelve_model.getFive() + "," + twelve_model.getSix());
                        phone.setText(twelve_model.getSeven());
                        tracking_code.setText("Tracking Code: " + id);
                        note_t.setText(twelve_model.getEight());
                        main_amount.setText(twelve_model.getNine() + " TK");
                        position = twelve_model.getTen();
                        position_setup(position);
                        pickup_setup(twelve_model.getEleven());
                    }
                }
            };
            details_controller.getViewData().observe(this, data_observer);

            final Observer<four_model> rider_observer = new Observer<four_model>() {
                @Override
                public void onChanged(four_model four_model) {
                    if (four_model != null) {
                        assign_name.setText(four_model.getOne());
                        assign_phone.setText(four_model.getTwo());
                        assign_zone.setText(four_model.getThree());
                    }
                }
            };
            details_controller.getRiderData().observe(this, rider_observer);

            final Observer<four_model> date_observer = new Observer<four_model>() {
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
            };
            details_controller.gettrack_date().observe(this, date_observer);
        } catch (Exception e) {
        }
    }

    private void position_setup(String position) {
        try {
            switch (position) {
                case "2":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    status.setText("IN_REVIEW");
                    break;
                case "3":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    status.setText("PROCESSING");
                    break;
                case "4":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    status.setText("PROCESSING");
                    break;
                case "5":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    track_checkimg_6.setVisibility(View.VISIBLE);
                    status.setText("PROCESSING");
                    break;
                case "6":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    track_checkimg_6.setVisibility(View.VISIBLE);
                    track_checkimg_7.setVisibility(View.VISIBLE);
                    status.setText("PROCESSING");
                    break;
                case "7":
                    track_checkimg_1.setVisibility(View.VISIBLE);
                    track_checkimg_2.setVisibility(View.VISIBLE);
                    track_checkimg_4.setVisibility(View.VISIBLE);
                    track_checkimg_5.setVisibility(View.VISIBLE);
                    track_checkimg_6.setVisibility(View.VISIBLE);
                    track_checkimg_7.setVisibility(View.VISIBLE);
                    track_checkimg_8.setVisibility(View.VISIBLE);
                    status.setText("DELIVERED");
                    reject.setEnabled(false);
                    edit_btn.setEnabled(false);
                    break;
                case "8":
                    track_checkimg_1.setVisibility(View.GONE);
                    reject.setEnabled(false);
                    edit_btn.setEnabled(false);
                    status.setText("REJECTED");
                    break;
            }
        } catch (Exception e) {
        }
    }

    private void pickup_setup(String position) {
        try {
            if (position.equals("1")) {
                track_checkimg_3.setVisibility(View.VISIBLE);
                reject.setEnabled(false);
            }
        } catch (Exception e) {
        }
    }

    private void setRejection() {
        try {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(details_one.this, R.style.RoundShapeTheme);
            dialogBuilder.setTitle("Confirmation");
            dialogBuilder.setMessage("Are you sure you want to Reject");
            dialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                reject();
            });
            dialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            dialogBuilder.show();
        } catch (Exception e) {
        }
    }

    private void reject() {
        try {
            progressDialog = ProgressDialog.show(details_one.this, "", "Loading...", false, false);
            String sql = "UPDATE delivery SET position = '8' where delivery.id = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {
                                reject.setEnabled(false);
                                edit_btn.setEnabled(false);
                                CONFIG();
                            } else {
                                Toast.makeText(details_one.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(details_one.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(details_one.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void update_details(String name, String phone, String address, String note, AlertDialog alertDialog) {
        try {
            progressDialog = ProgressDialog.show(details_one.this, "", "Loading...", false, false);
            String sql = "UPDATE delivery SET customer_name = '" + name + "',customer_phone = '" + phone + "'," +
                    "customer_address = '" + address + "',note = '" + note + "' where delivery.id = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.trim().equals("")) {
                                CONFIG();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(details_one.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(details_one.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(details_one.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void get_update_details(EditText name, EditText phone, EditText address, EditText note, Button update) {
        try {

            String sql = "SELECT customer_name as 'one',customer_phone as 'two'," +
                    "customer_address as 'three',note as 'four' FROM delivery where delivery.id = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FOUR_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.trim());
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    name.setText(collegeData.getString(config.ONE));
                                    phone.setText(collegeData.getString(config.TWO));
                                    address.setText(collegeData.getString(config.THREE));
                                    note.setText(collegeData.getString(config.FOUR));
                                } catch (Exception e) {
                                }
                            } else {
                                update.setEnabled(false);
                                Toast.makeText(details_one.this, "not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(details_one.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(details_one.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        CONFIG();
    }
}
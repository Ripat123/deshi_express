package com.sbitbd.deshi_express.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.sbitbd.deshi_express.model.home_model;
import com.sbitbd.deshi_express.ui.home.HomeViewModel;
import com.sbitbd.deshi_express.view_form.view_form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dash_adapter extends RecyclerView.Adapter<dash_adapter.userHolder> {
    private List<home_model> userList;
    private Context context;

    public dash_adapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_card, null);
        userHolder userHolder = new userHolder(inflate);
        return userHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
        home_model user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void ClearCategory() {
        userList.clear();
        notifyDataSetChanged();
    }

    public void addUser(home_model user) {
        try {
            userList.add(user);
            //notifyDataSetChanged();
            int position = userList.indexOf(user);
            notifyItemInserted(position);
        } catch (Exception e) {
        }
    }

    class userHolder extends RecyclerView.ViewHolder {
        TextView name;
        MaterialCardView cardView;
        ConstraintLayout constraintLayout;
        final View cat_view;
        private final Context context1;
        config config = new config();
        Intent intent;
        HomeViewModel homeViewModel = new HomeViewModel();

        public userHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.dash_t);
            cardView = itemView.findViewById(R.id.dash_card);
            constraintLayout = itemView.findViewById(R.id.dash_la);
            cat_view = itemView;
            context1 = itemView.getContext();
        }

        public void bind(home_model user) {
            try {
                name.setText(user.getName());
                switch (user.getId()) {
                    case "1":
                        constraintLayout.setBackgroundResource(R.drawable.gradient1);
                        break;
                    case "2":
                        constraintLayout.setBackgroundResource(R.drawable.gradient2);
                        break;
                    case "3":
                        constraintLayout.setBackgroundResource(R.drawable.gradient3);
                        homeViewModel.get_shipment(context1,user.getName(),name,"SELECT COUNT(id) " +
                                "AS 'id' FROM delivery where merchantId = '" + config.getUser(context) + "' and position = '4'");
                        break;
                    case "4":
                        constraintLayout.setBackgroundResource(R.drawable.gradient4);
                        homeViewModel.get_balance(context1,user.getName(),name);
                        break;
                    case "5":
                        constraintLayout.setBackgroundResource(R.drawable.gradient5);
                        homeViewModel.get_shipment(context1,user.getName(),name,"SELECT COUNT(id) " +
                                "AS 'id' FROM delivery where merchantId = '" + config.getUser(context) + "' and position = '11'");
                        break;
                    case "6":
                        constraintLayout.setBackgroundResource(R.drawable.gradient6);

                        homeViewModel.get_shipment(context1,user.getName(),name,"Select CAST(((Select Count(*)" +
                                " From delivery where position = '7' and merchantId = '"+config.getUser(context)+"')" +
                                " / Count(*)) * 100 as DECIMAL(10,2)) as id From delivery where merchantId = '"+config.getUser(context)+"'");

                        break;
                }


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (user.getId()) {
                            case "1":
                                pickup();
                                break;
                            case "2":
                                payment();
                                break;
                            case "3":
                                intent = new Intent(context1, view_form.class);
                                intent.putExtra("status","ship");
                                intent.putExtra("title","Shipment");
                                context1.startActivity(intent);
                                break;
                            case "5":
                                intent = new Intent(context1, view_form.class);
                                intent.putExtra("status","return");
                                intent.putExtra("title","Return");
                                context1.startActivity(intent);
                                break;
                        }
                    }
                });
            } catch (Exception e) {
            }
        }

        private void pickup() {
            EditText note, estimate, address;
            Button request,close;
            MaterialCardView edit;
            try {
                View view1 = LayoutInflater.from(context1).inflate(R.layout.pickup_lay, null);
                note = view1.findViewById(R.id.note_p);
                estimate = view1.findViewById(R.id.e_parcel);
                address = view1.findViewById(R.id.address_pick);
                request = view1.findViewById(R.id.request_submit);
                close = view1.findViewById(R.id.close);
                edit = view1.findViewById(R.id.edit_card);
                getaddress(address);
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context1, R.style.RoundShapeTheme);
                dialogBuilder.setTitle("Pickup Request");
                dialogBuilder.setView(view1);
                AlertDialog alertDialog = dialogBuilder.create();
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!address.isEnabled()) {
                            address.setEnabled(true);
                        }
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (address.getText().toString().trim().equals("")) {
                            Toast.makeText(context1, "Empty Address", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (address.isEnabled()) {
                            homeViewModel.Update_address(address.getText().toString().trim(), context1);
                        }
                        homeViewModel.request(context1, note.getText().toString().trim(), estimate.getText().toString().trim());

                    }
                });

                alertDialog.show();
            } catch (Exception e) {
            }
        }

        private void payment() {
            AutoCompleteTextView method;
            List<String> method_list = new ArrayList<>();
            Button request,close;
            TextView number_t;

            try {
                View view1 = LayoutInflater.from(context1).inflate(R.layout.pay_req, null);
                method = view1.findViewById(R.id.method);
                request = view1.findViewById(R.id.pay_request_submit);
                close = view1.findViewById(R.id.close);
                number_t = view1.findViewById(R.id.number_t);
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context1, R.style.RoundShapeTheme);
                dialogBuilder.setTitle("Payment Request");
                dialogBuilder.setView(view1);
                AlertDialog alertDialog = dialogBuilder.create();
                method_list.add("Cash");
                method_list.add("Bank");
                method_list.add("Bkash");
                method_list.add("Rocket");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context1,
                        R.layout.item_name, method_list);
                method.setAdapter(dataAdapter);
                method.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (method.getText().toString().trim().equals("Cash"))
                            number_t.setText("Cash");
                        else if (method.getText().toString().trim().equals("Bank"))
                            get_number(context1, number_t, "SELECT bank_acc as 'id' FROM merchant_acc" +
                                    " WHERE userid = '" + config.getUser(context) + "'");
                        else if (method.getText().toString().trim().equals("Bkash"))
                            get_number(context1, number_t, "SELECT bkash as 'id' FROM merchant_acc" +
                                    " WHERE userid = '" + config.getUser(context) + "'");
                        else
                            get_number(context1, number_t, "SELECT rocket as 'id' FROM merchant_acc" +
                                    " WHERE userid = '" + config.getUser(context) + "'");
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int method_status;
                        if (method.getText().toString().trim().equals("")) {
                            Toast.makeText(context1, "Select Method", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (method.getText().toString().trim().equals("Cash"))
                            method_status = 1;
                        else if (method.getText().toString().trim().equals("Bank"))
                            method_status = 2;
                        else if (method.getText().toString().trim().equals("Bkash"))
                            method_status = 3;
                        else
                            method_status = 4;
                        homeViewModel.get_pay_amount(context1, method_status);

                    }
                });

                alertDialog.show();
            } catch (Exception e) {
            }
        }

        private void getaddress(EditText editText) {
            try {
                String sql = "SELECT Address as 'id' FROM merchant_acc WHERE userid = '" + config.getUser(context) + "'";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                editText.setText(response.trim());
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context1, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context1);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (Exception e) {
            }
        }

        private void get_number(Context context, TextView textView, String sql) {
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                textView.setText(response.trim());
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context1, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(config.QUERY, sql);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context1);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (Exception e) {
            }
        }

    }
}

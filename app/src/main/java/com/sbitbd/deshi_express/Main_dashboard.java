package com.sbitbd.deshi_express;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.parcel.parcel;
import com.sbitbd.deshi_express.profile.profile;
import com.sbitbd.deshi_express.search.search;
import com.squareup.picasso.Picasso;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Main_dashboard extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MenuItem parcel, search;
    private TextView nav_text;
    private ImageView menu_image;
    private config config = new config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (fab.isExtended())
//                    fab.shrink();
//                else
//                    fab.extend();
                startActivity(new Intent(Main_dashboard.this, parcel.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_area, R.id.nav_pricing,
                R.id.nav_return, R.id.nav_all, R.id.nav_payments, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        parcel = navigationView.getMenu().findItem(R.id.nav_parcel);
        search = navigationView.getMenu().findItem(R.id.nav_search);
        parcel.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Main_dashboard.this, parcel.class));
                return true;
            }
        });
        search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Main_dashboard.this, search.class));
                return true;
            }
        });
        nav_text = navigationView.getHeaderView(0).findViewById(R.id.nav_text);
        try {
            menu_image = navigationView.getHeaderView(0).findViewById(R.id.menu_image);
            Picasso.get().load(config.PROFILE + config.getUser(Main_dashboard.this) + ".jpg").transform(new RoundedCornersTransformation(30, 0))
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(menu_image);
            getInfo(nav_text);
        }catch (Exception e){
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_dashboard, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Main_dashboard.this, profile.class));
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Main_dashboard.this,R.style.RoundShapeTheme);
                dialogBuilder.setTitle("Log Out!");
                dialogBuilder.setMessage("Are you sure, you want to Log Out?");
                dialogBuilder.setPositiveButton("yes",(dialog, which) -> {
                    config.deleteuser(Main_dashboard.this);
                    startActivity(new Intent(Main_dashboard.this,MainActivity.class));
                    finish();
                });
                dialogBuilder.setNegativeButton("no",(dialog, which) -> {
                    dialog.cancel();
                });
                dialogBuilder.show();
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Main_dashboard.this,R.style.RoundShapeTheme);
                dialogBuilder.setTitle("Exit!");
                dialogBuilder.setMessage("Are you sure, you want to Exit?");
                dialogBuilder.setPositiveButton("yes",(dialog, which) -> {
                    System.exit(1);
                });
                dialogBuilder.setNegativeButton("no",(dialog, which) -> {
                    dialog.cancel();
                });
                dialogBuilder.show();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getInfo(TextView textView) {
        String uid = config.getUser(Main_dashboard.this);
        try {
            String sql = "SELECT Businessname AS 'one' FROM `merchant_acc` WHERE " +
                    "userid = '"+uid+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.TWO_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response != null && !response.trim().equals("") && !response.trim().equals("{\"result\":[]}")
                                        && !response.trim().equals("Unable to Connect")) {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(config.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    textView.setText(collegeData.getString(config.ONE) + " ("+ uid + ")");
                                }
                                else if (response.trim().equals("Unable to Connect")){
                                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Main_dashboard.this,R.style.RoundShapeTheme);
                                    dialogBuilder.setTitle("Unable to Connect!");
                                    dialogBuilder.setCancelable(false);
                                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                                        dialog.cancel();
                                        startActivity(new Intent(Main_dashboard.this,MainActivity.class));
                                        finish();
                                    });
                                    dialogBuilder.show();
                                }
                                else {
                                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Main_dashboard.this,R.style.RoundShapeTheme);
                                    dialogBuilder.setTitle("Not a valid user!");
                                    dialogBuilder.setMessage("Please login for user validation.");
                                    dialogBuilder.setCancelable(false);
                                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                                        dialog.cancel();
                                        config.deleteuser(Main_dashboard.this);
                                        startActivity(new Intent(Main_dashboard.this,MainActivity.class));
                                        finish();
                                    });
                                    dialogBuilder.show();
                                }
                            } catch (Exception e) {
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Main_dashboard.this);
                    dialogBuilder.setTitle("Internet Error!");
                    dialogBuilder.setMessage(error.toString());
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        startActivity(new Intent(Main_dashboard.this,MainActivity.class));
                        finish();
                    });
                    dialogBuilder.setPositiveButton("retry",(dialog, which) -> {
                        getInfo(textView);
                    });
                    dialogBuilder.show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Main_dashboard.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }
}
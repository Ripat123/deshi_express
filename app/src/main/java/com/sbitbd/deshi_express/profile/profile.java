package com.sbitbd.deshi_express.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class profile extends AppCompatActivity {

    private ImageView pro_back;
    private TextView owener_name, company_name, phone, email, address;
    private ImageView profile_img;
    private MaterialCardView img_edit;
    private int CODE_GALLERY_REQUEST = 5555;
    private config config = new config();
    final int PIC_CROP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    private void initView() {
        try {
            pro_back = findViewById(R.id.pro_back);
            owener_name = findViewById(R.id.owner_nt);
            company_name = findViewById(R.id.company_nt);
            phone = findViewById(R.id.phone_nt);
            email = findViewById(R.id.email_nt);
            address = findViewById(R.id.address_nt);
            profile_img = findViewById(R.id.profile_img);
            img_edit = findViewById(R.id.img_edit);

            pro_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            getdata();
            img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(
                            profile.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
                }
            });

            Picasso.get().load(config.PROFILE + config.getUser(profile.this) + ".jpg").transform(new RoundedCornersTransformation(100, 0))
                    .fit().centerInside()
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(profile_img);
        } catch (Exception e) {
        }
    }

    private void getdata() {
        try {
            String sql = "SELECT Businessname AS 'one',ownername AS 'two',contactnumber AS 'three'," +
                    "Address AS 'four',email AS 'five' FROM merchant_acc WHERE userid = '" + config.getUser(profile.this) + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FIVE_DIMENSION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.trim().equals("") && !response.trim()
                                    .equals("{\"result\":[]}"))
                                showdata(response.trim());
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(profile.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(profile.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    protected void showdata(String response) {
        String one = "", two = "", three = "", four = "", five = "";
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
                    company_name.setText(one);
                    owener_name.setText(two);
                    phone.setText(three);
                    address.setText(four);
                    email.setText(five);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CODE_GALLERY_REQUEST);
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
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            performCrop(filepath);
            try {
//                InputStream inputStream = getContentResolver().openInputStream(filepath);
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
//                uploadBitmap(bitmap);
//                imagev.setImageBitmap(bitmap);
            }catch(Exception e){
            }
        }
        else if (requestCode == PIC_CROP && data != null) {
            try {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");

                profile_img.setImageBitmap(selectedBitmap);
            }catch (Exception e){
            }
        }
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

}
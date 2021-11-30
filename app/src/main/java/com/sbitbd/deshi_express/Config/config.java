package com.sbitbd.deshi_express.Config;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class config {

    public static final String TWO_DIMENSION = "https://deshiexpress.com/android/two_dms.php";
    public static final String FILE_UPLOAD = "https://deshiexpress.com/android/parcel_file_upload.php?apicall=uploadpic";
    public static final String TWELVE_DIMENSION = "https://deshiexpress.com/android/twelve_dms.php";
    public static final String FOUR_DIMENSION = "https://deshiexpress.com/android/four_dms.php";
    public static final String FIVE_DIMENSION = "https://deshiexpress.com/android/five_dms.php";
    public static final String EIGHT_DIMENSION = "https://deshiexpress.com/android/eight_dms.php";
    public static final String GET_ID = "https://deshiexpress.com/android/getID.php";
    public static final String PASS_HASH = "https://deshiexpress.com/android/password_enc.php";
    public static final String LOGIN = "https://deshiexpress.com/android/login.php";
    public static final String INSERT = "https://deshiexpress.com/android/insert.php";
    public static final String DATA = "https://deshiexpress.com/android/getData.php";
    public static final String ARRAY_ADD = "https://deshiexpress.com/android/array_add.php";
    public static final String SERVICES_IMG = "https://deshiexpress.com/";
    public static final String HELP_IMG = "https://deshiexpress.com/public/frontend/img/help.png";
    public static final String PROFILE = "https://deshiexpress.com/public/merchatDashboard/Profile/";
    public static final String DELIVERY_ADDRESS_IMG = "https://deshiexpress.com/public/android/infoimg/";
    public static final String LINK = "https://deshiexpress.com/seller-register/";
    public static final String ID = "id";
    public static final String RESULT = "result";
    public static final String QUERY = "query";
    public static final String PRO_IMAGE = "image";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASS = "pass";
    public static final String BUSINESS = "business";
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String ONE = "one";
    public static final String TWO = "two";
    public static final String THREE = "three";
    public static final String FOUR = "four";
    public static final String FIVE = "five";
    public static final String SIX = "six";
    public static final String SEVEN = "seven";
    public static final String EIGHT = "eight";
    public static final String NINE = "nine";
    public static final String TEN = "ten";
    public static final String ELEVEN = "eleven";
    public static final String TWELVE = "twelve";
    private String session;

//    public static final String TWO_DIMENSION = "https://fixedcourier.com/android/two_dms.php";
//    public static final String FOUR_DIMENSION = "https://fixedcourier.com/android/four_dms.php";
//    public static final String FIVE_DIMENSION = "https://fixedcourier.com/android/five_dms.php";
//    public static final String EIGHT_DIMENSION = "https://fixedcourier.com/android/eight_dms.php";
//    public static final String ORDER_DATA = "http://salesman-bd.com/java/getOrderData.php";
//    public static final String GET_ID = "https://fixedcourier.com/android/getID.php";
//    public static final String PASS_HASH = "https://fixedcourier.com/android/password_enc.php";
//    public static final String LOGIN = "https://fixedcourier.com/android/login.php";
//    public static final String INSERT = "https://fixedcourier.com/android/insert.php";
//    public static final String INSERT_1 = "https://courier.demo.sbit.com.bd/android/insert.php";
//    public static final String DATA = "https://fixedcourier.com/android/getData.php";
//    public static final String SERVICES_IMG = "https://fixedcourier.com/";
//    public static final String HELP_IMG = "https://fixedcourier.com/public/frontend/img/help.png";

    public Boolean checkUser(Context context) {
        database sqliteDB = new database(context);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM user");
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    return true;
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                sqliteDB.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public String getUser(Context context) {
        database sqliteDB = new database(context);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM user");
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow("user_id"));
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                sqliteDB.close();
            } catch (Exception e) {
            }
        }
        return "";
    }

    public String getAgent(Context context) {
        database sqliteDB = new database(context);
        try {
            Cursor cursor = sqliteDB.getUerData("SELECT * FROM user");
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow("agent_id"));
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                sqliteDB.close();
            } catch (Exception e) {
            }
        }
        return "";
    }

    public void deleteuser(Context context) {
        database sqliteDB = new database(context);
        try {
            boolean check = sqliteDB.DataOperation(null, "delete", "user",
                    "id = '1'");
            if (!check) {
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
        } finally {
            try {
                sqliteDB.close();
            } catch (Exception e) {
            }
        }
    }

    public void regularSnak(View fab, String msg){
        Snackbar snackbar = Snackbar.make(fab,msg, Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    public void session(Context context){
        try {
            String sql="SELECT DATE_FORMAT(NOW(), '%y%m%d%h%m%s') AS 'id'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("")) {
                                session(context,response);
                            }
                            else {
                                Toast.makeText(context,"session generation failed!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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
    public void session(Context context,String dbdate){
        database sqlite_db = new database(context);
        String session;
        try {
            @SuppressLint("HardwareIds") String deviceUniqueIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMdd");
//            Date todayDate = new Date();
//            String thisDate = currentDate.format(todayDate);
            session = deviceUniqueIdentifier+dbdate.trim() + System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id","1");
            contentValues.put("session_data",session.trim());
            boolean ch = sqlite_db.DataOperation(contentValues,"insert","session",null);
            if(ch){
            }else
                Toast.makeText(context,"Failed to create Session",Toast.LENGTH_LONG).show();
        }catch (Exception e){
        }finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }
    public String getSession(Context context){
        database sqlite_db = new database(context);
        try {
            Cursor cursor = sqlite_db.getUerData("SELECT * FROM session");
            if (cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    session = cursor.getString(cursor.getColumnIndexOrThrow("session_data"));
                }
            }else {
                Toast.makeText(context,"Session not found",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
        return session;
    }
    public void resetSession(Context context){
        session = null;
        database sqlite_db = new database(context);
        try {
            boolean check = sqlite_db.DataOperation(null,"delete","session","id = '1'");
            if(check){
            }else
                Toast.makeText(context,"Session deleting failed",Toast.LENGTH_LONG).show();
            session(context);
        }catch (Exception e){
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }

    public String getdata(Context context,String sql,String cl){
        String data = "";
        database sqlite_db = new database(context);
        try {
            Cursor cursor = sqlite_db.getUerData(sql);
            if (cursor.getCount() > 0){
                if(cursor.moveToNext()){
                    data = cursor.getString(cursor.getColumnIndexOrThrow(cl));
                }
            }else {
                Toast.makeText(context,"not found",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
        }
        finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
        return data;
    }

    public String generatePickup_id(String uid,String tag) {
        String prefix;
        int id;
        try {
            prefix = tag;
            id = Integer.parseInt(uid);
            id++;
            if (id <= 9) {
                return (prefix + "00000000" + "" + Long.toString(id));

            } else if (id <= 99) {
                return (prefix + "0000000" + "" + Long.toString(id));
            } else if (id <= 999) {
                return (prefix + "000000" + "" + Long.toString(id));
            } else if (id <= 9999) {
                return (prefix + "00000" + "" + Long.toString(id));
            } else if (id <= 99999) {
                return (prefix + "0000" + "" + Long.toString(id));
            } else if (id <= 999999) {
                return (prefix + "000" + "" + Long.toString(id));
            } else if (id <= 9999999) {
                return (prefix + "00" + "" + Long.toString(id));
            }else if (id <= 99999999) {
                return (prefix + "0" + "" + Long.toString(id));
            }else if (id <= 999999999) {
                return (prefix + "" + "" + Long.toString(id));
            }

        } catch (Exception e) {
        }
        return null;
    }

    public String generatePickup_agent_id(String uid) {
        String prefix;
        int id;
        try {
            prefix = "PRA-";
            id = Integer.parseInt(uid);
            id++;
            if (id <= 9) {
                return (prefix + "0000000" + "" + Long.toString(id));

            } else if (id <= 99) {
                return (prefix + "000000" + "" + Long.toString(id));
            } else if (id <= 999) {
                return (prefix + "00000" + "" + Long.toString(id));
            } else if (id <= 9999) {
                return (prefix + "0000" + "" + Long.toString(id));
            } else if (id <= 99999) {
                return (prefix + "000" + "" + Long.toString(id));
            } else if (id <= 999999) {
                return (prefix + "00" + "" + Long.toString(id));
            } else if (id <= 9999999) {
                return (prefix + "0" + "" + Long.toString(id));
            }else if (id <= 99999999) {
                return (prefix + "" + "" + Long.toString(id));
            }

        } catch (Exception e) {
        }
        return null;
    }
}

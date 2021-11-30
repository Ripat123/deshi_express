package com.sbitbd.deshi_express.login;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.sbitbd.deshi_express.Config.config;
import com.sbitbd.deshi_express.Config.database;

import org.json.JSONArray;
import org.json.JSONObject;

public class login_controller {
    config config = new config();

    protected boolean insertuser(Context context,String user_id, String user,String agent_id){
        database sqlite_db = new database(context);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", "1");
            contentValues.put("user_name", user);
            contentValues.put("user_id", user_id);
            contentValues.put("agent_id", agent_id);
            return sqlite_db.DataOperation(contentValues, "insert", "user", null);
        } catch (Exception e) {e.printStackTrace();
        } finally {
            try {
                sqlite_db.close();
            } catch (Exception e) {
            }
        }
        return false;
    }
    protected void showJson(String response,String user,Context context) {
        String id = "",agent ="";
        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            JSONObject collegeData = result.getJSONObject(0);
            id = collegeData.getString(config.ID);
            agent = collegeData.getString(config.ONE);

            if (config.checkUser(context)) {
                updateUser(context, id,user,agent);
            } else {
                if (!insertuser(context, id, user,agent))
                    Toast.makeText(context, "User insert failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }

    protected void updateUser(Context context, String id, String user,String agent) {
        database sqliteDB = new database(context);
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_name", user);
            contentValues.put("user_id", id);
            contentValues.put("agent_id", agent);
            boolean ch = sqliteDB.DataOperation(contentValues, "update", "user", "id = '1'");
            if (!ch)
                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        } finally {
            try {
                sqliteDB.close();
            } catch (Exception e) {
            }
        }
    }
}

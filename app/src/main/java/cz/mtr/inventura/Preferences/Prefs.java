package cz.mtr.inventura.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.mtr.inventura.listView.Item;

public class Prefs {

    public static final String MY_PREFS_NAME = "inventura";
    public static final String ITEMS_NAME = "items";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    public Prefs(Context context) {
        prefs = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getIp() {
        return prefs.getString("ip", "");
    }

    public void setIP(String ip) {
        editor = prefs.edit();
        editor.putString("ip", ip);
        editor.apply();
    }

    public int getCurrentDatabaseVersion() {
        return prefs.getInt("currentDbVersion", -99);
    }

    public void setCurrentDatabaseVersion(int currentDbVersion) {
        editor = prefs.edit();
        editor.clear();
        editor.putInt("currentDbVersion", currentDbVersion);
//        editor.apply();
        editor.commit(); // Application will be restarted after this is called. I am forced to use commit instead of apply because its immediate, using apply wont work because data wont be saved fast enough.
    }


    public void setItems(ArrayList<Item> items) {
        editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        editor.putString("items", json);
        editor.apply();
    }

    public ArrayList<Item> getItems() {
        try {
            Gson gson = new Gson();
            String json = prefs.getString("items", null);
            Type type = new TypeToken<ArrayList<Item>>() {
            }.getType();
            ArrayList<Item> items;
            items = gson.fromJson(json, type);
            if (items == null) {
                items = new ArrayList<>();
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

package com.meowt.wallpapers;

import android.support.annotation.NonNull;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JSONParser {

    private static final String MAIN_URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1cjaGVvl0PXJcjfDdS6UCr3QZLvxPyoNXwxPILE_ZhKY&sheet=Sheet1";
    public static final String TAG = "TAG";
    private static Response response;
    public static JSONObject getDataFromWeb() {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(MAIN_URL).build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {

        }
        return null;
    }
}

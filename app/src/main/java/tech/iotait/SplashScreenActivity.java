package tech.iotait;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tech.iotait.helper.Ads;
import tech.iotait.helper.Apis;
import tech.iotait.helper.CustomMethod;
import tech.iotait.model.AdsModel;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private RequestQueue requestQueue;
    public ArrayList<AdsModel> adsCodeM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        adsCodeM = new ArrayList<>();

        CustomMethod.parseJSON(SplashScreenActivity.this);
        adCode();

    }

    private void adCode() {

        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Apis.adsCode, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject item = jsonArray.getJSONObject(i);

                        String key = item.getString("keyId");
                        String value = item.getString("value");
                        String type = item.getString("type");


                        adsCodeM.add(new AdsModel(key, value, type));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SplashScreenActivity.this, "Catch : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                for (int ads = 0; ads < adsCodeM.size(); ads++) {
                    String key = adsCodeM.get(ads).getKey();
                    String value = adsCodeM.get(ads).getAdvalue();
                    if (key.equals("fb_banner")) {
                        Ads.fb_banner = value;
                    } else if (key.equals("g_intial_code")) {
                        Ads.intial = value;
                    } else if (key.equals("g_interstitial")) {
                        Ads.Int_ad = value;
                    } else if (key.equals("g_banner")) {
                        Ads.googleBanner = value;
                    } else if (key.equals("fb_interstitial")) {
                        Ads.fb_Int_ad = value;
                    } else if (key.equals("fb_native")) {
                        Ads.fbNativeAdsCode = value;
                    }
                }

                if (!Ads.fb_Int_ad.equals("")) {
                    goHome();
                } else {
                    adCode();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse response = volleyError.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 403:
                            Toast.makeText(SplashScreenActivity.this, "403 Error", Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(SplashScreenActivity.this, "400 Error", Toast.LENGTH_SHORT).show();

                            break;
                        case 503:
                            Toast.makeText(SplashScreenActivity.this, "503 Error", Toast.LENGTH_SHORT).show();

                            break;

                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String key = "";
                try {
                    key = Ads.generateKey();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                headers.put("PUBLIC-KEY", key);


                return headers;
            }
        };
        queue.add(request);
    }

    private void goHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

package tech.iotait;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tech.iotait.helper.Apis;

public class NewsDetails extends AppCompatActivity {


    String news_id;
    private RequestQueue mRequestQueu;

    WebView html_details;


    WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        news_id = getIntent().getStringExtra("id");
        mRequestQueu = Volley.newRequestQueue(this);
        parseJSONDetails(news_id);

        mWeb=findViewById(R.id.my_web);
    }

    private void parseJSONDetails(String id) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.newsDetails+id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String newsHeadline = response.getString("hline");
                            String myjsonHline=newsHeadline;
                            myjsonHline=  myjsonHline.substring(0,myjsonHline.length()-0);
                            JSONArray myArray = response.getJSONArray("story");
                            for (int i=0;i<myArray.length();i++){
                                String data=myArray.getString(i);
                                mWeb.loadData(data,"text/html; charset=UTF-8", null);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueu.add(request);
    }
}

package tech.iotait.helper;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import tech.iotait.MainActivity;
import tech.iotait.model.Fixture;

public class CustomMethod {
    private static RequestQueue mRequestQueue;
    public static List<Fixture> fixtureList= new ArrayList<>();
    public static String stampDate;
    public static String date;

    public static String getDateFromUTCTimestamp(String mTimestamp, String mDateFormate) {

        try {
            Long timeStamp = Long.valueOf(mTimestamp);
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.setTimeInMillis(timeStamp * 1000L);
            CustomMethod.date = DateFormat.format(mDateFormate, cal.getTimeInMillis()).toString();

            SimpleDateFormat formatter = new SimpleDateFormat(mDateFormate);
            formatter.setTimeZone(TimeZone.getDefault());
            Date value = formatter.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormate);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            CustomMethod.date = dateFormatter.format(value);
            return CustomMethod.date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CustomMethod.date;

    }
    public static void parseJSON(final Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.schedule, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String tmName1 = object.getString("name");
                        String tmImage1 = object.getString("url");
                        String tmName2 = object.getString("alt_url");
                        String tmImage2 = object.getString("image");
                        String matchNum = object.getString("alt_image");
                        String date = object.getString("apk");
                        String stadium_name = object.getString("yitid");
                        if (i == 0) {
                            CustomMethod.stampDate = date;
                            fixtureList.add(new Fixture(tmName1, tmName2, date, matchNum, tmImage1,
                                    tmImage2, stadium_name));
                        }
                    }
                    MainActivity.load = 2;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(request);
    }


}

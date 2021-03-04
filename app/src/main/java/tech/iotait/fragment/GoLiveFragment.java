package tech.iotait.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.iotait.LiveCricketVideoActivity;
import tech.iotait.MainActivity;
import tech.iotait.R;
import tech.iotait.adapter.VideoAdapter;
import tech.iotait.helper.Ads;
import tech.iotait.helper.Apis;
import tech.iotait.model.LiveTv;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoLiveFragment extends Fragment {

    private RecyclerView rvLiveTvList;
    private ImageView ivNoMatch;
    private VideoAdapter adapter;
    private List<LiveTv> liveTvList;
    private RequestQueue mRequestQueue;

    public GoLiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_go_live, container, false);

        rvLiveTvList = mView.findViewById(R.id.tvRecyclerView);
        //ivNoMatch = mView.findViewById(R.id.ivNoMatch);


        liveTvList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getContext());
        parseJSON();
        return mView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.position = 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void parseJSON() {


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.liveTv, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String team_name1 = hit.getString("name");
                                    String Imageurl_1 = hit.getString("url");
                                    String team_name2 = hit.getString("alt_url");
                                    String Imageurl_2 = hit.getString("image");
                                    String v_player_id = hit.getString("alt_image");
                                    String apk = hit.getString("apk");

                                    liveTvList.add(new LiveTv(team_name1, Imageurl_1, team_name2, Imageurl_2, v_player_id, apk));


                                }
                                adapter = new VideoAdapter(getActivity(), liveTvList);
                                rvLiveTvList.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mRequestQueue = Volley.newRequestQueue(getActivity());
                                rvLiveTvList.setAdapter(adapter);

                            } else {
                                //Toast.makeText(LiveTvListActivity.this, "Called", Toast.LENGTH_SHORT).show();
                                ivNoMatch.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ivNoMatch.setVisibility(View.VISIBLE);


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
        mRequestQueue.add(request);
    }
}

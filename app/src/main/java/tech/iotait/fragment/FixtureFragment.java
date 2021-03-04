package tech.iotait.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import tech.iotait.MainActivity;
import tech.iotait.R;
import tech.iotait.adapter.FixtureAdapter;
import tech.iotait.helper.Apis;
import tech.iotait.model.Fixture;

/**
 * A simple {@link Fragment} subclass.
 */
public class FixtureFragment extends Fragment {
    private RecyclerView recyclerView;
    public FixtureAdapter fixtureAdapter;
    private List<Fixture> fixtureList;
    private RequestQueue mRequestQueue;
    private ProgressBar progressBar;
    public FixtureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_fixture, container, false);
        fixtureList = new ArrayList<>();
        // ResyclerView
        recyclerView = mView.findViewById(R.id.fixtureRecyclerView);
        progressBar = mView.findViewById(R.id.progressBar);
        mRequestQueue = Volley.newRequestQueue(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fixtureAdapter = new FixtureAdapter(getContext(), fixtureList);
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
        setRetainInstance(true);
        super.onDestroyView();
    }

    private void parseJSON() {
        progressBar.setVisibility(View.VISIBLE);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.schedule, null, new Response.Listener<JSONObject>() {
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
                        fixtureList.add(new Fixture(tmName1, tmName2, date, matchNum, tmImage1,
                                tmImage2,stadium_name));

                    }
                    recyclerView.setAdapter(fixtureAdapter);
                    fixtureAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(request);
    }
}

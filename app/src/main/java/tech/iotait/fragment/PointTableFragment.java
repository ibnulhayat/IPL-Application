package tech.iotait.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import tech.iotait.adapter.PointTableAdapter;
import tech.iotait.helper.Apis;
import tech.iotait.model.Fixture;
import tech.iotait.model.PointTable;

/**
 * A simple {@link Fragment} subclass.
 */
public class PointTableFragment extends Fragment {
    private RecyclerView recyclerView;
    private PointTableAdapter pointTableAdapter;
    private List<PointTable> pointTablesList;
    private RequestQueue mRequestQueue;
    private ProgressBar progressBar;

    public PointTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_point_table, container, false);


        pointTablesList = new ArrayList<>();
        // ResyclerView
        recyclerView = mView.findViewById(R.id.pointRecyclerView);
        progressBar = mView.findViewById(R.id.progressBar);
        mRequestQueue = Volley.newRequestQueue(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pointTableAdapter = new PointTableAdapter(getContext(), pointTablesList);

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
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Apis.pointtable, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String teamName = hit.getString("name");
                                    String match = hit.getString("url");
                                    String win = hit.getString("alt_url");
                                    String lost = hit.getString("image");
                                    String tie = hit.getString("alt_image");
                                    String point = hit.getString("apk");
                                    String nrr = hit.getString("yitid");
                                    int num = i+1;
                                    String pos = String.valueOf(num);
                                    pointTablesList.add(new PointTable(teamName, match, win, lost, nrr, point,pos));

                                }

                                recyclerView.setAdapter(pointTableAdapter);
                                progressBar.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }

        );
        mRequestQueue.add(request);
    }

}

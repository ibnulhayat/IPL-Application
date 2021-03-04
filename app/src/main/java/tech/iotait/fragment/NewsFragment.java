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
import tech.iotait.adapter.NewsAdapter;
import tech.iotait.helper.Apis;
import tech.iotait.model.NewsModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    public NewsAdapter newsAdapter;
    private List<NewsModel> newsModelList;
    private RequestQueue mRequestQueue;
    private ProgressBar progressBar;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_news, container, false);

        newsModelList = new ArrayList<>();
        recyclerView = mView.findViewById(R.id.newsRecyclerView);
        progressBar = mView.findViewById(R.id.progressBar);
        mRequestQueue = Volley.newRequestQueue(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsAdapter = new NewsAdapter(getContext(), newsModelList);

        parseJSON(Apis.news);
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

    private void parseJSON(String commonUrl) {
            progressBar.setVisibility(View.VISIBLE);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, commonUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("stories");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String id = hit.getString("id");
                                    String hline = hit.getString("hline");
                                    String into = hit.getString("intro");
                                    JSONObject object = hit.getJSONObject("ximg");
                                    String imagUrl = object.getString("ipath");

                                    newsModelList.add(new NewsModel(hline, into, imagUrl, id));
                                }
                                recyclerView.setAdapter(newsAdapter);
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
            }
        }

        );
        mRequestQueue.add(request);
    }


}

package tech.iotait.fragment;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import tech.iotait.MainActivity;
import tech.iotait.R;
import tech.iotait.helper.Apis;
import tech.iotait.model.BattingLeadersModel;
import tech.iotait.model.BowlingLeadersModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaticticsFragment extends Fragment {

    private ImageView iv1stPImage, iv2ndPImage, iv3rdPImage;
    private ImageView iv1stBowPImage, iv2ndBowPImage, iv3rdBowPImage;

    private TextView tv1stPLType, tv2ndPLType, tv3rdPLType;
    private TextView tv1stBowPLType, tv2ndBowPLType, tv3rdBowPLType;

    private TextView tv1stPName, tv2ndPName, tv3rdPName;
    private TextView tv1stBowPName, tv2ndBowPName, tv3rdBowPName;

    private TextView tv1stPRun, tv1stPSix, tv1stPScore;
    private TextView tv1stBowPWickets, tv1stBowPBfigure, tv1stBowPDots;

    private List<BattingLeadersModel> battingList;
    private List<BowlingLeadersModel> bowlingList;
    private RequestQueue mRequestQueue;
    private ProgressBar progressBar;
    private ConstraintLayout layoutstatictics;

    public StaticticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_statictics, container, false);

        battingList = new ArrayList<>();
        bowlingList = new ArrayList<>();
        progressBar = mView.findViewById(R.id.progressBar);
        layoutstatictics = mView.findViewById(R.id.layoutstatictics);

        iv1stPImage = mView.findViewById(R.id.iv1stPImage);
        iv2ndPImage = mView.findViewById(R.id.iv2ndPImage);
        iv3rdPImage = mView.findViewById(R.id.iv3rdPImage);
        iv1stBowPImage = mView.findViewById(R.id.iv1stBowPImage);
        iv2ndBowPImage = mView.findViewById(R.id.iv2ndBowPImage);
        iv3rdBowPImage = mView.findViewById(R.id.iv3rdBowPImage);

        tv1stPLType = mView.findViewById(R.id.tv1stPLType);
        tv2ndPLType = mView.findViewById(R.id.tv2ndPLType);
        tv3rdPLType = mView.findViewById(R.id.tv3rdPLType);
        tv1stBowPLType = mView.findViewById(R.id.tv1stBowPLType);
        tv2ndBowPLType = mView.findViewById(R.id.tv2ndBowPLType);
        tv3rdBowPLType = mView.findViewById(R.id.tv3rdBowPLType);

        tv1stPName = mView.findViewById(R.id.tv1stPName);
        tv2ndPName = mView.findViewById(R.id.tv2ndPName);
        tv3rdPName = mView.findViewById(R.id.tv3rdPName);
        tv1stBowPName = mView.findViewById(R.id.tv1stBowPName);
        tv2ndBowPName = mView.findViewById(R.id.tv2ndBowPName);
        tv3rdBowPName = mView.findViewById(R.id.tv3rdBowPName);

        tv1stPRun = mView.findViewById(R.id.tv1stPRun);
        tv1stPSix = mView.findViewById(R.id.tv1stPSix);
        tv1stPScore = mView.findViewById(R.id.tv1stPScore);
        tv1stBowPWickets = mView.findViewById(R.id.tv1stBowPWickets);
        tv1stBowPBfigure = mView.findViewById(R.id.tv1stBowPBfigure);
        tv1stBowPDots = mView.findViewById(R.id.tv1stBowPDots);

        mRequestQueue = Volley.newRequestQueue(getContext());
        parseJSON(Apis.batting);
        parseJSONBowling(Apis.bowling);
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
                            JSONArray jsonArray = response.getJSONArray("items");

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String playerName = hit.getString("name");
                                    String image = hit.getString("url");
                                    String score = hit.getString("alt_url");
                                    String leading = hit.getString("image");

                                    battingList.add(new BattingLeadersModel(playerName, image, score, leading));
                                }
                                progressBar.setVisibility(View.GONE);
                                layoutstatictics.setVisibility(View.VISIBLE);

                                for (int i = 0; i <battingList.size();i++){
                                    Picasso.get().load(battingList.get(0).getImageUrl()).into(iv1stPImage);
                                    Picasso.get().load(battingList.get(1).getImageUrl()).into(iv2ndPImage);
                                    Picasso.get().load(battingList.get(2).getImageUrl()).into(iv3rdPImage);
                                    tv1stPLType.setText(battingList.get(0).getPlayerLeading());
                                    tv2ndPLType.setText(battingList.get(1).getPlayerLeading());
                                    tv3rdPLType.setText(battingList.get(2).getPlayerLeading());
                                    tv1stPName.setText(battingList.get(0).getName());
                                    tv2ndPName.setText(battingList.get(1).getName());
                                    tv3rdPName.setText(battingList.get(2).getName());
                                    tv1stPRun.setText(battingList.get(0).getPlayerScore());
                                    tv1stPSix.setText(battingList.get(1).getPlayerScore());
                                    tv1stPScore.setText(battingList.get(2).getPlayerScore());
                                }

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


    private void parseJSONBowling(String commonUrl) {

        progressBar.setVisibility(View.VISIBLE);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, commonUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("items");

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String playerName = hit.getString("name");
                                    String image = hit.getString("url");
                                    String score = hit.getString("alt_url");
                                    String leading = hit.getString("image");

                                    bowlingList.add(new BowlingLeadersModel(playerName, image, score, leading));
                                }
                                progressBar.setVisibility(View.GONE);
                                layoutstatictics.setVisibility(View.VISIBLE);

                                for (int i = 0; i <bowlingList.size();i++){
                                    Picasso.get().load(bowlingList.get(0).getImageUrl()).into(iv1stBowPImage);
                                    Picasso.get().load(bowlingList.get(1).getImageUrl()).into(iv2ndBowPImage);
                                    Picasso.get().load(bowlingList.get(2).getImageUrl()).into(iv3rdBowPImage);
                                    tv1stBowPLType.setText(bowlingList.get(0).getPlayerLeading());
                                    tv2ndBowPLType.setText(bowlingList.get(1).getPlayerLeading());
                                    tv3rdBowPLType.setText(bowlingList.get(2).getPlayerLeading());
                                    tv1stBowPName.setText(bowlingList.get(0).getName());
                                    tv2ndBowPName.setText(bowlingList.get(1).getName());
                                    tv3rdBowPName.setText(bowlingList.get(2).getName());
                                    tv1stBowPWickets.setText(bowlingList.get(0).getPlayerScore());
                                    tv1stBowPBfigure.setText(bowlingList.get(1).getPlayerScore());
                                    tv1stBowPDots.setText(bowlingList.get(2).getPlayerScore());
                                }
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

package tech.iotait.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.iotait.MainActivity;
import tech.iotait.R;
import tech.iotait.adapter.LiveScoreAdapter;
import tech.iotait.helper.Apis;
import tech.iotait.model.LiveScore;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveScoreFragment extends Fragment {
    private RecyclerView rvLiveScore;
    private List<LiveScore> liveScoreList;
    private LiveScoreAdapter favTeamLiveScoreAdapter;
    private ProgressDialog progressDialog;

    private RequestQueue mRequestQueue;

    public LiveScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView =  inflater.inflate(R.layout.fragment_live_score, container, false);
        rvLiveScore = mView.findViewById(R.id.rvLiveScore);
        liveScoreList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getContext());
        favTeamLiveScoreAdapter = new LiveScoreAdapter(liveScoreList);
        rvLiveScore.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...!");
        progressDialog.show();

        parseLiveScoreDetails();
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

    private void parseLiveScoreDetails() {
        StringRequest request = new StringRequest(Request.Method.GET, Apis.liveScoreLink, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String batTeamName = "";
                    String batTeamSname = "";

                    String bowlTeamname = "";
                    String bowlTeamSname = "";
                    String batTeamImg = "";
                    String bowlTeamImg = "";
                    String target = "";

                    JSONArray jsonArray = new JSONArray(response);
                    int numberofItem = jsonArray.length();
                    for (int i = 0; i < numberofItem; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String matchId = object.getString("matchId");
                        String srs = object.getString("srs");
                        String datapath = object.getString("datapath");

                        JSONObject header = object.getJSONObject("header");
                        String mchState = header.getString("mchState");
                        String type = header.getString("type");
                        String mnum = header.getString("mnum");
                        String status = header.getString("status");
                        String NoOfIngs = header.getString("NoOfIngs");

                        if(mchState.equals("inprogress") || mchState.equals("complete")){
                            JSONObject miniscore = object.getJSONObject("miniscore");

                            String batTeamId = miniscore.getString("batteamid");
                            String batteamscore = miniscore.getString("batteamscore");
                            String bowlteamscore = miniscore.getString("bowlteamscore");
                            target = bowlteamscore;
                            if (target.equals("") || target.equals("00")) {
                                target = "0";
                            } else {
                                try {
                                    String[] trg = target.split("/");
                                    int res = Integer.parseInt(trg[0]);
                                    String trget = String.valueOf(res + 1);
                                    target = trget;
                                }catch (Exception e){
                                    target ="0";
                                }
                            }
                            String batteamovers = miniscore.getString("overs");
                            String bowlteamovers = miniscore.getString("bowlteamovers");

                            String batTeamCRR = miniscore.getString("crr");
                            String batTeamRR = miniscore.getString("rrr");

                            JSONObject teamOneObj = object.getJSONObject("team1");
                            String teamOneID = teamOneObj.getString("id");

                            JSONObject teamTwoObj = object.getJSONObject("team2");

                            if(batTeamId.equals(teamOneID)){
                                String teamID = teamOneObj.getString("id");
                                String teamID2 = teamTwoObj.getString("id");

                                batTeamName = teamOneObj.getString("name");
                                batTeamSname = teamOneObj.getString("sName");

                                bowlTeamname = teamTwoObj.getString("name");
                                bowlTeamSname = teamTwoObj.getString("sName");

                                batTeamImg = "http://i.cricketcb.com/cbzandroid/2.0/flags/team_"+teamID+"_50.png";
                                bowlTeamImg = "http://i.cricketcb.com/cbzandroid/2.0/flags/team_" + teamID2 + "_50.png";
                            }else{
                                String teamID = teamOneObj.getString("id");
                                String teamID2 = teamTwoObj.getString("id");

                                batTeamName = teamTwoObj.getString("name");
                                batTeamSname = teamTwoObj.getString("sName");

                                bowlTeamname = teamOneObj.getString("name");
                                bowlTeamSname = teamOneObj.getString("sName");

                                batTeamImg = "http://i.cricketcb.com/cbzandroid/2.0/flags/team_" + teamID2 + "_50.png";
                                bowlTeamImg = "http://i.cricketcb.com/cbzandroid/2.0/flags/team_" + teamID + "_50.png";
                            }


                            if(mchState.equals("inprogress")){
                                batTeamCRR = "0";
                                batTeamRR = "0";
                            }else if(mchState.equals("stump")){
                                batTeamRR = "0";
                            }else{
                                //Toast.makeText(getActivity(), "RR : " +batTeamRR, Toast.LENGTH_SHORT).show();
                                target = "0";
                            }

                            liveScoreList.add(new LiveScore(mnum + " "+ srs,batTeamId,batTeamSname,batTeamImg,bowlTeamImg,
                                    bowlTeamSname,batteamscore,batteamovers,batTeamRR,batTeamCRR,target,bowlteamscore,
                                    bowlteamovers,status,datapath,mnum,matchId,type,mchState,NoOfIngs));

                        }

                    }
                    rvLiveScore.setHasFixedSize(true);
                    rvLiveScore.setAdapter(favTeamLiveScoreAdapter);
                    favTeamLiveScoreAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Catch Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
        mRequestQueue.add(request);
    }

}

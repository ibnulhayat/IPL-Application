package tech.iotait.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import tech.iotait.R;
import tech.iotait.adapter.BowlerDetailsAdapter;
import tech.iotait.adapter.ScoreDetailsAdapter;
import tech.iotait.model.Batsmans;
import tech.iotait.model.Bowlers;
import tech.iotait.model.Players;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondInsFragment extends Fragment {

    RequestQueue tQueue;
    TextView txtTeam, txtRans, txtWickets, txtOvers;

    private RecyclerView mRecyclerFullScore;
    private RecyclerView mRecyclerBowlers;
    private RecyclerView mRecyclerFWicket;
    private ScoreDetailsAdapter mScoreAdapter;
    private BowlerDetailsAdapter mBowlerAdapter;
    private List<Batsmans> mBatsmans;
    private List<Bowlers> mBowlers;
    private List<Players> mPlayers;


    public SecondInsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_ins, container, false);

        String datapath = this.getArguments().getString("data_path");

        mRecyclerFullScore = view.findViewById(R.id.recycler_score_details);
        mRecyclerFullScore.setHasFixedSize(true);
        mRecyclerFullScore.setNestedScrollingEnabled(true);
        mRecyclerFullScore.setLayoutManager(new LinearLayoutManager(getContext()));

        //bowler
        mRecyclerBowlers = view.findViewById(R.id.recycler_bowler);
        mRecyclerBowlers.setHasFixedSize(true);
        mRecyclerBowlers.setLayoutManager(new LinearLayoutManager(getContext()));


        mBatsmans = new ArrayList<>();
        mBowlers = new ArrayList<>();
        mPlayers = new ArrayList<>();

        tQueue = Volley.newRequestQueue(getContext());
        txtTeam = view.findViewById(R.id.txtTeamName);
        parseJsonData(datapath);
        return view;
    }

    private void parseJsonData(String datpath) {
        String url = "http://synd.cricbuzz.com/iphone/3.0/match/" + datpath + "/scorecard.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String pfname = null;
                    String bId="";
                    String pFname="";
                    String pname="";
                    String  bowlerId = "";
                    String batFid = "";

                    //for heading item
                    JSONObject object = response.getJSONObject("Innings");
                    JSONObject firstTeam = object.getJSONObject("2");

                    String battingTeam = firstTeam.getString("battingteam");
                    String totalScore = firstTeam.getString("runs");
                    String totalWicket = firstTeam.getString("wickets");
                    String totalOvars = firstTeam.getString("overs");

                    //txtTeam.setText(" " + battingTeam + " ");


                    //player list for name
                    final JSONArray player = response.getJSONArray("players");
                    for (int i=0;i<player.length();i++){
                        JSONObject players=player.getJSONObject(i);
                        String pid=players.getString("id");
                        pFname=players.getString("fName");
                        pname=players.getString("name");
                        mPlayers.add(new Players(pid,pFname,pname));
                    }

                    //for batsnam details
                    JSONArray object2 = firstTeam.getJSONArray("batsmen");
                    for (int i = 0; i < object2.length(); i++) {
                        JSONObject batObj = object2.getJSONObject(i);

                        for (int j =0; j<player.length();j++){
                            JSONObject plyObj =player.getJSONObject(j);
                            if (plyObj.getString("id").equals(batObj.getString("batsmanId")))
                                bId = plyObj.getString("fName");
                        }

                        String bRans = batObj.getString("run");
                        String bBalls = batObj.getString("ball");
                        String bFours = batObj.getString("four");
                        String bSixs = batObj.getString("six");
                        String bStikeRate = batObj.getString("sr");
                        String bOutDes = batObj.getString("outdescription");

                        mBatsmans.add(new Batsmans(bId, bRans, bBalls, bFours, bSixs, bStikeRate, bOutDes));
                    }



                    //for bowler details
                    JSONArray object3 = firstTeam.getJSONArray("bowlers");
                    for (int i = 0; i < object3.length(); i++) {
                        JSONObject playerObject=player.getJSONObject(i);
                        JSONObject bowler = object3.getJSONObject(i);

                        for (int j =0; j<player.length();j++){
                            JSONObject plyObj =player.getJSONObject(j);
                            if (plyObj.getString("id").equals(bowler.getString("bowlerId")))
                                bowlerId = plyObj.getString("fName");
                        }

                        String bowlerOver = bowler.getString("over");
                        String bowlerMaiden = bowler.getString("maiden");
                        String bowlerRans = bowler.getString("run");
                        String bowlerWickets = bowler.getString("wicket");
                        String bowlerStikeRate = bowler.getString("sr");


                        mBowlers.add(new Bowlers(bowlerId, bowlerOver, bowlerMaiden, bowlerRans, bowlerWickets, bowlerStikeRate));
                    }



                    mScoreAdapter = new ScoreDetailsAdapter(getContext(), mBatsmans,mPlayers);
                    mRecyclerFullScore.setNestedScrollingEnabled(false);
                    mRecyclerFullScore.setAdapter(mScoreAdapter);

                    mBowlerAdapter = new BowlerDetailsAdapter(getContext(), mBowlers,mPlayers);
                    mRecyclerBowlers.setNestedScrollingEnabled(false);
                    mRecyclerBowlers.setAdapter(mBowlerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        tQueue.add(request);
    }

}

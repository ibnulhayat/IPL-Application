package tech.iotait;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

import tech.iotait.adapter.BowlerDetailsAdapter;
import tech.iotait.adapter.ScoreDetailsAdapter;
import tech.iotait.fragment.FirstInsFragment;
import tech.iotait.fragment.FouthInsFragment;
import tech.iotait.fragment.SecondInsFragment;
import tech.iotait.fragment.ThirdInsFragment;
import tech.iotait.model.Batsmans;
import tech.iotait.model.Bowlers;
import tech.iotait.model.Players;

import static java.security.AccessController.getContext;

public class ScoreDetailsActivity extends AppCompatActivity {

    private TextView tvToolbarName;

    String datapath,batTeamFlag,bowlTeamFlag,batTeamName,bowlTeamName,series,
            mnum,status,batTeamScore,bowlTeamScore,batTeamOvr,bowlTeamOvr,NoOfIngs;

    private TextView txt_score_details_team_one,txt_score_details_team_two,
            txt_score_details_team_one_run,txt_score_details_team_two_run,txt_score_details_status;
    private ImageView img_score_details_first_team_logo,img_score_details_second_team_logo;

    private TextView btnFirstIns,btnSecondIns,btnTrirdIns,btnForthIns;
    private FirstInsFragment firstInsFragment;
    private SecondInsFragment secondInsFragment;
    private ThirdInsFragment thirdInsFragment;
    private FouthInsFragment fouthInsFragment;
    private RequestQueue requestQueue;
    RequestQueue tQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_details);

        tvToolbarName = findViewById(R.id.tvToolbarName);
        tvToolbarName.setText("Full Scorecard");
        requestQueue = Volley.newRequestQueue(this);


        if (!getIntent().getStringExtra("dataPath").equals(null)){
            datapath=getIntent().getStringExtra("dataPath");
            batTeamFlag=getIntent().getStringExtra("batTeamFlag");
            bowlTeamFlag=getIntent().getStringExtra("bowlTeamFlag");
            batTeamName=getIntent().getStringExtra("batTeamName");
            bowlTeamName=getIntent().getStringExtra("bowlTeamName");
            series=getIntent().getStringExtra("series");
            mnum=getIntent().getStringExtra("mnum");
            status=getIntent().getStringExtra("status");
            batTeamScore=getIntent().getStringExtra("batTeamScore");
            bowlTeamScore=getIntent().getStringExtra("bowlTeamScore");
            batTeamOvr=getIntent().getStringExtra("batTeamOvr");
            bowlTeamOvr=getIntent().getStringExtra("bowlTeamOvr");
            NoOfIngs=getIntent().getStringExtra("NoOfIngs");
        }
        txt_score_details_team_one=findViewById(R.id.txt_score_details_team_one);
        txt_score_details_team_two=findViewById(R.id.txt_score_details_team_two);
        txt_score_details_team_one_run=findViewById(R.id.txt_score_details_team_one_run);
        txt_score_details_team_two_run=findViewById(R.id.txt_score_details_team_two_run);
        txt_score_details_status=findViewById(R.id.txt_score_details_status);

        img_score_details_first_team_logo=findViewById(R.id.img_score_details_first_team_logo);
        img_score_details_second_team_logo=findViewById(R.id.img_score_details_second_team_logo);

        txt_score_details_team_one.setText(batTeamName);
        txt_score_details_team_two.setText(bowlTeamName);


        txt_score_details_status.setText(status);

        if (NoOfIngs.equals("1")){
            txt_score_details_team_one_run.setText(batTeamScore+" \n "+batTeamOvr);
            txt_score_details_team_two_run.setText("");
        }else if (NoOfIngs.equals("2")){
            txt_score_details_team_two_run.setText(bowlTeamScore+" \n"+bowlTeamOvr);
            txt_score_details_team_one_run.setText(batTeamScore+" \n"+batTeamOvr);
        }
        else if (NoOfIngs.equals("3")){
            txt_score_details_team_one_run.setText(batTeamScore+" \n"+batTeamOvr);
            txt_score_details_team_two_run.setText(bowlTeamScore+" \n"+bowlTeamOvr);
        }
        else if (NoOfIngs.equals("4")){
            txt_score_details_team_one_run.setText(batTeamScore+" \n"+batTeamOvr);
            txt_score_details_team_two_run.setText(bowlTeamScore+" \n"+bowlTeamOvr);
        }

        Picasso.get().load(batTeamFlag).placeholder(R.drawable.default_flag).into(img_score_details_first_team_logo);
        Picasso.get().load(bowlTeamFlag).placeholder(R.drawable.default_flag).into(img_score_details_second_team_logo);


        firstInsFragment = new FirstInsFragment();
        secondInsFragment = new SecondInsFragment();
        thirdInsFragment = new ThirdInsFragment();
        fouthInsFragment = new FouthInsFragment();

        btnFirstIns = findViewById(R.id.btnFirstIns);
        btnSecondIns = findViewById(R.id.btnSecondIns);
        btnTrirdIns = findViewById(R.id.btnTrirdIns);
        btnForthIns = findViewById(R.id.btnForthIns);
        btnFirstIns.setBackgroundResource(R.drawable.select_fregment);
        btnSecondIns.setBackgroundResource(R.drawable.unselect_fregment);
        btnTrirdIns.setBackgroundResource(R.drawable.unselect_fregment);
        btnForthIns.setBackgroundResource(R.drawable.unselect_fregment);
        parseInnins(datapath);

        replaceFragment(firstInsFragment,datapath);

        btnFirstIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(firstInsFragment,datapath);
                btnFirstIns.setBackgroundResource(R.drawable.select_fregment);
                btnSecondIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnTrirdIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnForthIns.setBackgroundResource(R.drawable.unselect_fregment);
            }
        });

        btnSecondIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(secondInsFragment,datapath);
                btnFirstIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnSecondIns.setBackgroundResource(R.drawable.select_fregment);
                btnTrirdIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnForthIns.setBackgroundResource(R.drawable.unselect_fregment);

            }
        });

        btnTrirdIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(thirdInsFragment,datapath);
                btnFirstIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnSecondIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnTrirdIns.setBackgroundResource(R.drawable.select_fregment);
                btnForthIns.setBackgroundResource(R.drawable.unselect_fregment);
            }
        });

        btnForthIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(fouthInsFragment,datapath);
                btnFirstIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnSecondIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnTrirdIns.setBackgroundResource(R.drawable.unselect_fregment);
                btnForthIns.setBackgroundResource(R.drawable.select_fregment);
            }
        });

    }

    private void parseInnins(String datapath) {
        String url = "http://synd.cricbuzz.com/iphone/3.0/match/" + datapath + "/scorecard.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject ins = response.getJSONObject("Innings");


                    if(ins.optJSONObject("1") != null){
                        btnFirstIns.setVisibility(View.VISIBLE);
                    }
                    if(ins.optJSONObject("2") != null){
                        btnSecondIns.setVisibility(View.VISIBLE);
                    }
                    if(ins.optJSONObject("3") != null){
                        btnTrirdIns.setVisibility(View.VISIBLE);
                    }
                    if(ins.optJSONObject("4") != null){
                        btnForthIns.setVisibility(View.VISIBLE);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void replaceFragment(Fragment fragment, String dataPath){
        Bundle bundle=new Bundle();
        bundle.putString("data_path", dataPath);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer,fragment);
        fragmentTransaction.commit();
    }
}

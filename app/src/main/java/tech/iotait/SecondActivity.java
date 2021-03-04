package tech.iotait;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import tech.iotait.fragment.FixtureFragment;
import tech.iotait.fragment.GoLiveFragment;
import tech.iotait.fragment.LiveScoreFragment;
import tech.iotait.fragment.NewsFragment;
import tech.iotait.fragment.PointTableFragment;
import tech.iotait.fragment.StaticticsFragment;
import tech.iotait.helper.Ads;
import tech.iotait.helper.Apis;
import tech.iotait.helper.CustomMethod;
import tech.iotait.model.Fixture;

public class SecondActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView ivToolbar;
    private TextView tvToolbarName;

    private TextView tvFixture2, tvNews2, tvStatictics2, tvResults2, tvGoLive2;
    private TextView tvSetting, tvShare2, tvRateUs2;

    private FrameLayout frameLayout;

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        /** Navigation button and Toolbar */
        ivToolbar = findViewById(R.id.ivToolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        tvToolbarName = findViewById(R.id.tvToolbarName);

        /** Navigation Image onClick and open the Navigation Drawer */
        ivToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        /** Navigation Menu Icon findView By Id and work java code */

        tvFixture2 = findViewById(R.id.tvFixture2);
        tvNews2 = findViewById(R.id.tvNews2);
        tvStatictics2 = findViewById(R.id.tvStatictics2);
        tvResults2 = findViewById(R.id.tvResults2);
        tvGoLive2 = findViewById(R.id.tvGoLive2);
        tvShare2 = findViewById(R.id.tvShare2);
        tvRateUs2 = findViewById(R.id.tvRateUs2);

        tvFixture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFragmentChange("fixture");
                tvToolbarName.setText("Fixture");
                closeDrawer();

            }
        });
        tvNews2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickFragmentChange("News");
                tvToolbarName.setText("News");
                closeDrawer();

            }
        });
        tvStatictics2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickFragmentChange("Statictics");
                tvToolbarName.setText("Statictics");
                closeDrawer();
            }
        });
        tvResults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickFragmentChange("pointtable");
                tvToolbarName.setText("Point Table");
                closeDrawer();

            }
        });
        tvGoLive2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickFragmentChange("Go Live");
                tvToolbarName.setText("Live Tv");
                closeDrawer();

            }
        });
        tvShare2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecondActivity.shareApp(SecondActivity.this);
                closeDrawer();
            }
        });
        tvRateUs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecondActivity.appRatting(SecondActivity.this);
                closeDrawer();
            }
        });


        /** Homepage button */
        frameLayout = findViewById(R.id.main_fragment_container);
        String name = getIntent().getStringExtra("name");
        //Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();
        onClickFragmentChange(name);
        if (name.equals("Go Live")){
            tvToolbarName.setText("Live Tv");
        }else if (name.equals("livescore")){
            tvToolbarName.setText("Live Score");
        }else if (name.equals("Statictics")){
            tvToolbarName.setText("Statictics");
        }else if (name.equals("News")){
            tvToolbarName.setText("News");
        }else if (name.equals("fixture")){
            tvToolbarName.setText("Fixture");
        }

    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.START);
    }


    public static void shareApp(Context context) {

        final String appPackageName = BuildConfig.APPLICATION_ID;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareSubText = context.getString(R.string.app_name);
        String shareBodyText = "https://play.google.com/store/apps/details?id=" + appPackageName;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        context.startActivity(Intent.createChooser(shareIntent, "Share With"));
    }

    public static void appRatting(Context context) {

        final String appPackageName = BuildConfig.APPLICATION_ID;
        try {

            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));

        } catch (android.content.ActivityNotFoundException e) {

            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    // onClick Fragment Change Method
    private void onClickFragmentChange(String name) {
        if (name.equals("fixture")) {
            FixtureFragment fixtureFragment = new FixtureFragment();
            changefragment(fixtureFragment);

        } else if (name.equals("News")) {
            NewsFragment newsFragment = new NewsFragment();
            changefragment(newsFragment);

        } else if (name.equals("Statictics")) {
            StaticticsFragment staticticsFragment = new StaticticsFragment();
            changefragment(staticticsFragment);

        } else if (name.equals("pointtable")) {
            PointTableFragment ptFragment = new PointTableFragment();
            changefragment(ptFragment);

        } else if (name.equals("livescore")) {
            LiveScoreFragment liveScoreFragment = new LiveScoreFragment();
            changefragment(liveScoreFragment);

        } else if (name.equals("Go Live")) {
            GoLiveFragment goliveFragment = new GoLiveFragment();
            changefragment(goliveFragment);

        }
    }

    // Fragment Changeing this method
    private void changefragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

}

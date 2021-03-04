package tech.iotait;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView ivToolbar;
    private TextView tvToolbarName;

    private TextView tvFixture, tvNews, tvStatictics, tvResults, tvGoLive;
    private TextView tvSetting, tvShare, tvRateUs;

    private LinearLayout layoutFixture, layoutNews, layoutStacti, layoutPointTable,
            layoutLiveScore, layoutLiveTv, LayoutHome;

    public static int position;
    public static int load;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private String EVENT_DATE_TIME;// = "2019-03-12 01:50:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_1, linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private Handler handler = new Handler();
    private Runnable runnable;

    TextView  tvDate, tvTime, tvMatchNum,tvVenu,tvTeamName1,tvTeamName2,tvWatchLive,textView;
    ImageView ivTeam1Img, ivTeam2Img;
    public static String OUTPUT_DATE_FORMATE = "dd/MM/yyyy - hh:mm a";

    private static RequestQueue mRequestQueue;
    private com.facebook.ads.InterstitialAd FbInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    private Dialog mDialogAppClode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartAppSDK.init(this, "202325982", true);
        StartAppAd.disableSplash();

        MobileAds.initialize(this, Ads.intial);
        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Ads.Int_ad);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("224DC558963BFFA0A2F762B6D38D9C90").build());

        if (load ==2){
            CustomMethod.getDateFromUTCTimestamp(CustomMethod.stampDate, DATE_FORMAT);
            EVENT_DATE_TIME = CustomMethod.date;
            initUI();
            countDownStart(EVENT_DATE_TIME);
        }


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

        tvFixture = findViewById(R.id.tvFixture);
        tvNews = findViewById(R.id.tvNews);
        tvStatictics = findViewById(R.id.tvStatictics);
        tvResults = findViewById(R.id.tvResults);
        tvGoLive = findViewById(R.id.tvGoLive);
        tvShare = findViewById(R.id.tvShare);
        tvRateUs = findViewById(R.id.tvRateUs);

        tvFixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","fixture");
                startActivity(intent);
                closeDrawer();
            }
        });
        tvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","News");
                startActivity(intent);
                closeDrawer();
            }
        });
        tvStatictics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","Statictics");
                startActivity(intent);
                closeDrawer();
            }
        });
        tvResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","pointtable");
                startActivity(intent);
                closeDrawer();
            }
        });
        tvGoLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","Go Live");
                startActivity(intent);
                closeDrawer();

            }
        });
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.shareApp(MainActivity.this);
                closeDrawer();
            }
        });
        tvRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.appRatting(MainActivity.this);
                closeDrawer();
            }
        });


        /** Homepage button */
        layoutFixture = findViewById(R.id.layoutFixture);
        layoutNews = findViewById(R.id.layoutNews);
        layoutStacti = findViewById(R.id.layoutStatictics);
        layoutPointTable = findViewById(R.id.layoutPointTable);
        layoutLiveScore = findViewById(R.id.layoutLiveSore);
        layoutLiveTv = findViewById(R.id.layoutLiveTv);
        LayoutHome = findViewById(R.id.LayoutHome);

        layoutFixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixtureAdsLode("fixture");
            }
        });
        layoutNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","News");
                startActivity(intent);
            }
        });
        layoutStacti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","Statictics");
                startActivity(intent);
            }
        });
        layoutPointTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonAdsLode("pointtable");
            }
        });
        layoutLiveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveScoreAdsLode("livescore");
            }
        });
        layoutLiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("name","Go Live");
                startActivity(intent);
            }
        });


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


    private void showDialog() {

        Button yesButton, rateUsButton, noButton;
        mDialogAppClode = new Dialog(MainActivity.this);

        mDialogAppClode.setContentView(R.layout.custom_popup);
        mDialogAppClode.setCancelable(false);
        yesButton = mDialogAppClode.findViewById(R.id.yesButton);
        rateUsButton = mDialogAppClode.findViewById(R.id.rateUsButton);
        noButton = mDialogAppClode.findViewById(R.id.noButton);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogAppClode.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);

            }
        });

        rateUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appRatting(MainActivity.this);
                mDialogAppClode.dismiss();
            }
        });
        mDialogAppClode.show();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
        }else {
            showDialog();
        }

    }

    private void initUI() {
        linear_layout_1 = findViewById(R.id.linear_layout_1);
        linear_layout_2 = findViewById(R.id.linear_layout_2);
        tv_days = findViewById(R.id.tv_days);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        tv_second = findViewById(R.id.tv_second);

        tvDate = findViewById(R.id.tvBPLDate);
        tvTime = findViewById(R.id.tvBPLTime);
        tvMatchNum = findViewById(R.id.tvMatchNum);
        ivTeam1Img = findViewById(R.id.ivTeamImage1);
        ivTeam2Img = findViewById(R.id.ivTeamImage2);
        tvVenu = findViewById(R.id.tvVenu);
        tvTeamName1 = findViewById(R.id.tvTeamName1);
        tvTeamName2 = findViewById(R.id.tvTeamName2);
        tvWatchLive = findViewById(R.id.tvWatchLive);
        textView = findViewById(R.id.textView);

        for (int i = 0; i<CustomMethod.fixtureList.size();i++){
            final Fixture match = CustomMethod.fixtureList.get(i);
            String tName1 = match.getTeamname1();
            String tName2 = match.getTeamname2();
            String DATE = match.getDate();
            String MATCHno = match.getMarchNum();
            String image1 = match.getTeam1img();
            String image2 = match.getTeam2img();
            CustomMethod.getDateFromUTCTimestamp(DATE, OUTPUT_DATE_FORMATE);
            String final_date = CustomMethod.date.substring(0, CustomMethod.date.length() - 11);
            final String final_time = CustomMethod.date.substring(13, CustomMethod.date.length() - 0);

            tvDate.setText(final_date);
            tvTime.setText(final_time);
            tvMatchNum.setText(MATCHno);
            tvVenu.setText(match.getStadium_name());
            Picasso.get().load(image1).fit().into(ivTeam1Img);
            Picasso.get().load(image2).fit().into(ivTeam2Img);
            tvTeamName1.setText(tName1);
            tvTeamName2.setText(tName2);
            textView.setText("VS");
        }
    }

    private void countDownStart(final String time) {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(time);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));
                    } else {
                        linear_layout_1.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    // Ads Load Method
    private void fixtureAdsLode(final String name){
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = Apis.adsControl;
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {

                        JSONObject fixobject = response.getJSONObject(i);
                        JSONObject object1 = fixobject.getJSONObject("fixture");
                        String action  = object1.getString("action");
                        //Toast.makeText(MainActivity.this, ""+action, Toast.LENGTH_SHORT).show();
                        if(action.equals("0")){
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        // Load the next interstitial.
                                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("224DC558963BFFA0A2F762B6D38D9C90").build());
                                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                                        intent.putExtra("name",name);
                                        startActivity(intent);

                                    }

                                });
                            } else {
                                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                        }else if(action.equals("1")){
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);

                            FbInterstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), Ads.fb_Int_ad);
                            FbInterstitialAd.setAdListener(new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(Ad ad) {
                                }

                                @Override
                                public void onInterstitialDismissed(Ad ad) {


                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {

                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    FbInterstitialAd.show();
                                }

                                @Override
                                public void onAdClicked(Ad ad) {
                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {
                                }
                            });

                            AdSettings.addTestDevice("224DC558963BFFA0A2F762B6D38D9C90");
                            FbInterstitialAd.loadAd();
                        }else{
                            // smart ads load here
                            //HERE HERE
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);
                            StartAppAd.showAd(MainActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mRequestQueue.add(request);
    }

    private void jsonAdsLode(final String name){
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = Apis.adsControl;
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {

                        JSONObject fixobject = response.getJSONObject(i);
                        JSONObject object1 = fixobject.getJSONObject("pointtable");
                        String action  = object1.getString("action");

                        if(action.equals("0")){
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        // Load the next interstitial.
                                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("224DC558963BFFA0A2F762B6D38D9C90").build());
                                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                                        intent.putExtra("name",name);
                                        startActivity(intent);
                                    }

                                });
                            } else {
                                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                        }else if(action.equals("1")){
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);

                            FbInterstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), Ads.fb_Int_ad);
                            FbInterstitialAd.setAdListener(new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(Ad ad) {
                                }

                                @Override
                                public void onInterstitialDismissed(Ad ad) {


                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {

                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    FbInterstitialAd.show();
                                }

                                @Override
                                public void onAdClicked(Ad ad) {
                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {
                                }
                            });

                            AdSettings.addTestDevice("224DC558963BFFA0A2F762B6D38D9C90");
                            FbInterstitialAd.loadAd();
                        }else{
                            // smart ads load here
                            //HERE HERE
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);
                            StartAppAd.showAd(MainActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mRequestQueue.add(request);
    }

    private void liveScoreAdsLode(final String name){
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = Apis.adsControl;
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {

                        JSONObject object = response.getJSONObject(i);
                        JSONObject object1 = object.getJSONObject("livescore");
                        String action  = object1.getString("action");

                        if(action.equals("0")){
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        // Load the next interstitial.
                                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("224DC558963BFFA0A2F762B6D38D9C90").build());
                                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                                        intent.putExtra("name",name);
                                        startActivity(intent);
                                    }

                                });
                            } else {
                                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                        }else if(action.equals("1")){
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);

                            FbInterstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(), Ads.fb_Int_ad);
                            FbInterstitialAd.setAdListener(new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(Ad ad) {
                                }

                                @Override
                                public void onInterstitialDismissed(Ad ad) {


                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {

                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    FbInterstitialAd.show();
                                }

                                @Override
                                public void onAdClicked(Ad ad) {
                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {
                                }
                            });

                            AdSettings.addTestDevice("224DC558963BFFA0A2F762B6D38D9C90");
                            FbInterstitialAd.loadAd();
                        }else{
                            // smart ads load here
                            //HERE HERE
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);

                            StartAppAd.showAd(MainActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

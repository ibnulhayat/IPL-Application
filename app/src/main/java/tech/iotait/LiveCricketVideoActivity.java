package tech.iotait;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.exoplayer2.Player;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.startapp.android.publish.adsCommon.StartAppAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tech.iotait.FloatingPlayer.FloatingViewService;
import tech.iotait.helper.Ads;
import tech.iotait.helper.Apis;

public class LiveCricketVideoActivity extends AppCompatActivity implements OnPreparedListener, View.OnClickListener {
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private VideoView videoView;
    private AdView mFbAddView;
    private LinearLayout adContainer;
    private com.google.android.gms.ads.AdView adView;
    private ImageView landScripButton;
    private String Videourl, small_url;
    private TextView showCurrentTime, showRecivedMb;
    private String mTime;

    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;

    private int mVideoWidth;
    private int mVideoHeight;
    private static RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_live_cricket_video);


        smallBannerAdsLode();

        findViewById(R.id.flottingBtn).setOnClickListener(this);

        small_url = getIntent().getStringExtra("small_url");

        final Intent intent = getIntent();
        Videourl = intent.getStringExtra("url");
        videoView = findViewById(R.id.video_view);
        videoView.setOnPreparedListener(LiveCricketVideoActivity.this);
        videoView.setRepeatMode(Player.REPEAT_MODE_ONE);
        videoView.setVideoRotation(0);

        landScripButton = findViewById(R.id.landScripButton);

        if (TextUtils.isEmpty(small_url)) {
            videoView.setVideoURI(Uri.parse(Videourl));
        } else {
            videoView.setVideoURI(Uri.parse(small_url));
        }

        videoView.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(Exception e) {
                alertMessage();
                videoView.setVisibility(View.GONE);
                return false;
            }
        });

        landScripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LiveCricketVideoActivity.this, FullScreenActivity.class).putExtra("url", Videourl));
                finish();
            }
        });

        showCurrentTime = findViewById(R.id.showCurrentTime);
        showRecivedMb = findViewById(R.id.showRecivedMb);

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTime = new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
                showCurrentTime.setText(mTime);
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

        //for internet consumption
        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        } else {
            mHandler.postDelayed(mRunnable, 1000);
        }

    }

    private void smallBannerAdsLode() {
        mRequestQueue = Volley.newRequestQueue(LiveCricketVideoActivity.this);
        String url = Apis.adsControl;
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject fixobject = response.getJSONObject(i);
                        JSONObject object1 = fixobject.getJSONObject("smallbanner");
                        String action = object1.getString("action");
                        //Toast.makeText(MainActivity.this, ""+action, Toast.LENGTH_SHORT).show();
                        if (action.equals("0")) {
                            MobileAds.initialize(getApplicationContext(), Ads.googleBanner);
                            adView = findViewById(R.id.adView);
                            adView.setVisibility(View.VISIBLE);
                            AdRequest adRequest = new AdRequest.Builder().build();
                            adView.loadAd(adRequest);
                        } else if (action.equals("1")) {

                            //============Fb Banner Ad  implement===========
                            mFbAddView = new AdView(LiveCricketVideoActivity.this, Ads.fb_banner, AdSize.BANNER_HEIGHT_50);
                            adContainer = findViewById(R.id.banner_container);
                            adContainer.addView(mFbAddView);
                            mFbAddView.setAdListener(new AdListener() {
                                @Override
                                public void onError(Ad ad, AdError adError) {

                                }

                                @Override
                                public void onAdLoaded(Ad ad) {

                                }

                                @Override
                                public void onAdClicked(Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {

                                }
                            });
                            mFbAddView.loadAd();


                        } else {
                            // smart ads load here
                            //HERE HERE

                            //StartAppAd.showAd(LiveCricketVideoActivity.this);
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


    final Runnable mRunnable = new Runnable() {
        public void run() {
            double rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            double KB = rxBytes / 1024;
            double MB = KB / 1024;
            double GB = MB / 1024;
            if (KB <= 1024) {
                showRecivedMb.setText(String.format("%.2f", KB) + " KB");
            } else if (KB >= 1024) {
                showRecivedMb.setText(String.format("%.2f", MB) + " MB");
            } else if (MB >= 1024) {
                showRecivedMb.setText(String.format("%.2f", GB) + " GB");
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    @Override
    public void onPrepared() {
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }

    private void alertMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please try another link");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });
        builder.show();
    }

    private void permissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required");
        builder.setMessage("BPL Live needs permission to draw over floating player in order to enable this feature");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                askPermission();
            }
        });

        builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            permissionDialog();
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Your Device not supported this feature", Toast.LENGTH_LONG).show();
        } else {
            if (TextUtils.isEmpty(small_url)) {
                //if (TextUtils.isEmpty(Videourl)) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Intent intent = new Intent(LiveCricketVideoActivity.this, FloatingViewService.class);
                    intent.putExtra("url", Videourl);
                    this.startService(intent);
                    onBackPressed();

                } else if (Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(LiveCricketVideoActivity.this, FloatingViewService.class);
                    intent.putExtra("url", Videourl);
                    this.startService(intent);
                    onBackPressed();

                } else {
                    permissionDialog();
                }
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Intent intent = new Intent(LiveCricketVideoActivity.this, FloatingViewService.class);
                    intent.putExtra("url", small_url);
                    this.startService(intent);
                    backPress();

                } else if (Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(LiveCricketVideoActivity.this, FloatingViewService.class);
                    intent.putExtra("url", small_url);
                    this.startService(intent);
                    backPress();
                }
            }
        }
    }

    private void backPress() {
        Intent intent1 = new Intent(LiveCricketVideoActivity.this, LiveCricketVideoActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
        finish();
    }

}

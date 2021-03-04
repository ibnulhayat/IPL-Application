package tech.iotait.FloatingPlayer;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import com.google.android.exoplayer2.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tech.iotait.R;


public class FloatingLandscapeViewService extends Service implements View.OnClickListener{


    private WindowManager mWindowManager;
    WindowManager.LayoutParams params;
    private View mFloatingView;
    private VideoView videoView;
    private ImageView closeBtn, fullScreenBtn;
    private String url;
    private RelativeLayout layoutLoading;
    private ConstraintLayout buttonEnableAndDisable;
    private TextView showCurrentTime, showRecivedMb;

    private String mTime;

    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;

    //ads
//    private com.facebook.ads.AdView mFbAddView;
//    private LinearLayout adContainer;
//    private AdView adView;

    public FloatingLandscapeViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();




        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_landscape, null);

//        MainActivity main=MainActivity.getInstance();
//        main.hideActionBar();


        //ads

//
//        //============Fb Banner Ad  implement===========
//        mFbAddView = new com.facebook.ads.AdView(this, fbban.FbBannerId, AdSize.BANNER_HEIGHT_50);
//        adContainer = mFloatingView.findViewById(R.id.banner_container);
//        adContainer.addView(mFbAddView);
//        mFbAddView.setAdListener(new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//                MobileAds.initialize(getApplicationContext(), String.valueOf(fbban.GoBannerId));
//                adView = mFloatingView.findViewById(R.id.adView);
//                adView.setVisibility(View.VISIBLE);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                adView.loadAd(adRequest);
//
//            }
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        });
//        mFbAddView.loadAd();
//
//
//

        mFloatingView.setSystemUiVisibility(
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.

                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


        //setting the layout parameters
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.screenOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        }
        else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.screenOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        }

        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        videoView = mFloatingView.findViewById(R.id.video_view);
        videoView.setRepeatMode(Player.REPEAT_MODE_OFF);
        videoView.setRotation(0);
        closeBtn = mFloatingView.findViewById(R.id.closeBtn);
        fullScreenBtn = mFloatingView.findViewById(R.id.fullScreenBtn);
        buttonEnableAndDisable = mFloatingView.findViewById(R.id.buttonEnableAndDisable);
        showCurrentTime = mFloatingView.findViewById(R.id.showCurrentTime);
        showRecivedMb = mFloatingView.findViewById(R.id.showRecivedMb);
        layoutLoading = videoView.findViewById(R.id.img_loading);
        buttonEnableAndDisable.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);

        videoView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        buttonEnableAndDisable.setVisibility(View.VISIBLE);
                        showCurrentTime.setVisibility(View.GONE);
                        showRecivedMb.setVisibility(View.GONE);
                        fullScreenBtn.setVisibility(View.VISIBLE);

                        return true;

                    case MotionEvent.ACTION_UP:
                        final Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                buttonEnableAndDisable.setVisibility(View.GONE);
                                showCurrentTime.setVisibility(View.VISIBLE);
                                showRecivedMb.setVisibility(View.VISIBLE);
                            }
                        },5000);
                        videoView.showControls();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        return true;
                }
                return false;
            }
        });

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTime = new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime());
                showCurrentTime.setText(mTime);
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

        closeBtn.setOnClickListener(this);
        fullScreenBtn.setOnClickListener(this);

        //for internet consumption
        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
        if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uh Oh!");
            alert.setMessage("Your device does not support traffic stat monitoring.");
            alert.show();
        }
        else {
            mHandler.postDelayed(mRunnable, 1000);
        }

    }

    final Runnable mRunnable = new Runnable() {
        public void run() {
            double rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            double KB=rxBytes/1024;
            double MB=KB/1024;
            double GB=MB/1024;
            if (KB<=1024){
                showRecivedMb.setText(String.format("%.2f",KB)+" KB");
            }
            else if (KB>=1024){
                showRecivedMb.setText(String.format("%.2f",MB)+" MB");
            }
            else if (MB>=1024){
                showRecivedMb.setText(String.format("%.2f",GB)+" GB");
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public int onStartCommand (Intent intent, int flags, int startId) {
        if((String) intent.getExtras().get("small_url_land") != null){
            url=(String) intent.getExtras().get("small_url_land");

            videoView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    videoView.start();
                    layoutLoading.setVisibility(View.GONE);
                }
            });
            videoView.setVideoURI(Uri.parse(url));
            videoView.setRepeatMode(Player.REPEAT_MODE_ONE);
            return  Service.START_NOT_STICKY;
        }
        else {
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
            videoView.pause();
            stopSelf();
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.closeBtn) {
            stopSelf();
            stopService(new Intent(this,FloatingViewService.class));
        }
        else if (id==R.id.fullScreenBtn){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Intent intent = new Intent(FloatingLandscapeViewService.this, FloatingViewService.class);
                intent.putExtra("small_url_landscape", url);
                this.startService(intent);
                stopSelf();

            } else if (Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(FloatingLandscapeViewService.this, FloatingViewService.class);
                intent.putExtra("small_url_landscape", url);
                this.startService(intent);

            }
            stopSelf();
        }
    }

}

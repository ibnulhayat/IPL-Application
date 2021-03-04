package tech.iotait.FloatingPlayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.exoplayer2.Player;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.startapp.android.publish.adsCommon.StartAppAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tech.iotait.MainActivity;
import tech.iotait.R;
import tech.iotait.helper.Ads;
import tech.iotait.helper.Apis;

public class FloatingViewService extends Service implements View.OnClickListener{


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

    //ads
    private com.facebook.ads.AdView mFbAddView;
    private LinearLayout adContainer;
    private AdView adView;

    ProgressBar progressBar = null;

    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    private static RequestQueue mRequestQueue;
    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);


        //setting the layout parameters
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        videoView = mFloatingView.findViewById(R.id.video_view);
        videoView.setRepeatMode(Player.REPEAT_MODE_OFF);
        closeBtn = mFloatingView.findViewById(R.id.closeBtn);
        fullScreenBtn = mFloatingView.findViewById(R.id.fullScreenBtn);
        buttonEnableAndDisable = mFloatingView.findViewById(R.id.buttonEnableAndDisable);
        showCurrentTime = mFloatingView.findViewById(R.id.showCurrentTime);
        showRecivedMb = mFloatingView.findViewById(R.id.showRecivedMb);
        layoutLoading = videoView.findViewById(R.id.img_loading);
        buttonEnableAndDisable.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
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
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
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
        if((String) intent.getExtras().get("url") != null){
            url=(String) intent.getExtras().get("url");
            //Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();

            videoView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    videoView.start();
                    layoutLoading.setVisibility(View.GONE);
                }

            });
           videoView.setOnBufferUpdateListener(new OnBufferUpdateListener() {
               @Override
               public void onBufferingUpdate(int percent) {
                   if (percent>=0&percent<=100){
                       layoutLoading.setVisibility(View.GONE);
                   }
                   else {
                       layoutLoading.setVisibility(View.VISIBLE);
                   }
               }
           });


           videoView.setOnErrorListener(new OnErrorListener() {
               @Override
               public boolean onError(Exception e) {
                   Toast.makeText(FloatingViewService.this, "An error occured, Please try again", Toast.LENGTH_SHORT).show();
                   return false;
               }
           });
           if (!videoView.isPlaying());{
               layoutLoading.setVisibility(View.VISIBLE);
            }




            videoView.setVideoURI(Uri.parse(url));
            videoView.setRepeatMode(Player.REPEAT_MODE_ONE);
            return  Service.START_NOT_STICKY;
        }
        else if ((String)intent.getExtras().get("small_url_landscape")!=null){
            url=(String) intent.getExtras().get("small_url_landscape");
            //Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();

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
            stopSelf();
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.closeBtn) {
            stopSelf();
            stopService(new Intent(this,FloatingLandscapeViewService.class));
        }
        else if (id==R.id.fullScreenBtn){
            Intent intent = new Intent(FloatingViewService.this, FloatingLandscapeViewService.class);
            intent.putExtra("small_url_land",url);
            this.startService(intent);
            videoView.pause();
            //Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        }
    }
}

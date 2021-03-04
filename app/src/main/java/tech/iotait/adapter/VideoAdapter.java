package tech.iotait.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.List;

import tech.iotait.LiveCricketVideoActivity;
import tech.iotait.MainActivity;
import tech.iotait.R;
import tech.iotait.helper.Ads;
import tech.iotait.helper.Apis;
import tech.iotait.model.LiveTv;

import static com.facebook.FacebookSdk.getApplicationContext;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<LiveTv> liveTvList;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private com.facebook.ads.InterstitialAd FbInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;


    public VideoAdapter(Context context, List<LiveTv> liveTvList) {
        this.mContext = context;
        this.liveTvList = liveTvList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {

        mContext = parent.getContext();
        StartAppSDK.init(mContext, "202325982", true);
        StartAppAd.disableSplash();
        mRequestQueue = Volley.newRequestQueue(mContext);

        MobileAds.initialize(mContext, Ads.intial);
        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(Ads.Int_ad);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("224DC558963BFFA0A2F762B6D38D9C90").build());

        View view = LayoutInflater.from(mContext).inflate(R.layout.cricketvideoview, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        String date = liveTvList.get(position).getApk();
        String team1 = liveTvList.get(position).getName();
        String team2 = liveTvList.get(position).getAlt_url();
        String flg1 = liveTvList.get(position).getUrl();
        String flg2 = liveTvList.get(position).getImage();
        final String channel = liveTvList.get(position).getAlt_image();

        final ViewHolder myholder = (ViewHolder) viewHolder;
        myholder.setDetails(date, team1, team2, flg1, flg2);
        myholder.watch_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fixtureAdsLode(channel);
            }
        });

    }

    // Ads Load Method
    private void fixtureAdsLode(final String channel) {
        String url = Apis.adsControl;
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject fixobject = response.getJSONObject(i);
                        JSONObject object1 = fixobject.getJSONObject("live");
                        String action = object1.getString("action");
                        if (action.equals("0")) {
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        // Load the next interstitial.
                                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("224DC558963BFFA0A2F762B6D38D9C90").build());

                                        Intent intent = new Intent(mContext, LiveCricketVideoActivity.class);
                                        intent.putExtra("url", channel);
                                        mContext.startActivity(intent);
                                    }

                                });
                            } else {
                                Intent intent = new Intent(mContext, LiveCricketVideoActivity.class);
                                intent.putExtra("url", channel);
                                mContext.startActivity(intent);
                            }
                        } else if (action.equals("1")) {
                            Intent intent = new Intent(mContext, LiveCricketVideoActivity.class);
                            intent.putExtra("url", channel);
                            mContext.startActivity(intent);

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
                        } else {
                            // smart ads load here
                            //HERE HERE
                            Intent intent = new Intent(mContext, LiveCricketVideoActivity.class);
                            intent.putExtra("url", channel);
                            mContext.startActivity(intent);
                            StartAppAd.showAd(mContext);
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


    @Override
    public int getItemCount() {
        return liveTvList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, first_country_name, second_country_name;
        ImageView first_country_flag, second_country_flag;
        private View mAddView;
        Button watch_live;
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            watch_live = mView.findViewById(R.id.watch_live);
        }

        public void setDetails(String date, String fstcountry_name, String snd_country_name, String fst_country_flag, String snd_country_flag) {
            tv_date = mView.findViewById(R.id.tv_date);
            first_country_name = mView.findViewById(R.id.first_country_name);
            second_country_name = mView.findViewById(R.id.second_country_name);
            first_country_flag = mView.findViewById(R.id.first_country_flag);
            second_country_flag = mView.findViewById(R.id.second_country_flag);
            tv_date.setText(date);
            first_country_name.setText(fstcountry_name);
            second_country_name.setText(snd_country_name);
            Picasso.get().load(fst_country_flag).fit().placeholder(R.drawable.default_flag).centerInside().into(first_country_flag);
            Picasso.get().load(snd_country_flag).fit().centerInside().placeholder(R.drawable.default_flag).into(second_country_flag);
        }
    }

}

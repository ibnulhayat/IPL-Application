package tech.iotait.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.iotait.MainActivity;
import tech.iotait.NewsDetails;
import tech.iotait.R;
import tech.iotait.helper.Ads;
import tech.iotait.helper.Apis;
import tech.iotait.model.NewsModel;
import tech.iotait.model.PointTable;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.GraphRequest.TAG;


public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NewsModel> newsModels;
    private NativeAd nativeAd;
    private LinearLayout adView;
    private final String TEG = "NATIVEaD";

    private RequestQueue mRequestQueue;
    private com.facebook.ads.InterstitialAd FbInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;
    public NewsAdapter(Context context, List<NewsModel> newsModels) {
        this.mContext = context;
        this.newsModels = newsModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        StartAppSDK.init(mContext, "202325982", true);
        StartAppAd.disableSplash();

        MobileAds.initialize(mContext, Ads.intial);
        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(Ads.Int_ad);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("28e79a60-5d8f-4585-9aaf-37cc9f4de209").build());

        View v = LayoutInflater.from(mContext).inflate(R.layout.news_item_view, parent, false);

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(mContext);
        ViewHolder viewHolder = new ViewHolder(parent);
        if(viewType % 3 == 0 ){

            viewHolder.loadNativeAd();
        }
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NewsModel point = newsModels.get(position);
        String newsTitel = point.getNewsTitle();
        String newsDescre = point.getNewsDescre();
        String image = point.getNewsImage();
        final String matchID = point.getMatchID();


        ViewHolder myHolder = (ViewHolder) holder;

        Picasso.get().load(image).into(myHolder.newsImage);
        myHolder.tvNewsHLine.setText(newsTitel);
        myHolder.tvNewsDescreption.setText(newsDescre);
        myHolder.newsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixtureAdsLode(matchID);
            }
        });


    }
    private void fixtureAdsLode(final String matchID){
        mRequestQueue = Volley.newRequestQueue(mContext);
        String url = Apis.adsControl;
        final JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {

                        JSONObject fixobject = response.getJSONObject(i);
                        JSONObject object1 = fixobject.getJSONObject("fixture");
                        String action  = object1.getString("action");
                        //Toast.makeText(mContext, ""+action, Toast.LENGTH_SHORT).show();
                        if(action.equals("0")){
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        // Load the next interstitial.
                                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("224DC558963BFFA0A2F762B6D38D9C90").build());
                                        Intent intent = new Intent(mContext, NewsDetails.class);
                                        intent.putExtra("id",matchID);
                                        mContext.startActivity(intent);
                                    }

                                });
                            } else {
                                Intent intent = new Intent(mContext, NewsDetails.class);
                                intent.putExtra("id",matchID);
                                mContext.startActivity(intent);
                            }
                        }else if(action.equals("1")){
                            Intent intent = new Intent(mContext, NewsDetails.class);
                            intent.putExtra("id",matchID);
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
                        }else{
                            // smart ads load here
                            //HERE HERE
                            Intent intent = new Intent(mContext, NewsDetails.class);
                            intent.putExtra("id",matchID);
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNewsHLine,tvNewsDescreption,tvAds;
        ImageView newsImage;
        LinearLayout newsLayout;
        View mView;
        private NativeAdLayout nativeAdLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            tvNewsHLine = mView.findViewById(R.id.tvNewsHLine);
            tvNewsDescreption = mView.findViewById(R.id.tvNewsDescreption);
            newsImage = mView.findViewById(R.id.newsImage);
            newsLayout = mView.findViewById(R.id.newsLayout);

            nativeAdLayout = mView.findViewById(R.id.native_ad_container);
            tvAds = mView.findViewById(R.id.tvAds);
        }

        private void loadNativeAd() {

            nativeAd = new NativeAd(mContext, Ads.fbNativeAdsCode);
            nativeAd.setAdListener(new NativeAdListener() {

                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Race condition, load() called again before last ad was displayed
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    // Inflate Native Ad into Container
                    inflateAd(nativeAd);
                    Log.e(TAG, "Native ad finished downloading all assets.");
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }

            });
            // Request an ad
            nativeAd.loadAd();
        }

        private void inflateAd(NativeAd nativeAd) {

            nativeAd.unregisterView();
            tvAds.setText("Ad");
            tvAds.setVisibility(View.VISIBLE);
            // Add the Ad view into the ad container.
            LayoutInflater inflater = LayoutInflater.from(mContext);
            //Inflate the Ad view.  The layout referenced should be the one you created in the last step.
            adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
            nativeAdLayout.addView(adView);

            // Add the AdOptionsView
            LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(mContext, nativeAd, nativeAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            // Create native UI using the ad metadata.
            AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            // Set the Text.
            nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            // Create a list of clickable views
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);

            // Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdMedia,
                    nativeAdIcon,
                    clickableViews);
        }

    }

}

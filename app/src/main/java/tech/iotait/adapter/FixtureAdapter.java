package tech.iotait.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import tech.iotait.R;
import tech.iotait.helper.Ads;
import tech.iotait.helper.CustomMethod;
import tech.iotait.model.Fixture;

import static com.facebook.GraphRequest.TAG;


public class FixtureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Fixture> matchList;
    private final static int FADE_DURATION = 800;
    public static String OUTPUT_DATE_FORMATE = "dd/MM/yyyy - hh:mm a";
    public final int TYPE_POST = 0;
    public final int TYPE_ADD = 1;
    private String image,title,description,date;
    private NativeAd nativeAd;
    private LinearLayout adView;
    private final String TEG = "NATIVEaD";

    public FixtureAdapter(Context context, List<Fixture> matches) {
        this.mContext = context;
        this.matchList = matches;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.matchlist, parent, false);
        mContext = parent.getContext();
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
        Fixture match = matchList.get(position);

        String DATE = match.getDate();
        String MATCHno = match.getMarchNum();
        String image1 = match.getTeam1img();
        String image2 = match.getTeam2img();
        CustomMethod.getDateFromUTCTimestamp(DATE, OUTPUT_DATE_FORMATE);
        String final_date = CustomMethod.date.substring(0, CustomMethod.date.length() - 11);
        String final_time = CustomMethod.date.substring(13, CustomMethod.date.length() - 0);
        ViewHolder myHolder = (ViewHolder) holder;

        myHolder.tvDate.setText(final_date);
        myHolder.tvTime.setText(final_time);
        myHolder.tvMatchNum.setText(MATCHno);
        myHolder.tvVenu.setText(match.getStadium_name());
        Picasso.get().load(image1).fit().into(myHolder.ivTeam1Img);
        Picasso.get().load(image2).fit().into(myHolder.ivTeam2Img);

        setFadeAnimation(myHolder.mView);

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    //animation
    private void setFadeAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  tvDate, tvTime, tvMatchNum,tvVenu,tvAds;
        View mView;
        ImageView ivTeam1Img, ivTeam2Img;
        private NativeAdLayout nativeAdLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tvDate = mView.findViewById(R.id.tvBPLDate);
            tvTime = mView.findViewById(R.id.tvBPLTime);
            tvMatchNum = mView.findViewById(R.id.tvMatchNum);
            ivTeam1Img = mView.findViewById(R.id.ivTeamImage1);
            ivTeam2Img = mView.findViewById(R.id.ivTeamImage2);
            tvVenu = mView.findViewById(R.id.tvVenu);

            nativeAdLayout = mView.findViewById(R.id.native_ad_container);
            tvAds = mView.findViewById(R.id.tvAds);
        }

        private void loadNativeAd() {
            Toast.makeText(mContext, ""+Ads.fbNativeAdsCode, Toast.LENGTH_SHORT).show();
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

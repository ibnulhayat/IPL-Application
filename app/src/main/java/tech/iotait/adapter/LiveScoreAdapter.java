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
import com.android.volley.toolbox.Volley;
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

import java.util.ArrayList;
import java.util.List;

import tech.iotait.R;
import tech.iotait.ScoreDetailsActivity;
import tech.iotait.helper.Ads;
import tech.iotait.model.LiveScore;

import static com.facebook.GraphRequest.TAG;

public class LiveScoreAdapter extends RecyclerView.Adapter<LiveScoreAdapter.ViewHolder> {
    private List<LiveScore> liveScoreList;
    private Context context;
    private NativeAd nativeAd;
    private LinearLayout adView;
    private final String TEG = "NATIVEaD";

    public LiveScoreAdapter(List<LiveScore> liveScoreList) {
        this.liveScoreList = liveScoreList;
    }

    @NonNull
    @Override
    public LiveScoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.livescore_item,viewGroup,false);

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(context);
        ViewHolder viewHolder = new ViewHolder(viewGroup);
        if(i % 3 == 0){

            viewHolder.loadNativeAd();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LiveScoreAdapter.ViewHolder viewHolder,final int i) {


        final LiveScore items = liveScoreList.get(i);

        final String batTeamName = items.getBatTeamName();
        final String batTeamImg = items.getBatTeamImg();
        final String bowlTeamName = items.getBowlTeamName();
        final String bowlTeamImg = items.getBowlTeamImg();
        final String batTeamScore = items.getBatTeamScore();
        String batTeamOvers = items.getBatTeamOvers();
        final String batTeamRR = items.getBatTeamRR();
        final String batTeamCRR = items.getBatTeamCRR();
        final String bowlTeamScore = items.getBowlTeamScore();
        final String bowlTeamOvrs = items.getBowlTeamOvrs();
        final String target = items.getTarget();
        final String type = items.getType();
        final String matchId = items.getMatchId();

        if(type.equals("ODI")){
            batTeamOvers = batTeamOvers + "/50";
        }else if(type.equals("T20") || type.equals("T20I")){
            batTeamOvers = batTeamOvers + "/20";
        }

        viewHolder.setDetails(batTeamName,batTeamImg,bowlTeamName,bowlTeamImg,batTeamScore,batTeamOvers,batTeamRR,batTeamCRR,bowlTeamScore,bowlTeamOvrs,target);

        if(bowlTeamScore.equals("")){
            viewHolder.tvBowlTeamScore.setVisibility(View.GONE);
        }

        if(bowlTeamOvrs.equals("0")){
            viewHolder.tvBowlTeamOver.setVisibility(View.GONE);
        }

        if(target.equals("0") || target.equals("")){
            viewHolder.tvTarget.setVisibility(View.GONE);
        }

        viewHolder.liveScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LiveScore liveScore=liveScoreList.get(i);
                String batTeamFlag = liveScore.getBatTeamImg();
                String bowlTeamFlag = liveScore.getBowlTeamImg();
                String batTeamName = liveScore.getBatTeamName();
                String bowlTeamName = liveScore.getBowlTeamName();
                String dataPath = liveScore.getDatapath();
                String series = liveScore.getShowDes();
                String mnum = liveScore.getMnum();
                String status = liveScore.getStatus();
                String batTeamScore = liveScore.getBatTeamScore();
                String bowlTeamScore = liveScore.getBowlTeamScore();
                String batTeamOvr = liveScore.getBatTeamOvers();
                String bowlTeamOvr = liveScore.getBowlTeamOvrs();
                String NoOfIngs = liveScore.getNoOfIngs();
                String matchId = liveScore.getMatchId();

                Intent intent = new Intent(context, ScoreDetailsActivity.class);
                intent.putExtra("batTeamFlag",batTeamFlag);
                intent.putExtra("bowlTeamFlag",bowlTeamFlag);
                intent.putExtra("batTeamName",batTeamName);
                intent.putExtra("bowlTeamName",bowlTeamName);
                intent.putExtra("dataPath",dataPath);
                intent.putExtra("series",series);
                intent.putExtra("mnum",mnum);
                intent.putExtra("status",status);
                intent.putExtra("batTeamScore",batTeamScore);
                intent.putExtra("bowlTeamScore",bowlTeamScore);
                intent.putExtra("batTeamOvr",batTeamOvr);
                intent.putExtra("bowlTeamOvr",bowlTeamOvr);
                intent.putExtra("NoOfIngs",NoOfIngs);
                intent.putExtra("matchId",matchId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return liveScoreList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShowDes,tvBatTeamName,tvBowlTeamName,tvBatTeamScore,tvBatTeamOvers,tvBatteamRr,tvBatTeamCrr,tvBowlTeamScore,tvBowlTeamOver,tvTarget,tvStatus;
        ImageView ivBatTeam,ivBowlTeam;
        View mView;
        TextView watchLiveBtn,liveScoreBtn,tvAds;
        private NativeAdLayout nativeAdLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            liveScoreBtn = mView.findViewById(R.id.liveScoreBtn);
            nativeAdLayout = mView.findViewById(R.id.native_ad_container);
            tvAds = mView.findViewById(R.id.tvAds);
        }

        public void setDetails(String batTeamName,String batTeamImg,String bowlTeamName,String bowlTeamImg,
                               String batTeamScore,String batTeamOvers,String batTeamRR,String batTeamCRR,
                               String bowlTeamScore,String bowlTeamOvrs,String target){


            tvBatTeamName = mView.findViewById(R.id.tvBatTeamName);
            tvBatTeamName.setText(batTeamName);

            tvBowlTeamName = mView.findViewById(R.id.tvBowlTeamName);
            tvBowlTeamName.setText(bowlTeamName);

            tvBatTeamScore = mView.findViewById(R.id.tvBatTeamScore);
            tvBatTeamScore.setText("SCORE: " + batTeamScore);

            tvBatTeamOvers = mView.findViewById(R.id.tvBatTeamOvers);
            tvBatTeamOvers.setText("OVER: " + batTeamOvers);


            tvBowlTeamScore = mView.findViewById(R.id.tvBowlTeamScore);
            tvBowlTeamScore.setText("SCORE: " + bowlTeamScore);

            tvBowlTeamOver = mView.findViewById(R.id.tvBowlTeamOver);
            tvBowlTeamOver.setText("OVER: " + bowlTeamOvrs);

            tvTarget = mView.findViewById(R.id.tvTarget);
            tvTarget.setText("TARGET: " + target);


            ivBatTeam = mView.findViewById(R.id.ivBatTeam);
            Picasso.get().load(batTeamImg).placeholder(R.drawable.default_flag).into(ivBatTeam);

            ivBowlTeam = mView.findViewById(R.id.ivBowlTeam);
            Picasso.get().load(bowlTeamImg).placeholder(R.drawable.default_flag).into(ivBowlTeam);
        }


        private void loadNativeAd() {

            nativeAd = new NativeAd(context, Ads.fbNativeAdsCode);
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
            LayoutInflater inflater = LayoutInflater.from(context);
            //Inflate the Ad view.  The layout referenced should be the one you created in the last step.
            adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
            nativeAdLayout.addView(adView);

            // Add the AdOptionsView
            LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
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

package tech.iotait.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import tech.iotait.R;
import tech.iotait.model.Batsmans;
import tech.iotait.model.Players;


public class ScoreDetailsAdapter extends RecyclerView.Adapter<ScoreDetailsAdapter.ScoreViewHolder> {

    Context mContext;
    List<Batsmans> batsmans;
    List<Players> playersList;

    public ScoreDetailsAdapter(Context mContext, List<Batsmans> batsmans, List<Players> player) {
        this.mContext = mContext;
        this.batsmans = batsmans;
        this.playersList = player;

    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.batsman_index,parent,false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {

        //batsnman
        Batsmans batsman = batsmans.get(position);
        final String bid = batsman.getId();
        String bRans=batsman.getRans();
        String bBalls=batsman.getBalls();
        String bFours=batsman.getFours();
        String bSixs=batsman.getSixs();
        String bStikeRate=batsman.getStike_rate();
        String bOutDetails=batsman.getbOutDescriptions();

        //players
        Players players = playersList.get(position);
        final String pid=players.getId();
        String pFName=players.getFname();

        //set view
        holder.txtBatName.setText(bid);
        holder.txtBatRans.setText(bRans);
        holder.txtBatFBalls.setText(bBalls);
        holder.txtBatFour.setText(bFours);
        holder.txtBatSixs.setText(bSixs);
        holder.txtBatStikeRate.setText(bStikeRate);

        if (bOutDetails.equals("not out")){
            holder.contaner_index_1.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        }

        //holder.txtOutDescriptions.setText(bOutDetails);
    }

    @Override
    public int getItemCount() {
        return batsmans.size();

    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView txtBatName,txtBatRans,txtBatFBalls,txtBatFour,txtBatSixs,txtBatStikeRate;
        LinearLayout contaner_index_1;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            txtBatName=itemView.findViewById(R.id.txtBatsmanName);
            txtBatRans=itemView.findViewById(R.id.batsman_rans);
            txtBatFBalls=itemView.findViewById(R.id.batsman_faced_balls);
            txtBatFour=itemView.findViewById(R.id.batsman_fours);
            txtBatSixs=itemView.findViewById(R.id.batsman_sixs);
            txtBatStikeRate=itemView.findViewById(R.id.batsman_stike_rate);
            contaner_index_1=itemView.findViewById(R.id.contaner_index_1);
        }
    }
}

package tech.iotait.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tech.iotait.R;
import tech.iotait.model.Bowlers;
import tech.iotait.model.Players;


public class BowlerDetailsAdapter extends RecyclerView.Adapter<BowlerDetailsAdapter.BowlerViewHolder>{
    Context mContext;
    List<Bowlers> bowler;
    List<Players> player;

    public BowlerDetailsAdapter(Context mContext, List<Bowlers> bowler, List<Players> player) {
        this.mContext = mContext;
        this.bowler = bowler;
        this.player = player;
    }

    @NonNull
    @Override
    public BowlerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.bowlers_index,parent,false);
        return new BowlerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BowlerViewHolder holder, int position) {

        //Bowlers
        Bowlers bowlers=bowler.get(position);
        String bowlerid=bowlers.getBowlerId();
        String bOvers=bowlers.getbOver();
       double over = Double.parseDouble(bOvers);
       double over2 = Double.parseDouble(bOvers);
//        int over2 = Integer.parseInt(bOvers);
        String bMaiden=bowlers.getbMaiden();
        String bRans=bowlers.getbRans();
        String bWicket=bowlers.getbWickets();
        String bEconomy=bowlers.getEconomyRate();

        //Bowlers
        Players players=player.get(position);
        String pid=players.getId();
        String pFName=players.getFname();
        String pName=players.getName();

        //set view
        holder.txtBowlerName.setText(bowlerid);
        holder.txtBowlerovers.setText(bOvers);
        holder.txtBowlerMeiden.setText(bMaiden);
        holder.getTxtBowlerRans.setText(bRans);
        holder.txtBowlerWicket.setText(bWicket);
        holder.txtBowlerStikeRate.setText(bEconomy);


    }

    @Override
    public int getItemCount() {
        return bowler.size();
    }

    public class BowlerViewHolder extends RecyclerView.ViewHolder {

        TextView txtBowlerName,txtBowlerovers,txtBowlerMeiden,getTxtBowlerRans,txtBowlerWicket,txtBowlerStikeRate;
        LinearLayout show_backround_color;

        public BowlerViewHolder(View itemView) {
            super(itemView);

            txtBowlerName=itemView.findViewById(R.id.txtBowlerName);
            txtBowlerovers=itemView.findViewById(R.id.bowler_over);
            txtBowlerMeiden=itemView.findViewById(R.id.bowler_maiden);
            getTxtBowlerRans=itemView.findViewById(R.id.bowler_rans);
            txtBowlerWicket=itemView.findViewById(R.id.bowler_wicket);
            txtBowlerStikeRate=itemView.findViewById(R.id.bowler_economy);
            show_backround_color=itemView.findViewById(R.id.show_backround_color);
        }
    }
}

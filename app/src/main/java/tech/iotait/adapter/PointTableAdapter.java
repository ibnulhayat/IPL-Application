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
import tech.iotait.model.PointTable;


public class PointTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PointTable> pointtable;

    public PointTableAdapter(Context context, List<PointTable> pointTable) {
        this.mContext = context;
        this.pointtable = pointTable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.point_table_item_view, parent, false);
        mContext = parent.getContext();

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PointTable point = pointtable.get(position);
        final String teamName = point.getTeamName();
        String totalMatch = point.getTotalmatch();
        String win = point.getWinn();
        String lost = point.getLost();
        String nrr = point.getNrr();
        String points = point.getPoint();
        String pos = point.getPos();

        ViewHolder myHolder = (ViewHolder) holder;

        myHolder.tvTotalMatch.setText(totalMatch);
        myHolder.tvWin.setText(win);
        myHolder.tvLost.setText(lost);
        myHolder.tvNRR.setText(nrr);
        myHolder.tvPoint.setText(points);
        myHolder.tvTeamName.setText(teamName);
        myHolder.tvSeriolNo.setText(pos);


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return pointtable.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeamName, tvTotalMatch, tvWin, tvLost, tvNRR, tvPoint, tvSeriolNo;
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            tvSeriolNo = mView.findViewById(R.id.tvSeriolNo);
            tvTeamName = mView.findViewById(R.id.tvTeamName);
            tvTotalMatch = mView.findViewById(R.id.tvTotalMatch);
            tvWin = mView.findViewById(R.id.tvWinn);
            tvLost = mView.findViewById(R.id.tvLost);
            tvNRR = mView.findViewById(R.id.tvNRR);
            tvPoint = mView.findViewById(R.id.tvPoint);
        }

    }

}

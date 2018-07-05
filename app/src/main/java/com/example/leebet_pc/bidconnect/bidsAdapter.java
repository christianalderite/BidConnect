package com.example.leebet_pc.bidconnect;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class bidsAdapter extends RecyclerView.Adapter<bidsAdapter.MyViewHolder> {

    private List<Bid> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView viewcount, bidtimer, username, timestamp, currbid, buyoutprice, title;

        public MyViewHolder(View view) {
            super(view);
            viewcount = (TextView) view.findViewById(R.id.bid_views_count);
            bidtimer = (TextView) view.findViewById(R.id.bid_timer);
            username = (TextView) view.findViewById(R.id.bid_username);
            timestamp = (TextView) view.findViewById(R.id.bid_timestamp);
            currbid = (TextView) view.findViewById(R.id.bid_currentbid);
            buyoutprice = (TextView) view.findViewById(R.id.bid_buyoutprice);
            title = view.findViewById(R.id.bid_title);
        }
    }


    public bidsAdapter(List<Bid> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bids_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Bid movie = moviesList.get(position);
        //public TextView viewcount, bidtimer, username, timestamp, currbid, buyoutprice;
        holder.viewcount.setText(movie.getViews());
        holder.bidtimer.setText(movie.getTimer());
        holder.username.setText(movie.getUsername());
        holder.timestamp.setText(movie.getTimestamp());
        holder.currbid.setText(movie.getCurrbid());
        holder.buyoutprice.setText(movie.getBuyoutprice());
        holder.title.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

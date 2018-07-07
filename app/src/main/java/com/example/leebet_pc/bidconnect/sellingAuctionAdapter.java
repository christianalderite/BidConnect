package com.example.leebet_pc.bidconnect;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class sellingAuctionAdapter extends RecyclerView.Adapter<sellingAuctionAdapter.MyViewHolder> {

    private List<Bid> moviesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;

    private Integer mode;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView img, time, currbid, buyoutprice, title;

        public MyViewHolder(View view) {
            super(view);
            img = (TextView) view.findViewById(R.id.sell_img);
            time = (TextView) view.findViewById(R.id.sell_time_left);
            currbid = (TextView) view.findViewById(R.id.sell_curr_bid);
            title = (TextView) view.findViewById(R.id.bid_title);
            buyoutprice = (TextView) view.findViewById(R.id.bid_buyoutprice);
        }
    }
    public sellingAuctionAdapter(Integer mode, List<Bid> moviesList) {
        this.mode = mode;
        this.moviesList = moviesList;
    }
    @Override
    public sellingAuctionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.selling_row, parent, false);
        return new sellingAuctionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(sellingAuctionAdapter.MyViewHolder holder, int position) {

        Bid movie = moviesList.get(position);

        //public TextView viewcount, bidtimer, username, timestamp, currbid, buyoutprice;
        holder.img.setText(movie.getImg_url());
        holder.time.setText(movie.getTimer());
        //holder.timestamp.setText(movie.getTimestamp());
        holder.currbid.setText(movie.getCurrbid());
        holder.buyoutprice.setText(movie.getBuyoutprice());
        holder.title.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

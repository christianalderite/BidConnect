package com.example.leebet_pc.bidconnect;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class biddingAuctionAdapter extends RecyclerView.Adapter<biddingAuctionAdapter.MyViewHolder> {

    private List<Bid> moviesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;

    private Integer mode;
    private Context mCont;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  time, currbid, buyoutprice, title, status, highest;
        public CardView img;
        public MyViewHolder(View view) {
            super(view);
            img = (CardView) view.findViewById(R.id.bid_img);
            time = (TextView) view.findViewById(R.id.bid_time_left);
            currbid = (TextView) view.findViewById(R.id.bid_curr_bid);
            title = (TextView) view.findViewById(R.id.bid_item_name);
            buyoutprice = (TextView) view.findViewById(R.id.bid_buyout);
            status = (TextView) view.findViewById(R.id.bid_status);
            highest = (TextView) view.findViewById(R.id.bid_highest_bid);

        }
    }
    public biddingAuctionAdapter(Integer mode, List<Bid> moviesList) {
        this.mode = mode;
        this.moviesList = moviesList;
    }
    @Override
    public biddingAuctionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
            mCont = parent.getContext();
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bidding_row, parent, false);
        return new biddingAuctionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(biddingAuctionAdapter.MyViewHolder holder, int position) {

        Bid movie = moviesList.get(position);

        //public TextView viidtimer, username, timestamp, currbid, buyoutprice;
       // holder.img.setbac(movie.getImg_url()); WILLL SET LATER MGA BOBO
        holder.time.setText(movie.getTimer());
        holder.currbid.setText(movie.getCurrbid());

        holder.buyoutprice.setText(movie.getBuyoutprice());
        holder.title.setText(movie.getTitle());
        holder.status.setText(movie.getStatus());
        holder.highest.setText(movie.getHighestbid());

        float currbid_fl = Float.valueOf(movie.getCurrbid());
        float highbid_fl = Float.valueOf(movie.getHighestbid());

        if (currbid_fl<highbid_fl){
            holder.status.setText("Outbid");
            holder.status.setTextColor(ContextCompat.getColor(mCont, R.color.outbid));
        }
        else{
            holder.status.setText("Winning");
            holder.status.setTextColor(ContextCompat.getColor(mCont, R.color.win));
        }
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

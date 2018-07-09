package com.example.leebet_pc.bidconnect;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class auctionsAdapter extends RecyclerView.Adapter<auctionsAdapter.MyViewHolder> {

    private List<Auction> moviesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;
    private Context mCont;
    private Integer mode;

    private DatabaseReference dbAuctions = FirebaseDatabase.getInstance().getReference("auctions");
    private DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

    private CountDownTimer countDownTimer;
    private long timeLeftinMS = 600000;//10mintes

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView viewcount, bidtimer, username, timestamp, currbid, buyoutprice, title;
        public CardView uson;
        public ImageView itempic;

        public MyViewHolder(View view) {
            super(view);
            viewcount = (TextView) view.findViewById(R.id.bid_views_count);
            bidtimer = (TextView) view.findViewById(R.id.bid_timer);
            //timestamp = (TextView) view.findViewById(R.id.bid_timestamp);
            currbid = (TextView) view.findViewById(R.id.bid_currentbid);
            //buyoutprice = (TextView) view.findViewById(R.id.bid_buyoutprice);
            title = view.findViewById(R.id.bid_title);
            itempic = view.findViewById(R.id.auc_image);

            if(mode==ACTIVITY_HOME){
                username = (TextView) view.findViewById(R.id.bid_username);
                uson = view.findViewById(R.id.bid_bobo_andrew);
            }
        }
    }

    public auctionsAdapter(Integer mode, List<Auction> moviesList) {
        this.mode = mode;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(mode==ACTIVITY_ACCOUNT){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bids_row_account, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bids_row, parent, false);
        }
        mCont = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Auction auction = moviesList.get(position);


        Picasso.get().load(auction.getImg_url()).into(holder.itempic);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
        String today = dateFormat2.format(new Date());
        Log.e("MAMA KO KALBO","today: " + today);
        try {
            Date date1 = dateFormat2.parse(auction.getTimer());
            Date date2 = dateFormat2.parse(today);
            timeLeftinMS = (date1.getTime()) - date2.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(timeLeftinMS <= 0){
            holder.bidtimer.setText("DONE");
        }
        else{
            countDownTimer = new CountDownTimer(timeLeftinMS,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftinMS = millisUntilFinished;

                    //Update UI
                    int seconds = (int) (timeLeftinMS / 1000) % 60;
                    int minutes = (int) ((timeLeftinMS / (1000 * 60)) % 60);
                    int hours = (int) ((timeLeftinMS / (1000 * 60 * 60)) % 24);

                    holder.bidtimer.setText( String.format("%02d:%02d:%02d", hours, minutes, seconds) );
                }

                @Override
                public void onFinish() {
                    countDownTimer.cancel();
                }
            }.start();
        }



        holder.currbid.setText("Current bid temp");
        holder.title.setText(auction.getTitle());

        dbAuctions.child(auction.getAuctionID() + "/views").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.viewcount.setText(dataSnapshot.getValue().toString());
                auction.setViews(Long.parseLong(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbUsers.child(auction.getUsername() + "/username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(holder.username!=null){
                    holder.username.setText( dataSnapshot.getValue().toString() );
                }

            }
            @Override public void onCancelled(@NonNull DatabaseError databaseError) { }});


        if(mode==ACTIVITY_HOME){
            holder.uson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(mCont, ItemPageActivity.class);
                    myIntent.putExtra("auctionKey", auction.getAuctionID()); //Optional parameters
                    mCont.startActivity(myIntent);
                    dbAuctions.child(auction.getAuctionID() + "/views").setValue(auction.getViews() + 1);
                    mCont.startActivity(myIntent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}

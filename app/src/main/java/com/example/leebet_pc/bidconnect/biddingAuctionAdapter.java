package com.example.leebet_pc.bidconnect;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class biddingAuctionAdapter extends RecyclerView.Adapter<biddingAuctionAdapter.MyViewHolder> {

    private List<Auction> moviesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;

    private Integer mode;
    private Context mCont;


    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;
    private long timeLeftinMS = 600000;//10mintes
    private CountDownTimer countDownTimer;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbMyBids;
    private DatabaseReference aucDB;
    private Auction myAuction;
    private ActualBid myHighestBid;
    private ActualBid highestBid;

    private DatabaseReference dbSingleItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  time, currbid, buyoutprice, title, status, highest;
        public ImageView img;
        public Button buyoutbtn, cancelbtn;

        public MyViewHolder(View view) {
            super(view);

            img = (ImageView) view.findViewById(R.id.bid_img);
            time = (TextView) view.findViewById(R.id.bid_time_left);
            currbid = (TextView) view.findViewById(R.id.bid_curr_bid);
            title = (TextView) view.findViewById(R.id.bid_item_name);
            buyoutprice = (TextView) view.findViewById(R.id.bid_buyout);
            status = (TextView) view.findViewById(R.id.bid_status);
            highest = (TextView) view.findViewById(R.id.bid_highest_bid);

            buyoutbtn = (Button) view.findViewById(R.id.bid_buyout_button);
            cancelbtn = (Button) view.findViewById(R.id.bid_cancel_button);

        }
    }
    public biddingAuctionAdapter(Integer mode, List<Auction> moviesList) {
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
    public void onBindViewHolder(final biddingAuctionAdapter.MyViewHolder holder, int position) {

        final Auction auc = moviesList.get(position);
        mAuth = FirebaseAuth.getInstance();

        fbCurrUser = mAuth.getCurrentUser();
        aucDB = mainDB.getReference("auctions").child(auc.getAuctionID());

        holder.buyoutprice.setText(String.valueOf(auc.getBuyoutprice()));
        holder.title.setText(auc.getTitle());

        aucDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    Utilities.loadImage(mCont, dataSnapshot.child("img_url").getValue().toString(), holder.img);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dbAuctionBids = mainDB.getReference("auctionBids").child(auc.getAuctionID()).child(fbCurrUser.getUid()).child(fbCurrUser.getUid());
        dbAuctionBids.orderByChild("bidAmount").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){

                    String newBidz = object.getValue().toString();
                    ActualBid newBid = object.getValue(ActualBid.class);
                    myHighestBid = newBid;

                    holder.currbid.setText(String.valueOf(myHighestBid.getBidAmount()));

                    dbSingleItem = FirebaseDatabase.getInstance().getReference("auctionBids/" + auc.getAuctionID());
                    dbSingleItem.orderByChild("bidAmount").limitToLast(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot object: dataSnapshot.getChildren()){
                                ActualBid newBid = object.getValue(ActualBid.class);
                                highestBid = newBid;

                                holder.highest.setText(String.valueOf(highestBid));

                                Double currbid_fl = myHighestBid.getBidAmount(); //Float.valueOf();
                                Double highbid_fl = highestBid.getBidAmount();

                                if (currbid_fl<highbid_fl){
                                    holder.status.setText("Outbid");
                                    holder.status.setTextColor(ContextCompat.getColor(mCont, R.color.outbid));
                                }
                                else{
                                    holder.status.setText("Winning");
                                    holder.status.setTextColor(ContextCompat.getColor(mCont, R.color.win));
                                }

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        * Fetched from firebase:
        * - time left
        * - get my latest bid.
        * - get highest bid.
        * - status: winning, and outbid
        * */

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
            String today = dateFormat2.format(new Date());

            Date date1 = dateFormat2.parse(auc.getTimer());
            Date date2 = dateFormat2.parse(today);
            timeLeftinMS = (date1.getTime()) - date2.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(timeLeftinMS <= 0){
            holder.time.setText("DONE");
        }
        else{
            countDownTimer = new CountDownTimer(timeLeftinMS,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftinMS = millisUntilFinished;

                    int seconds = (int) (timeLeftinMS / 1000) % 60;
                    int minutes = (int) ((timeLeftinMS / (1000 * 60)) % 60);
                    int hours = (int) ((timeLeftinMS / (1000 * 60 * 60)));

                    holder.time.setText( String.format("%02d:%02d:%02d", hours, minutes, seconds) );
                }
                @Override
                public void onFinish() {
                    countDownTimer.cancel();
                }
            }.start();
        }


        //



    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
    public double getHighestBid(String auctionID){

        dbSingleItem = FirebaseDatabase.getInstance().getReference("auctionBids/" + auctionID);
        dbSingleItem.orderByChild("bidAmount").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    ActualBid newBid = object.getValue(ActualBid.class);
                    highestBid = newBid;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return highestBid.getBidAmount();
    }
}

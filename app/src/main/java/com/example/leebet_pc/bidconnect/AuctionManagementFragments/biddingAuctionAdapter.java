package com.example.leebet_pc.bidconnect.AuctionManagementFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leebet_pc.bidconnect.ActualBid;
import com.example.leebet_pc.bidconnect.Auction;
import com.example.leebet_pc.bidconnect.R;
import com.example.leebet_pc.bidconnect.Utilities;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private String unikSalonga = "";
    private Context mCont;


    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;
    private long timeLeftinMS = 600000;//10mintes
    private CountDownTimer countDownTimer;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbBids;
    private DatabaseReference aucDB;
    private DatabaseReference dbCancel;
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
    public biddingAuctionAdapter(Integer mode, List<Auction> moviesList,Context mContext) {
        this.moviesList = moviesList;
        this.mCont = mContext;
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
    public void onBindViewHolder(final biddingAuctionAdapter.MyViewHolder holder, final int position) {

        final Auction auc = moviesList.get(position);
        mAuth = FirebaseAuth.getInstance();

        fbCurrUser = mAuth.getCurrentUser();
        aucDB = mainDB.getReference("auctions").child(auc.getAuctionID());
        dbBids = mainDB.getReference("auctionBids/"+auc.getAuctionID()+"/"+fbCurrUser.getUid());
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

        DatabaseReference dbAuctionBids = FirebaseDatabase.getInstance().getReference("auctionBids/"+auc.getAuctionID());
        dbAuctionBids.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot object: dataSnapshot.getChildren()){
                    myHighestBid = object.getValue(ActualBid.class);

                    if(myHighestBid.getAuctionID().equalsIgnoreCase(auc.getAuctionID())
                    && myHighestBid.getBidderID().equalsIgnoreCase(fbCurrUser.getUid()))
                    {
                        holder.currbid.setText(String.valueOf(myHighestBid.getBidAmount()));
                        unikSalonga = myHighestBid.getAuctionID();
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                dbCancel = mainDB.getReference("auctionBids/" + auc.getAuctionID() + "/" + myHighestBid.getBidID());
                                dbCancel.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        moviesList.remove(position);
                                        notifyItemRemoved(position);
                                        //this line below gives you the animation and also updates the
                                        //list items after the deleted item
                                        notifyItemRangeChanged(position, getItemCount());
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(mCont);
                builder.setMessage("Cancel this bid?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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
            dbSingleItem = FirebaseDatabase.getInstance().getReference("auctionBids/" + auc.getAuctionID());
            dbSingleItem.orderByChild("bidAmount").limitToLast(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot object: dataSnapshot.getChildren()){
                        ActualBid newBid = object.getValue(ActualBid.class);
                        highestBid = newBid;

                        holder.highest.setText(String.valueOf(highestBid.getBidAmount()));

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

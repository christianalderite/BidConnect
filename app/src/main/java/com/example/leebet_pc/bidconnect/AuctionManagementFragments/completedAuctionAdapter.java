package com.example.leebet_pc.bidconnect.AuctionManagementFragments;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leebet_pc.bidconnect.ActualBid;
import com.example.leebet_pc.bidconnect.Auction;
import com.example.leebet_pc.bidconnect.R;
import com.example.leebet_pc.bidconnect.Utilities;
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

public class completedAuctionAdapter extends RecyclerView.Adapter<completedAuctionAdapter.MyViewHolder> {

    private List<Auction> moviesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;
    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference aucDB;


    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;

    private Integer mode;

    private CountDownTimer countDownTimer;
    private long timeLeftinMS = 600000;//10mintes

    private Context mCont;
    private DatabaseReference dbSingleItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  time, currbid, buyoutprice, title;
        public ImageView img;
        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.sell_img);
            time = (TextView) view.findViewById(R.id.sell_time_left);
            currbid = (TextView) view.findViewById(R.id.sell_curr_bid);
            title = (TextView) view.findViewById(R.id.sell_item_name);
            buyoutprice = (TextView) view.findViewById(R.id.sell_buy_bid);

        }
    }
    public completedAuctionAdapter(Integer mode, List<Auction> moviesList) {
        this.mode = mode;
        this.moviesList = moviesList;
    }
    @Override
    public completedAuctionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

            mCont = parent.getContext();
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.completed_row, parent, false);
        return new completedAuctionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final completedAuctionAdapter.MyViewHolder holder, int position) {

        final Auction auc = moviesList.get(position);


        mAuth = FirebaseAuth.getInstance();

        aucDB = mainDB.getReference("auctions").child(auc.getAuctionID());
        dbSingleItem = mainDB.getReference("auctionBids/"+auc.getAuctionID());

        Log.e("EYUT", "tangina auc id: " + auc.getAuctionID());
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
            String today = dateFormat2.format(new Date());
            Log.e("MAMA KO KALBO","today: " + today);
            try {
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

                        //Update UI
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
            aucDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Utilities.loadImage(mCont, dataSnapshot.child("img_url").getValue().toString(), holder.img);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            dbSingleItem.orderByChild("bidAmount").limitToLast(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot object: dataSnapshot.getChildren()){
                        ActualBid newBid = object.getValue(ActualBid.class);
                        holder.currbid.setText(Double.toString(newBid.getBidAmount()));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e){ }
        holder.time.setText(auc.getTimer());
        holder.buyoutprice.setText(String.valueOf(auc.getBuyoutprice()));
        holder.title.setText(auc.getTitle());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

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

import com.example.leebet_pc.bidconnect.Account;
import com.example.leebet_pc.bidconnect.ActualBid;
import com.example.leebet_pc.bidconnect.Auction;
import com.example.leebet_pc.bidconnect.R;
import com.example.leebet_pc.bidconnect.User;
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
    private List<String> typeList;

    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;
    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference aucDB;
    private DatabaseReference userDB;


    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;

    private Integer mode;

    private CountDownTimer countDownTimer;
    private long timeLeftinMS = 600000;//10mintes

    private Context mCont;
    private DatabaseReference dbSingleItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  time, price, buyoutprice, title, sellername,bidnumber, remark;
        public ImageView img;
        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.comp_img);
            time = (TextView) view.findViewById(R.id.comp_time_ended);
            sellername = (TextView) view.findViewById(R.id.comp_seller);
            price = (TextView) view.findViewById(R.id.comp_price);
            title = (TextView) view.findViewById(R.id.comp_item_name);
            bidnumber = (TextView) view.findViewById(R.id.comp_bid_no);
            remark = (TextView) view.findViewById(R.id.comp_remark);
        }
    }
    public completedAuctionAdapter(Integer mode, List<Auction> moviesList, List<String> types, Context mCont) {
        this.mode = mode;
        this.mCont = mCont;
        this.typeList = types;
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
        final String type = typeList.get(position);

        mAuth = FirebaseAuth.getInstance();
        aucDB = mainDB.getReference("auctions").child(auc.getAuctionID());
        dbSingleItem = mainDB.getReference("auctionBids/"+auc.getAuctionID());


        holder.title.setText(auc.getTitle());
        // ### Time Ended
        holder.time.setText(auc.getTimer());
        holder.sellername.setText(auc.getActualusername());

        // ### Loads Image
        aucDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utilities.loadImage(mCont, dataSnapshot.child("img_url").getValue().toString(), holder.img);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // ### Gets Highest Last Amount and number of bids
        dbSingleItem.orderByChild("bidAmount").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    Log.e("AUC_ADAP:",auc.getTitle() +" ");
                    ActualBid newBid = object.getValue(ActualBid.class);
                    holder.price.setText(Double.toString(newBid.getBidAmount()));
                    holder.bidnumber.setText(Long.toString(dataSnapshot.getChildrenCount()));

                    userDB = mainDB.getReference("users/"+object.getKey());

                    switch (type){
                        case "sell":

                            userDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot object: dataSnapshot.getChildren()){
                                        User acc = object.getValue(User.class);
                                        holder.sellername.setText(acc.getUsername());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            holder.remark.setText("SOLD");
                            break;
                        case "bid":
                            holder.sellername.setText(auc.getActualusername());
                            holder.remark.setText("WON");
                            break;
                        case "buyout":
                            holder.sellername.setText(auc.getActualusername());
                            holder.remark.setText("Bought Out");
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

package com.example.leebet_pc.bidconnect;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class sellingAuctionAdapter extends RecyclerView.Adapter<sellingAuctionAdapter.MyViewHolder> {

    private List<Auction> moviesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;
    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference aucDB;


    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;

    private Integer mode;

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
    public sellingAuctionAdapter(Integer mode, List<Auction> moviesList) {
        this.mode = mode;
        this.moviesList = moviesList;
    }
    @Override
    public sellingAuctionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

            mCont = parent.getContext();
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.selling_row, parent, false);
        return new sellingAuctionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final sellingAuctionAdapter.MyViewHolder holder, int position) {

        Auction auc = moviesList.get(position);

        dbSingleItem = mainDB.getReference("auctionBids").child(auc.getAuctionID());
        mAuth = FirebaseAuth.getInstance();
        aucDB = mainDB.getReference("auctions").child(auc.getAuctionID());
        aucDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utilities.loadImage(mCont, dataSnapshot.child("img_url").getValue().toString(), holder.img);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbSingleItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ActualBid newBid = dataSnapshot.getValue(ActualBid.class);

                Query highestBid = dbSingleItem.child(newBid.getBidID()).orderByChild("bidAmount").limitToLast(1);
                highestBid.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            String Key = childSnapshot.getKey();
                            holder.currbid.setText(Key);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.time.setText(auc.getTimer());
        holder.buyoutprice.setText(String.valueOf(auc.getBuyoutprice()));
        holder.title.setText(auc.getTitle());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

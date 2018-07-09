package com.example.leebet_pc.bidconnect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class commentAuctionAdapter extends RecyclerView.Adapter<commentAuctionAdapter.MyViewHolder> {

    private List<AuctionComment> moviesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference userDB;

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;

    private Integer mode;
    private Context mCont;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  username, timestamp, content;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.bid_comment_pic);
            username = (TextView) view.findViewById(R.id.itempage_bid_comment_username);
            timestamp = (TextView) view.findViewById(R.id.itempage_bid_comment_timestamp);
            content = (TextView) view.findViewById(R.id.itempage_bid_comment_content);

        }
    }
    public commentAuctionAdapter(Integer mode, List<AuctionComment> moviesList) {
        this.mode = mode;
        this.moviesList = moviesList;
    }
    @Override
    public commentAuctionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
            mCont = parent.getContext();
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bids_comments_row, parent, false);
        return new commentAuctionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final commentAuctionAdapter.MyViewHolder holder, int position) {

        final AuctionComment comment = moviesList.get(position);

        mAuth = FirebaseAuth.getInstance();
        userDB = mainDB.getReference("users").child(comment.getUsername());

        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Utilities.loadImage(mCont, dataSnapshot.child("photourl").getValue().toString(), holder.img);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
                String timestamp = dateFormat.format(new Date());

                holder.username.setText(dataSnapshot.child("username").getValue().toString());
                holder.timestamp.setText(comment.getTimestamp());
                holder.content.setText(comment.getComment());
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

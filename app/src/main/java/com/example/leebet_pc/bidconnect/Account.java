package com.example.leebet_pc.bidconnect;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account extends AppCompatActivity {
    
    private bidsAdapter bAdapter;
    private List<Bid> biddingItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;

    private TextView fullName;
    private TextView username;
    private TextView address;
    private TextView joindate;
    private CircleImageView userpic;
    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference userDB;
    private User theuser;
    private RatingBar ratingbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        theuser = new User();

        fullName = findViewById(R.id.fullName);
        username = findViewById(R.id.userName);
        address = findViewById(R.id.account_address);
        joindate = findViewById(R.id.account_joinDate);
        userpic = findViewById(R.id.account_userphoto);
        ratingbar = findViewById(R.id.account_userRating);

        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();

        userDB = mainDB.getReference("users").child(fbCurrUser.getUid());
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                theuser.setFullname(dataSnapshot.child("fullname").getValue().toString());
                theuser.setAddress(dataSnapshot.child("address").getValue().toString());
                theuser.setUsername(dataSnapshot.child("username").getValue().toString());
                theuser.setJoindate(dataSnapshot.child("joindate").getValue().toString());
                theuser.setRating(Float.parseFloat(dataSnapshot.child("rating").getValue().toString()));

                fullName.setText(theuser.getFullname());
                username.setText(theuser.getUsername());
                if(theuser.getAddress().equals("")){
                    address.setText("No address set");
                }
                else{
                    address.setText(theuser.getAddress());
                }

                joindate.setText(theuser.getJoindate());
                Picasso.get().load(fbCurrUser.getPhotoUrl()).into(userpic);
                ratingbar.setRating(theuser.getRating());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        recyclerView = (RecyclerView) findViewById(R.id.bids_recycler);

        bAdapter = new bidsAdapter(2,biddingItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);
        prepareBiddingItems();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void prepareBiddingItems() {
        //public Bid(String username, String title, String views, String timer, String timestamp, String currbid, String buyoutprice)
        Bid movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        bAdapter.notifyDataSetChanged();
    }
}

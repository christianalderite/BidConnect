package com.example.leebet_pc.bidconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
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
    private ImageButton logoutbtn;

    FirebaseAuth.AuthStateListener mAuthListener;
    
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
        logoutbtn = findViewById(R.id.account_logoutbtn);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent i = new Intent(Account.this,Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
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

                Utilities.loadImage(Account.this, fbCurrUser.getPhotoUrl().toString(), userpic);
                ratingbar.setRating(theuser.getRating());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                mAuth.signOut();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Account.this);
                builder.setMessage("Proceed to log out?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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

        Bid movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP","");
        biddingItems.add(movie);


        //RETRIEVE BIDDING ITEMS HERE

        bAdapter.notifyDataSetChanged();
    }

//    public void prepareAuctions(){
//        biddingItems.clear();
//        DatabaseReference dbAuctions = mainDB.getReference("auctions");
//        dbAuctions.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot object: dataSnapshot.getChildren()){
//                    Auction theauc = object.getValue(Auction.class);
//                    if(theauc.getStatus() == 1){
//
//                        latestAucs2.add(theauc);
//                    }
//                }
//                bAdapter.reverseItems();
//                bAdapter.notifyDataSetChanged();
//                dbAuctions.keepSynced(true);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}

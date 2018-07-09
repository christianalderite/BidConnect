package com.example.leebet_pc.bidconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemPageActivity extends AppCompatActivity {

    private TextView sellername, currbid, buyoutprice, timer, description,category, txtHighestBid;
    private ImageView userpic,itempic;
    private RatingBar userrating;

    private Button makeBid;
    private Button placeBid;
    private RelativeLayout dialogBid;
    private EditText inputBid;

    private String highestBid;

    private String receiveID;
    private Auction receiveAuction;
    private User sellerUser;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions = mainDB.getReference("auctions");
    private DatabaseReference dbAuctionBids = mainDB.getReference("auctionBids");
    private DatabaseReference dbUsers = mainDB.getReference("users");

    private Toolbar toolbar;

    private CountDownTimer countDownTimer;
    private long timeLeftinMS = 600000;//10mintes
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);

        sellername = findViewById(R.id.seller_name);
        currbid = findViewById(R.id.itempage_bid_currbid);
        buyoutprice = findViewById(R.id.itempage_bid_buyoutprice);
        timer = findViewById(R.id.itempage_bid_timer);
        description = findViewById(R.id.info_desc);
        userpic = findViewById(R.id.circle_crop);
        userrating = findViewById(R.id.itempage_ratingBar);
        category = findViewById(R.id.itempage_cate);
        itempic = findViewById(R.id.itempage_mainitempic);

        Intent i = getIntent();
        receiveID = i.getStringExtra("auctionKey");
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        DatabaseReference dbItem = dbAuctions.child(receiveID);

        dbItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiveAuction = dataSnapshot.getValue(Auction.class);
                toolbar.setTitle(receiveAuction.getTitle());
                currbid.setText("₱"+String.valueOf(receiveAuction.getMinprice()));
                buyoutprice.setText("₱"+Double.toString(receiveAuction.getBuyoutprice()));
                description.setText(receiveAuction.getDesc());
                category.setText(receiveAuction.getCategory());

                Utilities.loadImage(ItemPageActivity.this, receiveAuction.getImg_url(),itempic);
                //Picasso.get().load(receiveAuction.getImg_url()).into(itempic);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
                String timestamp = dateFormat.format(new Date());

                try {
                    Date date1 = dateFormat.parse(receiveAuction.getTimer());
                    Date date2 = dateFormat.parse(timestamp);
                    timeLeftinMS = (date1.getTime()) - date2.getTime();

                    startTimer();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dbUsers.child(receiveAuction.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        sellerUser = dataSnapshot.getValue(User.class);
                        Utilities.loadImage(ItemPageActivity.this, sellerUser.getPhotourl().toString(),userpic);
                        sellername.setText(sellerUser.getUsername());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toChat = new Intent(ItemPageActivity.this, ChatActivity.class);
                toChat.putExtra("userName", sellerUser.getUsername());
                toChat.putExtra("displayName", sellerUser.getFullname());
                toChat.putExtra("userID", sellerUser.getUid());
                toChat.putExtra("userAddress",sellerUser.getAddress());
                toChat.putExtra("photoUrl",sellerUser.getPhotourl());
                startActivity(toChat);
            }
        });

        makeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBid.setVisibility(View.VISIBLE);
                dialogBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBid.setVisibility(View.GONE);
                    }
                });

                placeBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }

    public void updateHighestBid(){
        Query highestBid = dbAuctionBids.orderByChild("bidAmount").limitToFirst(1);
        highestBid.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ActualBid newBid = dataSnapshot.getValue(ActualBid.class);
                currbid.setText(Double.toString(newBid.getBidAmount()));
                txtHighestBid.setText(Double.toString(newBid.getBidAmount()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftinMS,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMS = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                stopTimer();
            }
        }.start();

        timerRunning = true;
    }

    private void stopTimer(){
        countDownTimer.cancel();
        timerRunning = false;
    }

    private void updateTimer(){
        int seconds = (int) (timeLeftinMS / 1000) % 60;
        int minutes = (int) ((timeLeftinMS / (1000 * 60)) % 60);
        int hours = (int) ((timeLeftinMS / (1000 * 60 * 60)) % 24);

        timer.setText( String.format("%02d:%02d:%02d", hours, minutes, seconds) );
    }
}

package com.example.leebet_pc.bidconnect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private TextView sellername, currbid, buyoutprice, timer, description,category, textHighestBid;
    private ImageView userpic,itempic;
    private RatingBar userrating;
    private Button btnMakeBid;
    private Button btnPlaceBid;
    private EditText editBidAmount;

    private RelativeLayout dialogMakeBid;

    private String receiveID;
    private Auction receiveAuction;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private User sellerUser;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions = mainDB.getReference("auctions");
    private DatabaseReference dbUsers = mainDB.getReference("users");
    private DatabaseReference dbAuctionBids = mainDB.getReference("auctionBids");

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
        btnMakeBid = findViewById(R.id.itempage_btn_makebid);

        btnPlaceBid = findViewById(R.id.btnPlaceBid);
        editBidAmount = findViewById(R.id.inputBid);
        dialogMakeBid = findViewById(R.id.makeBidDialog);
        textHighestBid = findViewById(R.id.highestBid);

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
                Utilities.loadImage(ItemPageActivity.this, receiveAuction.getImg_url().toString(),itempic);
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
                        //Picasso.get().load(sellerUser.getPhotourl()).into(userpic);
                        sellername.setText(dataSnapshot.child("username").getValue().toString());
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


        btnMakeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHighestBid();
                dialogMakeBid.setVisibility(View.VISIBLE);
                dialogMakeBid.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialogMakeBid.setVisibility(View.GONE);
                    }
                });
                btnPlaceBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(editBidAmount.getText().toString())){
                            placeBid(editBidAmount.getText().toString());
                            Utilities.makeToast(ItemPageActivity.this,"You placed a bid worth "+editBidAmount.getText().toString());
                            dialogMakeBid.setVisibility(View.GONE);
                        }else{
                            Utilities.makeToast(ItemPageActivity.this,"Please provide bid amount.");
                        }

                    }
                });
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toChat = new Intent(ItemPageActivity.this, ChatActivity.class);
                toChat.putExtra("photoUrl",sellerUser.getPhotourl());
                toChat.putExtra("userName",sellerUser.getUsername());
                toChat.putExtra("displayName",sellerUser.getFullname());
                toChat.putExtra("userID",sellerUser.getUid());
                startActivity(toChat);
            }
        });
    }

    public void placeBid(String bidAmount){
        final String bertumen = dbAuctionBids.push().getKey();
        ActualBid newBid = new ActualBid(bertumen, receiveID,firebaseUser.getUid(),Float.valueOf(bidAmount));
        dbAuctionBids.child(bertumen).setValue(newBid);
    }


    public void updateHighestBid(){
        final Query highestBid = dbAuctionBids.child(receiveID).orderByChild("bidAmount").limitToFirst(1);
        highestBid.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    final String temp = dataSnapshot.child("bidAmount").getValue().toString();
                    currbid.setText(temp);
                    textHighestBid.setText(temp);
                }
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
        });
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

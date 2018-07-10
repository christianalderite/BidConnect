package com.example.leebet_pc.bidconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

public class ItemPageActivity extends AppCompatActivity {

    private LinearLayout thelinear;
    private TextView sellername, currbid, buyoutprice, timer, description,category, txtHighestBid;
    private ImageView userpic,itempic;
    private RatingBar userrating;

    private Button makeBid;
    private Button placeBid;
    private Button buyoutBtn;
    private RelativeLayout dialogBid;
    private EditText inputBid;

    private EditText commentContent;
    private ImageButton commentSend;

    private List<AuctionComment> commentList = new ArrayList<>();

   private String receiveID;
    private Auction receiveAuction;
    private User sellerUser;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions = mainDB.getReference("auctions");
    private DatabaseReference dbAuctionBids = mainDB.getReference("auctionBids");
    private DatabaseReference dbUsers = mainDB.getReference("users");
    private DatabaseReference dbComments = mainDB.getReference("auctionComments");

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private Toolbar toolbar;

    private CountDownTimer countDownTimer;
    private long timeLeftinMS = 600000;//10mintes
    private boolean timerRunning;

    private FirebaseAuth mAuth;

    private RecyclerView recyclerComment;
    private commentAuctionAdapter CommentAuctionAdapter;

    FirebaseUser fbCurrUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);


        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();

        thelinear = findViewById(R.id.itempage_LINEARLINEAR);
        commentContent = findViewById(R.id.itempage_commentcontent);
        commentSend = findViewById(R.id.itempage_commentsend);

        sellername = findViewById(R.id.seller_name);
        currbid = findViewById(R.id.itempage_bid_currbid);
        buyoutprice = findViewById(R.id.itempage_bid_buyoutprice);
        timer = findViewById(R.id.itempage_bid_timer);
        description = findViewById(R.id.info_desc);
        userpic = findViewById(R.id.circle_crop);
        userrating = findViewById(R.id.itempage_ratingBar);
        category = findViewById(R.id.itempage_cate);
        itempic = findViewById(R.id.itempage_mainitempic);
        buyoutBtn = findViewById(R.id.itempage_btn_buyout);

        inputBid = findViewById(R.id.inputBid);
        dialogBid = findViewById(R.id.makeBidDialog);
        makeBid = findViewById(R.id.itempage_btn_makebid);
        placeBid = findViewById(R.id.btnPlaceBid);
        txtHighestBid = findViewById(R.id.highestBid);

        Intent i = getIntent();
        receiveID = i.getStringExtra("auctionKey");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerComment = findViewById(R.id.recyclerComment);

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

                dbAuctions.child(receiveAuction.getAuctionID() + "/views").setValue(receiveAuction.getViews() + 1);

                Utilities.loadImage(ItemPageActivity.this, receiveAuction.getImg_url(),itempic);
                //Picasso.get().load(receiveAuction.getImg_url()).into(itempic);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
                String timestamp = dateFormat.format(new Date());

                try {
                    Date date1 = dateFormat.parse(receiveAuction.getTimer());
                    Date date2 = dateFormat.parse(timestamp);
                    timeLeftinMS = (date1.getTime()) - date2.getTime();

                    if(timeLeftinMS <=0){
                        timer.setText("DONE");
                    }
                    else{
                        startTimer();
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                commentContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            makeBid.setVisibility(View.INVISIBLE);
                            buyoutBtn.setVisibility(View.INVISIBLE);
                        }
                        else{

                            makeBid.setVisibility(View.VISIBLE);
                            buyoutBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
                commentSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(commentContent.getText().toString() != ""){

                            final String nigel_tan = dbComments.push().getKey();

                            dbComments.child(nigel_tan).setValue( new AuctionComment(receiveAuction.getAuctionID(),nigel_tan,fbCurrUser.getUid(),commentContent.getText().toString()))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(getApplicationContext(),"Comment Posted!", Toast.LENGTH_SHORT).show();
                                            commentContent.setText("");
                                        }
                                    });

                        }
                    }
                });

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
                        if(!TextUtils.isEmpty(inputBid.getText().toString())){
                            insertBid(inputBid.getText().toString());
                            Utilities.makeToast(ItemPageActivity.this,"You placed a bid worth "+inputBid.getText().toString());
                            dialogBid.setVisibility(View.GONE);
                        }else{
                            Utilities.makeToast(ItemPageActivity.this, "Please provide bid amount.");
                        }
                    }
                });
            }
        });

        updateHighestBid();
        dbComments.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot object: dataSnapshot.getChildren()){
                    AuctionComment zucc = object.getValue(AuctionComment.class);
                    String a = receiveAuction.getAuctionID();
                    String b =  zucc.getAuctionID();
                    if(a.equals(b)){
                        Log.d("FUCK",a+" --- "+b);


                        commentList.add(zucc);
                    }

                }
                Collections.reverse(commentList);
                CommentAuctionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        CommentAuctionAdapter = new commentAuctionAdapter(1,commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerComment.setLayoutManager(layoutManager);
        recyclerComment.setItemAnimator(new DefaultItemAnimator());
        recyclerComment.setAdapter(CommentAuctionAdapter);
    }

    public void insertBid(String bidAmount){
        String newBidKey = dbAuctionBids.push().getKey();
        ActualBid newBid = new ActualBid(newBidKey, receiveID, firebaseUser.getUid(), Double.valueOf(bidAmount));
        dbAuctionBids.child(newBidKey).setValue(newBid);
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
        //int hours = (int) ((timeLeftinMS / (1000 * 60 * 60)) % 24);
        int hours = (int) ((timeLeftinMS / (1000 * 60 * 60)));

        timer.setText( String.format("%02d:%02d:%02d", hours, minutes, seconds) );
    }
}

package com.example.leebet_pc.bidconnect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;


public class ItemPageActivity extends AppCompatActivity {
    private String oldestPostId;

    private LinearLayout thelinear;
    private TextView sellername, currbid, buyoutprice, timer, description,category, txtHighestBid;
    private ImageView userpic,itempic;
    private RatingBar userrating;
    private LinearLayout growingpains;
    private ImageView bgdimmer;
    private CardView thecard;

    private Button makeBid, placeBid, buyoutBtn;
    private RelativeLayout dialogBid;
    private EditText inputBid;

    private KeyboardEditText commentContent;
    private ImageButton commentSend;
    private KeyboardEditText thegod;

    private List<AuctionComment> commentList = new ArrayList<>();

    private String receiveID;
    private Auction receiveAuction;
    private User sellerUser;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions = mainDB.getReference("auctions");
    private DatabaseReference dbAuctionBids = mainDB.getReference("auctionBids");
    private DatabaseReference dbUsers = mainDB.getReference("users");
    private DatabaseReference dbComments = mainDB.getReference("auctionComments");
    private DatabaseReference dbSingleItem;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private Toolbar toolbar;

    private CountDownTimer countDownTimer;
    private long timeLeftinMS = 600000;//10minutes
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

        bgdimmer = findViewById(R.id.bgdimmer);
        thecard = findViewById(R.id.isangbandila);

        thelinear = findViewById(R.id.seventeen_ac);
        growingpains = findViewById(R.id.growingpains_ac);
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
        dbSingleItem = FirebaseDatabase.getInstance().getReference("auctionBids/" + receiveID);


        DatabaseReference dbItem = dbAuctions.child(receiveID);
        dbItem.keepSynced(true);

        dbItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                receiveAuction = dataSnapshot.getValue(Auction.class);
                toolbar.setTitle(receiveAuction.getTitle());
                //currbid.setText("₱"+String.valueOf(receiveAuction.getMinprice()));
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
                            //makeBid.setVisibility(View.INVISIBLE);
                            //buyoutBtn.setVisibility(View.INVISIBLE);
                            makeBid.animate()
                                    .setStartDelay(0)
                                    .scaleY(0)
                                    .scaleX(0)
                                    .setDuration(0).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    makeBid.setVisibility(View.GONE);
                                }
                            });

                            buyoutBtn.animate()
                                    .setStartDelay(0)
                                    .scaleY(0)
                                    .scaleX(0)
                                    .setDuration(0).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    buyoutBtn.setVisibility(View.GONE);
                                }
                            });
                        }
                        else{
                            makeBid.setVisibility(View.VISIBLE);
                            buyoutBtn.setVisibility(View.VISIBLE);

                            makeBid.animate()
                                    .setStartDelay(300)
                                    .scaleY(1)
                                    .scaleX(1)
                                    .setDuration(300).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    makeBid.setVisibility(View.VISIBLE);
                                }
                            });

                            buyoutBtn.animate()
                                    .setStartDelay(300)
                                    .scaleY(1)
                                    .scaleX(1)
                                    .setDuration(300).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    buyoutBtn.setVisibility(View.VISIBLE);
                                }
                            });
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
                        else{
                            Utilities.makeToast(getApplicationContext(),"Cannot post an empty comment.");
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
                toChat.putExtra("productId",receiveID);
                startActivity(toChat);
            }
        });

        makeBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgdimmer.setAlpha(0.0f);
                dialogBid.setVisibility(View.VISIBLE);
                bgdimmer.animate().alpha(1.0f).setDuration(300);
                thecard.setScaleX(0);
                thecard.setScaleY(0);
                thecard.animate()
                        .scaleY(1)
                        .scaleX(1)
                        .setInterpolator(new OvershootInterpolator())
                        .setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        dialogBid.setVisibility(View.VISIBLE);
                        //dialogBid.setBackgroundResource(R.color.common_google_signin_btn_text_light_focused);
                    }
                });

                dialogBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBid.setVisibility(View.GONE);
                        //dialogBid.setBackgroundResource(0);
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

        dbComments.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    oldestPostId = object.getKey();
                    AuctionComment zucc = object.getValue(AuctionComment.class);
                    String a = receiveAuction.getAuctionID();
                    String b =  zucc.getAuctionID();
                    if(a.equals(b)){
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

        ActualBid newBid = new ActualBid(receiveID, Double.valueOf(bidAmount),  firebaseUser.getUid(),firebaseUser.getUid());

        dbAuctionBids = mainDB.getReference("auctionBids/" + receiveAuction.getAuctionID()+"/"+newBid.getBidID());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/auctionID/", newBid.getAuctionID());
        childUpdates.put("/bidAmount/", newBid.getBidAmount());
        childUpdates.put("/bidID/", newBid.getBidID());
        childUpdates.put("/bidderID/", newBid.getBidderID());
        dbAuctionBids.updateChildren(childUpdates);


       // dbAuctionBids.child(receiveAuction.getAuctionID()).child(newBidKey).setValue(newBid);
    }

    public void updateHighestBid(){
        dbSingleItem.orderByChild("bidAmount").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot object: dataSnapshot.getChildren()){
                    ActualBid newBid = object.getValue(ActualBid.class);
                    currbid.setText(Double.toString(newBid.getBidAmount()));
                    txtHighestBid.setText(Double.toString(newBid.getBidAmount()));
                }
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

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}

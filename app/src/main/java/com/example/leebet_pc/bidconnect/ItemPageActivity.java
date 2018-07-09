package com.example.leebet_pc.bidconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemPageActivity extends AppCompatActivity {

    private TextView sellername, currbid, buyoutprice, timer, description,category;
    private ImageView userpic,itempic;
    private RatingBar userrating;

    private EditText commentContent;
    private ImageButton commentSend;

    private String receiveID;
    private Auction receiveAuction;

    private FirebaseAuth mAuth;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions = mainDB.getReference("auctions");
    private DatabaseReference dbUsers = mainDB.getReference("users");
    private DatabaseReference dbComments = mainDB.getReference("auctionComments");

    private Toolbar toolbar;

    FirebaseUser fbCurrUser;
    private CountDownTimer countDownTimer;
    private long timeLeftinMS = 600000;//10mintes
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);

        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();


        sellername = findViewById(R.id.seller_name);
        currbid = findViewById(R.id.itempage_bid_currbid);
        buyoutprice = findViewById(R.id.itempage_bid_buyoutprice);
        timer = findViewById(R.id.itempage_bid_timer);
        description = findViewById(R.id.info_desc);
        userpic = findViewById(R.id.circle_crop);
        userrating = findViewById(R.id.itempage_ratingBar);
        category = findViewById(R.id.itempage_cate);
        itempic = findViewById(R.id.itempage_mainitempic);

        commentContent = findViewById(R.id.itempage_commentcontent);
        commentSend = findViewById(R.id.itempage_commentsend);

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
                Picasso.get().load(receiveAuction.getImg_url()).into(itempic);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
                String timestamp = dateFormat.format(new Date());

                commentSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(commentContent.getText().toString() != ""){

                            final String nigel_tan = dbComments.push().getKey();

                            dbComments.child(nigel_tan).setValue( new AuctionComment(receiveAuction.getAuctionID(),nigel_tan,fbCurrUser.getUid(),commentContent.getText().toString()))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(getApplicationContext(),"Comment Posted!",Toast.LENGTH_SHORT).show();
                                        commentContent.setText("");
                                    }
                                });

                        }
                    }
                });

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

                dbUsers.child(receiveAuction.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Picasso.get().load(dataSnapshot.child("photourl").getValue().toString()).into(userpic);
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

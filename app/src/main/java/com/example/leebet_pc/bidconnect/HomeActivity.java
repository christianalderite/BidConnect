package com.example.leebet_pc.bidconnect;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private List<Bid> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private bidsAdapter bAdapter;

    private RecyclerView recyclerView2;
    private bidsAdapter bAdapter2;

    private RecyclerView recyclerView3;
    private bidsAdapter bAdapter3;
    private ImageButton btnAccount;
    private ImageButton btnProfile;
    private ImageButton btnGroups;
    private ImageButton btnAuctions;
    private Button btnSearch;
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser fbCurrUser;
    DatabaseReference dbUsers;
    FirebaseDatabase mainDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        mainDB = FirebaseDatabase.getInstance();


        //INITIALIZE THE USER IN DB
        dbUsers = mainDB.getReference("users");
        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Fetching data..."); // Setting Message
        progressDialog.setTitle("BidConnect"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show();
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //User already exists
                if( dataSnapshot.hasChild(fbCurrUser.getUid()) ) {
                    dbUsers.removeEventListener(this);
                    updateUserSession();
                }
                //If user is new
                else{
                    dbUsers.removeEventListener(this);
                    NewUserSession();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(),"Cannot download data from the internet.",Toast.LENGTH_SHORT).show();
            }
        });//INITIALIZE THE USER IN DB

        btnAccount = findViewById(R.id.imgBtn_home_me);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAccount = new Intent(HomeActivity.this, Account.class);
                startActivity(toAccount);
            }
        });

        btnAuctions = findViewById(R.id.imgBtn_home_auctions);
        btnAuctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAuctions = new Intent(HomeActivity.this, AuctionActivity.class);
                startActivity(toAuctions);
            }
        });

        btnSearch = findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSearch = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(toSearch);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.bids_recycler);

        bAdapter = new bidsAdapter(1,movieList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        recyclerView2 = (RecyclerView) findViewById(R.id.bids_recycler_3rd);

        bAdapter2 = new bidsAdapter(1,movieList);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(bAdapter2);

        recyclerView3 = (RecyclerView) findViewById(R.id.bids_recycler_4th);

        bAdapter3 = new bidsAdapter(1,movieList);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(bAdapter3);

        prepareMovieData();
    }

    private void prepareMovieData() {
        //public Bid(String username, String title, String views, String timer, String timestamp, String currbid, String buyoutprice)
        Bid movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");
        movieList.add(movie);

        bAdapter.notifyDataSetChanged();
    }

    public void NewUserSession(){
        progressDialog.setMessage("Registering new user..."); // Setting Message
        String email = fbCurrUser.getEmail();
        int index = email.indexOf('@');
        email = email.substring(0,index);
        email = email.replace(".","");
        email = email.replace("_","");

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String timestamp = dateFormat.format(new Date());

        dbUsers = mainDB.getReference("users").child(fbCurrUser.getUid());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/token_id/", FirebaseInstanceId.getInstance().getToken());
        childUpdates.put("/fullname/", fbCurrUser.getDisplayName());
        childUpdates.put("/username/", email);
        childUpdates.put("/address/", "");
        childUpdates.put("/rating/", 0.0);
        childUpdates.put("/joindate/", timestamp);
        childUpdates.put("/photourl/", fbCurrUser.getPhotoUrl().toString());
        childUpdates.put("/uid/", fbCurrUser.getUid().toString());
        dbUsers.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.cancel();
            }
        });
    }

    public void updateUserSession(){
        dbUsers = mainDB.getReference("users").child(fbCurrUser.getUid());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/token_id/", FirebaseInstanceId.getInstance().getToken());
        childUpdates.put("/photourl/", fbCurrUser.getPhotoUrl().toString());
        dbUsers.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.cancel();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        //mAuth.addAuthStateListener(mAuthListener);
        fbCurrUser = mAuth.getCurrentUser();
    }
}

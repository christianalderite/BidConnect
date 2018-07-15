package com.example.leebet_pc.bidconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leebet_pc.bidconnect.AuctionManagementFragments.AuctionActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private List<Bid> movieList = new ArrayList<>();

    private List<Auction> latestSold = new ArrayList<>();
    private List<Auction> latestAucs = new ArrayList<>();
    private List<Auction> latestAucs2 = new ArrayList<>();
    private RecyclerView recyclerView;
    private auctionsAdapter bAdapter;

    private RecyclerView recyclerView2;
    private auctionsAdapter bAdapter2;

    private RecyclerView recyclerView3;
    private auctionsAdapter bAdapter3;
    private ImageButton btnAccount;
    private ImageButton btnProfile;
    private ImageButton btnGroups;
    private ImageButton btnAuctions;
    private Button btnSearch;
    private ImageButton messages;

    private Button btnSeeAllCategories;
    private ProgressDialog progressDialog;

    private CarouselView carouselView;
    int[] sampleImages = {R.drawable.carousel1, R.drawable.broeli, R.drawable.broelih,R.drawable.hart};

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser fbCurrUser;
    DatabaseReference dbUsers;
    DatabaseReference dbAuctions;
    FirebaseDatabase mainDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainDB = FirebaseDatabase.getInstance();
        dbAuctions = mainDB.getReference("auctions");
        dbAuctions.keepSynced(true);

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        messages = findViewById(R.id.imgBtnMessages);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMessenger = new Intent(HomeActivity.this, MessagesActivity.class);
                startActivity(toMessenger);
            }
        });


        carouselView.setImageListener(imageListener);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        mainDB = FirebaseDatabase.getInstance();


        //INITIALIZE THE USER IN DB
        dbUsers = mainDB.getReference("users");

        Utilities.showLoadingDialog(this);
        Utilities.setDialogMessage("Fetching data...");
//        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
//        progressDialog.setMessage("Fetching data..."); // Setting Message
//        progressDialog.setTitle("BidConnect"); // Setting Title
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//        progressDialog.setCancelable(false);
//        progressDialog.show();
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
                Utilities.dismissDialog();
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

        btnSeeAllCategories = findViewById(R.id.btn_see_all_categories);
        btnSeeAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSeeCategory = new Intent(HomeActivity.this, CategoriesActivity.class);
                toSeeCategory.putExtra("search_type","search_all_cat");
                startActivity(toSeeCategory);
            }
        });

        //LATEST BIDS LIST//
        recyclerView = (RecyclerView) findViewById(R.id.bids_recycler);

        bAdapter = new auctionsAdapter(1,latestAucs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        //POPULAR BIDS LIST//

        recyclerView2 = (RecyclerView) findViewById(R.id.bids_recycler_3rd);

        bAdapter2 = new auctionsAdapter(1,latestAucs2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(bAdapter2);
        //COMPLETED BIDS LIST//

        recyclerView3 = (RecyclerView) findViewById(R.id.bids_recycler_4th);

        bAdapter3 = new auctionsAdapter(1,latestSold);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(bAdapter3);

        prepareLatestList();
        preparePopularList();
        prepareLatestSoldList();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


    private void prepareLatestList(){
        latestAucs.clear();
        dbAuctions.orderByChild("timestamp").limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    Auction theauc = object.getValue(Auction.class);
                    if (theauc.getStatus() == 1){
                        latestAucs.add(theauc);
                    }
                }
                bAdapter.reverseItems();
                bAdapter.notifyDataSetChanged();
                dbAuctions.keepSynced(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void prepareLatestSoldList(){
        dbAuctions.orderByChild("timestamp").limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    Auction theauc = object.getValue(Auction.class);

                    if (theauc.getStatus() == 2 || theauc.getStatus() == 4){
                        latestSold.add(theauc);
                    }
                }
                bAdapter3.notifyDataSetChanged();

                dbAuctions.keepSynced(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void preparePopularList(){
        latestAucs.clear();
        dbAuctions.orderByChild("views").limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    Auction theauc = object.getValue(Auction.class);
                    if(theauc.getStatus() == 1){

                        latestAucs2.add(theauc);
                    }
                }
                bAdapter2.reverseItems();
                bAdapter2.notifyDataSetChanged();
                dbAuctions.keepSynced(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void NewUserSession(){
        Utilities.setDialogMessage("Registering new user...");
        // progressDialog.setMessage("Registering new user..."); // Setting Message
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
                Utilities.dismissDialog();
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
                Utilities.dismissDialog();
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

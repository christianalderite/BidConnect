package com.example.leebet_pc.bidconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                Intent toAccount = new Intent(HomeActivity.this, AuctionActivity.class);
                startActivity(toAccount);
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

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
        Bid movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP");
        movieList.add(movie);

        bAdapter.notifyDataSetChanged();
    }
}

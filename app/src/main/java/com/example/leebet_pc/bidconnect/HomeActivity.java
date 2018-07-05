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
    private ImageButton btnAccount;

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

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        recyclerView = (RecyclerView) findViewById(R.id.bids_recycler);

        bAdapter = new bidsAdapter(movieList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);
        prepareMovieData();
    }

    private void prepareMovieData() {
        //public Bid(String username, String title, String views, String timer, String timestamp, String currbid, String buyoutprice)
        Bid movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","Current bid: 350.0 PHP","Current bid: 350.0 PHP");
        movieList.add(movie);

        bAdapter.notifyDataSetChanged();
    }
}

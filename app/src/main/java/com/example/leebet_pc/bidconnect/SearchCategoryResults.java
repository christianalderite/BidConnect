package com.example.leebet_pc.bidconnect;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryResults extends AppCompatActivity {
    private List<Bid> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private bidsAdapter bAdapter;


    private TextView search_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String search_res = getIntent().getStringExtra("Category");

        search_txt = (TextView) findViewById(R.id.search_txt);
        recyclerView = (RecyclerView) findViewById(R.id.results_recycler);

        if(search_res != null){
            search_txt.setText(search_res);
        }
        else{
            search_txt.setText("N/A");
        }
        bAdapter = new bidsAdapter(1,movieList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);
        prepareMovieData();
    }

    private void prepareMovieData() {
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

    }


}

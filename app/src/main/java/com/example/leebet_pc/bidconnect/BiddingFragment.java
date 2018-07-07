package com.example.leebet_pc.bidconnect;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BiddingFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Bidding Fragement";

    private List<Bid> movieList = new ArrayList<>();
    private Button btnTEST;
    private RecyclerView recyclerView;
    private biddingAuctionAdapter bAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bidding_fragment,container,false);
        //btnTEST = (Button) view.findViewById(R.id.button_bidding);


        recyclerView = (RecyclerView) view.findViewById(R.id.bidding_recycler);
        bAdapter = new biddingAuctionAdapter(1,movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);
        prepareData();
        return view;
    }
    public void prepareData(){

        Bid movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","₱350.00","Current bid: 350.0 PHP","");

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","50.00","199.0","","Outbid","150.00");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","130.00","750.0","", "Winning", "130.00");
        movieList.add(movie);

        movie = new Bid("elisoriano","GALASYA 3:3","2.6k","1:33:00","11 hours ago","360.00","455.0","", "Outbid", "500.00");
        movieList.add(movie);


        bAdapter.notifyDataSetChanged();
    }
}

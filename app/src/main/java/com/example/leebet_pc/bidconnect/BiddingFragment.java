package com.example.leebet_pc.bidconnect;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BiddingFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Bidding Fragement";

    private List<String> aucsList = new ArrayList<>();
    private List<ActualBid> aucBidsList = new ArrayList<>();
    private Button btnTEST;
    private RecyclerView recyclerView;
    private biddingAuctionAdapter bAdapter;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctionBids = mainDB.getReference("auctionBids");
    private User sellerUser;
    private DatabaseReference dbUsers = mainDB.getReference("users");

    FirebaseUser fbCurrUser;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bidding_fragment,container,false);
        //btnTEST = (Button) view.findViewById(R.id.button_bidding);

        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();

        recyclerView = (RecyclerView) view.findViewById(R.id.bidding_recycler);
        bAdapter = new biddingAuctionAdapter(1,aucBidsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        dbAuctionBids.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    String zucc = object.getKey();
                    aucsList.add(zucc);
                }
                getUserBids(aucsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("mine po",aucBidsList.size()+" -- ");
        //Collections.reverse(aucBidsList);
        //bAdapter.notifyDataSetChanged();
        return view;
    }
    public void getUserBids(List<String> auctions){


        final ArrayList<ActualBid> bobo = new ArrayList<>();

        for(final String aucStr : auctions){
            DatabaseReference dbTemp = mainDB.getReference("auctionBids").child(aucStr);
            dbTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot object: dataSnapshot.getChildren()){

                        ActualBid zucc = object.getValue(ActualBid.class);
                        ActualBid ace = new ActualBid();

                        ace.setAuctionID(zucc.getAuctionID());
                        ace.setBidID(zucc.getBidID());
                        ace.setBidderID(zucc.getBidderID());
                        ace.setBidAmount(zucc.getBidAmount());

                        String a = fbCurrUser.getUid();
                        String b =  zucc.getBidderID();

                        if (a.equalsIgnoreCase(b)){

                            bobo.add(ace);
                            aucBidsList.add(zucc);
                        }
                        //aucBidsList.add(zucc);
                        Log.d("mine po",aucBidsList.size()+" -- "+aucStr+"xd:"+zucc.getBidID());
                    }

                    Collections.reverse(bobo);
                    bAdapter.notifyDataSetChanged();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        Log.d("mine:",bobo.size()+"");
    }
}

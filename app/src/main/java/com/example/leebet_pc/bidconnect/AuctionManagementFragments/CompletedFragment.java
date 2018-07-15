package com.example.leebet_pc.bidconnect.AuctionManagementFragments;

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

import com.example.leebet_pc.bidconnect.Auction;
import com.example.leebet_pc.bidconnect.R;
import com.example.leebet_pc.bidconnect.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletedFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Bidding Fragement";

    private List<Auction> aucsList = new ArrayList<>();

    private List<String> typeList = new ArrayList<>();
    private Button btnTEST;
    private RecyclerView recyclerView;
    private boolean hasBidz;
    private completedAuctionAdapter bAdapter;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctionBids = mainDB.getReference("auctions");
    private User sellerUser;
    private DatabaseReference dbUsers = mainDB.getReference("users");

    FirebaseUser fbCurrUser;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();

        recyclerView = (RecyclerView) view.findViewById(R.id.completed_recycler);
        bAdapter = new completedAuctionAdapter(1,aucsList, typeList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        dbAuctionBids.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    final Auction auc_zucc = object.getValue(Auction.class);

                    if (auc_zucc.getStatus() != 1 || auc_zucc.getStatus() !=3){

                        if(auc_zucc.getUsername() == fbCurrUser.getUid()){
                            typeList.add("sell");
                        }
                        else if(auc_zucc.getStatus() == 3){
                            typeList.add("buyout");
                        }
                        else{
                            typeList.add("bid");
                        }
                        aucsList.add(auc_zucc);
                    }
                    bAdapter.notifyDataSetChanged();
                }
                bAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}

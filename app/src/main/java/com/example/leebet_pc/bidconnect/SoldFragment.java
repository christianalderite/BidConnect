package com.example.leebet_pc.bidconnect;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class SoldFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Selling Fragement";

    private List<Auction> sellingList = new ArrayList<>();
    private Button btnTEST;
    private RecyclerView recyclerView;
    private sellingAuctionAdapter bAdapter;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions = mainDB.getReference("auctions");

    private User sellerUser;
    private DatabaseReference dbUsers = mainDB.getReference("users");

    FirebaseUser fbCurrUser;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selling_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();

        recyclerView = (RecyclerView) view.findViewById(R.id.selling_recycler);

        bAdapter = new sellingAuctionAdapter(1,sellingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        dbAuctions.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    Auction zucc = object.getValue(Auction.class);
                    String a = fbCurrUser.getUid();
                    String b =  zucc.getUsername();
                    if(a.equals(b)){
                        if(zucc.getStatus() == 1){
                            sellingList.add(zucc);
                        }
                    }
                }
                Collections.reverse(sellingList);
                bAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}

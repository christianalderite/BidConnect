package com.example.leebet_pc.bidconnect.AuctionManagementFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import java.util.List;

public class CompletedActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();

        recyclerView = (RecyclerView) findViewById(R.id.completed_recycler);
        bAdapter = new completedAuctionAdapter(1,aucsList, typeList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bAdapter);

        dbAuctionBids.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot object: dataSnapshot.getChildren()){
                    final Auction auc_zucc = object.getValue(Auction.class);
                    Log.e("COMP_FRAG:",auc_zucc.getTitle()+" "+auc_zucc.getStatus());
                    if (auc_zucc.getStatus() != 1 || auc_zucc.getStatus() !=3){

                        if(auc_zucc.getUsername().equalsIgnoreCase(fbCurrUser.getUid())){
                            typeList.add("sell");

                            aucsList.add(auc_zucc);
                        }
                        else if(auc_zucc.getStatus() == 2){
                            typeList.add("buyout");

                            aucsList.add(auc_zucc);
                        }
                        else{
                            typeList.add("bid");

                            aucsList.add(auc_zucc);
                        }
                    }
                }
                bAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}

package com.example.leebet_pc.bidconnect;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRoot = database.getReference();
    DatabaseReference dbReceived;
    DatabaseReference dbUser;
    DatabaseReference dbAuction;

    private RecyclerView recyclerView;
    private messagesListAdapter mAdapter;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        recyclerView= findViewById(R.id.messagesRecycler);

        mAdapter = new messagesListAdapter(messageList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        dbReceived = dbRoot.child("conversations").child(firebaseUser.getUid());
        dbReceived.keepSynced(true);

        dbUser = dbRoot.child("users");
        dbAuction = dbRoot.child("auctions");
        //fetchMessages();

        deepQuery();
    }

    public void fetchMessages(){
        dbReceived.limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot message: dataSnapshot.getChildren()){
                    Message newMessage = message.getValue(Message.class);
                    messageList.add(newMessage);
                }
                mAdapter.notifyDataSetChanged();
                Utilities.makeToast(MessagesActivity.this, "Finished querying...");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fetchProductInformation(final String userId, final String productId, final String message){

    }

    public void buildUserMessageRow(final String userId, final String productId, final String message,final String timestamp){
        dbUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User newUser = dataSnapshot.getValue(User.class);

                dbAuction.child(productId);
                dbAuction.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isYou = false;
                        if(newUser.getUid()==firebaseUser.getUid()){
                            isYou=true;
                        }
                        String auctionName = dataSnapshot.child("title").getValue(String.class);
                        Message newMessage = new Message(message, productId,auctionName, timestamp, isYou, newUser);
                        messageList.add(newMessage);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Utilities.makeToast(MessagesActivity.this,"Finished querying!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deepQuery(){
        dbReceived.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if(dataSnapshot.exists()) {
                    // GETTING ALL USERS
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        final String userId = user.getKey();
                        dbReceived.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //GETTING ALL USER'S PRODUCTS
                                for (DataSnapshot object : dataSnapshot.getChildren()) {
                                    final String productId = object.getKey();
                                    dbReceived.child(userId).child(productId).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //GETTING LATEST MESSAGE ABOUT A PRODUCT
                                            for (DataSnapshot message : dataSnapshot.getChildren()) {
                                                String messageText = message.child("message").getValue(String.class);
                                                String timestamp = message.child("timestamp").getValue(String.class);
                                                buildUserMessageRow(userId, productId, messageText, timestamp);
                                                //Log.e("TANGNINANG MESSAGE TO: ",messageText);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
               // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

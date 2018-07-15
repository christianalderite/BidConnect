package com.example.leebet_pc.bidconnect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String sellerPhotoUrl, sellerDisplayName, sellerUserName, sellerUid, sellerAddress, productName;
    private String yourPhotoUrl, yourDisplayName, yourUid;
    private String productId;

    private TextView txtSellerName;
    private TextView txtSellerUserName;
    private TextView txtSellerAddress;
    private ImageView imgSellerImage;
    private TextView txtProductName;
    private EditText typeMessage;
    private ImageButton btnSend;
    private Button btnAuction;

    //private ArrayList<Message> messagesList = new ArrayList<>();
    private ListView messagesView;
    private messagesAdapter mAdapter ;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions = database.getReference("auctions");
    private DatabaseReference dbConversations = database.getReference("conversations");
    private DatabaseReference dbMessages;
    private DatabaseReference dbMessagesOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imgSellerImage = findViewById(R.id.account_userphoto);
        txtSellerName = findViewById(R.id.fullName);
        txtSellerUserName = findViewById(R.id.userName);
        txtSellerAddress = findViewById(R.id.account_address);
        txtProductName = findViewById(R.id.displayProductName);
        typeMessage = findViewById(R.id.editText);
        btnSend = findViewById(R.id.btnSend);
        btnAuction = findViewById(R.id.btnAuction);

        mAdapter = new messagesAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(mAdapter);

        Intent fromItemPage = getIntent();
        if (fromItemPage.getExtras() != null) {
            Bundle bundle = fromItemPage.getExtras();

            sellerPhotoUrl = bundle.getString("photoUrl");
            sellerDisplayName = bundle.getString("displayName");
            sellerUserName = bundle.getString("userName");
            sellerUid = bundle.getString("userID");
            sellerAddress = bundle.getString("userAddress");
            productName = bundle.getString("productName");


            productId = bundle.getString("productId");

            Utilities.loadImage(this, sellerPhotoUrl, imgSellerImage);
            txtSellerUserName.setText(sellerUserName);
            txtSellerName.setText(sellerDisplayName);
            txtSellerAddress.setText(sellerAddress);
            txtProductName.setText(productName);


            dbMessages = dbConversations.child(firebaseUser.getUid()).child(sellerUid).child(productId);
            dbMessagesOther = dbConversations.child(sellerUid).child(firebaseUser.getUid()).child(productId);

//            dbMessages = dbConversations.child(firebaseUser.getUid());
//            dbMessagesOther = dbConversations.child(sellerUid);

            displayMessages();

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage();
                }
            });
            btnAuction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(ChatActivity.this, ItemPageActivity.class);
                    myIntent.putExtra("auctionKey", productId); //Optional parameters
                    startActivity(myIntent);
                }
            });

        }else{
            finish();
        }
    }

    public void sendMessage(){
        String temp = typeMessage.getText().toString();
        if(!temp.isEmpty()){
            DatabaseReference newMsg = dbMessages.push();
            Map<String, String> newMessage = new HashMap<String, String>();
            newMessage.put("senderId", firebaseUser.getUid());
            newMessage.put("productId", productId);
            newMessage.put("message", temp);
            newMsg.setValue(newMessage);

            dbMessagesOther.child(newMsg.getKey()).setValue(newMessage);
            typeMessage.setText("");
        }
    }

    public void displayMessages(){
        dbMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String senderId = null;

                    if(dataSnapshot.child("message").getValue()!=null){
                        message = dataSnapshot.child("message").getValue().toString();
                    }
                    if(dataSnapshot.child("senderId").getValue()!=null){
                        senderId = dataSnapshot.child("senderId").getValue().toString();
                    }
                    if(message!=null && senderId!=null){
                        Boolean isCurrentUser = false;
                        String photoUrl = "";
                        if(senderId.equals(firebaseUser.getUid())){
                            isCurrentUser =true;
                            photoUrl = sellerPhotoUrl;
                        }

                        Message newMessage = new Message(message, photoUrl, isCurrentUser);
                        //messagesList.add(newMessage);
                        mAdapter.add(newMessage);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

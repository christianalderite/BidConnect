package com.example.leebet_pc.bidconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private String sellerPhotoUrl, sellerDisplayName, sellerUserName, sellerUid;
    private String yourPhotoUrl, yourDisplayName, yourUid;

    private TextView txtSellerName;
    private TextView txtSellerUserName;
    private ImageView imgSellerImage;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imgSellerImage = findViewById(R.id.account_userphoto);
        txtSellerName = findViewById(R.id.fullName);
        txtSellerUserName = findViewById(R.id.userName);

        Intent fromItemPage = getIntent();
        if (fromItemPage.getExtras() != null) {
            Bundle bundle = fromItemPage.getExtras();

            sellerPhotoUrl = bundle.getString("photoUrl");
            sellerDisplayName = bundle.getString("displayName");
            sellerUserName = bundle.getString("userName");
            sellerUid = bundle.getString("userID");

            Utilities.loadImage(this, sellerPhotoUrl, imgSellerImage);
            txtSellerUserName.setText(sellerUserName);
            txtSellerName.setText(sellerDisplayName);

        }else{
            finish();
        }
    }
}

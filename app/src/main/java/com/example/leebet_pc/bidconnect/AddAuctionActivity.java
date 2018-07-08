package com.example.leebet_pc.bidconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddAuctionActivity extends AppCompatActivity {
    public TextView chooseCat;
    public TextView itemCat;

    private ImageView imgchooser;
    private Uri imageUri;
    private String imageurl = "";
    private static final int PICK_IMAGE = 90;
    private Button submitbutton;
    private StorageReference mStorage;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_auction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner dropdown = findViewById(R.id.addauction_duration);
        String[] items = new String[]{"12:00", "24:00", "48:00"};

        dbAuctions = mainDB.getReference("auctions");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);

        //Andrew
        imageUri = null;
        mStorage = FirebaseStorage.getInstance().getReference().child("auctionImages");

        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Step 1 of 2\nUploading Image..."); // Setting Message
        progressDialog.setTitle("Add Professor"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

        imgchooser = findViewById(R.id.btn_upload_img);
        submitbutton = findViewById(R.id.addauction_submitbtn);

        chooseCat = this.findViewById(R.id.choose_cat);

        itemCat = this.findViewById(R.id.addauction_cat);

        chooseCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AddAuctionActivity.this, CategoriesActivity.class);
                startActivityForResult(myIntent,69);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgchooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uniksalonga = dbAuctions.push().getKey();

                if(imageUri != null){
                    progressDialog.show(); // Display Progress Dialog
                    StorageReference prof_pic = mStorage.child(uniksalonga + ".jpg");

                    prof_pic.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                imageurl = task.getResult().getUploadSessionUri().toString();
                                /*
                                dbAuctions.child(uniksalonga).setValue( .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),"Professor added!",Toast.LENGTH_SHORT).show();
                                            name.setText("");
                                            email.setText("");
                                            deptSpinner.setSelection(0);
                                            posSpinner.setSelection(0);
                                            addpic.setImageResource(R.drawable.ic_add_circle);
                                        }
                                        else{
                                            Toast.makeText(getActivity(),"There was an error.",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }

                                    }
                                });*/

                            }
                            else{Toast.makeText(getApplicationContext(),"Error uploading pic",Toast.LENGTH_SHORT).show();progressDialog.dismiss();}
                        }
                    });

                }//imageuri if end
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 69) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                itemCat.setText(data.getStringExtra("Category"));
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here (bigger example below)
            }
        }
        if(requestCode == PICK_IMAGE){
            if(data !=null){
                imageUri = data.getData();
                imgchooser.setColorFilter(null);
                imgchooser.setImageURI(imageUri);
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }
}

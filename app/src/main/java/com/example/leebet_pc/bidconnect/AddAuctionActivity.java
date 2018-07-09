package com.example.leebet_pc.bidconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAuctionActivity extends AppCompatActivity {
    public TextView chooseCat, AuctionDuration;
    public TextView itemCat,AuctionDur;

    private ImageView imgchooser;
    private Uri imageUri;
    private String imageurl = "";
    private static final int PICK_IMAGE = 90;
    private Button submitbutton;
    private StorageReference mStorage;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbAuctions;
    private FirebaseAuth mAuth;
    FirebaseUser fbCurrUser;
    UploadTask uploadTask;

    private EditText itemname, description, minimumprice, stealprice;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        fbCurrUser = mAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_auction);
        final View dialogView = View.inflate(AddAuctionActivity.this, R.layout.date_time_picker, null);

        itemname = findViewById(R.id.addauction_itemname);
        description = findViewById(R.id.addauction_itemdesc);
        minimumprice = findViewById(R.id.addauction_minprice);
        stealprice = findViewById(R.id.addauction_buyoutprice);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AuctionDuration = (TextView) this.findViewById(R.id.addauction_duration);
        AuctionDur = (TextView) this.findViewById(R.id.addauction_dur);

        String[] items = new String[]{"12:00", "24:00", "48:00"};
        AuctionDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddAuctionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        AuctionDur.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        dbAuctions = mainDB.getReference("auctions");


        //Andrew
        imageUri = null;

        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Step 1 of 2\nUploading Image..."); // Setting Message
        progressDialog.setTitle("Add An Auction"); // Setting Title
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
                mStorage = FirebaseStorage.getInstance().getReference().child("auctionImages/" + uniksalonga);

                if(imageUri != null){
                    progressDialog.show(); // Display Progress Dialog
                    StorageReference prof_pic = mStorage.child(uniksalonga + ".jpg");

                    uploadTask = mStorage.putFile(imageUri);

                    /////MAMA MO////
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return mStorage.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                imageurl = task.getResult().toString();
                                progressDialog.setMessage("Step 2 of 2\nRegistering details..."); // Setting Message
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a");
                                String timestamp = dateFormat.format(new Date());

                                dbAuctions.child(uniksalonga).setValue( new Auction(uniksalonga, fbCurrUser.getUid(),itemname.getText().toString(),0,AuctionDur.getText().toString(),timestamp,Double.parseDouble(stealprice.getText().toString()),imageurl,
                                        itemCat.getText().toString(),description.getText().toString(),Double.parseDouble(minimumprice.getText().toString()),1) ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"Auction registered!",Toast.LENGTH_SHORT).show();
                                            itemname.setText("");
                                            description.setText("");
                                            itemCat.setText("");
                                            minimumprice.setText("");
                                            stealprice.setText("");
                                            AuctionDur.setText("");
                                            imgchooser.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"There was an error. Please try again.",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(),"Error uploading picture. Please try again.",Toast.LENGTH_SHORT).show();progressDialog.dismiss(); }
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

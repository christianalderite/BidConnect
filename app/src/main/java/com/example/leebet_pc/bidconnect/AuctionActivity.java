package com.example.leebet_pc.bidconnect;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AuctionActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button btnsell;
    private static final String TAG = "AuctionActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        btnsell = (Button) findViewById(R.id.buttonsell);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        btnsell.setVisibility(View.INVISIBLE);
        btnsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAccount = new Intent(AuctionActivity.this, AddAuctionActivity.class);
                startActivity(toAccount);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        btnsell.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        btnsell.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        btnsell.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        btnsell.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new BiddingFragment(), "Bidding");
        adapter.addFragment(new SellingFragment(), "Selling");
        adapter.addFragment(new WonFragment(), "Won");
        adapter.addFragment(new SoldFragment(), "Sold");
        viewPager.setAdapter(adapter);

    }



}

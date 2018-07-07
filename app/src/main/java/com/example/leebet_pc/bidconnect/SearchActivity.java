package com.example.leebet_pc.bidconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {


    private searchResultAdapter sAdapter;
    private List<String> searchResults = new ArrayList<>();
    private ArrayList<String> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView popularSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        recyclerView = findViewById(R.id.recyclerView);
        searchView =  findViewById(R.id.searchView);
        popularSearches = findViewById(R.id.imageView2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshSearchResults(newText);
                return false;
            }
        });

        sAdapter = new searchResultAdapter(searchResults);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter);

        prepareSearchResults();
    }

    public void prepareSearchResults(){

        String[] otherList = new String[] {"bags", "mops", "coffee", "laptops", "cellphones",
                                            "soap", "skin care", "cameras", "shampoo", "herbs",
                                            "cookies", "sim card", "search", "straight", "men's wear",
                                            "samsung", "appliances", "electronics","makeup", "shoe rack"};
        arrayList.addAll(Arrays.asList(otherList));
    }

    public void refreshSearchResults(String searchText){
        searchText = searchText.toLowerCase(Locale.getDefault());
        searchResults.clear();


        if(searchText.length()>0){
            for(String wp: arrayList ){
                if(wp.toLowerCase(Locale.getDefault()).contains(searchText)){
                    searchResults.add(wp);
                }
            }
            popularSearches.setVisibility(View.GONE);
        }else{
            popularSearches.setVisibility(View.VISIBLE);
        }

        sAdapter.notifyDataSetChanged();
    }


}

package com.example.leebet_pc.bidconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {
    Button back;

    private ArrayList<String> categories = new ArrayList<>();

    private ArrayList<String> filteredList = new ArrayList<>();
    public categoriesAdapter CategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        final EditText et = (EditText) findViewById(R.id.et_search);

        back = (Button) this.findViewById(R.id.button_category_back);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // big bad betboi

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //hello
            }
        });

        RecyclerView recyclerView_contacts = (RecyclerView) findViewById(R.id.recyclerCategories);
        CategoriesAdapter = new categoriesAdapter(filteredList);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_contacts.setLayoutManager(rLayoutManager);
        recyclerView_contacts.setItemAnimator(new DefaultItemAnimator());
        recyclerView_contacts.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView_contacts.setAdapter(CategoriesAdapter);

        prepareData();
        filteredList.addAll(categories);
        CategoriesAdapter.notifyDataSetChanged();
    }
    private void prepareData() {
        categories.add("Fashion");
        categories.add("Electronices");
        categories.add("Men's Wear");
        categories.add("Womans Wear");
        categories.add("Jewelry");
        categories.add("Submarine");
        categories.add("Gadget");
    }
    private void filter(String s) {
        Log.i("BOBO","hello im filtering");
        Log.i("BOBO",s);

        ArrayList<String> copyList = new ArrayList<>();

        copyList.addAll(filteredList);
        filteredList.clear();

        if (s.isEmpty()) {
            filteredList.addAll(categories);
        } else {
            s = s.toLowerCase();
            for (int i = 0; i<copyList.size(); i++) {
                // Adapt the if for your usage
                if (copyList.get(i).toLowerCase().contains(s))
                {
                    filteredList.add(copyList.get(i));
                }
            }
        }
        CategoriesAdapter.notifyDataSetChanged();
    }
}

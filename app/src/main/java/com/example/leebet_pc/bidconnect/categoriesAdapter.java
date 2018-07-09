package com.example.leebet_pc.bidconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.MyViewHolder> {

    private List<String> categoriesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;
    private Context mCont;
    private Activity myAct;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public LinearLayout item;

        public MyViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.cat_name);
            item = (LinearLayout) view.findViewById(R.id.category_item);
        }
    }
    public categoriesAdapter(List<String> categoriesList) {

        this.categoriesList = categoriesList;
    }
    @Override
    public categoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
            myAct = (Activity)parent.getContext();
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.categories_row, parent, false);
        return new categoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(categoriesAdapter.MyViewHolder holder, int position) {

        final String category = categoriesList.get(position);
        holder.categoryName.setText(category);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String search_type = myAct.getIntent().getStringExtra("search_type");
                if (search_type == "search_all_cat") {
                    Log.i("waw", search_type);

                    switch (search_type){

                        case "search_all_cat":
                            Intent searchIntent = new Intent(myAct, SearchCategoryResults.class);
                            searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            searchIntent.putExtra("Category", category);
                            myAct.startActivity(searchIntent);
                            myAct.finish();
                            break;

                        case "wew":
                            break;

                        default:
                            break;
                    }
                }

                Intent putIntent = new Intent();
                putIntent.putExtra("Category", category);
                myAct.setResult(RESULT_OK, putIntent);
                myAct.finish();





            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }
}

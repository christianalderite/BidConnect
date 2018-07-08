package com.example.leebet_pc.bidconnect;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.MyViewHolder> {

    private List<String> categoriesList;
    private static final Integer ACTIVITY_ACCOUNT = 2;
    private static final Integer ACTIVITY_HOME = 1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public TextView categoryName;

        public MyViewHolder(View view) {
            super(view);
            category = (TextView) view.findViewById(R.id.cat_name);
            categoryName = (TextView) view.findViewById(R.id.cat_name);
        }
    }
    public categoriesAdapter(List<String> categoriesList) {

        this.categoriesList = categoriesList;
    }
    @Override
    public categoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.categories_row, parent, false);
        return new categoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(categoriesAdapter.MyViewHolder holder, int position) {

        String category = categoriesList.get(position);

        holder.categoryName.setText(category);

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }
}

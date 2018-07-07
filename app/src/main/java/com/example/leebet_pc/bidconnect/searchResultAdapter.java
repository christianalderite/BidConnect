package com.example.leebet_pc.bidconnect;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class searchResultAdapter extends RecyclerView.Adapter<searchResultAdapter.MyViewHolder> {

    private List<String> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView searchResult;

        public MyViewHolder(View view) {
            super(view);
            searchResult = (TextView) view.findViewById(R.id.searchResult);
        }
    }

    public searchResultAdapter( List<String> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String movie = moviesList.get(position);
        holder.searchResult.setText(movie);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}


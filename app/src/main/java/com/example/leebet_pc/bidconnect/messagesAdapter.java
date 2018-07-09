package com.example.leebet_pc.bidconnect;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class messagesAdapter extends RecyclerView.Adapter<messagesAdapter.MyViewHolder>{

    private List<Message> messageList;
    private Context mCont;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, message, photoUrl;
        public ImageView image;
        public CardView container;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            message = (TextView) itemView.findViewById(R.id.message);
            container = (CardView) itemView.findViewById(R.id.container);

        }




    }
    public messagesAdapter(List<Message> messageList){
        this.messageList = messageList;
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }
}

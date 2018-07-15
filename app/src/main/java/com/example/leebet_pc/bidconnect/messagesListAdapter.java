package com.example.leebet_pc.bidconnect;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class messagesListAdapter  extends RecyclerView.Adapter<messagesListAdapter.MyViewHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbMessages = database.getReference("users");

    List<Message> messageNotificationList;
    MessagesActivity messagesActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName, displayMessage, displayAuctionName;
        public ImageView displayPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);

            displayName = itemView.findViewById(R.id.displayName);
            displayMessage = itemView.findViewById(R.id.displayMessage);
            displayPhoto = itemView.findViewById(R.id.displayPhoto);
            displayAuctionName = itemView.findViewById(R.id.displayProductName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Message newMessage = messageNotificationList.get(getAdapterPosition());
                    User sellerUser = newMessage.getUser();
                    Intent toChat = new Intent(messagesActivity, ChatActivity.class);
                    toChat.putExtra("userName", sellerUser.getUsername());
                    toChat.putExtra("displayName", sellerUser.getFullname());
                    toChat.putExtra("userID", sellerUser.getUid());
                    toChat.putExtra("userAddress",sellerUser.getAddress());
                    toChat.putExtra("photoUrl",sellerUser.getPhotourl());
                    toChat.putExtra("productId", newMessage.getProductId());
                    toChat.putExtra("productName",newMessage.getProductName());
                    messagesActivity.startActivity(toChat);

                }
            });

        }
    }

    public messagesListAdapter(List<Message> messagesNotificationList, MessagesActivity messagesActivity){
        this.messageNotificationList=messagesNotificationList;
        this.messagesActivity = messagesActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messenger_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message msg = messageNotificationList.get(position);
        holder.displayName.setText(msg.getUser().getFullname());
        holder.displayAuctionName.setText(msg.getProductName());
        if(msg.isBelongsToCurrentUser()){
            holder.displayMessage.setText("You: "+msg.getmessage());
        }else{
            holder.displayMessage.setText(msg.getmessage());
        }
        Utilities.loadImage(messagesActivity, msg.getUser().getPhotourl(), holder.displayPhoto);
    }

    @Override
    public int getItemCount() {
        return messageNotificationList.size();
    }
}

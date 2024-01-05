package com.example.adp2_ex3.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adp2_ex3.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
   public TextView content;
   public TextView when;
   public  ImageView profilePic;
   public boolean outgoingMessage;

   public MessageViewHolder(@NonNull View itemView) {
      super(itemView);
      when = itemView.findViewById(R.id.message_when);
      profilePic = itemView.findViewById(R.id.message_profilePic);
      content = itemView.findViewById(R.id.message_content);
   }


}

package com.example.adp2_ex3.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adp2_ex3.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    public String chatId;
    public TextView displayName;
    public TextView when;
    public TextView lastMessage;
    public ImageView profilePic;
    public ConstraintLayout linearLayout;
    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        displayName = itemView.findViewById(R.id.contact_displayName);
        when = itemView.findViewById(R.id.contact_when);
        lastMessage = itemView.findViewById(R.id.contact_lastMessage);
        profilePic = itemView.findViewById(R.id.contact_profilePic);
        linearLayout = itemView.findViewById(R.id.single_contact_all);
    }
}

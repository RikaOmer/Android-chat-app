package com.example.adp2_ex3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adp2_ex3.Chats.Message;
import com.example.adp2_ex3.R;
import com.example.adp2_ex3.views.MessageViewHolder;

import java.util.Collections;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    List<Message> messages;
    private final LayoutInflater mInflater;
    private String mainUser;

    public MessageAdapter(Context context,String mainUser){ mInflater=LayoutInflater.from(context);this.mainUser=mainUser;}

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        if (viewType==0)
            item =mInflater.inflate(R.layout.message_row_outgoing,parent,false);
        else
            item = mInflater.inflate(R.layout.message_row_incoming,parent,false);
        return new MessageViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (messages!=null){
            final  Message current= messages.get(position);
            holder.when.setText(current.getCreated());
            holder.content.setText(current.getContent());
            holder.profilePic.setImageBitmap(current.getSender().getProfilePicBitmap());
            if (!current.getSender().getUsername().equals(mainUser)){
                Log.d("FLAG", "true");
                holder.outgoingMessage = true;
            }
            else{
                holder.outgoingMessage=false;
            }
        }

    }

    @Override
    public int getItemCount() {
        if (messages==null) return 0;
        return messages.size();
    }

    public void setMessages(List<Message> m){
        messages=m;
        notifyDataSetChanged();
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getItemViewType(int position)
    {
        Message current = messages.get(position);
        if (current.getSender().getUsername().equals(mainUser)){
            return 0;
        }
        else{
            return 1;
        }
    }
}

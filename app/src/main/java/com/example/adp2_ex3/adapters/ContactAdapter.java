package com.example.adp2_ex3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.adp2_ex3.Chats.Chat;
import com.example.adp2_ex3.R;
import com.example.adp2_ex3.SelectListener.SelectListener;
import com.example.adp2_ex3.views.ContactViewHolder;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    List<Chat> chats;

    private final LayoutInflater mInflater;
    private SelectListener listener;
    public ContactAdapter(Context context,SelectListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.listener=listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mInflater.inflate(R.layout.single_contact,parent,false);

        return new ContactViewHolder(item);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (chats!=null){
            final Chat c = chats.get(position);
            holder.displayName.setText(c.getOtherUser().getDisplayName());
            holder.when.setText(c.getLastMessage().getCreated());
            holder.lastMessage.setText(c.getLastMessage().getContent());
            holder.profilePic.setImageBitmap(c.getOtherUser().getProfilePicBitmap());
            holder.chatId=c.getChatId();
            holder.linearLayout.setOnClickListener(v -> listener.onItemClicked(chats.get(holder.getAdapterPosition())));
        }
    }


        @Override
    public int getItemCount() {
        if(chats==null) return 0;
        return chats.size();
    }

    public void setChats(List<Chat> chats) {
        List<Chat> sortedChats = new ArrayList<>(chats); // Create a new list to hold the sorted chats
        sortChats(sortedChats); // Sort the new list

        this.chats = sortedChats; // Assign the sorted list to this.chats
        notifyDataSetChanged();
    }


    public void sortChats(List<Chat> filteredList){

        Collections.sort(filteredList, (chat1, chat2) -> {
            String date1 = chat1.getLastMessage().getCreated();
            String date2 = chat2.getLastMessage().getCreated();
            if (date1.length() == 5 && date2.length() == 5) {
                // Both dates are in "hh:mm" format
                int hour1 = Integer.parseInt(date1.substring(0, 2));
                int minute1 = Integer.parseInt(date1.substring(3));
                int hour2 = Integer.parseInt(date2.substring(0, 2));
                int minute2 = Integer.parseInt(date2.substring(3));

                if (hour1 != hour2) {
                    return Integer.compare(hour2, hour1);
                } else {
                    return Integer.compare(minute2, minute1);
                }
            } else if (date1.length() == 10 && date2.length() == 10) {
                // Both dates are in "dd/MM/yyyy" format
                int year1 = Integer.parseInt(date1.substring(6));
                int year2 = Integer.parseInt(date2.substring(6));

                if (year1 != year2) {
                    return Integer.compare(year2, year1);
                } else {
                    int month1 = Integer.parseInt(date1.substring(3, 5));
                    int month2 = Integer.parseInt(date2.substring(3, 5));

                    if (month1 != month2) {
                        return Integer.compare(month2, month1);
                    } else {
                        int day1 = Integer.parseInt(date1.substring(0, 2));
                        int day2 = Integer.parseInt(date2.substring(0, 2));
                        return Integer.compare(day2, day1);
                    }
                }
            } else if (date1.length() == 5 && date2.length() == 10) {
                return -1;
            } else if (date1.length() == 10 && date2.length() == 5) {
                return 1;
            }
            else if (date1.length() == 0 && date2.length() != 0){
                return 1;
            }
            else if (date1.length() != 0 && date2.length() == 0){
                return -1;
            }
            return 0;
        });
    }
}

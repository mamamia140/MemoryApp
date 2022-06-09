package com.example.endtermproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.ViewHolder> {
    ArrayList<singleMemory> memories;
    Context context;
    int index;

    public MemoryListAdapter(ArrayList<singleMemory> memories, Context context) {
        this.memories = memories;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.memory,parent,false);
        return new MemoryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemoryListAdapter.ViewHolder holder, int position) {
        singleMemory memoryData = memories.get(position);
        holder.memoryImage.setImageURI(memoryData.image);
        holder.title.setText(memoryData.title);
        holder.date.setText(memoryData.date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another activity
                Intent intent = new Intent(context,MemoryViewer.class);
                intent.putExtra("LIST",memories);
                intent.putExtra("INDEX",holder.getAdapterPosition());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.cardOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.cardOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                index = holder.getAdapterPosition();
                                MainActivity.notify(index);
                                return true;
                            case R.id.menu2:
                                //handle menu2 click
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        try{
        return memories.size();
        }
        catch(Exception e){
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView date;
        TextView location;
        ImageView memoryImage;
        TextView cardOption;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.memory_title);
            date = itemView.findViewById(R.id.memory_date);
            location = itemView.findViewById(R.id.memory_location);
            memoryImage = itemView.findViewById(R.id.memory_image);
            cardOption = itemView.findViewById(R.id.card_options);
        }
    }


}

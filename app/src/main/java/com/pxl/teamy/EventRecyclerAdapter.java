package com.pxl.teamy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    public List<EventPost> event_list;
    public Context context;



    public EventRecyclerAdapter(List<EventPost> event_list){ this.event_list = event_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list_item,viewGroup,false);
        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String desc_data = event_list.get(i).getDesc();



        String image_url = event_list.get(i).getImage_uri();
        viewHolder.setDescText(desc_data);
        viewHolder.setEventImage(image_url);
    }

    @Override
    public int getItemCount() {
        return event_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView descView;
        private ImageView eventImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescText(String descText){
            descView = mView.findViewById(R.id.event_desc);
            descView.setText(descText);
        }

            public void setEventImage(String downloadUrl){
                eventImageView = mView.findViewById(R.id.event_image);
                Glide.with(context).load(downloadUrl).into(eventImageView);

            }


    }




}






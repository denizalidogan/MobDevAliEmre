package com.pxl.teamy.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pxl.teamy.DomainClasses.Comments;
import com.pxl.teamy.DomainClasses.User;
import com.pxl.teamy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comments> commentsList;
    public List<User> user_list;
    public Context context;




    public CommentsRecyclerAdapter(List<Comments> commentsList, List <User> user_list){
        this.user_list = user_list;
        this.commentsList = commentsList;

    }



    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String commentMessage = commentsList.get(position).getMessage();
        String userName = user_list.get(position).getUsername();


        holder.setComment_ProfImage( user_list.get(position).getImage());

        holder.setComment_username(userName);
        holder.setComment_message(commentMessage);


//        String date = DateFormat.format("HH:mm:ss dd-MM-yyyy", commentsList.get(position).getTimestamp()).toString();
//        holder.setComment_date(commentsList.get(position).getTimestamp().toString());
    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;



        private TextView comment_username;



        private TextView comment_date;



        private ImageView comment_ProfImage;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComment_date(String datum) {
            comment_date = mView.findViewById(R.id.comment_date);
            comment_date.setText(datum);
        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }
        public void setComment_username(String username) {
            comment_username = mView.findViewById(R.id.comment_username);
            comment_username.setText(username);
        }

        public void setComment_ProfImage(String image) {



            comment_ProfImage =  mView.findViewById(R.id.comment_image);
            RequestOptions placeholderOption = new RequestOptions();

            placeholderOption.placeholder(R.drawable.profile_placeholder);


            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(comment_ProfImage);

        }


    }

}

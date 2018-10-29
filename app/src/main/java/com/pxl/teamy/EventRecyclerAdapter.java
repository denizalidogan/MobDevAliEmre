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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Converter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    public List<EventPost> event_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    public EventRecyclerAdapter(List<EventPost> event_list){ this.event_list = event_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list_item,viewGroup,false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String desc_data = event_list.get(i).getDesc();
        String image_url = event_list.get(i).getImage_uri();
        String user_id = event_list.get(i).getUser_id();

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    viewHolder.setUserData(userName,userImage);



                } else{

                    //Firebase Exception
                }




            }
        });





        viewHolder.setDescText(desc_data);
        viewHolder.setEventImage(image_url);



//        // SimpleDateFormat formatter = new SimpleDateFormat("dd:HH:mm:ss");
//
//
//        long  milliseconds = Long.parseLong(event_list.get(i).getTime());
//
//        String dateString = android.text.format.DateFormat.format("dd/MM/yyyy", new Date(milliseconds)).toString();
//
//
//        Date date = new Date(milliseconds);
//        String dateString2 = formatter.format(date);


        viewHolder.setTime(event_list.get(i).getDate() + " " + event_list.get(i).getTime());

    }




    @Override
    public int getItemCount() {
        return event_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView descView;
        private ImageView eventImageView;
        private TextView eventCreatedDate;

        private TextView eventUserName;
        private CircleImageView eventUserImage;

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

                RequestOptions placeholderOption = new RequestOptions();
                placeholderOption.placeholder(R.drawable.image_placeholder);
                Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(downloadUrl).into(eventImageView);

            }

            public void setTime(String date){
                eventCreatedDate = mView.findViewById(R.id.event_created_date);
                eventCreatedDate.setText(date);
        }

            public void setUserData(String name, String image){
            eventUserImage = mView.findViewById(R.id.event_user_image);
            eventUserName = mView.findViewById(R.id.event_Username);

            eventUserName.setText(name);

                RequestOptions placeholderOption = new RequestOptions();
                placeholderOption.placeholder(R.drawable.profile_placeholder);



            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(eventUserImage);


            }


    }




}






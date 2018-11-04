package com.pxl.teamy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Converter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    public List<EventPost> event_list;
    public List<User> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;





    public EventRecyclerAdapter(List<EventPost> event_list, List <User> user_list) {
        this.event_list = event_list;
        this.user_list = user_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list_item, viewGroup, false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {


        viewHolder.setIsRecyclable(false);


        String desc_data = event_list.get(i).getDesc();
        String image_url = event_list.get(i).getImage_uri();
        String event_user_id = event_list.get(i).getUser_id();


        String userName = user_list.get(i).getName();
        String userImage = user_list.get(i).getImage();

        viewHolder.setUserData(userName, userImage);




        String thumbUrl = event_list.get(i).getImage_thumb();
        final String maxParticipants = event_list.get(i).getMaxParticipants();
        final String eventPostId = event_list.get(i).EventPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        if(event_user_id.equals(currentUserId)){
            viewHolder.btnEventDelete.setEnabled(true);
            viewHolder.btnEventDelete.setVisibility(View.VISIBLE);
        }



        viewHolder.setDescText(desc_data);
        viewHolder.setEventImage(image_url, thumbUrl);


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


        //Get Likes Count
        firebaseFirestore.collection("Posts/" + eventPostId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (!documentSnapshots.isEmpty()) {

                    int count = documentSnapshots.size();

                    viewHolder.updateJoinersCount(count, maxParticipants);

                } else {

                    viewHolder.updateJoinersCount(0, maxParticipants);

                }

            }
        });


        //Get Likes
        firebaseFirestore.collection("Posts/" + eventPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (documentSnapshot.exists()) {

                    viewHolder.eventJoinBtn.setImageDrawable(context.getDrawable(R.mipmap.action_join_color));

                } else {

                    viewHolder.eventJoinBtn.setImageDrawable(context.getDrawable(R.mipmap.action_join_accent));

                }

            }
        });


        //get Likes


//
//        firebaseFirestore.collection("Posts").document(eventPostId).collection("Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                if(!documentSnapshot.exists()){
//                    viewHolder.eventJoinBtn.setImageDrawable(context.getDrawable(R.mipmap.action_join_accent));
//
//                }
//
//                else{
//                    viewHolder.eventJoinBtn.setImageDrawable(context.getDrawable(R.mipmap.action_join_color));
//                }
//
//            }
//        });


        viewHolder.eventJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts").document(eventPostId).collection("Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                        if (!task.getResult().exists()) {
                            Map<String, Object> JoinersMap = new HashMap<>();
                            JoinersMap.put("timestamp", FieldValue.serverTimestamp());
                            JoinersMap.put("user_id", currentUserId);
                            firebaseFirestore.collection("Posts").document(eventPostId).collection("Likes").document(currentUserId).set(JoinersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "You have succesfully Joined!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {

                            firebaseFirestore.collection("Posts").document(eventPostId).collection("Likes").document(currentUserId).delete();
                            Toast.makeText(context, "You have left the event!", Toast.LENGTH_LONG).show();

                        }


                    }
                });


            }


        });

        viewHolder.detailpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("EVENT_POST_ID", eventPostId);
                context.startActivity(intent);
            }
        });

        viewHolder.detailpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("EVENT_POST_ID", eventPostId);
                context.startActivity(intent);
            }
        });

        viewHolder.btnEventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts").document(eventPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        event_list.remove(i);
                        user_list.remove(i);
                        
                    }
                });

            }
        });



        viewHolder.eventCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context,CommentsActivity.class);
                commentIntent.putExtra("event_post_id", eventPostId);
                context.startActivity(commentIntent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return event_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView descView;
        private ImageView eventImageView;
        private TextView eventCreatedDate;
        private Button btnEventDelete;
        private TextView eventUserName;
        private CircleImageView eventUserImage;
        private ImageView eventJoinBtn;
        private TextView eventJoinCount;

        private ImageView eventCommentBtn;


        private ConstraintLayout detailpage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            eventJoinBtn = mView.findViewById(R.id.event_join_btn);
            detailpage = mView.findViewById(R.id.detailpage);
             eventCommentBtn = mView.findViewById(R.id.event_comment_icon);
             btnEventDelete = mView.findViewById(R.id.btnDeleteEvent);


        }

        public void setDescText(String descText) {
            descView = mView.findViewById(R.id.event_desc);
            descView.setText(descText);
        }

        public void setEventImage(String downloadUrl, String thumbUrl) {
            eventImageView = mView.findViewById(R.id.event_image);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.image_placeholder);
            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(downloadUrl).thumbnail(Glide.with(context).load(thumbUrl)).into(eventImageView);

        }

        public void setTime(String date) {
            eventCreatedDate = mView.findViewById(R.id.event_created_date);
            eventCreatedDate.setText(date);
        }

        public void setUserData(String name, String image) {
            eventUserImage = mView.findViewById(R.id.event_user_image);
            eventUserName = mView.findViewById(R.id.event_Username);

            eventUserName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);


            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(eventUserImage);


        }


        public void updateJoinersCount(int count, String maxParticipants) {

            eventJoinCount = mView.findViewById(R.id.event_join_count);


            if (count < 1)
                eventJoinCount.setText(count + " / " + maxParticipants);
            else
                eventJoinCount.setText(count + " / " + maxParticipants);

        }


    }


}






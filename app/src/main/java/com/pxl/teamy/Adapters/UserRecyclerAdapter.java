package com.pxl.teamy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pxl.teamy.DomainClasses.EventPost;
import com.pxl.teamy.DomainClasses.User;
import com.pxl.teamy.R;
import com.pxl.teamy.ViewActivities.DetaillActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    public List<EventPost> event_list;
    public List<User> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public UserRecyclerAdapter(List<EventPost> event_list, List <User> user_list) {
        this.event_list = event_list;
        this.user_list = user_list;
    }

    @NonNull
    @Override
    public UserRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_item, viewGroup, false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new UserRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserRecyclerAdapter.ViewHolder viewHolder, final int i) {


        viewHolder.setIsRecyclable(false);



        String userName = user_list.get(i).getName();
        String userImage = user_list.get(i).getImage();

        viewHolder.setUserData(userName, userImage);


        final String maxParticipants = event_list.get(i).getMaxParticipants();
        final String eventPostId = event_list.get(i).EventPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

//        if(event_user_id.equals(currentUserId)){
//            viewHolder.btnEventDelete.setEnabled(true);
//            viewHolder.btnEventDelete.setVisibility(View.VISIBLE);
//        }

        //Get Likes Count
//        firebaseFirestore.collection("Posts/" + eventPostId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//
//                if (!documentSnapshots.isEmpty()) {
//
//                    int count = documentSnapshots.size();
//
//                    viewHolder.updateJoinersCount(count, maxParticipants);
//
//                } else {
//
//                    viewHolder.updateJoinersCount(0, maxParticipants);
//
//                }
//
//            }
//        });
//
//
//        //Get Likes
//        firebaseFirestore.collection("Posts/" + eventPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//
//                if (documentSnapshot.exists()) {
//
//                    viewHolder.eventJoinBtn.setImageDrawable(context.getDrawable(R.mipmap.action_join_color));
//
//                } else {
//
//                    viewHolder.eventJoinBtn.setImageDrawable(context.getDrawable(R.mipmap.action_join_accent));
//
//                }
//
//            }
//        });


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



    }


    @Override
    public int getItemCount() {
        return user_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView eventUserName;
        private CircleImageView eventUserImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUserData(String name, String image) {
            eventUserImage = mView.findViewById(R.id.event_user_image);
            eventUserName = mView.findViewById(R.id.event_Username);

            eventUserName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);


            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(eventUserImage);


        }


//        public void updateJoinersCount(int count, String maxParticipants) {
//
//            eventJoinCount = mView.findViewById(R.id.event_join_count);
//
//
//            if (count < 1)
//                eventJoinCount.setText(count + " / " + maxParticipants);
//            else
//                eventJoinCount.setText(count + " / " + maxParticipants);
//
//        }


    }


}






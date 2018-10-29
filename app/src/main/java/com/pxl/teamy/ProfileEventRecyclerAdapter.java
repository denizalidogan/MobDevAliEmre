package com.pxl.teamy;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEventRecyclerAdapter extends RecyclerView.Adapter<ProfileEventRecyclerAdapter.ViewHolder> {

    public List<EventPost> event_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public ProfileEventRecyclerAdapter(List<EventPost> event_list) {
        this.event_list = event_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_list_item, viewGroup, false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        String title_data = event_list.get(i).getTitle();
        String desc_data = event_list.get(i).getDesc();
        String location = event_list.get(i).getLocation();
        String count = event_list.get(i).getMaxParticipants();
        String image_url = event_list.get(i).getImage_uri();
        String user_id = event_list.get(i).getUser_id();
        String thumbUrl = event_list.get(i).getImage_thumb();
        final String eventPostId = event_list.get(i).EventPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    viewHolder.setUserData(userName, userImage);


                } else {

                    //Firebase Exception
                }


            }
        });

        viewHolder.setTitleText(title_data);
        viewHolder.setLocationText(location);
        viewHolder.setCountText(count + " Max Participants");
        viewHolder.setEventImage(image_url,thumbUrl);



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


        viewHolder.eventJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> JoinersMap = new HashMap<>();
                JoinersMap.put("timestamp", FieldValue.serverTimestamp());
                firebaseFirestore.collection("Posts").document(eventPostId).collection("Likes").document(currentUserId).set(JoinersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "You have succesfully Joined!",Toast.LENGTH_LONG).show();
                    }
                });

            }


        }) ;


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
        private TextView countView;
        private TextView titleView;
        private TextView locationView;

        private TextView eventUserName;
        private CircleImageView eventUserImage;
        private ImageView eventJoinBtn;
        private TextView eventJoinCount;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            eventJoinBtn = mView.findViewById(R.id.event_join_btn);

        }

        public void setTitleText(String titleText) {
            titleView = mView.findViewById(R.id.event_title);
            titleView.setText(titleText);
        }

        public void setLocationText(String locationText) {
            locationView = mView.findViewById(R.id.event_adress);
            locationView.setText(locationText);
        }

        public void setCountText(String countText) {
            countView = mView.findViewById(R.id.event_count);
            countView.setText(countText);
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


    }


}






package com.pxl.teamy.ViewFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pxl.teamy.Adapters.EventRecyclerAdapter;
import com.pxl.teamy.DomainClasses.EventPost;
import com.pxl.teamy.DomainClasses.Statics;
import com.pxl.teamy.DomainClasses.User;
import com.pxl.teamy.R;
import com.pxl.teamy.ViewFragments.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ImageView detailImageView;
    private TextView detailPostDesc;
    private TextView detailPostDate;
    private TextView detailPostTime;
    private TextView detailPostLocation;
    private TextView detailPostMaxPart;
    private TextView detailPostTitle;
    private Button deleteBtn;
    private RecyclerView recyclerView;

    private TextView desc_hint;

    private Boolean isFirstPageFirstLoad = true;

    private DocumentSnapshot lastVisible;

    private List<EventPost> event_list;
    private List<User> user_list;
    private EventRecyclerAdapter eventRecyclerAdapter;

    private ProgressBar detailPostProgress;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);


        event_list = new ArrayList<>();

        user_list = new ArrayList<>();
        eventRecyclerAdapter = new EventRecyclerAdapter(event_list, user_list);


//        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
//        setSupportActionBar(setupToolbar);
//        getSupportActionBar().setTitle("Account Setup");

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        detailImageView = view.findViewById(R.id.detail_post_image);
        detailPostDesc = view.findViewById(R.id.detail_post_desc);
        detailPostDate = view.findViewById(R.id.detail_post_date);
        detailPostTime = view.findViewById(R.id.detail_post_time);
        detailPostLocation = view.findViewById(R.id.detail_post_location);
        detailPostMaxPart = view.findViewById(R.id.detail_post_max);
        detailPostProgress = view.findViewById(R.id.detail_post_progress);
        detailPostTitle = view.findViewById(R.id.detail_post_title);
        deleteBtn = view.findViewById(R.id.btnDeleteEvent);
        desc_hint = view.findViewById(R.id.desc_hint);

        final Bundle b = getArguments();


//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent commentIntent = new Intent(getContext(), CommentsActivity.class);
//                commentIntent.putExtra("event_post_id", eventpostid);
//                startActivity(commentIntent);
//            }
//        });

        if(b != null){
            firebaseFirestore.collection("Posts").document(b.getString("EVENT_POST_ID")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                    //Does the colllection excist?
                    if (task.isSuccessful()) {
                        // User exist?
                        if (task.getResult().exists()) {

                            String date = task.getResult().getString("date");
                            String desc = task.getResult().getString("desc");
                            String image = task.getResult().getString("image_url");
                            String location = task.getResult().getString("location");
                            String max = task.getResult().getString("maxParticipants");
                            String time = task.getResult().getString("time");
                            String title = task.getResult().getString("title");
                            String user_id = task.getResult().getString("user_id");


                            detailImageView.setImageURI(Uri.parse(image));
                            detailPostDate.setText(date);
                            detailPostDesc.setText(desc);
                            detailPostLocation.setText(location);
                            detailPostMaxPart.setText(max);
                            detailPostTime.setText(time);
                            //detailPostTitle.setText(title);
                            //


//                        RequestOptions placeholderRequest = new RequestOptions();
//                        placeholderRequest.placeholder(R.drawable.default_image);
                            Glide.with(DetailFragment.this).load(image).into(detailImageView);

                            final String currentUserId = firebaseAuth.getCurrentUser().getUid();


                            if (user_id.equals(currentUserId)) {
                                deleteBtn.setEnabled(true);
                                deleteBtn.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                    }

                    detailPostProgress.setVisibility(View.INVISIBLE);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    firebaseFirestore.collection("Posts").document(b.getString("EVENT_POST_ID")).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

//                        for(int i = 0; i <= event_list.size(); i++){
//                            if(event_list.get(i).EventPostId == eventpostid){
//                                event_list.remove(i);
//                            }
//                        }
                            //event_list.remove(eventRecyclerAdapter.getItemNumber());
                            //user_list.remove(eventRecyclerAdapter.getItemNumber());
                            Intent mainIntent = new Intent(getContext(), HomeFragment.class);
                            startActivity(mainIntent);
                        }
                    });

                }
            });
         }
// else{
//            firebaseFirestore.collection("Posts").document(Statics.getEventpostId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//
//                    //Does the colllection excist?
//                    if (task.isSuccessful()) {
//                        // User exist?
//                        if (task.getResult().exists()) {
//
//                            String date = task.getResult().getString("date");
//                            String desc = task.getResult().getString("desc");
//                            String image = task.getResult().getString("image_url");
//                            String location = task.getResult().getString("location");
//                            String max = task.getResult().getString("maxParticipants");
//                            String time = task.getResult().getString("time");
//                            String title = task.getResult().getString("title");
//                            String user_id = task.getResult().getString("user_id");
//
//
//                            detailImageView.setImageURI(Uri.parse(image));
//                            detailPostDate.setText(date);
//                            detailPostDesc.setText(desc);
//                            detailPostLocation.setText(location);
//                            detailPostMaxPart.setText(max);
//                            detailPostTime.setText(time);
//                            //detailPostTitle.setText(title);
//                            //
//
//
////                        RequestOptions placeholderRequest = new RequestOptions();
////                        placeholderRequest.placeholder(R.drawable.default_image);
//                            Glide.with(DetailFragment.this).load(image).into(detailImageView);
//
//                            final String currentUserId = firebaseAuth.getCurrentUser().getUid();
//
//
//                            if (user_id.equals(currentUserId)) {
//                                deleteBtn.setEnabled(true);
//                                deleteBtn.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                    } else {
//
//                        String error = task.getException().getMessage();
//                        Toast.makeText(getContext(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
//                    }
//
//                    detailPostProgress.setVisibility(View.INVISIBLE);
//                }
//            });
//
//            deleteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    firebaseFirestore.collection("Posts").document(Statics.getEventpostId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
////                        for(int i = 0; i <= event_list.size(); i++){
////                            if(event_list.get(i).EventPostId == eventpostid){
////                                event_list.remove(i);
////                            }
////                        }
//                            //event_list.remove(eventRecyclerAdapter.getItemNumber());
//                            //user_list.remove(eventRecyclerAdapter.getItemNumber());
//                            Intent mainIntent = new Intent(getContext(), HomeFragment.class);
//                            startActivity(mainIntent);
//                        }
//                    });
//
//                }
//            });
//        }


        return view;
    }


    @Override
    public void onDetach() {
        isFirstPageFirstLoad = true;
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        isFirstPageFirstLoad = true;
        super.onAttach(context);
    }




}

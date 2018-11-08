package com.pxl.teamy.ViewActivities;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pxl.teamy.Adapters.EventRecyclerAdapter;
import com.pxl.teamy.Adapters.UserRecyclerAdapter;
import com.pxl.teamy.DomainClasses.EventPost;
import com.pxl.teamy.DomainClasses.User;
import com.pxl.teamy.R;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class DetaillActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        event_list = new ArrayList<>();

        user_list = new ArrayList<>();
        eventRecyclerAdapter = new EventRecyclerAdapter(event_list, user_list);


//        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
//        setSupportActionBar(setupToolbar);
//        getSupportActionBar().setTitle("Account Setup");

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        detailImageView = findViewById(R.id.detail_post_image);
        detailPostDesc = findViewById(R.id.detail_post_desc);
        detailPostDate = findViewById(R.id.detail_post_date);
        detailPostTime = findViewById(R.id.detail_post_time);
        detailPostLocation = findViewById(R.id.detail_post_location);
        detailPostMaxPart = findViewById(R.id.detail_post_max);
        detailPostProgress = findViewById(R.id.detail_post_progress);
        detailPostTitle = findViewById(R.id.detail_post_title);
        deleteBtn = findViewById(R.id.btnDeleteEvent);
        desc_hint = findViewById(R.id.desc_hint);


        final String eventpostid = getIntent().getStringExtra("EVENT_POST_ID");
        final String titelLabel = getIntent().getStringExtra("EVENT_TITLE");



        getSupportActionBar().setTitle(titelLabel);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(DetaillActivity.this, CommentsActivity.class);
                commentIntent.putExtra("event_post_id", eventpostid);
                startActivity(commentIntent);
            }
        });

        firebaseFirestore.collection("Posts").document(eventpostid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                        getSupportActionBar().setTitle(title);

//                        RequestOptions placeholderRequest = new RequestOptions();
//                        placeholderRequest.placeholder(R.drawable.default_image);

                        Glide.with(DetaillActivity.this).load(image).into(detailImageView);

                        final String currentUserId = firebaseAuth.getCurrentUser().getUid();


                        if(user_id.equals(currentUserId)){
                            deleteBtn.setEnabled(true);
                            deleteBtn.setVisibility(View.VISIBLE);
                        }
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(DetaillActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                }

                detailPostProgress.setVisibility(View.INVISIBLE);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts").document(eventpostid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

//                        for(int i = 0; i <= event_list.size(); i++){
//                            if(event_list.get(i).EventPostId == eventpostid){
//                                event_list.remove(i);
//                            }
//                        }
                        //event_list.remove(eventRecyclerAdapter.getItemNumber());
                        //user_list.remove(eventRecyclerAdapter.getItemNumber());
                        Intent mainIntent = new Intent(DetaillActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                    }
                });

            }
        });


    }



}

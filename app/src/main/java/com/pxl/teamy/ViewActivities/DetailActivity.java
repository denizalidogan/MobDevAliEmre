package com.pxl.teamy.ViewActivities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pxl.teamy.R;


public class DetailActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private ImageView detailImageView;
    private TextView detailPostDesc;
    private TextView detailPostDate;
    private TextView detailPostTime;
    private TextView detailPostLocation;
    private TextView detailPostMaxPart;
    private TextView detailPostTitle;

    private ProgressBar detailPostProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailevent);

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

        String eventpostid = getIntent().getStringExtra("EVENT_POST_ID");

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
                        detailPostTitle.setText(title);

//                        RequestOptions placeholderRequest = new RequestOptions();
//                        placeholderRequest.placeholder(R.drawable.default_image);

                        Glide.with(DetailActivity.this).load(image).into(detailImageView);
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(DetailActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                }

                detailPostProgress.setVisibility(View.INVISIBLE);
            }
        });

    }
}

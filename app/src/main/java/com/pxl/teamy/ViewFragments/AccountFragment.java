package com.pxl.teamy.ViewFragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pxl.teamy.Adapters.ProfileEventRecyclerAdapter;
import com.pxl.teamy.DomainClasses.EventPost;
import com.pxl.teamy.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    private String user_id;
    private ImageView setupImage;
    private Uri mainImageURI = null;
    private TextView setupName;
    private TextView setupBirth;
    private TextView setupBio;
    private TextView setupGender;
    private TextView countEvents;


    private RecyclerView event_list_view;
    private List<EventPost> event_list;

    private int count = 0;

    private FirebaseFirestore firebaseFirestore;
    private ProfileEventRecyclerAdapter profileEventRecyclerAdapter;
    private DocumentSnapshot lastVisible;
    private FirebaseAuth firebaseAuth;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        event_list = new ArrayList<>();
        event_list_view = view.findViewById(R.id.profile_event_list_view);

        profileEventRecyclerAdapter = new ProfileEventRecyclerAdapter(event_list);

        event_list_view.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false));
        event_list_view.setAdapter(profileEventRecyclerAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

        setupImage = view.findViewById(R.id.setup_image);
        setupName = view.findViewById(R.id.setup_name);
        setupBirth = view.findViewById(R.id.setup_birth);
        setupBio = view.findViewById(R.id.setup_bio);
        setupGender = view.findViewById(R.id.setup_gender);
        countEvents = view.findViewById(R.id.setup_events);

        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            event_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    boolean reachedBottom = !recyclerView.canScrollHorizontally(1);

                    if (reachedBottom) {
                        String desc = lastVisible.getString("desc");
                        Toast.makeText(container.getContext(), "No more events listed" , Toast.LENGTH_LONG).show();
                        LoadMorePost();
                    }
                }
            });

            Query firstQuery = firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING);


            firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        lastVisible = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.size() - 1);


                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String user = doc.getDocument().toObject(EventPost.class).getUser_id();


                                if(doc.getDocument().toObject(EventPost.class).getUser_id().contains(user_id)){
                                    String blogPostId = doc.getDocument().getId();
                                    EventPost eventPost = doc.getDocument().toObject(EventPost.class).withId(blogPostId);
                                    event_list.add(eventPost);
                                    profileEventRecyclerAdapter.notifyDataSetChanged();
                                    count ++;
                                }

                            }

                        }
                    }



                }
            });
        }

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                //Does the colllection excist?
                if (task.isSuccessful()) {
                    // User exist?
                    if (task.getResult().exists()) {

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String date = task.getResult().getString("dateOfBirth");
                        String bio = task.getResult().getString("bio");
                        String genderStr = task.getResult().getString("gender");


                        mainImageURI = Uri.parse(image);
                        setupName.setText(name);
                        setupBirth.setText(date);
                        setupGender.setText(genderStr);
                        setupBio.setText(bio);
                        countEvents.setText(event_list.size() + "");

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_image);

                        Glide.with(AccountFragment.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);
                    }

                } else {

                    String error = task.getException().getMessage();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void LoadMorePost() {
        Query nextQuery = firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).startAfter(lastVisible).limit(3);

        nextQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {


                    lastVisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);


                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {


                            if(doc.getDocument().toObject(EventPost.class).getUser_id().contains(user_id)){
                                EventPost eventPost = doc.getDocument().toObject(EventPost.class);
                                event_list.add(eventPost);
                                profileEventRecyclerAdapter.notifyDataSetChanged();
                                count++;
                            }
                        }

                    }
                }
            }
        });
    }

}

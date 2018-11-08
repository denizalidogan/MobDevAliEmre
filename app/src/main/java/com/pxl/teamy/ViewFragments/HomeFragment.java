package com.pxl.teamy.ViewFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.pxl.teamy.Adapters.EventRecyclerAdapter;
import com.pxl.teamy.DomainClasses.EventPost;
import com.pxl.teamy.DomainClasses.User;
import com.pxl.teamy.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView event_list_view;
    private List<EventPost> event_list;
    private List<User> user_list;


    private FirebaseFirestore firebaseFirestore;
    private EventRecyclerAdapter eventRecyclerAdapter;
    private DocumentSnapshot lastVisible;
    private FirebaseAuth firebaseAuth;

    private Boolean isFirstPageFirstLoad = true;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        event_list = new ArrayList<>();

        user_list = new ArrayList<>();
        event_list_view = view.findViewById(R.id.event_list_view);

        firebaseAuth = firebaseAuth.getInstance();
        eventRecyclerAdapter = new EventRecyclerAdapter(event_list, user_list);

        event_list_view.setLayoutManager(new GridLayoutManager(container.getContext(), 2));
        event_list_view.setAdapter(eventRecyclerAdapter);


        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            event_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {
                        String desc = lastVisible.getString("desc");
                        Toast.makeText(container.getContext(), "Reached" + desc, Toast.LENGTH_LONG).show();
                        LoadMorePost();
                    }
                }
            });


            Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(6);

            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        if (isFirstPageFirstLoad) {

                            lastVisible = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                            event_list.clear();
                            user_list.clear();
                        }

                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String eventPostId = doc.getDocument().getId();
                                final EventPost eventPost = doc.getDocument().toObject(EventPost.class).withId(eventPostId);

                                String eventUserId = doc.getDocument().getString("user_id");


                                firebaseFirestore.collection("Users").document(eventUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){

                                            User user = task.getResult().toObject(User.class);


                                            //dont load before users are getted
                                            if (isFirstPageFirstLoad) {
                                                event_list.add(eventPost);
                                                user_list.add(user);

                                            } else {
                                                event_list.add(0, eventPost);
                                                user_list.add(0,user);

                                            }
                                            eventRecyclerAdapter.notifyDataSetChanged();
                                        }

                                    }
                                });



                            }
                        }
                        isFirstPageFirstLoad = false;

                    }
                }
            });
        }
        // Inflate the layout for this fragment
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

    public void LoadMorePost() {
        Query nextQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).startAfter(lastVisible).limit(6);

        nextQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {


                    lastVisible = queryDocumentSnapshots.getDocuments()
                            .get(queryDocumentSnapshots.size() - 1);


                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {


                            String blogPostId = doc.getDocument().getId();


                            final EventPost eventPost = doc.getDocument().toObject(EventPost.class).withId(blogPostId);
                            String eventUserId = doc.getDocument().getString("user_id");



                            firebaseFirestore.collection("Users").document(eventUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){

                                        User user = task.getResult().toObject(User.class);


                                        //dont load before users are getted
                                             event_list.add(eventPost);
                                            user_list.add(user);


                                        eventRecyclerAdapter.notifyDataSetChanged();
                                    }

                                }
                            });


                        }

                    }
                }
            }
        });
    }

}

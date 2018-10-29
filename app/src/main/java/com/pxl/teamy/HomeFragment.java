package com.pxl.teamy;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView event_list_view;
    private List<EventPost> event_list;



    private FirebaseFirestore firebaseFirestore;
    private EventRecyclerAdapter eventRecyclerAdapter;
    private DocumentSnapshot lastVisible;


private FirebaseAuth firebaseAuth;
    public HomeFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        event_list = new ArrayList<>();
        event_list_view = view.findViewById(R.id.event_list_view);

        firebaseAuth = firebaseAuth.getInstance();
        eventRecyclerAdapter = new EventRecyclerAdapter(event_list);

         event_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
         event_list_view.setAdapter(eventRecyclerAdapter);



         if ( firebaseAuth.getCurrentUser()!=null) {

             firebaseFirestore = FirebaseFirestore.getInstance();

             event_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                     @Override
                                                     public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                         super.onScrolled(recyclerView, dx, dy);

                                                        boolean reachedBottom = !recyclerView.canScrollVertically(1);

        if (reachedBottom){
                                                            String desc = lastVisible.getString("desc");
                                                            Toast.makeText(container.getContext(),"Reached"  + desc,Toast.LENGTH_LONG).show();
                                                            LoadMorePost();
        }
                                                     }
                                                     });

                     Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);




             firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                 @Override
                 public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                     lastVisible = queryDocumentSnapshots.getDocuments()
                             .get(queryDocumentSnapshots.size() -1);



                     for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                         if (doc.getType() == DocumentChange.Type.ADDED) {

                             EventPost eventPost = doc.getDocument().toObject(EventPost.class);
                             event_list.add(eventPost);

                             eventRecyclerAdapter.notifyDataSetChanged();

                         }

                     }

                 }
             });
         }

        // Inflate the layout for this fragment
        return view;
    }

    public void LoadMorePost(){
        Query nextQuery = firebaseFirestore.collection("Posts").orderBy("timestamp",Query.Direction.DESCENDING).startAfter(lastVisible).limit(3);

        nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if(!queryDocumentSnapshots.isEmpty()){


                lastVisible = queryDocumentSnapshots.getDocuments()
                        .get(queryDocumentSnapshots.size() -1);



                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {





                        EventPost eventPost = doc.getDocument().toObject(EventPost.class);
                        event_list.add(eventPost);

                        eventRecyclerAdapter.notifyDataSetChanged();

                    }

                }
                    }
            }
        });
    }

}

package com.pxl.teamy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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


    public HomeFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        event_list = new ArrayList<>();
        event_list_view = view.findViewById(R.id.event_list_view);

        eventRecyclerAdapter = new EventRecyclerAdapter(event_list);

         event_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
         event_list_view.setAdapter(eventRecyclerAdapter);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if ( doc.getType() == DocumentChange.Type.ADDED){

                        EventPost eventPost = doc.getDocument().toObject(EventPost.class);
                        event_list.add(eventPost);

                        eventRecyclerAdapter.notifyDataSetChanged();

                    }

                }

            }
        });


        // Inflate the layout for this fragment
        return view;



    }

}

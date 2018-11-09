package com.pxl.teamy.Controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pxl.teamy.DomainClasses.User;
import com.pxl.teamy.IControllers.IUserCrud;

public class UserController implements IUserCrud {

    private FirebaseFirestore firebaseFirestore;
    User returnedUser;

    public User getUserByEventUserId(String eventUserId) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(eventUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    returnedUser = user;
                }
            }
        });
        return returnedUser;
    }


}




package com.pxl.teamy;



import com.google.firebase.firestore.Exclude;

import io.reactivex.annotations.NonNull;

public class EventPostId {

    @Exclude
    public String EventPostId;

    public <T extends EventPostId> T withId(@NonNull final String id) {
        this.EventPostId = id;

        return (T) this;

    }
}


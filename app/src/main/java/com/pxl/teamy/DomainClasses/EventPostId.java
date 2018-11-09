package com.pxl.teamy.DomainClasses;


import com.google.firebase.firestore.Exclude;

import io.reactivex.annotations.NonNull;

public class EventPostId {

    @Exclude
    public String EventPostId;

    public <T extends com.pxl.teamy.DomainClasses.EventPostId> T withId(@NonNull final String id) {
        this.EventPostId = id;

        return (T) this;

    }
}


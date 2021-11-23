package com.group10.uniso;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReceivedItem {
    private DatabaseReference databaseReference;
    public ReceivedItem() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Received.class.getSimpleName());

    }
    public Task<Void> add(Received emp) {

        return databaseReference.push().setValue(emp);

    }
}

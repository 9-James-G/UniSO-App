package com.group10.uniso;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelledItem {
    private DatabaseReference databaseReference;

    public CancelledItem() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Cancelled.class.getSimpleName());

    }

    public Task<Void> add(Cancelled emp) {

        return databaseReference.child(emp.getName()).setValue(emp);
    }
}
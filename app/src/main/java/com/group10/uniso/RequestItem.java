package com.group10.uniso;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestItem {
    private DatabaseReference databaseReference;

    public RequestItem() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Requests.class.getSimpleName());

    }

    public Task<Void> add(Requests emp) {

        return databaseReference.child(emp.getName()).setValue(emp);
    }
}
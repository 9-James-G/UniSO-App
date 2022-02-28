package com.group10.uniso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    TextInputEditText name, contact,department,idTxt;
    TextInputLayout FormnameLayout,FormcontactLayout,departmentLayout,idLayout;
    TextView tv1,selectImg;
    ImageView image;
    DrawerLayout drawerLayout;
    String userID,id,wews;
    FirebaseAuth fAuth;
    FirebaseUser user;
    DatabaseReference dbReference,dbRequests;
    Button btnEdit,btnSave,btnCancel;
    ProgressBar progressBar;
    public static String PREFS_NAME="MyPrefsFile";
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        dbRequests = FirebaseDatabase.getInstance().getReference("Requests");
        progressBar= findViewById(R.id.progressBar);
        tv1 = findViewById(R.id.tv1);
        selectImg= findViewById(R.id.selectImg);
//        saveImg= findViewById(R.id.saveImg);
        image= findViewById(R.id.image);
        idTxt=findViewById(R.id.idTxt);
        name = findViewById(R.id.Formname);
        contact = findViewById(R.id.Formcontact);
        department = findViewById(R.id.department);
        FormnameLayout = findViewById(R.id.FormnameLayout);
        FormcontactLayout = findViewById(R.id.FormcontactLayout);
        departmentLayout= findViewById(R.id.departmentLayout);
        idLayout = findViewById(R.id.departmentLayout);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel= findViewById(R.id.btnCancel);
        drawerLayout = findViewById(R.id.drawer_layout);

        dbReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(user!=null){
                    String userName, userContact,userDepartment,userImage,getId;
                    userName = user.name;
                    userDepartment = user.department;
                    userContact = user.contact;
                    userImage = user.imageUrl;
                    getId = user.id;

                    Picasso.get().load(userImage).into(image);
                    name.setText(userName);
                    contact.setText(userContact);
                    department.setText(userDepartment);
                    idTxt.setText(getId);
                    id = getId;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.GONE);
                name.setEnabled(true);
                contact.setEnabled(true);
//                department.setEnabled(true);
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
            }
        });
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wews = "wews";
//                saveImg.setVisibility(View.VISIBLE);
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });
//        saveImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (imageUri != null){
//                    uploadToFirebase(imageUri);
//
//                }else{
//                    Toast.makeText(Profile.this, "Please select an image", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name0 = name.getText().toString().trim();
                String contact0 = contact.getText().toString().trim();
                String department0 = department.getText().toString().trim();

//                if(wews!=null){
//                    Toast.makeText(Profile.this, "Upload image first!", Toast.LENGTH_LONG).show();
//                }else {
                    if (TextUtils.isEmpty(name0)) {
                        name.setError("Name required!");
                        return;
                    }
                    if (TextUtils.isEmpty(contact0) || contact0.length() < 11 || contact0.length() > 11) {
                        contact.setError("Valid contact required!");
                        return;
                    }
                    if (TextUtils.isEmpty(department0)) {
                        department.setError("Department required!");
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("name").setValue(name.getText().toString());
                    dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("contact").setValue(contact.getText().toString());
                    dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("department").setValue(department.getText().toString());
                dbRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(id).exists()){
                            dbRequests.child(id).child("name").setValue(name.getText().toString());
                            dbRequests.child(id).child("contact").setValue(contact.getText().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (imageUri != null){
                    uploadToFirebase(imageUri);

                }else{
                    selectImg.setVisibility(View.INVISIBLE);
                    btnEdit.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.GONE);
                    name.setEnabled(false);
                    contact.setEnabled(false);
                    department.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    recreate();}
//                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectImg.setVisibility(View.INVISIBLE);
//                btnEdit.setVisibility(View.VISIBLE);
//                btnSave.setVisibility(View.GONE);
//                name.setEnabled(false);
//                contact.setEnabled(false);
//                department.setEnabled(false);
//                progressBar.setVisibility(View.GONE);
                recreate();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            image.setImageURI(imageUri);}
    }
    private void uploadToFirebase(Uri uri){

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

//                        Model model = new Model();
                        dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("imageUrl").setValue(uri.toString());
                        dbRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child(id).exists()){
                                    dbRequests.child(id).child("imageUrl").setValue(uri.toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//                        saveImg.setVisibility(View.GONE);
                        selectImg.setVisibility(View.INVISIBLE);
                        btnEdit.setVisibility(View.VISIBLE);
                        btnSave.setVisibility(View.GONE);
                        name.setEnabled(false);
                        contact.setEnabled(false);
                        department.setEnabled(false);
                        progressBar.setVisibility(View.GONE);
                        recreate();
                        Picasso.get().load(uri.toString()).into(image);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                selectImg.setText("Uploading image....");
                btnSave.setEnabled(false);
                btnCancel.setEnabled(false);
                selectImg.setEnabled(false);
//                saveImg.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                selectImg.setText("Select image");
                btnSave.setEnabled(true);
                btnCancel.setEnabled(true);
                selectImg.setEnabled(true);
//                saveImg.setVisibility(View.VISIBLE);
                Toast.makeText(Profile.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    public void ClickMenu(View view){
        Form.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        Form.closeDrawer(drawerLayout);}

//    public void ClickNewReq(View view){
//        Form.redirectActivity(this,Form.class);finish();}

    public void ClickItems(View view){
        Form.redirectActivity(this,Form.class);
        finish();}

    public void ClickRequestsList(View view){
        Form.redirectActivity(this, RequestsList.class);finish();}

    public void ClickProfile(View view){
        recreate();}
    public void ClickPassword(View view){
        final AlertDialog.Builder passwordDialog = new AlertDialog.Builder(view.getContext());
        View mView = getLayoutInflater().inflate(R.layout.updatepassword,null);
        final EditText updatePassword = (EditText) mView.findViewById(R.id.updatePassword);

        passwordDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });passwordDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        passwordDialog.setView(mView);
        AlertDialog dialog =passwordDialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = updatePassword.getText().toString();
                if (TextUtils.isEmpty(newPass)) {
                    updatePassword.setError("Please enter your new password.");
                    return;
                }
                user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Profile.this, "Changed successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "An Error Occurred!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void ClickLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("token","signed out");
//                dbReference.child(userID).updateChildren(map);
                dbRequests.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(id).exists()){
                            dbRequests.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Requests requests = snapshot.getValue(Requests.class);
                                    if(requests!=null){
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("token", "signed out");
                                        dbRequests.child(id).updateChildren(map);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Profile.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            String status="else";
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                SharedPreferences sharedPreferences = getSharedPreferences(Form.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasloggedIn",false);
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Profile.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                finishAffinity();


            }});
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        builder.show();}

    @Override
    protected void onPause(){
        super.onPause();
        Form.closeDrawer(drawerLayout);
    }
//    public void btnEdit(View view) {
//        image.setEnabled(true);
//        tv1.setVisibility(View.GONE);
//        name.setEnabled(true);
//        contact.setEnabled(true);
//        department.setEnabled(true);
//        btnSave.setVisibility(View.VISIBLE);
//        btnEdit.setVisibility(View.GONE);
//    }

//    public void btnSave(View view) {
//
//        String name0 = name.getText().toString().trim();
//        String contact0 = contact.getText().toString().trim();
//        String department0 = department.getText().toString().trim();
//
//        if(TextUtils.isEmpty(name0)){
//            name.setError("Name required!");
//            return;
//        }
//        if (TextUtils.isEmpty(contact0)|| contact0.length()<11|| contact0.length()>11) {
//            contact.setError("Valid contact required!");
//            return;
//        }
//        if(TextUtils.isEmpty(department0)) {
//            department.setError("Department required!");
//            return;
//        }
//
//        dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("name").setValue(name.getText().toString());
//        dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("contact").setValue(contact.getText().toString());
//        dbReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("department").setValue(department.getText().toString());
//
////        tv1.setVisibility(View.VISIBLE);
//        btnEdit.setVisibility(View.VISIBLE);
//        btnSave.setVisibility(View.GONE);
//        name.setEnabled(false);
//        contact.setEnabled(false);
//        department.setEnabled(false);
//        recreate();
//    }
long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Double tap to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
package com.example.accesmembres;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MemberProfile extends AppCompatActivity {
    TextView nomMembre, emailMembre, phoneMembre;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String membreID;
    ImageView profileimage;
    StorageReference storageReference;
    Button buttonimage;
    public Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);
        nomMembre = findViewById(R.id.nomMembre);
        emailMembre = findViewById(R.id.emailMembre);
        phoneMembre = findViewById(R.id.phoneMembre);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        membreID = firebaseAuth.getCurrentUser().getUid();
        profileimage = findViewById(R.id.profileimage);
        buttonimage = findViewById(R.id.buttonimage);
        StorageReference profilRef = storageReference.child("users/" + membreID + "/profile.jpg");
        profilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileimage);
            }
        });
        DocumentReference documentReference = firestore.collection("membres").document(membreID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException err
            nomMembre.setText(value.getString("nom"));
            emailMembre.setText(value.getString("email"));
            phoneMembre.setText(value.getstring("phone"));
        }
    });
            buttonimage.setOnClickListener(new View.OnCLickListener()
    {
        @Override
        public void onClick (View v){
        choosePicture();
    }
    });


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult),
new ActivityResultCallback<ActivityResult>(){
        @Override
        public void onActivityResult (ActivityResult result){
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData);
            imageUri = data.getData();
            profileImage.setImageURI(imageUri):
        }
    }
    });


    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}
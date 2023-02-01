package com.example.accesmembres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MemberRegister extends AppCompatActivity {
    EditText NameText, email2, password, phone;
    Button button2;
    TextView login;
    ProgressBar progressBar2;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    String membreID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);
        // champs à utiliser
        NameText = findViewById(R.id.NameText);
        email2 = findViewById(R.id.email2);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        button2 = findViewById(R.id.button2);
        progressBar2 = findViewById(R.id.progressBar2);
        login = findViewById(R.id.login);
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = email2.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String fullname = NameText.getText().toString();
                String phonenum = phone.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    email2.setError("L'adresse email est requise");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Le mot de passe est requis");
                    return;
                }
                if (pass.length() < 6) {
                    password.setError("Minimum 6 caractères pour le mot de passe");
                    return;
                }
                progressBar2.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Toast.makeText(MemberRegister.this, "Compte crée", Toast.LENGTH_SHORT).show();
                                                                                                    membreID = fAuth.getCurrentUser().getUid();
                                                                                                    DocumentReference documentRefererence = firestore.collection("membres").document(membreID);
                                                                                                    Map<String, Object> membre = new HashMap<>();
                                                                                                    membre.put("nom", fullname);
                                                                                                    membre.put("email", email);
                                                                                                    membre.put("phone", phone);
                                                                                                    documentRefererence.set(membre).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void unused) {
                                                                                                            Log.d("TAG", "Profil créé pour le membre " + membreID);
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                );
            }
        }
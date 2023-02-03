package com.example.accesmembres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MemberLogin extends AppCompatActivity {
    TextView createacc, forgotpass;
    EditText Email, Password;
    Button button;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);
        createacc = findViewById(R.id.createacc);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        forgotpass = findViewById(R.id.forgpass);

        //validation entrés et login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString().trim();
                String passe = Password.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Email.setError("L'adresse email est requise");
                    return;
                }
                if (TextUtils.isEmpty(passe)) {
                    Password.setError("Le mot de passe est requis");
                    return;
                }

                if (passe.length() < 6) {
                    Password.setError("Minimum 6 caractères pour le mot de passe");
                    return;
                }
                progressBar.setVisibility(view.VISIBLE);
//rechercher les données
                fAuth.signInWithEmailAndPassword(email, passe).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MemberLogin.this, "Membre connecté", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MemberProfile.class));
                        } else {
                            Toast.makeText(MemberLogin.this, "Une erreur s'est produite " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MemberRegister.class));
            }
        });
        //password forgor
        forgotpass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Mot de passe oublié?");
                passwordResetDialog.setMessage("Ecris ton courriel pour recevoir le lien");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
// recupérer le email et envoyer le lien
                        String courriel = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(courriel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void Unused) {
                                Toast.makeText(MemberLogin.this, "Le lien a été envoyé à ton adresse", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MemberLogin.this, "Le lien n'a pas ete envoye" + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.show();
            }
        });
    }
}
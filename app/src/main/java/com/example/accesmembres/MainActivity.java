package com.example.accesmembres;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


//import com.google.firebase.firestore.core.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void entrer(View view) {
        startActivity(new Intent(getApplication(), MemberLogin.class));
        finish();
    }
}
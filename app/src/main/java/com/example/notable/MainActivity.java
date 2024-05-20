package com.example.notable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText girisEmail, girisSifre;
    private RelativeLayout giris, kayitOlGit;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        girisEmail = findViewById(R.id.girisEmail);
        girisSifre = findViewById(R.id.girisSifre);
        giris = findViewById(R.id.giris);
        kayitOlGit = findViewById(R.id.kayitOlGit);
        progressBar = findViewById(R.id.progressBarMain);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            Intent intent = new Intent(MainActivity.this, NotlarActivity.class);
            startActivity(intent);
        }

        kayitOlGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KayitOlActivity.class);
                startActivity(intent);
            }
        });

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = girisEmail.getText().toString().trim();
                String sifre = girisSifre.getText().toString().trim();

                if(email.isEmpty() || sifre.isEmpty())
                {
                    Toast.makeText(MainActivity.this, R.string.emptyEP, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, R.string.successG, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, NotlarActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, R.string.errorG, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
}
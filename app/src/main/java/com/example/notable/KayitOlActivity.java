package com.example.notable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class KayitOlActivity extends AppCompatActivity {

    private EditText kayitEmail, kayitSifre;
    private RelativeLayout kayitOl;
    private TextView girisGit;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        getSupportActionBar().hide();

        kayitEmail = findViewById(R.id.kayitEmail);
        kayitSifre = findViewById(R.id.kayitSifre);
        kayitOl = findViewById(R.id.kayitOl);
        girisGit = findViewById(R.id.girisGit);

        firebaseAuth = FirebaseAuth.getInstance();


        girisGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitOlActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = kayitEmail.getText().toString().trim();
                String sifre = kayitSifre.getText().toString().trim();

                if(email.isEmpty() || sifre.isEmpty())
                {
                    Toast.makeText(KayitOlActivity.this, R.string.emptyEP, Toast.LENGTH_SHORT).show();
                }
                else if(sifre.length()<5)
                {
                    Toast.makeText(KayitOlActivity.this, R.string.five, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),R.string.success, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(KayitOlActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),R.string.error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });
    }
}
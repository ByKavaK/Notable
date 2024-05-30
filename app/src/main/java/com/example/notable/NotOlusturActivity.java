package com.example.notable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotOlusturActivity extends AppCompatActivity {

    EditText notBaslikOlustur, notIcerik;
    FloatingActionButton notKaydet;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_olustur);

        notKaydet = findViewById(R.id.notKaydet);
        notBaslikOlustur = findViewById(R.id.notBaslikOlustur);
        notIcerik = findViewById(R.id.notIcerik);

        Toolbar toolbar = findViewById(R.id.notToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        notKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baslik = notBaslikOlustur.getText().toString();
                String icerik = notIcerik.getText().toString();
                if(baslik.isEmpty() || icerik.isEmpty())
                {
                    Toast.makeText(NotOlusturActivity.this, R.string.noteE, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String encryptedBaslik = caesarCipherEncrypt(baslik, 3);  // Şifreleme
                    String encryptedIcerik = caesarCipherEncrypt(icerik, 3);  // Şifreleme

                    DocumentReference documentReference = firebaseFirestore.collection("notlar").document(firebaseUser.getUid()).collection("notlarım").document();
                    Map<String, Object> not = new HashMap<>();
                    not.put("baslik", encryptedBaslik);
                    not.put("icerik", encryptedIcerik);

                    documentReference.set(not).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(NotOlusturActivity.this, R.string.noteU, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NotOlusturActivity.this, NotlarActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NotOlusturActivity.this, R.string.noteUE, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private String caesarCipherEncrypt(String text, int shift) {
        String alphabet = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ";
        String lowerAlphabet = alphabet.toLowerCase();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                String alphabetToUse = Character.isLowerCase(c) ? lowerAlphabet : alphabet;
                int baseIndex = alphabetToUse.indexOf(c);
                if (baseIndex != -1) {
                    int newIndex = (baseIndex + shift) % alphabetToUse.length();
                    result.append(alphabetToUse.charAt(newIndex));
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}

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

public class NotDuzenleActivity extends AppCompatActivity {

    Intent data;
    EditText notBaslikDuzenle, notIcerikDuzenle;
    FloatingActionButton notKaydetDuzenle;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_duzenle);

        notBaslikDuzenle = findViewById(R.id.notBaslikDuzenle);
        notIcerikDuzenle = findViewById(R.id.notIcerikDuzenle);
        notKaydetDuzenle = findViewById(R.id.notKaydetDuzenle);

        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.notToolbarDuzenle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notKaydetDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yeniBaslik = notBaslikDuzenle.getText().toString();
                String yeniIcerik = notIcerikDuzenle.getText().toString();

                if(yeniBaslik.isEmpty() || yeniIcerik.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), R.string.emptyS,Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DocumentReference documentReference = firebaseFirestore.collection("notlar").document(firebaseUser.getUid()).collection("notlarım").document(data.getStringExtra("notId"));
                    Map<String, Object> not = new HashMap<>();
                    not.put("baslik", yeniBaslik);
                    not.put("icerik", yeniIcerik);
                    documentReference.set(not).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), R.string.updateN,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NotDuzenleActivity.this, NotlarActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), R.string.eUpdateN,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        String notBaslik = data.getStringExtra("baslik");
        String notIcerik = data.getStringExtra("icerik");
        notBaslikDuzenle.setText(notBaslik);
        notIcerikDuzenle.setText(notIcerik);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
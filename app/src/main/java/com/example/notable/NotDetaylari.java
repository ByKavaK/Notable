package com.example.notable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotDetaylari extends AppCompatActivity {

    private TextView notBaslikDetay, notIcerikDetay;
    FloatingActionButton notDuzenleGit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_detaylari);
        notBaslikDetay = findViewById(R.id.notBaslikDetay);
        notIcerikDetay = findViewById(R.id.notIcerikDetay);
        notDuzenleGit = findViewById(R.id.notDuzenleGit);
        Toolbar toolbar = findViewById(R.id.notDetayToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();



        notDuzenleGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NotDuzenleActivity.class);
                intent.putExtra("baslik", data.getStringExtra("baslik"));
                intent.putExtra("icerik", data.getStringExtra("icerik"));
                intent.putExtra("notId", data.getStringExtra("notId"));
                v.getContext().startActivity(intent);
            }
        });

        notIcerikDetay.setText(data.getStringExtra("icerik"));
        notBaslikDetay.setText(data.getStringExtra("baslik"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private String caesarCipherDecrypt(String text, int shift) {
        String alphabet = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ";
        String lowerAlphabet = alphabet.toLowerCase();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                String alphabetToUse = Character.isLowerCase(c) ? lowerAlphabet : alphabet;
                int baseIndex = alphabetToUse.indexOf(c);
                if (baseIndex != -1) {
                    int newIndex = (baseIndex - shift + alphabetToUse.length()) % alphabetToUse.length();
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
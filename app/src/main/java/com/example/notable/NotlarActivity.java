package com.example.notable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotlarActivity extends AppCompatActivity {

    FloatingActionButton notOlustur;
    private FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder> notAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notlar);

        notOlustur = findViewById(R.id.notOlustur);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle(R.string.notes);

        notOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotlarActivity.this, NotOlusturActivity.class);
                startActivity(intent);
            }
        });

        Query query = firebaseFirestore.collection("notlar").document(firebaseUser.getUid()).collection("notlarım").orderBy("baslik", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<FirebaseModel> butunKulNot = new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query, FirebaseModel.class).build();
        notAdapter = new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(butunKulNot) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder notViewHolder, int i, @NonNull FirebaseModel firebaseModel) {

                ImageView popupButton = notViewHolder.itemView.findViewById(R.id.menupopbutton);

                int renkKod = getRandomColor();
                notViewHolder.not.setBackgroundColor(notViewHolder.itemView.getResources().getColor(renkKod, null));

                notViewHolder.notBaslik.setText(firebaseModel.getBaslik());
                notViewHolder.notIcerik.setText(firebaseModel.getIcerik());

                String docId = notAdapter.getSnapshots().getSnapshot(i).getId();

                notViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), NotDetaylari.class);
                        intent.putExtra("baslik",firebaseModel.getBaslik());
                        intent.putExtra("icerik",firebaseModel.getIcerik());
                        intent.putExtra("notId",docId);
                        v.getContext().startActivity(intent);
                    }
                });

                popupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Düzenle").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem item) {

                                Intent intent = new Intent(v.getContext(), NotDuzenleActivity.class);
                                intent.putExtra("baslik",firebaseModel.getBaslik());
                                intent.putExtra("icerik",firebaseModel.getIcerik());
                                intent.putExtra("notId",docId);
                                v.getContext().startActivity(intent);

                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Sil").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(@NonNull MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notlar").document(firebaseUser.getUid()).collection("notlarım").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(),R.string.delete,Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),R.string.deleteE,Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return false;
                            }
                        });

                        popupMenu.show();

                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notlar_layout, parent,false);
                return new NoteViewHolder(view);
            }
        };

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(notAdapter);

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder
    {
        private TextView notBaslik;
        private TextView notIcerik;
        LinearLayout not;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            notBaslik = itemView.findViewById(R.id.notBaslik);
            notIcerik = itemView.findViewById(R.id.notIcerik);
            not = itemView.findViewById(R.id.not);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.cikisYap)
        {
            firebaseAuth.signOut();
            Intent intent = new Intent(NotlarActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(notAdapter != null)
        {
            notAdapter.stopListening();
        }
    }

    private int getRandomColor()
    {
        List<Integer> renkKod = new ArrayList<>();
        renkKod.add(R.color.gray);
        renkKod.add(R.color.green);
        renkKod.add(R.color.lightgreen);
        renkKod.add(R.color.pink);
        renkKod.add(R.color.skyblue);
        renkKod.add(R.color.color1);
        renkKod.add(R.color.color2);
        renkKod.add(R.color.color3);
        renkKod.add(R.color.color4);
        renkKod.add(R.color.color5);

        Random random = new Random();
        int sayi = random.nextInt(renkKod.size());
        return renkKod.get(sayi);
    }

}
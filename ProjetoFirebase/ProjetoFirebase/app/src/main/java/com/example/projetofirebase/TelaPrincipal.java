package com.example.projetofirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class TelaPrincipal extends AppCompatActivity {

    private TextView user_name, user_email;
    private Button btn_out;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        getSupportActionBar().hide(); //esconder barra de ação

        StartComponents();
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TelaPrincipal.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        });

    }

    protected void onStart(){
       super.onStart();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = database.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot , @Nullable FirebaseFirestoreException error) {
            if (documentSnapshot != null){
                user_name.setText(documentSnapshot.getString("nome"));
                user_email.setText(email);

            }
            }
        });
    }

    public void StartComponents(){
        user_name = findViewById(R.id.text_nome_usuario);
        user_email = findViewById(R.id.text_email);
        btn_out = findViewById(R.id.btn_deslogar);
    }
}
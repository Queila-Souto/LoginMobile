package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class FormCadastro extends AppCompatActivity {
    private EditText edit_name, edit_email, edit_password;
    private Button btn_register;
    private String[] msg_error = {"Por favor, preencha todos os campos", "Cadastro realizado com suscesso"};
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide(); //esconder barra de ação
        StartComponents(); // inicializando os componentes

        btn_register.setOnClickListener(new View.OnClickListener() { //criando evento de cick no botao
            @Override
            public void onClick(View v) {  // Comando para a função clique do botao. É preciso criar uma View (v) para passar como parametro.

                //criando uma String que recebe o nome digitado pelo usuario(gettext) e converte em string.
                String name = edit_name.getText().toString();
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                //se os campos estiverem vazios vamos mostrar uma msg de erro pelo snackbar
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, msg_error[0], Snackbar.LENGTH_SHORT);
                    //No método make, é necessário passar como parametro uma view (nesse caso ja foi criada no evento clique)
                    // e uma mensagem, que nesse caso será uma array de strings, mensagem do indice 0;
                    //lenght é o tempo em que a mensagem será exibida na tela, no caso, será um tempo curto.
                    snackbar.setBackgroundTint(Color.WHITE); // definindo cor de fundo da mensagem
                    snackbar.setTextColor(Color.BLACK); // definindo cor de text
                    snackbar.show(); //exibindo mensagem
                } else {
                    Register_user(v);
                }
            }
        });
    }

    private void SaveUserData(){
        String name = edit_name.getText().toString();

        //iniciando firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> users = new HashMap<>(); //criando um map
        users.put("nome", name); //adicionando usuario ao map

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference= db.collection("users").document(userId);
        documentReference.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("db","Sucesso ao salvar os dados");

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
            Log.d("db_Error", "Erro ao salvar os dados" + e.toString());
            }
        });


    }

    private void Register_user(View v) { //função para cadastrar usuário
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // se o cadastro for bem sucedido: cadastre no BD Firebase e ...
                    SaveUserData();
                    TelaPrincipal();

                    //  envie a mensagemn
                    Snackbar snackbar = Snackbar.make(v, msg_error[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {

//tratando erros
                    String error;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Digite uma senha com no mínimo 6 caracteres";
                    } //senha com menos de 6 caracteres

                    catch (FirebaseAuthUserCollisionException e) {
                        error = "E-mail ja esta cadastrado.";
                    } // email ja existe

                    catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "E-mail inválido. ";
                    } // email com caracteres invalidos

                    catch (Exception e) {
                        error = "Erro ao cadastrar usuário";
                    }


                    Snackbar snackbar = Snackbar.make(v, error, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
                }
               });
    }

    private void StartComponents() { // função para relacionar o campo com os objetos criados
        edit_name = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_senha);
        btn_register = findViewById(R.id.btn_cadastrar);
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(FormCadastro.this,TelaPrincipal.class);
        startActivity(intent);
        finish();
    }


}
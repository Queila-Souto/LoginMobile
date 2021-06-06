package com.example.projetofirebase;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.time.format.TextStyle;


public class FormLogin extends AppCompatActivity {
private TextView text_tela_cadastro;
private EditText edit_email, edit_password;
private Button btn_entrar;
private ProgressBar progressBar;
private String[] msg_error = {"Por favor, preencha todos os campos", "Login realizado com suscesso"};
private Button showpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_senha);
        btn_entrar = findViewById((R.id.bt_entrar));
        progressBar = findViewById(R.id.progressbar);

        getSupportActionBar().hide(); //esconder barra de ação
        IniciarComponentes();

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(FormLogin.this,FormCadastro.class);
                startActivity(intent);
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = edit_email.getText().toString();
            String password = edit_password.getText().toString();

            if (email.isEmpty() || password.isEmpty()){
                Snackbar snackbar = Snackbar.make(v, msg_error[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
            Authenticater_user(v);

            }


            }
        });
    }

private void MostrarSenha(){


        edit_password.setTransformationMethod(null);
}


private void Authenticater_user(View v){
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            //se a autentificação for bem sucedida, exibir a progressbar

            if (task.isSuccessful()){
            progressBar.setVisibility(View.VISIBLE);

            //efeito animação:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TelaPrincipal();
                    }
                }, 3000); //duração da animação.

            } else{
                String error;
                try {
                    throw task.getException();
                }

                catch (FirebaseAuthInvalidCredentialsException e) {
                    error = "E-mail inválido. ";
                } // email com caracteres invalidos

                catch (Exception e) {
                    error = "Erro ao logar usuário";
                }

                Snackbar snackbar = Snackbar.make(v, error, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        }
    });


}

    @Override
    protected void onStart() { //ciclo de vida
        super.onStart();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if(current_user!=null){
            TelaPrincipal();
        }
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this,TelaPrincipal.class);
        startActivity(intent);
        finish();
    }


    //método para iniciar os componentes da nova tela

    private void IniciarComponentes(){
        text_tela_cadastro=findViewById(R.id.text_tela_cadastro);
    }
}
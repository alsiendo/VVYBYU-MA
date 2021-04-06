package com.example.vvybyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.rey.material.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vvybyu.Model.Users;
import com.example.vvybyu.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberInput_Login, passwordInput_Login;
    private Button loginBtn_Login;
    private ProgressDialog loadingBar;
    private TextView adminLink_Login, userLink_Login;

    private String parentDBName = "Users";
    private CheckBox rememberCheckbox_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn_Login = (Button) findViewById(R.id.loginBtn_Login);
        passwordInput_Login = (EditText) findViewById(R.id.passwordInput_Login);
        phoneNumberInput_Login = (EditText) findViewById(R.id.phoneNumberInput_Login);
        adminLink_Login = (TextView) findViewById(R.id.adminLink_Login);
        userLink_Login = (TextView) findViewById(R.id.userLink_Login);
        loadingBar = new ProgressDialog(this);

        rememberCheckbox_Login = (CheckBox) findViewById(R.id.rememberCheckbox_Login);
        Paper.init(this);

        loginBtn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        adminLink_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn_Login.setText("Login as Admin");
                adminLink_Login.setVisibility(View.INVISIBLE);
                userLink_Login.setVisibility(View.VISIBLE);
                parentDBName = "Admins";
            }
        });

        userLink_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn_Login.setText("Login");
                adminLink_Login.setVisibility(View.VISIBLE);
                userLink_Login.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
            }
        });
    }

    private void Login(){
        String phone = phoneNumberInput_Login.getText().toString();
        String password = passwordInput_Login.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Mohon isi nomor telepon anda...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Mohon isi password anda...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Proses Login");
            loadingBar.setMessage("Mohon menunggu.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(String phone, String password){

        if(rememberCheckbox_Login.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDBName).child(phone).exists()){
                    Users usersData = snapshot.child(parentDBName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            if (parentDBName.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Berhasil login sebagai Admin", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }else if (parentDBName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Berhasil login sebagai User", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password salah.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Akun dengan nomor " + phone + " belum dibuat.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.vvybyu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn_Register;
    private EditText usernameInput_Register, phoneNumberInput_Register, passwordInput_Register;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn_Register = (Button) findViewById(R.id.registerBtn_Register);
        usernameInput_Register = (EditText) findViewById(R.id.usernameInput_Register);
        phoneNumberInput_Register = (EditText) findViewById(R.id.phoneNumberInput_Register);
        passwordInput_Register = (EditText) findViewById(R.id.passwordInput_Register);
        loadingBar = new ProgressDialog(this);

        registerBtn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void Register(){
        String username = usernameInput_Register.getText().toString();
        String phone = phoneNumberInput_Register.getText().toString();
        String password = passwordInput_Register.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Mohon isi username anda...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Mohon isi nomor telepon anda...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Mohon isi password anda...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Proses Register");
            loadingBar.setMessage("Mohon menunggu.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(username, phone, password);
        }

    }

    private void ValidatePhoneNumber(String username, String phone, String password){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Selamat! Akun berhasil dibuat.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error: Coba lagi!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this,"Akun dengan nomor " + phone + " telah dibuat.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Gunakan nomor lain..", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
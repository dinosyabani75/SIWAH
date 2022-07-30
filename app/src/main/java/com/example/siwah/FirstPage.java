package com.example.siwah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstPage extends AppCompatActivity {
    private long exitTime = 0;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://siwah-ba1e5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        final TextView register = findViewById(R.id.register_button);
        final EditText nip_input = findViewById(R.id.nip_input);
        final EditText password_input = findViewById(R.id.password_input);
        final Button login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nip_txt = nip_input.getText().toString();
                final String password_txt = password_input.getText().toString();

                if(nip_txt.isEmpty() || password_txt.isEmpty()){
                    Toast.makeText(FirstPage.this, "Masukkan NIP dan Password Anda", Toast.LENGTH_LONG).show();
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(nip_txt)){
                                final String getPassword = snapshot.child(nip_txt).child("password").getValue(String.class);
                                if (getPassword.equals(password_txt)){
                                    Toast.makeText(FirstPage.this, "Berhasil Login", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                }else{
                                    Toast.makeText(FirstPage.this, "Password Salah", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(FirstPage.this, "NIP Belum Terdaftar / Salah", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    //Saat menekan tombol kembali, akan dikonfirmasi lagi apakah akan keluar aplikasi.
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 4000) {
            Toast.makeText(this, "Tekan lagi untuk keluar", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finishAndRemoveTask();
            this.finishAffinity();
        }

    }


}
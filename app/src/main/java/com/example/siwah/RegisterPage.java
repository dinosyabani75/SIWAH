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

public class RegisterPage extends AppCompatActivity {

//DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://siwah-ba1e5-default-rtdb.asia-southeast1.firebasedatabase.app");
DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://siwah-ba1e5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        final TextView login = findViewById(R.id.login_button);
        final Button register = findViewById(R.id.register_button);
        final EditText nip_reg = findViewById(R.id.nip_reg);
        final EditText nama_reg = findViewById(R.id.nama_reg);
        final EditText password_reg = findViewById(R.id.password_reg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nip_reg_txt = nip_reg.getText().toString();
                final String nama_reg_txt = nama_reg.getText().toString();
                final String password_reg_txt = password_reg.getText().toString();

                if(nip_reg_txt.isEmpty() || nama_reg_txt.isEmpty() || password_reg_txt.isEmpty()){
                    Toast.makeText(RegisterPage.this, "Lengkapi Data Anda", Toast.LENGTH_LONG).show();
                }
                else{


                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(nip_reg_txt)){
                                Toast.makeText(RegisterPage.this, "NIP Sudah Terdaftar", Toast.LENGTH_LONG).show();
                            }
                            else {
                                //Memasukkan data ke Firebase Realtime Database
                                //Menggunakan NIP sebagai Primary Key
                                //Sehingga semua informasi user akan tertera di NIP terkait.
                                databaseReference.child("users").child(nip_reg_txt).child("nomorindukkepegawaian").setValue(nip_reg_txt);
                                databaseReference.child("users").child(nip_reg_txt).child("namalengkap").setValue(nama_reg_txt);
                                databaseReference.child("users").child(nip_reg_txt).child("password").setValue(password_reg_txt);

                                //Mencetak text berhasil registrasi
                                Toast.makeText(RegisterPage.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });//End Of addListernerValueForSingle

                }

            }//End Of Method
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FirstPage.class));
            }
        });
    }

    //Saat menekan tombol kembali, akan dikonfirmasi lagi apakah akan keluar aplikasi.
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), FirstPage.class));
    }
}
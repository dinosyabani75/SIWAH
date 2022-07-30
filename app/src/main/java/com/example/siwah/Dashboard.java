package com.example.siwah;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final CardView menuCari = findViewById(R.id.menu1Card);
        final CardView menuUpload = findViewById(R.id.menu2Card);
        final CardView menuAbout = findViewById(R.id.menu3Card);
        final CardView menuExit = findViewById(R.id.menu4Card);

        menuCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), cariwarkahPage.class));
            }
        });
        menuUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), uploadwarkahPage.class));
            }
        });
        menuAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), aboutPage.class));
            }
        });
        menuExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });

    }

    private void showLogoutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title dialog
        alertDialogBuilder.setTitle("SIWAH");
        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Anda yakin untuk keluar?")
                .setIcon(R.drawable.logobpn)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialog, id) -> {
                    // jika tombol diklik, maka akan menutup activity ini
                    finishAndRemoveTask();
                    finishAffinity();
                    startActivity(new Intent(getApplicationContext(),FirstPage.class));
                })
                .setNegativeButton("Tidak", (dialog, id) -> {
                    // jika tombol ini diklik, akan menutup dialog
                    // dan tidak terjadi apa2
                    dialog.cancel();
                });
        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();
        // menampilkan alert dialog
        alertDialog.show();
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
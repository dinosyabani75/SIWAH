package com.example.siwah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class cariwarkahPage extends AppCompatActivity {

    Button view;
    DatabaseReference database;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cariwarkah_page);
        view = findViewById(R.id.view_button);

        // Initialising the reference to database
        database = FirebaseDatabase
                .getInstance("https://siwah-ba1e5-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference()
                .child("warkah");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // getting a DataSnapshot for the location at the specified
                // relative path and getting in the link variable
                message = dataSnapshot.getValue(String.class);
            }

            // this will called when any problem
            // occurs in getting data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(cariwarkahPage.this, "Error Loading Pdf", Toast.LENGTH_LONG).show();
            }
        });
        // After clicking here alert box will come
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CharSequence options[] = new CharSequence[]{
                        "Download",
                        "View",
                        "Cancel"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("OPTIONS");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we will be downloading the pdf
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(message));
                            startActivity(intent);
                        }
                        // We will view the pdf
                        if (which == 1) {
                            Intent intent = new Intent(v.getContext(), lihatwarkahPage.class);
                            intent.putExtra("url", message);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

    }
}
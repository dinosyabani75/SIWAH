package com.example.siwah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadwarkahPage extends AppCompatActivity {
    Uri imageuri = null;
    EditText nomorwarkah, tahunwarkah;
    Button uploadpdf;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://siwah-ba1e5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadwarkah_page);

        //final ImageView upload = findViewById(R.id.uploadpdf);
        uploadpdf = findViewById(R.id.upload_but);
        nomorwarkah = findViewById(R.id.nomor_warkah);
        tahunwarkah = findViewById(R.id.tahun_warkah);

        // After Clicking on this we will be
        // redirected to choose pdf
        uploadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mengambil value dari edittext untuk nama file
                final String nomor_warkah_inp = nomorwarkah.getText().toString();
                final String tahun_warkah_inp = tahunwarkah.getText().toString();

                //Jika data nomor dan tahun kosong, maka beri peringatan.
                if (nomor_warkah_inp.isEmpty() || tahun_warkah_inp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Lengkapi Data Warkah", Toast.LENGTH_LONG).show();
                }
                //Jika data lengkap maka bisa upload file.
                else{
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    // We will be redirected to choose pdf
                    galleryIntent.setType("application/pdf");
                    startActivityForResult(galleryIntent, 1);
                }

            }
        });
    }

    ProgressDialog dialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // Here we are initialising the progress dialog box
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading File");

            // this will show message uploading
            // while pdf is uploading
            dialog.show();
            imageuri = data.getData();
            final String timestamp = "" + System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance("gs://siwah-ba1e5.appspot.com").getReference("warkah");
            final String messagePushID = timestamp;
            Toast.makeText(uploadwarkahPage.this, imageuri.toString(), Toast.LENGTH_SHORT).show();

            //Mengambil value dari edittext untuk nama file
            final String nomor_warkah_txt = nomorwarkah.getText().toString();
            final String tahun_warkah_txt = tahunwarkah.getText().toString();
            final String kodewarkah = nomor_warkah_txt + "_" + tahun_warkah_txt;

            // Here we are uploading the pdf in firebase storage with the name of current time
            //final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
            final StorageReference filepath = storageReference.child(kodewarkah + "." + "pdf");
            databaseReference.child("warkah").child(kodewarkah).child("nomorwarkah").setValue(nomor_warkah_txt);
            databaseReference.child("warkah").child(kodewarkah).child("tahunwarkah").setValue(tahun_warkah_txt);
            Toast.makeText(uploadwarkahPage.this, filepath.getName(), Toast.LENGTH_SHORT).show();

            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        dialog.dismiss();
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();

                        //Pemberitahuan berhasil upload
                        Toast.makeText(uploadwarkahPage.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    } else {
                        dialog.dismiss();
                        //Pemberitahuan gagal upload
                        Toast.makeText(uploadwarkahPage.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
}
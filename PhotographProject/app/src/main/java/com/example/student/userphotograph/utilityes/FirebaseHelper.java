package com.example.student.userphotograph.utilityes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.student.userphotograph.models.Pictures;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class FirebaseHelper {

    public static void downloadImageAndSetAvatar(final StorageReference ref, final ImageView img) {
        ref.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public static String getFileExtension(Uri uri, Activity activity) {
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public static void upload(final Context context,
                              final String imageName,
                              final EditText titlePhoto,
                              final DatabaseReference databaseGalleryRef,
                              StorageReference storageGalleryRef,
                              Uri filePath
                              ){

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        StorageReference sRef = storageGalleryRef.child(Constants.STORAGE_PATH_UPLOADS + imageName);

        sRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "File Uploaded ", Toast.LENGTH_LONG).show();

                        @SuppressWarnings("VisibleForTests")
                        Pictures picture = new Pictures(titlePhoto.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString(), imageName);

                        String uploadId = databaseGalleryRef.push().getKey();
                        databaseGalleryRef.child(uploadId).setValue(picture);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(context.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        Toast.makeText(context, "Warning !!!, Error file ", Toast.LENGTH_LONG).show();
                        titlePhoto.setText("");
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
    }
}

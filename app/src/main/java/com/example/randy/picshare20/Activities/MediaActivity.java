package com.example.randy.picshare20.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.randy.picshare20.R;
import com.example.randy.picshare20.Model.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MediaActivity extends AppCompatActivity {

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseAddMainPhotos;
    private Button imageButton;
    private ImageView imageFromPhone;
    private static final int GALLERY_INTENT = 2;
    private static final String STORAGE_PATH_UPLOADS = "Photos/";
    private static final String DATABASE_PATH_UPLOADS = "Photos";
    private static final String DATABASE_PATH_UPLOADS_ROOT = "users";
    private Uri uri;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseAddMainPhotos = FirebaseDatabase.getInstance().getReference();
        imageButton = (Button) findViewById(R.id.button_choose);
        imageFromPhone = (ImageView) findViewById(R.id.image_from_phone);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.add));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri == null){
                    Snackbar.make(view, "You must select a photo from phone", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);

                    StorageReference filePath = mStorage.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(uri));

                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri aURI = taskSnapshot.getDownloadUrl();
                            String temp = aURI.toString();
                            Upload upload = new Upload(temp);
                            String uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String key = mDatabase.push().getKey();
                            mDatabase.child(DATABASE_PATH_UPLOADS_ROOT).child(uploadId).child(DATABASE_PATH_UPLOADS).child(key).setValue(upload);
                            mDatabaseAddMainPhotos.child(DATABASE_PATH_UPLOADS).child(key).setValue(upload);

                            Toast.makeText(MediaActivity.this, "Upload Successful",
                                    Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MediaActivity.this, "Upload Failed",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Firebase","Upload Image");
                            onBackPressed();
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            uri = data.getData();
            String[]projection={MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            if(filePath != null){
                try{
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    Drawable drawable = new BitmapDrawable(selectedImage);
                    imageFromPhone.setImageDrawable(drawable);
                    Log.d("Image", "Figure this out");
                }catch (OutOfMemoryError e){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    imageFromPhone.setImageDrawable(drawable);
                }
            }

    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

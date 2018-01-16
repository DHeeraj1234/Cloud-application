package com.example.deerajreddy.app;

/**
 * Created by DEERAJREDDY on 22-02-2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Welcome extends AppCompatActivity {
    private static final String TAG = "Welcome";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 22113;
    // private final String  TAG=this.getClass().getName();
    Drawable drawable;
    Uri fileUri = null;
    //  Uri photoUri = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://myapp-d7068.appspot.com");
    ImageView ivCamera, ivGallery, ivUpload, ivImage,ivDownload,ivView;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    TextView tv;
    String selectedPhoto = null;
    final int CAMERA_PIC_REQUEST = 22113;
    final int GALLERY_REQUEST = 22113;
    private int PICK_IMAGE_REQUEST = 1;
    Button b = null;
    ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
    private DatabaseReference databaseReference;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                Uri photoUri = null;

                if (data == null) {

                    Toast.makeText(this, "Image saved successfully",
                            Toast.LENGTH_LONG).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(this, "Image saved successfully in: " + data.getData(),
                            Toast.LENGTH_LONG).show();
                }
                showPhoto(photoUri);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Callout for image capture failed!",
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ImageView imageView = (ImageView) findViewById(R.id.ivImage);

                    imageView.setImageBitmap(bitmap);
                    imageView.invalidate();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }


    private File getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM) + File.separator + ".thumbnails");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.UK).format(new Date());
        return new File(directory.getPath() + File.separator + "IMG_"
                + timeStamp + ".jpg");
    }
    private File getOutputPhotoFile1() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM) + File.separator + ".thumbnails");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.UK).format(new Date());
        return new File(directory.getPath() + File.separator + "IMG_"
                + timeStamp + ".jpg");
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);


        tv = (TextView) findViewById(R.id.textView);
        b = (Button) findViewById(R.id.button2);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());


        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
       // ivDownload = (ImageView) findViewById(R.id.ivDownload);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivView=(ImageView)findViewById(R.id.ivView);
        databaseReference= FirebaseDatabase.getInstance().getReference();

        if (!hasCamera()) {
            ivCamera.setEnabled(false);
        }

        ivCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                   /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Toast.makeText(getApplicationContext(),"Something went wrong while taking photos",Toast.LENGTH_SHORT).show();
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }
                Toast.makeText(getApplicationContext(),"Something went wrong while taking ",Toast.LENGTH_SHORT).show();
                    //
                //}catch(IOException e){
                   // Toast.makeText(getApplicationContext(),"Something went wrong while taking photos",Toast.LENGTH_SHORT).show();
                //}
                */

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getOutputPhotoFile();
                fileUri = Uri.fromFile(getOutputPhotoFile());
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ);
            }
        });
        ivGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                //startActivityForResult(galleryPhoto.openGalleryIntent(),GALLERY_REQUEST);
            }
        });
        ivView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(Welcome.this,RecycleMain.class);
                startActivity(i);
            }
        });
        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
                /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date now = new Date();
                String fileName = formatter.format(now) + "myupload.jpg";
                */
                SimpleDateFormat formatter = new SimpleDateFormat("mm_ss");
                Date now = new Date();
                String fileName = formatter.format(now);
                StorageReference myfileRef = storageRef.child(fileName);
               // RecycleMain m=new RecycleMain();
             //   m.onStart();
                ivImage.setDrawingCacheEnabled(true);
                ivImage.buildDrawingCache();
                Bitmap bitmap = ivImage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] data = baos.toByteArray();
                UploadTask uploadTask = myfileRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(Welcome.this, "TASK FAILED", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Welcome.this, "IMAGE UPLOADED", Toast.LENGTH_SHORT).show();
                        @SuppressWarnings("VisibleForTests")Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String DOWNLOAD_URL = downloadUrl.toString();
                       // String D="https://firebasestorage.googleapis.com";
                        //String DURL=D+DOWNLOAD_URL;
                        AuthData ad = null;
                        SimpleDateFormat formatter = new SimpleDateFormat("mm_ss");
                        Date now = new Date();
                        String fileName = formatter.format(now);
                        FirebaseAuth mAuth=null;
                        String user_id=mAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child(user_id).child(fileName).child("image").setValue(DOWNLOAD_URL);
                        ModelClass m=new ModelClass();
                        m.setImage(DOWNLOAD_URL);
                       // databaseReference.child(fileName).setValue(user_id);
                        Log.v("DOWNLOAD URL", DOWNLOAD_URL);
                        Toast.makeText(Welcome.this, DOWNLOAD_URL, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
      /*  ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
               // StorageReference storageRef = storage.getReferenceFromUrl("gs://myapp-d7068.appspot.com");
                StorageReference storageRef = storage.getReferenceFromUrl("gs://myapp-d7068.appspot.com").child("2017_03_05_21_31_02myupload.jpg");

                final File localFile = new File(Environment
                        .getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)+ File.separator
                        +"img.png");

                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                        ivImage.setImageBitmap(bitmap);
                        Toast.makeText(Welcome.this, "TASK SUCCEEDED", Toast.LENGTH_SHORT).show();
                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(localFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, fOut);
                        galleryAddPic(localFile);
                        /*MediaScannerConnection.scanFile(Welcome.this,
                                new String[] { localFile.toString() }, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {

                                        Toast.makeText(Welcome.this, "VISIBLE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception exception) {
                        Log.e("firebase ",";local tem file not created  created " +exception.toString());
                        Toast.makeText(Welcome.this, "TASK Not SUCCEEDED", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            });*/
    }

    public void galleryAddPic(File currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(currentPhotoPath);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    public void sh(View view) {
        String msg = "world";
        tv.setText(msg);
    }

    public void logout(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        finish();
    }

    public boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void showPhoto(Uri photoUri) {
        File imageFile = new File(String.valueOf(photoUri));
        if (imageFile.exists()) {
            Drawable oldDrawable = ivImage.getDrawable();
            if (oldDrawable != null) {
                ((BitmapDrawable) oldDrawable).getBitmap().recycle();
            }
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            ivImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivImage.setImageDrawable(drawable);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   /* public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Welcome Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }*/
}


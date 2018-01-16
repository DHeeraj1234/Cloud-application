package com.example.deerajreddy.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

public class RecycleMain extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView mBlogList;
    Firebase mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rmain);

        //Recycler View
         mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        // Send a Query to the database
        FirebaseAuth mAuth=null;
        String user_id=mAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user_id);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelClass, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ModelClass, BlogViewHolder>(
                        ModelClass.class,
                        R.layout.design_row,
                        BlogViewHolder.class,
                        myRef)  {



                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, ModelClass model,int position) {
                       // viewHolder.setTitle(model.getTitle());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                       final String key=getRef(position).getKey();
                        FirebaseAuth mAuth = null;
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String user_id = mAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference myRef = database.getReference(user_id);
                        //Toast.makeText ( RecycleMain.this, (CharSequence) myRef, Toast.LENGTH_SHORT).show();
                        viewHolder.mView.setOnClickListener(new  View.OnClickListener() {
                                                                public void onClick(View view) {
                                                                    // mAuth=database.getReference(user_id);
                                                                    FirebaseAuth mAuth = null;
                                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                    String user_id = mAuth.getInstance().getCurrentUser().getUid();
                                                                    DatabaseReference myRef = database.getReference(user_id).child(key);
                                                                    //mref=new Firebase("https://myapp-d7068.firebaseio.com/").child(user_id).child("image");

                                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                                                      //  private DataSnapshot dataSnapshot;

                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                         //   this.dataSnapshot = dataSnapshot;
                                                                            //String name=dataSnapshot.getValue(String.class);
                                                                            // Toast.makeText ( v.getContext(),"hello2", Toast.LENGTH_SHORT).show();
                                                                           // String name1=dataSnapshot.child(key).getValue(String.class);

                                                                                 FirebaseAuth mAuth = null;
                                                                                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                                 String user_id = mAuth.getInstance().getCurrentUser().getUid();

                                                                                 DatabaseReference myRef = database.getReference(user_id);
                                                                                 //  ModelClass template = data.getValue(ModelClass.class);
                                                                                 // use this object and store it into an ArrayList<Template> to use it further
                                                                                 //  String author =         dataSnapshot.child(user_id).child(key).child("image").getValue().toString();
                                                                                // ModelClass key = dataSnapshot.getValue(ModelClass.class);

                                                                              //  DataSnapshot d= (DataSnapshot) dataSnapshot.getChildren();


                                                                                 ModelClass data = dataSnapshot.getValue(ModelClass.class);
                                                                                 //Log.i("value is:", String.valueOf(data.getImage()));
                                                                               //  Toast.makeText(RecycleMain.this, "name", Toast.LENGTH_SHORT).show();
                                                                            String url= String.valueOf(data.getImage());
                                                                            String url1=url.substring(70,75);
                                                                            Toast.makeText ( RecycleMain.this,url1, Toast.LENGTH_SHORT).show();

                                                                            //download:
                                                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                                                            // StorageReference storageRef = storage.getReferenceFromUrl("gs://myapp-d7068.appspot.com");
                                                                            StorageReference storageRef = storage.getReferenceFromUrl("gs://myapp-d7068.appspot.com").child(url1);

                                                                            SimpleDateFormat formatter = new SimpleDateFormat("mm_ss");
                                                                            Date now = new Date();
                                                                            String fileName = formatter.format(now);
                                                                            final File localFile = new File(Environment
                                                                                    .getExternalStoragePublicDirectory(
                                                                                            Environment.DIRECTORY_PICTURES)+ File.separator
                                                                                    +fileName+"img.png");

                                                                            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                                                                   // ivImage.setImageBitmap(bitmap);
                                                                                    Toast.makeText(RecycleMain.this, "IMAGE DOWNLOADED", Toast.LENGTH_SHORT).show();
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
                    */
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure( Exception exception) {
                                                                                    Log.e("firebase ",";local tem file not created  " +exception.toString());
                                                                                    Toast.makeText(RecycleMain.this, "TASK Not SUCCEEDED", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                            //download.
                                                                        }


                                                                        public void onCancelled(DatabaseError databaseError) {
                                                                            //  Toast.makeText ( v.getContext(),"hello1", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                }
                                                            });

                        //final String k= myRef.child(user_id).child(key).child("image").getKey();
                       // DataSnapshot ds=null;

                     //   Toast.makeText(RecycleMain.this, (CharSequence) ds.getValue(ModelClass.class), Toast.LENGTH_SHORT).show();
                       // System.out.println("key:"+key);
                       /* viewHolder.mView.setOnClickListener(new  View.OnClickListener(){
                            public void onClick(View view){
                               // Toast.makeText(RecycleMain.this,key, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(RecycleMain.this,k, Toast.LENGTH_SHORT).show();
                            }
                        });
                    */
                    }

                };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }
    public void galleryAddPic(File currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(currentPhotoPath);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    //View Holder For Recycler View
    public static class BlogViewHolder extends RecyclerView.ViewHolder  {
        View mView;
        ArrayList<ModelClass> dataItems;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v) {
                   // ModelClass m=(ModelClass) this.getItem(position);

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.androidsquad.space/"));
                    Intent browserChooserIntent = Intent.createChooser(browserIntent , "Choose browser of your choice");
                    v.getContext().startActivity(browserChooserIntent);
                    int pos = getAdapterPosition();
                    FirebaseAuth mAuth = null;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String user_id = mAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference myRef = database.getReference(user_id);
                  /*  myRef.addValueEventListener(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                                String name=dataSnapshot.child("image").getValue(String.class);
                                                                 //Toast.makeText(v.getContext(), "You clicked " , Toast.LENGTH_SHORT).show();
                                                             }

                                                             @Override
                                                             public void onCancelled(DatabaseError databaseError) {

                                                             }
                                                         });
*/
                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
//                        ModelClass clickedDataItem = dataItems.get(pos);
                        Toast.makeText(v.getContext(), "You clicked " +pos, Toast.LENGTH_SHORT).show();
                    }

                }
            });
            /******** For More Android Tutorials .. Download "Master Android" Application From Play Store Free********/
        }
        /*public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.titleText);
            post_title.setText(title);
        }
        */

        public void setImage(Context ctx , String image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.imageViewy);
            // We Need TO pass Context
            Picasso.with(ctx).load(image).into(post_image);
        }    }

    }
////////////////////////*******************************************////////////////////////////



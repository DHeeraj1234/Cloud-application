package com.example.deerajreddy.app.mFireBase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.deerajreddy.app.mData.Data;
import com.example.deerajreddy.app.mRecycler.MyAdapter;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

/**
 * Created by DEERAJREDDY on 12-03-2017.
 */

public class FireBaseClient {
    Context c;
    String DB_URL;
    RecyclerView rv;
    Firebase fv;
    ArrayList<Data> datas=new ArrayList<>();
    MyAdapter adapter;
    public FireBaseClient(Context c,String DB_URL,RecyclerView rv){
        this.c=c;
        this.DB_URL=DB_URL;
        this.rv=rv;
        Firebase.setAndroidContext(c);
        fv=new Firebase(DB_URL);
    }
      public void saveOnline(String name,String url){
          Data d=new Data();
          d.setName(name);
            d.setUrl(url);
          fv.child("data").push().setValue(d);
      }
    public void refreshData(){
        fv.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUpdates(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void getUpdates(DataSnapshot dataSnapshot){
        datas.clear();
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            Data d=new Data();
            d.setName(ds.getValue(Data.class).getName());
            d.setUrl(ds.getValue(Data.class).getName());
            datas.add(d);
        }
        if(datas.size()>0){
               adapter=new MyAdapter(c,datas);
            rv.setAdapter(adapter);
        }
        else{
            Toast.makeText(c,"No data",Toast.LENGTH_SHORT).show();
        }
    }
}

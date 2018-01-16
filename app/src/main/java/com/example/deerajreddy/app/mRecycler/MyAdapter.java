package com.example.deerajreddy.app.mRecycler;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.deerajreddy.app.R;
import com.example.deerajreddy.app.mData.Data;
import com.example.deerajreddy.app.mPicasso.PicassoClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DEERAJREDDY on 12-03-2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    Context c;
    ArrayList<Data> datas;
    public MyAdapter(Context c,ArrayList<Data> datas){
        this.c=c;
        this.datas=datas;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
       // holder.nametxt.setText(datas.get(position).getName());
        PicassoClient.downloadImage(c,datas.get(position).getUrl(),holder.img);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }
}

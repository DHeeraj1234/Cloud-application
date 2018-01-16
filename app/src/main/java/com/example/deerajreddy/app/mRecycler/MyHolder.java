package com.example.deerajreddy.app.mRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deerajreddy.app.R;

/**
 * Created by DEERAJREDDY on 12-03-2017.
 */

public class MyHolder extends RecyclerView.ViewHolder {
    TextView nameTxt;
    ImageView img;
    public MyHolder(View itemView) {
        super(itemView);
        //nameTxt=(TextView)itemView.findViewById(R.id.nameTxt);
    }
}

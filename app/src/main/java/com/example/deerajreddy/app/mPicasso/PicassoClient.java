package com.example.deerajreddy.app.mPicasso;

import android.content.Context;
import android.widget.ImageView;

import com.example.deerajreddy.app.R;
import com.squareup.picasso.Picasso;

/**
 * Created by DEERAJREDDY on 12-03-2017.
 */

public class PicassoClient {
    public static void downloadImage(Context c,String url,ImageView img){
        if(url!=null&&url.length()>0){
            Picasso.with(c).load(url).into(img);
        }
    }
}

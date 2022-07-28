package com.example.birdgametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bullet {
   public static int x,y,width,heidth;
    Bitmap bullet;
    Bullet(Resources res){
        bullet= BitmapFactory.decodeResource(res,R.drawable.bullet);
         width=bullet.getWidth();
         heidth=bullet.getHeight();
        width/=4;
        heidth/=4;

        bullet=Bitmap.createScaledBitmap(bullet,width,heidth,false);
    }
    Rect getCollisionShape(){
        return new Rect(x,y,x+width,y+heidth);
    }
}

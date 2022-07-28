package com.example.birdgametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bird {
    public int speed=20;
    public boolean wasShot=true;
    int x=0,y,width,heidth,birdCounter=1;
    Bitmap bird1,bird2,bird3,bird4;

    public Bird(Resources res) {
        bird1= BitmapFactory.decodeResource(res,R.drawable.bird1);
        bird2= BitmapFactory.decodeResource(res,R.drawable.bird2);
        bird3= BitmapFactory.decodeResource(res,R.drawable.bird3);
        bird4= BitmapFactory.decodeResource(res,R.drawable.bird4);


        width=bird1.getWidth();
        heidth=bird1.getHeight();


        width/=6;
        heidth/=6;


        bird1=Bitmap.createScaledBitmap(bird1,width,heidth,false);
        bird2=Bitmap.createScaledBitmap(bird2,width,heidth,false);
        bird3=Bitmap.createScaledBitmap(bird3,width,heidth,false);
        bird4=Bitmap.createScaledBitmap(bird4,width,heidth,false);

        y=-heidth;
    }
    Bitmap getBird(){
        if(birdCounter==1){
            birdCounter++;
            return bird1;
        }
        if(birdCounter==2){
            birdCounter++;
            return bird2;
        } if(birdCounter==3){
            birdCounter++;
            return bird3;
        }
        birdCounter=1;
        return bird4;
    }
    Rect getCollisionShape(){
        return new Rect(x,y,x+width,y+heidth);
    }
}

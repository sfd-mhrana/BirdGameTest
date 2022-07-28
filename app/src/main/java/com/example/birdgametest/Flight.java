package com.example.birdgametest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.birdgametest.GameView.screenRatioX;

public class Flight {
    public boolean isGointUp=false;
    int x=0,y=0,width,height,wingcounter=0,toShoot=0,
    shootCounter=1;
    Bitmap flight1,flight2,shoot1,shoot2,shoot3,shoot4,shoot5,dead;
    private GameView gameView;
    Flight(GameView gameView,int screenY, Resources res){

        this.gameView=gameView;
        flight1= BitmapFactory.decodeResource(res,R.drawable.fly1);
        flight2= BitmapFactory.decodeResource(res,R.drawable.fly2);

        shoot1= BitmapFactory.decodeResource(res,R.drawable.shoot1);
        shoot2= BitmapFactory.decodeResource(res,R.drawable.shoot2);
        shoot3= BitmapFactory.decodeResource(res,R.drawable.shoot3);
        shoot4= BitmapFactory.decodeResource(res,R.drawable.shoot4);
        shoot5= BitmapFactory.decodeResource(res,R.drawable.shoot5);

        dead= BitmapFactory.decodeResource(res,R.drawable.dead);

        width=flight1.getWidth();
       height=flight1.getHeight();

       width/=4;
       height/=4;

         flight1=Bitmap.createScaledBitmap(flight1,width,height,false);
         flight2=Bitmap.createScaledBitmap(flight2,width,height,false);

        shoot1=Bitmap.createScaledBitmap(shoot1,width,height,false);
        shoot2=Bitmap.createScaledBitmap(shoot2,width,height,false);
        shoot3=Bitmap.createScaledBitmap(shoot3,width,height,false);
        shoot4=Bitmap.createScaledBitmap(shoot4,width,height,false);
        shoot5=Bitmap.createScaledBitmap(shoot5,width,height,false);

        dead=Bitmap.createScaledBitmap(dead,width,height,false);
        y=screenY/2;
        x= (int) (64*screenRatioX);
    }
    Bitmap getFlight(){
        if(toShoot!=0){
            if(shootCounter==1){
            shootCounter++;
            return shoot1;
            }if(shootCounter==2){
                shootCounter++;
                return shoot2;
            }if(shootCounter==3){
                shootCounter++;
                return shoot3;
            }if(shootCounter==4){
                shootCounter++;
                return shoot4;
            }
            shootCounter=1;
            gameView.newBullet();
            toShoot--;
            return shoot5;
        }
        if(wingcounter==0){
            wingcounter++;
            return flight1;
        }else{
        wingcounter--;
        return flight2;}
    }
    Rect getCollisionShape(){
        return new Rect(x,y,x+width,y+height);
    }
    Bitmap getDead(){
        return dead;
    }
}

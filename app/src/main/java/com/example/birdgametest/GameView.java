package com.example.birdgametest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    boolean isGameover=false;
    private Thread thread;
    private boolean isPlaing;
    public int screenX,screenY;
    private static int score=0;
    private Paint paint;
    private int sound;
    private Background background1;
    private Background background2;
    private Flight flight;
    private List<Bullet> bullets;
    private Bird[] birds;
    private Random random;
    private SharedPreferences shared;
    private SoundPool soundPool;
    Bird bird;
    public static float screenRatioX,screenRationY;
    private GameActivity activity;
    public GameView(GameActivity activity,int screenX,int screenY) {
        super(activity);
        this.activity=activity;
        this.screenX=screenX;
        this.screenY=screenY;
        flight=new Flight(this,screenY,getResources());
        screenRatioX=1920f/screenX;
        screenRationY=1920f/screenY;
        background1= new Background(screenX,screenY,getResources());
        background2= new Background(screenX,screenY,getResources());
         background2.x=screenX;
         paint=new Paint();
         paint.setTextSize(128);
         paint.setColor(Color.WHITE);
         random=new Random();
         shared=activity.getSharedPreferences("Game",Context.MODE_PRIVATE);
         bullets=new ArrayList<>();
         birds=new Bird[4];
        for (int i = 0; i < 4; i++) {
           bird=new Bird(getResources());
           birds[i]=bird;
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            AudioAttributes audioAttributes=new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            soundPool =new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        }else{
            soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }
        sound=soundPool.load(activity,R.raw.shoot,1);
    }

    @Override
    public void run() {
        while (isPlaing){
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        background1.x-=10*screenRatioX;
        background2.x-=10*screenRatioX;
        if(background1.x+background1.background.getWidth()<0){
            background1.x=screenX;
        }
        if(background2.x+background2.background.getWidth()<0){
            background2.x=screenX;
        }
        if(flight.isGointUp){
            flight.y-=30*screenRationY;
        }else{
            flight.y+=30*screenRationY;
        }
        if(flight.y<0){
            flight.y=0;
        }
        if(flight.y>screenY-flight.height){
            flight.y=screenY-flight.height;
        }
        List<Bullet> trash=new ArrayList<>();
        for(Bullet bullet:bullets){
            if(bullet.x>screenX){
                trash.add(bullet);
            }
            bullet.x += 40*screenRatioX;
            for(Bird bird:birds){
                if(Rect.intersects(bird.getCollisionShape(),bullet.getCollisionShape())){
                   score++;
                   bird.x=screenX+500;
                   bullet.x=screenX+500;
                   bird.wasShot=true;
                }
            }
        }
        for(Bullet bullet:trash){
            bullets.remove(bullet);
        }
        for(Bird bird:birds){
                bird.x -= bird.speed;
                if(bird.x + bird.width < 0){
                    if(!bird.wasShot){
                        isGameover=true;
                        return;
                    }
                    int bound = (int) (30*screenRatioX);
                    bird.speed=5;
                    if(bird.speed< 10*screenRatioX ){
                           bird.speed= (int) (10*screenRatioX);
                    }
                    bird.x = screenX;
                    bird.y=random.nextInt(screenY-bird.heidth);
                    bird.wasShot=false;
                }
                 if(Rect.intersects(bird.getCollisionShape(),flight.getCollisionShape())){
                     isGameover= true;
                     return;
                 }
        }
    }
    private void draw() {
        if(getHolder().getSurface().isValid()){
            Canvas canvas=getHolder().lockCanvas();
            canvas.drawBitmap(background1.background,background1.x,background1.y,paint);
            canvas.drawBitmap(background2.background,background2.x,background2.y,paint);
            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);
            canvas.drawText(" "+score,screenX/2f,164,paint);
            if(isGameover){
               isPlaing=false;
               canvas.drawBitmap(flight.getDead(),flight.x,flight.y,paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
               waitBeforeExiting();
               return;
           }
            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);
           for(Bullet bullet:bullets){
               canvas.drawBitmap(bullet.bullet,bullet.x,bullet.y,paint);
           }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity,MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {
        if(shared.getInt("highScore",0)<score){
            SharedPreferences.Editor editor=shared.edit();
            editor.putInt("highScore",score);
            editor.apply();
        }

    }

    private void sleep() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaing=true;
        thread=new Thread(this);
        thread.start();
    }
    public void pause(){
        try {
            isPlaing=false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
               if(event.getX()<screenX/2){
                   flight.isGointUp=true;
               }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGointUp=false;
                if(event.getX()>screenX/2){
                    flight.toShoot++;
                }
                break;
        }
        return true;
    }

    public void newBullet() {
      if(!shared.getBoolean("isMute",false))
      {
          soundPool.play(sound,1,1,0,1,1);
      }
      Bullet bullet=new Bullet(getResources());
      bullet.x=flight.x+flight.width;
      bullet.y=flight.y+(flight.height/2);
      bullets.add(bullet);
    }
}

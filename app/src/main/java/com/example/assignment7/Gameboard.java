package com.example.assignment7;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Vector;

public class Gameboard extends AppCompatActivity implements View.OnTouchListener{
    float screenWidth;
    float screenHeight;
    float centerX;
    float centerY;
    Vector<Tile> gameboard = new Vector<>();
    GameView gameView;
    Tile selectedTile = null;
    int dieRoll = 3; //HARDCODED, THIS WILL CHANGE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        gameView.setOnTouchListener(this);
        setContentView(gameView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        centerX = screenWidth/2;
        centerY = screenHeight/2;
        initBoard();
    }

    public void initBoard(){
        //create the tiles
        Tile tile1 = new Tile(centerX, centerY+346);
        Tile tile2 = new Tile(centerX+150, centerY+261);
        Tile tile3 = new Tile(centerX+300, centerY+173);
        Tile tile4 = new Tile(centerX+300, centerY);
        Tile tile5 = new Tile(centerX+300, centerY-173);
        Tile tile6 = new Tile(centerX+150, centerY-261);
        Tile tile7 = new Tile(centerX, centerY-346);
        Tile tile8 = new Tile(centerX-150, centerY-261);
        Tile tile9 = new Tile(centerX-300, centerY-173);
        Tile tile10 = new Tile(centerX-300, centerY);
        Tile tile11 = new Tile(centerX-300, centerY+173);
        Tile tile12 = new Tile(centerX-150, centerY+261);

        //Home spaces
        Tile tile13 = new Tile(centerX, centerY+173);
        Tile tile14 = new Tile(centerX+150, centerY-87);
        Tile tile15 = new Tile(centerX-150, centerY-87);

        //Add the neighbors
        tile1.addNeighbor(tile12);
        tile2.addNeighbor(tile1);
        tile2.addNeighbor(tile13);
        tile3.addNeighbor(tile2);
        tile4.addNeighbor(tile3);
        tile5.addNeighbor(tile4);
        tile6.addNeighbor(tile5);
        tile6.addNeighbor(tile14);
        tile7.addNeighbor(tile6);
        tile8.addNeighbor(tile7);
        tile9.addNeighbor(tile8);
        tile10.addNeighbor(tile9);
        tile10.addNeighbor(tile15);
        tile11.addNeighbor(tile10);
        tile12.addNeighbor(tile11);


        gameboard.add(tile1);
        gameboard.add(tile2);
        gameboard.add(tile3);
        gameboard.add(tile4);
        gameboard.add(tile5);
        gameboard.add(tile6);
        gameboard.add(tile7);
        gameboard.add(tile8);
        gameboard.add(tile9);
        gameboard.add(tile10);
        gameboard.add(tile11);
        gameboard.add(tile12);
        gameboard.add(tile13);
        gameboard.add(tile14);
        gameboard.add(tile15);


        //TESTING: place a cloud token on space 1
        tile1.tileOccupant = Tile.token.cloud;
        tile5.tileOccupant = Tile.token.tree;
        tile9.tileOccupant = Tile.token.sun;

    }

    class GameView extends SurfaceView implements Runnable{
        Thread gameThread = null;
        Canvas canvas;
        Paint paint;
        SurfaceHolder ourHolder;
        boolean running = true;

        public GameView(Context context){
            super(context);
            ourHolder = getHolder();
            paint = new Paint();

        }


        public void update(){
            //
        }
        public void draw(){
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();

                //gameboard.forEach((n)->n.draw(canvas, paint));
                for(int i = 0; i < gameboard.size(); i++){
                    gameboard.elementAt(i).draw(canvas, paint);
                }

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
        @Override
        public void run(){
            while (running){
                update();
                draw();

                try{
                    gameThread.sleep(20);

                }catch (InterruptedException e){

                }

            }
        }

        public void resume(){
            gameThread = new Thread(this);
            gameThread.start();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        gameView.resume();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Vector<Tile> targetTile = new Vector<>();//Used for finding the target tiles

        int closestIndex = -1;
        double closestDistance = 100;
        for(int i = 0; i < gameboard.size(); i++){
            double newDistance = gameboard.elementAt(i).distanceTo(x,y);
            if(newDistance <= closestDistance || closestDistance == -1){
                closestDistance = newDistance;
                closestIndex = i;
            }
        }
        if(closestIndex != -1) {
            if (gameboard.elementAt(closestIndex).tileStatus == Tile.status.target && selectedTile.tileOccupant != Tile.token.none) {
                gameboard.elementAt(closestIndex).tileOccupant = selectedTile.tileOccupant;
                selectedTile.tileOccupant = Tile.token.none;
            }
        }
        //Color the tiles correctly
        for (int i = 0; i < gameboard.size(); i++) {
            if (i == closestIndex) {
                gameboard.elementAt(i).tileStatus = Tile.status.selected;
                targetTile.add(gameboard.elementAt(i));
                selectedTile = gameboard.elementAt(i);
            } else {
                gameboard.elementAt(i).tileStatus = Tile.status.none;
            }
        }

        //Find tiles the correct number of spaces away
        for (int iter = 0; iter < dieRoll; iter++) {
            Vector<Tile> tempVector = new Vector<>();
            for (int i = 0; i < targetTile.size(); i++) {
                tempVector.addAll(targetTile.elementAt(i).availableSpaces);
            }
            targetTile = tempVector;
        }
        for (int i = 0; i < targetTile.size(); i++) {
            targetTile.elementAt(i).tileStatus = Tile.status.target;
        }



        return true;
    }

}

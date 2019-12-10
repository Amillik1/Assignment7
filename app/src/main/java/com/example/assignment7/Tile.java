package com.example.assignment7;

public class Tile {
    boolean selected = false;
    float centerX;
    float centerY;
    enum token{none, tree, cloud, sun}
    token tileOccupant = token.none;


    public Tile(float X, float Y){
        centerX = X;
        centerY = Y;
    }

    public void draw(){
        //draw hex

        if(tileOccupant == token.cloud){
            //draw a little cloud symbol
        }else if(tileOccupant == token.sun){
            //draw a little sun symbol
        }else if(tileOccupant == token.tree){
            //draw a little tree
        }


    }
}

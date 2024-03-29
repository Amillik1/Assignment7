package com.example.assignment7;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Vector;

public class Tile {
    boolean selected = false;
    float centerX;
    float centerY;
    enum status{none, selected, target}
    status tileStatus = status.none;
    enum token{none, tree, cloud, sun}
    token tileOccupant = token.none;
    Vector<Tile> availableSpaces = new Vector<>();


    public Tile(float X, float Y){
        centerX = X;
        centerY = Y;
    }
    public Tile(float X, float Y, Vector<Tile> spaces){
        centerX = X;
        centerY = Y;
        availableSpaces = spaces;

    }

    public String printTile(){
        return Float.toString(centerX) + " " + Float.toString(centerY);
    }

    public void draw(Canvas canvas, Paint paint){
        //draw hex
        float[] vert = {centerX, centerY, centerX+50, centerY-87, centerX-50, centerY-87, centerX-100, centerY, centerX-50, centerY+87, centerX+50, centerY+87,centerX+100, centerY, centerX+50, centerY-87};
        int[] colorsSelected = {Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW};
        int[] colorsNormal = {Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY};
        int[] colorsTarget = {Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED};
        //canvas mode is triangle_fan, hex needs 7 points
        if(tileOccupant == token.cloud){
            colorsSelected[0] = Color.BLUE;
            colorsNormal[0] = Color.BLUE;
            colorsTarget[0] = Color.BLUE;
        }else if(tileOccupant == token.tree){
            colorsSelected[0] = Color.GREEN;
            colorsNormal[0] = Color.GREEN;
            colorsTarget[0] = Color.GREEN;
        }else if(tileOccupant == token.sun){
            //COLOR = ORANGE
            colorsSelected[0] = Color.rgb(255,165,0);
            colorsNormal[0] = Color.rgb(255,165,0);
            colorsTarget[0] = Color.rgb(255,165,0);
        }

        if (tileStatus == status.none){
            canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, vert.length, vert, 0, null, 0, colorsNormal, 0, null, 0, 0, paint);
        }
        if (tileStatus == status.selected) {
            canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, vert.length, vert, 0, null, 0, colorsSelected, 0, null, 0, 0, paint);
        }
        if (tileStatus == status.target){
            canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, vert.length, vert, 0, null, 0, colorsTarget, 0, null, 0, 0, paint);
        }


    }

    public void addNeighbor(Tile space){
        availableSpaces.add(space);
    }

    public double distanceTo(float x, float y){
        return Math.hypot(Math.abs(x-centerX), Math.abs(y-centerY));
    }
}

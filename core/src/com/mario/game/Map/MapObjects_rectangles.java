package com.mario.game.Map;

import com.badlogic.gdx.graphics.glutils.FacedCubemapData;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;

import java.util.Iterator;

public class MapObjects_rectangles {
    MapObject object;
    float rectangle[];
    public boolean coin;
    public boolean loopCoin;
    public boolean mursh;
    public boolean health;
    public boolean empty;

    MapObjects_rectangles (MapObject a, float [] b){
        object = a;
        rectangle = new float[8];
        for (int i = 0; i < 8; ++i){
            rectangle[i] = b[i];
        }

        coin = true;
        loopCoin = false;
        mursh = false;
        health = false;
        empty = false;


    }

    public void get_view(){
        if (object.getName() == null) return;
        switch (Integer.parseInt( object.getName())){
            case 1:
                coin = true;
                break;
            case 2:
                coin = false;
                loopCoin = true;
                break;
            case 3:
                mursh = true;
                coin = false;
                break;
            case 4:
                health = true;
                coin = false;
                break;
        }
    }


}

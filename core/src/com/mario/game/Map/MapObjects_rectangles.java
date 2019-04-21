package com.mario.game.Map;

import com.badlogic.gdx.graphics.glutils.FacedCubemapData;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;

public class MapObjects_rectangles {
    MapObject object;
    float rectangle[];
    boolean hit;

    MapObjects_rectangles (MapObject a, float [] b){
        object = a;
        hit = false;
        rectangle = new float[8];
        for (int i = 0; i < 8; ++i){
            rectangle[i] = b[i];
        }}
}

package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

public class Pipes extends MapObject_{

    Pipes(Map ma, OrthographicCamera cam){
        camera = cam;
        map = ma;
        objects = map.tiledMap.getLayers().get("pipes").getObjects();
        length = objects.getCount();
        rectangle_object = new float[8];
        rectangle_objects = new float[length][8];
        temporary = new Vector2(0,0);
        set = new HashSet<Vector2>();
        temporary_arr = new Vector2[2];
        temporary_arr[0] = new Vector2();
        temporary_arr[1] = new Vector2(0,0);

        get_rectangles();
    }
}

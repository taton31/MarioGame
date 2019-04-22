package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mario.game.creatures.Mario.Mario;

import java.util.HashSet;

public class Pipes extends MapObject_{
    boolean eggs;

    Pipes(Map ma, OrthographicCamera cam, Mario mar){
        super(ma, cam, mar);
        objects = map.tiledMap.getLayers().get("pipes").getObjects();
        length = objects.getCount();
        rectangle_object = new float[8];
        temporary = new Vector2(0,0);
        set = new HashSet<Vector2>();
        temporary_arr = new Vector2[2];
        temporary_arr[0] = new Vector2();
        temporary_arr[1] = new Vector2(0,0);
        eggs = true;

        mapObjects = new Array<MapObjects_rectangles>();
        get_mapObjects_rectangle();
    }

    public HashSet<Vector2> collisium (float [] rectangle , boolean check_all_world){
        set.clear();
        temporary_arr[0].set(0,0);
        temporary_arr[1].set(0,0);

        for (int i = 0 ;i < mapObjects.size; i++){
            if (!check_all_world &&  !check_camera(mapObjects.get(i).rectangle)) continue;

            temporary.set(map.collisium(mapObjects.get(i).rectangle, rectangle));

            if (temporary.epsilonEquals(0,0)) continue;
            go_down(temporary);
            if (set.isEmpty()) {
                set.add(temporary_arr[0].set(temporary));
                continue;
            }

            if (has_collinear(temporary)) continue;

            if (temporary_arr[1].epsilonEquals(0,0)) set.add(temporary_arr[1].set(temporary));
            else {
                set.add(temporary.cpy());
                break;
            }

        }
        return set;
    }

    boolean go_down(Vector2 vec){
        if (vec.x == 0 && vec.y > 0 && mario.getPressButtonDown() && eggs){
            //код для узода в тайную комнату

        }
        return true;
    }
}

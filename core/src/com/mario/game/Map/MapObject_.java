package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mario.game.creatures.Mario.Mario;

import java.util.HashSet;

public abstract class MapObject_ {
    MapObjects objects;
    protected final Map map;
    final OrthographicCamera camera;
    Mario mario;

    Array <MapObjects_rectangles> mapObjects;
    float[] rectangle_object;
    int length;
    int j;
    Vector2 temporary;
    protected HashSet<Vector2> set;
    Vector2[] temporary_arr;

    MapObject_(Map ma, OrthographicCamera cam, Mario mar){
        map = ma;
        mario = mar;
        camera = cam;
    }

    public HashSet<Vector2> collisium (float [] rectangle , boolean check_all_world){
        set.clear();
        temporary_arr[0].set(0,0);
        temporary_arr[1].set(0,0);

        for (int i = 0 ;i < mapObjects.size; i++){
            if (!check_all_world &&  !check_camera(mapObjects.get(i).rectangle)) continue;

            temporary.set(map.collisium(mapObjects.get(i).rectangle, rectangle));

            if (temporary.epsilonEquals(0,0)) continue;

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


    protected void get_mapObjects_rectangle() {
        for (MapObject cell : objects) {
            map.get_rectangle(cell, rectangle_object);
            mapObjects.add(new MapObjects_rectangles(cell, rectangle_object));
        }
    }

    protected boolean check_camera (float [] a){
        if ( a[0] < camera.position.x && a[2] > camera.position.x ) return true;
        for (int k = 0; k < (a.length / 2); ++k){
            if ( a[2 * k] > camera.position.x - Gdx.app.getGraphics().getWidth() / 2 && a[2 * k] < camera.position.x + Gdx.app.getGraphics().getWidth() / 2) return true;
        }
        return false;
    }

    protected boolean has_collinear (Vector2 temp){
        for (Vector2 vec : set){
            if (vec.isCollinear(temp)) {
                return true;
            }
        }
        return false;
    }

    protected boolean choose_crash (int k){
        return ((mario.getX() + mario.getWidth() / 2f > mapObjects.get(k).rectangle[0]) && (mario.getX() + mario.getWidth() / 2f < mapObjects.get(k).rectangle[2]));
    }


}

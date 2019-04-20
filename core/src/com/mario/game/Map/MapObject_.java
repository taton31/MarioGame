package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

public abstract class MapObject_ {
    MapObjects objects;
    protected Map map;
    OrthographicCamera camera;

    float[] rectangle_object;
    float[][] rectangle_objects;
    int length;
    int i;
    int j;
    Vector2 temporary;
    protected HashSet<Vector2> set;
    Vector2[] temporary_arr;

    public HashSet<Vector2> collisium (float [] rectangle){
        set.clear();
        temporary_arr[0].set(0,0);
        temporary_arr[1].set(0,0);

        for (i = 0; i < length ; ++i){
            if (!check_camera(rectangle_objects[i])) continue;

            temporary.set(map.collisium(rectangle_objects[i], rectangle));

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



    protected void get_rectangles() {
        j=0;
        i=0;
        for (MapObject cell : objects) {
            map.get_rectangle(cell, rectangle_object);
            for (i=0 ; i < 8; ++i){
                rectangle_objects[j][i] = rectangle_object[i];
            }
            ++j;
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


}

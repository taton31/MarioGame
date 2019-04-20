package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mario.game.creatures.Mario.Mario;

import java.util.HashSet;

public class Grounds extends MapObject_{

    Grounds(Map ma, OrthographicCamera cam, Mario mar){
        super(ma, cam, mar);
        objects = map.tiledMap.getLayers().get("ground").getObjects();
        length = objects.getCount();
        rectangle_object = new float[8];
        temporary = new Vector2(0,0);
        set = new HashSet<Vector2>();
        temporary_arr = new Vector2[2];
        temporary_arr[0] = new Vector2();
        temporary_arr[1] = new Vector2(0,0);

        mapObjects = new Array<MapObjects_rectangles>();
        get_mapObjects_rectangle();
    }

    public HashSet<Vector2> collisium (float [] rectangle){
        set.clear();
        temporary_arr[0].set(0,0);
        temporary_arr[1].set(0,0);

        for (int i = 0 ;i < mapObjects.size; i++){
            if (!check_camera(mapObjects.get(i).rectangle)) continue;

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



    protected boolean check_camera (float [] a){
        if ( a[0] < camera.position.x && a[2] > camera.position.x ) return true;
        for (j = 0; j < (a.length / 2); ++j){
            if ( a[2 * j] > camera.position.x - Gdx.app.getGraphics().getWidth() / 2 && a[2 * j] < camera.position.x + Gdx.app.getGraphics().getWidth() / 2) return true;
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

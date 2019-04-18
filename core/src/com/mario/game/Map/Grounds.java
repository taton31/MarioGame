package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;
import com.mario.game.creatures.Mario;

import java.util.HashSet;

public class Grounds {

    private MapObjects grounds;
    private Map map;
    private OrthographicCamera camera;

    private float[] ground_rect;
    public final float[][] grounds_rectangle;
    public final int length;
    private int i;
    private int j;
    private Vector2 temporary;
    private HashSet<Vector2> set;
    private Vector2[] temporary_arr;

    Grounds(Map ma, OrthographicCamera cam){
        camera = cam;
        map = ma;
        grounds = map.tiledMap.getLayers().get("ground").getObjects();
        length = grounds.getCount();
        ground_rect = new float[8];
        grounds_rectangle = new float[length][8];
        temporary = new Vector2(0,0);
        set = new HashSet<Vector2>();
        temporary_arr = new Vector2[2];
        temporary_arr[0] = new Vector2();
        temporary_arr[1] = new Vector2(0,0);

        get_grounds_rectangle();
    }

    public HashSet<Vector2> collisium (float [] rectangle){
        set.clear();
        temporary_arr[0].set(0,0);
        temporary_arr[1].set(0,0);

        for (i = 0; i < length ; ++i){
            if (!check_camera(grounds_rectangle[i])) continue;

            temporary.set(map.collisium(grounds_rectangle[i], rectangle));

            //temporary.x += 0.1*Math.signum(temporary.x);
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



    private void get_grounds_rectangle() {
        j=0;
        i=0;
        for (MapObject cell : grounds) {
            map.get_rectangle(cell, ground_rect);
            for (i=0 ; i < 8; ++i){
                grounds_rectangle[j][i] = ground_rect[i];
            }
            ++j;
        }
    }

    private boolean check_camera (float [] a){
        if ( a[0] < camera.position.x && a[2] > camera.position.x ) return true;
        for (j = 0; j < (a.length / 2); ++j){
            if ( a[2 * j] > camera.position.x - Gdx.app.getGraphics().getWidth() / 2 && a[2 * j] < camera.position.x + Gdx.app.getGraphics().getWidth() / 2) return true;
        }
        return false;
    }

    private boolean has_collinear (Vector2 temp){
        for (Vector2 vec : set){
            if (vec.isCollinear(temp)) {
                return true;
            }
        }
        return false;
    }


}

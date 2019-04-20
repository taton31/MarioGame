package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

public class Pipes {

    private MapObjects pipes;
    private Map map;
    private OrthographicCamera camera;

    private float[] pipe_rect;
    public final float[][] pipes_rectangle;
    public final int length;
    private int i;
    private int j;
    private Vector2 temporary;
    private HashSet<Vector2> set;
    private Vector2[] temporary_arr;

    Pipes(Map ma, OrthographicCamera cam){
        camera = cam;
        map = ma;
        pipes = map.tiledMap.getLayers().get("pipes").getObjects();
        length = pipes.getCount();
        pipe_rect = new float[8];
        pipes_rectangle = new float[length][8];
        temporary = new Vector2(0,0);
        set = new HashSet<Vector2>();
        temporary_arr = new Vector2[2];
        temporary_arr[0] = new Vector2();
        temporary_arr[1] = new Vector2(0,0);

        get_pipes_rectangle();
    }

    public HashSet<Vector2> collisium (float [] rectangle){
        set.clear();
        temporary_arr[0].set(0,0);
        temporary_arr[1].set(0,0);

        for (i = 0; i < length ; ++i){
            if (!check_camera(pipes_rectangle[i])) continue;

            temporary.set(map.collisium(pipes_rectangle[i], rectangle));

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



    private void get_pipes_rectangle() {
        j=0;
        i=0;
        for (MapObject cell : pipes) {
            map.get_rectangle(cell, pipe_rect);
            for (i=0 ; i < 8; ++i){
                pipes_rectangle[j][i] = pipe_rect[i];
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

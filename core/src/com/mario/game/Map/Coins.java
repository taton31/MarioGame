package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

public class Coins {
    // элементы coins могут быть только кубиками 1 на 1!!!!!!!!!!!!!!!!!!!!!!
    private MapObjects coins;
    private Map map;
    private OrthographicCamera camera;

    private float[] coin_rect;
    public final float[][] coins_rectangle;
    public final int length;
    private int i;
    private int j;
    private Vector2 temporary;
    private HashSet<Vector2> set;
    private Vector2[] temporary_arr;
    private float proj_vec_x, proj_vec_y;
    private final float tile_size;

    Coins(Map ma, OrthographicCamera cam){
        camera = cam;
        map = ma;
        tile_size = 16 * map.PlayGame.game.ratioY;
        coins = map.tiledMap.getLayers().get("coins").getObjects();
        length = coins.getCount();
        coin_rect = new float[8];
        coins_rectangle = new float[length][8];
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
            if (!check_camera(coins_rectangle[i])) continue;
            temporary.set(map.collisium(coins_rectangle[i], rectangle));

            if (temporary.epsilonEquals(0,0)) continue;

            if( check_direction(i, temporary) ) continue;

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
        for (MapObject cell : coins) {
            map.get_rectangle(cell, coin_rect);
            for (i=0 ; i < 8; ++i){
                coins_rectangle[j][i] = coin_rect[i];
            }
            ++j;
        }
    }

    private boolean check_camera (float [] a){
        if ( a[0] < camera.position.x && a[2] > camera.position.x ) return true;
        for (int k = 0; k < (a.length / 2); ++k){
            if ( a[2 * k] > camera.position.x - Gdx.app.getGraphics().getWidth() / 2 && a[2 * k] < camera.position.x + Gdx.app.getGraphics().getWidth() / 2) return true;
        }
        return false;
    }

    private boolean check_direction(int i, Vector2 vec){
        proj_vec_x = Math.signum(vec.x);
        proj_vec_y = Math.signum(vec.y);
        float x, y;

        if (proj_vec_x != 0) {
            x = coins_rectangle[i][0] + tile_size / 2 + tile_size * proj_vec_x;
            y = coins_rectangle[i][1] + tile_size / 2;
            if (check_input_tile(x, y)) return true;
        }

        if (proj_vec_y != 0) {
            x = coins_rectangle[i][0] + tile_size / 2;
            y = coins_rectangle[i][1] + tile_size / 2 + tile_size * proj_vec_y;
            return check_input_tile(x, y);
        }

        return false;
    }

    private boolean check_input_tile(float x, float y){
        for (j = 0; j < length ; ++j){
            if (!check_camera(coins_rectangle[j])) continue;
            if (x > coins_rectangle[j][0] && x < coins_rectangle[j][2] && y > coins_rectangle[j][1] && y < coins_rectangle[j][7]) {
                return true;
            }
        }
        for (j = 0; j < map.grounds.length ; ++j){
            if (!check_camera(map.grounds.grounds_rectangle[j])) continue;
            if (x > map.grounds.grounds_rectangle[j][0] && x < map.grounds.grounds_rectangle[j][2] && y > map.grounds.grounds_rectangle[j][1] && y < map.grounds.grounds_rectangle[j][7]) {
                return true;
            }
        }
        for (j = 0; j < map.bricks.length ; ++j){
            if (!check_camera(map.bricks.bricks_rectangle[j])) continue;
            if (x > map.bricks.bricks_rectangle[j][0] && x < map.bricks.bricks_rectangle[j][2] && y > map.bricks.bricks_rectangle[j][1] && y < map.bricks.bricks_rectangle[j][7]) {
                return true;
            }
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

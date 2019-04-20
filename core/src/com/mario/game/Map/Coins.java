package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mario.game.creatures.Mario.Mario;

import java.util.HashSet;

public class Coins extends MapObject_ {
    private final float tile_size;

    Coins(Map ma, OrthographicCamera cam, Mario mar){
        mario = mar;
        camera = cam;
        map = ma;
        tile_size = 16 * map.PlayGame.game.ratioY;
        objects = map.tiledMap.getLayers().get("coins").getObjects();
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

            if( check_direction(temporary, mapObjects.get(i).rectangle) ) continue;

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
        for (int k = 0; k < (a.length / 2); ++k){
            if ( a[2 * k] > camera.position.x - Gdx.app.getGraphics().getWidth() / 2 && a[2 * k] < camera.position.x + Gdx.app.getGraphics().getWidth() / 2) return true;
        }
        return false;
    }

    private boolean check_direction(Vector2 vec,  float [] rectangle){
        // элементы coins могут быть только кубиками 1 на 1!!!!!!!!!!!!!!!!!!!!!!
        float proj_vec_x = Math.signum(vec.x);
        float proj_vec_y = Math.signum(vec.y);
        float x, y;

        if (proj_vec_x != 0) {
            x = rectangle[0] + tile_size / 2 + tile_size * proj_vec_x;
            y = rectangle[1] + tile_size / 2;
            if (check_input_tile(x, y)) return true;
        }

        if (proj_vec_y != 0) {
            x = rectangle[0] + tile_size / 2;
            y = rectangle[1] + tile_size / 2 + tile_size * proj_vec_y;
            return check_input_tile(x, y);
        }

        return false;
    }

    private boolean check_input_tile(float x, float y){
        for (MapObjects_rectangles obj_rec : mapObjects){
            if (!check_camera(obj_rec.rectangle)) continue;
            if (x > obj_rec.rectangle[0] && x < obj_rec.rectangle[2] && y > obj_rec.rectangle[1] && y < obj_rec.rectangle[7]) {
                return true;
            }
        }
        for (MapObjects_rectangles obj_rec : map.grounds.mapObjects){
            if (!check_camera(obj_rec.rectangle)) continue;
            if (x > obj_rec.rectangle[0] && x < obj_rec.rectangle[2] && y > obj_rec.rectangle[1] && y < obj_rec.rectangle[7]) {
                return true;
            }
        }
        for (MapObjects_rectangles obj_rec : map.bricks.mapObjects){
            if (!check_camera(obj_rec.rectangle)) continue;
            if (x > obj_rec.rectangle[0] && x < obj_rec.rectangle[2] && y > obj_rec.rectangle[1] && y < obj_rec.rectangle[7]) {
                return true;
            }
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

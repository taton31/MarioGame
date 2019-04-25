package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mario.game.creatures.Mario.Mario;
import com.mario.game.creatures.enemy.Goomba;
import com.mario.game.creatures.mushroom;
import com.mario.game.creatures.mushroomUP;

import java.util.HashSet;

public class Coins extends MapObject_ {
    private final float tile_size;


    Coins(Map ma, OrthographicCamera cam, Mario mar){
        super(ma, cam, mar);
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
        for (MapObjects_rectangles cell : mapObjects) {
            cell.get_view();
        }

    }

    public HashSet<Vector2> collisium (float [] rectangle, boolean check_all_world){
        set.clear();
        temporary_arr[0].set(0,0);
        temporary_arr[1].set(0,0);

        for (int i = 0 ;i < mapObjects.size; i++){



            if (!check_all_world && !check_camera(mapObjects.get(i).rectangle)) continue;
            temporary.set(map.collisium(mapObjects.get(i).rectangle, rectangle));

            if (temporary.epsilonEquals(0,0)) continue;

            if (mapObjects.get(i).health && (temporary.x != 0 || temporary.y > 0 || mario.velocity.y < 0))  continue;

            if( check_direction(temporary, mapObjects.get(i).rectangle) ) continue;

            if (set.isEmpty()) {
                set.add(temporary_arr[0].set(temporary));
                check_crash(temporary, i);
                continue;
            }

            if (has_collinear(temporary)) continue;

            if (temporary_arr[1].epsilonEquals(0,0)) set.add(temporary_arr[1].set(temporary));
            else {
                set.add(temporary.cpy());
                check_crash(temporary, i);
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

    void check_crash (Vector2 temp, int k){
        if (temp.x == 0 && temp.y < 0){

            for (Goomba goomba : map.getGoomdas()){
                if (mapObjects.get(k).rectangle[0] < goomba.position.x + goomba.width / 2f && mapObjects.get(k).rectangle[2] > goomba.position.x + goomba.width / 2f &&
                        mapObjects.get(k).rectangle[7] < goomba.position.y + goomba.height / 2f && mapObjects.get(k).rectangle[7] + tile_size > goomba.position.y + goomba.height / 2f){
                    goomba.mushDie.flip(false, true);
                    System.out.println(goomba.mushDie.isFlipY());
                    goomba.DIE = true;
                    goomba.velocity.set(Math.signum(goomba.random.nextInt(1000) - 500) * (goomba.random.nextInt(150) + 150), 650);
                }
            }

            if (mapObjects.get(k).empty) return;

                if (mapObjects.get(k).coin) {
                    mario.getCoin().play();
                } else if ( mapObjects.get(k).loopCoin){
                    mario.getCoin().play();
                    if (mapObjects.get(k).timeloop == -1f) mapObjects.get(k).timeloop = 0f;
                }
                else if (mapObjects.get(k).mursh) {
                    mario.getPowerup_spawn().play();
                    map.getMushrooms().add(new mushroom(mapObjects.get(k).rectangle[0],mapObjects.get(k).rectangle[7], map.PlayGame, mario ));
                } else {
                    mario.getPowerup_spawn().play();
                    map.getMushroomsUP().add(new mushroomUP(mapObjects.get(k).rectangle[0],mapObjects.get(k).rectangle[7], map.PlayGame, mario ));
                    mapObjects.get(k).health = false;
                    mapObjects.get(k).coin = true;
                }



            if (!mapObjects.get(k).loopCoin) {
                mapObjects.get(k).empty = true;
                TiledMapTile tile = map.tiledMap.getTileSets().getTile(28);
                map.layer.getCell((int) (mapObjects.get(k).rectangle[0] / tile_size), (int) (mapObjects.get(k).rectangle[1] / tile_size)).setTile(null).setTile(tile);
                ((OrthoCachedTiledMapRenderer) map.tiledMapRenderer).invalidateCache();
            }

        }
    }

    public void update_loop (float delta) {
        for (int k = 0; k < mapObjects.size; k++) {
            if (mapObjects.get(k).timeloop == -1f) continue;

            mapObjects.get(k).timeloop += delta;

            if (mapObjects.get(k).timeloop > 4f) {
                mapObjects.get(k).empty = true;
                TiledMapTile tile = map.tiledMap.getTileSets().getTile(28);
                map.layer.getCell((int) (mapObjects.get(k).rectangle[0] / tile_size), (int) (mapObjects.get(k).rectangle[1] / tile_size)).setTile(null).setTile(tile);
                ((OrthoCachedTiledMapRenderer) map.tiledMapRenderer).invalidateCache();
                mapObjects.get(k).timeloop = -1f;
            }
        }
    }

}

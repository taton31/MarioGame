package com.mario.game.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mario.game.creatures.enemy.mushroom;
import com.mario.game.Screens.play_game;

import java.util.HashSet;

public class Map implements Disposable {
    play_game PlayGame;
    TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    public Grounds grounds;
    public Bricks bricks;
    public Pipes pipes;
    public Coins coins;
    private HashSet<Vector2> set;
    private Vector2 RESULT, result;
    private int i, j;
    private Vector2 proj_first_point1, proj_first_point2, proj_second_point1, proj_second_point2;
    private Vector2 temporary_point, proj_firsec_point1, proj_firsec_point2;
    private Vector2 [] vec;
// в конструкторе должно быть количесв=тво монет и колво жизней марио

    public Array<mushroom> mush_array;

    public Map(play_game PlayGa, String fileName, OrthographicCamera cam) {
        PlayGame = PlayGa;
        PlayGame.mario.mario_clean();
        tiledMap = new TmxMapLoader().load(fileName);
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap, PlayGame.game.ratioY);
        ((OrthoCachedTiledMapRenderer) tiledMapRenderer).setBlending(true);
        grounds = new Grounds(this, cam);
        bricks = new Bricks(this, cam);
        pipes = new Pipes(this, cam);
        coins = new Coins(this, cam);
        mush_array = new Array<mushroom>();
        mush_array.add(new mushroom(10*16*PlayGame.game.ratioY, 5*16*PlayGame.game.ratioY, PlayGame, PlayGame.mario));
        mush_array.add(new mushroom(10*16*PlayGame.game.ratioY, 3*16*PlayGame.game.ratioY, PlayGame, PlayGame.mario));




        set = new HashSet<Vector2>();

        RESULT = new Vector2(111,111);
        result = new Vector2(111,111);

        proj_first_point1 = new Vector2();
        proj_first_point2 = new Vector2();
        proj_second_point1 = new Vector2();
        proj_second_point2 = new Vector2();
        temporary_point = new Vector2();
        proj_firsec_point1 = new Vector2();
        proj_firsec_point2 = new Vector2();

        vec = new Vector2[4];
        vec[0] = new Vector2();
        vec[1] = new Vector2();
        vec[2] = new Vector2();
        vec[3] = new Vector2();

    }

    public void update(float delta) {
        tiledMapRenderer.setView(PlayGame.camera);
        for (mushroom mush : mush_array){
            mush.update(delta);
            if (mush.die_time > 1.5f) mush_array.removeValue(mush, true);
        }
    }

    public void render() {
        tiledMapRenderer.render();
    }


    public Vector2 collisium(float[] body_first, float[] body_second){
        RESULT.set(-111,111);
        result.set(0,0);
        set.clear();
        j=0;
        for (i = 0; i < (body_first.length / 2); ++i){
            temporary_point.x = body_first[(2 * (i + 1)) % body_first.length] - body_first[2 * i];
            temporary_point.y = body_first[(2 * (i + 1) + 1) % body_first.length] - body_first[2 * i + 1];

            temporary_point.rotate90(1).nor();
            if (!(contanes(temporary_point, set))) {
                if (j < 4){
                    vec[j].set(temporary_point);
                    set.add(vec[j]);
                    ++j;
                } else set.add(temporary_point.cpy());

            }

        }

        for (i = 0; i < (body_second.length / 2); ++i){
            temporary_point.x = body_second[(2 * (i + 1)) % body_second.length] - body_second[2 * i];
            temporary_point.y = body_second[(2 * (i + 1) + 1) % body_second.length] - body_second[2 * i + 1];
            temporary_point.rotate90(1).nor();
            //set.add( (Math.abs(temporary_point.angle()) % 180));
            if (!(contanes(temporary_point, set))) {
                if (j < 4){
                    vec[j].set(temporary_point);
                    set.add(vec[j]);
                    ++j;
                } else set.add(temporary_point.cpy());
            }
        }

        for (Vector2 vector : set){
            search_projection(body_first, vector, proj_first_point1, proj_first_point2);
            search_projection(body_second, vector, proj_second_point1, proj_second_point2);
            if (cross_projections(proj_first_point1, proj_first_point2, proj_second_point1, proj_second_point2, result)){
                return result;
            }
            if (result.len2() > 0.000000001f && RESULT.len() > result.len()) {
                RESULT.set(result);
            }
        }
        if (RESULT.epsilonEquals(111,111)){
            RESULT.set(0,0);
        }
        return RESULT;
    }

    private void collisium_with_enemy(){

    }

    private void search_projection(float[] body, Vector2 vector, Vector2 start, Vector2 end){
        start.set(111, 111);
        end.set(111, 111);
        int length = body.length;
        vector.nor();
        for (j = 0; j < (length / 2); ++j) {
            if (vector.x != 0) {
                temporary_point.y = vector.y * (body[2 * j] * vector.x + body[2 * j + 1] * vector.y);
                temporary_point.x = (temporary_point.y - body[2 * j + 1]) * (-vector.y / vector.x) + body[2 * j];
            } else {
                temporary_point.x = 0;
                temporary_point.y = body[2 * j + 1];
            }

            if (start.epsilonEquals(111, 111)) {
                start.set(temporary_point);
                continue;
            } else if (end.epsilonEquals(111, 111)) {
                end.set(temporary_point);
                continue;
            } else if (start.x != end.x) {
                if (temporary_point.x < start.x && temporary_point.x < end.x) {
                    if (start.x < end.x) {
                        start.set(temporary_point);
                    } else {
                        end.set(temporary_point);
                    }
                } else if (temporary_point.x > start.x && temporary_point.x > end.x) {
                    if (start.x > end.x) {
                        start.set(temporary_point);
                    } else {
                        end.set(temporary_point);
                    }
                }
            } else {
                if (temporary_point.y < start.y && temporary_point.y < end.y) {
                    if (start.y < end.y) {
                        start.set(temporary_point);
                    } else {
                        end.set(temporary_point);
                    }
                } else if (temporary_point.y > start.y && temporary_point.y > end.y) {
                    if (start.y > end.y) {
                        start.set(temporary_point);
                    } else {
                        end.set(temporary_point);
                    }
                }
            }
        }
    }

    private boolean cross_projections(Vector2 start_first, Vector2 end_first, Vector2 start_second, Vector2 end_second, Vector2 result){

        if (start_first.x != end_first.x) {
            if (start_first.x <= start_second.x && start_first.x <= end_second.x && end_first.x <= start_second.x && end_first.x <= end_second.x) {
                result.set(0, 0);
                return true;
            }
            else if (start_first.x >= start_second.x && start_first.x >= end_second.x && end_first.x >= start_second.x && end_first.x >= end_second.x) {
                result.set(0, 0);
                return true;
            }
        } else  {
            if (start_first.y <= start_second.y && start_first.y <= end_second.y && end_first.y <= start_second.y && end_first.y <= end_second.y) {
                result.set(0, 0);
                return true;
            }
            else if (start_first.y >= start_second.y && start_first.y >= end_second.y && end_first.y >= start_second.y && end_first.y >= end_second.y) {
                result.set(0, 0);
                return true;
            }
        }

        if (start_first.x != end_first.x){
            if (start_first.x < end_first.x){
                if (start_second.x < end_first.x && start_second.x > start_first.x){
                    proj_firsec_point1.set(start_second);
                } else {
                    proj_firsec_point1.set(end_second);
                }
            } else {
                if (start_second.x < start_first.x && start_second.x > end_first.x){
                    proj_firsec_point1.set(start_second);
                } else {
                    proj_firsec_point1.set(end_second);
                }
            }

            if (start_second.x < end_second.x){
                if (start_first.x < end_second.x && start_first.x > start_second.x){
                    proj_firsec_point2.set(start_first);
                } else {
                    proj_firsec_point2.set(end_first);
                }
            } else {
                if (start_first.x < start_second.x && start_first.x > end_second.x){
                    proj_firsec_point2.set(start_first);
                } else {
                    proj_firsec_point2.set(end_first);
                }
            }
        } else {
            if (start_first.y < end_first.y){
                if (start_second.y < end_first.y && start_second.y > start_first.y){
                    proj_firsec_point1.set(start_second);
                } else {
                    proj_firsec_point1.set(end_second);
                }
            } else {
                if (start_second.y < start_first.y && start_second.y > end_first.y){
                    proj_firsec_point1.set(start_second);
                } else {
                    proj_firsec_point1.set(end_second);
                }
            }

            if (start_second.y < end_second.y){
                if (start_first.y < end_second.y && start_first.y > start_second.y){
                    proj_firsec_point2.set(start_first);
                } else {
                    proj_firsec_point2.set(end_first);
                }
            } else {
                if (start_first.y < start_second.y && start_first.y > end_second.y){
                    proj_firsec_point2.set(start_first);
                } else {
                    proj_firsec_point2.set(end_first);
                }
            }
        }
        result.set(proj_firsec_point2.x - proj_firsec_point1.x, proj_firsec_point2.y - proj_firsec_point1.y);
        return false;
    }

    private boolean contanes (Vector2 a, HashSet<Vector2> set){
        for (Vector2 vec : set){
            if (vec.angle() % 180 == a.angle() % 180) return true;
        }
        return  false;
    }

    void get_rectangle(MapObject cell, float[] ground_rect){
        float x;
        ground_rect[0] = x = ((RectangleMapObject) cell).getRectangle().getX() * PlayGame.game.ratioY;
        float y;
        ground_rect[1] = y = ((RectangleMapObject) cell).getRectangle().getY() * PlayGame.game.ratioY;
        float width = ((RectangleMapObject) cell).getRectangle().getWidth() * PlayGame.game.ratioY;
        float height = ((RectangleMapObject) cell).getRectangle().getHeight() * PlayGame.game.ratioY;
        ground_rect[2] = x + width;
        ground_rect[3] = y;
        ground_rect[4] = x + width;
        ground_rect[5] = y + height;
        ground_rect[6] = x;
        ground_rect[7] = y + height;
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}

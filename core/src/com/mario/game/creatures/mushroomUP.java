package com.mario.game.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mario.game.Screens.play_game;
import com.mario.game.creatures.Mario.Mario;

import java.util.HashSet;

public class mushroomUP {

    private final play_game playGame;

    public Vector2 position;
    public Vector2 velocity;
    private Vector2 acceleration;
    private Mario mario;
    private Vector2 coll_mar;

    private int velocity_jump;
    private int max_velocity;
    private boolean running_right;
    public boolean was_coll_withMar;
    public final int width;
    public int height;
    private HashSet<Vector2> bias, bias_ground, bias_bricks, bias_coins, bias_pipes;
    private float[] shape;

    public TextureRegion region;


    public mushroomUP(float x, float y, final play_game a, Mario mar){
        playGame = a;
        mario = mar;
        float RATIO = playGame.game.ratioY;
        running_right = true;
        velocity_jump = (int) ( 301 * RATIO);
        max_velocity = (int) (30 * RATIO);
        int acceleration_G = (int) (1000 * RATIO);

        was_coll_withMar = false;

        coll_mar = new Vector2();
        bias = new HashSet<Vector2>();
        shape = new float[8];

        region = new TextureRegion(new Texture("enemy/mushUP.png"), 0,0,16,16);

        width = (int) (16 * RATIO);
        height = (int) (16 * RATIO);
        position = new Vector2(x, y);
        velocity = new Vector2( max_velocity, 0);
        acceleration = new Vector2(0, -acceleration_G);
    }


    public void  update (float delta){
        update_velocity(delta);
        collisium();
        collisium_with_mar(delta);
    }

    private void update_velocity (float delta){

        velocity.x = running_right ? max_velocity : -max_velocity;
        velocity.y += acceleration.y * delta;

        if (velocity.y < -velocity_jump) velocity.y = -velocity_jump;

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

    }


    private void collisium(){
        get_shape();
        bias.clear();
        bias_ground = playGame.map.grounds.collisium(shape, true);
        bias_bricks = playGame.map.bricks.collisium(shape, true);
        bias_pipes = playGame.map.pipes.collisium(shape, true);
        bias_coins = playGame.map.coins.collisium(shape, true);
        check_sets();
        check_revers();

        for (Vector2 vec : bias) {
            position.x += vec.x;
            position.y += vec.y;
            vec.rotate90(1).nor();
            if (vec.hasOppositeDirection(velocity)) vec.scl(-1);
            velocity.set(vec.scl(vec.dot(velocity)));
            if (velocity.x == 0) acceleration.x = 0;
        }

    }

    private void collisium_with_mar(float delta){

        coll_mar.set(playGame.map.collisium(shape, mario.shape));
        if (mario.velocity.y < 0) return;
        if (!coll_mar.epsilonEquals(0,0) && !mario.isMarioDead()) {
            playGame.game.mario_health++;
            playGame.map.mushroomUP_array.removeValue(this, true);
        }
    }

    private void get_shape(){

        shape[0] = position.x ;
        shape[1] = position.y;

        shape[2] = position.x + width ;
        shape[3] = position.y;

        shape[4] = position.x + width ;
        shape[5] = position.y + 3 * height / 5f;

        shape[6] = position.x;
        shape[7] = position.y + 3 * height / 5f;
    }


    private void check_sets(){
        bias.addAll(bias_ground);
        for (Vector2 vec : bias_bricks){
            for (Vector2 b_vec : bias){
                if (b_vec.isCollinear(vec)) continue;
            }
            bias.add(vec);
        }
        for (Vector2 vec : bias_pipes){
            for (Vector2 b_vec : bias){
                if (b_vec.isCollinear(vec)) continue;
            }
            bias.add(vec);
        }
        for (Vector2 vec : bias_coins){
            for (Vector2 b_vec : bias){
                if (b_vec.isCollinear(vec)) continue;
            }
            bias.add(vec);
        }
    }

    private void check_revers(){
        for (Vector2 vec : bias){
            if (vec.x > 0) {
                running_right = true;
                return;
            } else if (vec.x < 0) {
                running_right = false;
                return;
            }
        }
    }


}

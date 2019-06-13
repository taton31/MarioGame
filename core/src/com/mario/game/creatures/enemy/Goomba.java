package com.mario.game.creatures.enemy;

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
import java.util.Random;

public class Goomba {

    private final play_game playGame;

    public Vector2 position;
    public Vector2 velocity;
    private Vector2 acceleration;
    private Mario mario;
    private Vector2 coll_mar;
    private Vector2 temporary;

    private  int velocity_jump;
    private  int max_velocity;
    public boolean running_right;
    public boolean DIE;
    public boolean stay;
    public boolean was_coll_withMar;
    public float die_time;
    public final int width;
    public int height;
    private HashSet<Vector2> bias, bias_ground, bias_bricks, bias_coins, bias_pipes;
    public float[] shape;
    private float timer;
    public Random random;

    private Animation<TextureRegion> mushRun;
    public TextureRegion mushDie;
    public TextureRegion region;


    public Goomba(float x, float y, final play_game a, Mario mar){
        random = new Random();
        playGame = a;
        mario = mar;
        float RATIO = playGame.game.ratioY;
        DIE = false;
        running_right = false;
        velocity_jump = (int) ( 301 * RATIO);
        max_velocity = (int) (30 * RATIO);
        int acceleration_G = (int) (1000 * RATIO);

        stay = true;
        was_coll_withMar = false;

        temporary = new Vector2();
        coll_mar = new Vector2();
        bias = new HashSet<Vector2>();
        shape = new float[8];
        doAnimation();
        region = mushDie;

        width = (int) (16 * RATIO) - 1;
        height = (int) (16 * RATIO);
        timer = 0;
        die_time = 0;
        position = new Vector2(x, y);
        velocity = new Vector2( -max_velocity, 0);
        acceleration = new Vector2(0, -acceleration_G);
    }


    public void  update (float delta){
        update_velocity(delta);
        collisium();
        collisium_with_mar(delta);
    }

    private void update_velocity (float delta){
        if (stay) return;
        if (DIE){
            if (mushDie.isFlipY()){
                velocity.y += acceleration.y * delta;
                position.x += velocity.x * delta;
                position.y += velocity.y * delta;
                region = mushRun.getKeyFrame(0);
                if (!region.isFlipY()) region.flip(false, true);
                return;
            }
            region = mushDie;
            return;
        }
        timer = timer % 1000 + delta;

        velocity.x = running_right ? max_velocity : -max_velocity;
        velocity.y += acceleration.y * delta;

        region = mushRun.getKeyFrame(timer, true);

        if (velocity.y < -velocity_jump) velocity.y = -velocity_jump;

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

        if (position.y + 150 < 0) DIE = true;
    }



    private void doAnimation(){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        Texture texture = new Texture(playGame.name_LVL.contains("black") ? "enemy/goombablack.png" : "enemy/goomba.png");

        frames.add(new TextureRegion(texture, 0, 0, 16, 16));
        frames.add(new TextureRegion(texture, 16, 0, 16, 16));
        mushRun = new Animation<TextureRegion>(0.4f, frames);

        frames.clear();

        mushDie = new TextureRegion(texture, 32, 0, 16, 16);
    }


    private void collisium(){
        if (DIE) return;
        get_shape();
        for (int k = 0; k < playGame.map.goombas_array.size; ++k) {
            if (playGame.map.goombas_array.get(k).position.x != position.x) {
                temporary.set(playGame.map.collisium_goomb(playGame.map.goombas_array.get(k).shape, shape));
                if (temporary.epsilonEquals(0, 0)) continue;
                playGame.map.goombas_array.get(k).running_right = !playGame.map.goombas_array.get(k).running_right;
                running_right = !running_right;
            }
        }

        for (int k = 0; k < playGame.map.turtles_array.size; ++k) {
            temporary.set(playGame.map.collisium_goomb(playGame.map.turtles_array.get(k).shape, shape));
            if (temporary.epsilonEquals(0, 0)) continue;
            if (!playGame.map.turtles_array.get(k).underPan)
                playGame.map.turtles_array.get(k).running_right = !playGame.map.turtles_array.get(k).running_right;
            running_right = !running_right;

        }
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
        if (DIE){
            die_time += delta;
            return;
        }

        coll_mar.set(playGame.map.collisium(shape, mario.shape));
        if (!coll_mar.epsilonEquals(0,0) && !mario.isMarioDead()) {
            if (coll_mar.y > 0 && mario.getVelocityY() < 0){
                if (was_coll_withMar) return;
                DIE = true;
                mario.velocity.y = velocity_jump / 1.6f;
                mario.getStomp().play(mario.getPlayGame().game.MUS_ON / 100f);
            } else {
                was_coll_withMar = true;
                if (mario.isMarioInvulnerable()) return;
                if (mario.isMarioBig()){
                    mario.setMarioSize(false);
                } else {
                    mario.setMarioDead();
                }
            }
        } else was_coll_withMar = false;
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

        /*shape[0] = position.x ;
        shape[1] = position.y;

        shape[2] = position.x + width ;
        shape[3] = position.y;

        shape[4] = position.x + width ;
        shape[5] = position.y + height / 2f;

        shape[6] = position.x + width / 2f ;
        shape[7] = position.y + height;

        shape[8] = position.x ;
        shape[9] = position.y + height / 2f;*/
    }

    public void get_shape_zero(){

        shape[0] = position.x ;
        shape[1] = -222;

        shape[2] = position.x + width ;
        shape[3] = -222;

        shape[4] = position.x + width ;
        shape[5] =-222 + 3 * height / 5f;

        shape[6] = position.x;
        shape[7] = -222 + 3 * height / 5f;
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

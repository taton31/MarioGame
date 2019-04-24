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
import com.mario.game.creatures.enemy.Goomba;

import java.util.HashSet;
import java.util.Random;

public class bullet {

    private final play_game playGame;

    public Vector2 position;
    public Vector2 velocity;
    public Vector2 acceleration;
    private Mario mario;
    private Vector2 coll_mar;
    private Vector2 temporary;

    private int velocity_jump;
    private int max_velocity;
    private boolean running_right;
    public boolean was_coll_withMar;
    public final int width;
    public int height;
    private HashSet<Vector2> bias, bias_ground, bias_bricks, bias_coins, bias_pipes;
    private float[] shape;
    private float timer;
    private Animation<TextureRegion> fly;


    public TextureRegion region;


    public bullet(float x, float y, final play_game a, Mario mar, boolean runningRight){
        playGame = a;
        mario = mar;
        float RATIO = playGame.game.ratioY;
        running_right = true;
        velocity_jump = (int) ( 301 * RATIO);
        max_velocity = (int) (120 * RATIO);

        was_coll_withMar = false;
        doAnimation();
        temporary = new Vector2();
        coll_mar = new Vector2();
        bias = new HashSet<Vector2>();
        shape = new float[8];

        width = (int) (8 * RATIO);
        height = (int) (8 * RATIO);
        timer = 0;
        position = new Vector2(x, y);
        velocity = new Vector2( runningRight ? max_velocity : -max_velocity, -max_velocity);
        acceleration = new Vector2(600 * (runningRight ? mario.getRATIO() : -mario.getRATIO()),-600 * mario.getRATIO());
    }


    public void  update (float delta){
        update_velocity(delta);
        collisium();
        collisium_with_enemy(delta);
    }

    private void update_velocity (float delta){

        timer = timer % 1000 + delta;
        velocity.x += acceleration.x * delta;
        velocity.y += acceleration.y * delta;

        if (velocity.x > max_velocity) velocity.x = max_velocity;
        if (velocity.x < -max_velocity) velocity.x = -max_velocity;
        if (velocity.y < -max_velocity) velocity.y = -max_velocity;

        region = fly.getKeyFrame(timer, true);

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
        check_kill();

        for (Vector2 vec : bias) {
            if (vec.y != 0)
            position.y += vec.y;
            velocity.y = max_velocity;
        }

    }

    private void collisium_with_enemy(float delta){

        for (Goomba goomba : playGame.map.goombas_array){
            coll_mar.set(playGame.map.collisium(shape, goomba.shape));
            if (!coll_mar.epsilonEquals(0,0) && !goomba.DIE) {
                playGame.map.bullet_array.removeValue(this, true);
                goomba.DIE = true;
            }
        }
    }

    private void get_shape(){

        shape[0] = position.x ;
        shape[1] = position.y;

        shape[2] = position.x + width ;
        shape[3] = position.y;

        shape[4] = position.x + width ;
        shape[5] = position.y + height;

        shape[6] = position.x;
        shape[7] = position.y + height;
    }

    private void doAnimation(){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        Texture texture = new Texture("fireball.png");

        frames.add(new TextureRegion(texture, 0, 0, 8, 8));
        frames.add(new TextureRegion(texture, 8, 0, 8, 8));
        frames.add(new TextureRegion(texture, 16, 0, 8, 8));
        frames.add(new TextureRegion(texture, 24, 0, 8, 8));
        fly = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();
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

    private void check_kill(){
        for (Vector2 vec : bias){
            if (vec.x != 0){
                playGame.map.bullet_array.removeValue(this, true);
            }
        }
    }


}

package com.mario.game.creatures.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private  int velocity_jump;
    private  int max_velocity;
    private boolean running_right;
    public boolean DIE;
    public float die_time;
    public final int width;
    public int height;
    private HashSet<Vector2> bias, bias_ground, bias_bricks, bias_coins, bias_pipes;
    private float[] shape;
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

        coll_mar = new Vector2();
        bias = new HashSet<Vector2>();
        shape = new float[8];
        doAnimation();

        width = (int) (16 * RATIO);
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

        if (position.x + width < mario.getPlayGame().camera.position.x - Gdx.app.getGraphics().getWidth() / 2f) {
            DIE = true;
        }

        if (position.y - 16 < 0) DIE = true;
    }



    private void doAnimation(){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        Texture texture = new Texture("enemy/Goomba.png");

        frames.add(new TextureRegion(texture, 0, 0, 16, 16));
        frames.add(new TextureRegion(texture, 16, 0, 16, 16));
        mushRun = new Animation<TextureRegion>(0.4f, frames);

        frames.clear();

        mushDie = new TextureRegion(texture, 32, 0, 16, 16);
    }


    private void collisium(){
        if (DIE) return;
        get_shape();
        bias.clear();
        bias_ground = playGame.map.grounds.collisium(shape);
        bias_bricks = playGame.map.bricks.collisium(shape);
        bias_pipes = playGame.map.pipes.collisium(shape);
        bias_coins = playGame.map.coins.collisium(shape);
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
                DIE = true;
                mario.velocity.y = velocity_jump / 1.6f;
                if (playGame.game.MUS_ON) mario.getStomp().play();
            } else {
                if (mario.isMarioInvulnerable()) return;
                if (mario.isMarioBig()){
                    mario.setMarioSize(false);
                } else {
                    mario.setMarioDead();
                }
            }
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

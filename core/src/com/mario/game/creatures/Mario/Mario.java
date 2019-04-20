package com.mario.game.creatures.Mario;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mario.game.Screens.play_game;

import java.util.HashSet;

public class Mario {
     play_game playGame;

     float RATIO;
     int width;
     int height;

     public Vector2 position;
     public Vector2 velocity;
     Vector2 acceleration;
     float sign_velocity = 0;

     int velocity_start;
     int acceleration_G;
     int velocity_jump;
     int max_velocity;
     float koff_acc;

     float stateTimer;
     boolean runningRight;
     boolean marioIsBig;
     boolean runGrowAnimation;
     boolean timeToDefineBigMario;
     boolean timeToRedefineMario;
     boolean marioIsDead;

     Sound stomp;
     Sound mariodie;

     boolean stayOnGround;
     HashSet<Vector2> bias, bias_ground, bias_bricks, bias_coins, bias_pipes;
     public float[] shape;

     boolean press_button = false;
     boolean press_button_up = false;

     enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}

     State currentState;
     State previousState;

     TextureRegion mario_texture;
     TextureRegion marioStand;
     Animation<TextureRegion> marioRun;
     TextureRegion marioJump;
     TextureRegion marioDead;
     TextureRegion marioStop;
     TextureRegion bigMarioStand;
     TextureRegion bigMarioJump;
     Animation<TextureRegion> bigMarioRun;
     Animation<TextureRegion> growMario;

     Texture texture;


     private Mario_move mario_move;
     private Mario_collisium mario_collisium;
     private Mario_HUD mario_HUD;
     private Mario_graphics mario_graphics;
     private Mario_music mario_music;
    public Mario(float x, float y, final play_game a){
        playGame = a;
        mario_move = new Mario_move(this, x, y);
        mario_collisium = new Mario_collisium(this);
        mario_graphics = new Mario_graphics(this);
        mario_HUD = new Mario_HUD(this);
        mario_music = new Mario_music(this);
    }


    public void  update (float delta) {
        mario_move.update(delta);
        mario_collisium.update(delta);
        mario_graphics.update(delta);
    }


    public int getWidth(){ return width; }

    public int getHeight(){
        return height;
    }

    public float getRATIO(){
        return RATIO;
    }

    public TextureRegion getTexture(){
        return mario_texture;
    }

    public Sound getStomp() { return stomp;}

    public boolean isMarioDead(){ return marioIsDead; }

    public void setMarioDead(){mario_move.mario_dead();}

    public void press_right(){
        mario_HUD.press_right();
    }

    public void unpress_button(){
        mario_HUD.unpress_button();
    }

    public void press_left(){
        mario_HUD.press_left();
    }

    public void press_up(){
        mario_HUD.press_up();
    }

    public void unpress_button_up(){
        mario_HUD.unpress_button_up();
    }

    public void press_down(){
        mario_HUD.press_down();
    }

    public void press_fire(){
        mario_HUD.press_fire();
    }

    public void set_vel_start(int a){
        velocity_start = a;
    }

    public void set_vel_jump(int a){
        velocity_jump = a;
    }

    public void set_max_vel(int a){
        max_velocity = a;
    }

    public void set_acceleration_G(int a){
        acceleration_G = a;
    }

    public void set_koff_acc(float a){
        koff_acc = a;
    }

    public void mario_clean(){
        mario_move.mario_clean();
    }
}

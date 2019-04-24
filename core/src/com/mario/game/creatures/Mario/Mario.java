package com.mario.game.creatures.Mario;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.mario.game.Screens.play_game;

import java.util.HashSet;

public class Mario implements Disposable {
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
     public float TimerInvulnerable;
     boolean runningRight;
     boolean marioIsBig;
     boolean marioIsFire;
     boolean runGrowAnimation;
     boolean timeToDefineBigMario;
     boolean timeToRedefineMario;
     boolean marioIsDead;
     boolean marioIsInvulnerable;
     public boolean Endgame;

     Sound stomp;
     Sound mariodie;
     Sound bump;
     Sound breakblock;
     Sound coin;
     Sound powerdown;
     Sound powerup;
     Sound powerup_spawn;

     boolean stayOnGround;
     HashSet<Vector2> bias, bias_ground, bias_bricks, bias_coins, bias_pipes;
     public float[] shape;

     boolean press_button = false;
     boolean press_button_up = false;
     boolean press_button_down = false;



    enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, SITTING}

     State currentState;
     State previousState;

     TextureRegion mario_texture;
     TextureRegion marioStand;
     Animation<TextureRegion> marioRun;
     TextureRegion marioJump;
     TextureRegion marioDead;
     TextureRegion marioStop;
     TextureRegion marioEmpty;
     TextureRegion marioSit;
     TextureRegion bigMarioStand;
     TextureRegion bigMarioJump;
     Animation<TextureRegion> bigMarioRun;
     Animation<TextureRegion> growMario;
     Animation<TextureRegion> growMarioDown;

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
        //setMarioSize(true);

        Endgame = false;
    }


    public void  update (float delta) {
        mario_move.update(delta);
        mario_collisium.update(delta);
        mario_graphics.update(delta);

        playGame.map.check_end_LVL();
    }

    @Override
    public void dispose() {

        stomp.dispose();
        mariodie.dispose();
        bump.dispose();
        breakblock.dispose();
        coin.dispose();
        powerdown.dispose();
        powerup.dispose();
        powerup_spawn.dispose();

        texture.dispose();

    }

    public float getX(){ return position.x; }

    public float getY(){ return position.y; }

    public float getVelocityY(){ return velocity.y; }

    public int getWidth(){ return width; }

    public int getHeight(){
        return height;
    }

    void setHeight(boolean a){
        height = a ? (int) (30 * RATIO) : (int) (16 * RATIO);
    }

    public float getRATIO(){
        return RATIO;
    }

    public TextureRegion getTexture(){
        return mario_texture;
    }

    public Sound getStomp() { return stomp;}
    public Sound getBreakblock() { return breakblock;}
    public Sound getBump() { return bump;}
    public Sound getCoin() { return coin;}
    public Sound getMariodie() { return mariodie;}
    public Sound getPowerdown() { return powerdown;}
    public Sound getPowerup() { return powerup;}
    public Sound getPowerup_spawn() { return powerup_spawn;}

    public boolean isMarioDead(){ return marioIsDead; }

    public boolean isMarioBig(){ return marioIsBig; }

    public void setMarioDead(){mario_move.mario_dead();}

    public void setMarioSize(boolean a){
        marioIsBig = a;
        height = a ? (int) (30 * RATIO) : (int) (16 * RATIO);
        if (!a) setMarioFire(false);
        if (!a) setMarioInvulnerable();
        (a ? powerup : powerdown).play(getPlayGame().game.MUS_ON / 100f);
    }

    public void setMarioFire(boolean a){
        marioIsFire = a;
        playGame.scene.fire.setVisible(a);
        (a ? powerup : powerdown).play(getPlayGame().game.MUS_ON / 100f);
    }

    public void press_right(){
        if (press_button_down) return;
        mario_HUD.press_right();
    }

    public void unpress_button(){
        mario_HUD.unpress_button();
    }

    public void press_left(){
        if (press_button_down) return;
        mario_HUD.press_left();
    }

    public void press_up(){
        if (press_button_down) return;
        mario_HUD.press_up();
    }


    public void unpress_button_up(){
        mario_HUD.unpress_button_up();

    }
    public void unpress_button_down(){
        mario_HUD.unpress_button_down();
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

    public void setMarioInvulnerable(){
        TimerInvulnerable = 0;
        marioIsInvulnerable = true;
    }

    public boolean isMarioInvulnerable(){
        return marioIsInvulnerable;
    }

    public play_game getPlayGame(){
        return playGame;
    }

    public boolean getPressButtonDown(){
        return press_button_down;
    }

    public void setXY(int x, int y){
        position.set(x,y);
    }
}

package com.mario.game.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mario.game.Map.Map;
import com.mario.game.Screens.GameOver;
import com.mario.game.Screens.play_game;

import java.util.HashSet;

public class Mario {

    private final play_game playGame;

    public Vector2 position;
    public Vector2 velocity;
    private Vector2 acceleration;
    private int health;

    public int velocity_start;
    public int acceleration_G;
    public int velocity_jump;
    public int max_velocity;
    public float koff_acc;

    private float sign_velocity = 0;
    private boolean press_button = false;
    private boolean press_button_up = false;
    private boolean stayOnGround;

    public final float RATIO;
    public final int width;
    public final int height;

    private HashSet<Vector2> bias, bias_ground, bias_bricks, bias_coins, bias_pipes;
    public float[] shape;


    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}
    private State currentState;
    private State previousState;

    public TextureRegion mario_texture;
    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private TextureRegion marioStop;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation<TextureRegion> bigMarioRun;
    private Animation<TextureRegion> growMario;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    public boolean marioIsDead;

    private Texture texture;

    public Sound stomp;
    public Sound mariodie;

    public Mario(float x, float y, final play_game a){
        stomp = Gdx.audio.newSound(Gdx.files.internal("music/sounds/stomp.wav"));
        mariodie = Gdx.audio.newSound(Gdx.files.internal("music/sounds/mariodie.wav"));
        //stomp = Gdx.audio.newSound(Gdx.files.internal("music/sounds/stomp.wav"));
        //stomp = Gdx.audio.newSound(Gdx.files.internal("music/sounds/stomp.wav"));
        //stomp = Gdx.audio.newSound(Gdx.files.internal("music/sounds/stomp.wav"));

        playGame = a;
        health = 3;
        RATIO = playGame.game.ratioY;
        velocity_start = (int) ( 200 * RATIO);
        velocity_jump = (int) ( 201 * RATIO);
        max_velocity = (int) (100 * RATIO);
        acceleration_G = (int) (1000 * RATIO);
        koff_acc = (float) (0.3 * RATIO);

        bias = new HashSet<Vector2>();
        shape = new float[12];
        doAnimation();

        width = (int) (12 * RATIO);
        height = (int) (16 * RATIO);

        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, -acceleration_G);
        get_shape();
    }


    public void  update (float delta){

        if (marioIsDead){
            mario_dead_update(delta);
            return;
        }

        update_velocity(delta);
        collisium();
        getFrame(delta);
        camera_setPosition(delta);
    }

    private void update_velocity (float delta){

        ///////////////////
        playGame.scene.testDesktop.update();

        if (!press_button && velocity.x !=0) {
            if ((velocity.x + acceleration.x * delta) * sign_velocity < 0)
            {
                velocity.x=0;
                acceleration.x=0;
            }
            else
            {
                sign_velocity = Math.signum(velocity.x);
                acceleration.x = -velocity_start*sign_velocity;
            }
        }

        if (stayOnGround && press_button && Math.signum(velocity.x)*Math.signum(acceleration.x) < 0){
            acceleration.x = -velocity_start*sign_velocity*1.5f;
        }

        acceleration.y = -acceleration_G;
        if (press_button_up) {
            acceleration.y = -acceleration_G * koff_acc;
            if (velocity.y < 0) press_button_up = false;
        }

        // остался косяк с прыжком возле стены высотой 3  блока

        sign_velocity = Math.signum(velocity.x);
        velocity.x += acceleration.x * delta;
        velocity.y += acceleration.y * delta;
        if (velocity.x > max_velocity) velocity.x = max_velocity;
        if (velocity.x < -max_velocity) velocity.x = -max_velocity;
        if (velocity.y < -velocity_jump) velocity.y = -velocity_jump;

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

    }

    private void camera_setPosition(float delta){
        if (position.x + width / 2f > playGame.camera.position.x)
            playGame.camera.position.x = position.x + width / 2f;
        else if (position.x < playGame.camera.position.x - Gdx.app.getGraphics().getWidth() / 2f) {
            position.x = playGame.camera.position.x - Gdx.app.getGraphics().getWidth() / 2f;
            velocity.x = 0;
            acceleration.x = 0;
        }
        else if (position.x + 5 * width > playGame.camera.position.x && sign_velocity > 0) {
            playGame.camera.position.x += velocity.x * delta / 3.5f;
        }
    }

    public void press_right (){
        acceleration.x = velocity_start;
        press_button = true;
    }

    public void press_left (){
        acceleration.x = - velocity_start;
        press_button = true;
    }

    public void press_up (){
        if (stayOnGround) {
            velocity.y = velocity_jump;
            press_button_up = true;
            stayOnGround = false;
            currentState = State.JUMPING;
        }
    }

    public void press_down (){
        press_button = true;
    }

    public void press_fire (){

    }

    public void unpress_button (){
        press_button = false;
    }

    public void unpress_button_up (){
        press_button_up = false;
    }

    private void doAnimation(){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        texture = new Texture("mario/little_mario.png");

        marioJump = new TextureRegion(texture, 80, 0, 16, 16);
        marioStand = new TextureRegion(texture, 0, 0, 16, 16);
        marioStop= new TextureRegion(texture, 64, 0, 16, 16);
        marioDead = new TextureRegion(texture, 96, 0, 16, 16);

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(texture, i * 16, 0, 16, 16));
        marioRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        texture = new Texture("big_mario/big_mario.png");

        bigMarioJump = new TextureRegion(texture, 80, 0, 16, 32);
        bigMarioStand = new TextureRegion(texture, 0, 0, 16, 32);

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(texture, i * 16, 0, 16, 32));
        bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(texture, 240, 0, 16, 32));
        frames.add(new TextureRegion(texture, 0, 0, 16, 32));
        frames.add(new TextureRegion(texture, 240, 0, 16, 32));
        frames.add(new TextureRegion(texture, 0, 0, 16, 32));
        growMario = new Animation<TextureRegion>(0.2f, frames);
    }

    private State getState(){
        if(marioIsDead)
            return State.DEAD;
        else if(runGrowAnimation)
            return State.GROWING;
        else if((velocity.y > 0 && currentState == State.JUMPING) || (velocity.y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(velocity.y < 0)
            return State.FALLING;
        else if(velocity.x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    private void getFrame(float dt){
        currentState = getState();

        TextureRegion region;

        switch(currentState){
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                if (stayOnGround && velocity.x * acceleration.x < 0 && press_button) region = marioStop;
                else region = (marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true));
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }

        if((velocity.x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        else if((velocity.x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        mario_texture = region;

    }

    private void collisium(){
        get_shape();
        stayOnGround = false;
        bias.clear();
        bias_ground = playGame.map.grounds.collisium(shape);
        bias_bricks = playGame.map.bricks.collisium(shape);
        bias_pipes = playGame.map.pipes.collisium(shape);
        bias_coins = playGame.map.coins.collisium(shape);
        check_sets();
        can_jump(bias);
        for (Vector2 vec : bias) {
            position.x += vec.x;
            position.y += vec.y;
            if (velocity.hasSameDirection(vec)) continue;
            vec.rotate90(1).nor();
            if (vec.hasOppositeDirection(velocity)) vec.scl(-1);
            velocity.set(vec.scl(vec.dot(velocity)));
            if (velocity.x == 0) acceleration.x = 0;
        }
    }

    private void get_shape(){

        shape[0] = position.x + RATIO;
        shape[1] = position.y;

        shape[2] = position.x + width - RATIO;
        shape[3] = position.y;

        shape[4] = position.x -  RATIO + width;
        shape[5] = position.y + 2 * height / 3f;

        shape[6] = position.x + 3 * RATIO + width / 2f;
        shape[7] = position.y  + height;

        shape[8] = position.x - 3 * RATIO + width / 2f;
        shape[9] = position.y  + height;

        shape[10] = position.x +  RATIO;
        shape[11] = position.y + 2 * height / 3f;


        /*shape[0] = position.x + RATIO;
        shape[1] = position.y + 2 * RATIO;

        shape[2] = position.x + 3 * RATIO;
        shape[3] = position.y;

        shape[4] = position.x + width - 3 * RATIO;
        shape[5] = position.y;

        shape[6] = position.x - RATIO + width;
        shape[7] = position.y + 2 * RATIO;

        shape[8] = position.x -  RATIO + width;
        shape[9] = position.y + 2 * height / 3f;

        shape[10] = position.x + 3 * RATIO + width / 2f;
        shape[11] = position.y  + height;

        shape[12] = position.x - 3 * RATIO + width / 2f;
        shape[13] = position.y  + height;

        shape[14] = position.x +  RATIO;
        shape[15] = position.y + 2 * height / 3f;*/
    }

    public void can_jump(HashSet<Vector2> set){
        if (stayOnGround) return;
        for (Vector2 vec : set){
            if (vec.y > 0 && vec.x == 0) {
                stayOnGround = true;
                return;
            }
        }
    }

    public void check_sets(){
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

    public void mario_dead (){
        marioIsDead = true;
        stateTimer = 0;
        velocity.set(0,velocity_jump);
        acceleration.set(0,0);
        getFrame(0);
        mariodie.play();
        playGame.game.gameMusic.stop();
    }

    private void mario_dead_update(float delta){
        stateTimer += delta;
        if (stateTimer > 4f) {

            //playGame.game.mario_health -= 1;
            if (playGame.game.mario_health == 0){
                playGame.game.setScreen(new GameOver(playGame.game));
            }
            mario_clean();
            playGame.map.dispose();
            playGame.map = new Map(playGame,  "tile/map1.tmx",playGame.camera);


        }
        if (stateTimer < 0.5f) return;
        acceleration.set(0, -acceleration_G);
        velocity.x = 0;
        velocity.y += acceleration.y * delta;
        if (velocity.y < -velocity_jump) velocity.y = -velocity_jump;
        position.y += velocity.y * delta;
    }

    public void mario_clean(){
        playGame.scene.timer = 0;
        marioIsDead = false;
        position = new Vector2(100, 100);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, -acceleration_G);
        get_shape();

        playGame.camera.position.x = Gdx.graphics.getWidth() / 2f;
        playGame.camera.position.y = Gdx.graphics.getHeight() / 2f;
    }
}

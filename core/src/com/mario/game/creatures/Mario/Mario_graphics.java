package com.mario.game.creatures.Mario;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

 class Mario_graphics{

    private Mario mario;


    Mario_graphics(Mario mar){
        mario = mar;
        doAnimation();
    }

    void update (float delta){
        getFrame(delta);
    }

    private void doAnimation(){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        mario.texture = new Texture("mario/little_mario.png");

        mario.marioJump = new TextureRegion(mario.texture, 80, 0, 16, 16);
        mario.marioStand = new TextureRegion(mario.texture, 0, 0, 16, 16);
        mario.marioStop= new TextureRegion(mario.texture, 64, 0, 16, 16);
        mario.marioDead = new TextureRegion(mario.texture, 96, 0, 16, 16);

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(mario.texture, i * 16, 0, 16, 16));
        mario.marioRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        mario.texture = new Texture("big_mario/big_mario.png");

        mario.marioSit = new TextureRegion(mario.texture, 96, 0, 16, 32);
        mario.bigMarioJump = new TextureRegion(mario.texture, 80, 0, 16, 32);
        mario.bigMarioStand = new TextureRegion(mario.texture, 0, 0, 16, 32);

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(mario.texture, i * 16, 0, 16, 32));
        mario.bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(mario.texture, 240, 0, 16, 32));
        frames.add(new TextureRegion(mario.texture, 0, 0, 16, 32));
        frames.add(new TextureRegion(mario.texture, 240, 0, 16, 32));
        frames.add(new TextureRegion(mario.texture, 0, 0, 16, 32));
        mario.growMario = new Animation<TextureRegion>(0.2f, frames);

        mario.marioEmpty = new TextureRegion(new Texture("mario/empty.png"));

        frames.add(mario.marioStand);
        frames.add(mario.marioEmpty);
        frames.add(mario.bigMarioStand);
        frames.add(mario.marioEmpty);

        mario.growMarioDown = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();
    }

    private Mario.State getState(){
        if(mario.marioIsDead)
            return Mario.State.DEAD;
        else if(mario.runGrowAnimation )
            return Mario.State.GROWING;
        else if (mario.isMarioInvulnerable() && mario.TimerInvulnerable < 2f)
            return Mario.State.STANDING;
        else if (mario.press_button_down)
            return Mario.State.SITTING;
        else if(mario.currentState == Mario.State.JUMPING && !mario.stayOnGround)
            return Mario.State.JUMPING;
        else if(mario.velocity.y < 0)
            return Mario.State.FALLING;
        else if(mario.velocity.x != 0)
            return Mario.State.RUNNING;
        else
            return Mario.State.STANDING;
    }

    private void getFrame(float dt){
        mario.currentState = getState();

        TextureRegion region;
//добавить случай growdown и в нем сделать нулевую скорость и ускорение
        switch(mario.currentState){
            case DEAD:
                region = mario.marioDead;
                break;
            case GROWING:
                region = mario.growMario.getKeyFrame(mario.stateTimer);
                if(mario.growMario.isAnimationFinished(mario.stateTimer)) {
                    mario.runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = mario.marioIsBig ? mario.bigMarioJump : mario.marioJump;
                break;
            case RUNNING:
                if (mario.stayOnGround && mario.velocity.x * mario.acceleration.x < 0 && (mario.press_button_left || mario.press_button_right)) region = mario.marioStop;
                else region = (mario.marioIsBig ? mario.bigMarioRun.getKeyFrame(mario.stateTimer * mario.RUN, true) : mario.marioRun.getKeyFrame(mario.stateTimer * mario.RUN, true));
                break;
            case SITTING:
                region = mario.marioIsBig ? mario.marioSit : mario.marioStand;
                break;
            case FALLING:
            case STANDING:
            default:
                region = mario.marioIsBig ? mario.bigMarioStand : mario.marioStand;
                break;
        }

        if((mario.velocity.x < 0 || !mario.runningRight) && !region.isFlipX()){
            region.flip(true, false);
            mario.runningRight = false;
        }

        else if((mario.velocity.x > 0 || mario.runningRight) && region.isFlipX()){
            region.flip(true, false);
            mario.runningRight = true;
        }

        mario.stateTimer = mario.currentState == mario.previousState ? mario.stateTimer + dt : 0;
        mario.previousState = mario.currentState;
        if (mario.isMarioInvulnerable() && mario.stateTimer % 0.2f > 0.1) region = mario.marioEmpty;
        mario.mario_texture = region;

    }
}

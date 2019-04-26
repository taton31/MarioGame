package com.mario.game.creatures.Mario;

import com.mario.game.creatures.bullet;

class Mario_HUD{

    private Mario mario;

    private float time;


    Mario_HUD(Mario mar){
        mario = mar;
        mario.velocity_start = (int) ( 200 * mario.RATIO);
        mario.velocity_jump = (int) ( 210 * mario.RATIO);
        mario.max_velocity = (int) (100 * mario.RATIO);
        mario.acceleration_G = (int) (1000 * mario.RATIO);
        mario.koff_acc = 0.3f;
        mario.RUN = 1;
    }

    void press_right (){
        if (mario.Timer - time < 0.5f) {
            mario.max_velocity = (int) (100 * mario.RUN * mario.RATIO);
            mario.RUN = 1.5f;
        }
        mario.acceleration.x = mario.velocity_start;
        mario.press_button_right = true;
        time = mario.Timer;
    }

    void press_left (){
        if (mario.Timer - time < 0.5f) {
            mario.RUN = 1.5f;
            mario.max_velocity = (int) (100 * mario.RUN * mario.RATIO);
        }
        mario.acceleration.x = - mario.velocity_start;
        mario.press_button_left = true;
        time = mario.Timer;
    }

    void press_up (){
        if (mario.stayOnGround) {
            mario.velocity.y = mario.velocity_jump;
            mario.press_button_up = true;
            mario.stayOnGround = false;
            mario.currentState = Mario.State.JUMPING;
        }
    }

    void press_down (){
        mario.press_button_down = true;
        mario.unpress_button();
        mario.unpress_button_up();

    }

    void press_fire (){
        mario.playGame.map.bullet_array.add(new bullet(mario.runningRight ? mario.getX() - 8 * mario.getRATIO() + mario.getWidth() : mario.getX()+8 * mario.getRATIO(), mario.getY() + 2 * mario.getHeight() / 3f,
                mario.playGame, mario, mario.runningRight));
    }

    void unpress_button (){
        mario.max_velocity = (int) (100 * mario.RATIO);
        mario.RUN = 1;
        mario.press_button_left= mario.press_button_right = false;
    }

    void unpress_button_up (){
        mario.press_button_up = false;
    }

    void unpress_button_down (){
        mario.press_button_down = false;
    }
}

package com.mario.game.creatures.Mario;

 class Mario_HUD{

    Mario mario;


    Mario_HUD(Mario mar){
        mario = mar;
        mario.velocity_start = (int) ( 200 * mario.RATIO);
        mario.velocity_jump = (int) ( 201 * mario.RATIO);
        mario.max_velocity = (int) (100 * mario.RATIO);
        mario.acceleration_G = (int) (1000 * mario.RATIO);
        mario.koff_acc = 0.3f;
    }

    public void press_right (){
        mario.acceleration.x = mario.velocity_start;
        mario.press_button = true;
    }

    public void press_left (){
        mario.acceleration.x = - mario.velocity_start;
        mario.press_button = true;
    }

    public void press_up (){
        if (mario.stayOnGround) {
            mario.velocity.y = mario.velocity_jump;
            mario.press_button_up = true;
            mario.stayOnGround = false;
            mario.currentState = Mario.State.JUMPING;
        }
    }

    public void press_down (){
        mario.press_button = true;
    }

    public void press_fire (){

    }

    public void unpress_button (){
        mario.press_button = false;
    }

    public void unpress_button_up (){
        mario.press_button_up = false;
    }
}

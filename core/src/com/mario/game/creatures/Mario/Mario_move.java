package com.mario.game.creatures.Mario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mario.game.Map.Map;
import com.mario.game.Screens.GameOver;

class Mario_move{
    Mario mario;

    Mario_move(Mario mar, float x, float y){
        mario = mar;
        mario.position = new Vector2(x, y);
        mario.velocity = new Vector2(0, 0);
        mario.acceleration = new Vector2(0, -mario.acceleration_G);

    }

    void update (float delta){


        if (mario.marioIsDead){
            mario_dead_update(delta);
            return;
        }

        update_velocity(delta);
        camera_setPosition(delta);
    }

    private void update_velocity (float delta){

        ///////////////////
        mario.playGame.scene.testDesktop.update();

        if (!mario.press_button && mario.velocity.x !=0) {
            if ((mario.velocity.x + mario.acceleration.x * delta) * mario.sign_velocity < 0)
            {
                mario.velocity.x=0;
                mario.acceleration.x=0;
            }
            else
            {
                mario.sign_velocity = Math.signum(mario.velocity.x);
                mario.acceleration.x = -mario.velocity_start*mario.sign_velocity;
            }
        }

        if (mario.stayOnGround && mario.press_button && Math.signum(mario.velocity.x)*Math.signum(mario.acceleration.x) < 0){
            mario.acceleration.x = -mario.velocity_start*mario.sign_velocity*1.5f;
        }

        mario.acceleration.y = -mario.acceleration_G;
        if (mario.press_button_up) {
            mario.acceleration.y = -mario.acceleration_G * mario.koff_acc;
            if (mario.velocity.y < 0) mario.press_button_up = false;
        }

        // остался косяк с прыжком возле стены высотой 3  блока

        mario.sign_velocity = Math.signum(mario.velocity.x);
        mario.velocity.x += mario.acceleration.x * delta;
        mario.velocity.y += mario.acceleration.y * delta;
        if (mario.velocity.x > mario.max_velocity) mario.velocity.x = mario.max_velocity;
        if (mario.velocity.x < -mario.max_velocity) mario.velocity.x = -mario.max_velocity;
        if (mario.velocity.y < -mario.velocity_jump) mario.velocity.y = -mario.velocity_jump;

        mario.position.x += mario.velocity.x * delta;
        mario.position.y += mario.velocity.y * delta;

    }

    private void camera_setPosition(float delta){
        if (mario.position.x + mario.width / 2f > mario.playGame.camera.position.x)
            mario.playGame.camera.position.x = mario.position.x + mario.width / 2f;
        else if (mario.position.x < mario.playGame.camera.position.x - Gdx.app.getGraphics().getWidth() / 2f) {
            mario.position.x = mario.playGame.camera.position.x - Gdx.app.getGraphics().getWidth() / 2f;
            mario.velocity.x = 0;
            mario.acceleration.x = 0;
        }
        else if (mario.position.x + 5 * mario.width > mario.playGame.camera.position.x && mario.sign_velocity > 0) {
            mario.playGame.camera.position.x += mario.velocity.x * delta / 3.5f;
        }
    }

    void mario_dead (){
        mario.marioIsDead = true;
        mario.stateTimer = 0;
        mario.velocity.set(0,mario.velocity_jump);
        mario.acceleration.set(0,0);
        //Mario. getFrame(0);
        mario.mariodie.play();
        mario.playGame.game.gameMusic.stop();
    }

    private void mario_dead_update(float delta){
        mario.stateTimer += delta;
        if (mario.stateTimer > 5f) {

            mario.playGame.game.mario_health -= 1;
            if (mario.playGame.game.mario_health == 0){
                mario.playGame.game.setScreen(new GameOver(mario.playGame.game));
            }
            mario.playGame.map.dispose();
            mario.playGame.map = new Map(mario.playGame,  "tile/map1.tmx",mario.playGame.camera);


        }
        if (mario.stateTimer < 0.5f) return;
        mario.acceleration.set(0, -mario.acceleration_G);
        mario.velocity.x = 0;
        mario.velocity.y += mario.acceleration.y * delta;
        if (mario.velocity.y < -mario.velocity_jump) mario.velocity.y = -mario.velocity_jump;
        mario.position.y += mario.velocity.y * delta;
    }

    void mario_clean(){
        mario.playGame.scene.timer = 0;
        mario.marioIsDead = false;
        mario.position = new Vector2(100, 100);
        mario.velocity = new Vector2(0, 0);
        mario.acceleration = new Vector2(0, -mario.acceleration_G);
        //get_shape();
        mario.playGame.camera.position.x = Gdx.graphics.getWidth() / 2f;
        mario.playGame.camera.position.y = Gdx.graphics.getHeight() / 2f;
    }

}

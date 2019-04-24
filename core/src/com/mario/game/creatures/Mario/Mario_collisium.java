package com.mario.game.creatures.Mario;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

 class Mario_collisium{

    Mario mario;

    Mario_collisium(Mario mar){
        mario = mar;
        mario.RATIO = mario.playGame.game.ratioY;
        mario.width = (int) (12 * mario.RATIO);
        mario.height = (int) (16 * mario.RATIO);

        mario.bias = new HashSet<Vector2>();
        mario.shape = new float[12];
        mario.marioIsInvulnerable = false;

        get_shape();

    }

    void update (float delta){
        if (mario.isMarioDead()) return;
        collisium();
        checkMarioInvulnerable(delta);
    }

    private void collisium(){
        get_shape();
        mario.stayOnGround = false;
        mario.bias.clear();
        mario.bias_ground = mario.playGame.map.grounds.collisium(mario.shape, false);
        mario.bias_pipes = mario.playGame.map.pipes.collisium(mario.shape, false);
        mario.bias_coins = mario.playGame.map.coins.collisium(mario.shape, false);
        mario.bias_bricks = mario.playGame.map.bricks.collisium(mario.shape, false);
        check_sets();
        can_jump(mario.bias);
        for (Vector2 vec : mario.bias) {
            mario.position.x += vec.x;
            mario.position.y += vec.y;
            if (mario.velocity.hasSameDirection(vec)) continue;
            vec.rotate90(1).nor();
            if (vec.hasOppositeDirection(mario.velocity)) vec.scl(-1);
            mario.velocity.set(vec.scl(vec.dot(mario.velocity)));
            if (mario.velocity.x == 0) mario.acceleration.x = 0;
        }
    }

    private void get_shape(){

        mario.shape[0] = mario.position.x + mario.RATIO;
        mario.shape[1] = mario.position.y;

        mario.shape[2] = mario.position.x + mario.width - mario.RATIO;
        mario.shape[3] = mario.position.y;

        mario.shape[4] = mario.position.x -  mario.RATIO + mario.width;
        mario.shape[5] = mario.position.y + 2 * mario.height / 3f;

        mario.shape[6] = mario.position.x + 2 * mario.RATIO + mario.width / 2f;
        mario.shape[7] = mario.position.y  + mario.height;

        mario.shape[8] = mario.position.x - 2 * mario.RATIO + mario.width / 2f;
        mario.shape[9] = mario.position.y  + mario.height;

        mario.shape[10] = mario.position.x +  mario.RATIO;
        mario.shape[11] = mario.position.y + 2 * mario.height / 3f;
    }

    private void can_jump(HashSet<Vector2> set){
        if (mario.stayOnGround) return;
        for (Vector2 vec : set){
            if (vec.y > 0 && vec.x == 0) {
                mario.stayOnGround = true;
                return;
            }
        }
    }

    private void check_sets(){
        mario.bias.addAll(mario.bias_ground);
        for (Vector2 vec : mario.bias_bricks){
            for (Vector2 b_vec : mario.bias){
                if (b_vec.isCollinear(vec)) continue;
            }
            mario.bias.add(vec);
        }
        for (Vector2 vec : mario.bias_pipes){
            for (Vector2 b_vec : mario.bias){
                if (b_vec.isCollinear(vec)) continue;
            }
            mario.bias.add(vec);
        }
        for (Vector2 vec : mario.bias_coins){
            for (Vector2 b_vec : mario.bias){
                if (b_vec.isCollinear(vec)) continue;
            }
            mario.bias.add(vec);
        }
    }

    private void checkMarioInvulnerable(float delta){
        if (!mario.marioIsInvulnerable) return;
        mario.TimerInvulnerable += delta;
        if (mario.TimerInvulnerable > 3.5f) {
            mario.marioIsInvulnerable = false;
        }
    }
}

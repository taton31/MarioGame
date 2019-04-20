package com.mario.game.creatures.Mario;

import com.badlogic.gdx.Gdx;

 class Mario_music{

     private Mario mario;

     Mario_music(Mario mar){
         mario = mar;
         mario.stomp = Gdx.audio.newSound(Gdx.files.internal("music/sounds/stomp.wav"));
         mario.mariodie = Gdx.audio.newSound(Gdx.files.internal("music/sounds/mariodie.wav"));
    }

}

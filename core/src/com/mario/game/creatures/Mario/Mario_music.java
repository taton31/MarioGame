package com.mario.game.creatures.Mario;

import com.badlogic.gdx.Gdx;

 class Mario_music{

     private Mario mario;

     Mario_music(Mario mar){
         mario = mar;
         mario.stomp = Gdx.audio.newSound(Gdx.files.internal("music/sounds/stomp.wav"));
         mario.mariodie = Gdx.audio.newSound(Gdx.files.internal("music/sounds/mariodie.wav"));
         mario.bump = Gdx.audio.newSound(Gdx.files.internal("music/sounds/bump.wav"));
         mario.coin = Gdx.audio.newSound(Gdx.files.internal("music/sounds/coin.wav"));
         mario.powerdown = Gdx.audio.newSound(Gdx.files.internal("music/sounds/powerdown.wav"));
         mario.powerup = Gdx.audio.newSound(Gdx.files.internal("music/sounds/powerup.wav"));
         mario.powerup_spawn = Gdx.audio.newSound(Gdx.files.internal("music/sounds/powerup_spawn.wav"));
         mario.breakblock = Gdx.audio.newSound(Gdx.files.internal("music/sounds/breakblock.wav"));
    }
}

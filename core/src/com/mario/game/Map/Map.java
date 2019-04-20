package com.mario.game.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mario.game.creatures.enemy.mushroom;
import com.mario.game.Screens.play_game;


public class Map extends Collisium implements Disposable  {
    play_game PlayGame;
    TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    public Grounds grounds;
    public Bricks bricks;
    public Pipes pipes;
    public Coins coins;


// в конструкторе должно быть количесв=тво монет и колво жизней марио

    public Array<mushroom> mush_array;

    public Map(play_game PlayGa, String fileName, OrthographicCamera cam) {
        super(PlayGa.game.ratioY);
        PlayGame = PlayGa;
        PlayGame.mario.mario_clean();
        tiledMap = new TmxMapLoader().load(fileName);
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap, PlayGame.game.ratioY);
        ((OrthoCachedTiledMapRenderer) tiledMapRenderer).setBlending(true);
        grounds = new Grounds(this, cam);
        bricks = new Bricks(this, cam);
        pipes = new Pipes(this, cam);
        coins = new Coins(this, cam);
        mush_array = new Array<mushroom>();
        mush_array.add(new mushroom(10*16*PlayGame.game.ratioY, 5*16*PlayGame.game.ratioY, PlayGame, PlayGame.mario));
        mush_array.add(new mushroom(10*16*PlayGame.game.ratioY, 3*16*PlayGame.game.ratioY, PlayGame, PlayGame.mario));






    }

    public void update(float delta) {
        tiledMapRenderer.setView(PlayGame.camera);
        for (mushroom mush : mush_array){
            mush.update(delta);
            if (mush.die_time > 1.5f) mush_array.removeValue(mush, true);
        }
    }

    public void render() {
        tiledMapRenderer.render();
    }


    @Override
    public void dispose() {
        tiledMap.dispose();
    }
}

package com.mario.game.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mario.game.creatures.enemy.Goomba;
import com.mario.game.Screens.play_game;


public class Map extends Collisium implements Disposable  {

    public Grounds grounds;
    public Bricks bricks;
    public Pipes pipes;
    public Coins coins;


// в конструкторе должно быть количесв=тво монет и колво жизней марио

    public Array<Goomba> mush_array;

    public Map(play_game PlayGa, String fileName, OrthographicCamera cam) {
        super(PlayGa.game.ratioY);
        PlayGame = PlayGa;
        PlayGame.mario.mario_clean();
        tiledMap = new TmxMapLoader().load(fileName);
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap, PlayGame.game.ratioY);
        ((OrthoCachedTiledMapRenderer) tiledMapRenderer).setBlending(true);
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("grounds");
        grounds = new Grounds(this, cam, PlayGame.mario);
        bricks = new Bricks(this, cam, PlayGame.mario);
        pipes = new Pipes(this, cam, PlayGame.mario);
        coins = new Coins(this, cam, PlayGame.mario);
        mush_array = new Array<Goomba>();
        mush_array.add(new Goomba(4*16*PlayGame.game.ratioY, 7*16*PlayGame.game.ratioY, PlayGame, PlayGame.mario));
        mush_array.add(new Goomba(10*16*PlayGame.game.ratioY, 3*16*PlayGame.game.ratioY, PlayGame, PlayGame.mario));

    }

    public void update(float delta) {
        tiledMapRenderer.setView(PlayGame.camera);
        for (Goomba mush : mush_array){
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

    public Array<Goomba> getGoomdas(){
        return mush_array;
    }
}

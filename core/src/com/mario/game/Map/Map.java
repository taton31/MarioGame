package com.mario.game.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mario.game.creatures.Mario.Mario;
import com.mario.game.creatures.enemy.Goomba;
import com.mario.game.Screens.play_game;


public class Map extends Collisium implements Disposable  {

    public Grounds grounds;
    public Bricks bricks;
    public Pipes pipes;
    public Coins coins;


// в конструкторе должно быть количесв=тво монет и колво жизней марио
    public Array<Goomba> goombas_array;

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
        goombas_array = new Array<Goomba>();

        create_Goombas();
    }

    public void update(float delta) {
        tiledMapRenderer.setView(PlayGame.camera);

        check_Goombas_move(goombas_array, PlayGame.mario);
        for (Goomba mush : goombas_array){
            mush.update(delta);
            if (mush.die_time > 1.5f) goombas_array.removeValue(mush, true);
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
        return goombas_array;
    }

    public void create_Goombas (){
        MapObjects objects = tiledMap.getLayers().get("goombas").getObjects();
        for (MapObject obj : objects){
            goombas_array.add(new Goomba(((RectangleMapObject) obj).getRectangle().getX() * RATIO,((RectangleMapObject) obj).getRectangle().getY() * RATIO, PlayGame, PlayGame.mario));
        }
    }

    public void check_Goombas_move(Array<Goomba> arr, Mario mario){
        for (Goomba goomba : arr){
            if (goomba.stay && goomba.position.x < mario.getPlayGame().camera.position.x + Gdx.app.getGraphics().getWidth() / 2f + 32 * mario.getRATIO()){
                goomba.stay = false;
            }

        }
    }
}

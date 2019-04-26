package com.mario.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.HUD.Scene;
import com.mario.game.Map.Map;
import com.mario.game.MarioGame;
import com.mario.game.creatures.Mario.Mario;
import com.mario.game.creatures.bullet;
import com.mario.game.creatures.enemy.Goomba;
import com.mario.game.creatures.enemy.Turtle;
import com.mario.game.creatures.mushroom;
import com.mario.game.creatures.mushroomUP;

public class play_game implements Screen {

    public final MarioGame game;

    public String name_LVL;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    private Viewport viewport;
    public Map map;
    public Mario mario;
    public boolean marioIsBig;
    public boolean marioIsFire;
    private float time_render = 0f;

    public Scene scene;


    play_game (final MarioGame gam, String name_LVL){
        game=gam;
        this.name_LVL = name_LVL;
        marioIsBig = false;
        marioIsFire = false;



        create_world(name_LVL);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (scene.timer < 3){
            scene.start_game();
            batch.begin();
            batch.draw(scene.texture, Gdx.graphics.getWidth() / 2f - 12 * mario.getRATIO(), camera.position.y, 16 * mario.getRATIO(), 16  * mario.getRATIO());
            batch.end();

            scene.stage.act(delta);
            scene.stage.draw();
            return;
        }

        //time_render += delta;
        update(delta);
        //if (time_render > 1/60f) {

            camera.update();
            map.render();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            batch.draw(mario.getTexture(), mario.position.x, mario.position.y, mario.getWidth(), mario.getHeight());

            for (Goomba mash : map.goombas_array){
                batch.draw(mash.region, mash.position.x, mash.position.y, mash.width, mash.height);
            }

            for (mushroom mash : map.mushroom_array){
                batch.draw(mash.region, mash.position.x, mash.position.y, mash.width, mash.height);
            }

            for (Turtle turt : map.turtles_array){
                batch.draw(turt.region, turt.position.x, turt.position.y, turt.width, turt.height);
            }

            for (mushroomUP mash : map.mushroomUP_array){
                batch.draw(mash.region, mash.position.x, mash.position.y, mash.width, mash.height);
            }

            for (bullet bull : map.bullet_array){
                batch.draw(bull.region, bull.position.x, bull.position.y, bull.width, bull.height);
            }

            batch.end();

            batch.setProjectionMatrix(scene.stage.getCamera().combined);
            scene.stage.act(delta);
            scene.stage.draw();
            //time_render = 0f;
        //}
        if (mario.Endgame) next_world(name_LVL);
        if (mario.Endgame_dead) create_world(name_LVL);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.dispose();
    }

    private void update (float delta){

        mario.update(delta);
        map.update(delta);
        //camera.update();
    }

    public void create_world(String name_LVL){
        if (scene != null && map != null && mario != null) {
            scene.dispose();
            map.dispose();
            mario.dispose();
            batch.dispose();
            camera = null;
            viewport = null;
        }
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, MarioGame.WIDTH * game.ratioX, MarioGame.HEIGHT * game.ratioY);

        batch = new SpriteBatch();
        mario = new Mario(this,marioIsBig, marioIsFire);
        scene = new Scene(batch, mario, game);
        map = new Map(this,  name_LVL, camera);
        MapObjects objects = map.tiledMap.getLayers().get("mario").getObjects();
        mario.setXY((int)(((RectangleMapObject) objects.get(0)).getRectangle().getX() * game.ratioY), (int)(((RectangleMapObject) objects.get(0)).getRectangle().getY() * game.ratioY));
    }

    public void next_world(String nameLVL){
        if (scene != null && map != null && mario != null) {
            scene.dispose();
            map.dispose();
            mario.dispose();
            batch.dispose();
            camera = null;
            viewport = null;

        }
        name_LVL = nexWorld(nameLVL);
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        camera.setToOrtho(false, MarioGame.WIDTH * game.ratioX, MarioGame.HEIGHT * game.ratioY);

        batch = new SpriteBatch();
        mario = new Mario(this,marioIsBig, marioIsFire);
        scene = new Scene(batch, mario, game);
        map = new Map(this,  name_LVL, camera);
        MapObjects objects = map.tiledMap.getLayers().get("mario").getObjects();
        mario.setXY((int)(((RectangleMapObject) objects.get(0)).getRectangle().getX() * game.ratioY), (int)(((RectangleMapObject) objects.get(0)).getRectangle().getY() * game.ratioY));
    }

    String nexWorld (String a){
        if (a.contains("11")) {game.number_world++; return "tile/map12black.tmx"; }
        if (a.contains("12")) {game.number_world++; return "tile/map13.tmx";}
        if (a.contains("13")) {game.number_world++; return "tile/map14.tmx";}
        if (a.contains("14")) {game.number_world += 10 - 3; return "tile/map21.tmx";}
        if (a.contains("21")) {game.number_world++; return "tile/map22.tmx";}
        if (a.contains("22")) {game.number_world++; return "tile/map23.tmx";}
        if (a.contains("23")) {game.number_world++; return "tile/map24.tmx";}
        if (a.contains("24")) {game.number_world++; return "tile/map31.tmx";}
        if (a.contains("31")) {game.number_world += 10 - 3; return "tile/map32.tmx";}
        return "tile/map11.tmx";
    }
}

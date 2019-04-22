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
import com.mario.game.creatures.enemy.Goomba;

public class play_game implements Screen {

    public final MarioGame game;

    private SpriteBatch batch;
    public OrthographicCamera camera;
    private Viewport viewport;
    public Map map;
    public Mario mario;
    private float time_render = 0f;

    public Scene scene;


    play_game (final MarioGame gam, String name_LVL){
        game=gam;




        create_world(name_LVL);

        //Goomba.create_Goombas(map.goombas_array, this, mario);

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
            batch.draw(scene.texture, Gdx.graphics.getWidth() / 2f - 27, camera.position.y, 32, 32);
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

            batch.end();

            batch.setProjectionMatrix(scene.stage.getCamera().combined);
            scene.stage.act(delta);
            scene.stage.draw();
            //time_render = 0f;
        //}
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
        if (mario.Endgame)create_world("tile/map2.tmx");
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
        mario = new Mario(100,250, this);
        scene = new Scene(batch, mario, game);
        map = new Map(this,  name_LVL, camera);
        MapObjects objects = map.tiledMap.getLayers().get("mario").getObjects();
        mario.setXY((int)(((RectangleMapObject) objects.get(0)).getRectangle().getX() * game.ratioY), (int)(((RectangleMapObject) objects.get(0)).getRectangle().getY() * game.ratioY));
    }
}

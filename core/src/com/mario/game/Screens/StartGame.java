package com.mario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mario.game.HUD.Scene;
import com.mario.game.MarioGame;


public class StartGame implements Screen {

    private final MarioGame game;
    public Stage stage;
    private Scene scene;
    float time;


    TextureRegion texture;

    StartGame (final MarioGame gam, Scene ss){
        scene = ss;
        texture = new TextureRegion(new Texture("mario/little_mario.png"), 0, 0, 16, 16);
        game = gam;
        game.font.getData().setScale(2f);

        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.setFillParent(true);
        time = 0;

        table.add(scene.marioLabel).expandX().padTop(10);
        table.add(scene.worldLabel).expandX().padTop(10);
        table.add(scene.timeLabel).expandX().padTop(10);
        table.row();
        table.add(scene.scoreLabel).expandX();
        table.add(scene.levelLabel).expandX();
        table.add(scene.countdownLabel).expandX();



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (time > 5) {
            game.setScreen(game.menu_screen);
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
        //game.dispose();
    }
}

package com.mario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mario.game.MarioGame;


public class GameOver implements Screen {

    private final MarioGame game;
    public Stage stage;
    private Label gam_ov;
    float time;

    GameOver (final MarioGame gam){
        game = gam;
        stage = new Stage(new ScreenViewport());
        time = 0;
        game.font.getData().setScale(2f);
        gam_ov =new Label("Game over", new Label.LabelStyle(game.font, Color.WHITE));
        stage.addActor(gam_ov);

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

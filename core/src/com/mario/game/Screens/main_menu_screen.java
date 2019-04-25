package com.mario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mario.game.MarioGame;


public class main_menu_screen implements Screen {

    private final MarioGame game;
    public Stage stage;
    private Settings settings;

    public main_menu_screen(final MarioGame gam){
        game = gam;
        settings = new Settings(game);
        stage = new Stage(new ScreenViewport());
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        Table table = new Table();
        table.setFillParent(true);

        game.font.getData().setScale(1.5f * game.ratioY);
        textButtonStyle.font = game.font;
        TextButton play = new TextButton("PLAY", textButtonStyle);
        TextButton sett = new TextButton("SETTINGS", textButtonStyle);
        TextButton exit = new TextButton("EXIT", textButtonStyle);


        play.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new play_game(game, "tile/map11.tmx"));
                dispose();
            }
        });

        sett.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.input.setInputProcessor(settings.stage);
                game.setScreen(settings);
            }
        });

        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.dispose();
                Gdx.app.exit();
                dispose();
            }
        });

        table.add(play);
        table.row();
        table.add (sett).pad(30);
        table.row();
        table.add (exit);



        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        //Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
        game.dispose();
    }
}

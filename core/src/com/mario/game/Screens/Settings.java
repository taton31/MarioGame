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


public class Settings implements Screen {

    private final MarioGame game;
    //private final main_menu_screen menu_screen;
    public Stage stage;
    private TextButton music, exit;
    private TextButton.TextButtonStyle textButtonStyle;
    private Table table;
    private SpriteBatch batch;

    Settings (final MarioGame gam){
        game = gam;
        stage = new Stage(new ScreenViewport());
        textButtonStyle = new TextButton.TextButtonStyle();
        table = new Table();
        batch = new SpriteBatch();
        table.setFillParent(true);

        game.font.getData().setScale(2f);
        textButtonStyle.font = game.font;

        music = new TextButton(game.MUS_ON ? "MUSIC ON" : "MUSIC OFF", textButtonStyle);
        exit = new TextButton("<-", textButtonStyle);


        music.addListener(new ClickListener() {
                              @Override
                              public void clicked(InputEvent event, float x, float y) {
                                  if (game.MUS_ON) {
                                      game.MUS_ON = false;
                                      music.setText("MUSIC OFF");
                                  } else {
                                      game.MUS_ON = true;
                                      music.setText("MUSIC ON");
                                  }
                              }
                          });


        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.input.setInputProcessor(game.menu_screen.stage);
                game.setScreen(game.menu_screen);

            }
        });

        exit.setPosition(15, 15);
        table.add(music);

        stage.addActor(table);
        stage.addActor(exit);

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
        //game.dispose();
    }
}

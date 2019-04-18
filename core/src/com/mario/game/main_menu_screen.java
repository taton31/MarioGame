package com.mario.game;

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


public class main_menu_screen implements Screen {

    private final MarioGame game;
    private Stage stage;
    private TextButton play, exit;
    private TextButton.TextButtonStyle textButtonStyle;
    private Table table;
    private SpriteBatch batch;

    main_menu_screen(final MarioGame gam){
        game = gam;

        stage = new Stage(new ScreenViewport());
        textButtonStyle = new TextButton.TextButtonStyle();
        table = new Table();
        batch = new SpriteBatch();
        table.setFillParent(true);

        game.font.getData().setScale(2f);
        textButtonStyle.font = game.font;
        play = new TextButton("PLAY", textButtonStyle);
        exit = new TextButton("EXIT", textButtonStyle);


        play.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                game.setScreen(new play_game(game));
                dispose();
            }
        });

        exit.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                game.dispose();
                Gdx.app.exit();
                dispose();
            }
        });

        table.add(play).width(100).height(100);
        table.row();
        table.add (exit);



        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);

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

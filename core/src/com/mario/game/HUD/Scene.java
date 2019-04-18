package com.mario.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.creatures.Mario;

public class Scene implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private final Mario mario;

    ////////
    public test_desktop testDesktop;

    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    private float RATIO;
    private Button up, left, right, down, fire;


    public Scene (SpriteBatch batch, final Mario mario){
        RATIO = mario.RATIO;
        this.mario = mario;

        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new ScreenViewport( new OrthographicCamera());
        stage = new Stage(viewport, batch);


        Table table = new Table();
        table.top();
        table.setFillParent(true);


        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel =new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        Texture texture = new Texture("move_button/atlas_button.png");
        right = new Button(new TextureRegionDrawable(new TextureRegion(texture, 48, 0, 16, 16)));
        right.setPosition(Gdx.app.getGraphics().getWidth() - (8 + 30) * RATIO,8 * RATIO);
        right.setSize(32 * RATIO,32 * RATIO);
        left = new Button(new TextureRegionDrawable(new TextureRegion(texture, 32, 0, 16, 16)));
        left.setPosition(Gdx.app.getGraphics().getWidth() - 2 * (8 + 30) * RATIO, 8 * RATIO);
        left.setSize(32 * RATIO,32 * RATIO);
        up = new Button(new TextureRegionDrawable(new TextureRegion(texture, 64, 0, 16, 16)));
        up.setPosition(8 * RATIO, (16 + 30) * RATIO);
        up.setSize(32 * RATIO,32 * RATIO);
        down = new Button(new TextureRegionDrawable(new TextureRegion(texture, 0, 0, 16, 16)));
        down.setPosition(8 * RATIO,8 * RATIO);
        down.setSize(32 * RATIO,32 * RATIO);
        fire = new Button(new TextureRegionDrawable(new TextureRegion(texture, 16, 0, 16, 16)));
        fire.setPosition(8 * RATIO,(24 + 2 * 30) * RATIO);
        fire.setSize(32 * RATIO,32 * RATIO);
        fire.setVisible(false);

        right.addListener(new ClickListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                mario.press_right();
                return true;
            }

            @Override
            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {
                mario.unpress_button();
            }
        });
        left.addListener(new ClickListener(){
            @Override
            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {
                mario.press_left();
                return true;
            }

            @Override
            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {
                mario.unpress_button();
            }
        });
        up.addListener(new ClickListener(){
            @Override
            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {
                mario.press_up();
                return true;
            }

            @Override
            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {
                mario.unpress_button_up();
            }
        });
        down.addListener(new ClickListener(){
            @Override
            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {
                mario.press_down();
                return true;
            }

            @Override
            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {
                mario.unpress_button();
            }
        });
        fire.addListener(new ClickListener(){
            @Override
            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {
                mario.press_fire();
                return true;
            }
        });
        stage.addActor(table);
        stage.addActor(right);
        stage.addActor(left);
        stage.addActor(up);
        stage.addActor(down);
        stage.addActor(fire);
        testDesktop = new test_desktop(stage, mario);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

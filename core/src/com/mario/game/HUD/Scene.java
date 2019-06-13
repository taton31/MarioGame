package com.mario.game.HUD;import com.badlogic.gdx.Gdx;import com.badlogic.gdx.graphics.Color;import com.badlogic.gdx.graphics.OrthographicCamera;import com.badlogic.gdx.graphics.Texture;import com.badlogic.gdx.graphics.g2d.Batch;import com.badlogic.gdx.graphics.g2d.BitmapFont;import com.badlogic.gdx.graphics.g2d.SpriteBatch;import com.badlogic.gdx.graphics.g2d.Animation;import com.badlogic.gdx.graphics.g2d.TextureRegion;import com.badlogic.gdx.scenes.scene2d.Actor;import com.badlogic.gdx.scenes.scene2d.InputEvent;import com.badlogic.gdx.scenes.scene2d.Stage;import com.badlogic.gdx.scenes.scene2d.ui.Button;import com.badlogic.gdx.scenes.scene2d.ui.Label;import com.badlogic.gdx.scenes.scene2d.ui.Table;import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;import com.badlogic.gdx.utils.Disposable;import com.badlogic.gdx.utils.viewport.ScreenViewport;import com.badlogic.gdx.utils.viewport.Viewport;import com.mario.game.MarioGame;import com.mario.game.creatures.Mario.Mario;public class Scene implements Disposable {    public Stage stage;    private Viewport viewport;    private final Mario mario;    private final MarioGame game;    ////////    public test_desktop testDesktop;    private Integer worldTimer;    private boolean timeUp; // true when the world timer reaches 0    private float timeCount;    private static Integer score;    public float timer;    private int sizeButton;    private Label countdownLabel;    private Label scoreLabel;    private Label countcoinsLabel;    private Label timeLabel;    private Label levelLabel;    private Label worldLabel;    private Label marioLabel;    private Label marioHealth;    private Label numberWorld;    public TextureRegion texture;    public class coin extends Actor {        Animation<TextureRegion> countcoinsAnimation;        float stateTimer;        coin(){            Texture texture = new Texture("coins.png");            TextureRegion[] coinsAnim = new TextureRegion[4];            coinsAnim[0] = new TextureRegion(texture,0,0,16,16);            coinsAnim[1] = new TextureRegion(texture,18,0,16,16);            coinsAnim[2] = new TextureRegion(texture,36,0,16,16);            coinsAnim[3] = new TextureRegion(texture,18,0,16,16);            countcoinsAnimation = new Animation<TextureRegion>(0.3f, coinsAnim);        }        @Override        public void draw(Batch batch, float alpha){            stateTimer = (stateTimer % 100000) + Gdx.graphics.getDeltaTime();            batch.draw(countcoinsAnimation.getKeyFrame(stateTimer, true),this.getX(),getY());        }    }    private coin countCoins;    public Button up, left, right, down, fire;    public Scene (SpriteBatch batch, final Mario mario, final MarioGame ga){        sizeButton = (int) (8 * mario.getRATIO());        timer = 0;        game = ga;        float RATIO = mario.getRATIO();        this.mario = mario;        texture = new TextureRegion(new Texture("mario/little_mario.png"), 0, 0, 16, 16);        worldTimer = 300;        timeCount = 0;        score = 0;        viewport = new ScreenViewport( new OrthographicCamera());        stage = new Stage(viewport, batch);        countCoins = new coin();        Table table = new Table();        table.top();        table.setFillParent(true);        game.font.getData().setScale(0.8f * game.ratioY);        marioHealth = new Label(String.format("    X   %d", game.mario_health), new Label.LabelStyle(game.font, Color.WHITE));        marioHealth.setPosition(Gdx.graphics.getWidth() / 2f - 4 * game.ratioY, Gdx.graphics.getHeight() / 2f - 2 * game.ratioY);        numberWorld = new Label(String.format("WORLD %d - %d", game.number_world / 10, game.number_world % 10), new Label.LabelStyle(game.font, Color.WHITE));        numberWorld.setPosition(Gdx.graphics.getWidth() / 2f - 25 * game.ratioY, Gdx.graphics.getHeight() / 2f + 20 * game.ratioY);        stage.addActor(marioHealth);        stage.addActor(numberWorld);        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(game.font, Color.WHITE));        countcoinsLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(game.font, Color.WHITE));        scoreLabel =new Label(String.format("%06d", score), new Label.LabelStyle(game.font, Color.WHITE));        timeLabel = new Label("TIME", new Label.LabelStyle(game.font, Color.WHITE));        levelLabel = new Label("1-1", new Label.LabelStyle(game.font, Color.WHITE));        worldLabel = new Label("WORLD", new Label.LabelStyle(game.font, Color.WHITE));        marioLabel = new Label("MARIO", new Label.LabelStyle(game.font, Color.WHITE));        table.add(marioLabel).expandX().padTop(10);        table.add().expandX().padTop(10);        table.add(worldLabel).expandX().padTop(10);        table.add(timeLabel).expandX().padTop(10);        table.row();        table.add(scoreLabel).expandX();        table.add(countCoins).expandX();        table.add(levelLabel).expandX();        table.add(countdownLabel).expandX();        Texture texture = new Texture("move_button/atlas_button.png");        right = new Button(new TextureRegionDrawable(new TextureRegion(texture, 48, 0, 16, 16)));        right.setPosition(Gdx.app.getGraphics().getWidth() - (8 + sizeButton) * RATIO,8 * RATIO);        right.setSize(sizeButton * RATIO,sizeButton * RATIO);        left = new Button(new TextureRegionDrawable(new TextureRegion(texture, 32, 0, 16, 16)));        left.setPosition(Gdx.app.getGraphics().getWidth() - 2 * (8 + sizeButton) * RATIO, 8 * RATIO);        left.setSize(sizeButton * RATIO,sizeButton * RATIO);        up = new Button(new TextureRegionDrawable(new TextureRegion(texture, 64, 0, 16, 16)));        up.setPosition(8 * RATIO, (16 + sizeButton) * RATIO);        up.setSize(sizeButton * RATIO,sizeButton * RATIO);        down = new Button(new TextureRegionDrawable(new TextureRegion(texture, 0, 0, 16, 16)));        down.setPosition(8 * RATIO,8 * RATIO);        down.setSize(sizeButton * RATIO,sizeButton * RATIO);        fire = new Button(new TextureRegionDrawable(new TextureRegion(texture, 16, 0, 16, 16)));        fire.setPosition(8 * RATIO,(24 + 2 * sizeButton) * RATIO);        fire.setSize(sizeButton * RATIO,sizeButton * RATIO);        right.addListener(new ClickListener(){            @Override            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {                mario.press_right();                return true;            }            @Override            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {                mario.unpress_button();            }        });        left.addListener(new ClickListener(){            @Override            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {                mario.press_left();                return true;            }            @Override            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {                mario.unpress_button();            }        });        up.addListener(new ClickListener(){            @Override            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {                mario.press_up();                return true;            }            @Override            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {                mario.unpress_button_up();            }        });        down.addListener(new ClickListener(){            @Override            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {                mario.press_down();                return true;            }            @Override            public void touchUp (InputEvent event,float x, float y, int pointer, int button) {                mario.unpress_button_down();            }        });        fire.addListener(new ClickListener(){            @Override            public boolean touchDown (InputEvent event,float x, float y, int pointer, int button) {                mario.press_fire();                return true;            }        });        fire.setVisible(mario.marioIsFire);        right.setVisible(false);        left.setVisible(false);        up.setVisible(false);        down.setVisible(false);        stage.addActor(table);        stage.addActor(right);        stage.addActor(left);        stage.addActor(up);        stage.addActor(down);        stage.addActor(fire);        ///////////////////////////////////////////////////////////////////////////////////////////////////        //testDesktop = new test_desktop(stage, mario);        Gdx.input.setInputProcessor(stage);    }    @Override    public void dispose() {        game.gameMusic.stop();        stage.dispose();    }    public void start_game (){        timer += Gdx.graphics.getDeltaTime();        if (timer > 3 ) {            //game.gameMusic.setVolume(game.MUS_ON / 100f);            game.gameMusic.play();            numberWorld.setVisible(false);            marioHealth.setVisible(false);            right.setVisible(true);            left.setVisible(true);            up.setVisible(true);            down.setVisible(true);        } else {            numberWorld.setText(String.format("WORLD %d - %d", game.number_world / 10, game.number_world % 10));            marioHealth.setText(String.format("    X   %d", game.mario_health));            numberWorld.setVisible(true);            marioHealth.setVisible(true);            right.setVisible(false);            left.setVisible(false);            up.setVisible(false);            down.setVisible(false);        }    }}
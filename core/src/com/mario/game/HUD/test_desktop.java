package com.mario.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mario.game.creatures.Mario.Mario;

public class test_desktop {
    Stage stage;
    Table table;
    Mario mario;
    private Label velocity_start;
    private Label velocity_jump;
    private Label max_velocity;
    private Label acceleration_G;
    private Label koff_accL;

    private TextField vel_start;
    private TextField vel_jump;
    private TextField max_vel;
    private TextField accel_G;
    private TextField koff_acc;


    private TextField.TextFieldStyle style = new TextField.TextFieldStyle();
    BitmapFont font;
    float RATIO;

    private Button turn_input;
    private boolean from_keyboard = true;


    public test_desktop (Stage sc, Mario mar){
        stage = sc;
        mario = mar;
        RATIO = mario.getRATIO();
        table = new Table();
        table.top();
        table.setFillParent(true);

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        style.font = font;
        style.fontColor = Color.BLACK;

        velocity_start = new Label("velocity_start", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        velocity_jump = new Label("velocity_jump", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        max_velocity = new Label("max_velocity", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        acceleration_G = new Label("acceleration_G", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        koff_accL = new Label("acceleration_G", new Label.LabelStyle(new BitmapFont(), Color.BLACK));



        vel_start = new TextField(String.valueOf( ( 200 )), style);
        vel_jump  = new TextField(String.valueOf( ( 250) ), style);
        max_vel = new TextField(String.valueOf( ( 100 )), style);
        accel_G = new TextField(String.valueOf( ( 800 )), style);
        koff_acc = new TextField(String.valueOf( 0.3), style);

        Texture texture = new Texture("mario/little_mario/mario_die.png");
        turn_input = new Button(new TextureRegionDrawable(new TextureRegion(texture, 0, 0, 16, 16)));
        turn_input.setPosition(Gdx.app.getGraphics().getWidth() - (20) * RATIO,Gdx.app.getGraphics().getHeight() - 70 * RATIO);
        turn_input.setSize(16 * RATIO,16 * RATIO);

        turn_input.addListener(new ClickListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                from_keyboard = !from_keyboard;
                return true;
            }
        });

        table.add(velocity_start).expandX().padTop(30 * RATIO);
        table.add(velocity_jump).expandX().padTop(30 * RATIO);
        table.add(max_velocity).expandX().padTop(30 * RATIO);
        table.add(acceleration_G).expandX().padTop(30 * RATIO);
        table.add(koff_accL).expandX().padTop(30 * RATIO);
        table.row();
        table.add(vel_start).expandX();
        table.add(vel_jump).expandX();
        table.add(max_vel).expandX();
        table.add(accel_G).expandX();
        table.add(koff_acc).expandX();


        stage.addActor(turn_input);
        stage.addActor(table);
    }

    public void update (){
        mario.set_vel_start((int) (Float.parseFloat(vel_start.getText()) * RATIO));
        mario.set_vel_jump((int) (Float.parseFloat(vel_jump.getText()) * RATIO));
        mario.set_max_vel((int) (Float.parseFloat(max_vel.getText()) * RATIO));
        mario.set_acceleration_G((int) (Float.parseFloat(accel_G.getText()) * RATIO));
        mario.set_koff_acc((Float.parseFloat(koff_acc.getText())));

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mario.press_left();
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mario.press_right();
        } else if (from_keyboard){
            mario.unpress_button();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            mario.press_up();
        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            mario.press_down();
        } else {
            //mario.unpress_button_up();
        }
    }
}

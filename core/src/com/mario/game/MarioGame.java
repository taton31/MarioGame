package com.mario.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mario.game.Screens.main_menu_screen;


public class MarioGame extends Game {
	public BitmapFont font;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 208;
	public int MUS_ON = 100;
	public int mario_health;
	public int number_world;
	public main_menu_screen menu_screen;
	public Music gameMusic;


	public float ratioX, ratioY;
	@Override
	public void create() {
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mariomusic/mario_music.ogg"));
		gameMusic.setLooping(true);

		mario_health = 3;
		number_world = 11;
		font = new BitmapFont();
		ratioX = (float) Gdx.app.getGraphics().getWidth() / WIDTH;
		ratioY = (float) Gdx.app.getGraphics().getHeight() / HEIGHT;
		menu_screen = new main_menu_screen(this);
		this.setScreen(menu_screen);
	}

	@Override
	public void render() {
		super.render();
	}
}

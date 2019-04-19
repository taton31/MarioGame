package com.mario.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mario.game.Screens.main_menu_screen;


public class MarioGame extends Game {
	public BitmapFont font;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 208;
	public boolean MUS_ON = true;
	public main_menu_screen menu_screen;

	public float ratioX, ratioY;
	@Override
	public void create() {
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

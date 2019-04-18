package com.mario.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;



public class MarioGame extends Game {
	BitmapFont font;
	static final int WIDTH = 400;
	static final int HEIGHT = 208;

	public float ratioX, ratioY;
	@Override
	public void create() {
		font = new BitmapFont();
		ratioX = (float) Gdx.app.getGraphics().getWidth() / WIDTH;
		ratioY = (float) Gdx.app.getGraphics().getHeight() / HEIGHT;
		this.setScreen(new main_menu_screen(this));
	}

	@Override
	public void render() {
		super.render();
	}
}

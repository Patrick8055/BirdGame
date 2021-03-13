package com.patrickharsch.birdgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.patrickharsch.birdgame.gamescreens.MenuScreen;

public class BirdGame extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 860;
	public static final String TITLE = "Good Title Soon...";


	public SpriteBatch batch;
	Texture texture;
	public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("Fonts/vcr-osd-mono.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("Fonts/vcr-osd-mono.fnt"), new TextureRegion(texture), false);

		Gdx.gl.glClearColor(0, 1, 0, 1);
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
		font.dispose();
	}
}

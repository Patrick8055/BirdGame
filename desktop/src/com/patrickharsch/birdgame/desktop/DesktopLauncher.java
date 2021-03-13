package com.patrickharsch.birdgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.patrickharsch.birdgame.BirdGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BirdGame.WIDTH;
		config.height = BirdGame.HEIGHT;
		config.title = BirdGame.TITLE;
		new LwjglApplication(new BirdGame(), config);
	}
}

package com.packtpub.libgdx.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;

/**
 * Creates the instance of the game.
 * @author Jacob, Drake, Christian
 */
public class CanyonBunnyMain extends Game {

	/**
	 * On creations, instantiate the assets, music, set the screen,
	 * and set the log level.
	 */
	@Override
	public void create() {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// Load assets
		Assets.instance.init(new AssetManager());
		
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
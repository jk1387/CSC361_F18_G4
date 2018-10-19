/*
 * Author: Christian Crouthamel
 * Description: Abstract class for game screen
 */

package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.badlogic.gdx.graphics.g2d.Animation;

/*
 * abstract class to create methods for the game screen to use 
 */
public abstract class AbstractGameScreen implements Screen {
	protected Game game;
	// state time of the animation
	public float stateTime;
	// current set animation
	public Animation animation;

	public AbstractGameScreen(Game game) {
		this.game = game;
	}
	
	/**
	 * Change the current animation and set the state time
	 * to 0.
	 * @param animation the current animation
	 */
	public void setAnimation (Animation animation) {
		this.animation = animation;
		stateTime = 0;
	}
	
	/**
	 * Increases the state time, which allows the animation to
	 * run.
	 * @param deltaTime
	 */
	public void update (float deltaTime) {
		stateTime += deltaTime;
	}

	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void show();

	public abstract void hide();

	public abstract void pause();

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 * resume and dispose methods for the asset manager
	 */
	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	public void dispose() {
		Assets.instance.dispose();
	}
}
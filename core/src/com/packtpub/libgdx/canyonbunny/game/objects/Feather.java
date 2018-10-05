package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;

/**
 * The feather object allows the bunnyhead to jump infinitely (fly)
 * when collected
 * @author Jacob Kole
 */
public class Feather extends AbstractGameObject {
	
	// texture region for the feather
	private TextureRegion regFeather;
	
	// has the bunnyhead collided with the feather
	public boolean collected;
	
	/**
	 * Initialize the feather.
	 */
	public Feather () {
		init();
	}
	
	/**
	 * Initialize the dimension and image for the feather.
	 * Make sure its bound box is set for when the bunnyhead
	 * collides with the feather.
	 */
	private void init () {
		// sets the size of the feather
		dimension.set(0.5f, 0.5f);
		// grabs the image for the feather
		regFeather = Assets.instance.feather.feather;
		
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		
		// flag for item collection
		collected = false;
	}
	
	/**
	 * Renders the feather into the game world.
	 */
	public void render (SpriteBatch batch) {
		// if it's collected, don't render
		if (collected) return;
		
		// reset the texture region
		TextureRegion reg = null;
		reg = regFeather;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	public int getScore() {
		return 250;
	}
}

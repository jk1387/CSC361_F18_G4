/*
 * Author: Christian Crouthamel
 * Description: Carrot class
 */

package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;
	
public class Carrot extends AbstractGameObject {
	
		//instance variables
		private TextureRegion regCarrot;
	
		/*
		 * carrot init constructor
		 */
		public Carrot () {
			init();
		}	
	
		/*
		 * init method for carrot
		 */
		private void init () {
		
		dimension.set(0.25f, 0.5f);
		regCarrot = Assets.instance.levelDecoration.carrot;
		
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		origin.set(dimension.x / 2, dimension.y / 2);
	}
		
		/*
		 * (non-Javadoc)
		 * @see com.packtpub.libgdx.canyonbunny.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
		 * render method for the carrot
		 */
		public void render (SpriteBatch batch) {
				TextureRegion reg = null;
				reg = regCarrot;
				batch.draw(reg.getTexture(), position.x - origin.x,
						position.y - origin.y, origin.x, origin.y, dimension.x,
						dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
						reg.getRegionY(), reg.getRegionWidth(),
						reg.getRegionHeight(), false, false);
				}
}
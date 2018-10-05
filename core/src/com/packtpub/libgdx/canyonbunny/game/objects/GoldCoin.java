package com.packtpub.libgdx.canyonbunny.game.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;
/*
 * Author: Drake Conaway
 */
public class GoldCoin extends AbstractGameObject {
	private TextureRegion regGoldCoin;
	public boolean collected;
	
	/*
	 * Constructor for the GoldCoin object
	 */
    public GoldCoin(){
    	init();
    }
    /*
     * Initialization method for gold coin, set
     * dimensionsa nd select assets
     */
    private void init(){
    	dimension.set(0.5f, 0.5f);
    	regGoldCoin = Assets.instance.goldCoin.goldCoin;
    	 // Set bounding box for collision detection
    	bounds.set(0,0,dimension.x, dimension.y);
    	collected = false;
    }
    /*
     * Render method for the gold coin object
     */
    public void render (SpriteBatch batch){
    	if(collected) return;
    	
    	TextureRegion reg = null; //init reg var
    	reg = regGoldCoin; //set reg var
    	batch.draw(reg.getTexture(), position.x, position.y,
    			origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
    			rotation, reg.getRegionX(), reg.getRegionY(),
    			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
    /*
     * Get score method, returns score
     */
    public int getScore(){
    	return 100;
    }
}

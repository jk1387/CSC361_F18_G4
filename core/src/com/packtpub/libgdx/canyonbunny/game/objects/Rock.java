package com.packtpub.libgdx.canyonbunny.game.objects;
/*
 * Author: Drake Conaway
 */
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;
public class Rock extends AbstractGameObject {
private TextureRegion regEdge;
private TextureRegion regMiddle;

private int length;

public Rock() {
	init();
}
private void init() {
	dimension.set(1,1.5f);
	regEdge = Assets.instance.rock.edge;//set edge assets
	regMiddle = Assets.instance.rock.middle; //set middle assets
	//Start length of this rock
	setLength(1);
}
public void setLength(int length) {
	this.length = length; //set rock length to val
}
public void increaseLength(int amount) {
	setLength(length + amount); //increase rock length
	
}
}

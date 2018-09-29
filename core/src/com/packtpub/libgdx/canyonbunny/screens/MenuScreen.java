package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuScreen extends AbstractGameScreen {
	
	
	
	
	
	
	/**
	 * Anchored in top-left corner of the screen. This adds
	 * an image logo to that position.
	 * @return
	 */
	private Table buildLogosLayer () {
		Table layer = new Table();
		layer.left().top();
		// + Game Logo
		imgLogo = new Image(skinCanyonBunny, "logo");
		layer.add(imgLogo);
		layer.row().expandY();
		// + Info Logos
		imgInfo = new Image(skinCanyonBunny, "info");
		layer.add(imgInfo).bottom();
		if (debugEnabled) layer.debug();
		return layer;
	}
}

package com.packtpub.libgdx.canyonbunny.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuScreen extends AbstractGameScreen {
	
	
	
	
	
	
	
	
	
	
	/**
	 * Builds the controls behind the buttons in the game's
	 * menu screen. The "play" button and "options" button
	 * are given functionality here using ChangeListener.
	 * @return the table layer for menu controls
	 */
	private Table buildControlsLayer () {
		Table layer = new Table();
		layer.right().bottom();
		// + Play Button
		btnMenuPlay = new Button(skinCanyonBunny, "play");
		layer.add(btnMenuPlay);
		btnMenuPlay.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onPlayClicked();
			}
		});
		layer.row();
		// + Options Button
		btnMenuOptions = new Button(skinCanyonBunny, "options");
		layer.add(btnMenuOptions);
		btnMenuOptions.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onOptionClicked();
			}
		});
		if (debugEnabled) layer.debug();
		return layer;
	}
	
	/**
	 * Anchored in top-left corner of the screen. This adds
	 * an image logo to that position.
	 * @return the table layer for logos
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
	
	/**
	 * Sets the game screen when the play button
	 * is clicked.
	 */
	private void onPlayClicked() {
		game.setScreen(new GameScreen(game));
	}
	
	private void onOptionsClicked () { }
}

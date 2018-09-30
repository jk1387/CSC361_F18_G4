package com.packtpub.libgdx.canyonbunny.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/*
 * Author: Drake Conaway
 */
public class MenuScreen extends AbstractGameScreen {
 private final String TAG = MenuScreen.class.getName();
 private Stage stage;
 private Skin skinCanyonBunny;
 //menu
 private Image imgBackground;
 private Image imgLogo;
 private Image imgInfo;
 private Image imgCoins;
 private Image imgBunny;
 private Button btnMenuPlay;
 private Button btnMenuOptions;
 //options
 private Window winOptions;
 private TextButton btnWinOptSave;
 private TextButton btnWinOptCancel;
 private CheckBox chkSound;
 private Slider sldSound;
 private CheckBox chkMusic;
 private SelectBox<CharacterSkin> selCharSkin;
 private Image imgCharSkin;
 private CheckBox chkShowFpsCounter;
 //debug
 private final float DEBUG_REBUILD_INTERVAL = 5.0f;
 private boolean debugEnabled = false;
 private float debugRebuildStage;
 /*
  * Menu screen constructor
  */
 public MenuScreen(Game game){
	 super(game);
 }
 /*
  * Method to rebuild the stage/
  * select the skin from texture atlas
  */
 private void rebuildStage(){
	 skinCanyonBunny = new Skin(
			 Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI),
			 new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
	 //build all layers
	 Table layerBackground = buildBackgroundLayer();
	 Table layerObjects = buildObjectsLayer();
	 Table layerLogos = buildLogosLayer();
	 Table layerControls = buildControlsLayer();
	 Table layerOptionsWindow = buildOptionsWindowLayer();
	 
	 //assemble stage for menu screen
	 stage.clear();
	 Stack stack = new Stack();
	 stage.addActor(stack);
	 stage.setSize(Constants.VIEWPORT_GUI_WIDTH,
			 Constants.VIEWPORT_GUI_HEIGHT);
	 stack.add(layerBackground);
	 stack.add(layerObjects);
	 stack.add(layerLogos);
	 stack.add(layerControls);
	 stage.addActor(layerOptionsWindow);
 }
 /*
  * Render method for the 
  * game screens
  */
 @Override
 public void render(float deltaTime){
	 Gdx.g1.g1ClearColor(0.0f,0.0f,0.0f,1.0f);
	 Gdx.g1.g1Clear(GL20.GL_COLOR_BUFFER_BIT);
	 if(Gdx.input.isTouched())
		 Game.setScreen(new GameScreen(game));
 }
 @Override public void resize(int width, int height){}
 @Override public void show(){}
 @Override public void hide(){}
 @Override public void pause(){}
 
 
	
	
	
	
	
	
	
	
	
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

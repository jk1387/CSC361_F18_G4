package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.packtpub.libgdx.canyonbunny.CanyonBunnyMain;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher {
	
	//private static boolean for atlas and outline
	private static boolean rebuildAtlas = false; // rebuilds map. Switch to true if the way the atlas is drawn is changed
	private static boolean drawDebugOutline = false; // turn on to see pink lines
	
public static void main (String[] arg) {
		
		
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;

			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images","canyonbunny.atlas");
			
			}
			
			LwjglApplicationConfiguration cfg = new
			LwjglApplicationConfiguration();
			cfg.title = "CanyonBunny";
			cfg.width = 800;
			cfg.height = 480;
			new LwjglApplication(new CanyonBunnyMain(), cfg);
			}
}
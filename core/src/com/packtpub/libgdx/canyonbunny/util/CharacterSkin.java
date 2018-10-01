/*
 * Author: Christian Crouthamel
 * Description: Enum class for character skins
 */

package com.packtpub.libgdx.canyonbunny.util;

import com.badlogic.gdx.graphics.Color;

public enum CharacterSkin {
	
	WHITE("White", 1.0f, 1.0f, 1.0f),
	GRAY("Gray", 0.7f, 0.7f, 0.7f),
	BROWN("Brown", 0.7f, 0.5f, 0.3f);
	private String name;
	private Color color = new Color();
}
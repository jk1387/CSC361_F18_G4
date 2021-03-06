//Christian Crouthamel

package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.packtpub.libgdx.canyonbunny.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.packtpub.libgdx.canyonbunny.util.CharacterSkin;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * The player object. This has the most interactions across
 * all other objects in the game.
 * @author Christian, Drake, Jacob
 */
public class BunnyHead extends AbstractGameObject {
	
	public ParticleEffect dustParticles = new ParticleEffect();
	// Animation states
	private Animation<TextureRegion> animNormal;
	private Animation<TextureRegion> animCopterTransform;
	private Animation<TextureRegion> animCopterTransformBack;
	private Animation<TextureRegion> animCopterRotate;
	
	//Chapter 6
	public static final String TAG = BunnyHead.class.getName();
	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING =
	JUMP_TIME_MAX - 0.018f;
	
	public enum VIEW_DIRECTION { LEFT, RIGHT }
	public enum JUMP_STATE {GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING}
	
	private TextureRegion regHead;
	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFeatherPowerup;
	public float timeLeftFeatherPowerup;
	
	public BunnyHead () {
	      init();
	    }
	
	public void init () {
		
		//chapter 6
		dimension.set(1, 1);
		
		animNormal = Assets.instance.bunny.animNormal;
		animCopterTransform = Assets.instance.bunny.animCopterTransform;
		animCopterTransformBack = Assets.instance.bunny.animCopterTransformBack;
		animCopterRotate = Assets.instance.bunny.animCopterRotate;
		setAnimation(animNormal);
		
		regHead = Assets.instance.bunny.head;
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);
		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;
		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;
		// Power-ups
		hasFeatherPowerup = false;
		timeLeftFeatherPowerup = 0;
		// Particles
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"),
		Gdx.files.internal("particles"));
	};
	
	public void setJumping (boolean jumpKeyPressed) {
		
		//chapter 6
		switch (jumpState) {
		case GROUNDED: // Character is standing on a platform
		if (jumpKeyPressed) {
		AudioManager.instance.play(Assets.instance.sounds.jump);
		// Start counting jump time from the beginning
		timeJumping = 0;
		jumpState = JUMP_STATE.JUMP_RISING;
		}
		break;
		case JUMP_RISING: // Rising in the air
		if (!jumpKeyPressed)
		jumpState = JUMP_STATE.JUMP_FALLING;
		break;
		case FALLING:// Falling down
		case JUMP_FALLING: // Falling down after jump
			
		     if (jumpKeyPressed && hasFeatherPowerup) {
		    	 // jump with feather has a randomized pitch
		    	AudioManager.instance.play(
		    			Assets.instance.sounds.jumpWithFeather, 1,
		    			MathUtils.random(1.0f, 1.1f));
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
				}
		break;
		}
	};
	
	public void setFeatherPowerup (boolean pickedUp) {
		
		//chapter 6
		hasFeatherPowerup = pickedUp;
		if (pickedUp) {
		timeLeftFeatherPowerup =Constants.ITEM_FEATHER_POWERUP_DURATION;
		}
	};
	
	public boolean hasFeatherPowerup () {
		//chapter 6
		return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
	};
	
	@Override
	public void update (float deltaTime) {
	super.update(deltaTime);
	if (velocity.x != 0) {
	viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT :
	VIEW_DIRECTION.RIGHT;
	}
	if (timeLeftFeatherPowerup > 0) {
	timeLeftFeatherPowerup -= deltaTime;
	
	    if (timeLeftFeatherPowerup < 0) {
	    	if (animation == animCopterTransformBack) {
	    		// Restart "Transform" animation if another feather power-up
	    		// was picked up during "TransformBack" animation. Otherwise,
	    		// power-up is still active.
	    		setAnimation(animCopterTransform);
	    	}
	         // disable power-up
		     timeLeftFeatherPowerup = 0;
		     setFeatherPowerup(false);
		     setAnimation(animCopterTransformBack);
	         }
	  }	
	dustParticles.update(deltaTime);
	
	// Change animation state according to feather power-up
	if (hasFeatherPowerup) {
		if (animation == animNormal) {
			setAnimation(animCopterTransform);
		} else if (animation == animCopterTransform) {
			if (animation.isAnimationFinished(stateTime))
				setAnimation(animCopterRotate);
		}
	} else {
		if (animation == animCopterRotate) {
			if (animation.isAnimationFinished(stateTime))
				setAnimation(animCopterTransformBack);
		} else if (animation == animCopterTransformBack) {
			if (animation.isAnimationFinished(stateTime))
				setAnimation(animNormal);
		}
	}
   }
	
	
	@Override
	protected void updateMotionY (float deltaTime) {
	switch (jumpState) {
	case GROUNDED:
	jumpState = JUMP_STATE.FALLING;
	//if statement for dust particles
	if (velocity.x != 0) {
		dustParticles.setPosition(position.x + dimension.x / 2,
		position.y);
		dustParticles.start();
		}
	break;
	case JUMP_RISING:
	// Keep track of jump time
	timeJumping += deltaTime;
	// Jump time left?
	if (timeJumping <= JUMP_TIME_MAX) {
	// Still jumping
	velocity.y = terminalVelocity.y;
	}
	
	break;
	
	case FALLING:
	
	break;
	
	case JUMP_FALLING:
	// Add delta times to track jump time
	timeJumping += deltaTime;
	// Jump to minimal height if jump key was pressed too short
	if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
	// Still jumping
	velocity.y = terminalVelocity.y;
	}
	}
	if (jumpState != JUMP_STATE.GROUNDED)
		dustParticles.allowCompletion();
		super.updateMotionY(deltaTime);
	}
	
	@Override
	public void render (SpriteBatch batch) {
	TextureRegion reg = null;
	
	// Draw Particles
	dustParticles.draw(batch);
	
	// Apply Skin Color
	batch.setColor(
			CharacterSkin.values()[GamePreferences.instance.charSkin]
					.getColor());
	
	// correcting values to the width and height for rendering
	float dimCorrectionX = 0;
	float dimCorrectionY = 0;
	if (animation != animNormal) {
		dimCorrectionX = 0.05f;
		dimCorrectionY = 0.2f;
	}
	
	// Set special color when game object has a feather power-up
	//if (hasFeatherPowerup) {
	//batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
	//}
	
	// Draw image
	//reg = regHead;
	reg = animation.getKeyFrame(stateTime, true);
	
	batch.draw(reg.getTexture(), position.x, position.y, origin.x,
	origin.y, dimension.x + dimCorrectionX, dimension.y + dimCorrectionY, 
	scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
	reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
	false);
	// Reset color to white
	batch.setColor(1, 1, 1, 1);
	}
}
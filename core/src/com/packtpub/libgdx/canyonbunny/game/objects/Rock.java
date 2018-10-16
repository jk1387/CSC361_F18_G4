package com.packtpub.libgdx.canyonbunny.game.objects;

// rock assets
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.canyonbunny.game.Assets;

// used for rock movements
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Rocks are the platforms the BunnyHead stands and jumps on. These are the
 * objects in the map the bunny cannot pass through, and hitting the sides and
 * tops of these rocks have special collision results. They also move up and
 * down at set intervals, starting at random times upon rendering.
 * 
 * @author Drake Conaway, Jacob Kole
 */
public class Rock extends AbstractGameObject {

	private TextureRegion regEdge;
	private TextureRegion regMiddle;

// variables used by Lerp to move the rocks up and down
	private final float FLOAT_CYCLE_TIME = 2.0f; // how long it takes to go up and down
	private final float FLOAT_AMPLITUDE = 0.25f; // how far the rock moves
	private float floatCycleTimeLeft; // time left in the cycle
	private boolean floatingDownwards; // is floating downward
	private Vector2 floatTargetPosition; // next target position

	private int length;

	/**
	 * Initialize the rock object.
	 */
	public Rock() {
		init();
	}

	/**
	 * Initialize the Rock middle pieces and edge pieces with
	 * some set length. Also initializes the variables needed
	 * to control the up and down movements of rocks. 
	 */
	private void init() {
		dimension.set(1, 1.5f);

		regEdge = Assets.instance.rock.edge;// set edge assets
		regMiddle = Assets.instance.rock.middle; // set middle assets

		// Start length of this rock
		setLength(1);

		// initialized to up direction
		floatingDownwards = false;
		// cycle time is randomly picked between 0 and half of CYCLE_TIME
		floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
		// next target position
		floatTargetPosition = null;
	}

	/**
	 * Sets the length of the rock's boundaries.
	 * @param length bounds of the rock
	 */
	public void setLength(int length) {
		this.length = length; // set rock length to val

		// Update bounding box for collision detection
		bounds.set(0, 0, dimension.x * length, dimension.y);
	}

	/**
	 * Increases the length of the rock's bounds
	 * by some amount.
	 * @param amount addition to length of rock bounds
	 */
	public void increaseLength(int amount) {
		setLength(length + amount); // increase rock length

	}

	/**
	 * Draws the Rock using assets for the rock edges and
	 * the rock middles. These are knit together within
	 * the overall bounds of the rock to form the full
	 * rock as one single object.
	 */
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion reg = null;

		float relX = 0;
		float relY = 0;

		// Draw left edge
		reg = regEdge;
		relX -= dimension.x / 4;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x / 4 + 0.01f,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);

		// Draw middle
		relX = 0;
		reg = regMiddle;
		for (int i = 0; i < length; i++) {
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x + 0.01f,
					dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
					reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}

		// Draw right edge
		reg = regEdge;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, origin.y,
				dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), true, false);
	}

	/**
	 * Rocks update and need to move in the air by floating
	 * up and down. The duration, distance moved, and start
	 * times are controlled through here.
	 */
	@Override
		public void update (float deltaTime) {
		super.update(deltaTime);
		floatCycleTimeLeft -= deltaTime;
		if (floatCycleTimeLeft<= 0) {
			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
			floatingDownwards = !floatingDownwards;
			body.setLinearVelocity(0, FLOAT_AMPLITUDE* (floatingDownwards ? -1 : 1));
		} else {
			body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
		}
	}
}
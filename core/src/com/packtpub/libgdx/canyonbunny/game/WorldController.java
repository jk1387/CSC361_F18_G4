package com.packtpub.libgdx.canyonbunny.game;

import com.badlogic.gdx.graphics.Pixmap;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.packtpub.libgdx.canyonbunny.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;
import com.packtpub.libgdx.canyonbunny.util.Constants;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.canyonbunny.util.AudioManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.packtpub.libgdx.canyonbunny.game.objects.Carrot;

// specific objects
import com.packtpub.libgdx.canyonbunny.game.objects.BunnyHead;
import com.packtpub.libgdx.canyonbunny.game.objects.BunnyHead.JUMP_STATE;
import com.packtpub.libgdx.canyonbunny.game.objects.Feather;
import com.packtpub.libgdx.canyonbunny.game.objects.GoldCoin;
import com.packtpub.libgdx.canyonbunny.game.objects.Rock;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Game;
import com.packtpub.libgdx.canyonbunny.screens.MenuScreen;
import com.badlogic.gdx.utils.Disposable;

/**
 * Handles the updates in the world. It KNOWS where objects
 * are and what they're doing at all times and makes sure
 * the renderer can draw objects in their correct
 * positions. Also handles collision detection.
 * @author Drake Conaway, Jacob Kole, Christian Crouthamel
 */
public class WorldController extends InputAdapter implements Disposable {

	private static final String TAG = WorldController.class.getName();
	public Level level;
	public int lives;
	public int score;
	private Game game;
	public CameraHelper cameraHelper;
	
	public float livesVisual;
	public float scoreVisual;

	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	private float timeLeftGameOverDelay;

	private boolean goalReached; //has the goal been reached?
	public World b2world;
	/**
	 * Initialize the physics inside of the world
	 * using box2d assets
	 * dispose of excess to free memory
	 */
	private void initPhysics() {
		if(b2world != null) b2world.dispose(); //destroy if already init
		b2world = new World(new Vector2(0, -9.81f),true);
		//Rocks
		Vector2 origin = new Vector2();
		for(Rock rock : level.rocks) { //for each rock
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(rock.position);
			 Body body = b2world.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width / 2.0f;
			origin.y = rock.bounds.height / 2.0f;
			polygonShape.setAsBox(rock.bounds.width / 2.0f,
					rock.bounds.height / 2.0f, origin,0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
	}
	/**
	 * Method to spawn carrots within
	 * the World
	 */
	private void spawnCarrots(Vector2 pos, int numCarrots,float radius) {
		float carrotShapeScale = 0.5f;
		//create carrots w/ box2d body and fixture
		for(int i = 0; i <numCarrots;i++) {
			Carrot carrot = new Carrot();
			//calc random spawn position,rotation,scale
			float x = MathUtils.random(-radius,radius);
			float y = MathUtils.random(5.0f,15.0f);
			float rotation = MathUtils.random(0.0f,360.f)
					* MathUtils.degreesToRadians;
			float carrotScale = MathUtils.random(0.5f,1.5f);
			carrot.scale.set(carrotScale,carrotScale);
			//create box2d body for carrot with start position
			//and angel of rotation
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pos);
			bodyDef.position.add(x,y);
			bodyDef.angle =rotation;
			  Body body = b2world.createBody(bodyDef);
			  body.setType(BodyType.DynamicBody);
			  carrot.body = body;
			  //create rectangular shape for carrot to allow
			  //interactions w/ other objects
			  PolygonShape polygonShape = new PolygonShape();
			  float halfWidth = carrot.bounds.width/2.0f * carrotScale;
			  float halfHeight = carrot.bounds.height/2.0f * carrotScale;
			  polygonShape.setAsBox(halfWidth*carrotShapeScale,
					  halfHeight * carrotShapeScale);
			  //set physics attributes
			  FixtureDef fixtureDef = new FixtureDef();
			  fixtureDef.shape = polygonShape;
			  fixtureDef.density = 50;
			  fixtureDef.restitution = 0.5f;
			  fixtureDef.friction = 0.5f;
			  body.createFixture(fixtureDef);
			  polygonShape.dispose();
			  //finally, add new carrot to list for rendering and updating
			  level.carrots.add(carrot);
		}
	}
	/**
	 * Method handling bunnyhead collision with goal
	 * sets goalReached to true
	 */
	 private void onCollisionBunnyWithGoal() {
	 goalReached = true;
	 timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
	  Vector2 centerPosBunnyHead =
			  new Vector2(level.bunnyHead.position);
	  centerPosBunnyHead.x += level.bunnyHead.bounds.width;
	  spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX,
			  Constants.CARROTS_SPAWN_RADIUS);
	 }
	
	/**
	 * Boolean checker method for if the game has ended
	 * @return true if lives are < 0
	 */
	public boolean isGameOver() {
		return lives < 0;
	}

	/**
	 * Checks if the player fell off screen
	 * @return
	 */
	public boolean isPlayerInWater() {
		return level.bunnyHead.position.y < -5;
	}

	/**
	 * Collision based methods
	 * @param rock the rock object
	 */
	private void onCollisionBunnyHeadWithRock(Rock rock) {
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) {
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge) {
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return;
		}

		switch (bunnyHead.jumpState) {
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	};

	/**
	 * Bunny collides with a gold coin. Increase score.
	 * Set that gold coin's boolean value to true so
	 * it knows it's been collected.
	 * @param goldcoin
	 */
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {
		goldcoin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	};

	/**
	 * Bunny collides with a feather. Increase score.
	 * Set the powerup for the bunny to true that it has
	 * a feather and make sure the feather knows
	 * it's been collected.
	 * @param feather
	 */
	private void onCollisionBunnyWithFeather(Feather feather) {
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	};

	/**
	 * Method for testing the collision of the bunnyhead with objects
	 */
	private void testCollisions() {
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width,
				level.bunnyHead.bounds.height);

		//Test collison: Bunnny <-> Rocks
		for (Rock rock : level.rocks) {
			r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks
		}
		// Test collision: BunnyHead <-> Gold Coins
		for (GoldCoin goldcoin : level.goldcoins) {
			if (goldcoin.collected)
				continue;
			r2.set(goldcoin.position.x, goldcoin.position.y, goldcoin.bounds.width, goldcoin.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}
		// test collsion: Bunny Head <-> Feathers
		for (Feather feather : level.feathers) {
			if (feather.collected)
				continue;
			r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2))
				continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}
		//Test collision: BunnyHead <->Goal
		if(!goalReached) {
			r2.set(level.goal.bounds);
			r2.x += level.goal.position.x;
			r2.y += level.goal.position.y;
			if(r1.overlaps(r2)) onCollisionBunnyWithGoal();
			
		}
	}

	/**
	 * Level initialization method
	 */
	private void initLevel() {
		score = 0;
		scoreVisual = score;
		goalReached = false; //set goal reached to false at each init
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
		initPhysics();
	}

	/**
	 * constructor for world controller
	 * @param game instance of the game/BunnyMain
	 */
	public WorldController(Game game) {
		this.game = game;
		init();
	}

	/**
	 * Initialize the world controller. Set lives.
	 * Get a CameraHelper.
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	/**
	 * Build a pixmap
	 * @param width width of the procedural pixmap
	 * @param height height of the procedural pixmap
	 * @return the built pixmap
	 */
	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		// fill square w/ red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow colored x shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		// draw a cyan colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	/**
	 * Updates the level (and its objects), update the 
	 * camera, handle game inputs, handle collisions.
	 * @param deltaTime the game time
	 */
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		if (isGameOver()|| goalReached) {
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) {
				backToMenu();
				return;
			}
		} else {
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		b2world.step(deltaTime, 8, 3);
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) {
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		// mountains scroll at different speeds
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		// life lost animation goes as livesVisual catches up to lives
		if (livesVisual > lives)
			livesVisual = Math.max(lives,  livesVisual - 1 * deltaTime);
		// score gain animation goes as scoreVisual catches up to new score
		if (scoreVisual < score)
			scoreVisual = Math.min(score,  scoreVisual 
					+ 250 * deltaTime);
	}

	/**
	 * Add functionality behind every user input.
	 * @param deltaTime game time
	 */
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop)
			return;

		if (!cameraHelper.hasTarget(level.bunnyHead)) {
			// Camera Controls(move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP))
				moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0, 0);
			// Camera controls(zoom)
			float camZoomSpeed = 1 * deltaTime;
			float camZoomSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camZoomSpeed *= camZoomSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.COMMA))
				cameraHelper.addZoom(camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.PERIOD))
				cameraHelper.addZoom(-camZoomSpeed);
			if (Gdx.input.isKeyPressed(Keys.SLASH))
				cameraHelper.setZoom(1);

		}
	}

	/**
	 * Controls the x and y movements of the camera.
	 * @param x horizontal position
	 * @param y vertical position
	 */
	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	/**
	 * Checks for special key inputs that restart the world
	 * and change how the camera follows the player. Also
	 * handles escaping back to the menu screen.
	 */
	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world restarted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// Back to Menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			backToMenu();
		}
		return false;
	}

	/**
	 * Handles the input keys for the game. These affect
	 * player movements in-game and change the physics affecting
	 * the player.
	 * @param deltaTime game time
	 */
	private void handleInputGame(float deltaTime) {
		if (cameraHelper.hasTarget(level.bunnyHead)) {
			// player movement
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			} else {
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) {
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}
			}
			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
				level.bunnyHead.setJumping(true);
			} else {
				level.bunnyHead.setJumping(false);
			}
		}
	}

	/**
	 * save a reference to the game instance
	 */
	private void backToMenu() {
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
	/**
	 * Overridden dispose method
	 * destorys b2world if its extant
	 */
	@Override
	public void dispose() {
		if(b2world != null)b2world.dispose();
		
	}
}
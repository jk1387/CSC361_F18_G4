package com.packtpub.libgdx.canyonbunny.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Animation;

public abstract class AbstractGameObject {

	//variables
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	//chapter 6
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	public Vector2 acceleration;
	public Rectangle bounds;
	//chapter 11
	public Body body;
	// state time of the animation
	public float stateTime;
	// current set animation
	public Animation animation;
	
	//abstract constructor
	public AbstractGameObject () {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		//chapter 6
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();
	}
	
	/**
	 * Change the current animation and set the state time
	 * to 0.
	 * @param animation the current animation
	 */
	public void setAnimation (Animation animation) {
		this.animation = animation;
		stateTime = 0;
	}
	
	/*
	 * update method for the abstract game object class
	 */
	public void update (float deltaTime) {
		stateTime += deltaTime;
		//chapter 11 adds if statement
		if (body == null) {
		//chapter 6
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		// Move to new position
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		} else {
			position.set(body.getPosition());
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
			}
	}
	
	//abstract render method
	public abstract void render (SpriteBatch batch);
	
	//Chapter 6
	protected void updateMotionX (float deltaTime) {
		
		if (velocity.x != 0) {
		// Apply friction
		if (velocity.x > 0) {
		       velocity.x =
		       Math.max(velocity.x - friction.x * deltaTime, 0);
		} 
		else {
		      velocity.x =
		      Math.min(velocity.x + friction.x * deltaTime, 0);
		     }
		}
		
		// Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.x = MathUtils.clamp(velocity.x,
		-terminalVelocity.x, terminalVelocity.x);
		}
		
	    //motion y function
	    protected void updateMotionY (float deltaTime) {
		
	    if (velocity.y != 0) {
		// Apply friction
		if (velocity.y > 0) {
		     velocity.y = Math.max(velocity.y - friction.y *
		     deltaTime, 0);
		} 
		else {
		        velocity.y = Math.min(velocity.y + friction.y *
		        deltaTime, 0);
		       }
		}
		
		// Apply acceleration
		velocity.y += acceleration.y * deltaTime;
		// Make sure the object's velocity does not exceed the
		// positive or negative terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -
				terminalVelocity.y, terminalVelocity.y);
		}
}
package com.patrickharsch.birdgame.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Bird {
    private static final int GRAVITY = -15;
    private int movement = 100;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation birdAnimation;
    private Texture bird;
    private Sound flap;


    public Bird(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        bird  = new Texture("Sprites/BirdAnimation.png");
        birdAnimation =  new Animation(bird, 3, 0.3f);
        bounds = new Rectangle(x, y, bird.getWidth()/3, bird.getHeight()-5); // Bounds are of slightly smaller height than the texture to make collision less frustrating, because the bird looks round while the bounds are square
        flap = Gdx.audio.newSound(Gdx.files.internal("SFX/wing.wav"));
    }

    //update the birds position according to the current velocity
    public void update(float dt){
        birdAnimation.update(dt);

        if(position.y > 75) {
            velocity.add(0, GRAVITY, 0);
        }
        velocity.scl(dt);
        position.add(movement * dt, velocity.y, 0);
        if(position.y < 75){
            position.y = 75;
        }

        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y+2); // y+2 to position the bounds more to the y-center of the texture instead of just cutting of 5 at the top
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPositionY(float position) {
        this.position.y = position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocityY(int velocity) {
        this.velocity.y = velocity;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public TextureRegion getTexture() {
        return birdAnimation.getFrame();
    }


    public void jump(){
        velocity.y = 360;
        flap.play();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose(){
        bird.dispose();
        flap.dispose();
    }
}

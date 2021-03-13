package com.patrickharsch.birdgame.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Item {
    private static final int FLUCTUATION = 220;
    private static final int LOWEST_SPAWN = 100;
    private static final int ITEM_WIDTH = 32;
    private Vector3 position;
    private Texture item;
    private Animation itemAnimation;
    private Rectangle bounds;
    private Random rand;
    private int id;

    private Boolean itemWasMoved;

    public  Item(float pos){
        rand = new Random();
        itemWasMoved = false;

        // to achieve probabilities of 40%, 30%, 20% and 10%
        switch (rand.nextInt(10)) {
            case 0:
            case 1:
            case 2:
            case 3:
                item = new Texture("Sprites/BlueItemAnim.png");
                itemAnimation = new Animation(item, 2, 1f);
                id = 0;
                break;
            case 4:
            case 5:
            case 6:
                item = new Texture("Sprites/TurquoiseItemAnim.png");
                itemAnimation = new Animation(item, 2, 1f);
                id = 1;
                break;
            case 7:
            case 8:
                item = new Texture("Sprites/GreenItemAnim.png");
                itemAnimation = new Animation(item, 2, 1f);
                id = 2;
                break;
            case 9:
                item = new Texture("Sprites/RedItemAnim.png");
                itemAnimation = new Animation(item, 2, 1f);
                id = 3;
                break;

            default:
        }

        position = new Vector3(pos, rand.nextInt(FLUCTUATION)+LOWEST_SPAWN, 0);
        bounds = new Rectangle(position.x, position.y, item.getWidth()/2, item.getHeight());
    }

    public void update(float delta){
        itemAnimation.update(delta);
    }

    public static int getItemWidth() { return ITEM_WIDTH; }

    public Vector3 getPosition() {
        return position;
    }

    public void setPositionX(float position) {
        this.position.x = position;
    }

    public Boolean getItemWasMoved() {
        return itemWasMoved;
    }

    public void setItemWasMoved(Boolean itemWasMoved) {
        this.itemWasMoved = itemWasMoved;
    }

    public TextureRegion getTexture() {
        return itemAnimation.getFrame();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getId() {
        return id;
    }

    public boolean collides(Rectangle player){
        return player.overlaps(bounds);
    }

    public void dispose(){
        item.dispose();
    }


}

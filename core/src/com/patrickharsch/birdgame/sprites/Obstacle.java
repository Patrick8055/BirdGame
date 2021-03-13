package com.patrickharsch.birdgame.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Obstacle {
    private static final int LOG_WIDTH = 60;
    private static final int FLUCTUATION = 160;
    private static final int LOWEST_OPENING = 120;
    private Texture topLog, bottomLog;
    private Vector2 posTopLog, posBotLog;
    private Rectangle boundsTop, boundsBottom;
    private int logSpacing = 120;
    private Random rand;


    public Obstacle(float x){
        topLog = new Texture("Sprites/LogTop.png");
        bottomLog = new Texture("Sprites/LogBottom.png");
        rand = new Random();

        posTopLog = new Vector2(x, rand.nextInt(FLUCTUATION) + logSpacing + LOWEST_OPENING);
        posBotLog = new Vector2(x, posTopLog.y - logSpacing - bottomLog.getHeight());

        boundsTop = new Rectangle(posTopLog.x, posTopLog.y, topLog.getWidth(), topLog.getHeight()+1000); // extend bounds of the top log far above the screen to prevent players from passing the obstacles by flying above
        boundsBottom = new Rectangle(posBotLog.x, posBotLog.y, bottomLog.getWidth(), bottomLog.getHeight());
    }

    public Texture getTopLog() {
        return topLog;
    }

    public Texture getBottomLog() {
        return bottomLog;
    }

    public Vector2 getPosTopLog() {
        return posTopLog;
    }

    public Vector2 getPosBotLog() {
        return posBotLog;
    }

    public void setPosTopLog(Vector2 posTopLog) {
        this.posTopLog = posTopLog;
        this.boundsTop.setPosition(posTopLog);
    }

    public void setPosBotLog(Vector2 posBotLog) {
        this.posBotLog = posBotLog;
        this.boundsBottom.setPosition(posBotLog);
    }

    public void reposition(float x){
        posTopLog.set(x, rand.nextInt(FLUCTUATION) + logSpacing + LOWEST_OPENING); // randomize x position each time the logs get repositioned
        posBotLog.set(x, posTopLog.y - logSpacing - bottomLog.getHeight());
        boundsTop.setPosition(posTopLog.x, posTopLog.y+5);
        boundsBottom.setPosition(posBotLog.x, posBotLog.y-5); // +5 -5 to make bounds match better with visuals
    }

    public boolean collides(Rectangle player){
        return player.overlaps(boundsTop) || player.overlaps(boundsBottom);
    }

    public int getLowestOpening(){ return LOWEST_OPENING; };

    public static int getLogWidth() { return LOG_WIDTH; }

    public void dispose(){
        topLog.dispose();
        bottomLog.dispose();
    }
}

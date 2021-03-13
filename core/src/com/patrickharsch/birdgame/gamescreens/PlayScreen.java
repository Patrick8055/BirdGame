package com.patrickharsch.birdgame.gamescreens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.patrickharsch.birdgame.BirdGame;
import com.patrickharsch.birdgame.sprites.Bird;
import com.patrickharsch.birdgame.sprites.Item;
import com.patrickharsch.birdgame.sprites.Obstacle;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class PlayScreen extends ScreenAdapter {
    final BirdGame game;
    private static final int LOG_SPACING = 120;
    private static final int LOG_COUNT = 4;
    private static final int ITEM_SPACING = 600;
    private static final int ITEM_SPACING_FLUCTUATION = 1000;
    private Random random;

    private Array<Obstacle> logs;
    private Bird bird;
    private Texture background;
    private Texture ground;
    private Texture ring;
    OrthographicCamera camera;
    private Vector2 groundPos1, groundPos2;
    private Sound pickup;
    private Sound scoreUp;

    private Item item;
    private Item activeItem;
    private Boolean itemIsActive, redItemIsActive, blueItemIsActive, greenItemIsActive;
    private long duration;
    private Boolean ignoreCollision;

    private int score;
    public static int finalScore;

    private int increment;
    private Boolean alreadyExecuted;

    private Timer timer;
    ShaderProgram fontShader;


    public PlayScreen(BirdGame game) {
        this.game = game;
        random = new Random();
        itemIsActive = false;
        redItemIsActive = false;
        blueItemIsActive = false;
        greenItemIsActive = false;
        ignoreCollision = false;
        duration = 5; // default item duration set to 5

        score = 0;
        increment = 1;
        alreadyExecuted = false;
        timer = new Timer();
        game.font.getData().setScale(1, 1);

        bird = new Bird(70, 300);
        ring = new Texture("Sprites/Halo.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, BirdGame.WIDTH/2, BirdGame.HEIGHT/2);

        background = new Texture("Sprites/Background.png");
        ground = new Texture("Sprites/Ground.png");
        groundPos1 = new Vector2(camera.position.x - (camera.viewportWidth/2),0);
        groundPos2 = new Vector2(camera.position.x - (camera.viewportWidth/2) + ground.getWidth(), 0);

        pickup = Gdx.audio.newSound(Gdx.files.internal("SFX/powerup.wav"));
        scoreUp = Gdx.audio.newSound(Gdx.files.internal("SFX/pickup.wav"));

        fontShader = new ShaderProgram(Gdx.files.internal("Fonts/font.vert"), Gdx.files.internal("Fonts/font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }

        logs = new Array<Obstacle>();
        for(int i = 1; i <= LOG_COUNT; i++) {
            logs.add(new Obstacle(i * (LOG_SPACING + Obstacle.getLogWidth()) + 200));
        }
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        update();
        bird.update(delta);

        if(item != null){
            item.update(delta);
        }

        camera.position.x = bird.getPosition().x + 80;
        camera.update();

        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.draw(background, camera.position.x - (camera.viewportWidth / 2), 0, camera.viewportWidth, camera.viewportHeight);

        for(Obstacle log : logs) {
            game.batch.draw(log.getTopLog(), log.getPosTopLog().x, log.getPosTopLog().y);
            game.batch.draw(log.getBottomLog(), log.getPosBotLog().x, log.getPosBotLog().y);
        }

        game.batch.setShader(fontShader);
        game.font.draw(game.batch, ""+score , camera.position.x + camera.viewportWidth/2 - camera.viewportWidth/6, camera.position.y + camera.viewportHeight/2 - camera.viewportHeight/15);
        game.batch.setShader(null);

        game.batch.draw(ground, groundPos1.x, groundPos1.y, ground.getWidth(), ground.getHeight()/2);
        game.batch.draw(ground, groundPos2.x, groundPos2.y, ground.getWidth(), ground.getHeight()/2);

        //rotate the bird animation up/down or neutral when the birds y velocity is positive/negative or close to 0
        if(bird.getVelocity().y >= 50) {
            game.batch.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y,
                    bird.getTexture().getRegionWidth() / 2, bird.getTexture().getRegionHeight() / 2,
                    bird.getTexture().getRegionWidth(), bird.getTexture().getRegionHeight(), 1, 1, 30);
        } else if( bird.getVelocity().y < 50 && bird.getVelocity().y >= -50){
            game.batch.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y,
                    bird.getTexture().getRegionWidth()/2, bird.getTexture().getRegionHeight()/2,
                    bird.getTexture().getRegionWidth(), bird.getTexture().getRegionHeight(), 1, 1, 0);
        } else if(bird.getVelocity().y < -50 && bird.getVelocity().y >= -450){
            game.batch.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y,
                    bird.getTexture().getRegionWidth()/2, bird.getTexture().getRegionHeight()/2,
                    bird.getTexture().getRegionWidth(), bird.getTexture().getRegionHeight(), 1, 1, -30);
        } else {
            game.batch.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y,
                    bird.getTexture().getRegionWidth()/2, bird.getTexture().getRegionHeight()/2,
                    bird.getTexture().getRegionWidth(), bird.getTexture().getRegionHeight(), 1, 1, -70);
        }

        if(blueItemIsActive || redItemIsActive){
            game.batch.draw(ring, bird.getPosition().x - 3, bird.getPosition().y - 8);
        }

        if(item != null){
            game.batch.draw(item.getTexture(), item.getPosition().x, item.getPosition().y);
        }

        game.batch.end();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(!redItemIsActive) {
                    bird.jump();
                }
                return true;
            }
        });
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        bird.dispose();
        background.dispose();
        ground.dispose();
        ring.dispose();
        pickup.dispose();
        scoreUp.dispose();
        item.dispose();

        for(Obstacle log : logs){
            log.dispose();
        }
    }

    private void update(){
        updateGround();
        updateLogs();
        updateItem();

        // red item counteracts gravity
        if(redItemIsActive){
            bird.setVelocityY(15);
        }

        // red and green items move the logs up/down, score gets increased when the bird passes an obstacle, game ends on collision
        for(Obstacle log : logs){
            if(redItemIsActive || greenItemIsActive){
                if(log.getPosTopLog().y < camera.viewportHeight-camera.viewportHeight/20) {
                    log.setPosTopLog(new Vector2(log.getPosTopLog().x, log.getPosTopLog().y + 2));
                }
                if(log.getPosBotLog().y > log.getLowestOpening() - log.getBottomLog().getHeight()) {
                    log.setPosBotLog(new Vector2(log.getPosBotLog().x, log.getPosBotLog().y - 2));
                }
            }
            if(bird.getPosition().x > log.getPosTopLog().x + Obstacle.getLogWidth() - bird.getTexture().getRegionWidth()  && !alreadyExecuted){
                updateScore();
                increaseSpeed();
            }
            if(log.collides(bird.getBounds()) && !ignoreCollision){
                finalScore = score;
                game.setScreen(new GameOverScreen(game));
                break;
            }
        }

        if(bird.getPosition().y <= ground.getHeight()/2 ){
            finalScore = score;
            game.setScreen(new GameOverScreen(game));
        }

    }

    // speed gets increased the longer the game runs
    private void increaseSpeed(){
        bird.setMovement(bird.getMovement()+ increment*2);
    }

    private void updateScore(){
            score += increment;
            alreadyExecuted = true;
            scoreUp.play();
    }

    // to make the ground appear moving compared to the bird, 2 grounds are used and repositioned as soon as they move outside of the screen
    private void updateGround(){
        if(camera.position.x - (camera.viewportWidth/2) > groundPos1.x + ground.getWidth()){
            groundPos1.add(ground.getWidth()*2, 0);
        }
        if(camera.position.x - (camera.viewportWidth/2) > groundPos2.x + ground.getWidth()){
            groundPos2.add(ground.getWidth()*2, 0);
        }
    }

    private void updateLogs(){
        for(Obstacle log : logs){
            if(camera.position.x - (camera.viewportWidth / 2) > log.getPosTopLog().x + log.getTopLog().getWidth()) {
                log.reposition(log.getPosTopLog().x + ((Obstacle.getLogWidth() + LOG_SPACING) * LOG_COUNT));
                alreadyExecuted = false;
            }
        }
    }

    //spawn item while none is active or on the map, check for collision with player and activate effects and reposition when item is spawned inside an obstacle
    private void updateItem(){
        if(activeItem == null){
            spawnItem();
            if (item.collides(bird.getBounds())) {
                activeItem = item;
                activateItem();
                item = null;
                pickup.play();
            }

            for (Obstacle log : logs) {
                if (item != null && log.collides(item.getBounds()) && !item.getItemWasMoved()) {
                    item.setPositionX(item.getPosition().x + 100);
                    item.getBounds().setPosition(item.getPosition().x, item.getPosition().y);
                    item.setItemWasMoved(true);
                }
            }
        }

    }

    private void spawnItem(){
        if(item == null && !itemIsActive){
            item = new Item(camera.position.x - camera.viewportWidth/2 + ITEM_SPACING + random.nextInt(ITEM_SPACING_FLUCTUATION));
        } else if (item.getPosition().x + Item.getItemWidth() <= camera.position.x - camera.viewportWidth/2 && !itemIsActive) {
            item = null;
            item = new Item(camera.position.x - camera.viewportWidth/2 + ITEM_SPACING + random.nextInt(ITEM_SPACING_FLUCTUATION));
        }
    }

    /*
        Activate item effects,
        Blue: 5s no collision with obstacles
        Turquoise: 10s double score
        Green: move obstacles out of the way for 8s
        Red: turbo mode for 4s
        deactivation scheduled according to item duration

     */
    private void activateItem(){
        switch (activeItem.getId()){
            case 0:
                duration = 5;
                itemIsActive = true;
                blueItemIsActive = true;
                ignoreCollision = true;
                timer.schedule(new Task(), duration * 1000);
                break;
            case 1:
                duration = 10;
                itemIsActive = true;
                increment = 2;
                timer.schedule(new Task(), duration * 1000);
                break;
            case 2:
                duration = 8;
                itemIsActive = true;
                greenItemIsActive = true;
                timer.schedule(new Task(), duration * 1000);
                break;
            case 3:
                duration = 4;
                itemIsActive = true;
                redItemIsActive = true;
                bird.setPositionY(camera.viewportHeight/2);
                bird.setMovement(bird.getMovement()*5);
                ignoreCollision = true;
                timer.schedule(new Task(), duration * 1000);
                break;
            default:
        }
    }

    private void deactivateItem(){
        switch(activeItem.getId()){
            case 0:
                itemIsActive = false;
                blueItemIsActive = false;
                timer.schedule(new TaskCollision(), 1000);
                activeItem = null;
                break;
            case 1:
                itemIsActive = false;
                increment = 1;
                activeItem = null;
                break;
            case 2:
                itemIsActive = false;
                greenItemIsActive = false;
                activeItem = null;
                break;
            case 3:
                itemIsActive = false;
                redItemIsActive = false;
                bird.setMovement(bird.getMovement()/5);
                timer.schedule(new TaskCollision(), 1000);
                activeItem = null;
                break;
            default:
        }

    }

    class Task extends TimerTask {
        @Override
        public void run(){
            deactivateItem();
        }
    }

    class TaskCollision extends TimerTask {
        @Override
        public void run(){ ignoreCollision = false; }
    }
}

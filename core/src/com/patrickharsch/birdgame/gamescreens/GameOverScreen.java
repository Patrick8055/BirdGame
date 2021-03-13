package com.patrickharsch.birdgame.gamescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.patrickharsch.birdgame.BirdGame;
import com.patrickharsch.birdgame.sprites.Animation;
import com.patrickharsch.birdgame.sprites.Bird;

public class GameOverScreen extends ScreenAdapter {
    BirdGame game;

    private Texture background;
    private Texture ground;
    private Texture gameOver;
    private Texture mainMenu;
    private Texture playAgain;
    private Texture bird, redItem, blueItem, greenItem, turquoiseItem;
    private Animation birdAnimation, redItemAnimation, blueItemAnimation, greenItemAnimation, turquoiseItemAnimation;
    private int finalScore = PlayScreen.finalScore;
    private Preferences prefs;
    private int highScore;

    ShaderProgram fontShader;

    OrthographicCamera camera;

    public GameOverScreen(BirdGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, BirdGame.WIDTH, BirdGame.HEIGHT);
        background = new Texture("Sprites/Background.png");
        ground = new Texture("Sprites/Ground.png");
        bird  = new Texture("Sprites/BirdAnimation.png");
        birdAnimation =  new Animation(bird, 3, 0.3f);
        redItem = new Texture("Sprites/RedItemAnim.png");
        redItemAnimation = new Animation(redItem, 2, 1f);
        blueItem = new Texture("Sprites/BlueItemAnim.png");
        blueItemAnimation = new Animation(blueItem, 2, 1f);
        greenItem = new Texture("Sprites/GreenItemAnim.png");
        greenItemAnimation = new Animation(greenItem, 2, 1f);
        turquoiseItem = new Texture("Sprites/TurquoiseItemAnim.png");
        turquoiseItemAnimation = new Animation(turquoiseItem, 2, 1f);
        gameOver = new Texture("Sprites/GameOverYellow.png");
        playAgain = new Texture("Sprites/PlayAgainBlue.png");
        mainMenu = new Texture("Sprites/MainMenuBlue.png");

        fontShader = new ShaderProgram(Gdx.files.internal("Fonts/font.vert"), Gdx.files.internal("Fonts/font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }

        prefs = Gdx.app.getPreferences("BirdGame.Score");
        highScore = prefs.getInteger("highscore", 0);

        if(finalScore > highScore){
            prefs.putInteger("highscore", finalScore);
            prefs.flush();
        }

        highScore = prefs.getInteger("highscore", 0);


    }

    @Override
    public void render(float delta) {
        super.render(delta);
        birdAnimation.update(delta);
        redItemAnimation.update(delta);
        blueItemAnimation.update(delta);
        greenItemAnimation.update(delta);
        turquoiseItemAnimation.update(delta);
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.draw(background, 0, 0, BirdGame.WIDTH, BirdGame.HEIGHT);
        game.batch.draw(ground, 0, 0);
        game.batch.draw(birdAnimation.getFrame(), BirdGame.WIDTH/2 - birdAnimation.getFrame().getRegionWidth()/2 , BirdGame.HEIGHT - BirdGame.HEIGHT/3 - BirdGame.HEIGHT/8);
        game.batch.draw(redItemAnimation.getFrame(), BirdGame.WIDTH/2 - redItemAnimation.getFrame().getRegionWidth()*2 - 37, BirdGame.HEIGHT - BirdGame.HEIGHT/3 - BirdGame.HEIGHT/6);
        game.batch.draw(blueItemAnimation.getFrame(), BirdGame.WIDTH/2 - redItemAnimation.getFrame().getRegionWidth() -12, BirdGame.HEIGHT - BirdGame.HEIGHT/3 - BirdGame.HEIGHT/6);
        game.batch.draw(greenItemAnimation.getFrame(), BirdGame.WIDTH/2 + 12, BirdGame.HEIGHT - BirdGame.HEIGHT/3 - BirdGame.HEIGHT/6);
        game.batch.draw(turquoiseItemAnimation.getFrame(), BirdGame.WIDTH/2 + redItemAnimation.getFrame().getRegionWidth() + 37, BirdGame.HEIGHT - BirdGame.HEIGHT/3 - BirdGame.HEIGHT/6);
        game.batch.draw(gameOver, BirdGame.WIDTH/2-gameOver.getWidth()/2, BirdGame.HEIGHT- BirdGame.HEIGHT/3 - 50);
        game.batch.draw(playAgain, BirdGame.WIDTH/2 - playAgain.getWidth()/2, BirdGame.HEIGHT/4 - 40);
        game.batch.draw(mainMenu, BirdGame.WIDTH/2 - mainMenu.getWidth()/2, BirdGame.HEIGHT/4 - playAgain.getHeight() - 50);

        game.batch.setShader(fontShader);
        game.font.draw(game.batch, "Score:      "+finalScore ,BirdGame.WIDTH/2 - BirdGame.WIDTH/5 , BirdGame.HEIGHT/2-60);
        game.font.draw(game.batch, "High Score: "+highScore ,BirdGame.WIDTH/2 - BirdGame.WIDTH/5 , BirdGame.HEIGHT/2-80);
        game.batch.setShader(null);

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
                Vector3 pos = new Vector3(screenX, screenY, 0);
                camera.unproject(pos); // transform screen coordinates to game world coordinates
                if (pos.x >= BirdGame.WIDTH/2 - playAgain.getWidth()/2 && pos.x <= BirdGame.WIDTH/2 + playAgain.getWidth()/2
                        && pos.y >= BirdGame.HEIGHT/4 - 40 && pos.y <= BirdGame.HEIGHT/4 - 40 + playAgain.getHeight()) {
                    game.setScreen(new PlayScreen(game));
                    dispose();
                }
                if (pos.x >= BirdGame.WIDTH/2 - mainMenu.getWidth()/2 && pos.x <= BirdGame.WIDTH/2 + mainMenu.getWidth()/2
                        && pos.y >= BirdGame.HEIGHT/4 - playAgain.getHeight() - 50 && pos.y <= BirdGame.HEIGHT/4 - playAgain.getHeight() - 50 + mainMenu.getHeight()) {
                    game.setScreen(new MenuScreen(game));
                    dispose();
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
        background.dispose();
        ground.dispose();
        gameOver.dispose();
        playAgain.dispose();
        mainMenu.dispose();
        bird.dispose();
        redItem.dispose();
        blueItem.dispose();
        greenItem.dispose();
        turquoiseItem.dispose();
    }
}

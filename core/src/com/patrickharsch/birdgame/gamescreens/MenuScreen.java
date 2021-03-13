package com.patrickharsch.birdgame.gamescreens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.patrickharsch.birdgame.BirdGame;


public class MenuScreen extends ScreenAdapter {
    BirdGame game;

    private Texture background;
    private Texture ground;
    private Texture playBtn;

    OrthographicCamera camera;


    public MenuScreen(BirdGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, BirdGame.WIDTH/2, BirdGame.HEIGHT/2);
        background = new Texture("Sprites/Background.png");
        ground = new Texture("Sprites/GroundFull.png");
        playBtn = new Texture("Sprites/Playbutton.png");
    }



    @Override
    public void render(float delta) {
        super.render(delta);
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.draw(background, camera.position.x - (camera.viewportWidth / 2), camera.position.y-camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);
        game.batch.draw(ground, camera.position.x - (camera.viewportWidth / 2), camera.position.y-camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight);
        game.batch.draw(playBtn, camera.position.x - playBtn.getWidth()/2, camera.position.y);
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
                camera.unproject(pos);
                if (pos.x >= camera.position.x - playBtn.getWidth()/2 && pos.x <= camera.position.x + playBtn.getWidth()/2
                        && pos.y >= camera.position.y && pos.y < camera.position.y + playBtn.getHeight()) {
                    game.setScreen(new PlayScreen(game));
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
        playBtn.dispose();
    }
}
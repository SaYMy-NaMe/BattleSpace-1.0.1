package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.BattleSpace;

public class MainMenuScreen implements Screen{
    private static final int EXIT_BUTTON_WIDTH = 300;
    private static final int EXIT_BUTTON_HEIGHT = 150;
    private static final int PLAY_BUTTON_WIDTH = 330;
    private static final int PLAY_BUTTON_HEIGHT = 150;
    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 250;
    Texture PlayButtonActive;
    Texture PlayButtonInactive;
    Texture ExitButtonActive;
    Texture ExitButtonInactive;

    BattleSpace game;
    public MainMenuScreen(BattleSpace game)
    {
        this.game = game;
        PlayButtonActive = new Texture("play_button_active.png");
        PlayButtonInactive = new Texture("play_button_inactive.png");
        ExitButtonActive = new Texture("exit_button_active.png");
        ExitButtonInactive = new Texture("exit_button_inactive.png");

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        int x = BattleSpace.WIDTH/2-EXIT_BUTTON_WIDTH/2 ;
        if(Gdx.input.getX() < x+ EXIT_BUTTON_WIDTH && Gdx.input.getX() > x  && BattleSpace.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && BattleSpace.HEIGHT -Gdx.input.getY() > EXIT_BUTTON_Y)
        {
            game.batch.draw(ExitButtonActive,BattleSpace.WIDTH/2-EXIT_BUTTON_WIDTH/2,EXIT_BUTTON_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);
            if(Gdx.input.isTouched())
            {
                this.dispose();;
                Gdx.app.exit();
            }
        }
        else
        {
            game.batch.draw(ExitButtonInactive,BattleSpace.WIDTH/2-EXIT_BUTTON_WIDTH/2,EXIT_BUTTON_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);
        }
         x = BattleSpace.WIDTH/2-PLAY_BUTTON_WIDTH/2;
        if(Gdx.input.getX() < x+ PLAY_BUTTON_WIDTH && Gdx.input.getX() > x  && BattleSpace.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + EXIT_BUTTON_HEIGHT && BattleSpace.HEIGHT -Gdx.input.getY() > PLAY_BUTTON_Y)
        {
            game.batch.draw(PlayButtonActive,BattleSpace.WIDTH/2-PLAY_BUTTON_WIDTH/2,PLAY_BUTTON_Y,PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT);
            if(Gdx.input.isTouched())
            {
                this.dispose();;
                game.setScreen(new MainGameScreen(game));
            }
        }
        else
        {
            game.batch.draw(PlayButtonInactive,BattleSpace.WIDTH/2-PLAY_BUTTON_WIDTH/2,PLAY_BUTTON_Y,PLAY_BUTTON_WIDTH,PLAY_BUTTON_HEIGHT);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

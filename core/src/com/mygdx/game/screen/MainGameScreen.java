package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.BattleSpace;
import com.mygdx.game.entities.Astroid;
import com.mygdx.game.entities.Bullet;
import com.mygdx.game.entities.Explosion;
import com.mygdx.game.tools.CollisionRect;

import java.util.ArrayList;
import java.util.Random;

public class MainGameScreen implements Screen{



    public  static final float speed = 220;


    public  static final float SHIP_ANIMATION_SPEED = 0.5f;
    public  static final int SHIP_WIDTH_PIXEL = 17;
    public  static final int SHIP_HEIGHT_PIXEL = 32;
    public  static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
    public  static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
    public  static final float ROLL_TIMER_SWITCH_TIME = 0.15f;
    public static final float SHOOT_WAIT_TIME = 0.3f;
    public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;

    Animation[] rolls;


    float x  ;
    float y ;
    float stateTime ;
    float roltimer;
    float shootTimer;
    float asteroidSpawnTimer;
    Random random;
    int roll ;
    int score;
    BattleSpace game;
    ArrayList<Bullet> bullets;
    ArrayList<Astroid> astroids;
    ArrayList<Explosion> explosions;
    BitmapFont scoreFont;
    float Health = 1;
    Texture blank;
    CollisionRect  playerRect;
    public MainGameScreen(BattleSpace game)
    {

        this.game = game;
        y = 15;
        x = BattleSpace.WIDTH / 2 - SHIP_WIDTH/2;

        bullets = new ArrayList<Bullet>();
        astroids = new ArrayList<Astroid>();
        explosions = new ArrayList<Explosion>();
        random = new Random();
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        blank = new Texture("blank.png");
        playerRect = new CollisionRect(0,0,SHIP_WIDTH,SHIP_HEIGHT);
        score = 0;
        asteroidSpawnTimer = random.nextFloat() *( MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        roll = 2;
        roltimer = 0;
        shootTimer = 0;
        rolls = new Animation[5];
        TextureRegion[][] rollsSpriteSheet =  TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);

        rolls[0] =  new Animation(  SHIP_ANIMATION_SPEED,rollsSpriteSheet[2]); // all left
        rolls[1] =  new Animation(  SHIP_ANIMATION_SPEED,rollsSpriteSheet[1]);
        rolls[2] =  new Animation(  SHIP_ANIMATION_SPEED,rollsSpriteSheet[0]); // no tile
        rolls[3] =  new Animation(  SHIP_ANIMATION_SPEED,rollsSpriteSheet[3]);
        rolls[4] =  new Animation(  SHIP_ANIMATION_SPEED,rollsSpriteSheet[4]);// right


    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Shooting
        shootTimer += delta;
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= SHOOT_WAIT_TIME)
        {
            shootTimer = 0;
            int offset = 4;
            if(roll == 1 || roll == 3)
                offset = 8;
            if(roll == 0 || roll == 4)
                offset = 16;
            bullets.add(new Bullet(x+offset));
            bullets.add(new Bullet(x+SHIP_WIDTH-offset));

        }

        // Asteroid Spawn Code
        asteroidSpawnTimer -= delta;
        if(asteroidSpawnTimer <= 0) {

            asteroidSpawnTimer = random.nextFloat() *(MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            astroids.add(new Astroid(random.nextInt(BattleSpace.WIDTH - Astroid.WIDTH)));

        }
        // update Asteroids

        ArrayList<Astroid> astroidsToRemove = new ArrayList<Astroid>();
        for(Astroid astroid : astroids)
        {
            astroid.update(delta);
            if(astroid.remove)
            {
                astroidsToRemove.add(astroid);
            }
        }

        //updating Bullets
        ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets)
        {
            bullet.update(delta);
            if(bullet.remove)
            {
                bulletsToRemove.add(bullet);
            }
        }
        ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
        for(Explosion explosion: explosions)
        {
            explosion.update(delta);
            if(explosion.remove)
            {
                explosionsToRemove.add(explosion);
            }
        }
        explosions.removeAll(explosionsToRemove);



        //ship Movement
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            x += speed * Gdx.graphics.getDeltaTime();
            if(x + SHIP_WIDTH > Gdx.graphics.getWidth())
            {
                x = Gdx.graphics.getWidth() - SHIP_WIDTH;
            }
            roltimer  += Gdx.graphics.getDeltaTime();
            if(Math.abs(roltimer) > ROLL_TIMER_SWITCH_TIME)
            {
                roltimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
                if(roll > 4)
                {
                    roll = 4;
                }
            }

        }
        else
        {
            if(roll > 2)
            {
                roltimer  -= Gdx.graphics.getDeltaTime();
                if(Math.abs(roltimer) > ROLL_TIMER_SWITCH_TIME)
                {
                    roltimer -= ROLL_TIMER_SWITCH_TIME;
                    roll--;
                    if(roll < 0)
                    {
                        roll = 0;
                    }
                }
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            x -= speed * Gdx.graphics.getDeltaTime();
            {
                if(x < 0)
                    x = 0;
                roltimer  -= Gdx.graphics.getDeltaTime();
                if(Math.abs(roltimer) > ROLL_TIMER_SWITCH_TIME)
                {
                    roltimer -= ROLL_TIMER_SWITCH_TIME;
                    roll--;
                    if(roll < 0)
                    {
                        roll = 0;
                    }
                }
            }
        }
        else
        {
            if(roll < 2)
            {
                roltimer  += Gdx.graphics.getDeltaTime();
                if(Math.abs(roltimer) > ROLL_TIMER_SWITCH_TIME)
                {
                    roltimer -= ROLL_TIMER_SWITCH_TIME;
                    roll++;
                    if(roll > 4)
                    {
                        roll = 4;
                    }
                }
            }
        }
        stateTime += delta;
        //
        playerRect.move(x,y);
        //After All Update , Check for Collision
        for(Bullet bullet : bullets)
        {
            for (Astroid astroid : astroids)
            {
                if(bullet.getCollisionRect().collidesWith(astroid.getCollisionRect()))
                {
                    bulletsToRemove.add(bullet);
                    astroidsToRemove.add((astroid));
                    explosions.add(new Explosion(astroid.getX(), astroid.getY()));
                    score += 100;
                }
            }
        }

        bullets.removeAll(bulletsToRemove);

        for (Astroid astroid : astroids) {
            if (astroid.getCollisionRect().collidesWith(playerRect)) {
                astroidsToRemove.add(astroid);
                Health -= 0.1;
                int p = 0;
                //If health is depleted, go to game over screen
                if (Health <= 0)
                {
                    game.setScreen(new GameOverScreen(game, score));

                    return;
                }

            }
        }

        astroids.removeAll(astroidsToRemove);

        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont,"" + score);
        scoreFont.draw(game.batch, scoreLayout, BattleSpace.WIDTH / 2 - scoreLayout.width / 2, BattleSpace.HEIGHT - scoreLayout.height - 10);

        for(Bullet bullet : bullets)
        {
            bullet.render(game.batch);
        }
        for(Astroid astroid : astroids)
        {
            astroid.render(game.batch);
        }
        for(Explosion explosion : explosions)
        {
            explosion.render(game.batch);
        }

        if (Health > 0.6f)
            game.batch.setColor(Color.GREEN);
        else if (Health > 0.2f)
            game.batch.setColor(Color.ORANGE);
        else
            game.batch.setColor(Color.RED);

        game.batch.draw(blank, 0, 0, BattleSpace.WIDTH * Health, 5);
        game.batch.setColor(Color.WHITE);
        game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime,true),x,y, (float) SHIP_WIDTH,SHIP_HEIGHT);
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
    public void dispose () {
        game.batch.dispose();
        //game.batch.img.dispose();
    }
}

package com.sfz.space_rocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.sfz.space_rocks.SpaceRocks;

public class SplashScreen implements Screen {
	private SpaceRocks game;
	private AssetManager assets;

	private SpriteBatch batch;
	private Texture logoTexture;
	private Sprite logo;

	private float fadeValue = 0;
	private boolean fadeSwitch = false;
	private float fadeTimer = 1.0f;

	public SplashScreen(SpaceRocks game, AssetManager assets) {
		this.game = game;
		this.assets = assets;
		batch = new SpriteBatch();
		logoTexture = assets.get("data/logo.png", Texture.class);
		logo = new Sprite(logoTexture);
		logo.setPosition(Gdx.graphics.getWidth() / 2 - logoTexture.getWidth()
				/ 2, Gdx.graphics.getHeight() / 2 - logoTexture.getHeight() / 2);
		logo.setColor(1, 1, 1, fadeValue);

		assets.load("data/pack", TextureAtlas.class);
		assets.load("data/explosion.png", Texture.class);
		assets.load("data/ship.png", Texture.class);
		assets.load("data/asteroid.png", Texture.class);
		assets.load("data/yellow.png", Texture.class);
		assets.load("data/music.mp3", Music.class);
		assets.load("data/shot.wav", Sound.class);
		assets.load("data/explosion.wav", Sound.class);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (assets.update() && fadeSwitch && fadeValue == 0) {
			game.setScreen(game.mainMenu);
		} else {
			if (fadeValue < 1 && !fadeSwitch) {
				fadeValue += delta / 1.2;
				if (fadeValue > 1) {
					fadeValue = 1;
					fadeSwitch = true;
				}
			}

			if (fadeSwitch && fadeValue > 0) {
				if (fadeTimer > 0) {
					fadeTimer -= delta;
				} else {
					fadeValue -= delta;
					if (fadeValue < 0) {
						fadeValue = 0;
					}
				}
			}

			logo.setColor(1, 1, 1, fadeValue);
			batch.begin();
			logo.draw(batch);
			batch.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}

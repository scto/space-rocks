package com.sfz.space_rocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sfz.space_rocks.SpaceRocks;

public class Scores implements Screen {
	boolean loaded = false;
	AssetManager assets;
	SpriteBatch batch = new SpriteBatch();
	SpaceRocks game;
	int starsToFillWidth = 0;
	int starsToFillHeight = 0;
	TextureRegion stars;
	TextureAtlas atlas;
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	float xScale = width / 1920f;
	float yScale = height / 1080f;
	BitmapFont font;

	public Scores(SpaceRocks game, AssetManager assets) {
		this.game = game;
		this.assets = assets;
		font = new BitmapFont(Gdx.files.internal("data/pixel.fnt"), false);
		font.setScale(yScale * 5);

	}

	public void load() {
		if (loaded) {
			return;
		} else {
			loaded = true;
			atlas = assets.get("data/pack", TextureAtlas.class);
			stars = atlas.findRegion("stars");

			while (starsToFillWidth * stars.getRegionWidth() <= width) {
				starsToFillWidth++;
			}

			while (starsToFillHeight * stars.getRegionHeight() <= height) {
				starsToFillHeight++;
			}
		}
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyPressed(Keys.BACK)) {
			game.setScreen(game.mainMenu);
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		drawStars();
		drawGUI();

		batch.end();

	}

	private void drawGUI() {
		font.draw(batch, "Scores", width / 2 - font.getSpaceWidth() * 4f, height - font.getCapHeight());
	
		
	}

	private void drawStars() {
		for (int x = 0; x < starsToFillWidth; x++) {
			for (int y = 0; y < starsToFillHeight; y++) {
				batch.draw(stars, x * 512, y * 512, 0, 0, 512, 512, 1, 1, 0);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.input.setCatchBackKey(true);
		load();
	}

	@Override
	public void hide() {
		Gdx.input.setCatchBackKey(false);

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

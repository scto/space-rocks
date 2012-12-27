package com.sfz.space_rocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sfz.space_rocks.SpaceRocks;

public class MainMenu implements Screen {
	SpaceRocks game;
	AssetManager assets;
	Camera cam;

	TextureAtlas atlas;
	SpriteBatch batch;
	Sprite background;
	Sprite title;
	Sprite start;
	Sprite scores;

	Rectangle titleBounds;
	Rectangle startBounds;
	Rectangle scoresBounds;

	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();

	float xScale = width / 1920f;
	float yScale = height / 1080f;

	Vector3 touchPos;

	public MainMenu(SpaceRocks game, AssetManager assets) {
		this.game = game;
		this.assets = assets;

		cam = new OrthographicCamera();
		((OrthographicCamera) cam).setToOrtho(false, width, height);
		cam.rotate(0, 0, 0, 1);
		cam.position.set(width / 2, height / 2, 0);
		touchPos = new Vector3();
	}

	@Override
	public void render(float delta) {
		update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		title.draw(batch);
		start.draw(batch);
		scores.draw(batch);
		batch.end();

	}

	public void update() {
		for (int pointer = 0; pointer < 4; pointer++) {
			if (Gdx.input.justTouched()) {

				if (Gdx.input.isTouched(pointer)) {
					cam.unproject(touchPos.set(Gdx.input.getX(pointer),
							Gdx.input.getY(pointer), 0));
				}
				if (titleBounds.contains(touchPos.x, touchPos.y)) {
					Gdx.app.log("MainMenu", "Title touched");
				} else if (startBounds.contains(touchPos.x, touchPos.y)) {
					Gdx.app.log("MainMenu", "Start touched");
					Gdx.input.vibrate(9);
					game.setScreen(game.gameScreen);
					game.gameScreen.gameStart();
				} else if (scoresBounds.contains(touchPos.x, touchPos.y)) {
					Gdx.app.log("MainMenu", "Scores touched");
					Gdx.input.vibrate(9);
					game.setScreen(game.scoreScreen);
				}

			}
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		game.music = assets.get("data/music.mp3", Music.class);
		game.music.play();
		atlas = assets.get("data/pack", TextureAtlas.class);
		batch = new SpriteBatch();
		background = atlas.createSprite("background1");
		background.setOrigin(0, 0);
		background.setScale(xScale, yScale);

		title = atlas.createSprite("title");
		title.setScale(xScale, yScale);
		title.setOrigin(0, 0);
		title.setPosition((width / 2) - (title.getWidth() * xScale / 2),
				(height * 3 / 4) - (title.getHeight() * yScale / 2));
		titleBounds = new Rectangle((width / 2)
				- (title.getWidth() * xScale / 2), (height * 3 / 4)
				- (title.getHeight() * yScale / 2), title.getWidth() * xScale,
				title.getHeight() * yScale);

		start = atlas.createSprite("start");
		start.setScale(xScale, yScale);
		start.setOrigin(0, 0);
		start.setPosition(width / 2 - start.getWidth() * xScale / 2, height * 2
				/ 5 - start.getHeight() * yScale / 2);
		startBounds = new Rectangle((width - start.getWidth() * xScale) / 2,
				(height - start.getHeight() * yScale) * 2 / 5, start.getWidth()
						* xScale, start.getHeight() * yScale);

		scores = atlas.createSprite("scores");
		scores.setScale(xScale, yScale);
		scores.setOrigin(0, 0);
		scores.setPosition(width / 2 - scores.getWidth() * xScale / 2, height
				* 1 / 5 - scores.getHeight() * yScale / 2);
		scoresBounds = new Rectangle((width - scores.getWidth() * xScale) / 2,
				height * 1 / 5 - scores.getHeight() * yScale / 2,
				scores.getWidth() * xScale, scores.getHeight() * yScale);
		
		Gdx.input.setCatchBackKey(false);

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}

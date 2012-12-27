package com.sfz.space_rocks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.sfz.space_rocks.screens.GameOver;
import com.sfz.space_rocks.screens.GameScreen;
import com.sfz.space_rocks.screens.MainMenu;
import com.sfz.space_rocks.screens.Scores;
import com.sfz.space_rocks.screens.SplashScreen;

public class SpaceRocks extends Game {
	AssetManager assets;

	private boolean gameStarted = false;

	public SplashScreen splashScreen;
	public MainMenu mainMenu;
	public GameScreen gameScreen;
	public Scores scoreScreen;
	public GameOver gameOver;
	public Music music;
	public StillModel asteroidModel;
	public StillModel shipModel;
	public StillModel missileModel;
	
	public int finalScore = 0;

	@Override
	public void create() {
		assets = new AssetManager();
		assets.load("data/logo.png", Texture.class);
	}

	@Override
	public void dispose() {
		Gdx.app.log("Asteroids", "Disposing"); //
		assets.dispose();
	}

	@Override
	public void render() {
		super.render();
		if (assets.update()) {
			if (gameStarted) {

			} else {
				asteroidModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/asteroid.obj"));
				missileModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/missile.obj"));
				shipModel = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/spaceship.obj"));
				gameStarted = true;
				splashScreen = new SplashScreen(this, assets);
				mainMenu = new MainMenu(this, assets);
				gameScreen = new GameScreen(this, assets);
				scoreScreen = new Scores(this, assets);
				gameOver = new GameOver(this, assets);
				setScreen(splashScreen);
				Gdx.app.log("Asteroids", "Game Started"); //
			}
		} else {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void pause() {
		Gdx.app.log("SpaceRocks", "Space Rocks Paused");
		getScreen().pause();
	}

	@Override
	public void resume() {
		Gdx.app.log("SpaceROcks", "Space ROcks Resumed)");
		getScreen().resume();
	}
}

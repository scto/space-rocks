package com.sfz.space_rocks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.LightManager;
import com.badlogic.gdx.graphics.g3d.lights.LightManager.LightQuality;
import com.badlogic.gdx.graphics.g3d.test.PrototypeRendererGL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.sfz.space_rocks.Asteroid;
import com.sfz.space_rocks.Explosion;
import com.sfz.space_rocks.GameObject;
import com.sfz.space_rocks.Missile;
import com.sfz.space_rocks.Ship;
import com.sfz.space_rocks.SpaceRocks;

public class GameScreen implements Screen {
	SpaceRocks game;
	AssetManager assets;
	Camera cam;
	static final byte RUNNING = 1, PAUSED = 2, RESUMING = 3, GAMEOVER = 4;
	Byte gameState = RUNNING;

	int lives = 3;
	int score = 10;

	boolean loaded;
	TextureAtlas atlas;
	SpriteBatch batch;
	SpriteBatch batch2;
	TextureRegion stars;

	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();

	float xScale = width / 1920f;
	float yScale = height / 1080f;

	int starsToFillWidth = 0;
	int starsToFillHeight = 0;

	Ship ship;
	float timer = 0;
	GameObject[] objects;

	LightManager lights;
	Texture texture;
	PrototypeRendererGL20 renderer;

	Vector3 xAxis;
	Vector3 yAxis;
	Vector3 zAxis;
	Matrix4 temp;

	Vector3 moveTouchOrigin;
	Vector3 moveTouchPos;
	int moveTouchPointer = -1;

	BitmapFont font;

	float[] fingerTimer = new float[4];
	boolean[] fingerTouched = new boolean[4];
	boolean collision = false;
	ShapeRenderer shapes;
	Vector3 tempVect;
	float fpsTimer = 0;
	float fps = 0;

	boolean[] keyBuffer = new boolean[4];

	public GameScreen(SpaceRocks game, AssetManager assets) {
		this.game = game;
		this.assets = assets;

		cam = new PerspectiveCamera(40, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 0.1f;
		cam.far = 300f;
		cam.position.set(0, 0, 100);
		cam.lookAt(0, 0, 0);
		cam.update();

		loaded = false;

		xAxis = new Vector3(1, 0, 0);
		yAxis = new Vector3(0, 1, 0);
		zAxis = new Vector3(0, 0, 1);
		moveTouchOrigin = new Vector3();
		moveTouchPos = new Vector3();
		font = new BitmapFont(Gdx.files.internal("data/pixel.fnt"), false);

		font.setScale(yScale * 2);

		shapes = new ShapeRenderer();
		tempVect = new Vector3();
		gameState = 0;
	}

	void gameStart() {
		objects = new GameObject[1];
		ship = new Ship(game);
		objects[0] = ship;

		Asteroid asteroid;
		for (int i = 0; i < 4; i++) {
			asteroid = new Asteroid(game, 100 - (float) Math.random() * 200, 100 - (float) Math.random() * 200);
			asteroid.vel.set(10 - (float) Math.random() * 20, 10 - (float) Math.random() * 20, 0);
			addObject(asteroid);
		}

		lives = 3;
		score = 0;
	}

	private void explodeShip(Ship ship, GameObject exploder) {
		lives--;
		removeObject(ship);
		if (exploder.getClass() == Asteroid.class) {
			explodeAsteroid((Asteroid) exploder, ship);
		} else {
			removeObject(exploder);
		}

		Sound sound = assets.get("data/explosion.wav", Sound.class);
		sound.play();
		addObject(new Explosion(assets, ship.pos.x, ship.pos.y));

		if (lives > 0) {
			this.ship = (Ship) addObject(new Ship(game));
		} else {
			gameState = GAMEOVER;
			game.finalScore = score;
		}
	}

	private void copyObjectArray(GameObject[] original, GameObject[] copy) {
		int index = 0;
		for (GameObject object : original) {
			copy[index++] = object;
		}
	}

	private GameObject addObject(GameObject object) {
		GameObject[] temp = new GameObject[objects.length];
		copyObjectArray(objects, temp);
		objects = new GameObject[objects.length + 1];
		copyObjectArray(temp, objects);
		objects[objects.length - 1] = object;
		temp = null;
		return object;
	}

	private void load() {
		if (loaded) {
			return;
		} else {
			Gdx.app.log("GameScreen loadSprites()", "sprites loaded"); //
			loaded = true;
			atlas = assets.get("data/pack", TextureAtlas.class);
			batch = new SpriteBatch();
			batch2 = new SpriteBatch();
			stars = atlas.findRegion("stars");

			lights = new LightManager(10, LightQuality.VERTEX);

			lights.dirLight = new DirectionalLight();
			lights.dirLight.color.set(0.03706684f, 0.03706684f, 0.03706684f, 1.0f);
			lights.dirLight.direction.set(1, -1, -1).nor();

			lights.ambientLight.set(0.94760674f, 0.94760674f, 0.94760674f, 1.0f);

			renderer = new PrototypeRendererGL20(lights);
			renderer.cam = cam;

			while (starsToFillWidth * stars.getRegionWidth() <= width) {
				starsToFillWidth++;
			}

			while (starsToFillHeight * stars.getRegionHeight() <= height) {
				starsToFillHeight++;
			}

			// gameStart();
		}
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			if (!keyBuffer[2]) {
				keyBuffer[2] = true;
				if (gameState != PAUSED) {
					gameState = PAUSED;
				} else {
					gameState = RESUMING;
				}
			}
		} else {
			keyBuffer[2] = false;
		}

		if (gameState == RUNNING) {
			update(delta);
		} else if (gameState == RESUMING) {
			gameState = RUNNING;
			return;
		} else if (gameState == PAUSED) {
			return;
		} else if (gameState == GAMEOVER) {
			game.setScreen(game.gameOver);
		}

		checkForAsteroids();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Gdx.gl.glCullFace(GL20.GL_FRONT);
		// batch2.setProjectionMatrix(cam.combined);
		batch.begin();
		drawStars();
		drawGUI();
		if ((fpsTimer += delta) > 0.1f) {
			fpsTimer = 0.0f;
			fps = 1 / delta;
		}
		font.draw(batch, String.format("%-3.0f", fps), 0, font.getCapHeight());
		// font.draw(batch, String.valueOf(ship.boundingSphere.radius), 10, 50);
		batch.end();

		batch2.begin();
		for (GameObject object : objects) {
			if (object.getClass() == Explosion.class) {
				((Explosion) object).draw(delta, batch2, ((PerspectiveCamera) cam));
				if (((Explosion) object).finished) {
					removeObject(object);
				}
			}
		}
		batch2.end();

		Gdx.gl.glCullFace(GL20.GL_BACK);
		renderer.begin();

		for (GameObject object : objects) {
			if (object.getClass() == Asteroid.class) {
				((Asteroid) object).draw(renderer);
				((Asteroid) object).update(delta);
			} else if (object.getClass() == Ship.class) {
				((Ship) object).draw(renderer);
				((Ship) object).update(delta);
			} else if (object.getClass() == Missile.class) {
				((Missile) object).draw(renderer);
				((Missile) object).update(delta);
			}
		}

		renderer.end();

		// shapes.setProjectionMatrix(cam.combined);
		// shapes.begin(ShapeRenderer.ShapeType.Circle);
		// shapes.setColor(1f, 1f, 1f, 1);
		// for (GameObject object : objects) {
		// shapes.circle(object.pos.x, object.pos.y,
		// object.boundingSphere.radius, 24);
		// }
		// shapes.end();

	}

	private void update(float delta) {
		collision = false;
		for (GameObject object : objects) {
			if (object == null) {
				continue;
			}

			if (object.pos.x > 64f) {
				object.pos.x = -64f;
			} else if (object.pos.x < -64f) {
				object.pos.x = 64f;
			}

			if (object.pos.y > 34f) {
				object.pos.y = -34f;
			} else if (object.pos.y < -34) {
				object.pos.y = 34f;

			}

			if (object.getClass() == Explosion.class) {
				continue;
			}

			for (GameObject obj : objects) {
				if (obj == null) {
					continue;
				} else if (obj.getClass() == Explosion.class) {
					continue;
				}

				if (!object.equals(obj)) {
					if (object.boundingSphere.overlaps(obj.boundingSphere)) {
						if (object.getClass() == Missile.class) {
							if (((Missile) object).life < 1.4f) {
								if (obj.getClass() == Asteroid.class) {
									explodeAsteroid(((Asteroid) obj), object);
									continue;
								} else if (obj.getClass() == Ship.class) {
									explodeShip((Ship) obj, object);
								} else {
									removeObject(object);
									removeObject(obj);
									Sound sound = assets.get("data/explosion.wav", Sound.class);
									sound.play();
									addObject(new Explosion(assets, obj.pos.x, obj.pos.y));
								}
							}
						} else if (obj.getClass() == Missile.class) {
							if (((Missile) obj).life < 1.4f) {
								if (object.getClass() == Asteroid.class) {
									explodeAsteroid(((Asteroid) object), obj);
									continue;
								} else if (object.getClass() == Ship.class) {
									explodeShip((Ship) object, obj);
								} else {
									removeObject(object);
									removeObject(obj);
									Sound sound = assets.get("data/explosion.wav", Sound.class);
									sound.play();
									addObject(new Explosion(assets, obj.pos.x, obj.pos.y));
								}
							}
						} else if (object.getClass() == Asteroid.class && obj.getClass() == Asteroid.class) {
							object.vel.mul(-1);
							obj.vel.mul(-1);
							object.update(delta);
							obj.update(delta);
							object.update(delta);
							obj.update(delta);

						} else if (object.getClass() == Ship.class) {
							explodeShip((Ship) object, obj);
						} else if (obj.getClass() == Ship.class) {
							explodeShip((Ship) obj, object);
						} else {
							removeObject(object);
							removeObject(obj);
							Sound sound = assets.get("data/explosion.wav", Sound.class);
							sound.play();
							addObject(new Explosion(assets, object.pos.x, object.pos.y));
						}
					}
				}
			}

			if (object.getClass() == Missile.class) {
				((Missile) object).update(delta);
				if (((Missile) object).life < 0) {
					// ship.removeMissile(((Missile) object));
					removeObject(object);
				}
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			if (!keyBuffer[0]) {
				keyBuffer[0] = true;
				addObject(new Asteroid(game, 0, 0));
				objects[objects.length - 1].vel.set(10f - (float) Math.random() * 20, 10f - (float) Math.random() * 20, 0);
			}
		} else {
			keyBuffer[0] = false;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.B)) {
			if (!keyBuffer[1]) {
				keyBuffer[1] = true;
				addObject(new Ship(game));
				ship = (Ship) objects[objects.length - 1];
			}
		} else {
			keyBuffer[1] = false;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.G)) {
			if (!keyBuffer[1]) {
				keyBuffer[1] = true;
				game.setScreen(game.gameOver);
			}
		} else {
			keyBuffer[2] = false;
		}

		for (int pointer = 0; pointer < 4; pointer++) {
			if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
				game.setScreen(game.mainMenu);
			}

			if (Gdx.input.isTouched(pointer)) {
				if (fingerTouched[pointer] == false) {
					fingerTouched[pointer] = true;
					fingerTimer[pointer] = 0;
				} else {
					fingerTimer[pointer] += delta;
				}

				if (moveTouchPointer == -1) {
					moveTouchPointer = pointer;
					cam.unproject(moveTouchOrigin.set(Gdx.input.getX(pointer), Gdx.input.getY(pointer), 0));
				} else if (moveTouchPointer == pointer) {
					cam.unproject(moveTouchPos.set(Gdx.input.getX(pointer), Gdx.input.getY(pointer), 0));

					ship.accel.x = 255 * (moveTouchPos.x - moveTouchOrigin.x);
					ship.accel.y = 255 * (moveTouchPos.y - moveTouchOrigin.y);
				}

			} else {
				if (fingerTouched[pointer] && fingerTimer[pointer] < 0.2f) {
					Missile missile = ship.fire();
					if (missile != null) {
						addObject(missile);
					}
				}

				fingerTouched[pointer] = false;

				if (moveTouchPointer == pointer) {
					moveTouchPointer = -1;
					ship.accel.set(0, 0, 0);
				}
			}
		}
	}

	private void removeObject(GameObject object) {
		int index = 0;
		boolean found = false;
		for (GameObject searchFor : objects) {
			if (searchFor.equals(object)) {
				found = true;
				objects[index] = null;
				break;
			}
			index++;
		}

		if (found) {
			GameObject[] temp = new GameObject[objects.length];
			copyObjectArray(objects, temp);
			objects = new GameObject[objects.length - 1];
			index = 0;
			for (GameObject tempObject : temp) {
				if (tempObject != null) {
					objects[index++] = tempObject;
				}
			}
		}

		temp = null;

	}

	private void explodeAsteroid(Asteroid asteroid, GameObject object) {
		Sound sound = assets.get("data/explosion.wav", Sound.class);
		sound.play();
		addObject(new Explosion(assets, asteroid.pos.x, asteroid.pos.y));
		removeObject(object);
		if (object.getClass() == Missile.class) {
			if (ship.equals(((Missile) object).firingShip)) {
				score++;
			}
		}
		if (asteroid.scale > 2) {
			asteroid.scale--;
			addObject(new Asteroid(game, asteroid.pos.x + asteroid.scale, asteroid.pos.y + asteroid.scale));
			((Asteroid) objects[objects.length - 1]).scale = asteroid.scale;
			((Asteroid) objects[objects.length - 1]).vel.set(asteroid.vel);
			((Asteroid) objects[objects.length - 1]).vel.mul(-1);
		} else {
			removeObject(asteroid);
		}

	}

	void drawStars() {
		for (int x = 0; x < starsToFillWidth; x++) {
			for (int y = 0; y < starsToFillHeight; y++) {
				batch.draw(stars, x * 512, y * 512, 0, 0, 512, 512, 1, 1, 0);
			}
		}
	}

	private void drawGUI() {
		font.draw(batch, "Lives: " + lives, xScale * 0, height);
		font.draw(batch, "Score: " + score, xScale * 400, height);
	}

	private void checkForAsteroids() {
		boolean found = false;
		for (GameObject object : objects) {
			if (object.getClass() == Asteroid.class) {
				found = true;
			}
		}
		if (!found) {
			Asteroid asteroid;
			for (int i = 0; i < 4; i++) {
				asteroid = (Asteroid) addObject(new Asteroid(game, 30 - (float) Math.random() * 30, 30 - (float) Math.random() * 30));
				asteroid.vel.set(10 - (float) Math.random() * 10, 10 - (float) Math.random() * 10, 0);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glFrontFace(GL20.GL_CCW);

		Gdx.input.setCatchBackKey(true);
		load();
		gameState = RESUMING;
	}

	@Override
	public void hide() {
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_CCW);
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void pause() {
		gameState = PAUSED;
		Gdx.app.log("GameScreen", "Pau222sed");
		game.setScreen(game.mainMenu);

	}

	@Override
	public void resume() {
		gameState = RESUMING;

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}

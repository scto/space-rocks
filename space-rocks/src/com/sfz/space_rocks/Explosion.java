package com.sfz.space_rocks;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Explosion extends GameObject {
	private static final int FRAME_COLS = 4;
	private static final int FRAME_ROWS = 4;

	float stateTime;

	Animation animation;
	Texture sheet;
	TextureRegion[] walkFrames;
	TextureRegion currentFrame;
	public boolean finished = false;
	Vector3 spritePos = new Vector3();
	Vector3 origin = new Vector3();

	public Explosion(AssetManager assets, float x, float y) {

		sheet = assets.get("data/explosion.png", Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / FRAME_COLS, sheet.getHeight() / FRAME_ROWS);
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		animation = new Animation(0.1f, walkFrames);
		stateTime = 0f;
		pos.x = x;
		pos.y = y;
	}

	public void draw(float delta, SpriteBatch batch, PerspectiveCamera cam) {
		super.update(delta);
		stateTime += delta;
		currentFrame = animation.getKeyFrame(stateTime, false);
		cam.project(spritePos.set(pos));
		cam.project(origin.set(-currentFrame.getRegionWidth() / 2, -currentFrame.getRegionHeight() / 2, 0));
		batch.draw(currentFrame, spritePos.x, spritePos.y);
		if (animation.isAnimationFinished(stateTime)) {
			finished = true;
		}
	}
}

package com.sfz.space_rocks;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.StillModelNode;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.test.PrototypeRendererGL20;
import com.badlogic.gdx.math.Vector3;

public class Missile extends GameObject {
	StillModel model;
	StillModelNode node;
	Texture texture;
	float f = 0;
	public Ship firingShip;

	public float life = 1.5f;
	Vector3 bboxMinOrigin = new Vector3();
	Vector3 bboxMaxOrigin = new Vector3();

	public Missile(SpaceRocks game) {
		terminalSpeed.set(20, 20, 0);

		model = game.missileModel;
		texture = game.assets.get("data/yellow.png", Texture.class);
		MaterialAttribute m1 = new TextureAttribute(texture, 0, TextureAttribute.diffuseTexture);

		Material mat = new Material("basic", m1);
		model.setMaterial(mat);
		node = new StillModelNode();

		model.getBoundingBox(boundingBox);
		bboxMinOrigin.set(boundingBox.min);
		bboxMaxOrigin.set(boundingBox.max);
		update(0);
		
		Sound sound = game.assets.get("data/shot.wav", Sound.class);
		sound.play();

	}

	public void update(float delta) {
		super.update(delta);

		life -= delta;

		rot = (float) Math.atan2(vel.y, vel.x) * 180 / 3.14159f + 90;
		node.matrix.setFromEulerAngles(00, 0, rot);
		node.matrix.val[12] = pos.x;
		node.matrix.val[13] = pos.y;
		node.matrix.val[14] = pos.z;
		node.matrix.scale(.5f, .5f, .5f);

		boundingBox.min.set(bboxMinOrigin.x + pos.x, bboxMinOrigin.y + pos.y, bboxMinOrigin.z + pos.z);
		boundingBox.max.set(bboxMaxOrigin.x + pos.x, bboxMaxOrigin.y + pos.y, bboxMaxOrigin.z + pos.z);
		boundingSphere.radius = boundingBox.getDimensions().x;
		boundingSphere.center.set(pos);
	}

	public void draw(PrototypeRendererGL20 renderer) {
		renderer.draw(model, node);
	}
}

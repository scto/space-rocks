package com.sfz.space_rocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.StillModelNode;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.test.PrototypeRendererGL20;
import com.badlogic.gdx.math.Vector3;

public class Asteroid extends GameObject {
	StillModel model;
	StillModelNode node;
	Texture texture;
	float f = 0;
	public int scale = 4;
	
	Vector3 bboxMinOrigin = new Vector3();
	Vector3 bboxMaxOrigin = new Vector3();
	
	public Asteroid(SpaceRocks game, float x, float y) {
		terminalSpeed.set(20, 20, 0);
		
		model = game.asteroidModel;
		texture = game.assets.get("data/asteroid.png", Texture.class);
		MaterialAttribute m1 = new TextureAttribute(texture, 0, TextureAttribute.diffuseTexture);
		Material mat = new Material("basic", m1);
		model.setMaterial(mat);
		
		node = new StillModelNode();
		pos.x = x;
		pos.y = y;
		
		model.getBoundingBox(boundingBox);
		bboxMinOrigin.set(boundingBox.min);
		bboxMaxOrigin.set(boundingBox.max);
		update(0);
	}

	public void update(float delta) {
		super.update(delta);
		f = (float) (Math.atan2(vel.y - 0, vel.x - 0) * 180 / 3.14159f);
		rot += 60 * delta;
		node.matrix.setFromEulerAngles(1 / rot, 2 / rot, rot);
		node.matrix.val[12] = pos.x;
		node.matrix.val[13] = pos.y;
		node.matrix.val[14] = pos.z;
		node.matrix.scale(scale, scale, scale);
		boundingBox.min.set(bboxMinOrigin.x + pos.x, bboxMinOrigin.y + pos.y, bboxMinOrigin.z + pos.z);
		boundingBox.max.set(bboxMaxOrigin.x + pos.x, bboxMaxOrigin.y + pos.y, bboxMaxOrigin.z + pos.z);
		
		boundingSphere.radius = boundingBox.getDimensions().x * scale / 2;
		boundingSphere.center.set(pos);
		
	}

	public void draw(PrototypeRendererGL20 renderer) {
		renderer.draw(model, node);
	}
}

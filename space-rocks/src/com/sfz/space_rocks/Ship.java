package com.sfz.space_rocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.StillModelNode;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.test.PrototypeRendererGL20;
import com.badlogic.gdx.math.Vector3;

public class Ship extends GameObject {
	StillModel model;
	public StillModelNode node;
	Texture texture;
	float f = 0;
	// public Missile[] missiles = new Missile[0];
	SpaceRocks game;
	Vector3 bboxMinOrigin = new Vector3();
	Vector3 bboxMaxOrigin = new Vector3();
	public float fireDelay = 0;

	public Ship(SpaceRocks game) {
		this.game = game;
		model = game.shipModel;
		texture = game.assets.get("data/ship.png", Texture.class);
		MaterialAttribute m1 = new TextureAttribute(texture, 0, TextureAttribute.diffuseTexture);
		Material mat = new Material("basic", m1);
		model.setMaterial(mat);
		node = new StillModelNode();
		terminalSpeed.set(20, 20, 0);
		model.getBoundingBox(boundingBox);
		bboxMinOrigin.set(boundingBox.min);
		bboxMaxOrigin.set(boundingBox.max);
		update(0);

	}

	public Missile fire() {
		Missile missile;
		if (fireDelay >= 0.5) {
			//missile = addMissile();
			fireDelay = 0;
			missile = new Missile(game);
			missile.pos.set(pos);
			missile.rot = rot + 90;
			missile.vel.set(new Vector3((float) Math.cos((rot - 90) * 3.14159 / 180), (float) Math.sin((rot - 90) * 3.14159 / 180), 0).mul(75));
			missile.node.matrix.scale(.1f, .1f, .1f);
			missile.firingShip = this;
			return missile;
		} else {
			return null;
		}

	}

	/* private Missile addMissile() {
		Missile[] temp = new Missile[missiles.length];
		copyMissileArray(missiles, temp);
		missiles = new Missile[missiles.length + 1];
		copyMissileArray(temp, missiles);
		missiles[missiles.length - 1] = new Missile(game);
		missiles[missiles.length - 1].pos.set(pos);
		missiles[missiles.length - 1].rot = rot + 90;
		missiles[missiles.length - 1].vel.set(new Vector3((float) Math.cos((rot - 90) * 3.14159 / 180), (float) Math.sin((rot - 90) * 3.14159 / 180), 0).mul(75));
		missiles[missiles.length - 1].node.matrix.scale(.1f, .1f, .1f);
		return missiles[missiles.length - 1];

	} */

	/* public void removeMissile(Missile missile) {
		int index = 0;
		boolean found = false;
		for (Missile miss : missiles) {
			if (miss.equals(missile)) {
				found = true;
				missiles[index] = null;
				break;
			}
			index++;
		}

		if (found) {
			Missile[] temp = new Missile[missiles.length];
			copyMissileArray(missiles, temp);
			missiles = new Missile[missiles.length - 1];

			index = 0;
			for (Missile miss : temp) {
				if (miss != null) {
					missiles[index++] = miss;
				}
			}
		}
	} */

	/* private void copyMissileArray(Missile[] original, Missile[] copy) {
		int index = 0;
		for (Missile missile : original) {
			copy[index++] = missile;
		}
	} */

	public void update(float delta) {
		super.update(delta);

		fireDelay += (fireDelay < 0.5f) ? delta : 0;

		f = (float) (Math.atan2(vel.y - 0, vel.x - 0) * 180 / 3.14159f);
		rot = (float) Math.atan2(vel.y, vel.x) * 180 / 3.14159f + 90;
		node.matrix.setFromEulerAngles(00, 0, rot);
		node.matrix.val[12] = pos.x;
		node.matrix.val[13] = pos.y;
		node.matrix.val[14] = pos.z;
		node.matrix.scale(2, 2, 2);

		boundingBox.min.set(bboxMinOrigin.x + pos.x, bboxMinOrigin.y + pos.y, bboxMinOrigin.z + pos.z);
		boundingBox.max.set(bboxMaxOrigin.x + pos.x, bboxMaxOrigin.y + pos.y, bboxMaxOrigin.z + pos.z);
		boundingSphere.radius = boundingBox.getDimensions().x;
		boundingSphere.center.set(pos);

	}

	public void draw(PrototypeRendererGL20 renderer) {
		renderer.draw(model, node);
	}
}

package com.sfz.space_rocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;

public class GameObject {
	public Vector3 pos;
	public Vector3 accel;
	public Vector3 vel;
	public Sprite model;
	public int ID;
	public float rot;
	
	public BoundingBox boundingBox;
	public Sphere boundingSphere;
	
	Vector3 terminalSpeed = new Vector3(20, 20, 20);

	Vector3 xAxis;
	Vector3 yAxis;
	Vector3 zAxis;

	GameObject() {
		pos = new Vector3();
		accel = new Vector3();
		vel = new Vector3();
		rot = 0;
		

		xAxis = new Vector3(1, 0, 0);
		yAxis = new Vector3(0, 1, 0);
		zAxis = new Vector3(0, 0, 1);
		
		boundingBox = new BoundingBox();
		boundingSphere = new Sphere(new Vector3(), 0);
	}

	public GameObject(float x, float y) {

		pos = new Vector3(x, y, 0);
		accel = new Vector3();
		vel = new Vector3();
		rot = 0;
		
		xAxis = new Vector3(1, 0, 0);
		yAxis = new Vector3(0, 1, 0);
		zAxis = new Vector3(0, 0, 1);
		boundingBox = new BoundingBox();
	}
	
	public void update(float delta) {
		vel.x += accel.x * delta;
		
		if (vel.len2() > terminalSpeed.len2()) {
			vel.x -= accel.x * delta;
		}
		
		vel.y += accel.y * delta;
		
		if (vel.len2() > terminalSpeed.len2()) {
			vel.y -= accel.y * delta;
		}
		
		vel.z += accel.z * delta;
		
		if (vel.len2() > terminalSpeed.len2()) {
			vel.z -= accel.z * delta;
		}
		
		pos.x += vel.x * delta;
		pos.y += vel.y * delta;
		pos.z += vel.z * delta;
		
		//rot = (float)  Math.atan2(vel.y, vel.x) * 180 / 3.14159f + 90;

	}
}

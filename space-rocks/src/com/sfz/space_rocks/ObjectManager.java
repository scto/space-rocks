package com.sfz.space_rocks;

import com.badlogic.gdx.Gdx;


public class ObjectManager {
	public GameObject[] gameObjects;
	GameObject[] copies;

	public ObjectManager() {
		gameObjects = new GameObject[0];
		copies = new GameObject[0];
	}

	public void addObject(GameObject object) {
		StringBuilder builder = new StringBuilder();
		builder.append(gameObjects.length);

		copies = new GameObject[copies.length + 1];
		for (int i = 0; i < gameObjects.length; i++) {
			copies[i] = gameObjects[i];
		}

		gameObjects = new GameObject[gameObjects.length + 1];
		for (int i = 0; i < copies.length; i++) {
			gameObjects[i] = copies[i];
		}

		gameObjects[gameObjects.length - 1] = object;
		object.ID = gameObjects.length - 1;
	}

	public void removeObject(GameObject object) {
		boolean foundMatch = false;
		for (int i = 0; i < gameObjects.length; i++) {
			if (gameObjects[i] == object) {
				gameObjects[i] = null;
				foundMatch = true;
			}
			if (foundMatch && (i < gameObjects.length - 1)) {
				gameObjects[i] = gameObjects[i+1];
			}
			if (foundMatch && (i == gameObjects.length - 1)) {
				gameObjects[i] = null;
			}
		}
		if (!foundMatch) {
			Gdx.app.log("ObjectManager", "removeObject() was called on an object that wasn't in the stack");
		}
	}
}

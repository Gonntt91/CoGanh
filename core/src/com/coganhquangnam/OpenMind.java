package com.coganhquangnam;


import com.badlogic.gdx.Game;
import com.coganhquangnam.Screen.PlayScreen;


public class OpenMind extends Game {

	@Override
	public void create() {
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
}

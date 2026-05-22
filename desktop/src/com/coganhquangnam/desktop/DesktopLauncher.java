package com.coganhquangnam.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.coganhquangnam.OpenMind;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Co Ganh Quang Nam");
		config.setWindowedMode(480, 800);
		new Lwjgl3Application(new OpenMind(), config);
	}
}

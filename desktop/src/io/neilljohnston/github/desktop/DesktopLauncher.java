package io.neilljohnston.github.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.neilljohnston.github.IWannaJump;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "I Wanna Jump";
		config.addIcon("icon-16x.png", Files.FileType.Internal);
		config.addIcon("icon-32x.png", Files.FileType.Internal);
		config.addIcon("icon-128x.png", Files.FileType.Internal);
		new LwjglApplication(new IWannaJump(), config);
	}
}

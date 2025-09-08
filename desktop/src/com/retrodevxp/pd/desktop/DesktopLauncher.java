package com.retrodevxp.pd.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.pixeldungeon.PixelDungeon;
import com.retrodevxp.pixeldungeon.Preferences;
import com.retrodevxp.utils.JVMPlatformSupport;

public class DesktopLauncher {
	public static void main (String[] arg) {
		String version = DesktopLauncher.class.getPackage().getSpecificationVersion();
		if (version == null) {
			// version = "???";
			version = "1.3.0";
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		if (SharedLibraryLoader.isMac) {
			config.preferencesDirectory = "Library/Application Support/Renewed Pixel Dungeon/";
		} else if (SharedLibraryLoader.isLinux) {
			config.preferencesDirectory = ".retrox/renewed-pixel-dungeon/";
		} else if (SharedLibraryLoader.isWindows) {
			config.preferencesDirectory = "Saved Games/RenewedPixelDungeon/";
		}
		// FIXME: This is a hack to get access to the preferences before we have an application setup
		com.badlogic.gdx.Preferences prefs = new LwjglPreferences(Preferences.FILE_NAME, config.preferencesDirectory);

		boolean isFullscreen = prefs.getBoolean(Preferences.KEY_WINDOW_FULLSCREEN, false);
		config.fullscreen = isFullscreen;
		if (!isFullscreen) {
			config.width = prefs.getInteger(Preferences.KEY_WINDOW_WIDTH, Preferences.DEFAULT_WINDOW_WIDTH);
			config.height = prefs.getInteger(Preferences.KEY_WINDOW_HEIGHT, Preferences.DEFAULT_WINDOW_HEIGHT);
		}

		config.addIcon( "ic_launcher_128.png", Files.FileType.Internal );
		config.addIcon( "ic_launcher_32.png", Files.FileType.Internal );
		config.addIcon( "ic_launcher_16.png", Files.FileType.Internal );

		// TODO: It have to be pulled from build.gradle, but I don't know how it can be done
		config.title = "Renewed Pixel Dungeon";

		new LwjglApplication(new PixelDungeon(
				new DesktopSupport(version, config.preferencesDirectory, new DesktopInputProcessor())
		), config);
	}

	public static class DesktopSupport extends JVMPlatformSupport {
		public DesktopSupport( String version, String basePath, NoosaInputProcessor inputProcessor ) {
			super( version, basePath, inputProcessor );
		}

		@Override
		public boolean isFullscreenEnabled() {
		//	return Display.getPixelScaleFactor() == 1f;
            return !SharedLibraryLoader.isMac;
		}
	}
}

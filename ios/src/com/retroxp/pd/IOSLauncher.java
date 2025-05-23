package com.retrodevxp.pd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.retrodevxp.pixeldungeon.PixelDungeon;
import com.retrodevxp.utils.JVMPlatformSupport;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
	    final String version = NSBundle.getMainBundle().getInfoDictionaryObject("CFBundleShortVersionString").toString();
        return new IOSApplication(new PixelDungeon(new JVMPlatformSupport(version, null, new IOSInputProcessor())), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}
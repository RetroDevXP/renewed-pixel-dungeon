package com.retrodevxp.utils;

import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.utils.PDPlatformSupport;

public class JVMPlatformSupport<GameActionType> extends PDPlatformSupport<GameActionType> {
    
    public JVMPlatformSupport(String version, String basePath, NoosaInputProcessor<GameActionType> inputProcessor) {
        super(version, basePath, inputProcessor);
    }

    public PDThread newThread(Runnable runnable) {
		return new JavaThread(runnable);
	}

    static class JavaThread extends Thread implements PDThread {
		public JavaThread(Runnable runnable) {
			super(runnable);
		}
	}
}

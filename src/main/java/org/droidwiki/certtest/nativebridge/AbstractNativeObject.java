package org.droidwiki.certtest.nativebridge;

public abstract class AbstractNativeObject implements NativeObject {
	private boolean freed = false;

	protected void checkFreed() {
		if (this.freed) {
			throw new IllegalAccessError("This instance of " + this.getClass().getName() + " has already been freed.");
		}
	}

	public void free() {
		checkFreed();
		this.freed = true;
	}
}

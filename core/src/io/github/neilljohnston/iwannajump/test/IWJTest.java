package io.github.neilljohnston.iwannajump.test;

import io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment;

public class IWJTest extends IWJEnvironment {
    @Override
    public void create() {
        super.create();

        setScreen(new IWJTestScreen(this, "test.tmx", 1280, 720));
    }
}

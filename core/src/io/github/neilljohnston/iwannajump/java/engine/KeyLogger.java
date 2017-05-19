package io.github.neilljohnston.iwannajump.java.engine;

import java.util.HashMap;

import static com.badlogic.gdx.Gdx.input;

/**
 * Allow easier tracking of input states and timings. Has methods and data structures for holding key timings, i.e.
 * how long it's been since a key was pressed/released.
 */
public class KeyLogger {
    /**
     * Holds the press-times for keys.
     */
    private HashMap<Integer, Float> onTimes;

    /**
     * Holds the release-times for keys.
     */
    private HashMap<Integer, Float> offTimes;



    /**
     * Construct a KeyLogger with initial keys to track.
     *
     * @param keys  A list of keys to track
     */
    public KeyLogger(int[] keys) {
        onTimes = new HashMap<Integer, Float>();
        offTimes = new HashMap<Integer, Float>();

        for(int k : keys) {
            onTimes.put(k, 0f);
            offTimes.put(k, 0f);
        }
    }

    /**
     * Step some time forward, continue tracking those KEYS, bro.
     *
     * @param delta Time since last step (s)
     */
    public void step(float delta) {
        // Iterate through all trackable keys, update times
        for(int k : onTimes.keySet()) {
            if (input.isKeyPressed(k)) {
                onTimes.put(k, onTimes.get(k) + delta);
                offTimes.put(k, 0f);
            } else {
                onTimes.put(k, 0f);
                offTimes.put(k, offTimes.get(k) + delta);
            }
        }
    }

    /**
     * Return the on-time for a specified key.
     *
     * @param key   Key to track
     * @return The amount of time that this key has been held
     */
    public float on(int key) {
        return onTimes.get(key);
    }

    /**
     * Return the off-time for a specified key.
     *
     * @param key   Key to track
     * @return The amount of time since this key was last held
     */
    public float off(int key) {
        return offTimes.get(key);
    }

    /**
     * Alias for input.isKeyPressed(key).
     *
     * @param key   Key to track
     * @return True if the key is pressed, false if not
     */
    public boolean is(int key) {
        return input.isKeyPressed(key);
    }

    /**
     * Add another key to the tracker
     *
     * @param key   Key to track
     */
    public void track(int key) {
        onTimes.put(key, 0f);
        offTimes.put(key, 0f);
    }
}

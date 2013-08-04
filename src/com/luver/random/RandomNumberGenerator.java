package com.luver.random;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Random;

public class RandomNumberGenerator implements CallbackClient<Long>, SensorEventListener {

    private final int times;
    private final int capacity;
    private int counter = 0;

    // Accumulators for seeds.
    private float xSeed, ySeed, zSeed;

    // Lets assume that we can have only one callback
    private Callback<Long> callback;

    public RandomNumberGenerator(int times, int capacity) {
        this.times = times;
        this.capacity = capacity;
    }

    @Override
    public void registerCallback(Callback<Long> callback) {
        // Every new callback replaces old one.
        this.callback = callback;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            if (counter > this.times) {
                collectingFinished();
            } else {
                applyNewValue(event.values);
                counter++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void applyNewValue(float[] value) {
        if (value.length < 3) {
            throw new IllegalArgumentException("Rotation vector is too short");
        }

        xSeed += value[0];
        ySeed += value[1];
        zSeed += value[2];
    }

    private void collectingFinished() {
        counter = 0;
        callback.callback(getRandomNumber());
    }

    private long getRandomNumber() {
        Random random = new Random(getSeed());
        return (long) (random.nextFloat() * Math.pow(10, capacity));
    }

    /**
     * Absolutely genius algorithm to get seed
     *
     * @return
     */
    private long getSeed() {
        double xSeed =  this.xSeed / times * 100000;
        double ySeed =  this.ySeed / times * 100000;
        double zSeed =  this.zSeed / times * 100000;

        return (long) ((xSeed + ySeed + zSeed) / 3);
    }
}

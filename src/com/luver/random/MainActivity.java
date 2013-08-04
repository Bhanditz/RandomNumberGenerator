package com.luver.random;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends Activity {
    static private final int TIME = 200;
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private SensorEventListener randomNumberGenerator;
    // Views
    private ProgressBar progressBar;
    private TextView textView;
    private EditText capacityBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.generatedNumber);
        capacityBox = (EditText) findViewById(R.id.capacityTextBox);
    }

    /**
     * 'Generate' button's tap handler
     * <p/>
     * Show progress bar, print "Shake your device" and activates rotation sensor listener,
     *
     * @param view
     */
    public void generateNumberHandler(View view) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        textView.setText(R.string.shake_message);

        int capacity = Integer.parseInt(capacityBox.getText().toString());
        activateSensorListener(capacity);
    }

    private void activateSensorListener(int capacity) {
        RandomNumberGenerator generator = new RandomNumberGenerator(TIME, capacity);
        this.randomNumberGenerator = generator;

        generator.registerCallback(new Callback<Long>() {

            /**
             * Deactivates rotation sensor listener.
             * Hide progress bar, replace message by number.
             * @param number
             */
            @Override
            public void callback(Long number) {
                deactivateSensorListener();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                textView.setText(Long.toString(number));
            }
        });

        sensorManager.registerListener(generator, rotationSensor, 10000);
    }

    private void deactivateSensorListener() {
        if (randomNumberGenerator != null) {
            sensorManager.unregisterListener(randomNumberGenerator);
        }
    }
}

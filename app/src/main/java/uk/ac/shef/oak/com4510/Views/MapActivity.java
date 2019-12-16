package uk.ac.shef.oak.com4510.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import uk.ac.shef.oak.com4510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com4510.Models.Accelerometer;
import uk.ac.shef.oak.com4510.Models.Barometer;
import uk.ac.shef.oak.com4510.Models.MyMap;
import uk.ac.shef.oak.com4510.Models.Temperature;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.ViewModels.CameraViewModel;
import uk.ac.shef.oak.com4510.ViewModels.MapViewModel;
import uk.ac.shef.oak.com4510.ViewModels.PhotoViewModel;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Button mButtonStart;
    private Button mButtonEnd;
    private Barometer barometer;
    private Temperature temperature;
    private Accelerometer accelerometer;
    private MyMap map;
    private Marker current_loc_marker;
    private MapViewModel mapViewModel;
    private Polyline polyline;
    private String tripName;
    private Chronometer chronometer;
    private TextView barometerValue;
    private TextView temperatureValue;
    private ImageView imageThumbnail;
    // Photo model attributes
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");

        barometerValue = findViewById(R.id.barometerValue);
        temperatureValue = findViewById(R.id.temperatureValue);

        Intent intent = getIntent();
        tripName = intent.getStringExtra("tripName");

        barometer = new Barometer(this);
        current_loc_marker = null;
        accelerometer = new Accelerometer(this, barometer);
        temperature = new Temperature(this);

        TextView textView = findViewById(R.id.mapTripName);
        textView.setText(tripName);

        // Temp image thumbnail
        imageThumbnail = findViewById(R.id.imageThumbnail);
        //

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getLocAndSensorDataLiveData().observe(this, new Observer<LocAndSensorData>() {
            @Override
            public void onChanged(@NonNull final LocAndSensorData locAndSensorData) {
                if (locAndSensorData != null) {
                    if (current_loc_marker == null) {
                        current_loc_marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude())));
                    } else {
                        current_loc_marker.setPosition(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude()));
                        Log.i("marker", "" + locAndSensorData.getLatitude() + " , " + locAndSensorData.getLongitude());
                    }

                    if (map.getStarted()) {
                        ArrayList<LatLng> latLngs = map.getLatLngs();
                        if (polyline == null)
                            polyline = googleMap.addPolyline(new PolylineOptions().addAll(latLngs).width(10).color(Color.RED));
                        else
                            polyline.setPoints(latLngs);
                    }
                    if (temperature.getLatestValue() != -1000)
                        temperatureValue.setText("Temperature: " + temperature.getLatestValue());
                    if (barometer.getLatestValue() != -1000)
                        barometerValue.setText("Barometer: " + barometer.getLatestValue());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude()), 16.0f));
                }

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        map = new MyMap(this, tripName, barometer, temperature, mapViewModel);

        mButtonStart = (Button) findViewById(R.id.startTripButton);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.startLocationUpdates();
                map.setStarted(true);
                barometerValue.setText("");
                temperatureValue.setText("");
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                temperature.startTemperatureSensor();

                accelerometer.startAccelerometerRecording();

                if (mButtonEnd != null)
                    mButtonEnd.setEnabled(true);
                mButtonStart.setEnabled(false);
            }
        });
        mButtonStart.setEnabled(true);

        mButtonEnd = (Button) findViewById(R.id.stopTripButton);
        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.stopLocationUpdates();
                map.setStarted(false);
                accelerometer.stopAccelerometer();
                temperature.stopTemperatureSensor();

                barometerValue.setText("Start");
                temperatureValue.setText("Record");

                chronometer.stop();
                if (mButtonStart != null)
                    mButtonStart.setEnabled(true);
                mButtonEnd.setEnabled(false);
            }
        });
        mButtonEnd.setEnabled(false);
        /**
         * Create take a photo button using CameraViewModel intent
         */
        final FloatingActionButton photoButton = (FloatingActionButton) findViewById(R.id.takePhotoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.resume();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        map.permissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * @param googleMap Check google map ui setting , then set google map settings attributes
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    /**
     * Dispatch intent to take a photo , to launch hardware camera
     *
     * @// TODO: Check has system feature camera
     * hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            // Create file
            File photoFile = null;
            try {
                PhotoViewModel photoViewModel = new PhotoViewModel(this);

                photoFile = photoViewModel.createImageFile();
            } catch (IOException ex) {
                // Error when create file
            }
            // continue file exists
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "uk.ac.shef.oak.com4510.android.fileProvider",
                        photoFile);
                // If no use thumbnails then there is no need to put extra info else
                // You need to find extra info from photoURI then rescale it again
                // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Create on camera take picture result show picture
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageThumbnail.setImageBitmap(imageBitmap);
        }
    }
}

package uk.ac.shef.oak.com6510.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import uk.ac.shef.oak.com6510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com6510.Models.Accelerometer;
import uk.ac.shef.oak.com6510.Models.Barometer;
import uk.ac.shef.oak.com6510.Models.MyMap;
import uk.ac.shef.oak.com6510.Models.Temperature;
import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.MapViewModel;
import uk.ac.shef.oak.com6510.ViewModels.PhotoViewModel;

import static java.lang.System.out;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Button mButtonEnd;
    private Barometer barometer;
    private Temperature temperature;
    private Accelerometer accelerometer;
    private MyMap map;
    private Marker currentLocMarker;
    private MapViewModel mapViewModel;
    private Polyline polyline;
    private String tripName;
    private Chronometer chronometer;
    private TextView barometerValue;
    private TextView temperatureValue;
    private ImageView imageThumbnail;
    // PhotoModel model attributes
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_UPLOAD_PHOTO = 2;
    static final Integer READ_STORAGE_PERMISSION_REQUEST_CODE = 0x3;
    private PhotoViewModel photoViewModel;

    /**
     * the things needs to be done when the activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initView();
        initSensor();
        initMapViewModel();
        initPhotoViewModel();
        initMap();

        startAll();

        mButtonEnd = (Button) findViewById(R.id.stopTripButton);
        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToNewTrip("");
            }
        });
        /**
         * Create take a photo button using CameraViewModel intent
         */
        final FloatingActionButton photoButton = (FloatingActionButton) findViewById(R.id.takePhotoButton);
        if (checkCameraExist(MapActivity.this, photoButton)) {
            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
        }
        final FloatingActionButton uploadPhotoButton = (FloatingActionButton) findViewById(R.id.uploadPhotoButton);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchUploadPictureIntent();
            }
        });
    }

    /**
     * call the map's resume funciton to start some service
     */
    @Override
    protected void onResume() {
        super.onResume();
        map.resume();
    }

    /**
     * request a permission for accessing the location
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        map.permissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * when map ready check the permission and init the map class
     * @param googleMap Check google map ui setting , then set google map settings attributes
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        boolean started = map.startLocationIntentService();
        if (!started)
            goBackToNewTrip("Need Location Permission.\nPlease add permission in settings.");
        else
            map.setStarted(true);
    }

    /**
     * destory all the service and listener when quit
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.stopLocationUpdates();
        map.setStarted(false);
        accelerometer.stopAccelerometer();
        temperature.stopTemperatureSensor();
        chronometer.stop();
    }

    public static Activity getActivity() {
        return getActivity();
    }

    /**
     * if deny the location permission
     * will go back to the main activity with a message
     * @param mess a message wants to pass to the main activity
     */
    private void goBackToNewTrip(String mess) {
        Intent intent = new Intent(MapActivity.this, MainActivity.class);
        intent.putExtra("message", mess);
        startActivity(intent);
        finish();
    }

    /**
     * get and set view retrieve by id
     */
    private void initView() {
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");

        barometerValue = findViewById(R.id.barometerValue);
        temperatureValue = findViewById(R.id.temperatureValue);

        Intent intent = getIntent();
        tripName = intent.getStringExtra("tripName");
        TextView textView = findViewById(R.id.mapTripName);
        textView.setText(tripName);
        // Temp image thumbnail
        imageThumbnail = findViewById(R.id.imageThumbnail);
    }

    /**
     * init two sensors
     */
    private void initSensor() {
        barometer = new Barometer(this);
        currentLocMarker = null;
        accelerometer = new Accelerometer(this, barometer);
        temperature = new Temperature(this);
    }

    /**
     * init the map viewModel
     * add marker to current location
     * add a polyline on map to show the route
     * when the livedata change
     */
    private void initMapViewModel() {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getLocAndSensorDataLiveData().observe(this, new Observer<LocAndSensorData>() {
            @Override
            public void onChanged(@NonNull final LocAndSensorData locAndSensorData) {
                if (locAndSensorData != null) {
                    if (currentLocMarker == null) {
                        currentLocMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude())));
                        currentLocMarker.setTitle("I'm here");
                        currentLocMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    } else {
                        currentLocMarker.setPosition(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude()));
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
                    if (locAndSensorData.getId() % 2 == 0)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude()), 16.0f));
                }

            }
        });
    }

    /**
     * init the photo viewModel
     */
    private void initPhotoViewModel() {
        photoViewModel = new PhotoViewModel(this);
    }

    /**
     * init the map object and google map
     */
    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        map = new MyMap(this, tripName, barometer, temperature, mapViewModel);
    }

    /**
     * start all the sensor
     */
    private void startAll() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        temperature.startTemperatureSensor();
        accelerometer.startAccelerometerRecording();
    }

    /**
     * Dispatch intent to take a photo , to launch hardware camera
     * hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create file
            File photoFile = null;

            try {
                photoFile = photoViewModel.createImageFile();
            } catch (IOException ex) {
                // Error when create file
            }
            // continue file exists
            if (photoFile != null) {
                // Put it out to provide it to other application just in case.
                Uri photoURI = FileProvider.getUriForFile(this,
                        "uk.ac.shef.oak.com6510.fileprovider",
                        photoFile);
                // If EXTRA_OUTPUT were input into intent the result of onActivity result of data will be null, but the image will be saved
                // Else the result will be thumbnail image from camera.
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("photoPath", photoFile.getAbsolutePath());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Dispatch intent to pick a photo from gallery
     * Require a permission to ACTION_PICK
     */
    private void dispatchUploadPictureIntent() {
        Intent uploadPictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Only allow local file
        uploadPictureIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(uploadPictureIntent, REQUEST_UPLOAD_PHOTO);
    }

    /**
     * Create on camera take picture result show picture then create a file / copy file to target path.
     * This also create a thumbnail of that picture using rescale from viewModel then save the picture info to database.
     * @param requestCode Intent of request code with REQUEST_TAKE_PHOTO and REQUEST_UPLOAD_PHOTO
     * @param resultCode Result of operation of intent (RESULT_OK = -1)
     * @param data Data intent from cavera activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String lastPhotoPath = null;
        File file = null;
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                // Cannot pass extra data into intent
                out.println(photoViewModel.getCurrentPhotoPath());
                lastPhotoPath = photoViewModel.getCurrentPhotoPath();
                if (resultCode == RESULT_OK && lastPhotoPath != null) {
                    file = new File(lastPhotoPath);
                }
                break;
            case REQUEST_UPLOAD_PHOTO:
                Uri uploadPhotoUri = null;
                if (data != null && data.getData() != null) {
                    uploadPhotoUri = data.getData();
                }
                if (resultCode == RESULT_OK && uploadPhotoUri != null && uploadPhotoUri.getPath() != null) {
                    // Get true path from uri
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (!checkPermissionForReadExternalStorage(MapActivity.this)) {
                            requestPermissionForReadExternalStorage(MapActivity.this);
                        }
                        // Upload file to application storage
                        File photoFile = null;

                        try {
                            photoFile = photoViewModel.createImageFile();
                        } catch (IOException ex) {
                            // Error when create file
                        }
                        if (photoFile != null) {
                            lastPhotoPath = photoFile.getAbsolutePath();
                            file = new File(lastPhotoPath);
                            // Try copy file
                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                BitmapFactory.decodeFile(cursor.getString(idx)).compress(Bitmap.CompressFormat.JPEG, 90, out);
                                cursor.close();
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        if (file != null && file.exists()) {
            final String intentPhotoPath = lastPhotoPath;
            photoViewModel.saveImageToDb(getApplication(), lastPhotoPath, mapViewModel.getLatestTripId(), mapViewModel.getStopId());
            Bitmap bitmapThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getAbsolutePath()), 90, 120);
            imageThumbnail.setImageBitmap(bitmapThumbnail);
            Marker m = googleMap.addMarker(new MarkerOptions().position(mapViewModel.getLatestLoc()));
            imageThumbnail.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FullImageActivity.class);
                    intent.putExtra("imagePath", intentPhotoPath);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * Check either camera exist or not
     * If camera do not exists remove take photo using camera
     *
     * @param context context of current activity
     * @param button  button to hide and set enable option
     * @return Boolean for camera exist
     */
    private boolean checkCameraExist(Context context, FloatingActionButton button) {
        // There seem to be a bug for old android version where device return true for feature camera thus a need of numberOfcamera checking
        int cameraNumber = Camera.getNumberOfCameras();
        PackageManager packageManager = context.getPackageManager();
        boolean cameraExist = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        if (cameraExist && cameraNumber >= 1) {
            return true;

        } else {
            button.setEnabled(false);
            button.hide();
            return false;
        }
    }

    /**
     * Check the permission and android version to check that application need to request permission or not.
     *
     * @param context Context of this activity
     * @return boolean of authority for permission for externalStorage
     */
    public boolean checkPermissionForReadExternalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    /**
     * Request the permission at runTime for read external storage (This was needed for android > 6.0)
     *
     * @param context Context of this activity
     */
    public void requestPermissionForReadExternalStorage(Context context) {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

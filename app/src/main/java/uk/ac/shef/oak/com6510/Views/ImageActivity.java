package uk.ac.shef.oak.com6510.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.shef.oak.com6510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.ImageViewAdapter;
import uk.ac.shef.oak.com6510.ViewModels.PhotoViewModel;

/**
 * Activity to show the path info and image detail info
 */
public class ImageActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private ImageViewAdapter myImageViewAdapter;
    private int stopId;
    private String imagePath;

    /**
     * onCreate funciton for the activity
     * get tripid and stopId from last activity
     * stopId == -1 will load the path view, otherwise to load the image view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int tripId = intent.getIntExtra("tripId", -1);
        stopId = intent.getIntExtra("tripStopId", -1);
        imagePath = intent.getStringExtra("imagePath");

        if(stopId == -1){
            setContentView(R.layout.activity_path_detail);
        }else{
            setContentView(R.layout.activity_photo_detail);
        }

        myImageViewAdapter = new ImageViewAdapter(getApplication(), this, tripId);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment2);
        mapFragment.getMapAsync(this);
    }

    /**
     * when map is finished loading ask to imageApter to retrieve data for view and set the view
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        ArrayList<LatLng> latLngs = myImageViewAdapter.getAllPointsLoc();

        if (latLngs.size() > 0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 15.0f));
        googleMap.addPolyline(new PolylineOptions().addAll(latLngs).width(10).color(Color.RED));

        TextView tripName = (TextView) findViewById(R.id.tripName);
        TextView baro = (TextView) findViewById(R.id.barometerValue1);
        TextView temp = (TextView) findViewById(R.id.temperatureValue1);
        ImageView photo = (ImageView) findViewById(R.id.detail_photo);

        tripName.setText("Name: " + myImageViewAdapter.getTripName());
        if (stopId != -1) {
            LocAndSensorData l = myImageViewAdapter.getInfoById(stopId);
            baro.setText("Barometer: " + l.getPreasureValue());
            temp.setText("Temperature: " + l.getTemperatureValue());
            Bitmap photoBitmap = PhotoViewModel.getDecodedScaleImage(getApplication(), photo, imagePath);
            if (photoBitmap != null) {
                photo.setImageBitmap(photoBitmap);
                photo.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), FullImageActivity.class);
                        intent.putExtra("imagePath", imagePath);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            googleMap.addMarker(new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude())));
        }
    }

}

package uk.ac.shef.oak.com6510.Views;

import android.content.Intent;
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

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.shef.oak.com6510.Databases.LocAndSensorData;
import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.ImageViewAdapter;
import uk.ac.shef.oak.com6510.ViewModels.PhotoViewModel;

public class ImageActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private ImageViewAdapter myImageViewAdapter;
    private int stopId;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Intent intent = getIntent();
        int tripId = intent.getIntExtra("tripId", -1);
        myImageViewAdapter = new ImageViewAdapter(getApplication(), this, tripId);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment2);
        mapFragment.getMapAsync(this);

        stopId = intent.getIntExtra("tripStopId", -1);
        imagePath = intent.getStringExtra("imagePath");
    }

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
        if (stopId == -1) {
            baro.setText("");
            temp.setText("");
        } else {
            LocAndSensorData l = myImageViewAdapter.getInfoById(stopId);
            baro.setText("Barometer: " + l.getPreasureValue());
            temp.setText("Tempterature: " + l.getTemperatureValue());
            photo.setImageBitmap(PhotoViewModel.getDecodedScaleImage(getApplication(), photo, imagePath));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude())));
        }
    }

}

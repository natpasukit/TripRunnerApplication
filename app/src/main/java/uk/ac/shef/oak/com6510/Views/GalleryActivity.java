package uk.ac.shef.oak.com6510.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com6510.Models.PhotoModel;
import uk.ac.shef.oak.com6510.R;

public class GalleryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        // Recycle view creation
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);

        // Linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter for recycler view
        String[] strings = {"trip1", "trip2", "trip3"};

        RecyclerView.Adapter adapter = new TripGalleryAdapter(strings);
        recyclerView.setAdapter(adapter);
    }


}
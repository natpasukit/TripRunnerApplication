package uk.ac.shef.oak.com6510.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com6510.Models.PhotoModel;
import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.GridGalleryAdapter;
import uk.ac.shef.oak.com6510.ViewModels.TripGalleryAdapter;

public class GalleryActivity extends AppCompatActivity {

    private static final int GRID_COLUMN_NUMBER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

//        // Recycle view creation
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);
//
//        // Linear layout manager
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        // Set adapter for recycler view
//        RecyclerView.Adapter adapter = new TripGalleryAdapter(this.getApplication(),this);
//        recyclerView.setAdapter(adapter);


        // Recycle view creation
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);

        // Linear layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, GRID_COLUMN_NUMBER);
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter for recycler view
        RecyclerView.Adapter adapter = new GridGalleryAdapter(this.getApplication(), this);
        recyclerView.setAdapter(adapter);

    }


}
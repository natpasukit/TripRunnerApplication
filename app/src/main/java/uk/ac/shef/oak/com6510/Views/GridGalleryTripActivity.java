package uk.ac.shef.oak.com6510.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.GridGalleryAdapter;
import uk.ac.shef.oak.com6510.ViewModels.GridTripGalleryAdapter;

public class GridGalleryTripActivity extends AppCompatActivity {

    private static final int GRID_COLUMN_NUMBER = 3;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Intent intent = getIntent();
        this.tripId = intent.getIntExtra("tripId", -1);
        if (this.tripId > 0) {
            // Recycle view creation
            recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);

            // Linear layout manager
            layoutManager = new GridLayoutManager(this.getApplication(), GRID_COLUMN_NUMBER);
            recyclerView.setLayoutManager(layoutManager);

            // Set adapter for recycler view
            RecyclerView.Adapter adapter = new GridTripGalleryAdapter(getApplication(), this, this.tripId);
            recyclerView.setAdapter(adapter);
        }


    }
}